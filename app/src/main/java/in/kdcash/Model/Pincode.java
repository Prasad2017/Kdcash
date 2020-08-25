package in.kdcash.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pincode {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private List<PincodeResponse> data = null;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public List<PincodeResponse> getData() {
        return data;
    }

    public void setData(List<PincodeResponse> data) {
        this.data = data;
    }
}
