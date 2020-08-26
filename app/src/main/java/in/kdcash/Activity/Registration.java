package in.kdcash.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import in.kdcash.Adapter.AreaAdapter;
import in.kdcash.Adapter.CityAdapter;
import in.kdcash.Adapter.CountryAdapter;
import in.kdcash.Adapter.PincodeAdapter;
import in.kdcash.Adapter.StateAdapter;
import in.kdcash.Extra.DetectConnection;
import in.kdcash.Model.AllStateList;
import in.kdcash.Model.Area;
import in.kdcash.Model.AreaResponse;
import in.kdcash.Model.City;
import in.kdcash.Model.CityResponse;
import in.kdcash.Model.Country;
import in.kdcash.Model.CountryResponse;
import in.kdcash.Model.PincodeResponse;
import in.kdcash.Model.State;
import in.kdcash.Model.StateList;
import in.kdcash.Model.StateResponse;
import in.kdcash.R;
import in.kdcash.Retrofit.Api;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {

    @BindViews({R.id.firstName, R.id.lastName, R.id.mobileNumber, R.id.emailId, R.id.address})
    List<FormEditText> formEditTexts;
    public static TextView countryTxt, stateTxt, cityTxt, areaTxt, pincodeTxt;
    public static List<CountryResponse> countryResponseList = new ArrayList<>();
    public static List<CountryResponse> searchCountryResponseList = new ArrayList<>();
    public static List<StateResponse> stateResponseList = new ArrayList<>();
    public static List<StateResponse> searchStateResponseList = new ArrayList<>();
    public static List<AllStateList> allStateLists = new ArrayList<>();
    public static List<CityResponse> cityResponseList = new ArrayList<>();
    public static List<CityResponse> searchCityResponseList = new ArrayList<>();
    public static List<AreaResponse> areaResponseList = new ArrayList<>();
    public static List<AreaResponse> searchAreaResponseList = new ArrayList<>();
    public static List<PincodeResponse> pincodeResponseList = new ArrayList<>();
    public static List<PincodeResponse> searchPincodeResponseList = new ArrayList<>();
    public static Dialog dialog;
    RecyclerView recyclerView;
    TextView close, searchTxt;
    public static String countryId , countryName, stateId, stateName, cityId, cityName, areaId, areaName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        initViews();

    }

    private void initViews() {

        countryTxt = findViewById(R.id.countryTxt);
        stateTxt = findViewById(R.id.stateTxt);
        cityTxt = findViewById(R.id.cityTxt);
        areaTxt = findViewById(R.id.areaTxt);
        pincodeTxt = findViewById(R.id.pincodeTxt);

    }

    @OnClick({R.id.countryTxt, R.id.stateTxt, R.id.signUp})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.countryTxt:

                if (countryResponseList!=null){

                    dialog = new Dialog(Registration.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.country_list);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    recyclerView = dialog.findViewById(R.id.recyclerView);
                    close = dialog.findViewById(R.id.close);
                    searchTxt = dialog.findViewById(R.id.search);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    searchTxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            try {

                                String country = editable.toString();

                                if (country.length()>0){
                                    searchCountryResponseList = new ArrayList<>();
                                    for (int i=0;i<countryResponseList.size();i++)
                                        if (countryResponseList.get(i).getName().toLowerCase().contains(country.toLowerCase().trim())) {
                                            searchCountryResponseList.add(countryResponseList.get(i));
                                        }
                                    if (searchCountryResponseList.size() < 1) {
                                        Toast.makeText(Registration.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }

                                } else {
                                    searchCountryResponseList = new ArrayList<>();
                                    for (int i=0;i<countryResponseList.size();i++){
                                        searchCountryResponseList.add(countryResponseList.get(i));
                                    }
                                }

                                CountryAdapter countryAdapter = new CountryAdapter(Registration.this, searchCountryResponseList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                                recyclerView.setAdapter(countryAdapter);
                                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                countryAdapter.notifyDataSetChanged();
                                countryAdapter.notifyItemInserted(searchCountryResponseList.size() - 1);
                                recyclerView.setHasFixedSize(true);

                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });


                    try {

                        CountryAdapter countryAdapter = new CountryAdapter(Registration.this, countryResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                        recyclerView.setAdapter(countryAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        countryAdapter.notifyDataSetChanged();
                        countryAdapter.notifyItemInserted(countryResponseList.size() - 1);
                        recyclerView.setHasFixedSize(true);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dialog.show();


                } else {

                    countryTxt.setText("");
                    stateTxt.setText("");
                    cityTxt.setText("");
                    areaTxt.setText("");
                    pincodeTxt.setText("");

                }

                break;

            case R.id.stateTxt:

                if (stateResponseList!=null){

                    dialog = new Dialog(Registration.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.country_list);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    recyclerView = dialog.findViewById(R.id.recyclerView);
                    close = dialog.findViewById(R.id.close);
                    searchTxt = dialog.findViewById(R.id.search);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    searchTxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            try {

                                String country = editable.toString();

                                if (country.length()>0){
                                    searchStateResponseList = new ArrayList<>();
                                    for (int i=0;i<stateResponseList.size();i++)
                                        if (stateResponseList.get(i).getName().toLowerCase().contains(country.toLowerCase().trim())) {
                                            searchStateResponseList.add(stateResponseList.get(i));
                                        }
                                    if (searchStateResponseList.size() < 1) {
                                        Toast.makeText(Registration.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }

                                } else {
                                    searchStateResponseList = new ArrayList<>();
                                    for (int i=0;i<stateResponseList.size();i++){
                                        searchStateResponseList.add(stateResponseList.get(i));
                                    }
                                }

                                StateAdapter stateAdapter = new StateAdapter(Registration.this, searchStateResponseList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                                recyclerView.setAdapter(stateAdapter);
                                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                stateAdapter.notifyDataSetChanged();
                                stateAdapter.notifyItemInserted(searchStateResponseList.size() - 1);
                                recyclerView.setHasFixedSize(true);


                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });


                    try {

                        StateAdapter stateAdapter = new StateAdapter(Registration.this, stateResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                        recyclerView.setAdapter(stateAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        stateAdapter.notifyDataSetChanged();
                        stateAdapter.notifyItemInserted(stateResponseList.size() - 1);
                        recyclerView.setHasFixedSize(true);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dialog.show();


                } else {

                    stateTxt.setText("");
                    cityTxt.setText("");
                    areaTxt.setText("");
                    pincodeTxt.setText("");

                    Toasty.normal(Registration.this, "Select Other Country", Toasty.LENGTH_SHORT).show();

                }

                break;

            case R.id.cityTxt:

                if (cityResponseList!=null){

                    dialog = new Dialog(Registration.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.country_list);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    recyclerView = dialog.findViewById(R.id.recyclerView);
                    close = dialog.findViewById(R.id.close);
                    searchTxt = dialog.findViewById(R.id.search);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    searchTxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            try {

                                String country = editable.toString();

                                if (country.length()>0){
                                    searchCityResponseList = new ArrayList<>();
                                    for (int i=0;i<cityResponseList.size();i++)
                                        if (cityResponseList.get(i).getName().toLowerCase().contains(country.toLowerCase().trim())) {
                                            searchCityResponseList.add(cityResponseList.get(i));
                                        }
                                    if (searchCityResponseList.size() < 1) {
                                        Toast.makeText(Registration.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }

                                } else {
                                    searchCityResponseList = new ArrayList<>();
                                    for (int i=0;i<cityResponseList.size();i++){
                                        searchCityResponseList.add(cityResponseList.get(i));
                                    }
                                }

                                CityAdapter cityAdapter = new CityAdapter(Registration.this, searchCityResponseList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                                recyclerView.setAdapter(cityAdapter);
                                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                cityAdapter.notifyDataSetChanged();
                                cityAdapter.notifyItemInserted(searchCityResponseList .size() - 1);
                                recyclerView.setHasFixedSize(true);

                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });


                    try {

                        CityAdapter cityAdapter = new CityAdapter(Registration.this, cityResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                        recyclerView.setAdapter(cityAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        cityAdapter.notifyDataSetChanged();
                        cityAdapter.notifyItemInserted(cityResponseList.size() - 1);
                        recyclerView.setHasFixedSize(true);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dialog.show();


                } else {

                    cityTxt.setText("");
                    areaTxt.setText("");
                    pincodeTxt.setText("");

                    Toasty.normal(Registration.this, "Select Other State", Toasty.LENGTH_SHORT).show();

                }

                break;

            case R.id.areaTxt:

                if (areaResponseList!=null){

                    dialog = new Dialog(Registration.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.country_list);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    recyclerView = dialog.findViewById(R.id.recyclerView);
                    close = dialog.findViewById(R.id.close);
                    searchTxt = dialog.findViewById(R.id.search);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    searchTxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            try {

                                String country = editable.toString();

                                if (country.length()>0){
                                    searchAreaResponseList = new ArrayList<>();
                                    for (int i=0;i<areaResponseList.size();i++)
                                        if (areaResponseList.get(i).getName().toLowerCase().contains(country.toLowerCase().trim())) {
                                            searchAreaResponseList.add(areaResponseList.get(i));
                                        }
                                    if (searchAreaResponseList.size() < 1) {
                                        Toast.makeText(Registration.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }

                                } else {
                                    searchAreaResponseList = new ArrayList<>();
                                    for (int i=0;i<cityResponseList.size();i++){
                                        searchAreaResponseList.add(areaResponseList.get(i));
                                    }
                                }

                                AreaAdapter areaAdapter = new AreaAdapter(Registration.this, searchAreaResponseList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                                recyclerView.setAdapter(areaAdapter);
                                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                areaAdapter.notifyDataSetChanged();
                                areaAdapter.notifyItemInserted(searchAreaResponseList.size() - 1);
                                recyclerView.setHasFixedSize(true);


                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });


                    try {

                        AreaAdapter areaAdapter = new AreaAdapter(Registration.this, areaResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                        recyclerView.setAdapter(areaAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        areaAdapter.notifyDataSetChanged();
                        areaAdapter.notifyItemInserted(areaResponseList.size() - 1);
                        recyclerView.setHasFixedSize(true);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dialog.show();



                    areaTxt.setText("");
                    pincodeTxt.setText("");

                    Toasty.normal(Registration.this, "Select Other City", Toasty.LENGTH_SHORT).show();

                }

                break;

            case R.id.pincodeTxt:

                if (pincodeResponseList!=null){

                    dialog = new Dialog(Registration.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
                    dialog.setContentView(R.layout.country_list);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCancelable(true);

                    recyclerView = dialog.findViewById(R.id.recyclerView);
                    close = dialog.findViewById(R.id.close);
                    searchTxt = dialog.findViewById(R.id.search);

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            dialog.dismiss();

                        }
                    });

                    searchTxt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                            try {

                                String country = editable.toString();

                                if (country.length()>0){
                                    searchPincodeResponseList = new ArrayList<>();
                                    for (int i=0;i<pincodeResponseList.size();i++)
                                        if (pincodeResponseList.get(i).getName().toLowerCase().contains(country.toLowerCase().trim())) {
                                            searchPincodeResponseList.add(pincodeResponseList.get(i));
                                        }
                                    if (searchPincodeResponseList.size() < 1) {
                                        Toast.makeText(Registration.this, "Record Not Found", Toast.LENGTH_SHORT).show();
                                    } else {

                                    }

                                } else {
                                    searchPincodeResponseList = new ArrayList<>();
                                    for (int i=0;i<pincodeResponseList.size();i++){
                                        searchPincodeResponseList.add(pincodeResponseList.get(i));
                                    }
                                }

                                PincodeAdapter pincodeAdapter = new PincodeAdapter(Registration.this, searchPincodeResponseList);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                                recyclerView.setAdapter(pincodeAdapter);
                                recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                                pincodeAdapter.notifyDataSetChanged();
                                pincodeAdapter.notifyItemInserted(searchPincodeResponseList.size() - 1);
                                recyclerView.setHasFixedSize(true);


                            } catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    });


                    try {

                        PincodeAdapter pincodeAdapter = new PincodeAdapter(Registration.this, pincodeResponseList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Registration.this));
                        recyclerView.setAdapter(pincodeAdapter);
                        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
                        pincodeAdapter.notifyDataSetChanged();
                        pincodeAdapter.notifyItemInserted(pincodeResponseList.size() - 1);
                        recyclerView.setHasFixedSize(true);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    dialog.show();


                } else {

                    pincodeTxt.setText("");

                    Toasty.normal(Registration.this, "Select Other Pincode", Toasty.LENGTH_SHORT).show();

                }

                break;

            case R.id.signUp:

                if (formEditTexts.get(0).testValidity() && formEditTexts.get(1).testValidity() && formEditTexts.get(2).testValidity()
                        && formEditTexts.get(3).testValidity() && formEditTexts.get(4).testValidity()){

                    if (countryTxt.getText().toString().equalsIgnoreCase("")){

                        if (stateTxt.getText().toString().equalsIgnoreCase("")){

                            if (cityTxt.getText().toString().equalsIgnoreCase("")){

                                if (areaTxt.getText().toString().equalsIgnoreCase("")){

                                    if (pincodeTxt.getText().toString().equalsIgnoreCase("")){

                                        registration(formEditTexts.get(0).getText().toString(), formEditTexts.get(1).getText().toString(), formEditTexts.get(2).getText().toString(),
                                                formEditTexts.get(3).getText().toString(), formEditTexts.get(4).getText().toString(), countryTxt.getText().toString(), stateTxt.getText().toString(),
                                                cityTxt.getText().toString(), areaTxt.getText().toString(), pincodeTxt.getText().toString());

                                    } else {
                                        Toasty.normal(Registration.this, "select pincode", Toasty.LENGTH_SHORT);
                                    }

                                } else {
                                    Toasty.normal(Registration.this, "select area", Toasty.LENGTH_SHORT);
                                }

                            } else {
                                Toasty.normal(Registration.this, "select city", Toasty.LENGTH_SHORT);
                            }

                        } else {
                            Toasty.normal(Registration.this, "select state", Toasty.LENGTH_SHORT);
                        }

                    } else {
                        Toasty.normal(Registration.this, "select country", Toasty.LENGTH_SHORT);
                    }

                }

                break;

        }

    }

    public void registration(String firstName, String lastName, String mobileNumber, String emailId, String address, String countryName, String stateName, String cityName, String areaName, String pincodeName){

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DetectConnection.checkInternetConnection(Registration.this)){
            getCountryList();
        } else {
            Toasty.warning(Registration.this, "NO Internet Connection", Toasty.LENGTH_SHORT, true).show();
        }
    }

    private void getCountryList() {

        countryResponseList.clear();

        Call<Country> call = Api.getClient().getCountryList("1", "500");
        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {

                if (response.isSuccessful()){

                    if (response.body().getSuccess().booleanValue()==true){

                        Log.e("responce", ""+response.body().getData());

                        countryResponseList = response.body().getData();
                        if (countryResponseList.size()>0) {

                            for (int i = 0; i < countryResponseList.size(); i++) {

                                if (countryResponseList.get(i).getName().toLowerCase().contains("India".toLowerCase().trim())) {

                                    CountryResponse countryResponse = countryResponseList.get(i);
                                    countryTxt.setText(countryResponseList.get(i).getName());
                                    countryId = countryResponse.getCode();
                                    countryName = countryResponse.getName();

                                    getStateList(countryName);

                                } else {

                                    CountryResponse countryResponse = countryResponseList.get(0);
                                    countryTxt.setText(countryResponseList.get(0).getName());
                                    countryId = countryResponse.getCode();
                                    countryName = countryResponse.getName();

                                    getStateList(countryName);

                                }
                            }
                        } else {
                            countryResponseList = null;
                            countryTxt.setText("");
                            stateTxt.setText("");
                            cityTxt.setText("");
                            areaTxt.setText("");
                            pincodeTxt.setText("");
                        }

                    } else {

                        countryResponseList = null;
                        countryTxt.setText("");
                        stateTxt.setText("");
                        cityTxt.setText("");
                        areaTxt.setText("");
                        pincodeTxt.setText("");

                    }

                } else {
                    countryResponseList = null;
                    countryTxt.setText("");
                    stateTxt.setText("");
                    cityTxt.setText("");
                    areaTxt.setText("");
                    pincodeTxt.setText("");

                }

            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Log.e("countryError", ""+t.getMessage());
            }
        });

    }

    public static void getStateList(String countryName) {

        stateResponseList.clear();

        Call<State> call = Api.getClient().getStateList();
        call.enqueue(new Callback<State>() {
            @Override
            public void onResponse(Call<State> call, Response<State> response) {

                if (response.isSuccessful()){

                    if (response.body().getSuccess().booleanValue()==true){

                        StateList stateLists = response.body().getData();
                        List<String> stringList = new ArrayList<>();

                        if (stateLists.getAllStateLists().size()>0) {

                            allStateLists = stateLists.getAllStateLists();

                            for (int j = 0; j < allStateLists.size(); j++) {

                                if (allStateLists.get(j).getCountry().equalsIgnoreCase(countryName)) {

                                    Log.e("country", "" + countryName);

                                    stringList = allStateLists.get(j).getStates();

                                    for (int k = 0; k < stringList.size(); k++) {

                                        StateResponse stateResponse = new StateResponse();
                                        stateResponse.setName(stringList.get(k));
                                        stateResponseList.add(stateResponse);
                                    }

                                    StateResponse countryResponse = stateResponseList.get(0);
                                    stateTxt.setText(stateResponseList.get(0).getName());
                                    stateId = countryResponse.getCode();
                                    stateName = countryResponse.getName();

                                    getCityList(stateName);

                                } else {

                                }
                            }
                        } else {
                            stateResponseList = null;
                            stateTxt.setText("");
                            cityTxt.setText("");
                            areaTxt.setText("");
                            pincodeTxt.setText("");
                        }

                    } else {
                        stateResponseList = null;
                        stateTxt.setText("");
                        cityTxt.setText("");
                        areaTxt.setText("");
                        pincodeTxt.setText("");

                    }

                } else {
                    stateResponseList = null;
                    stateTxt.setText("");
                    cityTxt.setText("");
                    areaTxt.setText("");
                    pincodeTxt.setText("");

                }

            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
                stateResponseList = null;
                Log.e("stateError", ""+t.getMessage());
            }
        });

    }

    public static void getCityList(String stateName) {

        cityResponseList.clear();

        Call<City> call = Api.getClient().getCityList("1", "15000", stateName);
        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {

                if (response.isSuccessful()){

                    if (response.body().getSuccess().booleanValue()==true) {

                        cityResponseList = response.body().getData();

                        if (cityResponseList.size() > 0) {

                            CityResponse cityResponse = cityResponseList.get(0);
                            cityTxt.setText(cityResponseList.get(0).getName());
                            cityId = cityResponse.getCode();
                            cityName = cityResponse.getName();

                            //getAreaList(cityId);
                        } else {
                            cityResponseList = null;
                            cityTxt.setText("");
                            areaTxt.setText("");
                            pincodeTxt.setText("");

                        }
                    }

                } else {
                    cityResponseList = null;
                    cityTxt.setText("");
                    areaTxt.setText("");
                    pincodeTxt.setText("");

                }

            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                cityResponseList = null;
                Log.e("countryError", ""+t.getMessage());
            }
        });

    }

    private void getAreaList(String cityId) {

        areaResponseList.clear();

        Call<Area> call = Api.getClient().getAreaList(cityId);
        call.enqueue(new Callback<Area>() {
            @Override
            public void onResponse(Call<Area> call, Response<Area> response) {

                if (response.isSuccessful()){

                    if (response.body().getSuccess().booleanValue()==true){

                        areaResponseList = response.body().getData();

                        AreaResponse areaResponse = areaResponseList.get(0);
                        areaTxt.setText(areaResponseList.get(0).getName());
                        areaId = areaResponse.getCode();
                        areaName  = areaResponse.getName();


                    } else {
                        areaResponseList = null;
                        areaTxt.setText("");
                        pincodeTxt.setText("");

                    }

                } else {
                    areaResponseList = null;
                    areaTxt.setText("");
                    pincodeTxt.setText("");

                }

            }

            @Override
            public void onFailure(Call<Area> call, Throwable t) {
                areaResponseList = null;
                Log.e("countryError", ""+t.getMessage());
            }
        });

    }
}