package com.igitras.uaa.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;

/**
 * @author mason
 */
@RestController
@RequestMapping("/")
@SessionAttributes("authorizationRequest")
public class UserController {

    @RequestMapping("me")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }
}
