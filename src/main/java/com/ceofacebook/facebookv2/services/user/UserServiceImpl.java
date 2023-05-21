package com.ceofacebook.facebookv2.services.user;

import com.ceofacebook.facebookv2.dtos.user.UserDto;
import com.ceofacebook.facebookv2.entities.User;
import com.ceofacebook.facebookv2.exceptions.InvalidException;
import com.ceofacebook.facebookv2.exceptions.NotFoundException;
import com.ceofacebook.facebookv2.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> filter(String name, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<User> pageUsers;

        if (name != null) {
            pageUsers = userRepository.findByName(name, pageable);
        } else {
            pageUsers = userRepository.findAll(pageable);
        }

        return pageUsers;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist", id)));
    }

    @Override
    public User createUser(UserDto dto, Principal principal) {

        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new InvalidException("Email is required");
        }

        if (userRepository.getUser(dto.getEmail()).isPresent()) {
            throw new InvalidException(String.format("User with email %s already exist", dto.getEmail()));
        }

        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new InvalidException("Password is required");
        }

        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new InvalidException("Name is required");
        }

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            dto.setRoles(List.of("ROLE_USER"));
        } else {
            User AsUser = userRepository.getUser(principal.getName())
                    .orElseThrow(() -> new NotFoundException(String.format("User with email %s does not exist", principal.getName())));
            if (!AsUser.getRoles().contains("ROLE_ADMIN")) {
                throw new InvalidException("You don't have permission to create admin user");
            }
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setRoles(dto.getRoles());
        user.setPassword(dto.getPassword());

        return userRepository.save(user);
    }

    @Override
    public User updateUser(String id, UserDto dto, Principal principal) {
        User AsUser = userRepository.getUser(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s does not exist", principal.getName())));
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist", id)));
        String username = principal.getName();
        if (!user.getEmail().equals(username) && !AsUser.getRoles().contains("ROLE_ADMIN")) {
            throw new InvalidException("You don't have permission to update this user");
        }

        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new InvalidException("Email is required");
        }

        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new InvalidException("Password is required");
        }

        if (dto.getName() == null || dto.getName().isEmpty()) {
            throw new InvalidException("Name is required");
        }

        if (dto.getRoles() == null || dto.getRoles().isEmpty()) {
            dto.setRoles(List.of("ROLE_USER"));
        }

        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setRoles(dto.getRoles());
        user.setPassword(dto.getPassword());

        return userRepository.save(user);
    }

    @Override
    public User deleteUser(String id, Principal principal) {
        User AsUser = userRepository.getUser(principal.getName())
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s does not exist", principal.getName())));
        User user = userRepository.getUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist", id)));
        String username = principal.getName();
        if (!user.getEmail().equals(username) && !AsUser.getRoles().contains("ROLE_ADMIN")) {
            throw new InvalidException("You don't have permission to delete this user");
        }

        userRepository.delete(user);

        return user;
    }
}
