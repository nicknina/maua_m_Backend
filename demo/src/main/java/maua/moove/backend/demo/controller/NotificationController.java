package maua.moove.backend.demo.controller;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import maua.moove.backend.demo.model.Notification;
import maua.moove.backend.demo.repository.NotificationRepository;

// DTO para representar a notificação (pode ser movido para seu próprio arquivo)
class NotificationPayload {
    private String title;
    private String description;
    private String type;
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody NotificationPayload payload) {
        try {
            
            Notification notification = new Notification();
            notification.setTitle(payload.getTitle());
            notification.setDescription(payload.getDescription());
            notification.setType(payload.getType());
            notification.setCreatedAt(LocalDateTime.now());
            notificationRepository.save(notification);

         
            messagingTemplate.convertAndSend("/topic/notifications", payload);
            
            return ResponseEntity.ok().body("Notificação enviada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar a notificação.");
        }
    }
}