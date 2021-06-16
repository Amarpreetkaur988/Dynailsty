package omninos.com.dynailsty.fragments;


import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import omninos.com.dynailsty.activities.MainActivity;
import omninos.com.dynailsty.activities.OTPActivity;
import omninos.com.dynailsty.activities.RegisterLoginActivity;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;
import omninos.com.dynailsty.util.ConstantData;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginFragment extends Fragment implements View.OnClickListener {

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

    public LoginFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        InitFacebook();

        getActivity().stopService(new Intent(getActivity(), MySerives.class));
        Intent intent = new Intent(getActivity(), MySerives.class);
        if (!isMyServiceRunning1(intent.getClass())) {
            getActivity().startService(intent);
        }
        socialLoginViewModel = ViewModelProviders.of(this).get(SocialLoginViewModel.class);
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        initViews(v);
        setOnClickListeners();
        return v;
    }

    //facebook Login
    private void InitFacebook() {
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());
    }

    private boolean isMyServiceRunning1(Class<? extends Intent> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
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

    private void initViews(View v) {
        activity = getActivity();
        appBarTitle = v.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Login");
        loginBtn = v.findViewById(R.id.loginBtn);
        login_email = v.findViewById(R.id.login_email);
        login_password = v.findViewById(R.id.login_password);

        facebookSignIn = v.findViewById(R.id.facebookSignIn);

        facebookSignIn.setOnClickListener(this);
    }

    private void userLogIn() {
        email = login_email.getText().toString();
        pass = login_password.getText().toString();

        if (email.isEmpty()) {
            CommonUtils.showSnackbarAlert(login_email, "enter email");
        } else if (pass.isEmpty()) {
            CommonUtils.showSnackbarAlert(login_password, "enter password");
        } else {
            viewModel.UserLogIn(getActivity(), email, pass, reg_id, device_type).observe(getActivity(), new Observer<LoginRegisterModel>() {
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

        GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
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
        socialLoginViewModel.checkSocial(getActivity(), userIdfacebook).observe(getActivity(), new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                    App.getAppPreference().SaveString(ConstantData.TOKEN, "1");
                    App.getAppPreference().SaveString(ConstantData.USERID, loginRegisterModel.getDetails().getId());
                    App.getAppPreference().saveLoginDetail(loginRegisterModel);
                } else {
                    loadFragment();
//                    Intent intent = new Intent(activity, MobileNumberActivity.class);
//                    intent.putExtra("name", socialUsersername);
//                    intent.putExtra("SocailId", userIdfacebook);
//                    intent.putExtra("email", email);
//                    intent.putExtra("image", SocialImage);
//                    startActivity(intent);
                }
            }
        });
    }

    private void loadFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.containerLoginRegister, new RegisterFragment(), "NewFragmentTag");
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }
}
