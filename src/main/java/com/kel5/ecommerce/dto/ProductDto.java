package com.kel5.ecommerce.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String name;
    private String description;
    private float price;
    private int stock;
    private float weight;
    private String status;
    private List<MultipartFile> images;
}
