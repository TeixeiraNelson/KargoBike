package ch.ribeironelson.kargobike.adapter;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.entity.TripEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.repository.CheckpointRepository;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.database.repository.UserRepository;
import ch.ribeironelson.kargobike.ui.Delivery.AddDeliveryActivity;
import ch.ribeironelson.kargobike.ui.Delivery.DeliveryActivity;
import ch.ribeironelson.kargobike.ui.Delivery.DetailsActivity;
import ch.ribeironelson.kargobike.ui.DeliveryCompleteActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.util.TimeStamp;
import ch.ribeironelson.kargobike.viewmodel.CheckpointListViewModel;
import ch.ribeironelson.kargobike.viewmodel.DeliveriesListViewModel;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;


public class DeliveriesRecyclerViewAdapter extends RecyclerView.Adapter<DeliveriesRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<DeliveryEntity> mDeliveries;
    private Application app;
    private SchedulesEntity riderSchedule;

    private EditText commentData;

    public DeliveriesRecyclerViewAdapter(List<DeliveryEntity> mDeliveries, Context context, Application app) {
        mContext = context;
        this.mDeliveries = mDeliveries;
        this.app = app;
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

        holder.bindItem(delivery, mContext, position, DeliveriesRecyclerViewAdapter.this, mDeliveries, app);
        holder.productName.setText(delivery.getIdProduct());
        holder.clientName.setText(delivery.getNextPlaceToGo().getName());
        holder.deliveryDate.setText(delivery.getDeliveryTime());

        if(delivery.getNextPlaceToGo().getName().equals("Final Destination"))
            holder.clientName.setText(delivery.getFinalDestination());

        if(delivery.isLoaded()){
            holder.packageImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unloadpackage));
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.packageLoaded));
        }
        else {
            holder.packageImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.loadpackage));
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.packageUnloaded));
        }

    }

    @Override
    public int getItemCount() {
        if(mDeliveries==null)
            return 0;
        return mDeliveries.size();
    }

    public void updateData(final List<DeliveryEntity> data) {
        Log.d("RV ADAPTER", "updating data");
        if (this.mDeliveries == null) {
            this.mDeliveries = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return DeliveriesRecyclerViewAdapter.this.mDeliveries.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

                    if (DeliveriesRecyclerViewAdapter.this.mDeliveries instanceof DeliveryEntity) {
                        return (DeliveriesRecyclerViewAdapter.this.mDeliveries.get(oldItemPosition)).getIdDelivery().equals(
                                (data.get(newItemPosition)).getIdDelivery());
                    }
                    return false;
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (DeliveriesRecyclerViewAdapter.this.mDeliveries instanceof DeliveryEntity) {
                        DeliveryEntity newProduct = data.get(newItemPosition);
                        DeliveryEntity oldProduct = DeliveriesRecyclerViewAdapter.this.mDeliveries.get(newItemPosition);
                        return Objects.equals(newProduct.getIdDelivery(), oldProduct.getIdDelivery())
                                && Objects.equals(newProduct.getDescription(), oldProduct.getDescription());
                    }
                    return false;
                }
            });
            this.mDeliveries = data;
            result.dispatchUpdatesTo(this);
        }
    }

    public void bindSchedule(SchedulesEntity sch) {
        riderSchedule = sch;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView clientName;
        private TextView deliveryDate;
        private TextView productName;
        private CheckpointEntity selectedCheckpoint;
        private String selectedNextRider;

        private ImageView packageImg;
        private DeliveryEntity deliveryEntity;
        private Context mContext;
        private int position;
        private Application app;
        private List<DeliveryEntity> mDeliveries;
        private DeliveriesRecyclerViewAdapter deliveriesRecyclerViewAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            clientName = itemView.findViewById(R.id.clientname_txtview);
            deliveryDate = itemView.findViewById(R.id.deliveryDate_txtview);
            productName = itemView.findViewById(R.id.productType_txtview);
            packageImg = itemView.findViewById(R.id.packageImage);

        }

        public void bindItem(DeliveryEntity delivery, Context mContext, int position, DeliveriesRecyclerViewAdapter deliveriesRecyclerViewAdapter, List<DeliveryEntity> mDeliveries, Application app){
            deliveryEntity = delivery;
            this.mContext = mContext;
            this.mDeliveries = mDeliveries;
            this.position = position;
            this.app = app;
            this.deliveriesRecyclerViewAdapter = deliveriesRecyclerViewAdapter;

            if(deliveryEntity.isLoaded()){
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.packageLoaded));
            }
            else {
                itemView.setBackgroundColor(mContext.getResources().getColor(R.color.packageUnloaded));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            // custom dialog
            final Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.popup_window);
            dialog.setTitle("Next step");

            // set the custom dialog components - text, image and button
            Button dialogButton = (Button) dialog.findViewById(R.id.button_popup);
            CheckBox checkBox = dialog.findViewById(R.id.checkBox);
            Spinner nextRider = dialog.findViewById(R.id.spinner_next_rider);
            Spinner nextCheckpoint = dialog.findViewById(R.id.spinner_checkpoint);
            commentData = dialog.findViewById(R.id.comment_content);

            if(!deliveryEntity.isLoaded()){
                checkBox.setVisibility(View.INVISIBLE);
            }

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if(checked){
                        nextRider.setVisibility(View.VISIBLE);
                    }
                }
            });



            // Adding values to spinners
            loadDataCheckpoints(nextCheckpoint);
            loadDataNextRider(nextRider);


            // if button is clicked, update delivery
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateNextStep(v);
                    dialog.dismiss();
                }
            });

            if(deliveryEntity.getNextPlaceToGo().getName().equals("Final Destination"))
                finishDelivery(v);
            else
                dialog.show();


            return true;
        }

        private void finishDelivery(View deliveryEntity) {
            updateNextStep(deliveryEntity);
        }

        private void loadDataNextRider(Spinner nextRider) {
            UsersListViewModel.Factory factory = new UsersListViewModel.Factory(
                    app);
            UsersListViewModel viewModel = ViewModelProviders.of((DeliveryActivity) mContext, factory).get(UsersListViewModel.class);
            viewModel.getAllUsers().observe((LifecycleOwner) mContext, deliveryEntities -> {
                if (deliveryEntities != null) {
                    addToSpinnerRider(deliveryEntities, nextRider);
                }
            });
        }

        private void addToSpinnerRider(List<UserEntity> deliveryEntities, Spinner nextRider) {
            List<String> list = new ArrayList<>();
            for(UserEntity c : deliveryEntities){
                list.add(c.getFirstname()+ " " + c.getLastname());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            nextRider.setAdapter(dataAdapter);
            nextRider.setSelection(0);

            nextRider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(nextRider.getVisibility() == View.VISIBLE)
                        selectedNextRider = deliveryEntities.get(position).getIdUser();
                    else
                        selectedNextRider = null;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        private void loadDataCheckpoints(Spinner nextCheckpoint) {
            CheckpointListViewModel.Factory factory = new CheckpointListViewModel.Factory(
                    app);
            CheckpointListViewModel viewModel = ViewModelProviders.of((DeliveryActivity) mContext, factory).get(CheckpointListViewModel.class);
            viewModel.getCheckpoints().observe((LifecycleOwner) mContext, deliveryEntities -> {
                if (deliveryEntities != null) {
                    addToSpinner(deliveryEntities, nextCheckpoint);
                }
            });
        }

        private void addToSpinner(List<CheckpointEntity> deliveryEntities, Spinner nextCheckpoint) {
            List<String> list = new ArrayList<>();
            for(CheckpointEntity c : deliveryEntities){
                list.add(c.getName());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            nextCheckpoint.setAdapter(dataAdapter);
            nextCheckpoint.setSelection(0);

            nextCheckpoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCheckpoint = deliveryEntities.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        private void updateNextStep(View v) {
            if(deliveryEntity!=null){
                String tripType = "";
                String gpsCoordinates ="";

                String timestamp = TimeStamp.getTimeStamp();

                if(deliveryEntity.isLoaded())
                    tripType = "Unload";
                else
                    tripType = "Load";

                deliveryEntity.setLoaded(!deliveryEntity.isLoaded());
                TripEntity checkpoint = new TripEntity(deliveryEntity.getNextPlaceToGo(), tripType, deliveryEntity.getActuallyAssignedUser(), gpsCoordinates, timestamp, commentData.getText().toString());
                deliveryEntity.addCheckpoint(checkpoint);

                if(selectedCheckpoint!=null){
                    deliveryEntity.setNextPlaceToGo(selectedCheckpoint);
                }
                if(selectedNextRider!=null && selectedNextRider.length()>1)
                    deliveryEntity.setActuallyAssignedUser(selectedNextRider);

                if(deliveryEntity.getNextPlaceToGo().getName().equals("Final Destination") && !deliveryEntity.isLoaded()){
                    deliveryEntity.setActuallyAssignedUser("Delivery Finished");
                } else {
                    if(deliveryEntity.isLoaded()){
                        Toast.makeText(mContext,"Package loaded !", Toast.LENGTH_SHORT).show();
                        packageImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unloadpackage));
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.packageLoaded));
                    }
                    else {
                        Toast.makeText(mContext,"Package unloaded !", Toast.LENGTH_SHORT).show();
                        packageImg.setImageDrawable(mContext.getResources().getDrawable(R.drawable.loadpackage));
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.packageUnloaded));
                    }
                }


                DeliveryRepository.getInstance().update(deliveryEntity, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("Delivery status", "Delivery checkpoint added !");
                        if(deliveryEntity.getActuallyAssignedUser().equals("Delivery Finished")){
                            riderSchedule.addDelivery();
                            SchedulesRepository.getInstance().updateSchedules(riderSchedule, new OnAsyncEventListener() {
                                @Override
                                public void onSuccess() {
                                    Intent intent = new Intent(mContext, DeliveryCompleteActivity.class);
                                    intent.putExtra("DeliveryEntity",deliveryEntity);
                                    mContext.startActivity(intent);
                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("Delivery status", "Delivery checkpoint add attempt fail !");
                    }
                });

            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("deliveryId",deliveryEntity.getIdDelivery());
            mContext.startActivity(intent);
        }
    }
}
