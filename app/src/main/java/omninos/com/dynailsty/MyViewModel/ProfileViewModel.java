package omninos.com.dynailsty.MyViewModel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.facebook.login.Login;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import omninos.com.dynailsty.Retrofit.Api;
import omninos.com.dynailsty.Retrofit.ApiClient;
import omninos.com.dynailsty.model.LoginRegisterModel;
import omninos.com.dynailsty.util.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<LoginRegisterModel> updateProfile;

    public LiveData<LoginRegisterModel> update(Activity activity, RequestBody name, RequestBody phone, RequestBody userId, MultipartBody.Part image) {

        updateProfile = new MutableLiveData<>();

        CommonUtils.showProgress(activity);
        Api api= ApiClient.getApiClient().create(Api.class);
        api.updateProfile(name, phone, userId, image).enqueue(new Callback<LoginRegisterModel>() {
            @Override
            public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                CommonUtils.dismissProgress();
                if (response.body()!=null){
                    updateProfile.setValue(response.body());
                }else {
                    LoginRegisterModel loginRegisterModel=new LoginRegisterModel();
                    loginRegisterModel.setSuccess("0");
                    loginRegisterModel.setMessage("Server Error");
                    updateProfile.setValue(loginRegisterModel);
                }
            }

            @Override
            public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                LoginRegisterModel loginRegisterModel=new LoginRegisterModel();
                loginRegisterModel.setSuccess("0");
                loginRegisterModel.setMessage("Network Issue");
                updateProfile.setValue(loginRegisterModel);
            }
        });

        return updateProfile;
    }
}
