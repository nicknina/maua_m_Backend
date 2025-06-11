package maua.moove.backend.demo.service;

import maua.moove.backend.demo.dto.HorarioDTO;
import maua.moove.backend.demo.model.DiaDaSemana;
import maua.moove.backend.demo.model.Horario;
import maua.moove.backend.demo.model.SaidaDa;
import maua.moove.backend.demo.model.Van;
import maua.moove.backend.demo.repository.HorarioRepository;
import maua.moove.backend.demo.repository.VanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private VanRepository vanRepository;

    // -- MÉTODOS NOVOS E UNIFICADOS --

    // Cria um novo horário a partir de um DTO (usado pelo Itinerário)
    public Horario criarHorario(HorarioDTO dto) {
        Van van = vanRepository.findById(dto.getVanId())
                .orElseThrow(() -> new RuntimeException("Van não encontrada com id: " + dto.getVanId()));

        Horario horario = new Horario();
        horario.setVan(van);
        horario.setSaidaDa(SaidaDa.valueOf(dto.getSaidaDa().toUpperCase()));
        horario.setHorario(LocalTime.parse(dto.getHorario(), DateTimeFormatter.ofPattern("HH:mm")));
        horario.setDiaDaSemana(DiaDaSemana.valueOf(dto.getDiaDaSemana().toUpperCase()));

        return horarioRepository.save(horario);
    }

    // Busca um horário pelo ID
    public Optional<Horario> findById(Long id) {
        return horarioRepository.findById(id);
    }

    // -- MÉTODOS MANTIDOS DO SEU CÓDIGO ORIGINAL --

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
        // O método save também serve como update se o objeto já tiver um ID
        return horarioRepository.save(horario);
    }

    public void deleteById(Long id) {
        horarioRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return horarioRepository.existsById(id);
    }

    // -- MÉTODO AUXILIAR DE CONVERSÃO --

    // Converte a entidade Horario para HorarioDTO
    public HorarioDTO convertToDTO(Horario horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setId(horario.getId());
        dto.setVanId(horario.getVan().getId());
        dto.setTipoVeiculo(horario.getVan().getTipo().name());
        dto.setPlaca(horario.getVan().getPlaca());
        dto.setSaidaDa(horario.getSaidaDa().name());
        dto.setHorario(horario.getHorario().format(DateTimeFormatter.ofPattern("HH:mm")));
        dto.setDiaDaSemana(horario.getDiaDaSemana().name());
        return dto;
    }
}
