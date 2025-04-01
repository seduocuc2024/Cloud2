package com.userservice.dto;

import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Long roleId;
    private List<RoleDTO> roles;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getRoleId() {
        return roleId;
    }
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
    public List<RoleDTO> getRoles() {
        return roles;
    }
    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
    
}
