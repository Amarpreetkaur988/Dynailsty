package omninos.com.dynailsty.MyViewModel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import omninos.com.dynailsty.Retrofit.Api;
import omninos.com.dynailsty.Retrofit.ApiClient;
import omninos.com.dynailsty.model.MyAppointmentModel;
import omninos.com.dynailsty.model.StaffDetailModel;
import omninos.com.dynailsty.util.CommonUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentViewModel extends ViewModel {

    private MutableLiveData<MyAppointmentModel> myAppointmentModelMutableLiveData;

    private MutableLiveData<StaffDetailModel> staffDetailModelMutableLiveData;

    public LiveData<MyAppointmentModel> getAppointment(Activity activity, String userId) {
        myAppointmentModelMutableLiveData = new MutableLiveData<>();
        Api api = ApiClient.getApiClient().create(Api.class);
        CommonUtils.showProgress(activity);

        api.getAppointmentList(userId).enqueue(new Callback<MyAppointmentModel>() {
            @Override
            public void onResponse(Call<MyAppointmentModel> call, Response<MyAppointmentModel> response) {
                CommonUtils.dismissProgress();
                if (response.body() != null) {
                    myAppointmentModelMutableLiveData.setValue(response.body());
                } else {
                    MyAppointmentModel myAppointmentModel = new MyAppointmentModel();
                    myAppointmentModel.setSuccess("0");
                    myAppointmentModel.setMessage("Server Error");
                    myAppointmentModelMutableLiveData.setValue(myAppointmentModel);
                }

            }

            @Override
            public void onFailure(Call<MyAppointmentModel> call, Throwable t) {
                CommonUtils.dismissProgress();
                MyAppointmentModel myAppointmentModel = new MyAppointmentModel();
                myAppointmentModel.setSuccess("0");
                myAppointmentModel.setMessage("Server Error");
                myAppointmentModelMutableLiveData.setValue(myAppointmentModel);
            }
        });

        return myAppointmentModelMutableLiveData;
    }

    public LiveData<StaffDetailModel> staffDetailModelLiveData(Activity activity, String apointmentDate) {
        staffDetailModelMutableLiveData = new MutableLiveData<>();

        Api api = ApiClient.getApiClient().create(Api.class);

        api.getStaffDetail(apointmentDate).enqueue(new Callback<StaffDetailModel>() {
            @Override
            public void onResponse(Call<StaffDetailModel> call, Response<StaffDetailModel> response) {
                if (response.body() != null) {
                    staffDetailModelMutableLiveData.setValue(response.body());
                } else {
                    StaffDetailModel detailModel = new StaffDetailModel();
                    detailModel.setSuccess("0");
                    detailModel.setMessage("Server Error");
                    staffDetailModelMutableLiveData.setValue(detailModel);
                }
            }

            @Override
            public void onFailure(Call<StaffDetailModel> call, Throwable t) {

                StaffDetailModel detailModel = new StaffDetailModel();
                detailModel.setSuccess("0");
                detailModel.setMessage("Server Error");
                staffDetailModelMutableLiveData.setValue(detailModel);
            }
        });

        return staffDetailModelMutableLiveData;
    }
}
