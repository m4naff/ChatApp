package com.example.chatapp.model;

import com.example.chatapp.model.serializer.DocumentIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Message object model (MongoDB Document).
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@Document(collection = "messages")
public class Message extends BaseDocument {

    @CreatedDate
    private Instant createdDate;

    @NotNull
    @JsonSerialize(using = DocumentIdSerializer.class)
    @DBRef
    private Chat chat;

    @NotNull
    @JsonSerialize(using = DocumentIdSerializer.class)
    @DBRef
    private User sender;

    @NotBlank
    private String content;

}
