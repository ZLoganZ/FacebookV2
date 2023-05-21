package com.ceofacebook.facebookv2.services.post;

import com.ceofacebook.facebookv2.dtos.post.PostDto;
import com.ceofacebook.facebookv2.entities.Post;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface PostService {

    Page<Post> filter(String search,
                      int page, int size, String sort);

    List<Post> getAllPosts();

    Post getPost(String id);

    Post createPost(PostDto dto, Principal principal);

    Post updatePost(String id, PostDto dto, Principal principal);

    Post deletePost(String id, Principal principal);
}
