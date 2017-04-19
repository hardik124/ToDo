package com.todo.todo.ui.activities.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.todo.todo.R;
import com.todo.todo.models.userDetailsModel;
import com.todo.todo.ui.activities.base.BaseActivity;
import com.todo.todo.utils.CircularImageView;
import com.todo.todo.utils.IntentHandle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserDetails extends BaseActivity {
    private final int GALLERY_REQUEST = 7, PERMISSION_REQUEST = 12345;
    private EditText etName, etDesc;
    private CircularImageView userImage;
    private ImageView doneBtn;
    private DatabaseReference mUserDetails;
    private userDetailsModel currentUser;
    private Uri mImageUri;
    private Boolean imageChanged = false;
    private IntentHandle intentHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        setToolbar();
        showBackButton();

        etDesc = (EditText) findViewById(R.id.AboutME);
        etName = (EditText) findViewById(R.id.userName);
        userImage = (CircularImageView) findViewById(R.id.userPicture);
        userImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.defaultprofile));

        doneBtn = (ImageView) findViewById(R.id.doneButton);
        intentHandle = new IntentHandle();
        currentUser = new userDetailsModel();

        mUserDetails = FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        showProgressDialog();
        setData();
        getToolbar().setTitle(currentUser.getName());
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(
                        UserDetails.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            UserDetails.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST
                    );

                }
                if (ContextCompat.checkSelfPermission(UserDetails.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
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


    private void setData() {
        mUserDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(userDetailsModel.class);
                etDesc.setText(currentUser.getDesc());
                etName.setText(currentUser.getName());
                Picasso.with(UserDetails.this).load(currentUser.getImageUrl()).into(userImage);
                hideProgressDialog();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showSnack(getString(R.string.Error_retrieving));
                hideProgressDialog();
            }
        });


    }

    private void postData() {
        showProgressDialog();
        if (imageChanged) {
            StorageReference mStorage = FirebaseStorage.getInstance().getReference();
            final StorageReference filepath = mStorage.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    currentUser.setImageUrl(taskSnapshot.getDownloadUrl().toString());
                }
            });
        }
        currentUser.setDesc(etDesc.getText().toString());
        currentUser.setName(etName.getText().toString());
        mUserDetails.setValue(currentUser);
        hideProgressDialog();
        showSnack("Details Updated");
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
                    String path = MediaStore.Images.Media.insertImage(UserDetails.this.getContentResolver(), bitmap2, mImageUri.getLastPathSegment(), null);

                    mImageUri = Uri.parse(path);
                    userImage.setImageURI(mImageUri);


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