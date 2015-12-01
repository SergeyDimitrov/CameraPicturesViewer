package com.example.pb.camerapicturesviewer;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.ui.TwoWayAdapterView;
import com.jess.ui.TwoWayGridView;

import java.io.File;
import java.io.FilenameFilter;

public class CameraPicturesViewerActivity extends Activity {

    private TwoWayGridView gallery;
    private static final int ROWS_COUNT_PORTRAIT = 4;
    private static final int ROWS_COUNT_LANDSCAPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_pictures_viewer);

        gallery = (TwoWayGridView)findViewById(R.id.gallery_view);
        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera");

        String[] paths = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".jpg");
            }
        });

        for(int i = 0; i < paths.length; i++) {
            paths[i] = directory.getAbsolutePath() + "/" + paths[i];
        }

        int numOfRows = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                ROWS_COUNT_LANDSCAPE : ROWS_COUNT_PORTRAIT;

        int screenHeight;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;

        Log.d("myTAG1", "Width: " + screenHeight);
        Log.d("myTAG1", "Rows count: " + numOfRows);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.grid_container);
        Log.d("myTAG1", "Layout width: " + layout.getWidth() + " " + layout.getHeight());

        GalleryAdapter adapter = new GalleryAdapter(this, paths, screenHeight / numOfRows);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new TwoWayAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(TwoWayAdapterView<?> parent, View view, int position, long id) {
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
}