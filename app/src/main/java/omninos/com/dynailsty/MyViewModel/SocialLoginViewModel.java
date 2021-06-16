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

public class SocialLoginViewModel extends ViewModel {

    private MutableLiveData<LoginRegisterModel> checkId;

    private MutableLiveData<LoginRegisterModel> socialLogin;


    public LiveData<LoginRegisterModel> checkSocial(Activity activity, String socialId) {
        checkId = new MutableLiveData<>();

        CommonUtils.showProgress(activity);
        Api api = ApiClient.getApiClient().create(Api.class);
        api.socailIdCheck(socialId).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    checkId.setValue(response.body());
                } else {

                    LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                    loginRegisterModel.setMessage("Server Error");
                    loginRegisterModel.setSuccess("0");
                    checkId.setValue(loginRegisterModel);
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                loginRegisterModel.setMessage("Server Error");
                loginRegisterModel.setSuccess("0");
                checkId.setValue(loginRegisterModel);
            }
        });

        return checkId;
    }

    public LiveData<LoginRegisterModel> socialLoginLive(Activity activity, String social_id, String email, String phone, String name, String reg_id, String image, String device_type, String login_type) {
        socialLogin = new MutableLiveData<>();
        Api api = ApiClient.getApiClient().create(Api.class);


        CommonUtils.showProgress(activity);
        api.socialLoginAPI(social_id, email, phone, name, reg_id, image, device_type, login_type).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    socialLogin.setValue(response.body());

                } else {
                    LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                    loginRegisterModel.setMessage("Server Error");
                    loginRegisterModel.setSuccess("0");
                    checkId.setValue(loginRegisterModel);
                }

            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                LoginRegisterModel loginRegisterModel = new LoginRegisterModel();
                loginRegisterModel.setMessage("Network Issue");
                loginRegisterModel.setSuccess("0");
                checkId.setValue(loginRegisterModel);
            }
        });

        return socialLogin;
    }

}
