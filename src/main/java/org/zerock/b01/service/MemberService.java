package org.zerock.b01.service;

import org.zerock.b01.dto.MemberJoinDTO;

public interface MemberService {
  
  class MidExistException extends Exception{
  
  }
  
  void join(MemberJoinDTO memberJoinDTO) throws MidExistException;
}
