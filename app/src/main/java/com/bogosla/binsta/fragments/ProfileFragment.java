package com.bogosla.binsta.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bogosla.binsta.Dialog;
import com.bogosla.binsta.IndeterminateDialog;

import com.bogosla.binsta.SimplePostAdapter;

import com.bogosla.binsta.databinding.FragmentProfileBinding;
import com.bogosla.binsta.models.ParsePost;

import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class ProfileFragment extends MyBaseFragment {
    private static final String TAG = "ProfileFragment";
    public static final int PICK_PHOTO = 601;
    private ProfileListener mCallback;
    private FragmentProfileBinding binding;


    public interface ProfileListener {
        void onLoggedOut();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (ProfileListener) getActivity();
        } catch (ClassCastException e) {
            throw  new ClassCastException("Must be implements ProfileListener");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutManager = new GridLayoutManager(getContext(), 3);
        adapter = new SimplePostAdapter(getContext(), posts);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        this.rcPosts = (RecyclerView) binding.fragmentProfileRcProfile;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fragmentProfileChangeProfile.setOnClickListener(view12 -> takeImage(PICK_PHOTO));
        // Create an indeterminate progress dialog
        IndeterminateDialog dialog = IndeterminateDialog.newInstance("Logging out", "Please wait a while!");
        dialog.setCancelable(false);

        binding.fragmentProfileBtnLogout.setOnClickListener(view1 -> {
            dialog.show(getActivity().getSupportFragmentManager(), "logout");

            ParseUser.logOutInBackground(e -> {
                if (e != null) {
                    Toast.makeText(getContext(), "Error while logging out!!", Toast.LENGTH_SHORT).show();
                } else {
                    // Goto AuthActivity
                    mCallback.onLoggedOut();
                }
                // Dismiss the progress
                dialog.dismiss();
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG, "My result BB");
        if (requestCode == PICK_PHOTO && resultCode == RESULT_OK) {
            Log.i(TAG, data.toString() + "777");
            Uri image = data.getData();
            binding.fragmentProfileImgProfile.setImageURI(image);

            showConfirmationDialog(new ParseFile(new File(image.getPath())));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void takeImage(int request_code) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, request_code);
    }

    private void showConfirmationDialog(ParseFile file) {
        Dialog dialog2 = Dialog.newInstance("Update profile", "Are you sure to upload this image?");

        dialog2.setListener(new Dialog.Listener() {
            @Override
            public void onNegativeClick(DialogInterface dialog) {
                binding.fragmentProfileImgProfile.setImageURI(null);
                if (dialog != null) dialog.dismiss();
            }

            @Override
            public void onPositiveClick(DialogInterface dialog) {
                updateImage(file, e -> {
                    if (e == null) {
                        Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
                    }
                    if (dialog != null) dialog.dismiss();
                });
            }
        });
        dialog2.setCancelable(false);
        dialog2.show(getActivity().getSupportFragmentManager(), "upload");

    }

    @Override
    protected void getPosts() {
        ParseQuery<ParsePost> query = new ParseQuery<>(ParsePost.class);
        query.include(ParsePost.USER_KEY);
        query.whereEqualTo(ParsePost.USER_KEY, ParseUser.getCurrentUser());

        query.addDescendingOrder("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                posts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void updateImage(ParseFile file, SaveCallback callback) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("profile", file);
        user.saveInBackground(callback);
    }
}