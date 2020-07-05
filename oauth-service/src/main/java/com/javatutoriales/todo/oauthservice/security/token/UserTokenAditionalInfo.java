package com.javatutoriales.todo.oauthservice.security.token;

import com.javatutoriales.todo.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserTokenAditionalInfo implements TokenEnhancer {

    private final UserTokenEnhanced tokenEnhanced;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> aditionalInfo = new HashMap<>();

        User user = tokenEnhanced.findByUsername(authentication.getName());

        //TODO: Enhance with name of the user
        aditionalInfo.put("email", user.getEmail());
        aditionalInfo.put("name", user.getName());

        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(aditionalInfo);

        return accessToken;
    }
}
