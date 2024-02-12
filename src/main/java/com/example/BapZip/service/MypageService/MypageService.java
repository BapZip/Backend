package com.example.BapZip.service.MypageService;

import com.example.BapZip.web.dto.MypageResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MypageService {

    MypageResponseDTO.MypageInfoDTO getMypageInfo(Long userId);

    MypageResponseDTO.MypageInfoDTO fetchMypageProfile(Long userId, List<MultipartFile> image);
}
