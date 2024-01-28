package com.example.BapZip.service.CongestionService;

import com.example.BapZip.web.dto.CongestionRequestDTO;
import com.example.BapZip.web.dto.CongestionResponseDTO;

import java.util.List;

public interface CongestionService {
    CongestionResponseDTO.registerCongestion registerCongestion(CongestionRequestDTO.registerCongestion congestion, Long storeId, String userId);

    List<CongestionResponseDTO.getCongestionRanking> getRanking(String userId, String classification, Long schoolId);
}
