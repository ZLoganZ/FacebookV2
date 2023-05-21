package com.ceofacebook.facebookv2.services.post;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.ceofacebook.facebookv2.dtos.post.PostDto;
import com.ceofacebook.facebookv2.entities.Post;
import com.ceofacebook.facebookv2.exceptions.InvalidException;
import com.ceofacebook.facebookv2.exceptions.NotFoundException;
import com.ceofacebook.facebookv2.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> filter(String search, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<Post> pagePosts;

        if (search != null) {
            pagePosts = postRepository.findByAuthor(search, pageable);
        } else {
            pagePosts = postRepository.findAll(pageable);
        }

        return pagePosts;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPost(String id) {
        return postRepository.getPostById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Post with id %s does not exist", id)));
    }

    @Override
    public Post createPost(PostDto dto, Principal principal) {
        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new InvalidException("Title is required");
        }
        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new InvalidException("Content is required");
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthor(principal.getName());

        return postRepository.save(post);
    }

    @Override
    public Post updatePost(String id, PostDto dto, Principal principal) {
        Post post = postRepository.getPostById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Post with id %s does not exist", id)));

        if (!post.getAuthor().equals(principal.getName())) {
            throw new InvalidException("You are not the author of this post");
        }

        if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
            throw new InvalidException("Title is required");
        }
        if (dto.getContent() == null || dto.getContent().isEmpty()) {
            throw new InvalidException("Content is required");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return postRepository.save(post);
    }

    @Override
    public Post deletePost(String id, Principal principal) {
        Post post = postRepository.getPostById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Post with id %s does not exist", id)));

        if (!post.getAuthor().equals(principal.getName())) {
            throw new InvalidException("You are not the author of this post");
        }

        postRepository.delete(post);

        return post;
    }
}
