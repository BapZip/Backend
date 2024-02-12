package com.example.BapZip.web.controller;


import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.MypageService.MypageService;

import com.example.BapZip.web.dto.MypageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MypageController {
    private final MypageService mypageService;

    @Operation(summary = "마이페이지 내 정보 조회 API", description = "마이페이지 내 정보 조회 API입니다.토큰만 넣어주세요")
    @GetMapping("/info")
    public ApiResponse<MypageResponseDTO.MypageInfoDTO> getMypageInfo(Principal principal) {
        return ApiResponse.onSuccess(mypageService.getMypageInfo(Long.parseLong(principal.getName())));
    }

    @Operation(summary = "마이페이지 이미지 변경 API", description = "")
    @PostMapping(value = "/api/img/snapshot")
    public ApiResponse<MypageResponseDTO.MypageInfoDTO> fetchMypageProfile(Principal principal,@RequestPart(value = "images",required = true) List<MultipartFile> images) {
        return ApiResponse.onSuccess(mypageService.fetchMypageProfile(Long.parseLong(principal.getName()),images));
    }
}
