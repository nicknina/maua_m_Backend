package maua.moove.backend.demo.controller;

import maua.moove.backend.demo.dto.HorarioDTO;
import maua.moove.backend.demo.model.DiaDaSemana;
import maua.moove.backend.demo.model.Horario;
import maua.moove.backend.demo.model.SaidaDa;
import maua.moove.backend.demo.model.Van;
import maua.moove.backend.demo.repository.VanRepository;
import maua.moove.backend.demo.service.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    // Injetado para buscar a van durante a atualização
    @Autowired
    private VanRepository vanRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // --- MÉTODOS PARA O NOVO ITINERÁRIO (com WebSocket) ---

    // Endpoint para ADICIONAR um novo horário pelo itinerário
    @PostMapping
    public ResponseEntity<?> criarHorarioPeloItinerario(@RequestBody HorarioDTO horarioDTO) {
        try {
            Horario horarioSalvo = horarioService.criarHorario(horarioDTO);
            notifyScheduleUpdate(horarioSalvo.getSaidaDa(), horarioSalvo.getDiaDaSemana());
            return ResponseEntity.ok(horarioService.convertToDTO(horarioSalvo));
        } catch (Exception e) {
            // Log do erro para depuração no backend
            System.err.println("Erro ao criar horário: " + e.getMessage());
            // Retorna uma resposta 400 com a mensagem de erro para o frontend
            return ResponseEntity.badRequest().body("Erro ao processar a requisição: " + e.getMessage());
        }
    }

    // Endpoint para DELETAR um horário pelo itinerário
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarHorarioPeloItinerario(@PathVariable Long id) {
        try {
            Horario horario = horarioService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Horário com id " + id + " não encontrado."));
            
            SaidaDa saidaDa = horario.getSaidaDa();
            DiaDaSemana diaDaSemana = horario.getDiaDaSemana();
            
            horarioService.deleteById(id);
            
            // Após deletar, notifica os clientes da mudança
            notifyScheduleUpdate(saidaDa, diaDaSemana);
            
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.err.println("Erro ao deletar horário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Endpoint para OBTER horários por origem e dia
    @GetMapping("/origem/{origem}/dia/{dia}")
    public ResponseEntity<List<HorarioDTO>> obterHorariosPorFiltro(
            @PathVariable("origem") SaidaDa origem,
            @PathVariable("dia") DiaDaSemana dia) {
        List<Horario> horarios = horarioService.findByOrigemAndDia(origem, dia);
        List<HorarioDTO> horarioDTOs = horarios.stream()
                .map(horarioService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(horarioDTOs);
    }

    // --- MÉTODOS MANTIDOS DO SEU CÓDIGO ORIGINAL ---

    // Endpoint genérico para obter todos os horários de vans ativas
    @GetMapping
    public ResponseEntity<List<HorarioDTO>> getAllHorarios() {
        List<Horario> horarios = horarioService.findAllActiveVans();
        List<HorarioDTO> horarioDTOs = horarios.stream()
                .map(horarioService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(horarioDTOs);
    }
    
    // Endpoint para atualizar um horário (lógica corrigida e com tratamento de erro)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHorario(@PathVariable Long id, @RequestBody HorarioDTO horarioDTO) {
        try {
            // Garante que o horário a ser atualizado existe
            Horario horarioParaAtualizar = horarioService.findById(id)
                .orElseThrow(() -> new RuntimeException("Horário com id " + id + " não encontrado para atualização."));

            // Busca a entidade Van que será associada
            Van van = vanRepository.findById(horarioDTO.getVanId())
                .orElseThrow(() -> new RuntimeException("Van com id " + horarioDTO.getVanId() + " não encontrada."));

            // Atualiza os campos da entidade existente com os dados do DTO
            horarioParaAtualizar.setVan(van);
            horarioParaAtualizar.setSaidaDa(SaidaDa.valueOf(horarioDTO.getSaidaDa().toUpperCase()));
            horarioParaAtualizar.setHorario(LocalTime.parse(horarioDTO.getHorario()));
            horarioParaAtualizar.setDiaDaSemana(DiaDaSemana.valueOf(horarioDTO.getDiaDaSemana().toUpperCase()));

            // Chama o método 'update' do serviço com a entidade já atualizada, que espera um único argumento
            Horario horarioAtualizado = horarioService.update(horarioParaAtualizar);
            
            // Opcional: notificar sobre a atualização também
            notifyScheduleUpdate(horarioAtualizado.getSaidaDa(), horarioAtualizado.getDiaDaSemana());
            
            return ResponseEntity.ok(horarioService.convertToDTO(horarioAtualizado));
        } catch (Exception e) {
            System.err.println("Erro ao atualizar horário: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro ao atualizar horário: " + e.getMessage());
        }
    }


    // --- MÉTODO AUXILIAR PARA WebSocket ---

    /**
     * Busca a lista atualizada de horários e a envia para o tópico dinâmico.
     */
    private void notifyScheduleUpdate(SaidaDa saidaDa, DiaDaSemana diaDaSemana) {
        List<HorarioDTO> horariosAtualizados = horarioService.findByOrigemAndDia(saidaDa, diaDaSemana)
                .stream()
                .map(horarioService::convertToDTO)
                .collect(Collectors.toList());
        
        String topic = String.format("/topic/schedules/%s/%s", saidaDa.name(), diaDaSemana.name());
        
        System.out.println("Enviando atualização de " + horariosAtualizados.size() + " horários para o tópico: " + topic);
        
        messagingTemplate.convertAndSend(topic, horariosAtualizados);
    }
}
