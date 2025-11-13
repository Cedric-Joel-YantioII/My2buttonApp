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

    // Declare UI elements
    private Button openCamera;
    private ImageView clickedImage;

    // Variables to hold the file and its Uri
    private File photoFile;
    private Uri photoUri;

    // Unique request code for identifying the camera intent result
    private static final int CAMERA_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        openCamera = findViewById(R.id.buttonCaptureImage);
        clickedImage = findViewById(R.id.imageView);

        openCamera.setOnClickListener(v -> {
            try {
                // Create a temporary image file to store the captured photo
                photoFile = createImageFile();

                // Get a content URI for the file using FileProvider
                // This allows secure sharing of the file with the camera app
                photoUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider", // FileProvider authority (defined in manifest)
                        photoFile
                );

                // Create an intent to launch the camera app
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Tell the camera app where to save the image
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                // Grant temporary write permission to the camera app for the URI
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                // Start the camera app and wait for the result
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Function to create a temporary image file in the cache directory
    private File createImageFile() throws IOException {
        // Create a directory inside cache to store images (if not already created)
        File imageDir = new File(getCacheDir(), "images");
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        // Create a temp file with prefix "captured_" and suffix ".jpg"
        return File.createTempFile("captured_", ".jpg", imageDir);
    }

    // handles activity results (like camera)
    @Override
    @Deprecated
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the camera and was successful
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Load the captured image from the URI into the ImageView using Glide
            Glide.with(this).load(photoUri).into(clickedImage);
        }
    }
}
