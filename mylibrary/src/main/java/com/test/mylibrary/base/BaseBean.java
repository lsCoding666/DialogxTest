package com.test.mylibrary.base;

import java.io.Serializable;

/**
 * @Author 许朋友爱玩 @Date 2020/6/12 @Github https://github.com/LoveLifeEveryday @JueJin
 * https://juejin.im/user/5e429bbc5188254967066d1b/posts @Description BaseBean
 */
public class BaseBean<T> implements Serializable {

    /** data : code : 0 msg : */
    public int code;

    public String message;
    public T data;
    public boolean success;

    public BaseBean(boolean isSuccess, String message, int code, T data) {
        this.success = isSuccess;
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
