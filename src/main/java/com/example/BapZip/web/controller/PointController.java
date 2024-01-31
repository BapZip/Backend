package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.PointService.PointService;
import com.example.BapZip.web.dto.PointResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/point")
public class PointController {

    private final PointService pointService;

    @Operation(summary = "포인트 내역 조회 API", description = "포인트 내역 조회 API입니다.")
    @GetMapping("/myPoint")
    public ApiResponse<List<PointResponseDTO.PointHistoryDTO>> PointHistory(@AuthenticationPrincipal String userId){

        List<PointResponseDTO.PointHistoryDTO> result = pointService.getPointHistory(Long.valueOf(userId));
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "전체 포인트 조회 API", description = "전체 포인트 조회 API입니다.")
    @GetMapping("/myTotalPoint")
    public ApiResponse<Integer> AllPoints(@AuthenticationPrincipal String userId){
        int result = pointService.getAllPoints(Long.valueOf(userId));

        return ApiResponse.onSuccess(result);
    }


}
