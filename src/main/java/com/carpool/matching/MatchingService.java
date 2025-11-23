package com.carpool.matching;

import com.carpool.global.util.GeometryService;
import com.carpool.matching.dto.MatchNotificationDto;
import com.carpool.matching.dto.RideRequestDto;
import java.util.Optional;
import org.locationtech.jts.geom.Point;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

// ✨ Lombok 적용: @RequiredArgsConstructor 추가
@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final RideRequestRepository rideRequestRepository;
    private final GeometryService geometryService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 카풀 요청 처리 및 매칭 시도
     * @param requestDto (Client -> Server)
     */
    public void handleRideRequest(RideRequestDto requestDto) {
        // 0. [방어 로직] 이미 대기 중인 요청이 있는지 확인
        Optional<RideRequest> existingRequest = rideRequestRepository.findByUsernameAndStatus(requestDto.getUsername(), RideStatus.WAITING);
        if (existingRequest.isPresent()) {
            System.out.println("이미 대기 중인 요청이 있습니다: " + requestDto.getUsername());
            // (임시) 기존 요청을 덮어쓰기 위해 삭제 (실제로는 에러 메시지 전송)
            rideRequestRepository.delete(existingRequest.get());
        }

        // 1. DTO를 Point 객체로 변환
        Point startPoint = geometryService.createPoint(requestDto.getStartLng(), requestDto.getStartLat());
        Point endPoint = geometryService.createPoint(requestDto.getEndLng(), requestDto.getEndLat());

        // 2. [핵심] 100m 반경 매칭 시도
        Optional<RideRequest> match = rideRequestRepository.findMatchingRide(
                requestDto.getUsername(),
                startPoint,
                endPoint
        );

        // 3. 매칭 결과 처리
        if (match.isPresent()) {
            // --- 3.1. 매칭 성공 ---
            RideRequest user1_Request = match.get(); // 대기자 (User 1)
            String user1_Username = user1_Request.getUsername();
            String user2_Username = requestDto.getUsername(); // 요청자 (User 2)

            user1_Request.setStatus(RideStatus.MATCHED);
            rideRequestRepository.save(user1_Request);

            // ✨ User 2 (요청자)의 Ride도 MATCHED 상태로 DB에 저장
            RideRequest user2_Ride = createRideRequestFromDto(requestDto, startPoint, endPoint);
            user2_Ride.setStatus(RideStatus.MATCHED);
            rideRequestRepository.save(user2_Ride);

            System.out.println("매칭 성공! " + user1_Username + " <-> " + user2_Username);

            // [WebSocket 전송] - ✨✨ 로직 수정 (역할 반전) ✨✨

            // (1) User 1 (대기자)에게 "GOTO" 알림
            // "매칭이 되었습니다. (User 2) 출발지로 이동해주세요."
            MatchNotificationDto notifToUser1 = MatchNotificationDto.createGoToNotification(
                    user2_Username,               // 상대방 (User 2) 닉네임
                    requestDto.getStartAddress(), // 이동할 주소 (User 2의 출발지)
                    requestDto.getStartLat(),     // 이동할 위도 (User 2)
                    requestDto.getStartLng()      // 이동할 경도 (User 2)
            );
            messagingTemplate.convertAndSend("/topic/match/" + user1_Username, notifToUser1);

            // (2) User 2 (요청자)에게 "WAIT" 알림
            // "(User 1)과 매칭되었습니다. 상대방이 출발지로 오고 있습니다."
            MatchNotificationDto notifToUser2 = MatchNotificationDto.createWaitNotification(
                    user1_Username,                 // 상대방 (User 1) 닉네임
                    user1_Request.getStartAddress(),// 상대방 주소
                    user1_Request.getEndAddress()   // 상대방 목적지
            );
            messagingTemplate.convertAndSend("/topic/match/" + user2_Username, notifToUser2);

        } else {
            // --- 3.2. 매칭 실패 (대기열 등록) ---
            RideRequest newRequest = createRideRequestFromDto(requestDto, startPoint, endPoint); // ✨ DTO -> Entity 변환
            newRequest.setStatus(RideStatus.WAITING);

            rideRequestRepository.save(newRequest);

            System.out.println("매칭 실패, 대기열 등록: " + requestDto.getUsername());
        }
    }

    /**
     * DTO를 RideRequest 엔티티로 변환하는 헬퍼 메소드
     */
    private RideRequest createRideRequestFromDto(RideRequestDto dto, Point start, Point end) {
        RideRequest ride = new RideRequest();
        ride.setUsername(dto.getUsername());
        ride.setStartPoint(start);
        ride.setEndPoint(end);
        ride.setStartAddress(dto.getStartAddress());
        ride.setEndAddress(dto.getEndAddress());
        return ride;
    }

    /**
     * 카풀 요청 취소
     * @param username
     */
    public void cancelRideRequest(String username) {
        rideRequestRepository.findByUsernameAndStatus(username, RideStatus.WAITING)
                .ifPresent(request -> {
                    rideRequestRepository.delete(request);
                    System.out.println("요청 취소됨: " + username);
                });
    }
}