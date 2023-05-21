package com.ceofacebook.facebookv2.services.user;

import com.ceofacebook.facebookv2.dtos.user.UserDto;
import com.ceofacebook.facebookv2.entities.User;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface UserService {

    Page<User> filter(String search,
                      int page, int size, String sort);

    List<User> getAllUsers();

    User getUser(String id);

    User createUser(UserDto dto, Principal principal);

    User updateUser(String id, UserDto dto, Principal principal);

    User deleteUser(String id, Principal principal);
}
