package com.example.BapZip.service.StoreService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.PrintedMenu;
import com.example.BapZip.domain.Store;
import com.example.BapZip.domain.User;
import com.example.BapZip.repository.PrintedMenuRepository;
import com.example.BapZip.repository.StoreRepository;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.web.dto.MypageResponseDTO;
import com.example.BapZip.web.dto.StoreResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final PrintedMenuRepository printedMenuRepository;

    @Override
    public StoreResponseDTO.StoreInfoDTO getStoreDetailInfo(Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        if (store.isPresent()) {
            String waitingAverage = store.get().getWaitingAverage();
            String businessHours = store.get().getBusinessHours();
            String closedDay = store.get().getClosedDay();
            String position = store.get().getPosition();

            return StoreResponseDTO.StoreInfoDTO.builder()
                    .waitingAverage(waitingAverage)
                    .BusinessHours(businessHours)
                    .closedDay(closedDay)
                    .position(position)
                    .build();
        } else {
            throw new GeneralException(ErrorStatus.STORE_NOT_EXIST_ERROR);
        }
    }


    @Override
    public List<StoreResponseDTO.PrintedMenuDTO> getStorePrintedMenu(Long storeId) {

        return printedMenuRepository.findByStore_Id(storeId).stream().map(printedMenu -> new StoreResponseDTO.PrintedMenuDTO(printedMenu.getId(),printedMenu.getImageURL()))
                .collect(Collectors.toList());
    }
}
