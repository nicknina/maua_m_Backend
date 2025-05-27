package maua.moove.backend.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import maua.moove.backend.demo.model.DiaDaSemana;
import maua.moove.backend.demo.model.Horario;
import maua.moove.backend.demo.model.SaidaDa;
import maua.moove.backend.demo.repository.HorarioRepository;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;
    
    public List<Horario> findAllActiveVans() {
        return horarioRepository.findByVanAtivo(true);
    }
    
    public List<Horario> findByOrigemAndDia(SaidaDa saidaDa, DiaDaSemana diaDaSemana) {
        return horarioRepository.findBySaidaDaAndDiaDaSemanaAndVanAtivo(saidaDa, diaDaSemana, true);
    }
    
    public Horario save(Horario horario) {
        return horarioRepository.save(horario);
    }
    
    public Horario update(Horario horario) {
        return horarioRepository.save(horario);
    }
    
    public void deleteById(Long id) {
        horarioRepository.deleteById(id);
    }
    
    public boolean existsById(Long id) {
        return horarioRepository.existsById(id);
    }
}
