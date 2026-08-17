package de.ea234.evorgang.model.benutzer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvgBenutzerRepository extends JpaRepository<EvgBenutzer, Integer> {

  void deleteByUsername( String username );

  boolean existsByUsername( String username );

  EvgBenutzer findByUsernameIgnoreCase( String username );
}
