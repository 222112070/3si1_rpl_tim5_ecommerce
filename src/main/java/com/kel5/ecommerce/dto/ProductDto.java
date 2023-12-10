package com.kel5.ecommerce.dto;

import com.kel5.ecommerce.entity.Category;
import com.kel5.ecommerce.entity.Subcategory;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private float price;
    private int stock;
    private float weight;
    private String status;
    private Category category;
    private Subcategory subcategory;
    private List<MultipartFile> images;
}
