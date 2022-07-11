package it.cgmconsulting.myblogc9.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.model.Persona;

@Service
public class TestService {
	
	public List<Persona> creaListaPersone(){
		
		List<Persona> list = new ArrayList<Persona>();
		list.add(new Persona("Mario", "Rossi"));
		list.add(new Persona("Enrico", "Rossi"));
		list.add(new Persona("Luigi", "Bianchi"));
		list.add(new Persona("Lucia", "Verdi"));
		list.add(new Persona("Sara", "Neri"));
		
		return list;
		
	}
	
	public List<Persona> addPersona(Persona p){
		List<Persona> list = new ArrayList<Persona>();
		list = creaListaPersone();
		list.add(p);
		
		return list;
	}
	
	public List<Persona> aggiornaCognome(String vecchioCognome, String nuovoCognome){
		List<Persona> list = new ArrayList<Persona>();
		list = creaListaPersone();
		for (Persona p : list)
		{
		   if (p.getCognome().equals(vecchioCognome))
		   p.setCognome(nuovoCognome);
		}
		return list;
	}
	
	public List<Persona> rimuovi(String c){
		List<Persona> list = new ArrayList<Persona>();
		List<Persona> list2 = new ArrayList<Persona>();
		list = creaListaPersone();
		for (Persona p : list)
		{
		   if (!p.getCognome().equals(c))
		  list2.add(p);
		}
		return list2;
	}

}
