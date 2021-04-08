package com.awesome.video.module.course;

import com.awesome.video.module.BaseModel;

import java.util.ArrayList;

/**
 * @author: vision
 * @function:
 * @date: 16/9/8
 */
public class CourseModel extends BaseModel {

    public CourseHeaderValue head;
    public CourseFooterValue footer;
    public ArrayList<CourseCommentValue> body;
}
