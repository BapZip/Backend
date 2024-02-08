package com.example.BapZip.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MailDTO {
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CertificationNumber
    {
        private Integer number;
    }
}
