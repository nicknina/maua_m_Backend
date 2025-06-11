package maua.moove.backend.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import maua.moove.backend.demo.model.Notification;
import maua.moove.backend.demo.repository.NotificationRepository;
import maua.moove.backend.demo.repository.UserRepository;
import maua.moove.backend.demo.repository.VanRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private VanRepository vanRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalVans", vanRepository.count());
        stats.put("vansAtivas", vanRepository.countByAtivo(true));
        stats.put("totalUsuarios", userRepository.count());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getRecentNotifications() {
      
        return ResponseEntity.ok(notificationRepository.findTop5ByOrderByCreatedAtDesc());
    }

    @GetMapping("/activity")
    public ResponseEntity<List<Map<String, Object>>> getActivityData() {
       
        List<Map<String, Object>> activityData = new ArrayList<>();
        Random random = new Random();
        long totalUsuarios = userRepository.count() > 0 ? userRepository.count() : 10;

        for (int i = 6; i >= 0; i--) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", LocalDate.now().minusDays(i));
          
            dayData.put("activeUsers", random.nextInt((int) totalUsuarios + 1));
            activityData.add(dayData);
        }
        return ResponseEntity.ok(activityData);
    }
}
