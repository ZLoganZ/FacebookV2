package com.ceofacebook.facebookv2.controllers;

import com.ceofacebook.facebookv2.dtos.comment.CommentDto;
import com.ceofacebook.facebookv2.entities.Comment;
import com.ceofacebook.facebookv2.services.comment.CommentService;
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
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable String id) {
        return new ResponseEntity<>(commentService.getComment(id), HttpStatus.OK);
    }

    @GetMapping("/comment")
    public ResponseEntity<Iterable<Comment>> getAllComment() {
        return new ResponseEntity<>(commentService.getAllComments(), HttpStatus.OK);
    }

    @PostMapping("/comment")
    public ResponseEntity<Comment> create(@RequestBody CommentDto DTO, Principal principal) {
        return new ResponseEntity<>(commentService.createComment(DTO, principal), HttpStatus.OK);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<Comment> update(@PathVariable String id, @RequestBody CommentDto DTO, Principal principal) {
        return new ResponseEntity<>(commentService.updateComment(id, DTO, principal), HttpStatus.OK);
    }

    @GetMapping("/comment/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam(required = false) String postTitle, @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "3") int size, @RequestParam String sort) {
        try {
            Page<Comment> pageComments = commentService.filter(postTitle, page, size, sort);

            List<Comment> comments = pageComments.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("comments", comments);
            response.put("currentPage", pageComments.getNumber() + 1);
            response.put("totalItems", pageComments.getTotalElements());
            response.put("totalPages", pageComments.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable String id, Principal principal) {
        return new ResponseEntity<>(commentService.deleteComment(id, principal), HttpStatus.OK);
    }
}
