package com.ll.whev.domain.comment.Repository;

import com.ll.whev.domain.comment.dto.CommentDto;
import com.ll.whev.domain.comment.entity.Comment;
import com.ll.whev.domain.comment.entity.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.whev.domain.comment.entity.QComment.comment;

@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> getCommentsByImageId(Long imageId){
        return queryFactory.selectFrom(comment)
                .where(comment.image.id.eq(imageId))
                .orderBy(comment.vote.desc(),comment.createDate.desc())
                .fetch();
    }
}
