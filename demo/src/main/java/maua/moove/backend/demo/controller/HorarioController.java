package maua.moove.backend.demo.controller;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import maua.moove.backend.demo.dto.HorarioDTO;
import maua.moove.backend.demo.model.DiaDaSemana;
import maua.moove.backend.demo.model.Horario;
import maua.moove.backend.demo.model.SaidaDa;
import maua.moove.backend.demo.model.Van;
import maua.moove.backend.demo.repository.VanRepository;
import maua.moove.backend.demo.service.HorarioService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;
    
    // Endpoint para obter horários (acessível para todos)
    @GetMapping
    public ResponseEntity<List<HorarioDTO>> getAllHorarios() {
        List<Horario> horarios = horarioService.findAllActiveVans();
        List<HorarioDTO> horarioDTOs = horarios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(horarioDTOs);
    }
    
    // Endpoint para obter horários por origem e dia
    @GetMapping("/origem/{origem}/dia/{dia}")
    public ResponseEntity<List<HorarioDTO>> getHorariosByOrigemAndDia(
            @PathVariable String origem,
            @PathVariable String dia) {
        try {
            SaidaDa saidaDa = SaidaDa.valueOf(origem.toUpperCase());
            DiaDaSemana diaDaSemana = DiaDaSemana.valueOf(dia.toUpperCase());
            
            List<Horario> horarios = horarioService.findByOrigemAndDia(saidaDa, diaDaSemana);
            List<HorarioDTO> horarioDTOs = horarios.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(horarioDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Endpoint para adicionar um novo horário (apenas admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HorarioDTO> addHorario(@RequestBody HorarioDTO horarioDTO) {
        try {
            Horario horario = convertToEntity(horarioDTO);
            Horario savedHorario = horarioService.save(horario);
            return ResponseEntity.ok(convertToDTO(savedHorario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Endpoint para atualizar um horário (apenas admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HorarioDTO> updateHorario(
            @PathVariable Long id, 
            @RequestBody HorarioDTO horarioDTO) {
        
        if (!horarioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            Horario horario = convertToEntity(horarioDTO);
            horario.setId(id);
            Horario updatedHorario = horarioService.update(horario);
            return ResponseEntity.ok(convertToDTO(updatedHorario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Endpoint para excluir um horário (apenas admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHorario(@PathVariable Long id) {
        if (!horarioService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        horarioService.deleteById(id);
        return ResponseEntity.ok().build();
    }
    
    // Método auxiliar para converter entidade em DTO
    private HorarioDTO convertToDTO(Horario horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setId(horario.getId());
        dto.setVanId(horario.getVan().getId());
        dto.setTipoVeiculo(horario.getVan().getTipo().toString());
        dto.setSaidaDa(horario.getSaidaDa().toString());
        dto.setHorario(horario.getHorario().format(DateTimeFormatter.ofPattern("HH:mm")));
        dto.setDiaDaSemana(horario.getDiaDaSemana().toString());
        dto.setPlaca(horario.getVan().getPlaca());
        return dto;
    }
    
    // Método auxiliar para converter DTO em entidade
    private Horario convertToEntity(HorarioDTO dto) {
        Horario horario = new Horario();
        
        // A Van precisa ser obtida do repositório usando o vanId
        // Isso geralmente seria feito no serviço, mas simplificamos aqui
        Van van = vanRepository.findById(dto.getVanId())
                .orElseThrow(() -> new RuntimeException("Van não encontrada"));
        
        horario.setVan(van);
        horario.setSaidaDa(SaidaDa.valueOf(dto.getSaidaDa().toUpperCase()));
        horario.setHorario(LocalTime.parse(dto.getHorario()));
        horario.setDiaDaSemana(DiaDaSemana.valueOf(dto.getDiaDaSemana().toUpperCase()));
        
        return horario;
    }
    
    @Autowired
    private VanRepository vanRepository;
}
