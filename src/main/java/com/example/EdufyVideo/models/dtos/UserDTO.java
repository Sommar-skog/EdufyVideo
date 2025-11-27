package com.example.EdufyVideo.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long id;
    private String uuid;
    private String username;
    private String email;
    private Boolean creator;
    private Boolean active;

    public UserDTO() {

    }
    public UserDTO(Long id, String uuid, String username, String email, Boolean creator, Boolean active) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.creator = creator;
        this.active = active;
    }

    public UserDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Boolean getCreator() {
        return creator;
    }

    public void setCreator(Boolean creator) {
        this.creator = creator;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", creator=" + creator +
                ", active=" + active +
                '}';
    }
}
