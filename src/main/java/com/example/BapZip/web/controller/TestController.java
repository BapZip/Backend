package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.domain.User;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.service.S3Service.AmazonS3Service;
import com.example.BapZip.web.dto.TestResponseDTO;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final UserRepository userRepository; // 실제로는 컨트롤러에서 레포지토리 사용 안함.

    private final AmazonS3Service s3Service;

    @Operation(summary = "test API", description = "추후에 삭제 예정")
    @PostMapping
    public TestResponseDTO.testDTO test(@AuthenticationPrincipal String userId, // 토큰에서 id 추출 (여기서 id는 babbab 아님 , 정수 값인 id 값!)
                                       @RequestPart(value = "images", required = false) List<MultipartFile> images)
    {
        // userId 사용 방법 -> string 값을 long으로 바꿔줘야함.
        User user = userRepository.findById(Long.valueOf(userId)).get(); //실제로는 service에서 수행

        // s3 Service 사용 방법
        List<String> urls = s3Service.uploadFiles("test",images);

        TestResponseDTO.testDTO result =
                TestResponseDTO.testDTO.builder().nickname(user.getNickname()).userId(Long.valueOf(userId)).urls(urls).build();
        return result;

    }

    //@PostMapping("/{storeId}/reviews")
//    @ExistStore  @PathVariable(name = "storeId") Long storeId,
//    @ExistMember @RequestParam(name = "memberId") Long memberId


}
