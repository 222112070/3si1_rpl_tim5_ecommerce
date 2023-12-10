/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.kel5.ecommerce.repository;

import com.kel5.ecommerce.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User findByEmail(String email);

    User findByName(String name);

    User findByEmailIgnoreCase(String emailId);

    Boolean existsByEmail(String email);
    
    List<User> findByRoles_Name(String roleName);
    
    List<User> findByRoles_NameAndType(String roleName, String type);

}
