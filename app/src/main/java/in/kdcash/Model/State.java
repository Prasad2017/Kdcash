package in.kdcash.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class State {

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private StateList data = null;
    @SerializedName("AgentRandomId")
    @Expose
    private String AgentRandomId;



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

    public StateList getData() {
        return data;
    }

    public void setData(StateList data) {
        this.data = data;
    }

    public String getAgentRandomId() {
        return AgentRandomId;
    }

    public void setAgentRandomId(String agentRandomId) {
        AgentRandomId = agentRandomId;
    }
}
