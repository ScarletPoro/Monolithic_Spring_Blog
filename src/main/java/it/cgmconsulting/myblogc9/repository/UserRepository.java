package it.cgmconsulting.myblogc9.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.AuthorityName;
import it.cgmconsulting.myblogc9.entity.User;
import it.cgmconsulting.myblogc9.payload.response.ReaderXlsResponse;
import it.cgmconsulting.myblogc9.payload.response.RegistrationTrend;
import it.cgmconsulting.myblogc9.payload.response.UserResponse;
import it.cgmconsulting.myblogc9.payload.response.UserXlsResponse;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);
	
	// select * from user where username='username' or email='email';
	// Esempio: 
	// select * from user where username='admin' or email='admin';
	// select * from user where username='admin@gmail.abc' or email='admin@gmail.abc';
	Optional<User> findByUsernameOrEmail(String username, String email);
		
	Boolean existsByUsernameOrEmail(String username, String email);
	
	List<User> findAllByEnabledTrue();
	
	Optional<User> findByIdAndEnabledTrue(Long id);
	Optional<User> findByUsername(String username);
	Optional<User> findByUsernameAndEnabledTrue(String username);
	Optional<User> findByEmail(String email);	
	
	// JPQL
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.UserResponse("
			+ "u.username, "
			+ "u.email, "
			+ "u.createdAt) " // sul db corrisponde al campo created_at
			+ "FROM User u "
			+ "WHERE u.username = :username")
	UserResponse getUserResponse(@Param("username") String username);
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.UserXlsResponse("
			+ "u.id, "
			+ "u.username, "
			+ "(SELECT COUNT(p.id) from Post p WHERE u=p.author) as postsWritten, "
			+ "(SELECT COALESCE(ROUND(AVG(r.rate),2), 0) FROM Rating r WHERE r.ratingId.post.author.id=u.id) AS average "
			+ ") FROM User u "
			+ "INNER JOIN u.authorities a ON a.authorityName='ROLE_WRITER' "
			)
	List<UserXlsResponse> getReportAuthor();
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.ReaderXlsResponse("
			+ "u.id, "
			+ "u.username, "
			+ "(SELECT COUNT(c) FROM c WHERE u.id=c.author.id) as commentsWritten, "
			+ "(SELECT COUNT(a) FROM Reporting a WHERE a.reportingId.comment.author.id=u.id AND a.status='CLOSED_WITH_BAN') AS advisoriesWithBan,"
			+ "u.enabled "
			+ ") FROM User u "
			+ "LEFT JOIN Comment c ON u.id=c.author.id "
			+ "INNER JOIN u.authorities a ON a.authorityName='ROLE_READER' "
			+ "GROUP BY u.id"
			)
	List<ReaderXlsResponse> getReportReader();
	
	
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.RegistrationTrend(" 
			+ "COUNT(function('date_format', u.createdAt, '%Y-%m-%d')),"   
			+ "function('date_format', u.createdAt, '%Y-%m-%d')"
			+ ") "  
		    + "FROM User u " 
		    + "GROUP BY function('date_format', u.createdAt, '%Y-%m-%d')") 
	List<RegistrationTrend> getSchedule();
	
	@Query(value="SELECT u.email FROM User u "
			+ "INNER JOIN u.authorities a ON a.authorityName = 'ROLE_ADMIN'")
	String getAdminMail();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	@Query(value="SELECT new it.cgmconsulting.myblogc9.payload.response.RegistrationTrend("
		+ "function(date_format(created_at, '%Y-%m-%d')) AS registrationDate, "
		+ "COUNT(function(date_format(created_at, '%Y-%m-%d'))) AS registrationNumber"
		+ ") "
		+ "FROM User u "
		+ "GROUP BY function(date_format(created_at, '%Y-%m-%d')) "
		+ "ORDER BY registrationDate")
	List<RegistrationTrend> getTrend();
	*/
    	
	
	
	
}