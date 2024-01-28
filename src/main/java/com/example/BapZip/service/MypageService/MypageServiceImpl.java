package com.example.BapZip.service.MypageService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.User;
import com.example.BapZip.repository.SchoolRepository;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.security.TokenProvider;
import com.example.BapZip.web.dto.MypageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService{

    private final UserRepository userRepository;

    @Override
    public MypageResponseDTO.MypageInfoDTO getMypageInfo(Long userId) {
        Optional<User> tempUser = userRepository.findById(userId);
        if (tempUser.isPresent()) {
            String nickname = tempUser.get().getNickname();
            String major = tempUser.get().getMajor();
            String schoolName = tempUser.get().getSchool().getName();
            return MypageResponseDTO.MypageInfoDTO.builder()
                    .nickname(nickname)
                    .major(major)
                    .schoolName(schoolName)
                    .build();
        } else {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);//추후 존재하지 않는 유저라고 수정필요
        }
    }


}
