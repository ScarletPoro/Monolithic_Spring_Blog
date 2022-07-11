package it.cgmconsulting.myblogc9.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.cgmconsulting.myblogc9.entity.ReportingReason;
import it.cgmconsulting.myblogc9.payload.request.ReportingReasonRequest;
import it.cgmconsulting.myblogc9.service.ReportingService;

@RestController
@RequestMapping("reason")
@Validated
public class ReportingReasonController {
	
	@Autowired ReportingService reportingService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value="add-reason")
	public ResponseEntity<?> addReason(@RequestBody @Valid ReportingReasonRequest request){
		
		String reason = request.getReason().toUpperCase().trim();
		
		if(reportingService.existsByReason(reason))
			return new ResponseEntity<String>("Reason already present. You can only modify it", HttpStatus.FORBIDDEN);
		
		ReportingReason repReason = new ReportingReason(
				reason,
				request.getSeverity(),
				request.getStartValidity(),
				request.getEndValidity()
				);
		reportingService.save(repReason);
		
		return new ResponseEntity<String>("New reason added: "+reason, HttpStatus.OK);
	}
	
	@GetMapping("/get-all-valid")
	public ResponseEntity<?> getValidReasons(){
		List<ReportingReason> list = reportingService.getValidReasons();
		return new ResponseEntity<List<ReportingReason>>(list, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("update-reason")
	public ResponseEntity<?> updateReason(@RequestParam @NotBlank String reason, @RequestParam @Min(1) int severity,
			@RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate endValidity){
		
		// UPDATE
		// cercare Reason per nome e con endValidity = null (ovvero quella in corso di validità)
		// aggiornare l'endValidity
		reason = reason.toUpperCase().trim();
		
		Optional<ReportingReason> repReasonToUpdate = reportingService.findByReasonAndEndValidityIsNull(reason);
		if(!repReasonToUpdate.isPresent())
			return new ResponseEntity<String>("Updatable reason not found", HttpStatus.NOT_FOUND);
		
		if(repReasonToUpdate.get().getStartValidity().isAfter(endValidity) || 
				endValidity == null)
		return new ResponseEntity<String>("End validity is before Start validity", HttpStatus.FORBIDDEN);	
		
		if(repReasonToUpdate.get().getSeverity() == severity)
			return new ResponseEntity<String>("Same severity: it's useless to update", HttpStatus.FORBIDDEN);	
		
		repReasonToUpdate.get().setEndValidity(endValidity);
		reportingService.save(repReasonToUpdate.get());
		
		
		// INSERT
		// Inserire un nuovo record avente la stessa Reason di quello appena aggiornato,
		// una data di inizio validità superiore di 1 giorno rispetto alla
		// data di fine validità del record aggiornato e
		// una nuova severity
		
		ReportingReason repReasonToInsert = new ReportingReason(
				reason, 
				severity, 
				endValidity.plusDays(1), 
				null);
		reportingService.save(repReasonToInsert);
		
		return new ResponseEntity<String>("Reason "+reason+" updated", HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("delete-reason")
	public ResponseEntity<?> deleteReason(@RequestParam @NotBlank String reason){
		
		reason = reason.toUpperCase().trim();
		
		Optional<ReportingReason>  repReason = reportingService.findByReasonAndEndValidityIsNull(reason);
		if(!repReason.isPresent())
			return new ResponseEntity<String>("No reason found", HttpStatus.NOT_FOUND);
		
		repReason.get().setEndValidity(LocalDate.now());
		reportingService.save(repReason.get());
		return new ResponseEntity<String>("Reason "+reason+" removed", HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("restore-reason")
	public ResponseEntity<?> restoreReason(@RequestBody @Valid ReportingReasonRequest request){
		
		String reason = request.getReason().toUpperCase().trim();
		
		boolean ex = reportingService.existsByReasonAndEndValidityIsNotNull(reason);
		if(!ex)
			return new ResponseEntity<String>("No reason found to restore", HttpStatus.NOT_FOUND);
		
		ReportingReason repReason = new ReportingReason(
				reason,
				request.getSeverity(),
				request.getStartValidity(),
				request.getEndValidity()
				);
		reportingService.save(repReason);
		
		return new ResponseEntity<String>("Reason "+reason+" restored", HttpStatus.OK);
		
	}

}
