package ch.ribeironelson.kargobike.util;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;


public class DeliveriesRecyclerViewAdapter extends RecyclerView.Adapter<DeliveriesRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<DeliveryEntity> mDeliveries;

    public DeliveriesRecyclerViewAdapter(List<DeliveryEntity> mDeliveries, Context context) {
        mContext = context;
        mDeliveries = mDeliveries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeliveryEntity delivery = mDeliveries.get(position);

        Log.d("RV ADAPTER","SETTING DATA FOR A DELIVERY");
        holder.productName.setText(delivery.getIdProduct());
        holder.clientName.setText(delivery.getFinalDestination());
        holder.deliveryDate.setText(delivery.getDeliveryDate() + " " +delivery.getDeliveryTime());

    }

    @Override
    public int getItemCount() {
        if(mDeliveries==null)
            return 0;
        return mDeliveries.size();
    }

    public void updateData(List<DeliveryEntity> deliveries) {
        Log.d("RV ADAPTER", "updating data");
        for(DeliveryEntity dv : deliveries){
            Log.d("RV ADAPTER UPDATING : ", dv.getActuallyAssignedUser());
        }
        if(mDeliveries!= null){
            mDeliveries.clear();
        } else {
            mDeliveries = new ArrayList<>();
            mDeliveries.addAll(deliveries);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView clientName;
        private TextView deliveryDate;
        private TextView productName;

        private Button deliverBtn;
        private Button formBtn;
        private Button mapBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            clientName = itemView.findViewById(R.id.clientname_txtview);
            deliveryDate = itemView.findViewById(R.id.deliveryDate_txtview);
            productName = itemView.findViewById(R.id.productType_txtview);

            /*deliverBtn = itemView.findViewById(R.id.deliverButton);
            formBtn = itemView.findViewById(R.id.formBtn);
            mapBtn = itemView.findViewById(R.id.mapBtn);*/
        }
    }
}
