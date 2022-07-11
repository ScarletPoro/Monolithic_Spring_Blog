package it.cgmconsulting.myblogc9.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.cgmconsulting.myblogc9.entity.Authority;
import it.cgmconsulting.myblogc9.entity.AuthorityName;
import it.cgmconsulting.myblogc9.entity.Avatar;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.request.SigninRequest;
import it.cgmconsulting.myblogc9.payload.request.SignupRequest;
import it.cgmconsulting.myblogc9.payload.response.UserResponse;
import it.cgmconsulting.myblogc9.security.JwtAuthenticationResponse;
import it.cgmconsulting.myblogc9.security.JwtTokenUtil;
import it.cgmconsulting.myblogc9.service.AvatarService;
import it.cgmconsulting.myblogc9.service.ImageService;
import it.cgmconsulting.myblogc9.service.ReportingService;
import it.cgmconsulting.myblogc9.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Validated
public class UserController {
	
	@Autowired UserService userService;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired AuthenticationManager authenticationManager;
	@Autowired UserDetailsService userDetailsService;
	@Autowired JwtTokenUtil jwtTokenUtil;
	@Autowired AvatarService avatarService;
	@Autowired ImageService imageService;
	@Autowired ReportingService reportingService;
	
	@Value("${jwt.header}")
	private String tokenHeader;
	
	@Value("${avatar.user.size}")
	private long aSize;
	
	@Value("${avatar.user.width}")
	private int aWidth;
	
	@Value("${avatar.user.height}")
	private int aHeight;
	
	@Value("${avatar.user.ext}")
	private String[] extensions;
	
