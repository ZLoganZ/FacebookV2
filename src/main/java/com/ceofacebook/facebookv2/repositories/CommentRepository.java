package com.ceofacebook.facebookv2.repositories;

import com.ceofacebook.facebookv2.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
public interface CommentRepository extends MongoRepository<Comment, String>{
    @Query(value = "{'_id': ?0}")
    Optional<Comment> getCommentById(String id);

    Page<Comment> findByPostTitle(String title, Pageable pageable);
}
