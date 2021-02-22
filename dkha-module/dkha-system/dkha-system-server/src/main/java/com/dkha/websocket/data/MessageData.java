/**
 * Copyright (c) 2016-2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.dkha.websocket.data;

import lombok.Data;

/**
 * 响应客户端数据
 *
 * @author Mark sunlightcs@gmail.com
 */
@Data
public class MessageData<T> {
    /**
     * 编码  0：文本消息  1：推送人员信息(教职工、学生、重要设备、访客标签等信息变动) 2：设备变动
     */
    private int type = 0;
    /**
     * 文本消息
     */
    private String msg;
    /**
     * 对象消息
     */
    private T data;

    public MessageData<T> data(T data) {
        this.setData(data);
        this.type = 1;
        return this;
    }

    public MessageData<T> msg(String msg) {
        this.msg = msg;
        return this;
    }
}
