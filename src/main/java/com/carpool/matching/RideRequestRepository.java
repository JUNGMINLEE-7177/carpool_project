package com.carpool.matching;

import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

    // 현재 사용자의 활성(WAITING) 요청이 있는지 확인
    Optional<RideRequest> findByUsernameAndStatus(String username, RideStatus status);

    // [핵심 로직] MySQL 공간 쿼리 (Native Query)
    @Query(value =
            "SELECT * FROM ride_request r " +
                    "WHERE r.status = 'WAITING' " +
                    "AND r.username != :username " + // 1. 본인이 아니고
                    "AND ST_Distance_Sphere(r.start_point, :startPoint) <= 100 " + // 2. 출발지가 100m 이내
                    "AND ST_Distance_Sphere(r.end_point, :endPoint) <= 100 " + // 3. 목적지가 100m 이내
                    "LIMIT 1", // 4. 가장 먼저 찾은 1명
            nativeQuery = true)
    Optional<RideRequest> findMatchingRide(
            @Param("username") String username,
            @Param("startPoint") Point startPoint,
            @Param("endPoint") Point endPoint
    );
}

