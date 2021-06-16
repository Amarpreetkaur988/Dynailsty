package omninos.com.dynailsty.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import omninos.com.dynailsty.MyViewModel.LoginRegisterViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;
import omninos.com.dynailsty.util.ConstantData;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText ed1, ed2, ed3, ed4;
    private String otp_1, otp_2, otp_3, otp_4, otp, userId;
    private TextView appBarTitle, resendOtpTv;
    private Button verifyBtn;
    private Activity activity;
    private LoginRegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        stopService(new Intent(OTPActivity.this, MySerives.class));
        Intent intent = new Intent(OTPActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }


        activity = OTPActivity.this;
        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel.class);

        initViews();
        setClickListeners();

        userId = App.getAppPreference().GetString(ConstantData.USERID);
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

    private void setClickListeners() {
        verifyBtn.setOnClickListener(this);
        resendOtpTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verify:
//                handleVerifyBtn();
                matchVerification();
                break;

            case R.id.resendOtpTv:
                resendVerification();
                break;
        }
    }

    private void handleVerifyBtn() {
        LayoutInflater factory = LayoutInflater.from(OTPActivity.this);
        final View congDialogBox = factory.inflate(R.layout.congratulations_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(OTPActivity.this).create();
        dialog.setView(congDialogBox);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        congDialogBox.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getAppPreference().SaveString(ConstantData.TOKEN, "1");
                startActivity(new Intent(OTPActivity.this, MainActivity.class));
                OTPActivity.this.finish();
            }
        });
        dialog.show();

    }

    private void initViews() {
        ed1 = findViewById(R.id.firstNumberVerify);
        ed2 = findViewById(R.id.secondNumberVerify);
        ed3 = findViewById(R.id.thirdNumberVerify);
        ed4 = findViewById(R.id.forthNumberVerify);
        appBarTitle = findViewById(R.id.appBarTitle);
        resendOtpTv = findViewById(R.id.resendOtpTv);

        appBarTitle.setText("Verification");
        verifyBtn = findViewById(R.id.verify);

        ed1.addTextChangedListener(generalTextWatcher);
        ed2.addTextChangedListener(generalTextWatcher);
        ed3.addTextChangedListener(generalTextWatcher);
        ed4.addTextChangedListener(generalTextWatcher);

    }

    private void matchVerification() {
        otp = otp_1 + otp_2 + otp_3 + otp_4;
        if (otp.length() != 4) {
            CommonUtils.showSnackbarAlert(ed1, "enter valid otp");
        } else {
            viewModel.MatchVerification(activity, userId, otp).observe(OTPActivity.this, new Observer<LoginRegisterModel>() {
                @Override
                public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                    if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {

                        App.getAppPreference().saveLoginDetail(loginRegisterModel);
                        handleVerifyBtn();

                    } else {

                        CommonUtils.showSnackbarAlert(ed1, loginRegisterModel.getMessage());

                    }
                }
            });
        }
    }

    private void resendVerification() {
        viewModel.ResendVerification(activity, userId).observe(OTPActivity.this, new Observer<LoginRegisterModel>() {
            @Override
            public void onChanged(@Nullable LoginRegisterModel loginRegisterModel) {
                if (loginRegisterModel.getSuccess().equalsIgnoreCase("1")) {

                    Toast.makeText(activity, loginRegisterModel.getDetails().getOtp(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    TextWatcher generalTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (ed1.getText().hashCode() == editable.hashCode()) {
                otp_1 = ed1.getText().toString().trim();
                if (!otp_1.equalsIgnoreCase("")) {
                    ed2.requestFocus();
                } else {
                }

            } else if (ed2.getText().hashCode() == editable.hashCode()) {
                otp_2 = ed2.getText().toString().trim();
                if (!otp_2.equalsIgnoreCase("")) {
                    ed3.requestFocus();
                } else {
                    ed1.requestFocus();
                }

            } else if (ed3.getText().hashCode() == editable.hashCode()) {
                otp_3 = ed3.getText().toString().trim();
                if (!otp_3.equalsIgnoreCase("")) {
                    ed4.requestFocus();
                } else {
                    ed2.requestFocus();
                }
            } else if (ed4.getText().hashCode() == editable.hashCode()) {
                otp_4 = ed4.getText().toString().trim();
                if (!otp_4.equalsIgnoreCase("")) {
                    hideKeyboard(activity);
                } else {
                    ed3.requestFocus();
                }
            }
        }
    };

    //hide keyboard
    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
