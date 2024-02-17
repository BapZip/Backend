package com.example.BapZip.service.MypageService;

import com.example.BapZip.apiPayload.code.status.ErrorStatus;
import com.example.BapZip.apiPayload.exception.GeneralException;
import com.example.BapZip.domain.Point;
import com.example.BapZip.domain.User;
import com.example.BapZip.repository.PointRepository;
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
    private final PointRepository pointRepository;

    @Override
    public MypageResponseDTO.MypageInfoDTO getMypageInfo(Long userId) {
        Optional<User> tempUser = userRepository.findById(userId);

        List<Point> points = pointRepository.findByUserId(userId);

        int totalPoints = 0;
        for (Point point : points) {
            totalPoints += point.getPoint();
        }


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
                    .totalPoint(totalPoints)
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
