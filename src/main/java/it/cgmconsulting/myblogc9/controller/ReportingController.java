package it.cgmconsulting.myblogc9.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.cgmconsulting.myblogc9.entity.Comment;
import it.cgmconsulting.myblogc9.entity.Reporting;
import it.cgmconsulting.myblogc9.entity.ReportingId;
import it.cgmconsulting.myblogc9.entity.ReportingReason;
import it.cgmconsulting.myblogc9.entity.ReportingStatus;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.request.ReportingRequest;
import it.cgmconsulting.myblogc9.payload.response.ReportingStatusResponse;
import it.cgmconsulting.myblogc9.service.CommentService;
import it.cgmconsulting.myblogc9.service.ReportingService;
import it.cgmconsulting.myblogc9.service.UserService;

@RestController
@RequestMapping("rep")
public class ReportingController {
	
	@Autowired UserService userService;
	@Autowired CommentService commentService;
	@Autowired ReportingService reportingService;
	
	@PreAuthorize("hasRole('READER')")
	@PostMapping("add")
	public ResponseEntity<?> addReport(@RequestBody @Valid ReportingRequest request){
		
		Optional<Comment> c = commentService.findByIdAndCensoredFalse(request.getCommentId());
		if(!c.isPresent())
			return new ResponseEntity<String>("Comment to report not found", HttpStatus.NOT_FOUND);
		
		// Se tra le segnalazioni del commento in questione ce n'è ALMENO una che NON SIA in status OPEN
		// non permetto a nessuno di aprire un'ulteriore segnalazione per qeullo specifico commento,
		// visto che è già stato preso in carico (IN_PROGRESS) o chiuso (CLOSED_...)
		List<ReportingStatusResponse> rsr = reportingService.getReportByStatus(c.get().getId()); 
		if(!rsr.isEmpty()) {
			long countRep = 0;
			countRep = rsr.stream().filter(r -> !r.getStatus().equals(ReportingStatus.valueOf("OPEN"))).count();
			if(countRep > 0)
				return new ResponseEntity<String>("You cannot report this comment: it's alredy managed", HttpStatus.FORBIDDEN);
		}
		
		User u = userService.getAuthenticatedUser();
		
		if(u.equals(c.get().getAuthor()))
			return new ResponseEntity<String>("You cannot report yourself!", HttpStatus.FORBIDDEN);
			
		if(reportingService.existsById(c.get(), u))
			return new ResponseEntity<String>("You already reported this comment", HttpStatus.FORBIDDEN);
		
		Optional<ReportingReason> reason = reportingService.getValidReason(request.getReason());
		if(!reason.isPresent())
			return new ResponseEntity<String>("Reason not found", HttpStatus.NOT_FOUND);
		
		Reporting r = new Reporting(
				new ReportingId(c.get(), u),
				reason.get());
		
		reportingService.save(r);
			return new ResponseEntity<String>("New report has been added", HttpStatus.OK);	
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = {"update/{commentId}/{newStatus}", "update/{commentId}", "update/{commentId}/{newStatus}/{reasonId}"})
	@Transactional
	public ResponseEntity<?> update(
			@PathVariable long commentId, @PathVariable(required=false) Long reasonId, @PathVariable(required=false) String newStatus){
		
		Optional<Comment> c = commentService.findById(commentId);
		List<Reporting> rs = reportingService.getReportsByComment(commentId);
		ReportingReason reason = null;
		if(reasonId != null) {
			reason = reportingService.getValidReason(reasonId).get();
		}
		
		String status = rs.get(0).getStatus().name();
		
		if(status.equals("OPEN")) {
			// settare a IN_PROGRESS tutte le segnalazioni del commento preso in esame
			reportingService.saveAll(reportingService.massiveChangeStatus(rs, "IN_PROGRESS", reason));
		}
		
		if(status.equals("IN_PROGRESS")) {
			if(newStatus == null)
				return new ResponseEntity<String>("You need to set a CLOSE... status", HttpStatus.BAD_REQUEST);
			if(newStatus.equals("CLOSED_WITH_BAN")) {
				// censurare il commento
				c.get().setCensored(true);
				// disabilitare lo user
				c.get().getAuthor().setEnabled(false);
			} 			
			
			reportingService.saveAll(reportingService.massiveChangeStatus(rs, newStatus, reason));
		}
	return new ResponseEntity<String>("Reports about comment "+commentId+" have been updated to "+rs.get(0).getStatus().name(), HttpStatus.OK);
	}

}
