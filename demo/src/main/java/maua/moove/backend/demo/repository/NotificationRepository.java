package maua.moove.backend.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import maua.moove.backend.demo.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Busca as 5 notificações mais recentes, ordenadas pela data de criação
    List<Notification> findTop5ByOrderByCreatedAtDesc();
}