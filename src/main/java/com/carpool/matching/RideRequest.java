package com.carpool.matching;

import com.carpool.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 정보 (User 엔티티 참조)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // ✨ 매칭 요청을 한 유저 이름
    @Column(nullable = false)
    private String username;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point startPoint;

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    private Point endPoint;

    @Column(nullable = false)
    private String startAddress;

    @Column(nullable = false)
    private String endAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RideStatus status = RideStatus.WAITING;
}
