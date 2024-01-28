package com.example.BapZip.service.MypageService;

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
    public MypageResponseDTO.MypageInfoDTO getMypageInfo(Long userId){
        Optional<User> tempUser =userRepository.findById(userId);
        String nickname=tempUser.get().getNickname();
        String major=tempUser.get().getMajor();
        String schoolName=tempUser.get().getSchool().getName();
        return MypageResponseDTO.MypageInfoDTO.builder().nickname(nickname).major(major).schoolName(schoolName).build();
    }


}
