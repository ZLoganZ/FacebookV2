package com.ceofacebook.facebookv2.controllers;

import com.ceofacebook.facebookv2.dtos.like.LikeDto;
import com.ceofacebook.facebookv2.entities.Like;
import com.ceofacebook.facebookv2.services.like.LikeService;
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
public class LikeController {
    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping("/like/{id}")
    public ResponseEntity<Like> getLike(@PathVariable String id) {
        return new ResponseEntity<>(likeService.getLike(id), HttpStatus.OK);
    }

    @GetMapping("/like")
    public ResponseEntity<Iterable<Like>> getAllLike() {
        return new ResponseEntity<>(likeService.getAllLikes(), HttpStatus.OK);
    }

    @PostMapping("/like")
    public ResponseEntity<Like> create(@RequestBody LikeDto DTO, Principal principal) {
        return new ResponseEntity<>(likeService.createLike(DTO, principal), HttpStatus.OK);
    }

    @PutMapping("/like/{id}")
    public ResponseEntity<Like> update(@PathVariable String id, @RequestBody LikeDto DTO, Principal principal) {
        return new ResponseEntity<>(likeService.updateLike(id, DTO, principal), HttpStatus.OK);
    }

    @GetMapping("/like/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam(required = false) String postTitle, @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size, @RequestParam String sort) {
        try {
            Page<Like> pageLikes = likeService.filter(postTitle, page, size, sort);

            List<Like> likes = pageLikes.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("likes", likes);
            response.put("currentPage", pageLikes.getNumber() + 1);
            response.put("totalItems", pageLikes.getTotalElements());
            response.put("totalPages", pageLikes.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/like/{id}")
    public ResponseEntity<Like> deleteLike(@PathVariable String id, Principal principal) {
        return new ResponseEntity<>(likeService.deleteLike(id, principal), HttpStatus.OK);
    }
}
