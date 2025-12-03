package com.carpool.global.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class GeometryService {

    // SRID 4326 (WGS 84) - 위도/경도용 표준
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    /**
     * 위도(latitude), 경도(longitude)로 Point 객체를 생성합니다.
     * [중요] JTS의 Point는 (x, y) 즉, (경도, 위도) 순서로 생성해야 합니다.
     * * @param longitude 경도 (lng)
     * @param latitude  위도 (lat)
     * @return SRID 4326이 적용된 Point 객체
     */
    public Point createPoint(double longitude, double latitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
