package omninos.com.dynailsty.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StaffDetailModel {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("details")
    @Expose
    private Details details;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }


    public class TimeSlotDetails {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("end_time")
        @Expose
        private String endTime;
        @SerializedName("working_days")
        @Expose
        private String workingDays;
        @SerializedName("non_working_days")
        @Expose
        private String nonWorkingDays;
        @SerializedName("regularFirstShiftStartTime")
        @Expose
        private String regularFirstShiftStartTime;
        @SerializedName("regularFirstShiftEndTime")
        @Expose
        private String regularFirstShiftEndTime;
        @SerializedName("regularSecondShiftStartTime")
        @Expose
        private String regularSecondShiftStartTime;
        @SerializedName("regularSecondShiftEndTime")
        @Expose
        private String regularSecondShiftEndTime;
        @SerializedName("weekendFirstShiftStartTime")
        @Expose
        private String weekendFirstShiftStartTime;
        @SerializedName("weekendFirstShiftEndTime")
        @Expose
        private String weekendFirstShiftEndTime;
        @SerializedName("weekendSecondShiftStartTime")
        @Expose
        private String weekendSecondShiftStartTime;
        @SerializedName("weekendSecondShiftEndTime")
        @Expose
        private String weekendSecondShiftEndTime;
        @SerializedName("holiday_startDate")
        @Expose
        private String holidayStartDate;
        @SerializedName("holiday_endDate")
        @Expose
        private String holidayEndDate;
        @SerializedName("regular_times")
        @Expose
        private String regularTimes;
        @SerializedName("weekend_times")
        @Expose
        private String weekendTimes;
        @SerializedName("firstShiftStartTime")
        @Expose
        private String firstShiftStartTime;
        @SerializedName("firstShiftEndTime")
        @Expose
        private String firstShiftEndTime;
        @SerializedName("secondShifStartTime")
        @Expose
        private String secondShifStartTime;
        @SerializedName("secondShifEndTime")
        @Expose
        private String secondShifEndTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getWorkingDays() {
            return workingDays;
        }

        public void setWorkingDays(String workingDays) {
            this.workingDays = workingDays;
        }

        public String getNonWorkingDays() {
            return nonWorkingDays;
        }

        public void setNonWorkingDays(String nonWorkingDays) {
            this.nonWorkingDays = nonWorkingDays;
        }

        public String getRegularFirstShiftStartTime() {
            return regularFirstShiftStartTime;
        }

        public void setRegularFirstShiftStartTime(String regularFirstShiftStartTime) {
            this.regularFirstShiftStartTime = regularFirstShiftStartTime;
        }

        public String getRegularFirstShiftEndTime() {
            return regularFirstShiftEndTime;
        }

        public void setRegularFirstShiftEndTime(String regularFirstShiftEndTime) {
            this.regularFirstShiftEndTime = regularFirstShiftEndTime;
        }

        public String getRegularSecondShiftStartTime() {
            return regularSecondShiftStartTime;
        }

        public void setRegularSecondShiftStartTime(String regularSecondShiftStartTime) {
            this.regularSecondShiftStartTime = regularSecondShiftStartTime;
        }

        public String getRegularSecondShiftEndTime() {
            return regularSecondShiftEndTime;
        }

        public void setRegularSecondShiftEndTime(String regularSecondShiftEndTime) {
            this.regularSecondShiftEndTime = regularSecondShiftEndTime;
        }

        public String getWeekendFirstShiftStartTime() {
            return weekendFirstShiftStartTime;
        }

        public void setWeekendFirstShiftStartTime(String weekendFirstShiftStartTime) {
            this.weekendFirstShiftStartTime = weekendFirstShiftStartTime;
        }

        public String getWeekendFirstShiftEndTime() {
            return weekendFirstShiftEndTime;
        }

        public void setWeekendFirstShiftEndTime(String weekendFirstShiftEndTime) {
            this.weekendFirstShiftEndTime = weekendFirstShiftEndTime;
        }

        public String getWeekendSecondShiftStartTime() {
            return weekendSecondShiftStartTime;
        }

        public void setWeekendSecondShiftStartTime(String weekendSecondShiftStartTime) {
            this.weekendSecondShiftStartTime = weekendSecondShiftStartTime;
        }

        public String getWeekendSecondShiftEndTime() {
            return weekendSecondShiftEndTime;
        }

        public void setWeekendSecondShiftEndTime(String weekendSecondShiftEndTime) {
            this.weekendSecondShiftEndTime = weekendSecondShiftEndTime;
        }

        public String getHolidayStartDate() {
            return holidayStartDate;
        }

        public void setHolidayStartDate(String holidayStartDate) {
            this.holidayStartDate = holidayStartDate;
        }

        public String getHolidayEndDate() {
            return holidayEndDate;
        }

        public void setHolidayEndDate(String holidayEndDate) {
            this.holidayEndDate = holidayEndDate;
        }

        public String getRegularTimes() {
            return regularTimes;
        }

        public void setRegularTimes(String regularTimes) {
            this.regularTimes = regularTimes;
        }

        public String getWeekendTimes() {
            return weekendTimes;
        }

        public void setWeekendTimes(String weekendTimes) {
            this.weekendTimes = weekendTimes;
        }

        public String getFirstShiftStartTime() {
            return firstShiftStartTime;
        }

        public void setFirstShiftStartTime(String firstShiftStartTime) {
            this.firstShiftStartTime = firstShiftStartTime;
        }

        public String getFirstShiftEndTime() {
            return firstShiftEndTime;
        }

        public void setFirstShiftEndTime(String firstShiftEndTime) {
            this.firstShiftEndTime = firstShiftEndTime;
        }

        public String getSecondShifStartTime() {
            return secondShifStartTime;
        }

        public void setSecondShifStartTime(String secondShifStartTime) {
            this.secondShifStartTime = secondShifStartTime;
        }

        public String getSecondShifEndTime() {
            return secondShifEndTime;
        }

        public void setSecondShifEndTime(String secondShifEndTime) {
            this.secondShifEndTime = secondShifEndTime;
        }

    }

    public class Details {

        @SerializedName("barberDeatils")
        @Expose
        private List<BarberDeatil> barberDeatils = null;
        @SerializedName("timeSlotDetails")
        @Expose
        private TimeSlotDetails timeSlotDetails;

        public List<BarberDeatil> getBarberDeatils() {
            return barberDeatils;
        }

        public void setBarberDeatils(List<BarberDeatil> barberDeatils) {
            this.barberDeatils = barberDeatils;
        }

        public TimeSlotDetails getTimeSlotDetails() {
            return timeSlotDetails;
        }

        public void setTimeSlotDetails(TimeSlotDetails timeSlotDetails) {
            this.timeSlotDetails = timeSlotDetails;
        }

    }

    public class BookingTime {

        @SerializedName("bookingTime")
        @Expose
        private String bookingTime;
        @SerializedName("bookingDate")
        @Expose
        private String bookingDate;

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

    }

    public class BarberDeatil {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("crated")
        @Expose
        private String crated;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("bookingTime")
        @Expose
        private List<BookingTime> bookingTime = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCrated() {
            return crated;
        }

        public void setCrated(String crated) {
            this.crated = crated;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public List<BookingTime> getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(List<BookingTime> bookingTime) {
            this.bookingTime = bookingTime;
        }

    }
}
