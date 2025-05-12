package com.userservice.service;

import com.userservice.dto.UserDTO;
import com.userservice.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;


import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private RoleService roleService;
    
    @Value("${spring.app.function.user.url}")
    private String userFunctionUrl;
    
    public List<UserDTO> getAllUsers() {
        try {
            logger.info("Obteniendo todos los usuarios desde: {}", userFunctionUrl);
            
            // Obtener la respuesta como texto plano
            String response = restTemplate.getForObject(userFunctionUrl, String.class);
            logger.info("Respuesta recibida: {}", response);
            
            if (response == null || response.isEmpty()) {
                logger.warn("La respuesta está vacía");
                return Collections.emptyList();
            }
            
            // Si la respuesta no está vacía, intenta convertirla a objetos UserDTO
            try {
                ObjectMapper mapper = new ObjectMapper();
                UserDTO[] users = mapper.readValue(response, UserDTO[].class);
                logger.info("Usuarios convertidos correctamente: {}", users.length);
                return Arrays.asList(users);
            } catch (Exception e) {
                logger.error("Error al convertir la respuesta a objetos UserDTO: {}", e.getMessage(), e);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    
    public UserDTO getUserById(Long id) {
        return restTemplate.getForObject(userFunctionUrl + "/" + id, UserDTO.class);
    }
    
    public UserDTO createUser(UserDTO userDTO) throws Exception {
        try {
            logger.info("Intentando crear usuario: {}", userDTO);
            
            // Log del JSON que se va a enviar
            ObjectMapper mapper = new ObjectMapper();
            logger.info("JSON enviado: {}", mapper.writeValueAsString(userDTO));
            
            UserDTO createdUser = restTemplate.postForObject(userFunctionUrl, userDTO, UserDTO.class);
            logger.info("Usuario creado correctamente: {}", createdUser);
            return createdUser;
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage(), e);
            throw e;
        }
    }
    
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        restTemplate.put(userFunctionUrl + "/" + id, userDTO);
        return getUserById(id);
    }
    
    // Método para obtener un usuario con sus roles
    public UserDTO getUserWithRoles(Long userId) {
        UserDTO user = getUserById(userId);
        List<RoleDTO> userRoles = roleService.getRolesByUserId(userId);
        user.setRoles(userRoles);
        return user;
    }
    
    // Método para obtener todos los usuarios con sus roles
    public List<UserDTO> getAllUsersWithRoles() {
        List<UserDTO> users = getAllUsers();
        
        // Para cada usuario, obtenemos sus roles
        for (UserDTO user : users) {
            List<RoleDTO> userRoles = roleService.getRolesByUserId(user.getId());
            user.setRoles(userRoles);
        }
        
        return users;
    }
    
    // Método para crear un usuario con roles
    public UserDTO createUserWithRoles(UserDTO userDTO, List<Long> roleIds) throws Exception {
        // Primero creamos el usuario
        UserDTO createdUser = createUser(userDTO);
        
        // Luego asignamos los roles
        for (Long roleId : roleIds) {
            roleService.assignRoleToUser(createdUser.getId(), roleId);
        }
        
        return getUserWithRoles(createdUser.getId());
    }
}