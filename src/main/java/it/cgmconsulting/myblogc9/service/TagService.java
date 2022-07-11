package it.cgmconsulting.myblogc9.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.entity.Tag;
import it.cgmconsulting.myblogc9.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired TagRepository tagRepository;
	
	public String save(String tagName) {
		tagName = tagName.toUpperCase().trim();
		boolean a = tagRepository.existsByTagName(tagName);
		
		if(a)
			return "Tag "+tagName+" già presente.";
		
		Tag t = new Tag(tagName);
		tagRepository.save(t);
		return "Tag "+tagName+" inserito con successo";
	}
	
	public void save(Tag t) {
		tagRepository.save(t);
	}
	
	public String update(long id, String newTagName) {
		newTagName = newTagName.toUpperCase().trim();
		boolean a = tagRepository.existsByTagName(newTagName);
		if(a)
			return "Tag "+newTagName+" già presente.";
		
		Optional<Tag> t = tagRepository.findById(id);
		if(!t.isPresent())
			return "Tag con id "+id+" non esiste";
		
		t.get().setTagName(newTagName);
		tagRepository.save(t.get());
		return "Tag "+newTagName+" aggiornato con successo";
	}
	
	public String switchVisibility(long id) {
		Optional<Tag> t = tagRepository.findById(id);
		if(!t.isPresent())
			return "Tag con id "+id+" non esiste";
		
		t.get().setVisible(!t.get().isVisible());
		
		tagRepository.save(t.get());
		return "La visibilità del tag ora è: "+t.get().isVisible();
	}
	
	public List<Tag> getAll(){
		return tagRepository.findAll();
	}
	
	public List<Tag> getAllVisible(){
		return tagRepository.findByIsVisibleTrue();
	}
	
	public Set<Tag> getPostsFromTags(Set<Long> ids){
		return tagRepository.getPostsFromTags(ids);
	}
	
	public Tag getTagByName(String tagName) {
		return tagRepository.getTagByName(tagName);
		
	}
	
	public Set<Long> getPostIdsByTagname(String tagName){
		return tagRepository.getPostIdsByTagname(tagName);
	}
	
	public List<String> getTagsNameByPost(long postId){
		return tagRepository.getTagsNameByPost(postId);
	}

}
