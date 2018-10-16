package com.github.ayz6uem.restdoc.example.common;

public class ResultData<T> {

    /**
     * 返回码
     */
    int code;
    //返回信息
    String msg;
    T data;

    public static <T> ResultData<T> ok(){
        return ok(null);
    }
    public static <T> ResultData<T> ok(T data){
        ResultData<T> resultData = new ResultData<>();
        resultData.code = 0;
        resultData.msg = "ok";
        resultData.data = data;
        return resultData;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
