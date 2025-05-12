package com.userservice.controller;

import com.userservice.dto.RoleDTO;
import com.userservice.dto.UserDTO;
import com.userservice.service.GraphQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

@RestController
@RequestMapping("/api/graphql")
public class GraphQLController {
    
    private static final Logger logger = Logger.getLogger(GraphQLController.class.getName());

    @Autowired
    private GraphQLService graphQLService;

    // Endpoint para consultas GraphQL genéricas con manejo de errores mejorado
    @PostMapping
    public Mono<Object> executeGraphQL(@RequestBody Map<String, String> request) {
        try {
            String query = request.get("query");
            logger.info("Recibida consulta GraphQL: " + query);
            
            // Lógica mejorada para decidir si es una consulta de usuarios o roles
            if (query.contains("users") || query.contains("User")) {
                logger.info("Dirigiendo al servicio de usuarios");
                return graphQLService.executeUserGraphQL(query)
                    .doOnError(error -> logger.log(Level.SEVERE, "Error en consulta de usuarios: " + error.getMessage(), error));
            } else if (query.contains("roles") || query.contains("Role")) {
                logger.info("Dirigiendo al servicio de roles");
                return graphQLService.executeRoleGraphQL(query)
                    .doOnError(error -> logger.log(Level.SEVERE, "Error en consulta de roles: " + error.getMessage(), error));
            } else {
                // Por defecto, enviar a usuarios pero con advertencia
                logger.warning("No se pudo determinar el tipo de consulta, dirigiendo por defecto a usuarios");
                return graphQLService.executeUserGraphQL(query)
                    .doOnError(error -> logger.log(Level.SEVERE, "Error en consulta por defecto: " + error.getMessage(), error));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al procesar la consulta GraphQL: " + e.getMessage(), e);
            return Mono.error(e);
        }
    }
    
    // Endpoint específico para usuarios con manejo de errores
    @PostMapping("/users")
    public Mono<Object> executeUserGraphQL(@RequestBody Map<String, String> request) {
        try {
            String query = request.get("query");
            logger.info("Ejecutando consulta de usuario: " + query);
            return graphQLService.executeUserGraphQL(query)
                .doOnError(error -> logger.log(Level.SEVERE, "Error en endpoint de usuarios: " + error.getMessage(), error));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al procesar consulta de usuarios: " + e.getMessage(), e);
            return Mono.error(e);
        }
    }
    
    // Endpoint específico para roles con manejo de errores
    @PostMapping("/roles")
    public Mono<Object> executeRoleGraphQL(@RequestBody Map<String, String> request) {
        try {
            String query = request.get("query");
            logger.info("Ejecutando consulta de rol: " + query);
            return graphQLService.executeRoleGraphQL(query)
                .doOnError(error -> logger.log(Level.SEVERE, "Error en endpoint de roles: " + error.getMessage(), error));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al procesar consulta de roles: " + e.getMessage(), e);
            return Mono.error(e);
        }
    }

    // Endpoints GET para consultas específicas con manejo de errores
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsersGraphQL() {
        try {
            return ResponseEntity.ok(graphQLService.getAllUsersGraphQL());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener todos los usuarios: " + e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRolesGraphQL() {
        try {
            return ResponseEntity.ok(graphQLService.getAllRolesGraphQL());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al obtener todos los roles: " + e.getMessage(), e);
            throw e;
        }
    }
}