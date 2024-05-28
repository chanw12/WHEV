package com.ll.whev.global.exceptions;

public enum ExceptionCode {
    PAYMENT_AMOUNT_EXP("결제 금액 예외 발생"),
    PAYMENT_NOT_FOUND("결제 정보를 찾을 수 없음"),
    ALREADY_APPROVED("이미 승인된 결제입니다."),
    MEMBER_NOT_FOUND("회원 정보를 찾을 수 없음"),
    PAYMENT_NOT_ENOUGH_POINT("포인트가 부족합니다."),
    PAYMENT_NOT_ENOUGH_CASH("캐쉬가 부족합니다."),;

    private String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}