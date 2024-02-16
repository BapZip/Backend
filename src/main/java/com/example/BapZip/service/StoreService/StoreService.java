package com.example.BapZip.service.StoreService;

import com.example.BapZip.web.dto.StoreResponseDTO;

import com.example.BapZip.domain.Store;
import com.example.BapZip.repository.StoreRepository;
import com.example.BapZip.web.dto.StoreResponseDTO;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;

import java.util.List;

public interface StoreService {

    StoreResponseDTO.StoreDetailInfoDTO getStoreDetailInfo(Long storeId);

    List<StoreResponseDTO.PrintedMenuDTO> getStorePrintedMenu(Long storeId);

    List<StoreResponseDTO.MyZipDTO> getStoreMyZip(Long userId);

    List<StoreResponseDTO.HotPlaceDTO> getHotPlace(Long SchoolId);
    StoreResponseDTO.StoreInfoDTO getStoreInfo(String userId, Long storeId);

    List<StoreResponseDTO.StoreListReviewCountDTO> getStoreListByReviewCount(Long userId,Long schoolId);

    List<StoreResponseDTO.StoreListScoreDTO> getStoreListByScore(Long userId,Long schoolId);


    StoreResponseDTO.NoticeDTO getNotice(Long storeId);

    void zipStore(String userId, Long storeId);

    void unzipStore(String userId, Long storeId);

    List<StoreResponseDTO.searchStore> searchStore(String name);
    StoreResponseDTO.RecommandDTO getRecommendStoresByLikes(String categoryName,Long schoolId);

    List<List<StoreResponseDTO.menuDTO>> getMenuList(Long storeId);
}
