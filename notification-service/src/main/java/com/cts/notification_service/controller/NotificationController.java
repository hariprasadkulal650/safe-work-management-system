package com.cts.notification_service.controller;

import com.cts.notification_service.entity.Notification;
import com.cts.notification_service.enums.NotificationCategory;
import com.cts.notification_service.service.INotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(
            @PathVariable Long userId) {

        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @PostMapping("/internal/create")
    public ResponseEntity<String> createNotification(
            @RequestParam Long userId,
            @RequestParam Long entityId,
            @RequestParam String message,
            @RequestParam NotificationCategory category) {

        notificationService.createNotification(userId, entityId, message, category);
        return ResponseEntity.ok("Notification created");
    }

    @PutMapping("/markAsRead/{notificationId}")
    public ResponseEntity<String> markAsRead(@PathVariable Long notificationId) {

        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok("Notification marked as read");
    }
}