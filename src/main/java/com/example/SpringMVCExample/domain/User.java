package com.example.SpringMVCExample.domain;

import com.example.SpringMVCExample.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    private static final String SEQ_NAME = "user_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private long id;
    @JsonView(Views.UserSummary.class)
    private String name;
    private String password;
    @Email
    @JsonView(Views.UserSummary.class)
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonView(Views.UserDetails.class)
    private List<Order> ordersList = new ArrayList<>();
}
