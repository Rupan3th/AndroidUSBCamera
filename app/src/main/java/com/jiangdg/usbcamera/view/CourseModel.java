package com.jiangdg.usbcamera.view;

import android.graphics.Bitmap;

public class CourseModel {
    private String course_name;
    private Bitmap imgid;
    private String file_type;

    public CourseModel(String course_name, Bitmap imgid, String file_type) {
        this.course_name = course_name;
        this.imgid = imgid;
        this.file_type = file_type;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Bitmap getImgid() {
        return imgid;
    }

    public void setImgid(Bitmap  imgid) {
        this.imgid = imgid;
    }

    public String getFileType() {
        return file_type;
    }

    public void setFiletype(String file_type) { this.file_type = file_type; }
}
