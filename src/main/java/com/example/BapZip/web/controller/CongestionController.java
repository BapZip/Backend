package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.CongestionService.CongestionService;
import com.example.BapZip.service.UserService.UserService;
import com.example.BapZip.web.dto.CongestionRequestDTO;
import com.example.BapZip.web.dto.CongestionResponseDTO;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/congestion")
public class CongestionController {
    private  final CongestionService congestionService;

    @Operation(summary = "혼잡도 등록 api", description = "혼잡도 등록 api 입니다.")
    @PostMapping("/congestion/{storeId}")
    public ApiResponse<CongestionResponseDTO.registerCongestion> registerCongestion
            (@AuthenticationPrincipal String userId,
             @RequestBody CongestionRequestDTO.registerCongestion congestion, @PathVariable(name = "storeId") Long storeId)
    {
        CongestionResponseDTO.registerCongestion result= congestionService.registerCongestion(congestion,storeId,userId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "혼잡도 랭킹 api", description = "혼잡도 랭킹 api 입니다.")
    @GetMapping("/congestion/ranking")
    // 학교 id , in out 전체 , 사용자
    public ApiResponse<List<CongestionResponseDTO.getCongestionRanking>> getCongestionRanking
            (@AuthenticationPrincipal String userId,@RequestParam String classification,@RequestParam Long schoolId)
    {
        List<CongestionResponseDTO.getCongestionRanking> result = congestionService.getRanking(userId,classification,schoolId);
        return ApiResponse.onSuccess(result);
    }


}
