package com.ceofacebook.facebookv2.repositories;

import com.ceofacebook.facebookv2.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{'email': ?0}")
    Optional<User> getUser(String email);

    @Query(value = "{'_id': ?0}")
    Optional<User> getUserById(String id);

    Page<User> findByName(String name, Pageable pageable);
}
