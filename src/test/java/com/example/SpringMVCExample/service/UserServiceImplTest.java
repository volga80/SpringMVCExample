package com.example.SpringMVCExample.service;

import com.example.SpringMVCExample.dao.UserRepository;
import com.example.SpringMVCExample.domain.User;
import com.example.SpringMVCExample.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser;
    private UserDTO newUserDTO;

    @BeforeEach
    void setUp() {
        existingUser = new User(1L, "existingUser",
                "existingPassword", "existing@example.com", null);
        newUserDTO = new UserDTO("newUser", "new@example.com",
                "newPassword", null);
    }

    @Test
    void save_NewUser_ShouldSaveUser() {
        UserDTO newUserDTO = new UserDTO("newUser", "newPassword",
                "new@example.com", null);

        when(userRepository.findFirstByName(newUserDTO.getName())).thenReturn(null);

        User savedUser = User.builder()
                .name(newUserDTO.getName())
                .email(newUserDTO.getEmail())
                .password(newUserDTO.getPassword())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User returnedUser = userService.save(newUserDTO);

        assertNotNull(returnedUser);
        assertEquals(newUserDTO.getName(), returnedUser.getName());
        assertEquals(newUserDTO.getEmail(), returnedUser.getEmail());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void save_ExistingUser_ShouldThrowException() {
        when(userRepository.findFirstByName(existingUser.getName())).thenReturn(existingUser);

        assertThrows(RuntimeException.class, () -> userService.save(
                new UserDTO(existingUser.getName(), "password", "email", null)));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateProfile_ExistingUserAndCorrectPassword_ShouldUpdateUser() {
        when(userRepository.findFirstByName(existingUser.getName())).thenReturn(existingUser);

        UserDTO updatedUserDTO = new UserDTO(
                existingUser.getName(), existingUser.getPassword(), "existingPassword", null);

        User updatedUser = userService.updateProfile(updatedUserDTO);

        assertNotNull(updatedUser);
        assertEquals(updatedUserDTO.getEmail(), updatedUser.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void updateProfile_UserNotFound_ShouldThrowException() {
        when(userRepository.findFirstByName("nonExistingUser")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.updateProfile(
                new UserDTO("nonExistingUser", "password", "email", null)));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void delete_ExistingUser_ShouldDeleteUser() {
        when(userRepository.findFirstByName(existingUser.getName())).thenReturn(existingUser);

        boolean result = userService.delete(existingUser.getName());

        assertTrue(result);
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void delete_UserNotFound_ShouldThrowException() {
        when(userRepository.findFirstByName("nonExistingUser")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.delete("nonExistingUser"));
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "user1", "password1", "user1@example.com", null));
        users.add(new User(2L, "user2", "password2", "user2@example.com", null));

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOs = userService.getAllUsers();

        assertNotNull(userDTOs);
        assertEquals(users.size(), userDTOs.size());
        assertEquals(users.get(0).getName(), userDTOs.get(0).getName());
        assertEquals(users.get(1).getName(), userDTOs.get(1).getName());
    }

    @Test
    void getUserWithOrders_ExistingUser_ShouldReturnUserDTOWithOrders() {
        existingUser.setOrdersList(new ArrayList<>());

        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));

        UserDTO userDTO = userService.getUserWithOrders(existingUser.getId());

        assertNotNull(userDTO);
        assertEquals(existingUser.getName(), userDTO.getName());
        assertEquals(existingUser.getEmail(), userDTO.getEmail());
        assertTrue(userDTO.getOrders().isEmpty());
    }

    @Test
    void getUserWithOrders_UserNotFound_ShouldThrowException() {
        long nonExistingUserId = 100L;
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserWithOrders(nonExistingUserId));
    }

}