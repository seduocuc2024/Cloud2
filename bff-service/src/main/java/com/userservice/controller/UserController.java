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
        return new ResponseEntity<>(userService.createUser(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userDTO));
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
        return new ResponseEntity<>(userService.createUserWithRoles(userDTO, roleIds), HttpStatus.CREATED);
    }
}