package com.ceofacebook.facebookv2.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "like")
public class Like extends BaseEntity {
    @Id
    private String id;

    private String author;

    private String postTitle;

    private String likeType;
}
