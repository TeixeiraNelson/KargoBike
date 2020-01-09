package ch.ribeironelson.kargobike.ui.Riders;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.RidersRecyclerViewAdapter;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.util.RecyclerViewItemClickListener;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;

public class RidersList extends BaseActivity {

    private static final String TAG = "RidersList" ;

    private RecyclerView recyclerView;

    private RidersRecyclerViewAdapter<UserEntity> adapter;
    private UsersListViewModel usersListViewModel ;
    private List<UserEntity> users ;
    private Context mContext ;
    private Application app ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_riders_list, frameLayout);
        navigationView.setCheckedItem(R.id.nav_riders);

        recyclerView = findViewById(R.id.recyclerView_riders);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        users = new ArrayList<>();
        adapter = new RidersRecyclerViewAdapter<>(RidersList.this, RidersList.this.getApplication(), new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "Clicked position: "+position);
                Log.d(TAG, "Clicked on: "+users.get(position));

                Intent intent = new Intent(RidersList.this, RiderDetails.class);
                intent.putExtra("userid", users.get(position).getIdUser());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "Clicked position: "+position);
                Log.d(TAG, "Clicked on: "+users.get(position));

                Intent intent = new Intent(RidersList.this, RiderDetails.class);
                intent.putExtra("userid", users.get(position).getIdUser());
                startActivity(intent);
            }
        });

        UsersListViewModel.Factory factory = new UsersListViewModel.Factory(getApplication());
        usersListViewModel = ViewModelProviders.of(this, factory).get(UsersListViewModel.class);
        usersListViewModel.getAllUsers().observe(this, userEntities -> {
            if(userEntities != null){
                users = userEntities ;
                adapter.setData(users);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(RidersList.this));
        recyclerView.setAdapter(adapter);


    }

}
