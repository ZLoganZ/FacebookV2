package com.ceofacebook.facebookv2.services.like;


import com.ceofacebook.facebookv2.repositories.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.ceofacebook.facebookv2.dtos.like.LikeDto;
import com.ceofacebook.facebookv2.entities.Like;
import com.ceofacebook.facebookv2.exceptions.InvalidException;
import com.ceofacebook.facebookv2.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;

    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    @Override
    public Page<Like> filter(String search, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

        Page<Like> pageLikes;

        if (search != null) {
            pageLikes = likeRepository.findByPostTitle(search, pageable);
        } else {
            pageLikes = likeRepository.findAll(pageable);
        }

        return pageLikes;
    }

    @Override
    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    @Override
    public Like getLike(String id) {
        return likeRepository.getLikeById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Like with id %s does not exist", id)));
    }

    @Override
    public Like createLike(LikeDto dto, Principal principal) {
        if (dto.getPostTitle() == null || dto.getPostTitle().isEmpty()) {
            throw new InvalidException("Post title is required");
        }

        if (dto.getLikeType() == null || dto.getLikeType().isEmpty()) {
            throw new InvalidException("Like type is required");
        }

        Like like = new Like();
        like.setPostTitle(dto.getPostTitle());
        like.setAuthor(principal.getName());
        like.setLikeType(dto.getLikeType());

        return likeRepository.save(like);
    }

    @Override
    public Like updateLike(String id, LikeDto dto, Principal principal) {
        Like like = likeRepository.getLikeById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Like with id %s does not exist", id)));

        if (!like.getAuthor().equals(principal.getName())) {
            throw new InvalidException("You are not the author of this like");
        }

        if (dto.getPostTitle() == null || dto.getPostTitle().isEmpty()) {
            throw new InvalidException("Post title is required");
        }

        if (dto.getLikeType() == null || dto.getLikeType().isEmpty()) {
            throw new InvalidException("Like type is required");
        }

        like.setPostTitle(dto.getPostTitle());
        like.setLikeType(dto.getLikeType());

        return likeRepository.save(like);
    }

    @Override
    public Like deleteLike(String id, Principal principal) {
        Like like = likeRepository.getLikeById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Like with id %s does not exist", id)));

        if (!like.getAuthor().equals(principal.getName())) {
            throw new InvalidException("You are not the author of this like");
        }

        likeRepository.delete(like);

        return like;
    }
}
