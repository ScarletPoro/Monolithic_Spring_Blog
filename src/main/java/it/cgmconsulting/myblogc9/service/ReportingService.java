package it.cgmconsulting.myblogc9.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cgmconsulting.myblogc9.entity.Comment;
import it.cgmconsulting.myblogc9.entity.Reporting;
import it.cgmconsulting.myblogc9.entity.ReportingId;
import it.cgmconsulting.myblogc9.entity.ReportingReason;
import it.cgmconsulting.myblogc9.entity.ReportingStatus;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.response.ReportingStatusResponse;
import it.cgmconsulting.myblogc9.repository.ReportingReasonRepository;
import it.cgmconsulting.myblogc9.repository.ReportingRepository;

@Service
public class ReportingService {
	
	@Autowired ReportingRepository reportingRepository;
	@Autowired ReportingReasonRepository reportingReasonRepository;
	
	// REPORTING REASON
	public boolean existsByReason(String reason) {
		return reportingReasonRepository.existsByReason(reason);
	}
	
	public void save(ReportingReason repReason) {
		reportingReasonRepository.save(repReason);
	}
	
	public List<ReportingReason> getValidReasons(){
		return reportingReasonRepository.getValidReasons(LocalDate.now());
	}
	
	public Optional<ReportingReason> getValidReason(String reason){
		return reportingReasonRepository.getValidReason(LocalDate.now(), reason);
	}
	
	public Optional<ReportingReason> getValidReason(long reasonId){
		return reportingReasonRepository.getValidReason(LocalDate.now(), reasonId);
	}
	
	public Optional<ReportingReason> findByReasonAndEndValidityIsNull(String reason){
		return reportingReasonRepository.findByReasonAndEndValidityIsNull(reason);
	}
	
	public boolean existsByReasonAndEndValidityIsNotNull(String reason){
		return reportingReasonRepository.existsByReasonAndEndValidityIsNotNull(reason);
	}
	
	// REPORTING
	public void save(Reporting rep) {
		reportingRepository.save(rep);
	}
	
	public boolean existsById(Comment c, User u) {
		return reportingRepository.existsById(new ReportingId(c,u));
	}
	
	public List<ReportingStatusResponse> getReportByStatus(long commentId) {
		return reportingRepository.getReportByStatus(commentId);
	}
	
	public Optional<Reporting> findById(Comment c, User u) {
		return reportingRepository.findById(new ReportingId(c,u));
	}
	
	public List<Reporting> getReportsByComment(long commentId){
		return reportingRepository.getReportsByComment(commentId);
	}
	
	public void saveAll(List<Reporting> list) {
		reportingRepository.saveAll(list);
	}
	
	public List<Reporting> massiveChangeStatus(List<Reporting> rs, String status, ReportingReason reason){
	
		List<Reporting> list = new ArrayList<Reporting>();
		for(Reporting report : rs) {
			report.setStatus(ReportingStatus.valueOf(status));
			if(reason != null) {
				report.setReasonId(reason);
			}
			list.add(report);
		}
		return list;
	}
	
	public int getSeverity(long author) {
		return reportingRepository.getSeverity(author);
	}
}
