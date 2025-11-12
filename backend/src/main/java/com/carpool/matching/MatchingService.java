package com.carpool.matching;

import com.carpool.global.util.GeometryService;
import com.carpool.matching.dto.MatchNotificationDto;
import com.carpool.matching.dto.RideRequestDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// ✨ Lombok 적용: @RequiredArgsConstructor 추가
// (수동 생성자 삭제)

@Service
@Transactional
@RequiredArgsConstructor
public class MatchingService {

    private final RideRequestRepository rideRequestRepository;
    private final GeometryService geometryService;
    private final SimpMessagingTemplate messagingTemplate;


    public void handleRideRequest(RideRequestDto requestDto) {
        Optional<RideRequest> existingRequest = rideRequestRepository.findByUsernameAndStatus(requestDto.getUsername(), RideStatus.WAITING);
        if (existingRequest.isPresent()) {
            System.out.println("이미 대기 중인 요청이 있습니다: " + requestDto.getUsername());
            rideRequestRepository.delete(existingRequest.get());
        }

        Point startPoint = geometryService.createPoint(requestDto.getStartLng(), requestDto.getStartLat());
        Point endPoint = geometryService.createPoint(requestDto.getEndLng(), requestDto.getEndLat());

        Optional<RideRequest> match = rideRequestRepository.findMatchingRide(
            requestDto.getUsername(), 
            startPoint, 
            endPoint
        );

        if (match.isPresent()) {
            // --- 3.1. 매칭 성공 ---
            RideRequest user1_Request = match.get(); // 대기자 (User 1)
            String user1_Username = user1_Request.getUsername();
            String user2_Username = requestDto.getUsername(); // 요청자 (User 2)

            user1_Request.setStatus(RideStatus.MATCHED);
            rideRequestRepository.save(user1_Request);
            
            System.out.println("매칭 성공! " + user1_Username + " <-> " + user2_Username);

            // [WebSocket 전송]
            // User 1 (대기자)에게 User 2의 정보를 보냄 (WAIT 알림)
            MatchNotificationDto notifToUser1 = MatchNotificationDto.createWaitNotification(
                user2_Username, 
                requestDto.getStartAddress(), 
                requestDto.getEndAddress()    
            );
            messagingTemplate.convertAndSend("/topic/match/" + user1_Username, notifToUser1);

            // User 2 (요청자)에게 User 1의 정보를 보냄 (GOTO 알림)
            MatchNotificationDto notifToUser2 = MatchNotificationDto.createGoToNotification(
                user1_Username, 
                user1_Request.getStartAddress(),  
                user1_Request.getStartPoint().getY(), 
                user1_Request.getStartPoint().getX()  
            );
            messagingTemplate.convertAndSend("/topic/match/" + user2_Username, notifToUser2);

        } else {
            // --- 3.2. 매칭 실패 (대기열 등록) ---
            RideRequest newRequest = new RideRequest();
            newRequest.setUsername(requestDto.getUsername());
            newRequest.setStartPoint(startPoint);
            newRequest.setEndPoint(endPoint);
            newRequest.setStartAddress(requestDto.getStartAddress());
            newRequest.setEndAddress(requestDto.getEndAddress());
            newRequest.setStatus(RideStatus.WAITING);
            
            rideRequestRepository.save(newRequest); 
            
            System.out.println("매칭 실패, 대기열 등록: " + requestDto.getUsername());
        }
    }

    public void cancelRideRequest(String username) {
        rideRequestRepository.findByUsernameAndStatus(username, RideStatus.WAITING)
            .ifPresent(request -> {
                rideRequestRepository.delete(request);
                System.out.println("요청 취소됨: " + username);
            });
    }
}
