package maua.moove.backend.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import maua.moove.backend.demo.model.Van;

@Repository
public interface VanRepository extends JpaRepository<Van, Long> {
    List<Van> findByAtivo(Boolean ativo);
}