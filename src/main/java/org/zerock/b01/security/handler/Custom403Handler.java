package org.zerock.b01.security.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException
  ) throws IOException, ServletException {
    
    log.info("============");
    response.setStatus(HttpStatus.FORBIDDEN.value());
  
    //일반 request인지 Ajax인지
    String requestedWithHeader = request.getHeader("X-Requested-With");
  
    boolean ajaxRequest = "XMLHttpRequest".equals(requestedWithHeader);
  
    log.info("isAjax: " + ajaxRequest);
  
    //일반 request
    if (!ajaxRequest) {
    
      response.sendRedirect("/member/login?error=ACCESS_DENIED");
    }
  
  }
  
  
}
