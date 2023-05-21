package com.ceofacebook.facebookv2.dtos.like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {
    private String author;

    private String postTitle;

    private String likeType;
}
