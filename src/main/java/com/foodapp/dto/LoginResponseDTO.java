package com.foodapp.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class LoginResponseDTO {

    private String token;
    private String email;
    private Collection<?extends GrantedAuthority> collection;

    // Constructor, getters, and setters
    public LoginResponseDTO(String token, String email, Collection<? extends GrantedAuthority> collection) {
        this.token = token;
        this.email = email;
        this.collection=collection;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public Collection<? extends GrantedAuthority> getCollection() {
		return collection;
	}

	public void setCollection(Collection<? extends GrantedAuthority> collection) {
		this.collection = collection;
	}
    
}
