package com.example.BapZip.service.StoreService;

import com.example.BapZip.web.dto.StoreResponseDTO;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;

import java.util.List;

public interface StoreService {

    public StoreResponseDTO.StoreInfoDTO getStoreDetailInfo(Long storeId);

    List<StoreResponseDTO.PrintedMenuDTO> getStorePrintedMenu(Long storeId);

    List<StoreResponseDTO.MyZipDTO> getStoreMyZip(Long userId);

    List<StoreResponseDTO.HotPlaceDTO> getHotPlace();

}
