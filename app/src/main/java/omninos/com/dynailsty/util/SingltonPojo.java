package omninos.com.dynailsty.util;

import java.util.List;

import omninos.com.dynailsty.model.GetServiceListModel;

public class SingltonPojo {


    private String CategoryId,bookingDate,bookingTime,bookingAmt,ServiceTime,ServicePrice,ServiceTitle,MainServiceName,SelectedDate,BarbarId;

    public String getBarbarId() {
        return BarbarId;
    }

    public void setBarbarId(String barbarId) {
        BarbarId = barbarId;
    }

    public String getSelectedDate() {
        return SelectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        SelectedDate = selectedDate;
    }

    public String getMainServiceName() {
        return MainServiceName;
    }

    public void setMainServiceName(String mainServiceName) {
        MainServiceName = mainServiceName;
    }

    public String getServiceTitle() {
        return ServiceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        ServiceTitle = serviceTitle;
    }

    public String getServiceTime() {
        return ServiceTime;
    }

    public void setServiceTime(String serviceTime) {
        ServiceTime = serviceTime;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBookingAmt() {
        return bookingAmt;
    }

    public void setBookingAmt(String bookingAmt) {
        this.bookingAmt = bookingAmt;
    }

    List<GetServiceListModel.Category> categories;

    public List<GetServiceListModel.Category> getCategories() {
        return categories;
    }

    public void setCategories(List<GetServiceListModel.Category> categories) {
        this.categories = categories;
    }
}
