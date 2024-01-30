package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.StoreService.StoreService;
import com.example.BapZip.web.dto.CongestionResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;
    @Operation(summary = "가게 기본 정보 API ", description = "가게 기본 정보 API 입니다. 가게 id를 pathvariable로 전달해주세요.")
    @GetMapping("/{storeId}/info")
    public ApiResponse<StoreResponseDTO.StoreInfoDTO> getCongestionRanking
            (@AuthenticationPrincipal String userId,@PathVariable(name = "storeId") Long storeId)
    {
        StoreResponseDTO.StoreInfoDTO result = storeService.getStoreInfo(userId,storeId);
        return ApiResponse.onSuccess(result);
    }
}
