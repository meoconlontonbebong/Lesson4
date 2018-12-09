package com.example.kienphung.lesson4;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String[] MODIFIER = {".jpg", ".jpeg", ".png"};
    private static final int READ_EXTERNAL_STORAGE = 1;
    private static final String[] PATH = {"/storage/emulated/0/Download/"};
    private List<String> mFolderPaths;
    private ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initView();
        new GetImagesAsyncTask().execute(PATH);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);
        }
    }

    private void initView() {
        RecyclerView recyclerViewImages = findViewById(R.id.recycler_view);
        mFolderPaths = new ArrayList<>();
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 2));
        mImageAdapter = new ImageAdapter(mFolderPaths);
        recyclerViewImages.setAdapter(mImageAdapter);
    }

    private class GetImagesAsyncTask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            File imageFolder = new File(params[0]);
            File[] files = imageFolder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    for (String modifier : MODIFIER) {
                        if (pathname.getName().toLowerCase().endsWith(modifier))
                            return true;
                    }
                    return false;
                }
            });
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                publishProgress(file.getPath());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mFolderPaths.add(values[0]);
            mImageAdapter.notifyDataSetChanged();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
