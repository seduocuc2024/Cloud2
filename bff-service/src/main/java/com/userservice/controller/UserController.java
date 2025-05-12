package com.userservice.controller;

import com.userservice.dto.UserDTO;
import com.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
    try {
        UserDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    } catch (Exception e) {
        // Log del error
        // Si tienes un logger configurado, úsalo así:
        // logger.error("Error al crear usuario: {}", e.getMessage(), e);
        
        // Para una mejor respuesta al cliente, puedes incluir el mensaje de error
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(null); // o puedes crear un DTO de error con más detalles
    }
}

@PutMapping("/{id}")
public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
    try {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
    } catch (Exception e) {
        // Log del error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    // Nuevos endpoints que combinan información de usuarios y roles
    
    @GetMapping("/with-roles/{id}")
    public ResponseEntity<UserDTO> getUserWithRoles(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserWithRoles(id));
    }
    
    @GetMapping("/with-roles")
    public ResponseEntity<List<UserDTO>> getAllUsersWithRoles() {
        return ResponseEntity.ok(userService.getAllUsersWithRoles());
    }
    
    @PostMapping("/with-roles")
public ResponseEntity<UserDTO> createUserWithRoles(
        @RequestBody UserDTO userDTO,
        @RequestParam List<Long> roleIds) {
    try {
        return new ResponseEntity<>(userService.createUserWithRoles(userDTO, roleIds), HttpStatus.CREATED);
    } catch (Exception e) {
        // Log del error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
}