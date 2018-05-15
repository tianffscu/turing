package com.scu.turing.entity.result;

public class ServerException extends RuntimeException {

    private ExceptionMsg msg;

    public ServerException(ExceptionMsg msg) {
        this.msg = msg;
    }

    public ExceptionMsg getExpMsg() {
        return msg;
    }

    public String getMessage() {
        return msg.getMsg();
    }

    public String getCode() {
        return msg.getCode();
    }
}
