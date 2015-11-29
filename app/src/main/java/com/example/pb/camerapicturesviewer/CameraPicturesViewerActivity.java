package com.example.pb.camerapicturesviewer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

// Rotation
// Empty list

public class CameraPicturesViewerActivity extends AppCompatActivity {

    private OrientationEventListener orientationEventListener;
    private static final int delta = 10;
    private GridView gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_pictures_viewer);
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation > 90 - delta && orientation < 90 + delta) rotateScreen(90);
                else if (orientation < delta || orientation > 360 - delta) rotateScreen(0);
                else if (orientation > 270 - delta && orientation < 270 + delta) rotateScreen(270);
                rotateScreen(orientation);
            }
        };

        gallery = (GridView)findViewById(R.id.gallery_view);
        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera");


        String[] paths = new String[dir.listFiles().length];
        for(int i = 0; i < paths.length; i++) {
            paths[i] = dir.listFiles()[i].getAbsolutePath();
        }

        int numOfColumns = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        int width;
        int height;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        Log.d("myTAG", "Height: " + height);
        Log.d("myTAG", "Width: " + width);


        GalleryAdapter adapter = new GalleryAdapter(this, paths, width / numOfColumns, numOfColumns);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = (String) gallery.getItemAtPosition(position);
                if (path != null) {
                    Intent openingIntent = new Intent();
                    openingIntent.setAction(Intent.ACTION_VIEW);
                    File image = new File(path);
                    openingIntent.setDataAndType(Uri.fromFile(image), "image/*");
                    startActivity(openingIntent);
                }
            }
        });
    }

    private void rotateScreen(int angle) {
        Log.d("myTAG", "Angle: " + angle);
        switch (angle) {
            case 0:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case 90:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case 270:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            default:
                Toast.makeText(this, R.string.unknown_orientation_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        orientationEventListener.enable();
    }

    protected void onPause() {
        super.onPause();
        orientationEventListener.disable();
    }

}