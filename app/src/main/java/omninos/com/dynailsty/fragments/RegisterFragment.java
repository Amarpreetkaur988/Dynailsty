package omninos.com.dynailsty.fragments;


import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import omninos.com.dynailsty.MyViewModel.LoginRegisterViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.activities.OTPActivity;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;
import omninos.com.dynailsty.util.ConstantData;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private TextView appBarTitle;
    private Button registerBtn;
    private Activity activity;
    private EditText firstName, lastName, login_email, phoneNuber, password, confirmPassword;
    private String name, lname, email, phone_Nuber, pass, confpass, device_type = "Android", reg_id = "1";

    private LoginRegisterViewModel viewModel;

    public RegisterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().stopService(new Intent(getActivity(), MySerives.class));
        Intent intent = new Intent(getActivity(), MySerives.class);
        if (!isMyServiceRunning1(intent.getClass())) {
            getActivity().startService(intent);
        }
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);
        initViews(v);
        setOnClickListeners();
        return v;
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
        registerBtn.setOnClickListener(this);
    }

    private void initViews(View v) {
        appBarTitle = v.findViewById(R.id.appBarTitle);
        appBarTitle.setText("Register");
        registerBtn = v.findViewById(R.id.registerBtn);
        activity = getActivity();

        firstName = v.findViewById(R.id.firstName);
        lastName = v.findViewById(R.id.lastName);
        login_email = v.findViewById(R.id.login_email);
        phoneNuber = v.findViewById(R.id.phoneNuber);
        password = v.findViewById(R.id.password);
        confirmPassword = v.findViewById(R.id.confirmPassword);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerBtn:
                UserRegister();
//                startActivity(new Intent(activity, OTPActivity.class));
//                activity.finish();
                break;
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
            viewModel.register(getActivity(), name, email, phone_Nuber, pass, device_type, reg_id).observe(getActivity(), new Observer<LoginRegisterModel>() {
                @Override
                public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                    if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {
                        Toast.makeText(activity, loginRegisterModel.getDetails().getOtp(), Toast.LENGTH_SHORT).show();
                        App.getAppPreference().SaveString(ConstantData.USERID,loginRegisterModel.getDetails().getId());
                        startActivity(new Intent(getActivity(), OTPActivity.class));
                        Toast.makeText(activity, "Successful", Toast.LENGTH_SHORT).show();

                    } else {
                        CommonUtils.showSnackbarAlert(firstName, loginRegisterModel.getMessage());
                    }
                }
            });
        }
    }
}
