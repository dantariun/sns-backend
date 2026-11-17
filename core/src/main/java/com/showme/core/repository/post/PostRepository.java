package com.showme.core.repository.post;

import com.showme.core.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByMemberId(Long memberId, Pageable pageable);
}
