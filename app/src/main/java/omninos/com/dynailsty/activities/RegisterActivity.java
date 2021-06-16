package omninos.com.dynailsty.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import omninos.com.dynailsty.MyViewModel.LoginRegisterViewModel;
import omninos.com.dynailsty.MyViewModel.SocialLoginViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;
import omninos.com.dynailsty.util.ConstantData;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView appBarTitle, textPass, confPass;
    private Button registerBtn;
    private Activity activity;
    private EditText firstName, lastName, login_email, phoneNuber, password, confirmPassword;
    private String name, lname, email, phone_Nuber, pass, confpass, device_type = "Android", reg_id = "1";

    private LoginRegisterViewModel viewModel;
    private LinearLayout bottomText;
    private SocialLoginViewModel socialLoginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        stopService(new Intent(RegisterActivity.this, MySerives.class));
        Intent intent = new Intent(RegisterActivity.this, MySerives.class);
        if (!isMyServiceRunning1(intent.getClass())) {
            startService(intent);
        }

        socialLoginViewModel = ViewModelProviders.of(this).get(SocialLoginViewModel.class);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        initViews();
        setOnClickListeners();
    }

    private boolean isMyServiceRunning1(Class<? extends Intent> serviceClass) {
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

    private void setOnClickListeners() {
        registerBtn.setOnClickListener(this);
        bottomText.setOnClickListener(this);
    }

    private void initViews() {
        appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("Register");
        registerBtn = findViewById(R.id.registerBtn);
        activity = RegisterActivity.this;

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        login_email = findViewById(R.id.login_email);
        phoneNuber = findViewById(R.id.phoneNuber);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);

        bottomText = findViewById(R.id.bottomText);
        textPass = findViewById(R.id.textPass);
        confPass = findViewById(R.id.confPass);

        if (getIntent().getStringExtra("Type").equalsIgnoreCase("1")) {
            password.setVisibility(View.GONE);
            confirmPassword.setVisibility(View.GONE);
            textPass.setVisibility(View.GONE);
            confPass.setVisibility(View.GONE);
            firstName.setText(getIntent().getStringExtra("name"));
            lastName.setText(getIntent().getStringExtra("last"));
            login_email.setText(getIntent().getStringExtra("email"));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:
                if (getIntent().getStringExtra("Type").equalsIgnoreCase("1")) {
                    SocialLogin();
                } else {
                    UserRegister();
                }

//                startActivity(new Intent(activity, OTPActivity.class));
//                activity.finish();
                break;
            case R.id.bottomText:
                onBackPressed();
                break;
        }
    }

    private void SocialLogin() {
        name = firstName.getText().toString();
        lname = lastName.getText().toString();
        email = login_email.getText().toString();
        phone_Nuber = phoneNuber.getText().toString();

        if (name.isEmpty()) {
            CommonUtils.showSnackbarAlert(firstName, "enter name");
        } else if (lname.isEmpty()) {
            CommonUtils.showSnackbarAlert(lastName, "enter last name");
        } else if (email.isEmpty()) {
            CommonUtils.showSnackbarAlert(login_email, "enter email");
        } else if (phone_Nuber.isEmpty()) {
            CommonUtils.showSnackbarAlert(phoneNuber, "enter phone number");
        } else {
            socialLoginViewModel.socialLoginLive(activity, getIntent().getStringExtra("SocailId"), email, phone_Nuber, name + " " + lname, reg_id, getIntent().getStringExtra("image"), device_type, "Social Login").observe(RegisterActivity.this, new Observer<LoginRegisterModel>() {
                @Override
                public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                    if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                        Toast.makeText(activity, loginRegisterModel.getDetails().getOtp(), Toast.LENGTH_SHORT).show();
                        App.getAppPreference().SaveString(ConstantData.USERID, loginRegisterModel.getDetails().getId());
                        startActivity(new Intent(RegisterActivity.this, OTPActivity.class));
                        Toast.makeText(activity, "Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        CommonUtils.showSnackbarAlert(firstName, loginRegisterModel.getMessage());
                    }
                }
            });
        }
    }

    private void UserRegister() {
        name = firstName.getText().toString();
        lname = lastName.getText().toString();
        email = login_email.getText().toString();
        phone_Nuber = phoneNuber.getText().toString();
        pass = password.getText().toString();
        confpass = confirmPassword.getText().toString();

        if (name.isEmpty()) {
            CommonUtils.showSnackbarAlert(firstName, "enter name");
        } else if (lname.isEmpty()) {
            CommonUtils.showSnackbarAlert(lastName, "enter last name");
        } else if (email.isEmpty()) {
            CommonUtils.showSnackbarAlert(login_email, "enter email");
        } else if (phone_Nuber.isEmpty()) {
            CommonUtils.showSnackbarAlert(phoneNuber, "enter phone number");
        } else if (pass.isEmpty() || pass.length() < 7) {
            CommonUtils.showSnackbarAlert(password, "enter password");
        } else if (confpass.isEmpty() || !confpass.equals(pass)) {
            CommonUtils.showSnackbarAlert(confirmPassword, "password mismatch");
        } else {
            viewModel.register(activity, name, email, phone_Nuber, pass, device_type, reg_id).observe(RegisterActivity.this, new Observer<LoginRegisterModel>() {
                @Override
                public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                    if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                        Toast.makeText(activity, loginRegisterModel.getDetails().getOtp(), Toast.LENGTH_SHORT).show();
                        App.getAppPreference().SaveString(ConstantData.USERID, loginRegisterModel.getDetails().getId());
                        startActivity(new Intent(RegisterActivity.this, OTPActivity.class));
                        Toast.makeText(activity, "Successful", Toast.LENGTH_SHORT).show();

                    } else {
                        CommonUtils.showSnackbarAlert(firstName, loginRegisterModel.getMessage());
                    }
                }
            });
        }
    }
}
