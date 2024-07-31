package com.sky.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    //存储会话
    private Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 建立连接
     * @param session
     * @param sid
     */
    @OnOpen
    public void opOpen(Session session, @PathParam("sid") String sid){
        System.out.println("客户端:"+sid+"建立连接");
        sessionMap.put(sid,session);
    }

    /**
     * 收到客户端消息后调用方法
     * @param message
     * @param sid
     */
    @OnMessage
    public void onMessage(String message,@PathParam("sid") String sid){
        System.out.println("收到来自客户端:"+sid+"的信息:"+message);
    }

    /**
     * 断开连接
     * @param sid
     */
    public void onClose(@PathParam("sid") String sid){
        System.out.println(sid+"断开连接");
        sessionMap.remove(sid);
    }

    /**
     * 群发
     * @param message
     */
    public void sendToAllClient(String message){
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送信息
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
