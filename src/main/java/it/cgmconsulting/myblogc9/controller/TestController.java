package it.cgmconsulting.myblogc9.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.cgmconsulting.myblogc9.model.Persona;
import it.cgmconsulting.myblogc9.service.TestService;

@RestController
public class TestController {
	
	@Autowired TestService testService;
	
	@GetMapping("hello") // http://localhost:8085/hello
	public String hello() {
		return "Hello, anonymous";
	}
	
	@GetMapping("hello2") // http://localhost:8085/hello2?nome=Mario&cognome=Rossi
	public String hello2(@RequestParam String nome, @RequestParam(defaultValue="ignoto") String cognome) {
		return "Hello, "+nome+" "+cognome;
	}
	
	@GetMapping("hello/{nome}/{cognome}") // http://localhost:8085/hello/Mario/Rossi
	public String hello3(@PathVariable String nome, @PathVariable String cognome){
		return "Hello, "+nome+" "+cognome;
	}
	
	@PostMapping("add") // localhost:8085/add
	public List<Persona> aggiungiPersonaAllaLista(@RequestBody Persona p){
		
		Persona persona = new Persona(p.getNome(), p.getCognome());
		List<Persona> list = testService.addPersona(persona);
		
		return list;
		
	}
	
	@PutMapping("update") // localhost:8085/update
	public List<Persona> modificaLista(@RequestParam String vecchioCognome, @RequestParam String nuovoCognome){
		List<Persona> list = testService.aggiornaCognome(vecchioCognome, nuovoCognome);
		return list;
	}
	
	@DeleteMapping("remove/{cognome}") // // localhost:8085/remove
	public List<Persona> remove(@PathVariable String cognome){
		
		List<Persona> list = testService.rimuovi(cognome);
		
		return list;
		
	}
	
}
