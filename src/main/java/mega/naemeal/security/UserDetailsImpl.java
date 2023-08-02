package mega.naemeal.security;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Builder;
import mega.naemeal.enums.UserRoleEnum;
import mega.naemeal.user.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
public class UserDetailsImpl implements UserDetails {

  private final Member member;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    UserRoleEnum role = member.getRole();
    String authority = role.getAuthority();

    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(simpleGrantedAuthority);
    return authorities;
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  public Member getMember() {
    return member;
  }

  @Override
  public String getUsername() {
    return member.getUserId();
  }

  public String getUserId() {
    return this.member.getUserId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  public boolean isEnabled() {
    return false;
  }


}
