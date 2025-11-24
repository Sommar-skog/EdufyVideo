package com.example.EdufyVideo.clients;

import com.example.EdufyVideo.models.dtos.UserDTO;

//ED-345-AA
public interface UserClient {
    public UserDTO getUserBySub(String sub);
}
