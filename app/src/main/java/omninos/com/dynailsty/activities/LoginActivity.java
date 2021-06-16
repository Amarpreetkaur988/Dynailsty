package omninos.com.dynailsty.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;

import omninos.com.dynailsty.MyViewModel.LoginRegisterViewModel;
import omninos.com.dynailsty.MyViewModel.SocialLoginViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.fragments.RegisterFragment;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;
import omninos.com.dynailsty.util.ConstantData;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    CallbackManager callbackManager;

    private TextView appBarTitle;
    private Button loginBtn;
    private Activity activity;
    private EditText login_email, login_password;
    private LoginRegisterViewModel viewModel;
    private String email, pass, reg_id = "1", device_type = "Android";
    private RelativeLayout facebookSignIn;
    AccessToken accessToken;
    private String userIdfacebook, firstName, lastName, socialUsersername, SocialImage = "";
    private URL profilePicture;
    private SocialLoginViewModel socialLoginViewModel;
    private LinearLayout bottomText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InitFacebook();

        stopService(new Intent(LoginActivity.this, MySerives.class));
        Intent intent = new Intent(LoginActivity.this, MySerives.class);
        if (!isMyServiceRunning1(intent.getClass())) {
            startService(intent);
        }
        socialLoginViewModel = ViewModelProviders.of(this).get(SocialLoginViewModel.class);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        initViews();
        setOnClickListeners();
    }


    //facebook Login
    private void InitFacebook() {
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
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
        loginBtn.setOnClickListener(this);
    }

    private void initViews() {
        activity = LoginActivity.this;
        appBarTitle = findViewById(R.id.appBarTitle);
        appBarTitle.setText("Login");
        loginBtn = findViewById(R.id.loginBtn);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        facebookSignIn = findViewById(R.id.facebookSignIn);

        facebookSignIn.setOnClickListener(this);

        bottomText = findViewById(R.id.bottomText);
        bottomText.setOnClickListener(this);
    }

    private void userLogIn() {
        email = login_email.getText().toString();
        pass = login_password.getText().toString();

        if (email.isEmpty()) {
            CommonUtils.showSnackbarAlert(login_email, "enter email");
        } else if (pass.isEmpty()) {
            CommonUtils.showSnackbarAlert(login_password, "enter password");
        } else {
            viewModel.UserLogIn(activity, email, pass, reg_id, device_type).observe(LoginActivity.this, new Observer<LoginRegisterModel>() {
                @Override
                public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                    if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                        App.getAppPreference().SaveString(ConstantData.TOKEN, "1");
                        App.getAppPreference().saveLoginDetail(loginRegisterModel);
                        startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    } else if (loginRegisterModel.getSuccess().equalsIgnoreCase("2")) {
                        App.getAppPreference().SaveString(ConstantData.USERID, loginRegisterModel.getDetails().getId());
                        Toast.makeText(activity, loginRegisterModel.getDetails().getOtp(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(activity, OTPActivity.class));
                    } else {
                        CommonUtils.showSnackbarAlert(login_email, loginRegisterModel.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                userLogIn();
                break;

            case R.id.facebookSignIn:
                LoginWithFaceBook();
                break;
            case R.id.bottomText:
                startActivity(new Intent(activity, RegisterActivity.class).putExtra("Type", "0"));
                break;
        }
    }


    //get facebook Data
    private void LoginWithFaceBook() {

        if (CommonUtils.isNetworkConnected(activity)) {

            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {

                            Log.e("LoginActivity", "onSuccess method called");
                            getFacebookData(loginResult);
                            accessToken = loginResult.getAccessToken();
                            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                            Log.e("LoginActivity", "accessToken=>" + accessToken);
                        }

                        @Override
                        public void onCancel() {

                            Log.e("LoginActivity", "onCancel() method called");
                        }

                        @Override
                        public void onError(FacebookException exception) {

                            Log.e("LoginActivity", "onError method call" + exception);
                        }
                    });

        } else {
            Toast.makeText(activity, "Network Issue", Toast.LENGTH_SHORT).show();
        }

    }

    //Get facebook Data
    private void getFacebookData(LoginResult loginResult) {
        CommonUtils.showProgress(activity);
        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                CommonUtils.dismissProgress();
                if (object != null) {
                    try {
                        if (object.has("id")) {

                            userIdfacebook = object.getString("id");

                            Log.e("LoginActivity", "id" + userIdfacebook);

                        }
                        if (object.has("first_name")) {
                            firstName = object.getString("first_name");
                            Log.e("LoginActivity", "first_name" + firstName);

                        }
                        //check permisson last userName
                        if (object.has("last_name")) {
                            lastName = object.getString("last_name");
                            Log.e("LoginActivity", "last_name" + lastName);
                        }
                        //check permisson email
                        if (object.has("email")) {
                            email = object.getString("email");
                            Log.e("LoginActivity", "email" + email);
                        }

                        socialUsersername = firstName + " " + lastName;
                        Log.e("LoginActivity", "fbLastName" + socialUsersername);

                        JSONObject jsonObject = new JSONObject(object.getString("picture"));
                        Log.e("Loginactivity", "json oject get picture" + jsonObject);
                        if (jsonObject != null) {
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            Log.e("Loginactivity", "json oject get picture" + dataObject);
                            profilePicture = new URL("https://graph.facebook.com/" + userIdfacebook + "/picture?width=500&height=500");
                            Log.e("LoginActivity", "json object=>" + profilePicture);
                        }

                        SocialImage = String.valueOf(profilePicture);
                        App.getAppPreference().SaveString(ConstantData.LOGIN_TYPE, "FB");
                        CheckSocialId();
//                            Intent intent = new Intent(activity, MobileNumberActivity.class);
//                            intent.putExtra("name", socialUsersername);
//                            intent.putExtra("SocailId", userIdfacebook);
//                            intent.putExtra("email", email);
//                            intent.putExtra("image", SocialImage);
//                            startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        //Set Permissions
        Bundle bundle = new Bundle();
        Log.e("LoginActivity", "bundle set");
        bundle.putString("fields", "id, first_name, last_name,email,picture,gender");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    private void CheckSocialId() {
        socialLoginViewModel.checkSocial(activity, userIdfacebook).observe(LoginActivity.this, new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                    App.getAppPreference().SaveString(ConstantData.TOKEN, "1");
                    App.getAppPreference().SaveString(ConstantData.USERID, loginRegisterModel.getDetails().getId());
                    App.getAppPreference().saveLoginDetail(loginRegisterModel);
                } else {
                    Intent intent = new Intent(activity, RegisterActivity.class);
                    intent.putExtra("Type", "1");
                    intent.putExtra("name", firstName);
                    intent.putExtra("last", lastName);
                    intent.putExtra("SocailId", userIdfacebook);
                    intent.putExtra("email", email);
                    intent.putExtra("image", SocialImage);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
