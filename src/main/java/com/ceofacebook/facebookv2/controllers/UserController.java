package com.ceofacebook.facebookv2.controllers;

import com.ceofacebook.facebookv2.dtos.user.UserDto;
import com.ceofacebook.facebookv2.entities.User;
import com.ceofacebook.facebookv2.services.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Iterable<User>> getAllUser() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<User> create(@RequestBody UserDto DTO, Principal principal) {
        return new ResponseEntity<>(userService.createUser(DTO, principal), HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody UserDto DTO, Principal principal) {
        return new ResponseEntity<>(userService.updateUser(id, DTO, principal), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> delete(@PathVariable String id, Principal principal) {
        return new ResponseEntity<>(userService.deleteUser(id, principal), HttpStatus.OK);
    }

    @GetMapping("/user/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam(required = false) String name, @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size, @RequestParam String sort) {
        try {
            Page<User> pageUsers = userService.filter(name, page, size, sort);

            List<User> users = pageUsers.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("currentPage", pageUsers.getNumber() + 1);
            response.put("totalItems", pageUsers.getTotalElements());
            response.put("totalPages", pageUsers.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
