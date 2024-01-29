package com.example.BapZip.service.UserService;

import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;

public interface UserService {
    public UserResonseDTO.JoinDTO create(final UserRequestDTO.JoinDTO dto);
    public UserResonseDTO.loginDTO login(UserRequestDTO.LoginDTO loginDTO);
}
