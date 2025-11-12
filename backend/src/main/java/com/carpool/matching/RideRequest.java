package com.carpool.matching;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point; // JTS Point


// ✨ Lombok 적용: @Getter, @Setter, @NoArgsConstructor 추가
// (수동 Getter/Setter/생성자 모두 삭제)

@Getter
@Setter
@NoArgsConstructor // JPA 기본 생성자
@Entity
public class RideRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // 요청한 사용자의 ID

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point startPoint;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point endPoint;

    @Column(nullable = false)
    private String startAddress; // 표시용 주소

    @Column(nullable = false)
    private String endAddress; // 표시용 주소


    @Enumerated(EnumType.STRING) // Enum을 문자열("WAITING")로 저장
    @Column(nullable = false)
    private RideStatus status = RideStatus.WAITING;

}