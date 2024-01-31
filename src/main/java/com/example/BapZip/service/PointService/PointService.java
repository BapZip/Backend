package com.example.BapZip.service.PointService;

import com.example.BapZip.web.dto.PointResponseDTO;

import java.util.List;

public interface PointService {
    public List<PointResponseDTO.PointHistoryDTO> getPointHistory(Long userId);

    public int getAllPoints(Long userId);
}
