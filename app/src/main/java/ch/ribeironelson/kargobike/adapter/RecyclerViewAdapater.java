package ch.ribeironelson.kargobike.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;

public class RecyclerViewAdapater extends RecyclerView.Adapter<RecyclerViewAdapater.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList <UserEntity> users = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapater(Context context, ArrayList<UserEntity> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_account, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //for debuging
        UserEntity user = users.get(position);
        Log.d(TAG, "onBindViewHolder: called.");
        holder.lastname.setText(user.getLastname());
        holder.firstname.setText(user.getFirstname());
        holder.email.setText(user.getEmail());
        //holder.role.setText
        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lastname.setEnabled(true);
                holder.firstname.setEnabled(true);
                holder.email.setEnabled(true);
               // holder.role.setEnabled(true);
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lastname.setText(user.getLastname());
                holder.firstname.setText(user.getFirstname());
                holder.email.setText(user.getEmail());
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText lastname;
        EditText firstname;
        EditText email;
        Spinner role;
        Button modify;
        Button save;
        Button cancel;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lastname= itemView.findViewById(R.id.LastnameData);
            firstname= itemView.findViewById(R.id.FirstnameData);
            email = itemView.findViewById(R.id.EmailData);
            role= itemView.findViewById(R.id.RoleSpinner);
            modify=itemView.findViewById(R.id.ButtonModify);
            save = itemView.findViewById(R.id.ButtonSave);
            cancel = itemView.findViewById(R.id.ButtonCancel);
        }
    }
}
