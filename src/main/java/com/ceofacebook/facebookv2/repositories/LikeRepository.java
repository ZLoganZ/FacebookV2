package com.ceofacebook.facebookv2.repositories;

import com.ceofacebook.facebookv2.entities.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface LikeRepository extends MongoRepository<Like, String>{
    @Query(value = "{'_id': ?0}")
    Optional<Like> getLikeById(String id);

    Page<Like> findByPostTitle(String title, Pageable pageable);
}
