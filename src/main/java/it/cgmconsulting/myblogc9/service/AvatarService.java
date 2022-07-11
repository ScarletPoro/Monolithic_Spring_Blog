package it.cgmconsulting.myblogc9.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.entity.Avatar;
import it.cgmconsulting.myblogc9.repository.AvatarRepository;

@Service
public class AvatarService {
	
	@Autowired AvatarRepository avatarRepository;
	
	public void save(Avatar a) {
		avatarRepository.save(a);
	}

	public void remove(long id) {
		avatarRepository.deleteById(id); 
	}
	
	
}
