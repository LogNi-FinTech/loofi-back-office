package com.loofi.backoffice.userdetailservice;

import com.loofi.backoffice.repository.UserRepositoty;
import com.loofi.backoffice.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class UserDetailService implements UserDetailsService {
	@Autowired
	private UserRepositoty userRepositoty;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.loofi.backoffice.entity.User userEntity = userRepositoty.findByUsername(username);
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (userEntity.getRoles() != null)
			for (Role role : userEntity.getRoles()) {
				authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
			}
		User user = new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
		return user;
	}
}
