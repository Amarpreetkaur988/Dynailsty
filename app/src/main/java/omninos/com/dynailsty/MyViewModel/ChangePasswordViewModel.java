package omninos.com.dynailsty.MyViewModel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Map;

import omninos.com.dynailsty.Retrofit.Api;
import omninos.com.dynailsty.Retrofit.ApiClient;
import omninos.com.dynailsty.util.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends ViewModel {
    private MutableLiveData<Map> changePass;

    public LiveData<Map> changePassword(Activity activity, String userId, String old_password, String new_password) {
        changePass = new MutableLiveData<>();
        CommonUtils.showProgress(activity);
        Api api = ApiClient.getApiClient().create(Api.class);
        api.UpdatePass(userId, old_password, new_password).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    changePass.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                CommonUtils.dismissProgress();
            }
        });

        return changePass;
    }
}
