package com.example.websocket.controller;

import com.example.websocket.ws.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {

    @GetMapping("/index/{cid}")
    public String index(@PathVariable String cid, Model model) {
        model.addAttribute("cid", cid);
        return "websocketClient";
    }

    @ResponseBody
    @GetMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid,String message) {
        try {
            WebSocketServer.sendInfo(message, cid);
            return "success：" + cid;
        } catch (IOException e) {
            e.printStackTrace();
            return "error：" + cid;
        }
    }
}
