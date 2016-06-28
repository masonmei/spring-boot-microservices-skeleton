package com.igitras.uaa.web.controller;

import static com.igitras.common.utils.Constants.Headers.AUTH_HEADER;
import static com.igitras.common.utils.Constants.Headers.BEARER;
import static com.igitras.common.utils.Constants.Params.TOKEN;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * OAuth2 Controller.
 *
 * @author mason
 */
@Controller
@RequestMapping("oauth")
public class OAuth2Controller {

    @Autowired
    private DefaultTokenServices tokenServices;

    @RequestMapping(value = "token/revoke", method = GET)
    public void revokeToken(@RequestParam(TOKEN) String token) {
        tokenServices.revokeToken(token);
    }

    @RequestMapping(value = "logout", method = POST)
    public void logout(@RequestHeader(AUTH_HEADER) String authHeader) {
        if (authHeader != null) {
            String tokenValue = authHeader.replace(BEARER, "").trim();
            tokenServices.revokeToken(tokenValue);
        }
    }
}
