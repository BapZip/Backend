package com.example.BapZip.apiPayload.code.status;

import com.example.BapZip.apiPayload.code.BaseCode;
import com.example.BapZip.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {
    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // 멤버 관련 응답
    _USER_SIGN_UP_SUCCESS(HttpStatus.OK, "USER200", "회원가입에 성공하였습니다."),


    // ~~~ 관련 응답
    STORE_ZIP_SUCCESS(HttpStatus.OK, "STORE200", "가게 zip에 성공하였습니다."),
    STORE_UNZIP_SUCCESS(HttpStatus.OK, "STORE200", "가게 zip 해제에 성공하였습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
