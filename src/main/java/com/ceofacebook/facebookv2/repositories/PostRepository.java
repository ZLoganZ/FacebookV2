package com.ceofacebook.facebookv2.repositories;

import com.ceofacebook.facebookv2.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;
public interface PostRepository extends MongoRepository<Post, String>{
    @Query(value = "{'_id': ?0}")
    Optional<Post> getPostById(String id);

    Page<Post> findByAuthor(String author, Pageable pageable);
}
