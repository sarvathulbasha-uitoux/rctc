package com.google.firebase.example.fireeats.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.example.fireeats.R;
import com.google.firebase.example.fireeats.Utils.Util;
import com.google.firebase.example.fireeats.Utils.Utility;
import com.google.firebase.example.fireeats.models.Users;
import com.google.firebase.example.fireeats.recievers.ConnectivityReceiver;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;
    EditText name, desg, mail;
    String name_value, mail_value, desg_value;
    FirebaseFirestore db;
    Button bt_register;
    ProgressDialog progressDialog;
    Users users;
    Handler handler;
    TextView tv_upload;
    RelativeLayout rl_back_arrow;
    String userChoosenTask;
    Intent CropIntent;
    Bitmap photo = null;
    Uri uri;
    String imageFilePath;
    ConstraintLayout cl1;
    ImageView iv_img, iv_clear;
    int request_permissions = 122;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        handler = new Handler();


        init();

        Util.hideKeyboard(RegistrationActivity.this);


        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photo = null;
                cl1.setVisibility(View.GONE);
            }
        });


        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkConnection()) {
                    name_value = name.getText().toString().trim();
                    mail_value = mail.getText().toString().trim();
                    desg_value = desg.getText().toString().trim();

                    if (!name_value.equals("") && !name_value.equals(null)) {
                        if (!mail_value.equals("") && !mail_value.equals(null)) {
                            if (!desg_value.equals("") && !desg_value.equals(null)) {
//                                new FindExist().execute();
                                uploadFile(photo);
                            } else {
                                Util.showSnack(RegistrationActivity.this, "Kindly Enter Your Designation");
                            }
                        } else {
                            Util.showSnack(RegistrationActivity.this, "Kindly Enter Your Email ID");
                        }
                    } else {
                        Util.showSnack(RegistrationActivity.this, "Kindly Enter Your Name");
                    }
                } else {
                    Util.showSnack(RegistrationActivity.this, "Check Your InternetConnection !");
                }
            }
        });

        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (photo == null) {
                    selectImages();
                } else {
                    Util.showSnack(RegistrationActivity.this, "Already image selected, Please remove for upload new");
                }

            }
        });
    }

    private void init() {
        appBarLayout = findViewById(R.id.appBarLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 0.1f));
            appBarLayout.setStateListAnimator(stateListAnimator);
        }

        name = findViewById(R.id.et_name);
        mail = findViewById(R.id.et_email);
        desg = findViewById(R.id.et_desg);
        tv_upload = findViewById(R.id.tv_upload);
        bt_register = findViewById(R.id.bt_register);
        cl1 = findViewById(R.id.cl1);
        rl_back_arrow = findViewById(R.id.rl_back_arrow);
        iv_img = findViewById(R.id.iv_img);
        iv_clear = findViewById(R.id.iv_clear);
        cl1.setVisibility(View.GONE);
    }

    private void permission_check() {
        int permissionCheck_camera = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.CAMERA);
        int permissionCheck_read = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck_write = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck_camera != PackageManager.PERMISSION_GRANTED
                || permissionCheck_read != PackageManager.PERMISSION_GRANTED
                || permissionCheck_write != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(RegistrationActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    request_permissions);
        }
    }


    private void selectImages() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";

                    permission_check();
                    ClickImageFromCamera();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";

                    permission_check();
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), 2);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == request_permissions) {
            int permissionCheck_camera = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.CAMERA);
            int permissionCheck_read = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheck_write = ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck_camera == PackageManager.PERMISSION_GRANTED
                    && permissionCheck_read == PackageManager.PERMISSION_GRANTED
                    && permissionCheck_write == PackageManager.PERMISSION_GRANTED
                    ) {
                selectImages();
            } else {
                permission_check();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void uploadFile(Bitmap bitmap) {
        if (photo != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference mountainImagesRef = storageRef.child("images/" + bitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainImagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                sendMsg("" + downloadUrl, 2);
                    Log.e("downloadUrl-->", "" + downloadUrl);
//                    new AddUser().execute();
                }
            });
        }
//        else {
//            new AddUser().execute();
//        }

    }

    public void ClickImageFromCamera() {

        boolean result = Utility.checkPermission(RegistrationActivity.this);

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(RegistrationActivity.this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(RegistrationActivity.this,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                if (result) {


                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        //Create a file to store the image
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            // Error occurred while creating the File

                        }
                        if (photoFile != null) {
                            uri = FileProvider.getUriForFile(RegistrationActivity.this,
                                    "com.google.firebase.example.fireeats.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    uri);
                            startActivityForResult(intent, 0);

                            grantUriPermission("com.google.firebase.example.fireeats.fileprovider", uri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                        }
                    }


                }
            }
        } else {
            if (result) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    //Create a file to store the image
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    if (photoFile != null) {
                        uri = FileProvider.getUriForFile(RegistrationActivity.this,
                                "com.google.firebase.example.fireeats.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                uri);
                        startActivityForResult(intent, 0);
                    }
                }
            }
        }

    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {

            CropImage.activity(uri)
                    .start(this);

        } else if (requestCode == 2 && resultCode == RESULT_OK) {

            onSelectFromGalleryResult(data);

        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                processCropResult(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedMedia = data.getData();
        ContentResolver cr = RegistrationActivity.this.getContentResolver();
        String mime = cr.getType(selectedMedia);
        if (mime.toLowerCase().contains("image")) {
            Bitmap bitmap = null, scaled = null;
            if (data != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    int nh = (int) (bitmap.getHeight() * (800.0 / bitmap.getWidth()));
                    scaled = Bitmap.createScaledBitmap(bitmap, 800, nh, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            if (scaled != null) scaled.compress(Bitmap.CompressFormat.PNG, 90, bytes);
            photo = bitmap;
            iv_img.setImageBitmap(photo);
            cl1.setVisibility(View.VISIBLE);
        }
    }

    private void processCropResult(Uri uri) {

        try {
            photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
            iv_img.setImageBitmap(photo);
            cl1.setVisibility(View.VISIBLE);
        } catch (Exception e) {

        }

    }


    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected(RegistrationActivity.this);
    }

    public class FindExist extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final Query mQuery = db.collection("users").whereEqualTo("mail", mail_value);
            mQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot ds : task.getResult()) {
                            String userNames = ds.getString("mail");
                            if (userNames.equals(mail_value)) {
                                progressDialog.dismiss();
                                Util.showSnack(RegistrationActivity.this, "Email id Already Exist ");
                            }
                        }
                    }
                    //checking if task contains any payload. if no, then update
                    if (task.getResult().size() == 0) {
                        try {
                            progressDialog.dismiss();
//                                uploadFile(photo);
                        } catch (NullPointerException e) {
                        }
                    }
                }
            });
            return null;
        }
    }

    public class AddUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RegistrationActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            users = new Users(name_value, mail_value, desg_value);
            db.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            progressDialog.dismiss();
                            Util.showSnack(RegistrationActivity.this, "Registered Successfull");
                        }
                    }, 1000);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            progressDialog.dismiss();
                            Util.showSnack(RegistrationActivity.this, "Something WentWrong!");
                        }
                    }, 1000);

                }
            });
            return null;
        }


    }
}
