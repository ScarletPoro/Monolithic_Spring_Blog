package it.cgmconsulting.myblogc9.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.payload.response.RegistrationTrend;
import it.cgmconsulting.myblogc9.repository.UserRepository;
import it.cgmconsulting.myblogc9.utils.Mail;

@Service
public class ScheduledService {
	
	@Autowired UserRepository userRepository;
	@Autowired MailService mailService;
	
	//@Scheduled(fixedRate=3600000) // ogni ora -> tempo espresso in millisecondi
	//@Scheduled(cron = "* */59 * * * *")
	//@Scheduled(cron = "* */1 * * * *") // ogni secondo
	//@Scheduled(cron = "*/15 * * * * *") // ogni 15 secondi
	public void andamentoRegistrazioni() {
		
		String to = userRepository.getAdminMail();
		List<RegistrationTrend> trend = userRepository.getSchedule();
		System.out.println("------ START TREND -------");
		String report = "";
		for(RegistrationTrend r : trend) {
			//System.out.println(r.getRegistrationDate()+" - "+r.getRegistrationNumber());
			report += r.getRegistrationDate()+" - "+r.getRegistrationNumber()+"\n";
		}
		Mail m = mailService.sendTrend(to, report);
		mailService.sendMail(m);
		System.out.println("------ END TREND -------");
	}

}
