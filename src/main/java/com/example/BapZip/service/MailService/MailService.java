package com.example.BapZip.service.MailService;

import com.example.BapZip.web.dto.MailDTO;

public interface MailService{
    public MailDTO.CertificationNumber sendCertificationNumberEmail(String mail);
}
