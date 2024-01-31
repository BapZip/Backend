package com.example.BapZip.apiPayload.code.status;

import com.example.BapZip.apiPayload.code.BaseErrorCode;
import com.example.BapZip.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // user 관련 응답
    USER_JOIN_ERROR(HttpStatus.resolve(400),"USER400","회원가입에 실패하였습니다."),
    USER_LOGIN_ERROR(HttpStatus.resolve(400),"USER400","로그인에 실패하였습니다."),


    // coupon 관련 응답
    INSUFFICIENT_BALANCE_ERROR(HttpStatus.resolve(400), "COUPON400", "잔액이 부족하여 쿠폰발행에 실패했습니다."),
    USER_NOT_FOUND_ERROR(HttpStatus.resolve(404), "USER404", "해당 사용자를 찾을 수 없습니다."),

    //store 관련 응답
    STORE_NOT_EXIST_ERROR(HttpStatus.resolve(400),"STORE400","존재하지 않는 가게입니다."),
    STORE_IMAGE_NOT_EXIST_ERROR(HttpStatus.resolve(401), "STORE401", "해당 가게의 이미지가 존재하지 않습니다."),
    PRINTED_MENU_NOT_FOUND_ERROR(HttpStatus.resolve(402), "STORE402", "해당 가게의 인쇄된 메뉴가 존재하지 않습니다."),
    STORE_NOT_FOUND_FOR_USER_ERROR(HttpStatus.resolve(404), "STORE404", "해당 사용자에게 연결된 가게가 없습니다."),



    // 혼잡도 관련 응답
    CONGESTION_REGISTER_ERROR(HttpStatus.resolve(400),"CONGESTION400","혼잡도 등록에 실패하였습니다."),
    // 가게 관련 응답
    STORE_NOT_EXIST(HttpStatus.resolve(400),"STORE400","가게 id와 일치하는 가게가 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
