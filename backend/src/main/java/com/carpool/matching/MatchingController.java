package com.carpool.matching;

import com.carpool.matching.dto.CancelRequestDto;
import com.carpool.matching.dto.RideRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

// ✨ Lombok 적용: @RequiredArgsConstructor 추가
// (수동 생성자 삭제)

@Controller
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @MessageMapping("/match/request")
    public void handleRideRequest(@Payload RideRequestDto requestDto) {
        matchingService.handleRideRequest(requestDto);
    }

    @MessageMapping("/match/cancel")
    public void handleCancelRequest(@Payload CancelRequestDto requestDto) {
        matchingService.cancelRideRequest(requestDto.getUsername());
    }
}
