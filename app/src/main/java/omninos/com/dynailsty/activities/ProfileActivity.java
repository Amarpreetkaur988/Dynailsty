package omninos.com.dynailsty.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import omninos.com.dynailsty.BuildConfig;
import omninos.com.dynailsty.MyViewModel.ProfileViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;
import omninos.com.dynailsty.util.ImageUtil;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name, et_mail, et_number;
    private String name, email, number;
    private RelativeLayout updateButton;
    private CircleImageView userImg;
    private ProfileActivity activity;

    private Uri uri;
    private File photoFile;
    private static final int GALLERY_REQUEST = 101;

    private static final int CAMERA_REQUEST = 102;
    private String imagepath = "", previousNumber;

    private ProfileViewModel viewModel;
    private LinearLayout backLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        stopService(new Intent(ProfileActivity.this, MySerives.class));
        Intent intent = new Intent(ProfileActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }

        activity = ProfileActivity.this;

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        initView();
        SetUps();
    }

    private boolean isMyServiceRunning(Class<? extends Intent> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    private void initView() {
        et_name = findViewById(R.id.et_name);
        et_mail = findViewById(R.id.et_mail);
        et_number = findViewById(R.id.et_number);
        updateButton = findViewById(R.id.updateButton);
        userImg = findViewById(R.id.userImg);
        backLinearLayout = findViewById(R.id.backLinearLayout);


        et_name.setText(App.getAppPreference().getLoginDetail().getDetails().getName());
        et_mail.setText(App.getAppPreference().getLoginDetail().getDetails().getEmail());
        et_number.setText(App.getAppPreference().getLoginDetail().getDetails().getPhone());

        previousNumber = App.getAppPreference().getLoginDetail().getDetails().getPhone();

        if (!App.getAppPreference().getLoginDetail().getDetails().getImage().isEmpty()) {
            Glide.with(activity).load(App.getAppPreference().getLoginDetail().getDetails().getImage()).into(userImg);
        }

    }

    private void SetUps() {
        updateButton.setOnClickListener(this);
        userImg.setOnClickListener(this);
        backLinearLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userImg:
                GetPermissions();
                break;

            case R.id.updateButton:
                UpdateData();
                break;

            case R.id.backLinearLayout:
                onBackPressed();
                break;
        }
    }

    private void UpdateData() {

        MultipartBody.Part user_image;
        name = et_name.getText().toString();
        email = et_mail.getText().toString();
        number = et_number.getText().toString();
        if (name.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_name, "enter name");
        } else if (email.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_mail, "enter email");
        } else if (number.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_number, "enter number");
        } else {
            RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), App.getAppPreference().getLoginDetail().getDetails().getId());
            RequestBody numberBody = RequestBody.create(MediaType.parse("text/plain"), number);

            if (!imagepath.isEmpty()) {
                File file = new File(imagepath);
                final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                user_image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            } else {
//                File file = new File(App.getAppPreferences().getUserImagePath(activity));
                final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                user_image = MultipartBody.Part.createFormData("image", "", requestFile);
            }

            viewModel.update(activity, nameBody, numberBody, idBody, user_image).observe(ProfileActivity.this, new Observer<LoginRegisterModel>() {
                @Override
                public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                    if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                        if (!previousNumber.equalsIgnoreCase(number)) {
                            Toast.makeText(activity, loginRegisterModel.getDetails().getOtp(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProfileActivity.this, OTPActivity.class));
                        } else {
                            App.getAppPreference().saveLoginDetail(loginRegisterModel);
                            CommonUtils.showSnackbarAlert(et_mail, loginRegisterModel.getMessage());
                            onBackPressed();
                        }

                    }
                }
            });


        }
    }

    private void GetPermissions() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.WRITE_EXTERNAL_STORAGE + Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
        } else {
            OpenDialog();
        }

    }

    private void OpenDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    //choose from camera
    private void cameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            //Create a file to store the image
            photoFile = null;
            photoFile = ImageUtil.getTemporaryCameraFile();
            if (photoFile != null) {
                Uri uri = FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(pictureIntent,
                        CAMERA_REQUEST);
            }
        }
    }

    //Choose from gallery
    private void galleryIntent() {
        //gallery intent
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("profilePic/*");
        startActivityForResult(intent, GALLERY_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == CAMERA_REQUEST) {
                //set image path
                imagepath = ImageUtil.compressImage(photoFile.getAbsolutePath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap mBitmapInsurance = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
                //set image preview on imageView
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotated = Bitmap.createBitmap(mBitmapInsurance, 0, 0, mBitmapInsurance.getWidth(), mBitmapInsurance.getHeight(),
                        matrix, true);

                if (Build.VERSION.SDK_INT > 25 && Build.VERSION.SDK_INT < 27) {
                    // Do something for lollipop and above versions
                    userImg.setImageBitmap(rotated);
                } else {
                    // do something for phones running an SDK before lollipop
                    userImg.setImageBitmap(mBitmapInsurance);
                }
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            uri = data.getData();
            if (uri != null) {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                int column_index_data = cursor.getColumnIndex(projection[0]);
                cursor.moveToFirst();
                imagepath = cursor.getString(column_index_data);
                //set image preview on Image View
                Glide.with(activity).load("file://" + imagepath).into(userImg);
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean read = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean write = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean camera = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                if (grantResults.length > 0 && read && write && camera) {
                    OpenDialog();
                } else if (Build.VERSION.SDK_INT > 23 && !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.WRITE_EXTERNAL_STORAGE + Manifest.permission.CAMERA)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Permissions");
                    builder.setMessage("Permissions are requeired");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity.getApplicationContext(), "Go to the setting for granting permissions", Toast.LENGTH_SHORT).show();
                            boolean sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    })
                            .create()
                            .show();

                } else {
                    Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 100);
                }
                break;
        }
    }
}
