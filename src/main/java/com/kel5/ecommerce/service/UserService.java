
package com.kel5.ecommerce.service;

import com.kel5.ecommerce.dto.UserDto;
import com.kel5.ecommerce.entity.Cart;
import com.kel5.ecommerce.entity.User;

import java.util.List;


public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
    UserDto findUserDtoByEmail(String email);

    User findUserByName(String name);

    boolean forgotPassword(String email);

    void changePassword(String email, String newPassword);

    public List<UserDto> getUsers();
    public List<UserDto> getUsersUser();

    User getUserLogged();

    void confirmEmail(String confirmationToken);
    
    public Cart getUserCart();
}
