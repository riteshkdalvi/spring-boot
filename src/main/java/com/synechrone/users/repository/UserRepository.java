package com.synechrone.users.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.synechrone.users.entity.User;
public interface UserRepository extends MongoRepository<User,String> {

	Optional<User> findById(Long userId);

}
