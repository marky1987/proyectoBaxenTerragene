package com.terragene.baxen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDTO {
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Password")
    private String password;
    @JsonProperty("DomainDist")
    private String domainDist;

    public LoginDTO(String email, String password, String domainDist) {
        this.email = email;
        this.password = password;
        this.domainDist = domainDist;
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

    public String getDomainDist() {
        return domainDist;
    }

    public void setDomainDist(String domainDist) {
        this.domainDist = domainDist;
    }
}
