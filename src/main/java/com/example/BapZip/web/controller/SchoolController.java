package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.SchoolService.SchoolService;
import com.example.BapZip.web.dto.SchoolResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/school")
public class SchoolController {
    private final SchoolService schoolService;
    @Operation(summary = "학교 검색 api", description = "회원 가입시 사용하는 학교 찾기 api 입니다.")
    @GetMapping("/search")
    public ApiResponse<List<SchoolResponseDTO.SearchSchool>> searchSchool(@RequestParam(name = "schoolName") String schoolName)
    {
        List<SchoolResponseDTO.SearchSchool> result = schoolService.searchSchool(schoolName);
        return ApiResponse.onSuccess(result);
    }

    // 지역 api
    @Operation(summary = "지역 api", description = "등록된 모든 지역이름과 id 값을 반환하는 api입니다.")
    @GetMapping("/region")
    public ApiResponse<List<SchoolResponseDTO.getSchoolRegion>> getSchoolRegion()
    {
        List<SchoolResponseDTO.getSchoolRegion> result = schoolService.getSchoolRegion();
        return ApiResponse.onSuccess(result);
    }


    // 학교 리스트 선택 -> 시.도 선택하면 : 학교명 , id 리턴
    @Operation(summary = "학교 리스트 api", description = "메인페이지에서 사용하는 학교 리스트 api 입니다. 지역 id 값을 파라미터로 넣어주세요.")
    @GetMapping
    public ApiResponse<List<SchoolResponseDTO.SearchSchool>> getSchoolList(@RequestParam(name = "regionId") Long regionId)
    {
        List<SchoolResponseDTO.SearchSchool> result = schoolService.getSchoolList(regionId);
        return ApiResponse.onSuccess(result);
    }

    // 학과 리스트 -> 학교 id 값으로 학과 검색
    @Operation(summary = "학과 찾기 api", description = "회원 가입에 사용되는 학과 찾기 api 입니다. 학교 id 값을 파라미터에 넣어주세요.")
    @GetMapping("/major")
    public ApiResponse<List<SchoolResponseDTO.getSchoolMajor>> getMajorList
    (@RequestParam(name = "schoolId") Long schoolId , @RequestParam(name = "majorName") String majorName)
    {
        List<SchoolResponseDTO.getSchoolMajor> result = schoolService.getSchoolMajor(schoolId,majorName);
        return ApiResponse.onSuccess(result);
    }

    // 학교 가져오기
    @Operation(summary = "학교 이믈&로고 api", description = "학교 로고와 이름을 반환하는 api입니다. id를 입력해주세요.")
    @GetMapping("/logo")
    public ApiResponse<SchoolResponseDTO.getSchoolLogo> getSchoolLogo(@RequestParam(name = "schoolId") Long schoolId)
    {
        SchoolResponseDTO.getSchoolLogo result = schoolService.getSchoolLogo(schoolId);
        return ApiResponse.<SchoolResponseDTO.getSchoolLogo>onSuccess(result);
    }



}
