package com.ceofacebook.facebookv2.services.comment;

import com.ceofacebook.facebookv2.repositories.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.ceofacebook.facebookv2.dtos.comment.CommentDto;
import com.ceofacebook.facebookv2.entities.Comment;
import com.ceofacebook.facebookv2.exceptions.InvalidException;
import com.ceofacebook.facebookv2.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<Comment> filter(String search, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<Comment> pageComments;

        if (search != null) {
            pageComments = commentRepository.findByPostTitle(search, pageable);
        } else {
            pageComments = commentRepository.findAll(pageable);
        }

        return pageComments;
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getComment(String id) {
        return commentRepository.getCommentById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %s does not exist", id)));
    }

    @Override
    public Comment createComment(CommentDto dto, Principal principal) {
        if (dto.getPostTitle() == null || dto.getPostTitle().isEmpty()) {
            throw new InvalidException("Post title is required");
        }

        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new InvalidException("Content is required");
        }

        Comment comment = new Comment();
        comment.setPostTitle(dto.getPostTitle());
        comment.setContent(dto.getContent());
        comment.setAuthor(principal.getName());

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(String id, CommentDto dto, Principal principal) {
        Comment comment = commentRepository.getCommentById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %s does not exist", id)));

        if (!comment.getAuthor().equals(principal.getName())) {
            throw new InvalidException("You are not the author of this comment");
        }

        if (dto.getPostTitle() == null || dto.getPostTitle().isEmpty()) {
            throw new InvalidException("Post title is required");
        }

        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new InvalidException("Content is required");
        }

        comment.setPostTitle(dto.getPostTitle());
        comment.setContent(dto.getContent());
        comment.setAuthor(principal.getName());

        return commentRepository.save(comment);
    }

    @Override
    public Comment deleteComment(String id, Principal principal) {
        Comment comment = commentRepository.getCommentById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Comment with id %s does not exist", id)));

        if (!comment.getAuthor().equals(principal.getName())) {
            throw new InvalidException("You are not the author of this comment");
        }

        commentRepository.delete(comment);

        return comment;
    }

}
