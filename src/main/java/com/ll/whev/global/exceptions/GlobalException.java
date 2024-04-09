package com.ll.whev.global.exceptions;


import com.ll.whev.global.rsData.RsData;
import com.ll.whev.standard.base.Empty;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final RsData<Empty> rsData;

    public GlobalException(String resultCode, String msg) {
        super("resultCode=" + resultCode + ",msg=" + msg);
        this.rsData = RsData.of(resultCode, msg);
    }

//    public GlobalException(CodeMsg codeMsg) {
//        super(codeMsg);
//    }
//    public GlobalException(CodeMsg codeMsg) {
//        this(codeMsg.getCode(), codeMsg.getMessage());
//    }

    public GlobalException() {
        this("400-0", "에러");
    }

    public GlobalException(String msg) {
        this("400-0", msg);
    }

}
