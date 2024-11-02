package com.alhas.airbnb.user.application.dto;

import java.util.Set;
import java.util.UUID;

//public record ReadUserDTO(
//        UUID publicId,
//        String firstName,
//        String lastName,
//        String email,
//        String imageUrl,
//        Set<String> authorities
//) {
//}

public class ReadUserDTO {
    private UUID publicId;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    Set<String> authorities;

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public UUID getPublicI() {
        return publicId;
    }

    public void setPublicI(UUID publicI) {
        this.publicId = publicI;
    }
}
