package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.request.CommentRequest;
import com.alphaomega.alphaomegarestfulapi.payload.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse create(CommentRequest commentRequest, String userId, String courseId);

    List<CommentResponse> findByCourseId(String courseId);

    Boolean deleteById(String userId,  String commentId);

}
