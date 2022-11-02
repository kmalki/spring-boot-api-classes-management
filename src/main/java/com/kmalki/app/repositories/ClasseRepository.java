package com.kmalki.app.repositories;

import com.kmalki.app.entities.Classe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClasseRepository extends JpaRepository<Classe, Long> {

    List<Classe> findAll();

    Optional<Classe> findById(Long id);

    Optional<Classe> findByName(String name);
}
