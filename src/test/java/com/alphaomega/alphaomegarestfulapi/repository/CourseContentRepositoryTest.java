package com.alphaomega.alphaomegarestfulapi.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseContentRepositoryTest {

    @Autowired
    private CourseContentRepository courseContentRepository;

    @Test
    public void testQueryTotalDuration() {
        Integer totalDurationOfContentDetail = courseContentRepository.findTotalDurationOfContentDetailByContentId("cxct-9711f043-43b1-45a9-95e7-820f3e1fbfa2");
        Assertions.assertEquals(totalDurationOfContentDetail, 90);
    }

    @Test
    public void testQueryCountTotalCourseDetail() {
        Integer totalCount = courseContentRepository.findCountOfContentDetailByContentId("cxct-9711f043-43b1-45a9-95e7-820f3e1fbfa2");
        Assertions.assertEquals(totalCount, 1);
    }


}