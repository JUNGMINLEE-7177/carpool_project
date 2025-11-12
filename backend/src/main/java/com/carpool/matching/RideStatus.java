package com.carpool.matching;

// 카풀 요청의 상태
public enum RideStatus {
    WAITING,  // 매칭 대기 중
    MATCHED,  // 매칭 성공
    CANCELED  // 매칭 취소
}