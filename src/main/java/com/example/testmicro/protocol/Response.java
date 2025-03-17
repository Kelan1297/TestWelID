package com.example.testmicro.protocol;

public class Response{

    private short setResultCode;
    private String setResultDesc;
    public Response() {}
    public Response(short setResultCode, String setResultDesc) {
        this.setResultCode = setResultCode;
        this.setResultDesc = setResultDesc;
    }
    public short getResultCode() {
        return setResultCode;
    }

    public void setResultCode(short setResultCode) {
        this.setResultCode = setResultCode;
    }

    public String getResultDesc() {
        return setResultDesc;
    }

    public void setResultDesc(String setResultDesc) {
        this.setResultDesc = setResultDesc;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status='" + setResultCode + '\'' +
                ", message='" + setResultDesc + '\'' +
                '}';
    }
}
