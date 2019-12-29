package ch.ribeironelson.kargobike.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.util.RecyclerViewItemClickListener;

public class RidersRecyclerViewAdapter<T> extends RecyclerView.Adapter<RidersRecyclerViewAdapter.ViewHolder> {


    private RecyclerViewItemClickListener mListener ;
    private List<T> data ;

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

    public RidersRecyclerViewAdapter(RecyclerViewItemClickListener listener){mListener = listener;}

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
        holder.riderName.setText(((UserEntity)item).getFirstname() +" "+ ((UserEntity)item).getLastname());
        holder.workingZone.setText(((UserEntity)item).getIdZone());
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


/*
    private Context mContext;
    private List<UserEntity> mRiders;
    private Application app;
    private SchedulesEntity riderSchedule;


    public RidersRecyclerViewAdapter(List<UserEntity> mRiders, Context context, Application app){
        mContext = context ;
        this.mRiders = mRiders ;
        this.app = app ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_item, parent, false);
        ViewHolder  viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder){
        holder.bindItem(mContext, RidersRecyclerViewAdapter.this, mRiders, app);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView riderName ;
        private TextView workingZone ;
        private TextView email ;
        private TextView role ;

        private UserEntity rider ;
        private Context mContext ;
        private Application app ;
        private List<UserEntity> mRiders ;
        private RidersRecyclerViewAdapter ridersRecyclerViewAdapter ;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            riderName = itemView.findViewById(R.id.txt_riderName);
            workingZone = itemView.findViewById(R.id.txt_workingZone);
            email = itemView.findViewById(R.id.txt_email);
            role = itemView.findViewById(R.id.txt_role);
        }

        public void bindItem(Context mContext, RidersRecyclerViewAdapter ridersRecyclerViewAdapter, List<UserEntity> mRiders, Application app){
            */
/*this.rider = rider ;*//*

            this.mContext = mContext ;
            this.ridersRecyclerViewAdapter = ridersRecyclerViewAdapter ;
            this.mRiders = mRiders ;
            this.app = app ;
        }

        @Override
        public boolean onLongClick(View v){

            return true ;
        }

        @Override
        public void onClick(View v){
            Intent intent = new Intent(mContext, DetailsRiderActivity.class);
            intent.putExtra("riderId", rider.getIdUser());
            mContext.startActivity(intent);
        }

    }
*/

    }


