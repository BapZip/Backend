package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.apiPayload.code.status.SuccessStatus;
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
    public ApiResponse<List<StoreResponseDTO.HotPlaceDTO>> getHotPlace(@RequestParam Long schoolId) {
        return ApiResponse.onSuccess(storeService.getHotPlace(schoolId));
    }
    @Operation(summary = "가게리스트 조회(리뷰많은순)", description = "토큰만 필요함 In/out 공용")
    @GetMapping("/list/reviewcount")
    public ApiResponse<List<StoreResponseDTO.StoreListReviewCountDTO>> getStoreListByReviewCount(Principal principal,@RequestParam Long schoolId) {
        return ApiResponse.onSuccess(storeService.getStoreListByReviewCount(Long.parseLong(principal.getName()),schoolId));
    }
    @Operation(summary = "가게리스트 조회(별점순)", description = "토큰만 필요함 In/out 공용")
    @GetMapping("/list/score")
    public ApiResponse<List<StoreResponseDTO.StoreListScoreDTO>> getStoreListByScore(Principal principal,Long schoolId) {
        return ApiResponse.onSuccess(storeService.getStoreListByScore(Long.parseLong(principal.getName()),schoolId));
    }


    @Operation(summary = "오늘의 공지", description = "스토어 id 넣어주세요. PathVariable storeId")
    @GetMapping("/{storeId}/notice")
    public ApiResponse<StoreResponseDTO.NoticeDTO> getNotice(@PathVariable("storeId") Long storeId) {
        return ApiResponse.onSuccess(storeService.getNotice(storeId));
    }

    @Operation(summary = "가게 zip 하기", description = "스토어 id 넣어주세요. PathVariable storeId")
    @PostMapping("/zip")
    public ApiResponse zipStore(@AuthenticationPrincipal String userId,@RequestParam("storeId") Long storeId) {
        storeService.zipStore(userId,storeId);
        return ApiResponse.of(SuccessStatus.STORE_ZIP_SUCCESS,null);
    }

    @Operation(summary = "가게 zip 해제 하기", description = "스토어 id 넣어주세요. PathVariable storeId")
    @DeleteMapping("/deleteZip")
    public ApiResponse unzipStore(@AuthenticationPrincipal String userId,@RequestParam("storeId") Long storeId) {
        storeService.unzipStore(userId,storeId);
        return ApiResponse.of(SuccessStatus.STORE_UNZIP_SUCCESS,null);
    }

    @Operation(summary = "가게 검색", description = "스토어 이름을 넣어주세요. RequestParam name")
    @GetMapping("/search")
    public ApiResponse<List<StoreResponseDTO.searchStore>> searchStore (@RequestParam("name") String name) {
        List<StoreResponseDTO.searchStore> result = storeService.searchStore(name);
        return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "업종별 추천 식당 조회", description = "PathVariable 업종 -> KOREA, CHINA, WESTERN, CAFE, JAPAN")
    @GetMapping("/recommend/{categoryName}")
    public ApiResponse<StoreResponseDTO.RecommandDTO> getRecommendStoresByLikes(@AuthenticationPrincipal String userId,@PathVariable("categoryName") String categoryName,Long schoolId) {
        return ApiResponse.onSuccess(storeService.getRecommendStoresByLikes(Long.valueOf(userId),categoryName,schoolId));
    }

    @Operation(summary = "식당 메뉴 조회 API", description = "store Id 입력해주세요.")
    @GetMapping("/stores/{storeId}/menu")
    public ApiResponse<List<List<StoreResponseDTO.menuDTO>>> menuList(@PathVariable("storeId") Long storeId){
        return ApiResponse.onSuccess(storeService.getMenuList(storeId));
    }
}
