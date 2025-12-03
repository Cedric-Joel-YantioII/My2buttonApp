package com.assignment2.my2buttonapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.IOException;

public class ImageActivity extends AppCompatActivity {

    private Button openCamera;
    private ImageView clickedImage;
    private File photoFile;
    private Uri photoUri;
    private static final int CAMERA_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        openCamera = findViewById(R.id.buttonCaptureImage);
        clickedImage = findViewById(R.id.imageView);

        openCamera.setOnClickListener(v -> {
            try {
                photoFile = createImageFile();
                photoUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        photoFile
                );

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private File createImageFile() throws IOException {
        File imageDir = new File(getCacheDir(), "images");
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        return File.createTempFile("captured_", ".jpg", imageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Glide.with(this).load(photoUri).into(clickedImage);
        }
    }
}
