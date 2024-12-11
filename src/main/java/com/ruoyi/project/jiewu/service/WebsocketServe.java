package com.ruoyi.project.jiewu.service;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketServe {

    /**
     * 记录当前在线的Session
     */
    private static final Map<String, Session> ONLINE_SESSION = new ConcurrentHashMap<>();

    /**
     * 添加session
     *
     * @param userId
     * @param session
     */
    public static void addSession(String userId, Session session) {
        ONLINE_SESSION.put(userId, session);
    }

    /**
     * 关闭session
     *
     * @param userId
     */
    public static void removeSession(String userId) {
        ONLINE_SESSION.remove(userId);
    }

    /**
     * 给单个用户推送消息
     *
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message) {
        if (session == null) {
            return;
        }
        // 同步
        RemoteEndpoint.Async async = session.getAsyncRemote();
        async.sendText(message);
    }

    public static void sendUserTypeMessage(String userId, String message) {
        Session session = ONLINE_SESSION.get(userId);
        if (session == null || !session.isOpen()) {
            return;
        }

        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendUserListTypeMessage(String userId, String message) {
        ONLINE_SESSION.forEach((sessionId, session) -> {
                    if (sessionId.contains(userId)) {
                        if (session == null || !session.isOpen()) {
                            return;
                        }
                        sendMessage(session, message);
                    }
                }
        );
    }

    /**
     * 向所有在线人发送消息
     *
     * @param message
     */
    public static void sendMessageForAll(String message) {
        //jdk8 新方法
        ONLINE_SESSION.forEach((sessionId, session) -> sendMessage(session, message));
    }

}
