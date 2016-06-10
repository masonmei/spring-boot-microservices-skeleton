package com.igitras.uaa.custom.security;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * @author mason
 */
public class UaaClientDetailsService implements ClientDetailsService {
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        BaseClientDetails openid =
                new BaseClientDetails(clientId, null, "openid", "authorization_code,refresh_token,password", null);
        openid.setClientSecret("security");
        return openid;
    }
}
