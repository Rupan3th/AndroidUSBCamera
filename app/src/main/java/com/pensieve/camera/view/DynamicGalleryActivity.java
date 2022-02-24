package com.pensieve.camera.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;

import com.pensieve.camera.R;
import com.pensieve.camera.UVCCameraHelper;
import com.pensieve.camera.application.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DynamicGalleryActivity extends AppCompatActivity implements ItemClickListener{

    RecyclerView recyclerView;
    private Adapter mAdapter;
    private ArrayList<CourseModel> courseModelArrayList = new ArrayList<CourseModel>();
    private String path;   // = Environment.getExternalStorageDirectory().getAbsolutePath() + "/USBCamera/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_gallery);

//        path = path + getString(R.string.app_name) + "/";
        path = UVCCameraHelper.ROOT_PATH + MyApplication.DIRECTORY_NAME + "/";

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                201);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        CreateModelArrayList();

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        mAdapter = new Adapter(this, courseModelArrayList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        if(courseModelArrayList.get(position).getFileType().equals("image")){
            Intent detailImage = new Intent(this, ImageDetailActivity.class);
            String imageName = courseModelArrayList.get(position).getCourse_name();
            detailImage.putExtra("imageName", imageName);

            startActivity(detailImage);
        }
        if(courseModelArrayList.get(position).getFileType().equals("video")){
            Intent VideoPlay = new Intent(this, VideoPlayActivity.class);
            String videoName = courseModelArrayList.get(position).getCourse_name();
            VideoPlay.putExtra("videoName", videoName);

            startActivity(VideoPlay);
        }

    }

    private void CreateModelArrayList(){
        courseModelArrayList.clear();

        File dir_image = new File(path + "images");
        if(dir_image.exists()){
            for (int i = 0; i < dir_image.listFiles().length; i++) {
                String image_name = path + "images/" + dir_image.listFiles()[i].getName();
                Bitmap bitmap = BitmapFactory.decodeFile(image_name);
                courseModelArrayList.add(new CourseModel(image_name, bitmap, "image"));

            }
        }

        File dir_video = new File(path + "videos");
        if(dir_video.exists()){
            for (int i = 0; i < dir_video.listFiles().length; i++) {
                String image_name = path + "videos/" + dir_video.listFiles()[i].getName();
//                if(image_name.substring(image_name.length()-3).equals("jpg")){
//                    Bitmap bitmap = BitmapFactory.decodeFile(image_name);
//                    courseModelArrayList.add(new CourseModel(image_name, bitmap, "video"));
//                }
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(image_name,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                courseModelArrayList.add(new CourseModel(image_name, bitmap, "video"));
            }
        }

        Collections.sort(courseModelArrayList, new NameComparator().reversed());

    }

    class NameComparator implements Comparator<CourseModel> {
        @Override
        public int compare(CourseModel f1, CourseModel f2) {
            String real_Name1 = f1.getCourse_name().substring(f1.getCourse_name().lastIndexOf("/"));
            String real_Name2 = f2.getCourse_name().substring(f2.getCourse_name().lastIndexOf("/"));

            return real_Name1.compareTo(real_Name2);
        }
    }
}