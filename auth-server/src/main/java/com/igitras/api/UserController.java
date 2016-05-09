package com.igitras.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author mason
 */
@RestController
@RequestMapping("/")
public class UserController {

    @RequestMapping("me")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }
}
