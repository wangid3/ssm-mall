package com.ssm.mall.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ssm.mall.common.Result;

import java.io.Serializable;

/*
通过使用@JsonSerialize注解，返回json对象时，jackson不对值为null的属性进行包含和处理
例如：
1-当回传成功状态码status的值是200时，msg和data都为null
2-当回传错误信息（status、msg）时，data为null
这两种情况，json结果中都不必包含null值
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerRes<T> implements Serializable {
    private int status;
    private String msg;
    private T data;
    //根据返回信息的类型，设定构造函数
    private ServerRes(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private ServerRes(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ServerRes(int status) {
        this.status = status;
    }
    //使用静态方法
    public static<T>  ServerRes<T> success(Result result,T data){
        return new ServerRes<T>(result.getCode(),result.getMsg(),data);
    }
    //返回其他的成功信息
    public static<T> ServerRes<T> success(Result result){
        return new ServerRes<T>(result.getCode(),result.getMsg());
    }
    //使用@JsonIgnore，直接返回此方法的返回值，无需进行json序列化处理
    @JsonIgnore
    public static int success(){
        return Result.RESULT_SUCCESS.getCode();
    }
    //返回Error信息
    public static<T> ServerRes<T> error(Result result){
        return new ServerRes<T>(result.getCode(),result.getMsg());
    }

    public static<T> ServerRes error(){
        return new ServerRes(Result.RESULT_ERROR.getCode(),Result.RESULT_ERROR.getMsg());
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ServerRes{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
