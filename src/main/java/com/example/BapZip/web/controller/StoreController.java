package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.StoreService.StoreService;
import com.example.BapZip.web.dto.CongestionResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.BapZip.domain.Store;
import com.example.BapZip.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.MypageService.MypageService;
import com.example.BapZip.service.StoreService.StoreService;
import com.example.BapZip.web.dto.MypageResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @Operation(summary = "가게 상세 정보 조회 API", description = "가게 정보 조회 API입니다,PathVariable 스토어 ID.")
    @GetMapping("/{storeId}/detailinfo/")
    public ApiResponse<StoreResponseDTO.StoreDetailInfoDTO> getStoreDetailInfo(@PathVariable("storeId") Long storeId) {
        return ApiResponse.onSuccess(storeService.getStoreDetailInfo(storeId));
    }

    @Operation(summary = "가게 정보 내 메뉴판 사진 조회 API", description = "스토어 ID 넣어주세요,PathVariable")
    @GetMapping("/{storeId}/printedMenu")
    public ApiResponse<List<StoreResponseDTO.PrintedMenuDTO>> getStorePrintedMenu(@PathVariable("storeId") Long storeId) {
        return ApiResponse.onSuccess(storeService.getStorePrintedMenu(storeId));
    }

    @Operation(summary = "내가 좋아한(찜한) 가게 API", description = "토큰만 필요함")
    @GetMapping("/myZip")
    public ApiResponse<List<StoreResponseDTO.MyZipDTO>> getStoreMyZip(Principal principal) {
        return ApiResponse.onSuccess(storeService.getStoreMyZip(Long.parseLong(principal.getName())));
    }

    @Operation(summary = "핫플레이스 조회", description = "토큰만 필요함")
    @GetMapping("/hotPlace")
    public ApiResponse<List<StoreResponseDTO.HotPlaceDTO>> getHotPlace() {
        return ApiResponse.onSuccess(storeService.getHotPlace());
    }


}
