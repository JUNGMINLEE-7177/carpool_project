package com.carpool.matching.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// ✨ Lombok 적용: @Getter, @Setter, @NoArgsConstructor 추가
// (수동 Getter/Setter/생성자 모두 삭제)

// (Server -> Client) 매칭 성공 시 서버가 클라이언트에게 보내는 정보
@Getter
@Setter
@NoArgsConstructor
public class MatchNotificationDto {

    private String type; // "WAIT" (대기자) 또는 "GOTO" (이동자)
    private String opponentUsername;
    private String opponentStartAddress;
    private String opponentEndAddress;

    // "GOTO" 타입일 때만 사용
    private Double targetLat; // 이동해야 할 목표 위도 (User 1의 출발지)
    private Double targetLng; // 이동해야 할 목표 경도 (User 1의 출발지)


    // --- Static Factory Methods (Lombok과 별개로 유지) ---

    public static MatchNotificationDto createWaitNotification(String opponentUsername, String opponentStartAddress, String opponentEndAddress) {
        MatchNotificationDto dto = new MatchNotificationDto();
        dto.setType("WAIT");
        dto.setOpponentUsername(opponentUsername);
        dto.setOpponentStartAddress(opponentStartAddress);
        dto.setOpponentEndAddress(opponentEndAddress);
        return dto;
    }

    public static MatchNotificationDto createGoToNotification(String opponentUsername, String targetAddress, Double targetLat, Double targetLng) {
        MatchNotificationDto dto = new MatchNotificationDto();
        dto.setType("GOTO");
        dto.setOpponentUsername(opponentUsername);
        dto.setOpponentStartAddress(targetAddress); // User 1의 출발지 주소
        dto.setTargetLat(targetLat);
        dto.setTargetLng(targetLng);
        return dto;
    }
}