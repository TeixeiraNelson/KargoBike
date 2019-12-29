package ch.ribeironelson.kargobike.ui.Riders;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.RidersRecyclerViewAdapter;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.util.RecyclerViewItemClickListener;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;

public class RidersList extends BaseActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;

    private RidersRecyclerViewAdapter<UserEntity> adapter;
    private UsersListViewModel usersListViewModel ;
    private List<UserEntity> users ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_riders_list, frameLayout);
        navigationView.setCheckedItem(R.id.nav_delivery);

        searchEditText = findViewById(R.id.search_riders);
        recyclerView = findViewById(R.id.recyclerView_riders);


        users = new ArrayList<>();
        adapter = new RidersRecyclerViewAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

            }

            @Override
            public void onItemLongClick(View v, int position) {

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
