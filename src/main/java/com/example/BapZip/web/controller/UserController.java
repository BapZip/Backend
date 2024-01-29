package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.service.UserService.UserService;
import com.example.BapZip.service.UserService.UserServiceImpl;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API", description = "로그인 API입니다. 학과는 string 값, 학교는 int(id값)로 넣어주세요.")
    @PostMapping("/signup")
    public ApiResponse<UserResonseDTO.JoinDTO> registerUser(@RequestBody UserRequestDTO.JoinDTO user)
    {
            UserResonseDTO.JoinDTO result = userService.create(user);
            return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "로그인 API", description = "로그인 API입니다.")
    @PostMapping("/signin")
    public ApiResponse<UserResonseDTO.loginDTO> login(@RequestBody UserRequestDTO.LoginDTO loginDTO)
    {
            UserResonseDTO.loginDTO result = userService.login(loginDTO);
            return ApiResponse.onSuccess(result);
    }
}
