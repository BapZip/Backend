package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.MailService.MailService;
import com.example.BapZip.web.dto.MailDTO;
import com.example.BapZip.web.dto.PointResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;

    @Operation(summary = "이메일 인증", description = "본인 인증을 위한 이메일 api입니다.")
    @GetMapping
    public ApiResponse<MailDTO.CertificationNumber> mailSend(String mail){
        MailDTO.CertificationNumber certificationNumber = mailService.sendCertificationNumberEmail(mail);
        return ApiResponse.onSuccess(certificationNumber);
    }

}
