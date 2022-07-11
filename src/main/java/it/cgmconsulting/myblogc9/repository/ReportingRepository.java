package it.cgmconsulting.myblogc9.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.Reporting;
import it.cgmconsulting.myblogc9.entity.ReportingId;
import it.cgmconsulting.myblogc9.payload.response.ReportingResponse;
import it.cgmconsulting.myblogc9.payload.response.ReportingStatusResponse;

@Repository
public interface ReportingRepository extends JpaRepository<Reporting, ReportingId>{
	
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.ReportingStatusResponse("
			+ "r.reportingId.comment.id, "
			+ "r.status) "
			+ "FROM Reporting r "
			+ "WHERE r.reportingId.comment.id = :commentId ")
	List<ReportingStatusResponse> getReportByStatus(@Param("commentId") long commentId);
	
	@Query(value="SELECT r FROM Reporting r "
			+ "WHERE r.reportingId.comment.id = :commentId ")
	List<Reporting> getReportsByComment(@Param("commentId") long commentId);
	
	List<Reporting> findByReportingIdCommentId(long commentId);
	
	@Query(value="SELECT rr.severity FROM reporting_reason rr "
			+ "INNER JOIN reporting r ON r.reason_id = rr.id "
			+ "INNER JOIN comment c ON c.id=r.comment_id "
			+ "INNER JOIN user u ON u.id = c.author "
			+ "AND c.author = :authorId "
			+ "AND r.updated_at >= u.updated_at", nativeQuery=true
			)
	int getSeverity(@Param("authorId") long authorId);

}
