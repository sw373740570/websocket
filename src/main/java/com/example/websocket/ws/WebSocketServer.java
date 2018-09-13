package com.example.websocket.ws;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArrayList<WebSocketServer> websocketSet = new CopyOnWriteArrayList<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String sid;

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){
        this.session = session;
        websocketSet.add(this);
        addOnlineCount();
        System.out.println("有新窗口开始监听:"+sid+",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            System.out.println("websocket IO异常");
        }
    }

    @OnClose
    public void onClose() {
        websocketSet.remove(this);
        subOnlineCount();
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到来自窗口"+sid+"的信息:"+message);
        for (WebSocketServer webSocketServer : websocketSet) {
            try {
                webSocketServer.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        System.out.println("推送消息到窗口"+sid+"，推送内容:"+message);
        for (WebSocketServer webSocketServer : websocketSet) {
            try{
                if (sid == null) {
                    webSocketServer.sendMessage(message);
                } else if (sid.equals(webSocketServer.sid)){
                    webSocketServer.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }

        }
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
