package omninos.com.dynailsty.activities;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.Calendar;
import java.util.Map;

import omninos.com.dynailsty.MyViewModel.PaymentViewModel;
import omninos.com.dynailsty.MyViewModel.ServicesViewModel;
import omninos.com.dynailsty.R;
import omninos.com.dynailsty.model.PaypalTokenModel;
import omninos.com.dynailsty.services.MySerives;
import omninos.com.dynailsty.util.App;
import omninos.com.dynailsty.util.CommonUtils;

public class SelectPaymentMethodActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton creditCardRb, debitCardRb, netBankingRb, cashRb;

    private RelativeLayout creditCardHiddenRelativeLayout, debitCardHiddenRelativeLayout;

    private Button cashbookAppointmentBtn, debitCardBookAppointmentBtn, creditCardBookAppointmentBtn;

    private LinearLayout backLinearLayout, creditCardLinearLayout, debitCardLinearLayout, netBankingLinearLayout, cashLinearLayout;
    private String paymentType = "";
    private ServicesViewModel viewModel;
    private int value = 0;

    private PaymentViewModel paymentViewModel;

    private EditText expiryDate, et_name, et_last, et_card_number, cvvNumber;
    private String dateData, name, last, number, cvv;

    int cardMonth, cardYear, currentYear, currentMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_method);

        stopService(new Intent(SelectPaymentMethodActivity.this, MySerives.class));
        Intent intent = new Intent(SelectPaymentMethodActivity.this, MySerives.class);
        if (!isMyServiceRunning(intent.getClass())) {
            startService(intent);
        }
        viewModel = ViewModelProviders.of(this).get(ServicesViewModel.class);

        paymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
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

        //Credit Card
        creditCardRb = findViewById(R.id.creditCardRb);
        creditCardHiddenRelativeLayout = findViewById(R.id.creditCardHiddenRelativeLayout);
        creditCardLinearLayout = findViewById(R.id.creditCardLinearLayout);

        //Debit Card
        debitCardRb = findViewById(R.id.debitCardRb);
        debitCardLinearLayout = findViewById(R.id.debitCardLinearLayout);
        debitCardHiddenRelativeLayout = findViewById(R.id.debitCardHiddenRelativeLayout);

        //Net Banking
        netBankingLinearLayout = findViewById(R.id.netBankingLinearLayout);
        netBankingRb = findViewById(R.id.netBankingRb);


        //cash
        cashRb = findViewById(R.id.cashRb);
        cashLinearLayout = findViewById(R.id.cashLinearLayout);

        cashbookAppointmentBtn = findViewById(R.id.cashBookAppointmentBtn);
        cashbookAppointmentBtn.setVisibility(View.GONE);

        debitCardBookAppointmentBtn = findViewById(R.id.debitCardBookAppointmentBtn);
        creditCardBookAppointmentBtn = findViewById(R.id.creditCardBookAppointmentBtn);


        //credit
        expiryDate = findViewById(R.id.expiryDate);

        expiryDate.setOnClickListener(this);

        et_name = findViewById(R.id.et_name);
        et_last = findViewById(R.id.et_last);
        et_card_number = findViewById(R.id.et_card_number);
        cvvNumber = findViewById(R.id.cvvNumber);


    }

    private void setClicks() {
        backLinearLayout.setOnClickListener(this);

        creditCardRb.setOnClickListener(this);
        debitCardRb.setOnClickListener(this);
        netBankingRb.setOnClickListener(this);
        cashRb.setOnClickListener(this);
        cashbookAppointmentBtn.setOnClickListener(this);
        debitCardBookAppointmentBtn.setOnClickListener(this);
        creditCardBookAppointmentBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backLinearLayout:
                this.finish();
                break;
            case R.id.creditCardRb:
                deselectAllMethods();
                creditCardRb.setChecked(true);
                creditCardHiddenRelativeLayout.setVisibility(View.VISIBLE);
                creditCardLinearLayout.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
                break;

            case R.id.debitCardRb:
                deselectAllMethods();
                debitCardRb.setChecked(true);
                debitCardHiddenRelativeLayout.setVisibility(View.VISIBLE);
                debitCardLinearLayout.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
                break;

            case R.id.netBankingRb:
                value = 1;
                deselectAllMethods();
                netBankingRb.setChecked(true);
                netBankingLinearLayout.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
//                CommonUtils.showSnackbarAlert(cashbookAppointmentBtn, "Under Development");
                OpenConfirmBox();
//                cashbookAppointmentBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.cashRb:
                deselectAllMethods();
                cashRb.setChecked(true);
                cashLinearLayout.setBackgroundResource(R.drawable.btn_background_yellow_gradient);
                cashbookAppointmentBtn.setVisibility(View.VISIBLE);
                break;

            case R.id.cashBookAppointmentBtn:
                value = 2;
                OpenConfirmBox();
                break;

            case R.id.debitCardBookAppointmentBtn:
//                BookingAppointment("1");
                CommonUtils.showSnackbarAlert(cashbookAppointmentBtn, "Under Development");
                break;

            case R.id.creditCardBookAppointmentBtn:
//                BookingAppointment("1");
                value = 3;
                OpenConfirmBox();
                break;

            case R.id.expiryDate:
                OpenDialog();
                break;

        }
    }

    private void Validate() {
        name = et_name.getText().toString();
        last = et_last.getText().toString();
        number = et_card_number.getText().toString();
        cvv = cvvNumber.getText().toString();
        dateData = expiryDate.getText().toString();
        if (name.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_name, "enter first name");
        } else if (last.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_name, "enter last name");

        } else if (number.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_name, "enter card number");

        } else if (cvv.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_name, "enter cvv number");

        } else if (dateData.isEmpty()) {
            CommonUtils.showSnackbarAlert(et_name, "enter expiry date");

        } else if (currentYear <= cardYear) {
            if (currentYear == cardYear) {
                if (currentMonth > cardMonth) {
                    CommonUtils.showSnackbarAlert(et_name, "Enter valid exp month");
                } else {
                    Verifycard(number, cardMonth, cardYear, cvv);
                }
            } else {
                Verifycard(number, cardMonth, cardYear, cvv);
            }
        } else {
            Toast.makeText(this, "enter valid expiry date", Toast.LENGTH_SHORT).show();
        }
    }


    private void deselectAllMethods() {
        cashbookAppointmentBtn.setVisibility(View.GONE);

        //credit Card
        creditCardRb.setChecked(false);
        creditCardHiddenRelativeLayout.setVisibility(View.GONE);
        creditCardLinearLayout.setBackgroundResource(R.drawable.select_payment_method_back);

        //debit Card
        debitCardRb.setChecked(false);
        debitCardHiddenRelativeLayout.setVisibility(View.GONE);
        debitCardLinearLayout.setBackgroundResource(R.drawable.select_payment_method_back);

        //net Banking
        netBankingRb.setChecked(false);
        netBankingLinearLayout.setBackgroundResource(R.drawable.select_payment_method_back);

        //cash
        cashRb.setChecked(false);
        cashLinearLayout.setBackgroundResource(R.drawable.select_payment_method_back);
    }

    private void openDialog() {

        LayoutInflater factory = LayoutInflater.from(SelectPaymentMethodActivity.this);
        final View congDialogBox = factory.inflate(R.layout.congratulations_booking_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(SelectPaymentMethodActivity.this).create();
        dialog.setView(congDialogBox);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        congDialogBox.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectPaymentMethodActivity.this, MainActivity.class));
                finishAffinity();
            }
        });
        dialog.show();


    }

    private void OpenConfirmBox() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
