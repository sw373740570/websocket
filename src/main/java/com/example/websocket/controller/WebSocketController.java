package com.example.websocket.controller;

import com.example.websocket.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 基于STOMP
 */
@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    SimpUserRegistry simpUserRegistry;

    @MessageMapping("/welcome")
    @SendTo("/topic/getResponse")
    public String say(Message message, @Header("name") String name) {
        return "HELLO: This is " + name + ",says:" +  message.getContent();
    }

    @SubscribeMapping("/topic/getResponse")
    public String sub() {
        System.out.println("用户预订");
        return "感谢你订阅了我。。。";
    }

    @ResponseBody
    @GetMapping("/templateTest/{name}")
    public String templateTest(@PathVariable String name) {
        System.out.println("当前在线人数:" + simpUserRegistry.getUserCount());
        String users ="用户：";
        for (SimpUser user : simpUserRegistry.getUsers()){
            users += user.getName() + ",";
        }
        simpMessagingTemplate.convertAndSendToUser(name,"/queue/message","HELLO," + name + ".当前在线人数:" + simpUserRegistry.getUserCount() + users);
        return "当前在线人数:" + simpUserRegistry.getUserCount() + users;
    }

    @GetMapping("/index")
    public String index() {
        return "websocketStomp";
    }

}
