package com.showme.core.repository.post;

import com.showme.core.entity.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
