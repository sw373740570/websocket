<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h3>websocket STOMP</h3>
<button onclick="subscribe3()">订阅消息/user/queue/message</button>
<hr/>
<div id="message"></div>
<input type="text" id="say">
<button onclick="say()">发送</button>
</body>
<script src="http://cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://cdn.bootcss.com/sockjs-client/1.1.4/sockjs.min.js"></script>
<script type="text/javascript">
    // 建立连接对象（还未发起连接）
    var socket = new SockJS("http://localhost:8088/endpointService");
    // 获取 STOMP 子协议的客户端对象
    var stompClient = Stomp.over(socket);
    var name = "test" + parseInt(Math.random() * 10);
    console.log(name);
    // 向服务器发起websocket连接并发送CONNECT帧
    stompClient.connect({token:name},function (frame) {
        console.log(frame);
        console.log("连接成功");
        stompClient.subscribe("/topic/getResponse", function(response) {
            setMessageInnerHTML("/topic/getResponse 你接收到的消息为:" + response.body);
        });
    },function (error) {
        console.log(error);
        console.log("连接失败");
    })

    function subscribe3() {
        stompClient.subscribe("/user/queue/message", function(response) {
            setMessageInnerHTML("/user/queue/message 你接收到的消息为:" + response.body);
        });
    }
    
    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }



    function say() {
        stompClient.send("/welcome", {name:name}, JSON.stringify({'content': document.getElementById('say').value}));
    }
</script>
</html>