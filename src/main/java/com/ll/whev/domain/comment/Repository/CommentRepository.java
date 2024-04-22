package com.ll.whev.domain.comment.Repository;

import com.ll.whev.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
