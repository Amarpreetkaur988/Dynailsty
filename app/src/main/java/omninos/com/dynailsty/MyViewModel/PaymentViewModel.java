package omninos.com.dynailsty.MyViewModel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import omninos.com.dynailsty.Retrofit.Api;
import omninos.com.dynailsty.Retrofit.ApiClient;
import omninos.com.dynailsty.model.PaypalTokenModel;
import omninos.com.dynailsty.util.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewModel extends ViewModel {

    private MutableLiveData<PaypalTokenModel> tokenModelMutableLiveData;

    public LiveData<PaypalTokenModel> paypalTokenModelLiveData(Activity activity) {
        tokenModelMutableLiveData = new MutableLiveData<>();

        CommonUtils.showProgress(activity);
        Api api = ApiClient.getApiClient().create(Api.class);
        api.getToken().enqueue(new Callback<PaypalTokenModel>() {
            @Override
            public void onResponse(Call<PaypalTokenModel> call, Response<PaypalTokenModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    tokenModelMutableLiveData.setValue(response.body());
                } else {
                    PaypalTokenModel paypalTokenModel = new PaypalTokenModel();
                    paypalTokenModel.setSuccess("0");
                    paypalTokenModel.setMessage("Server Error");
                    tokenModelMutableLiveData.setValue(paypalTokenModel);
                }
            }

            @Override
            public void onFailure(Call<PaypalTokenModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                PaypalTokenModel paypalTokenModel = new PaypalTokenModel();
                paypalTokenModel.setSuccess("0");
                paypalTokenModel.setMessage("Network Issue");
                tokenModelMutableLiveData.setValue(paypalTokenModel);

            }
        });

        return tokenModelMutableLiveData;
    }
}
