package org.zerock.b01.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    
    log.info("======================");
    log.info("======================");
    log.info(userRequest);
    log.info("======================");
  
    ClientRegistration clientRegistration = userRequest.getClientRegistration();
    String clientName = clientRegistration.getClientName();
  
    log.info("NAME: "+clientName);
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> paramMap = oAuth2User.getAttributes();
  
    String email = null;
  
    switch (clientName){
      case "kakao":
        email = getKakaoEmail(paramMap);
        break;
    }
    log.info(email);
//    return super.loadUser(userRequest);
    
    return new MemberSecurityDTO(email,"1111", paramMap);
  }
  
  
  private String getKakaoEmail(Map<String, Object> paramMap){
    
    log.info("KAKAO-----------------------------------------");
    
    Object value = paramMap.get("kakao_account");
    
    log.info(value);
    
    LinkedHashMap accountMap = (LinkedHashMap) value;
    
    String email = (String)accountMap.get("email");
    
    log.info("email..." + email);
    
    return email;
  }
}
