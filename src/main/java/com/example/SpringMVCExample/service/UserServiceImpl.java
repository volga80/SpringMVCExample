package com.example.SpringMVCExample.service;

import com.example.SpringMVCExample.dao.UserRepository;
import com.example.SpringMVCExample.domain.User;
import com.example.SpringMVCExample.dto.OrderDTO;
import com.example.SpringMVCExample.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User save(UserDTO userDTO) {
        if (userRepository.findFirstByName(userDTO.getName()) != null) {
            throw new RuntimeException("Пользватель с таким именем уже есть");
        }
        User user = User.builder()
                .name(userDTO.getName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();
        userRepository.save(user);
        return user;
    }

    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    @Override
    public User updateProfile(UserDTO dto) {
        User savedUser = userRepository.findFirstByName(dto.getName());
        if (savedUser == null) {
            throw new RuntimeException("Пользователя нет" + dto.getName());
        }
        if (!dto.getPassword().equals(savedUser.getPassword())) {
            throw new RuntimeException("Введен неверный пароль для пользователя");
        }
        boolean isChanged = false;
        if (!Objects.equals(dto.getEmail(), savedUser.getEmail())) {
            savedUser.setEmail(dto.getEmail());
            isChanged = true;
        }
        if (isChanged) {
            userRepository.save(savedUser);
        }
        return savedUser;
    }

    @Override
    public boolean delete(String username) {
        if (userRepository.findFirstByName(username) == null) {
            throw new RuntimeException("Пользвателя с таким именем нет");
        }
        userRepository.delete(userRepository.findFirstByName(username));
        return true;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserWithOrders(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Пользователя с таким id нет: " + userId);
        }

        User user = userOptional.get();
        List<OrderDTO> orders = user.getOrdersList().stream()
                .map(order -> new OrderDTO(order.getId(), order.getSum(), order.getOrderProducts()))
                .collect(Collectors.toList());

        return new UserDTO(user.getName(),user.getEmail(), user.getPassword(), orders);
    }
}
