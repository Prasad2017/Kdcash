package in.kdcash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kdcash.Activity.Registration;
import in.kdcash.Model.AreaResponse;
import in.kdcash.Model.CityResponse;
import in.kdcash.R;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.MyViewHolder> {

    Context context;
    List<AreaResponse> countryResponseList;

    public AreaAdapter(Context context, List<AreaResponse> countryResponseList) {

        this.context = context;
        this.countryResponseList = countryResponseList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.country_list_item, null);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AreaResponse countryResponse = countryResponseList.get(position);

        holder.countryName.setText(countryResponseList.get(position).getName());

        holder.countryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Registration.areaTxt.setText(countryResponseList.get(position).getName());
                Registration.pincodeTxt.setText(countryResponseList.get(position).getCode());
                Registration.dialog.cancel();

            }
        });

    }

    @Override
    public int getItemCount() {
        return countryResponseList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.countryName)
        TextView countryName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

