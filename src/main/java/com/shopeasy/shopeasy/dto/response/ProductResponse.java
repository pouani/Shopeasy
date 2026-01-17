package com.shopeasy.shopeasy.dto.response;

import com.shopeasy.shopeasy.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long stock;
    private String categoryName;
}
