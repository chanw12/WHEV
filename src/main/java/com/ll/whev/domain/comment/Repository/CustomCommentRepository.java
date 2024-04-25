package com.ll.whev.domain.comment.Repository;

import com.ll.whev.domain.comment.dto.CommentDto;
import com.ll.whev.domain.comment.entity.Comment;

import java.util.List;

public interface CustomCommentRepository {

 public List<Comment> getCommentsByImageId(Long imageId);

 }
