package com.PS_Recipe.recipeApp.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Id;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @Nonnull
    private String username;
    @Nonnull
    private String password;
}
