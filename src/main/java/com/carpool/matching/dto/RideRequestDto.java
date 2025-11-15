package com.carpool.matching.dto;

import lombok.Data;

// ✨ Lombok 적용: @Data 추가
// (수동 Getter/Setter/생성자 모두 삭제)

// (Client -> Server) 카풀 요청 시 프론트가 보내는 정보
@Data
public class RideRequestDto {
    private String username;
    private double startLat;
    private double startLng;
    private String startAddress;
    private double endLat;
    private double endLng;
    private String endAddress;
}