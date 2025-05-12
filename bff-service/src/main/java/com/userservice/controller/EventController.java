package com.userservice.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.EventGridTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.userservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    
    private final NotificationService notificationService;
    
    @Autowired
    public EventController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @FunctionName("BffEventProcessor")
    public void processEvents(
        @EventGridTrigger(name = "event") String content,
        final ExecutionContext context) {
        
        context.getLogger().info("BFF recibió evento: " + content);
        
        try {
            // Parsear el contenido del evento
            JsonObject eventJson = JsonParser.parseString(content).getAsJsonObject();
            
            // Extraer información del evento
            String eventType = eventJson.get("eventType").getAsString();
            String subject = eventJson.get("subject").getAsString();
            JsonObject dataJson = eventJson.get("data").getAsJsonObject();
            
            context.getLogger().info("BFF procesando evento tipo: " + eventType);
            
            // Delegar el procesamiento al servicio de notificaciones
            notificationService.processEvent(eventType, subject, dataJson);
            
        } catch (Exception e) {
            context.getLogger().severe("Error procesando evento en BFF: " + e.getMessage());
        }
    }
}
