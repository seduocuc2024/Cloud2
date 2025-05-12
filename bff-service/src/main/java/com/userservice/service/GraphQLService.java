package com.userservice.service;

import com.userservice.dto.RoleDTO;
import com.userservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GraphQLService {

    private final RestTemplate restTemplate;
    private final WebClient.Builder webClientBuilder;
    private final String userFunctionGraphqlUrl;
    private final String roleFunctionGraphqlUrl;

    public GraphQLService(
            RestTemplate restTemplate,
            WebClient.Builder webClientBuilder,
            @Value("${user.function.graphql.url}") String userFunctionGraphqlUrl,
            @Value("${role.function.graphql.url}") String roleFunctionGraphqlUrl) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
        this.userFunctionGraphqlUrl = userFunctionGraphqlUrl;
        this.roleFunctionGraphqlUrl = roleFunctionGraphqlUrl;
    }

    // Método para ejecutar consultas GraphQL utilizando RestTemplate
    public Map<String, Object> executeGraphQLQuerySync(String url, String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query);
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        
        return restTemplate.postForObject(url, entity, Map.class);
    }
    
    // Método para ejecutar consultas GraphQL utilizando WebClient (asíncrono)
    public Mono<Object> executeGraphQLQuery(String url, String query) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("query", query);
        
        return webClientBuilder.build()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Object.class);
    }
    
    // Método genérico para ejecutar consultas GraphQL en el servicio de usuarios
    public Mono<Object> executeUserGraphQL(String query) {
        return executeGraphQLQuery(userFunctionGraphqlUrl, query);
    }
    
    // Método genérico para ejecutar consultas GraphQL en el servicio de roles
    public Mono<Object> executeRoleGraphQL(String query) {
        return executeGraphQLQuery(roleFunctionGraphqlUrl, query);
    }

    // Métodos para las consultas específicas (sincrónicos)
    public List<UserDTO> getAllUsersGraphQL() {
        String query = "{ users { id username email roleId } }";
        Map<String, Object> response = executeGraphQLQuerySync(userFunctionGraphqlUrl, query);
        
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        List<Map<String, Object>> users = (List<Map<String, Object>>) data.get("users");
        
        // Convertir a DTOs
        return users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public List<RoleDTO> getAllRolesGraphQL() {
        String query = "{ roles { id name description } }";
        Map<String, Object> response = executeGraphQLQuerySync(roleFunctionGraphqlUrl, query);
        
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        List<Map<String, Object>> roles = (List<Map<String, Object>>) data.get("roles");
        
        // Convertir a DTOs
        return roles.stream()
                .map(this::convertToRoleDTO)
                .collect(Collectors.toList());
    }
    
    // Métodos auxiliares para convertir Maps a DTOs
    private UserDTO convertToUserDTO(Map<String, Object> userMap) {
        UserDTO dto = new UserDTO();
        dto.setId(Long.valueOf(userMap.get("id").toString()));
        dto.setUsername((String) userMap.get("username"));
        dto.setEmail((String) userMap.get("email"));
        if (userMap.get("roleId") != null) {
            dto.setRoleId(Long.valueOf(userMap.get("roleId").toString()));
        }
        return dto;
    }
    
    private RoleDTO convertToRoleDTO(Map<String, Object> roleMap) {
        RoleDTO dto = new RoleDTO();
        dto.setId(Long.valueOf(roleMap.get("id").toString()));
        dto.setName((String) roleMap.get("name"));
        dto.setDescription((String) roleMap.get("description"));
        return dto;
    }
}