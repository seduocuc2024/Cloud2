package com.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userservice.dto.RoleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${spring.app.function.role.url}")
    private String roleFunctionUrl;
    
    public List<RoleDTO> getAllRoles() {
    try {
        logger.info("Obteniendo todos los roles desde: {}", roleFunctionUrl);
        
        // Obtener la respuesta como texto plano
        String response = restTemplate.getForObject(roleFunctionUrl, String.class);
        logger.info("Respuesta recibida: {}", response);
        
        if (response == null || response.isEmpty()) {
            logger.warn("La respuesta está vacía");
            return Collections.emptyList();
        }
        
        // Si la respuesta no está vacía, intenta convertirla a objetos RoleDTO
        try {
            ObjectMapper mapper = new ObjectMapper();
            RoleDTO[] roles = mapper.readValue(response, RoleDTO[].class);
            logger.info("Roles convertidos correctamente: {}", roles.length);
            return Arrays.asList(roles);
        } catch (Exception e) {
            logger.error("Error al convertir la respuesta a objetos RoleDTO: {}", e.getMessage(), e);
            
            // Si hay un error en la conversión, devuelve un rol de prueba para debug
            List<RoleDTO> testRoles = new ArrayList<>();
            RoleDTO testRole = new RoleDTO();
            testRole.setId(1L);
            testRole.setName("Test Role");
            testRole.setDescription("Role for testing");
            testRoles.add(testRole);
            return testRoles;
        }
    } catch (Exception e) {
        logger.error("Error al obtener roles: {}", e.getMessage(), e);
        return Collections.emptyList();
    }
}
    
    public RoleDTO getRoleById(Long id) {
        return restTemplate.getForObject(roleFunctionUrl + "/" + id, RoleDTO.class);
    }
    
    public RoleDTO createRole(RoleDTO roleDTO) {
        return restTemplate.postForObject(roleFunctionUrl, roleDTO, RoleDTO.class);
    }
    
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        restTemplate.put(roleFunctionUrl + "/" + id, roleDTO);
        return getRoleById(id);
    }
    
    // Método para obtener los roles de un usuario específico
    @SuppressWarnings("unchecked")
    public List<RoleDTO> getRolesByUserId(Long userId) {
        return restTemplate.getForObject(roleFunctionUrl + "/user/" + userId, List.class);
    }
    
    // Método para asignar un rol a un usuario
    public void assignRoleToUser(Long userId, Long roleId) {
        restTemplate.postForObject(roleFunctionUrl + "/assign/" + userId + "/" + roleId, null, Void.class);
    }

    
}
