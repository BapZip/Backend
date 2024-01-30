package com.example.BapZip.service.StoreService;

import com.example.BapZip.web.dto.StoreResponseDTO;

public interface StoreService {
    StoreResponseDTO.StoreInfoDTO getStoreInfo(String userId, Long storeId);
}
