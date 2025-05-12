package com.userservice.service;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    public void processEvent(String eventType, String subject, JsonObject data) {
        // Procesar según el tipo de evento
        switch (eventType) {
            case "User.Created":
                handleUserCreated(data);
                break;
            case "User.Updated":
                handleUserUpdated(data);
                break;
            case "User.Deleted":
                handleUserDeleted(data);
                break;
            case "Role.Created":
                handleRoleCreated(data);
                break;
            case "Role.Updated":
                handleRoleUpdated(data);
                break;
            case "Role.Deleted":
                handleRoleDeleted(data);
                break;
            default:
                logger.warn("Tipo de evento no reconocido: {}", eventType);
        }
    }
    
    private void handleUserCreated(JsonObject data) {
        // Extraer datos del usuario
        Long userId = data.get("id").getAsLong();
        String username = data.get("username").getAsString();
        String email = data.get("email").getAsString();
        Long roleId = data.has("roleId") ? data.get("roleId").getAsLong() : null;
        
        logger.info("NOTIFICACIÓN: Nuevo usuario creado - ID: {}, Username: {}, Email: {}, RoleId: {}", 
                    userId, username, email, roleId);
        
        // Implementar lógica de notificación
        sendWelcomeEmail(email, username);
    }
    
    private void handleUserUpdated(JsonObject data) {
        Long userId = data.get("id").getAsLong();
        String username = data.get("username").getAsString();
        
        logger.info("NOTIFICACIÓN: Usuario actualizado - ID: {}, Username: {}", userId, username);
        
        // Implementar lógica específica para actualización de usuarios
    }
    
    private void handleUserDeleted(JsonObject data) {
        Long userId = data.get("id").getAsLong();
        
        logger.info("NOTIFICACIÓN: Usuario eliminado - ID: {}", userId);
        
        // Implementar lógica específica para eliminación de usuarios
    }
    
    private void handleRoleCreated(JsonObject data) {
        Long roleId = data.get("id").getAsLong();
        String roleName = data.get("name").getAsString();
        
        logger.info("NOTIFICACIÓN: Nuevo rol creado - ID: {}, Nombre: {}", roleId, roleName);
        
        // Implementar lógica específica para creación de roles
    }
    
    private void handleRoleUpdated(JsonObject data) {
        Long roleId = data.get("id").getAsLong();
        String roleName = data.get("name").getAsString();
        
        logger.info("NOTIFICACIÓN: Rol actualizado - ID: {}, Nombre: {}", roleId, roleName);
        
        // Implementar lógica específica para actualización de roles
    }
    
    private void handleRoleDeleted(JsonObject data) {
        Long roleId = data.get("id").getAsLong();
        
        logger.info("NOTIFICACIÓN: Rol eliminado - ID: {}", roleId);
        
        // Implementar lógica específica para eliminación de roles
    }
    
    private void sendWelcomeEmail(String email, String username) {
        // En un entorno real, aquí implementarías la integración con un servicio de email
        logger.info("Simulando envío de email a: {}\nAsunto: Bienvenido a nuestro sistema\nCuerpo: Hola {}, ¡bienvenido a nuestro sistema!", 
                   email, username);
    }
}
