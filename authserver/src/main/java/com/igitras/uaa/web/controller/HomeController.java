package com.igitras.uaa.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * Created by mason on 6/20/16.
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    @ResponseBody
    public String user() {
        return "hello";
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
