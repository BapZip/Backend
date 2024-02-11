package com.example.BapZip.service.S3Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;


    public List<String> uploadFiles(String folder, List<MultipartFile> multipartFiles) {
        List<String> s3files = new ArrayList<>();

        // 리뷰 작성시 이미지 첨부가 선택사항이기에 null이 아닌 경우에만 이미지 처리 로직을 수행
        if (multipartFiles != null ) {
            for (MultipartFile multipartFile : multipartFiles) {

                String uploadFileUrl = "";

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(multipartFile.getSize());
                objectMetadata.setContentType(multipartFile.getContentType());

                try (InputStream inputStream = multipartFile.getInputStream()) {

                    String keyName = folder + "/" + UUID.randomUUID() + "." + multipartFile.getOriginalFilename();

                    // S3에 폴더 및 파일 업로드
                    amazonS3Client.putObject(
                            new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                    .withCannedAcl(CannedAccessControlList.PublicRead));

                    // S3에 업로드한 폴더 및 파일 URL
                    uploadFileUrl = amazonS3Client.getUrl(bucketName, keyName).toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("Filed upload failed", e);
                }

                s3files.add(uploadFileUrl);
            }

        }



        return s3files;
    }

}