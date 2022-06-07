package org.zerock.b01.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zerock.b01.domain.Member;
import org.zerock.b01.repository.MemberRepository;
import org.zerock.b01.security.dto.MemberSecurityDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {
  //메소드를 쓸때 추상타입을 써라
  
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private MemberRepository memberRepository;
  
  public CustomUserDetailsService() {
    this.passwordEncoder = new BCryptPasswordEncoder();
  }
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("-----------------------------------");
    log.info("-----------------login------------");
    log.info(username);
    
    Optional<Member> resultData = memberRepository.getWithRoles(username);
    Member member = resultData.orElseThrow(() -> new UsernameNotFoundException("AAAA"));
    
    log.info(member);
    
//    UserDetails userDetails = User.builder()
//            .username(username)
//            .password(passwordEncoder.encode("1111"))
//            .authorities("ROLE_USER")
//            .build();
  
    MemberSecurityDTO memberSecurityDTO =
            new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getEmail(),
                    member.isDel(),
                    false,
                    member.getRoleSet()
                            .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                            .collect(Collectors.toList()));
    return memberSecurityDTO;
  }
}
