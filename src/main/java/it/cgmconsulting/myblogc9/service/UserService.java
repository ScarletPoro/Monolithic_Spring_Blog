package it.cgmconsulting.myblogc9.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.entity.Authority;
import it.cgmconsulting.myblogc9.entity.AuthorityName;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.response.UserResponse;
import it.cgmconsulting.myblogc9.repository.AuthorityRepository;
import it.cgmconsulting.myblogc9.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired UserRepository userRepository;
	@Autowired AuthorityRepository authorityRepository;
	
	public Optional<User> findById(long id){
		return userRepository.findById(id);
	}
	
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public boolean existsByEmail(String email) {
		return userRepository.existsByUsername(email);
	}
	
	public void save(User u) {
		userRepository.save(u);
	}
	
	public long countUsers() {
		return userRepository.count();
	}
	
	public Optional<User> findByUsernameOrEmail(String usernameOrEmail){
		return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
	}
	
	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findByUsername(authentication.getName()).get();
	}
	
	public UserResponse getAuthenticatedUserResponse() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.getUserResponse(authentication.getName());
	}
	
	public Optional<Authority> findByAuthorityName(AuthorityName an){
		return authorityRepository.findByAuthorityName(an);
	}
	
	public Set<Authority> getByAuthorityNameIn(Set<String> authorityNames){
		return authorityRepository.getByAuthorityNameIn(authorityNames);
	}

}
