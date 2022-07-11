package it.cgmconsulting.myblogc9.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.ReportingReason;

@Repository
public interface ReportingReasonRepository extends JpaRepository<ReportingReason, Long>{

	boolean existsByReason(String reason);
	
	@Query(value="SELECT r FROM ReportingReason r "
			+ "WHERE :now >= r.startValidity "
			+ "AND (r.endValidity IS NULL OR r.endValidity >= :now) "
			+ "ORDER BY r.severity ASC")
	List<ReportingReason> getValidReasons(@Param("now") LocalDate now);
	
	@Query(value="SELECT r FROM ReportingReason r "
			+ "WHERE :now >= r.startValidity "
			+ "AND (r.endValidity IS NULL OR r.endValidity >= :now) "
			+ "AND r.reason = :reason "
			+ "ORDER BY r.severity ASC")
	Optional<ReportingReason> getValidReason(@Param("now") LocalDate now, @Param("reason") String reason);
	
	@Query(value="SELECT r FROM ReportingReason r "
			+ "WHERE :now >= r.startValidity "
			+ "AND (r.endValidity IS NULL OR r.endValidity >= :now) "
			+ "AND r.id = :reasonId ")
	Optional<ReportingReason> getValidReason(@Param("now") LocalDate now, @Param("reasonId") long reasonId);

	Optional<ReportingReason> findByReasonAndEndValidityIsNull(String reason);
	
	boolean existsByReasonAndEndValidityIsNotNull(String reason);
}
