package com.example.chatapp.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Base MongoDB document for all other document classes.
 */
@Data
public abstract class BaseDocument {

    @Id
    private String id;
}
