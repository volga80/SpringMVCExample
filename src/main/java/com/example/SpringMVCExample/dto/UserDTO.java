package com.example.SpringMVCExample.dto;

import com.example.SpringMVCExample.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {
    @JsonView(Views.UserSummary.class)
    private String name;
    @JsonView(Views.UserSummary.class)
    private String email;
    private String password;
    @JsonView(Views.UserDetails.class)
    private List<OrderDTO> orders;
}
