package ch.ribeironelson.kargobike.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.RoleEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.ui.Riders.RidersList;
import ch.ribeironelson.kargobike.util.RecyclerViewItemClickListener;
import ch.ribeironelson.kargobike.viewmodel.RoleListViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;

public class RidersRecyclerViewAdapter<T> extends RecyclerView.Adapter<RidersRecyclerViewAdapter.ViewHolder> {

    private RecyclerViewItemClickListener mListener ;
    private List<T> data ;
    private Context mContext ;
    private Application app ;

    private List<WorkingZoneEntity> workingZones ;
    private List<RoleEntity> roles ;


    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView riderName ;
        private final TextView workingZone ;
        private final TextView email ;
        private final TextView role ;


        private ViewHolder(View v, TextView riderName, TextView workingZone, TextView email, TextView role){
            super(v);
            this.riderName = riderName ;
            this.workingZone = workingZone ;
            this.email = email ;
            this.role = role ;
        }

    }

    public RidersRecyclerViewAdapter(RecyclerViewItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public RidersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rider_item, parent, false);

        final TextView riderName = v.findViewById(R.id.txt_riderName);
        final TextView workingZone = v.findViewById(R.id.txt_workingZone);
        final TextView email = v.findViewById(R.id.txt_email);
        final TextView role = v.findViewById(R.id.txt_role);

        final ViewHolder riderViewHolder = new ViewHolder(v, riderName, workingZone, email, role);
        v.setOnClickListener(view -> mListener.onItemClick(view, riderViewHolder.getAdapterPosition()));
        v.setOnClickListener(view -> {
            mListener.onItemLongClick(view, riderViewHolder.getAdapterPosition());
        });
        return riderViewHolder ;
    }

    @Override
    public void onBindViewHolder(RidersRecyclerViewAdapter.ViewHolder holder, int position){
        T item = data.get(position);
       /* String roleString = getRoleString((UserEntity)item);
        String workingZoneString = getWorkingZoneString((UserEntity)item);*/

        holder.riderName.setText(((UserEntity)item).getFirstname() +" "+ ((UserEntity)item).getLastname());
        if(((UserEntity)item).getIdZone().equals("0")){
            holder.workingZone.setText("No zone");
        }else{
            holder.workingZone.setText(((UserEntity)item).getIdZone());
        }
        holder.email.setText(((UserEntity)item).getEmail());
        holder.role.setText(((UserEntity)item).getIdRole());
    }

    @Override
    public int getItemCount(){
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }
    }

    private String getWorkingZoneString(UserEntity rider){

        String result = "No working zone" ;

        for(int i = 0 ; i != workingZones.size() ; i++){
            if(workingZones.get(i).getWorkingZoneId().equals(rider.getIdZone()))
                result = workingZones.get(i).getLocation();
        }

        return result ;
    }

    private String getRoleString(UserEntity rider){
        String result = "No role";

        for (int i = 0 ; i != roles.size() ; i++){
            if(roles.get(i).getRoleId().equals(rider.getIdRole()))
                result = roles.get(i).getRole();
        }
        return result ;
    }

    public void setData(final List<T> riders){
        if(this.data == null){
            this.data = riders ;
            notifyItemRangeInserted(0, riders.size());
        }else{
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return data.size();
                }

                @Override
                public int getNewListSize() {
                    return riders.size();
                }

                @Override
                public boolean areItemsTheSame(int i, int i1) {
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int i, int i1) {
                    return false;
                }
            });
            data = riders ;
            result.dispatchUpdatesTo(this);
        }
    }

    }


