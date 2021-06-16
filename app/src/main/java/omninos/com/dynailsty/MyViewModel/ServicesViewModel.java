package omninos.com.dynailsty.MyViewModel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Map;

import omninos.com.dynailsty.Retrofit.Api;
import omninos.com.dynailsty.Retrofit.ApiClient;
import omninos.com.dynailsty.model.GetServiceListModel;
import omninos.com.dynailsty.util.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesViewModel extends ViewModel {

    private MutableLiveData<GetServiceListModel> getServiceListModelMutableLiveData;

    private MutableLiveData<Map> booking;

    private MutableLiveData<Map> newboking;

    public LiveData<GetServiceListModel> getServiceListModelLiveData(Activity activity) {
        getServiceListModelMutableLiveData = new MutableLiveData<>();

        Api api = ApiClient.getApiClient().create(Api.class);

        CommonUtils.showProgress(activity);

        api.getServices().enqueue(new Callback<GetServiceListModel>() {
            @Override
            public void onResponse(Call<GetServiceListModel> call, Response<GetServiceListModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    getServiceListModelMutableLiveData.setValue(response.body());
                } else {
                    GetServiceListModel getServiceListModel = new GetServiceListModel();
                    getServiceListModel.setSuccess("0");
                    getServiceListModel.setMessage("Server Error");
                    getServiceListModelMutableLiveData.setValue(getServiceListModel);
                }
            }

            @Override
            public void onFailure(Call<GetServiceListModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                GetServiceListModel getServiceListModel = new GetServiceListModel();
                getServiceListModel.setSuccess("0");
                getServiceListModel.setMessage("Server Error");
                getServiceListModelMutableLiveData.setValue(getServiceListModel);
            }
        });

        return getServiceListModelMutableLiveData;
    }


    public LiveData<Map> bookingServices(Activity activity, String userId, String serviceId, String bookingDate, String bookingTime, String paymentStatus, String amount, String barber_id) {
        booking = new MutableLiveData<>();

        Api api = ApiClient.getApiClient().create(Api.class);
        CommonUtils.showProgress(activity);

        api.userbooking(userId, serviceId, bookingDate, bookingTime, paymentStatus, amount, barber_id).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    booking.setValue(response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                CommonUtils.dismissProgress();

            }
        });

        return booking;

    }


    public LiveData<Map> bookNewbooking(Activity activity, String paymentMethod, String userId, String barber_id, String serviceId, String bookingDate, String bookingTime, String amount, String token) {

        CommonUtils.showProgress(activity);
        newboking = new MutableLiveData<>();
        Api api = ApiClient.getApiClient().create(Api.class);

        api.userPaymentAcceptance(paymentMethod, userId, barber_id, serviceId, bookingDate, bookingTime, amount, token).enqueue(new Callback<Map>() {
            @Override
            public void onResponse(Call<Map> call, Response<Map> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    newboking.setValue(response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<Map> call, Throwable t) {
                CommonUtils.dismissProgress();
            }
        });

        return newboking;
    }
}
