package com.ravn.challenge.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.ravn.challenge.entity.User;
import com.ravn.challenge.repository.IGenericRepo;
import com.ravn.challenge.repository.IUserRepository;

/**
 * 
 * 
 * @author andres
 */
@Service
public class UserService extends GenericService<User, Long> implements UserDetailsService {

	private final IUserRepository userRepository;
	
	public UserService(IUserRepository userRepository) {
		super(User.class);
		this.userRepository = userRepository;
	}
	
	public User findByEmail(String email) {
		return this.userRepository.findOneByEmail(email)
				.orElseThrow(() -> new NoSuchElementException(String.format("User %s not found", email)));
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) {
		
		User user = this.userRepository.findOneByEmail(email)
				.orElseThrow(() -> new NoSuchElementException(String.format("User with email %s does not exists.", email)));
		
		List<GrantedAuthority> roles = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		
		return org.springframework.security.core.userdetails.User.builder()
				.username(email)
				.password(user.getPassword())
				.authorities(roles)
				.build();
	}

	@Override
	protected IGenericRepo<User, Long> getRepo() {
		return this.userRepository;
	}
}