//                        startActivity(new Intent(PaymentActivity.this, HomeActivity.class));
//                        finishAffinity();
//                        BookingAppointment("0");
                        if (value == 2) {
                            Newbooking("cash", " ");
                        } else if (value == 1) {
                            Gettoken();
                        } else if (value == 3) {
                            Validate();
                        }

                        dialog.dismiss();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.cancel();
                        break;
                }
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SelectPaymentMethodActivity.this);
        builder.setMessage("Are you Confirm ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void Gettoken() {
        paymentViewModel.paypalTokenModelLiveData(SelectPaymentMethodActivity.this).observe(SelectPaymentMethodActivity.this, new Observer<PaypalTokenModel>() {
            @Override
            public void onChanged(@Nullable PaypalTokenModel paypalTokenModel) {
                if (paypalTokenModel.getSuccess().equalsIgnoreCase("1")) {
                    submitPayment(paypalTokenModel.getDetails());
                } else {
                    CommonUtils.showSnackbarAlert(creditCardLinearLayout, paypalTokenModel.getMessage());
                }
            }
        });
    }

    private void submitPayment(String token) {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this), 1234);
    }

    private void Newbooking(String paymentMethod, String token) {
        viewModel.bookNewbooking(SelectPaymentMethodActivity.this, paymentMethod, App.getAppPreference().getLoginDetail().getDetails().getId(), App.getSinltonPojo().getBarbarId(), App.getSinltonPojo().getCategoryId(), App.getSinltonPojo().getBookingDate(), App.getSinltonPojo().getBookingTime(), App.getSinltonPojo().getServicePrice(), token).observe(SelectPaymentMethodActivity.this, new Observer<Map>() {
            @Override
            public void onChanged(@Nullable Map map) {
                if (map.get("success").toString().equalsIgnoreCase("1")) {
                    openDialog();
                } else {
                    Toast.makeText(SelectPaymentMethodActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1234:
                try {
                    DropInResult result1 = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                    String nonces = result1.getPaymentMethodNonce().getNonce();
                    Newbooking("paypal", nonces);
                    Toast.makeText(this, nonces, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
                break;

            case 0:
                Toast.makeText(this, "User cancel", Toast.LENGTH_SHORT).show();
                break;

            default:
                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);

                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void OpenDialog() {

        final Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
        currentMonth = mMonth;
        currentYear = mYear;
        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(SelectPaymentMethodActivity.this,
                android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                expiryDate.setText((month + 1) + "/" + year);
                cardMonth = month + 1;
                cardYear = year;
            }
        }, mYear, mMonth, mDay) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            }
        };
        monthDatePickerDialog.setTitle("select_month");
        monthDatePickerDialog.show();
    }

    public void Verifycard(String cardNumber, int cardExpMonth, int cardExpYear, String cardCVC) {
        CommonUtils.showProgress(SelectPaymentMethodActivity.this);
        Card card = new Card(
                cardNumber,
                cardExpMonth,
                cardExpYear,
                cardCVC
        );

        card.validateNumber();
        card.validateCVC();

        if (card != null) {
            Stripe stripe = new Stripe(this, "pk_test_3lWtnSnKLmRPg5RJZLBiHbH6009BDRluME");
            stripe.createToken(card, new TokenCallback() {
                @Override
                public void onError(Exception error) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(SelectPaymentMethodActivity.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(Token token) {
                    CommonUtils.dismissProgress();
                    Toast.makeText(SelectPaymentMethodActivity.this, "Success " + token.getId(), Toast.LENGTH_SHORT).show();
                    Log.d("onSuccess: ", token.getId());
                    Newbooking("stripe", token.getId());
                }
            });
        }
    }
}
