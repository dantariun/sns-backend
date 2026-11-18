package com.showme.core.repository.post;

import com.showme.core.entity.post.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Page<PostComment> findByPostId(Long postId, Pageable pageable);
}
