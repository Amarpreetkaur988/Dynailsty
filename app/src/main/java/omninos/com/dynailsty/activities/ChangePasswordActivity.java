package omninos.com.dynailsty.activities;

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

import java.util.Map;

import omninos.com.dynailsty.MyViewModel.ChangePasswordViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout backLinearLayout;
    private EditText oldPassword, newPassword, confirmNewPassword;
    private String old, newPas, confPass;
    private Button changePasswordBtn;
    private ChangePasswordViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        stopService(new Intent(ChangePasswordActivity.this, MySerives.class));
        Intent intent = new Intent(ChangePasswordActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }

        viewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);

        initViews();
        setClicks();
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

    private void initViews() {
        backLinearLayout = findViewById(R.id.backLinearLayout);

        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);

        changePasswordBtn = findViewById(R.id.changePasswordBtn);

    }

    private void setClicks() {
        backLinearLayout.setOnClickListener(this);
        changePasswordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backLinearLayout:
                this.finish();
                break;

            case R.id.changePasswordBtn:
                UpdatePassword();
                break;


        }
    }

    private void UpdatePassword() {

        old = oldPassword.getText().toString();
        newPas = newPassword.getText().toString();
        confPass = confirmNewPassword.getText().toString();

        if (old.isEmpty()) {
            CommonUtils.showSnackbarAlert(oldPassword, "enter old password");
        } else if (newPas.isEmpty() || newPas.length() < 7) {
            CommonUtils.showSnackbarAlert(oldPassword, "enter minimum 8 character password");

        } else if (confPass.isEmpty() || !confPass.equals(newPas)) {
            CommonUtils.showSnackbarAlert(oldPassword, "confirm password mismatch");

        } else {

            viewModel.changePassword(ChangePasswordActivity.this, App.getAppPreference().getLoginDetail().getDetails().getId(), old, newPas).observe(ChangePasswordActivity.this, new Observer<Map>() {
                @Override
                public void onChanged(@Nullable Map map) {
                    if (map.get("success").toString().equalsIgnoreCase("1")) {
                        CommonUtils.showSnackbarAlert(oldPassword, map.get("message").toString());
                        onBackPressed();
                    } else {

                        CommonUtils.showSnackbarAlert(oldPassword, map.get("message").toString());
                    }
                }
            });
        }
    }
}
