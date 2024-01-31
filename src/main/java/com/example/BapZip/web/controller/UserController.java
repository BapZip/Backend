package com.example.BapZip.web.controller;

import com.example.BapZip.apiPayload.ApiResponse;
import com.example.BapZip.apiPayload.code.BaseCode;
import com.example.BapZip.apiPayload.code.status.SuccessStatus;
import com.example.BapZip.service.UserService.UserService;
import com.example.BapZip.service.UserService.UserServiceImpl;
import com.example.BapZip.web.dto.UserRequestDTO;
import com.example.BapZip.web.dto.UserResonseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입 API", description = "로그인 API입니다. 학과는 string 값, 학교는 int(id값)로 넣어주세요.")
    @PostMapping("/auth/signup")
    public ApiResponse<UserResonseDTO.JoinDTO> registerUser(@RequestBody UserRequestDTO.JoinDTO user)
    {
            UserResonseDTO.JoinDTO result = userService.create(user);
            return ApiResponse.onSuccess(result);
    }


    @Operation(summary = "로그인 API", description = "로그인 API입니다.")
    @PostMapping("/auth/signin")
    public ApiResponse<UserResonseDTO.loginDTO> login(@RequestBody UserRequestDTO.LoginDTO loginDTO)
    {
            UserResonseDTO.loginDTO result = userService.login(loginDTO);
            return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "닉네임 중복 체크 API", description = "닉네임 중복 체크 API입니다.")
    @GetMapping("/auth/checkNickname")
    public ApiResponse<UserResonseDTO.checkNicknameAndIdDTO> checkNickname(@RequestParam(name = "nickname") String nickname)
    {
        UserResonseDTO.checkNicknameAndIdDTO result = userService.checkNickname(nickname);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "아이디 중복 체크 API", description = "아이디 중복 체크 API입니다.")
    @GetMapping("/auth/checkUserid")
    public ApiResponse<UserResonseDTO.checkNicknameAndIdDTO> checkUserid(@RequestParam(name = "userid") String userid)
    {
        UserResonseDTO.checkNicknameAndIdDTO result = userService.checkUserid(userid);
        return ApiResponse.onSuccess(result);
    }

    @Operation(summary = "이용 약관 API", description = "이용 약관 API입니다.")
    @PostMapping("/auth/agreement")
    public ApiResponse<UserResonseDTO.checkNicknameAndIdDTO> agreementToTerm(@RequestBody UserRequestDTO.TermDTO agreement)
    {
        userService.agreementToTerm(agreement);
        return ApiResponse.of(SuccessStatus._USER_SIGN_UP_SUCCESS,null);
    }


}
