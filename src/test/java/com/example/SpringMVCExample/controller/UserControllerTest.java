package com.example.SpringMVCExample.controller;

import com.example.SpringMVCExample.domain.User;
import com.example.SpringMVCExample.dto.UserDTO;
import com.example.SpringMVCExample.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_ReturnsUserList_Success() throws Exception {
        List<UserDTO> users = Arrays.asList(
                new UserDTO("user1", "password1", "user1@example.com", null),
                new UserDTO("user2", "password2", "user2@example.com", null)
        );
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(users.size()));
    }

    @Test
    void addNewUser_ValidUser_ReturnsUser_Success() throws Exception {
        UserDTO newUserDTO = new UserDTO("newUser", "newPassword", "new@example.com", null);
        User newUser = new User(1L, newUserDTO.getName(), newUserDTO.getPassword(), newUserDTO.getEmail(), null);
        when(userService.save(any(UserDTO.class))).thenReturn(newUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/new")
                        .content(objectMapper.writeValueAsString(newUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newUserDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(newUserDTO.getEmail()));
    }

    @Test
    void updateUserEmail_ValidUser_ReturnsUpdatedUser_Success() throws Exception {
        UserDTO updatedUserDTO = new UserDTO("userToUpdate", "updatedPassword", "updated@example.com", null);
        User updatedUser = new User(1L, updatedUserDTO.getName(), updatedUserDTO.getPassword(), updatedUserDTO.getEmail(), null);
        when(userService.updateProfile(any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/update")
                        .content(objectMapper.writeValueAsString(updatedUserDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(updatedUserDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(updatedUserDTO.getEmail()));
    }

    @Test
    void removingUserByName_ValidUsername_ReturnsOk_Success() throws Exception {
        String username = "userToDelete";
        when(userService.delete(username)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/remove")
                        .content(username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}