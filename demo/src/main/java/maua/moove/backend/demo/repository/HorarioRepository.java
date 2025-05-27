package maua.moove.backend.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import maua.moove.backend.demo.model.DiaDaSemana;
import maua.moove.backend.demo.model.Horario;
import maua.moove.backend.demo.model.SaidaDa;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {
    List<Horario> findBySaidaDaAndDiaDaSemanaAndVanAtivo(SaidaDa saidaDa, DiaDaSemana diaDaSemana, Boolean vanAtivo);
    List<Horario> findByVanAtivo(Boolean vanAtivo);
}