	@PostMapping("public/signup")
	@Transactional
	public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request){
		
		long countUser = userService.countUsers();
		
		// Verifico che lo username scelto non esista già sul database
		if(userService.existsByUsername(request.getUsername()))
			return new ResponseEntity<String>("Username already in use", HttpStatus.FORBIDDEN);
		
		// Verifico che l'email scelta non esista già sul database
		if(userService.existsByEmail(request.getEmail()))
			return new ResponseEntity<String>("Email already in use", HttpStatus.FORBIDDEN);
		
		// Creo un oggetto User in modo da poterlo poi persistere sul database.
		User u = new User(request.getUsername(), request.getPassword(), request.getEmail());
		// Setto la password criptata sullo user appena creato.
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		
		// Persisto l'utente sul database
		userService.save(u);
		
		// Creo un oggetto Authority 
		// che viene settato come ADMIN se è il primo utente a registrarsi
		// altrimenti viene settato a READER
		Optional<Authority> a = Optional.empty();
		if(countUser > 0) {
			a = userService.findByAuthorityName(AuthorityName.ROLE_READER);
		} else {
			a = userService.findByAuthorityName(AuthorityName.ROLE_ADMIN);
		}
		u.setAuthorities(Collections.singleton(a.get()));
		
		return new ResponseEntity<String>("User successfully registered", HttpStatus.CREATED);
	}
	
	@PostMapping("public/signin")
	@Transactional
	public ResponseEntity<?> signin(@Valid @RequestBody SigninRequest request, HttpServletResponse response){
		
		Optional<User> u = userService.findByUsernameOrEmail(request.getUsernameOrEmail());
		if(!u.isPresent())
			return new ResponseEntity<String>("Wrong username or password", HttpStatus.OK);
		
		// effettuare controllo su scadenza ban: recuperare updatedAt dell'UTENTE e sommarci la severity della reason
		// 		se la somma supera data e ora in cui l'utente fa login, l'utente viene riabilitato
		//		altrimenti mandare un messagio che dice "banned until ... <LocaldateTime di fine BAN>"
		if(!u.get().isEnabled()) {
			LocalDateTime ban = u.get().getUpdatedAt().plusDays(reportingService.getSeverity(u.get().getId()));
			if(ban.isBefore(LocalDateTime.now()))
				u.get().setEnabled(true);
			else
				return new ResponseEntity<String>("Utente bannato fino al "+ban , HttpStatus.FORBIDDEN);
		}
		// fine controllo ban
		
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(u.get().getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(u.get().getUsername());
		String token = null;
		try {
			token = jwtTokenUtil.generateToken(userDetails);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setHeader(tokenHeader, token);
		
		return new ResponseEntity<JwtAuthenticationResponse>(
				new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities(), token)
				, HttpStatus.OK);
	}
	
	
	@GetMapping("get-me-1")
	public ResponseEntity<?> getMe(){
		User u = userService.getAuthenticatedUser();
		UserResponse ur = new UserResponse(u.getUsername(), u.getEmail(), u.getCreatedAt());
		return new ResponseEntity<UserResponse>(ur, HttpStatus.OK);
	}
	
	@GetMapping("get-me-2")
	public ResponseEntity<?> getMe2(){
		log.info("estraggo lo user dal security context holder");
		UserResponse ur = userService.getAuthenticatedUserResponse();
		return new ResponseEntity<UserResponse>(ur, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("user/add-role/{userId}/{role}")
	public ResponseEntity<?> changeRole(@PathVariable long userId, @PathVariable String role){
		
		Optional<User> u = userService.findById(userId);
		if(!u.isPresent())
			return new ResponseEntity<String>("user not found", HttpStatus.NOT_FOUND);
		
		Optional<Authority> a = userService.findByAuthorityName(AuthorityName.valueOf(role));
		if(!a.isPresent())
			return new ResponseEntity<String>("role not found", HttpStatus.NOT_FOUND);
		
		Set<Authority> authorities = u.get().getAuthorities();
		boolean x = authorities.add(a.get());
		if(!x)
			return new ResponseEntity<String>("Role already present", HttpStatus.FORBIDDEN);
		userService.save(u.get());
		
		return new ResponseEntity<String>("New role added to "+u.get().getUsername(), HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("user/change-roles/{userId}")
	public ResponseEntity<?> changeRole(@PathVariable long userId, @RequestParam Set<String> roles){
		
		Optional<User> u = userService.findById(userId);
		if(!u.isPresent())
			return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
		
		Set<Authority> authorities = userService.getByAuthorityNameIn(roles);
		
		u.get().setAuthorities(authorities);
		userService.save(u.get());
		
		return new ResponseEntity<String>("New roles added to "+u.get().getUsername(), HttpStatus.OK);
		
	}
	
	@PutMapping("user/change-pwd")
	public ResponseEntity<?> changePassword(@RequestParam @NotBlank @Size(min=6, max=12) String newPwd){
	
		User user = userService.getAuthenticatedUser();
	
		if (passwordEncoder.matches(newPwd, user.getPassword()))
			return new ResponseEntity<String>("Le password sono uguali!", HttpStatus.BAD_REQUEST) ;
		
		user.setPassword(passwordEncoder.encode(newPwd));
		userService.save(user);
		return new ResponseEntity<String>("Password cambiata correttamente", HttpStatus.OK);
	}
	
	@PutMapping("user/update-avatar")
	@Transactional
	public ResponseEntity<?> updateAvatar(@RequestParam MultipartFile file){

		User u = userService.getAuthenticatedUser();
		Avatar a = new Avatar();
		
		if(u.getAvatar() != null) {

			try {
				u.getAvatar().setFileType(file.getContentType());
				u.getAvatar().setFileName(file.getOriginalFilename());
				u.getAvatar().setData(file.getBytes());
			} catch (IOException e) {
				return new ResponseEntity<String>("Something went wrong updatin the avatar", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}else {
			try {
				a.setFileType(file.getContentType());
				a.setFileName(file.getOriginalFilename());
				a.setData(file.getBytes());
				avatarService.save(a);
				u.setAvatar(a);
			} catch (IOException e) {
				return new ResponseEntity<String>("Something went wrong updating the avatar", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}

		return new ResponseEntity<String>("Avatar successfully updated", HttpStatus.OK);

	}
	
	@Transactional
	@PutMapping("user/update-avatar2")
	public ResponseEntity<?> updateAvatar2(@RequestParam MultipartFile file){
		User u = userService.getAuthenticatedUser();
		
		if(file.isEmpty())
			return new ResponseEntity<String>("Empty file !", HttpStatus.BAD_REQUEST);
		
		if(!imageService.checkExtension(file, extensions))
			return new ResponseEntity<String>("File type not allowed", HttpStatus.BAD_REQUEST);
		
		if(!imageService.checkDimensions(imageService.fromFileToBufferedImage(file), aHeight, aWidth))
			return new ResponseEntity<String>("Image tooo large !", HttpStatus.BAD_REQUEST);
		
		if(!imageService.checkSize(file, aSize))
			return new ResponseEntity<String>("File size greater then "+aSize/1024+" kb", HttpStatus.BAD_REQUEST);	
		
		Avatar a = new Avatar();
		try {
			if(u.getAvatar()!=null) {
				a.setId(u.getAvatar().getId());
			}
			a.setFileType(file.getContentType());
			a.setFileName(file.getOriginalFilename());
			a.setData(file.getBytes());
			avatarService.save(a);
			u.setAvatar(a);
		} catch (IOException e) {
			return new ResponseEntity<String>("Something went wrong updating the avatar", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Avatar successfully uploaded" , HttpStatus.OK);
	}
	
	@DeleteMapping("user/remove-image")
	@Transactional
	public ResponseEntity<?> removeAvatar(){
		
		User u = userService.getAuthenticatedUser();
		
		if(u.getAvatar()!=null) {
			long avatarId = u.getAvatar().getId();
			u.setAvatar(null);
			avatarService.remove(avatarId);
			return new ResponseEntity<String>("Avatar successfully removed" , HttpStatus.OK);
		}
		return new ResponseEntity<String>("No avatar to remove" , HttpStatus.BAD_REQUEST);
	}
}
