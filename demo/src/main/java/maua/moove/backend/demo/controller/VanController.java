package maua.moove.backend.demo.controller;


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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import maua.moove.backend.demo.dto.VanDTO;
import maua.moove.backend.demo.model.TipoVan;
import maua.moove.backend.demo.model.Van;
import maua.moove.backend.demo.repository.VanRepository;

@RestController
@RequestMapping("/api/vans")
public class VanController {

    @Autowired
    private VanRepository vanRepository;
    
    // Listar todas as vans
    @GetMapping
    public ResponseEntity<List<VanDTO>> getAllVans(
            @RequestParam(required = false) Boolean ativo) {
        
        List<Van> vans;
        if (ativo != null) {
            vans = vanRepository.findByAtivo(ativo);
        } else {
            vans = vanRepository.findAll();
        }
        
        List<VanDTO> vanDTOs = vans.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(vanDTOs);
    }
    
    // Obter van por ID
    @GetMapping("/{id}")
    public ResponseEntity<VanDTO> getVanById(@PathVariable Long id) {
        return vanRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Adicionar nova van (apenas admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VanDTO> addVan(@RequestBody VanDTO vanDTO) {
        Van van = convertToEntity(vanDTO);
        Van savedVan = vanRepository.save(van);
        return ResponseEntity.ok(convertToDTO(savedVan));
    }
    
    // Atualizar van existente (apenas admin)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VanDTO> updateVan(
            @PathVariable Long id, 
            @RequestBody VanDTO vanDTO) {
        
        if (!vanRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        Van van = convertToEntity(vanDTO);
        van.setId(id);
        Van updatedVan = vanRepository.save(van);
        return ResponseEntity.ok(convertToDTO(updatedVan));
    }
    
    // Desativar van (soft delete) (apenas admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVan(@PathVariable Long id) {
        if (!vanRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        // Soft delete - apenas marcamos como inativo
        Van van = vanRepository.findById(id).orElseThrow();
        van.setAtivo(false);
        vanRepository.save(van);
        
        return ResponseEntity.ok().build();
    }
    
    // Método para converter entidade em DTO
    private VanDTO convertToDTO(Van van) {
        VanDTO dto = new VanDTO();
        dto.setId(van.getId());
        dto.setPlaca(van.getPlaca());
        dto.setTipo(van.getTipo().toString());
        dto.setCapacidade(van.getCapacidade());
        dto.setAtivo(van.getAtivo());
        return dto;
    }
    
    // Método para converter DTO em entidade
    private Van convertToEntity(VanDTO dto) {
        Van van = new Van();
        // O ID seria setado apenas na atualização
        van.setPlaca(dto.getPlaca());
        van.setTipo(TipoVan.valueOf(dto.getTipo().toUpperCase()));
        van.setCapacidade(dto.getCapacidade());
        van.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        return van;
    }
}