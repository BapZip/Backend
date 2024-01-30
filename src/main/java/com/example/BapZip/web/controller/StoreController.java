package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.MypageService.MypageService;
import com.example.BapZip.service.StoreService.StoreService;
import com.example.BapZip.web.dto.MypageResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "가게 정보 조회 API", description = "가게 정보 조회 API입니다.")
    @PostMapping("/{storeId}/detailinfo/")
    public ApiResponse<StoreResponseDTO.StoreInfoDTO> getStoreDetailInfo(@PathVariable("storeId") Long storeId) {
        return ApiResponse.onSuccess(storeService.getStoreDetailInfo(storeId));
    }

    @Operation(summary = "가게 정보 내 메뉴판 사진 조회 API", description = ".")
    @PostMapping("/{storeId}/printedMenu")
    public ApiResponse<List<StoreResponseDTO.PrintedMenuDTO>> getStorePrintedMenu(@PathVariable("storeId") Long storeId) {
        return ApiResponse.onSuccess(storeService.getStorePrintedMenu(storeId));
    }


}
