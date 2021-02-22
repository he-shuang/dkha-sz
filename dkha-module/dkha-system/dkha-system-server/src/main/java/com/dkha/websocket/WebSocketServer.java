/**
 * Copyright (c) 2016-2020 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.dkha.websocket;



import com.alibaba.fastjson.JSON;
import com.dkha.websocket.config.WebSocketConfig;
import com.dkha.websocket.data.MessageData;
import com.dkha.websocket.data.WebSocketData;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * WebSocket服务
 *
 * @author Mark sunlightcs@gmail.com
 */

@Slf4j
@Component
@ServerEndpoint(value = "/ws", configurator = WebSocketConfig.class)
public class WebSocketServer {
    /**
     * 客户端连接信息
     */
    private static Map<String, WebSocketData> servers = new ConcurrentHashMap<>();

    @OnOpen
    public void open(Session session) {
        servers.put(session.getId(), new WebSocketData(1L, session));
    }

    @OnClose
    public void onClose(Session session) {
        //客户端断开连接
        servers.remove(session.getId());
        log.debug("websocket close, session id：" + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        servers.remove(session.getId());
        log.error(throwable.getMessage(), throwable);
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        log.info("session id: " + session.getId()+"， message：" + msg);
    }

    /**
     * 发送信息给全部用户
     * @param message    消息内容
     */
    public void sendMessageAll(MessageData<?> message) {
        servers.values().forEach(info -> sendMessage(info.getSession(), message));
    }

    public void sendMessage(Session session, MessageData<?> message) {
        try {
            session.getBasicRemote().sendText(JSON.toJSONString(message));
        } catch (IOException e) {
            log.error("send message error，" + e.getMessage(), e);
        }
    }
}