package mega.naemeal.security;

import lombok.RequiredArgsConstructor;
import mega.naemeal.user.entity.Member;
import mega.naemeal.user.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    Member member = memberRepository.findByUserId(userId).orElseThrow(
        () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")
    );

    List<GrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority("ROLE_" + member.getRole().toString())
    );
    return new UserDetailsImpl(member);
  }
}

