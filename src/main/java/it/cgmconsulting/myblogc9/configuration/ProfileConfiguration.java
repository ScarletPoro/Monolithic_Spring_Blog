package it.cgmconsulting.myblogc9.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class ProfileConfiguration {
	
	@Autowired Environment env;
	
	@Bean
	public void getActiveProfiles() {
		
		String[] profiloAttivo = env.getActiveProfiles();
		for(String profilo : profiloAttivo) {
			System.out.println("Profilo attivo : "+profilo);
		}
	}
	
	@Bean
	@Profile("produzione")
	public void getActiveProfile() {
		
		String tz = env.getProperty("spring.jpa.properties.hibernate.jdbc.time_zone");
		System.out.println("La time zone attiva è: "+tz);
	}

}
