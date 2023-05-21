package com.ceofacebook.facebookv2.services.like;

import com.ceofacebook.facebookv2.dtos.like.LikeDto;
import com.ceofacebook.facebookv2.entities.Like;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface LikeService {

    Page<Like> filter(String search,
                      int page, int size, String sort);

    List<Like> getAllLikes();

    Like getLike(String id);

    Like createLike(LikeDto dto, Principal principal);

    Like updateLike(String id, LikeDto dto, Principal principal);

    Like deleteLike(String id, Principal principal);
}
