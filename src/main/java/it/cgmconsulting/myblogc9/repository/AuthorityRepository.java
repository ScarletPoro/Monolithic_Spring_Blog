package it.cgmconsulting.myblogc9.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.Authority;
import it.cgmconsulting.myblogc9.entity.AuthorityName;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    
	Optional<Authority> findByAuthorityName(AuthorityName name);
	
	// SQL
	@Query(value="SELECT a.* "
			+ "FROM authority a "
			+ "WHERE authority_name IN (:roles)", nativeQuery=true)
	Set<Authority> getByAuthorityNameIn(@Param("roles") Set<String> roles);

}
