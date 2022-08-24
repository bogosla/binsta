package com.bogosla.binsta.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bogosla.binsta.BitmapScaler;
import com.bogosla.binsta.R;
import com.bogosla.binsta.models.ParsePost;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;


public class PostFragment extends Fragment {
    public static final int CAPTURE_REQUEST_CODE = 77;
    public static final String TAG = "PostFragment";
    EditText edDescriptionPost;
    ImageButton takeImage;
    ImageView imgPost;
    Button btnPost;
    File photoFile;
    ProgressBar progressBar;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_post, container, false);

        edDescriptionPost = root.findViewById(R.id.edDescriptionPost);
        takeImage = root.findViewById(R.id.takeImage);
        imgPost = root.findViewById(R.id.imgPost);
        btnPost = root.findViewById(R.id.btnPost);
        progressBar = root.findViewById(R.id.progress);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        takeImage.setOnClickListener(view12 -> launchCamera());

        btnPost.setOnClickListener(view1 -> {
            btnPost.setVisibility(Button.GONE);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            String desc = edDescriptionPost.getText().toString();
            if (desc.isEmpty()) {
                Toast.makeText(getContext(), "Description can't be empty !!", Toast.LENGTH_SHORT).show();
                btnPost.setVisibility(Button.VISIBLE);
                progressBar.setVisibility(ProgressBar.GONE);
                return;
            }
            if (photoFile == null || imgPost.getDrawable() == null) {
                Toast.makeText(getContext(), "There is no image !!", Toast.LENGTH_SHORT).show();
                btnPost.setVisibility(Button.VISIBLE);
                progressBar.setVisibility(ProgressBar.GONE);
                return;
            }
            // Save
            ParseUser user = ParseUser.getCurrentUser();
            savePost(desc, user, photoFile);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAPTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Create and configure BitmapFactory
                BitmapFactory.Options bounds = new BitmapFactory.Options();
                bounds.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(photoFile.getAbsolutePath(), bounds);
                BitmapFactory.Options opts = new BitmapFactory.Options();

                Bitmap rawTakenImg = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), opts);
                // Resize the image
                // Bitmap resizeTakenImg = BitmapScaler.scaleToFitWidth(rawTakenImg, 250);
                Bitmap takenImg = rotateBitmapOrientation(rawTakenImg, photoFile.getAbsolutePath(), bounds);

                imgPost.setImageBitmap(takenImg);
            } else {
                Toast.makeText(getContext(), "Picture wasn't taken !!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void savePost(String desc, ParseUser user, File photo) {
        ParsePost post = new ParsePost();
        post.setDescription(desc);
        post.setUser(user);
        post.setImage(new ParseFile(photo));
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Post save was successful !!", Toast.LENGTH_SHORT).show();
                    edDescriptionPost.setText("");
                    imgPost.setImageResource(0);
                }

                btnPost.setVisibility(Button.VISIBLE);
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }

    private void launchCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        long t = System.currentTimeMillis();
        photoFile = getPhotoFile(String.valueOf("binsta_"+t));
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.bogosla.fileprovider", photoFile);
        i.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (i.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(i, CAPTURE_REQUEST_CODE);
        }
    }

    private File getPhotoFile(String filename) {
        File storageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Binsta");
        if (!storageDir.exists() && !storageDir.mkdirs()) {
            Log.i(TAG, "Failed to create dir");
        }
        return new File(storageDir.getPath() + File.separator + filename);
    }
    public Bitmap rotateBitmapOrientation(Bitmap bm, String path, BitmapFactory.Options bounds) {
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }
}