package com.example.SpringMVCExample.service;

import com.example.SpringMVCExample.domain.User;
import com.example.SpringMVCExample.dto.UserDTO;

import java.util.List;

public interface UserService {
    User save(UserDTO userDTO);

    User updateProfile(UserDTO userDTO);

    boolean delete(String username);

    List<UserDTO> getAllUsers();

    UserDTO getUserWithOrders(Long userId);
}
