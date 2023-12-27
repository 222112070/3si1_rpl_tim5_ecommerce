package com.kel5.ecommerce.entity;

/**
 *
 * @author asus
 */

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subcategory")
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean status = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
