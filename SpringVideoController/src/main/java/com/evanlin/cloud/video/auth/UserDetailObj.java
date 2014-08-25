package com.evanlin.cloud.video.auth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;


public class UserDetailObj implements UserDetails{

	public static UserDetails create(String username, String password,
			String...authorities) {
		return new UserDetailObj(username, password, authorities);
	}
	
	private final Collection<GrantedAuthority> authorities_;
	private final String password_;
	private final String username_;

	
	private UserDetailObj(String username, String password, String...authorities) {
		username_ = username;
		password_ = password;
		authorities_ = AuthorityUtils.createAuthorityList(authorities);
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities_;
	}

	@Override
	public String getPassword() {
		return password_;
	}

	@Override
	public String getUsername() {
		return username_;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	

}
