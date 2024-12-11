package com.ruoyi.project.jiewu.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.jiewu.service.WebsocketServe;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/socket/{userType}")
public class WebSocketServerController {

    @OnOpen
    public void onOpen(@PathParam(value = "userType") String userType, Session session) {
        WebsocketServe.addSession(userType, session);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("客户端：" + session.getId() + " 连接断开");
        //业务代码
    }

    @OnMessage
    public String onMsg(String message, Session session) {
        try {
            if(StringUtils.isJSON(message)){
                JSONObject msg = JSONObject.parse(message);
                if("playMusicEr".equals(msg.getString("userType"))){
                    JSONObject sendMsg = new JSONObject();
                    WebsocketServe.sendUserTypeMessage(msg.getString("userType"), msg.getString("worksMusic"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "heart";
    }
}
