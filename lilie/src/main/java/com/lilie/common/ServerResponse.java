package com.lilie.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by geely
 */
//所有的功能或者接口都使用这个对象
//在web开发中，现在比较流行的是从控制层往前台返回json格式的数据，而若每次的返回都设计一个类的话，不方便使用的同时也会显得很臃肿。
// 因此可以设计一个高可用复用的对象，来统一返回的json格式的数据。

//1.首先，要明确，这个对象要实现序列化接口。它主要封装了三个属性，泛型的返回数据，字符串类型的提示信息以及整型的状态码，以及四个私有的构造函数，
// 需要注意的是，当T 的类型也就是数据类型是String类型时，好像会和下面的String msg重合，到底会调用哪一个呢？
// 答案是，当T为String时，的确会调用第二个，这样会产生一个问题，就是当返回的数据就是String，如果这样就会用到msg的那个构造函数，传到信息那边去了。
// 解决方法在后面
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//字段为空的话，不序列化进json数据，即不像前台返回空的值。
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable {

    private int status;//泛型的返回数据
    private String msg;//字符串类型的提示信息
    private T data;//整型的状态码，也就算放对象，因为不知道是哪个对象，所以就制成t。

    //下面是四个私有构造函数
    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //     公司用的这个
    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore//
    //成员变量的get方法。
    //如果status 是0 ，返回true，如果不是，返回false
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    //静态方法
//    提供对外访问的七个构造方法，成功的有四个，失败的三个
    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    //    这个方法就解决了msg和String类型的数据冲突的问题
    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    //这个方法就解决了msg和String类型的数据冲突的问题
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }


}
