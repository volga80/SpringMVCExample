package com.example.SpringMVCExample.dto;

import com.example.SpringMVCExample.domain.Product;
import com.example.SpringMVCExample.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderDTO {
    private Long id;
    @JsonView(Views.UserDetails.class)
    private BigDecimal sum;
    @JsonView(Views.UserDetails.class)
    private List<Product> productsList;
}
