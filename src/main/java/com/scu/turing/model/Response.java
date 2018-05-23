package com.scu.turing.model;

public class Response<T> {
    /**
     * 返回信息码
     */
    private String rspCode = "000000";
    /**
     * 返回信息内容
     */
    private String rspMsg = "操作成功";

    private T data;

    public Response() {
    }

    public Response(T data) {
        this.data = data;
    }

    public Response(ExceptionMsg msg) {
        this.rspCode = msg.getCode();
        this.rspMsg = msg.getMsg();
    }

    public Response(String rspCode, String rspMsg) {
        this.rspCode = rspCode;
        this.rspMsg = rspMsg;
    }

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }

    public String getRspMsg() {
        return rspMsg;
    }

    public void setRspMsg(String rspMsg) {
        this.rspMsg = rspMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "rspCode='" + rspCode + '\'' +
                ", rspMsg='" + rspMsg + '\'' +
                '}';
    }
}
