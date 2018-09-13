<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h3>Test Websocket</h3>
</body>
<script>
    var websocket;
    if(typeof(WebSocket) == "undefined") {
        alert("WebSocket");
    } else {
        console.log("您的浏览器支持WebSocket");
    }
    //实现化WebSocket对象，指定要连接的服务器地址与端口  建立连接
    websocket = new WebSocket("ws://localhost:8088/websocket/${cid}" );
    websocket.onopen = function (ev) {
        console.log("Socket 已打开");
        //socket.send("这是来自客户端的消息" + location.href + new Date());
    }
    websocket.onmessage = function (msg) {
        console.log(msg.data);
        //发现消息进入    开始处理前端触发逻辑
    }
    //关闭事件
    websocket.onclose = function() {
        console.log("Socket已关闭");
    };
    //发生了错误事件
    websocket.onerror = function() {
        alert("Socket发生了错误");
        //此时可以尝试刷新页面
    }
</script>
</html>