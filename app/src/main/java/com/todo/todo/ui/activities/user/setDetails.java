package com.todo.todo.ui.activities.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.todo.todo.R;
import com.todo.todo.models.userDetailsModel;
import com.todo.todo.ui.activities.base.BaseActivity;
import com.todo.todo.ui.activities.tasks.Home;
import com.todo.todo.utils.CircularImageView;
import com.todo.todo.utils.IntentHandle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class setDetails extends BaseActivity {

    private static final int GALLERY_REQUEST = 7;
    private static final int PERMISSION_REQUEST = 12345;
    private String userImageURL;
    private CircularImageView userProfile;
    private String userId;
    private Uri mImageUri = null;
    private userDetailsModel currentUser;
    private IntentHandle intentHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_details);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //noinspection ConstantConditions
        userId = mAuth.getCurrentUser().getUid();
        userProfile = (CircularImageView) findViewById(R.id.profileImage);
        Button submit = (Button) findViewById(R.id.submitDetails);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAccountSetup();
            }
        });
        userProfile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.defaultprofile));
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(
                        setDetails.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            setDetails.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST
                    );

                }
                if (ContextCompat.checkSelfPermission(setDetails.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    showSnack("Permission granted");
                    intentHandle = new IntentHandle();
                    Intent galleryIntent;
                    galleryIntent = intentHandle.getPickImageIntent(getApplicationContext()); //Get intent to create chooser .
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);

                }

            }
        });

    }


    private void startAccountSetup() {
        if (isNetworkAvailable(this)) {
            EditText userName = (EditText) findViewById(R.id.username);

            final String username = userName.getText().toString().trim();
            final DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child(userId);
            currentUser = new userDetailsModel();
            if (!TextUtils.isEmpty(username)) {
                if (mImageUri == null) {
                    showSnack(" Please select profile picture ");

                    getSnack().setAction("Skip", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentUser.setImageUrl(userImageURL); //Sets
                            currentUser.setName(username); //User
                            mDatabaseUser.setValue(currentUser);
                            hideProgressDialog();
                            gotoHome();
                        }
                    });
                } else {

                    final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("UserImages").child(userId);
                    showProgressDialog(getString(R.string.settingAccount));
                    filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            userImageURL = taskSnapshot.getDownloadUrl().toString();
                            currentUser.setImageUrl(userImageURL);
                            currentUser.setName(username);
                            mDatabaseUser.setValue(currentUser);
                            hideProgressDialog();
                            gotoHome();
                        }
                    });
                }

            } else {
                String message;
                {
                    if (mImageUri == null) {
                        message = "Please select image";
                    } else {
                        message = "Enter Name";
                    }
                    showSnack(message);
                }

            }

        } else {
            hideProgressDialog();
            gotoHome();
        }
    }

    private void gotoHome() {
        Intent setDetailsIntent = new Intent(setDetails.this, Home.class);
        setDetailsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setDetailsIntent.putExtra("user", "new User");
        startActivity(setDetailsIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = intentHandle.getPickImageResultUri(data); //Get data
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .setSnapRadius(2)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                try {
                    mImageUri = result.getUri();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), mImageUri);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    Double ratio = Math.ceil(100000.0 / bitmap.getByteCount());
                    bitmap.compress(Bitmap.CompressFormat.JPEG, (int) Math.min(ratio, 100), out);
                    Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
                    String path = MediaStore.Images.Media.insertImage(setDetails.this.getContentResolver(), bitmap2, mImageUri.getLastPathSegment(), null);

                    mImageUri = Uri.parse(path);
                    userProfile.setImageURI(mImageUri);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showSnack("Permission granted");
                    intentHandle = new IntentHandle();
                    Intent galleryIntent;
                    galleryIntent = intentHandle.getPickImageIntent(getApplicationContext()); //Get intent to create chooser .
                    startActivityForResult(galleryIntent, GALLERY_REQUEST);
                } else {
                    showSnack("Permission denied");
                }
                break;
        }
    }
}
