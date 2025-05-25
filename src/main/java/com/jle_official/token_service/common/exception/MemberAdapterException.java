package com.jle_official.token_service.common.exception;

public class MemberAdapterException extends RuntimeException {
    public MemberAdapterException(String msg) {
        super("Member Adapter Exception : " + msg);
    }
}
