package com.example.BapZip.service.MypageService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.User;
import com.example.BapZip.repository.SchoolRepository;
import com.example.BapZip.repository.UserRepository;
import com.example.BapZip.security.TokenProvider;
import com.example.BapZip.service.S3Service.AmazonS3Service;
import com.example.BapZip.web.dto.MypageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService{

    private final UserRepository userRepository;
    private final AmazonS3Service s3Service;

    @Override
    public MypageResponseDTO.MypageInfoDTO getMypageInfo(Long userId) {
        Optional<User> tempUser = userRepository.findById(userId);
        if (tempUser.isPresent()) {
            String nickname = tempUser.get().getNickname();
            String major = tempUser.get().getMajor();
            String schoolName = tempUser.get().getSchool().getName();
            String imageUrl=tempUser.get().getImageUrl();
            return MypageResponseDTO.MypageInfoDTO.builder()
                    .nickname(nickname)
                    .major(major)
                    .schoolName(schoolName)
                    .imageUrl(imageUrl)
                    .build();
        } else {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);//추후 존재하지 않는 유저라고 수정필요
        }
    }

    @Override
    public MypageResponseDTO.MypageInfoDTO fetchMypageProfile(Long userId, List<MultipartFile> imageList) {
        User tempUser = userRepository.findById(userId).get();
        List<String> urls = s3Service.uploadFiles("test",imageList);
        tempUser.setImageUrl(urls.get(0));
        userRepository.save(tempUser);

        return MypageResponseDTO.MypageInfoDTO.builder()
                .imageUrl(urls.get(0))
                .nickname(tempUser.getNickname())
                .schoolName(tempUser.getSchool().getName())
                .major(tempUser.getMajor())
                .build();

    }

}
