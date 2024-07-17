package com.example.SpringMVCExample.controller;


import com.example.SpringMVCExample.domain.User;
import com.example.SpringMVCExample.dto.UserDTO;
import com.example.SpringMVCExample.service.UserService;
import com.example.SpringMVCExample.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/new")
    public ResponseEntity<User> addNewUser(@RequestBody UserDTO userDTO) {
        if(userDTO.getName() == null || userDTO.getEmail() == null){
            return (ResponseEntity<User>) ResponseEntity.badRequest();
        }
        User newUser = userService.save(userDTO);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUserEmail(@RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateProfile(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/remove")
    public ResponseEntity removingUserByName(@RequestBody String username) {
        return ResponseEntity.ok(userService.delete(username));
    }

    @GetMapping("/userInfo/{id}")
    @JsonView(Views.UserDetails.class)
    public ResponseEntity<UserDTO> getInfoByUsername(@PathVariable("id") Long userId) {
        UserDTO userDto = userService.getUserWithOrders(userId);
        return ResponseEntity.ok(userDto);
    }
}
