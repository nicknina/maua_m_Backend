package maua.moove.backend.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import maua.moove.backend.demo.dto.NotificationPayload;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationPayload notification) {
        try {
            // Log para verificar se a notificação foi recebida no backend
            System.out.println("Enviando notificação para o tópico /topic/notifications: " + notification.getTitle());

            // Envia a notificação para o tópico do WebSocket.
            // Todos os clientes do app inscritos neste tópico receberão esta mensagem.
            messagingTemplate.convertAndSend("/topic/notifications", notification);
            
            return ResponseEntity.ok().body("Notificação enviada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação via WebSocket: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erro ao processar a notificação.");
        }
    }
}