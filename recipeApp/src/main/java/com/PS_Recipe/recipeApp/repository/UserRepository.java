package com.PS_Recipe.recipeApp.repository;

import com.PS_Recipe.recipeApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
}
