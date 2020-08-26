package in.kdcash.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateList {

    @SerializedName("countries")
    @Expose
    private List<AllStateList> allStateLists = null;


    public List<AllStateList> getAllStateLists() {
        return allStateLists;
    }

    public void setAllStateLists(List<AllStateList> allStateLists) {
        this.allStateLists = allStateLists;
    }
}
