package com.ceofacebook.facebookv2.controllers;

import com.ceofacebook.facebookv2.dtos.post.PostDto;
import com.ceofacebook.facebookv2.entities.Post;
import com.ceofacebook.facebookv2.services.post.PostService;
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
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPost(@PathVariable String id) {
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }

    @GetMapping("/post")
    public ResponseEntity<Iterable<Post>> getAllPost() {
        return new ResponseEntity<>(postService.getAllPosts(), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<Post> create(@RequestBody PostDto DTO, Principal principal) {
        return new ResponseEntity<>(postService.createPost(DTO, principal), HttpStatus.OK);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody PostDto DTO, Principal principal) {
        return new ResponseEntity<>(postService.updatePost(id, DTO, principal), HttpStatus.OK);
    }

    @GetMapping("/post/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam(required = false) String author, @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size, @RequestParam String sort) {
        try {
            Page<Post> pagePosts = postService.filter(author, page, size, sort);

            List<Post> posts = pagePosts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("posts", posts);
            response.put("currentPage", pagePosts.getNumber() + 1);
            response.put("totalItems", pagePosts.getTotalElements());
            response.put("totalPages", pagePosts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable String id, Principal principal) {
        return new ResponseEntity<>(postService.deletePost(id, principal), HttpStatus.OK);
    }
}
