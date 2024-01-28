package com.example.BapZip.service.StoreService;

import com.example.BapZip.web.dto.StoreResponseDTO;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;

public interface StoreService {

    public StoreResponseDTO.StoreInfoDTO getStoreDetailInfo(Long storeId);

}
