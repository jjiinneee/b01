package org.zerock.b01.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.b01.domain.Member;
import org.zerock.b01.domain.MemberRole;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTest {
  
  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private MemberRepository memberRepository;
  
  @Test
  public void insertMembers(){
    
    IntStream.rangeClosed(1,100).forEach(i -> {
        
        Member member = Member.builder()
                .mid("member"+i)
                .mpw(passwordEncoder.encode("1111"))
                .email("email"+i+"@aaa.bbb")
                .build();
        
        member.addRole(MemberRole.USER);
        
        if(i >= 90){
          member.addRole(MemberRole.ADMIN);
        }
        memberRepository.save(member);
    });
  }
 
  
  @Test
  public void selectMember(){
    String mid = "member90";
  
    Optional<Member> result = memberRepository.getWithRoles(mid);
    
    log.info(result.get());
  }
  
}
