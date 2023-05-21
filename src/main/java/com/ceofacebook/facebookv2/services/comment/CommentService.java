package com.ceofacebook.facebookv2.services.comment;

import com.ceofacebook.facebookv2.dtos.comment.CommentDto;
import com.ceofacebook.facebookv2.entities.Comment;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface CommentService {

    Page<Comment> filter(String search,
                         int page, int size, String sort);

    List<Comment> getAllComments();

    Comment getComment(String id);

    Comment createComment(CommentDto dto, Principal principal);

    Comment updateComment(String id, CommentDto dto, Principal principal);

    Comment deleteComment(String id, Principal principal);
}
