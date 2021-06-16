package omninos.com.dynailsty.MyViewModel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import omninos.com.dynailsty.Retrofit.Api;
import omninos.com.dynailsty.Retrofit.ApiClient;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.util.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRegisterViewModel extends ViewModel {

    private MutableLiveData<LoginRegisterModel> userRegister;

    public LiveData<LoginRegisterModel> register(Activity activity, String name, String email, String phone, String password, String device_type, String reg_id) {
        userRegister = new MutableLiveData<>();

        Api api = ApiClient.getApiClient().create(Api.class);
        CommonUtils.showProgress(activity);
        api.UserRegister(name, email, phone, password, device_type, reg_id).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    userRegister.setValue(response.body());
                } else {
                    LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                    loginRegisterModel.setSuccess("0");
                    loginRegisterModel.setMessage("Server Error");
                    userRegister.setValue(loginRegisterModel);
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                loginRegisterModel.setSuccess("0");
                loginRegisterModel.setMessage("Server Error");
                userRegister.setValue(loginRegisterModel);
            }
        });

        return userRegister;
    }

    private MutableLiveData<LoginRegisterModel> matchVerification;

    public LiveData<LoginRegisterModel> MatchVerification(Activity activity, String id, String token) {

        matchVerification = new MutableLiveData<>();
        Api api = ApiClient.getApiClient().create(Api.class);
        CommonUtils.showProgress(activity);
        api.MatchVerification(id, token).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    matchVerification.setValue(response.body());
                } else {
                    LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                    loginRegisterModel.setSuccess("0");
                    loginRegisterModel.setMessage("Server Error");
                    matchVerification.setValue(loginRegisterModel);
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                loginRegisterModel.setSuccess("0");
                loginRegisterModel.setMessage("Server Error");
                matchVerification.setValue(loginRegisterModel);
            }
        });

        return matchVerification;
    }

    private MutableLiveData<LoginRegisterModel> resendVerification;

    public LiveData<LoginRegisterModel> ResendVerification(Activity activity, String id) {

        resendVerification = new MutableLiveData<>();

        Api api = ApiClient.getApiClient().create(Api.class);
        CommonUtils.showProgress(activity);
        api.ResendVerification(id).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    resendVerification.setValue(response.body());
                } else {
                    LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                    loginRegisterModel.setSuccess("0");
                    loginRegisterModel.setMessage("Server Error");
                    resendVerification.setValue(loginRegisterModel);
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                loginRegisterModel.setSuccess("0");
                loginRegisterModel.setMessage("Server Error");
                resendVerification.setValue(loginRegisterModel);
            }
        });

        return resendVerification;
    }

    private MutableLiveData<LoginRegisterModel> userLogIn;

    public LiveData<LoginRegisterModel> UserLogIn(Activity activity, String email, String password, String reg_id, String device_type) {

        userLogIn = new MutableLiveData<>();
        CommonUtils.showProgress(activity);
        Api api = ApiClient.getApiClient().create(Api.class);
        api.UserLogin(email, password, reg_id, device_type).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    userLogIn.setValue(response.body());
                } else {
                    LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                    loginRegisterModel.setSuccess("0");
                    loginRegisterModel.setMessage("Server Error");
                    userLogIn.setValue(loginRegisterModel);
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                loginRegisterModel.setSuccess("0");
                loginRegisterModel.setMessage("Server Error");
                userLogIn.setValue(loginRegisterModel);
            }
        });

        return userLogIn;
    }
}
