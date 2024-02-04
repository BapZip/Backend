package com.example.BapZip.service.MypageService;

import com.example.BapZip.web.dto.MypageResponseDTO;

public interface MypageService {

    MypageResponseDTO.MypageInfoDTO getMypageInfo(Long userId);
}
