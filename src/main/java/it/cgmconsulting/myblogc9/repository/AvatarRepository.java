package it.cgmconsulting.myblogc9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.cgmconsulting.myblogc9.entity.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long>{

}
