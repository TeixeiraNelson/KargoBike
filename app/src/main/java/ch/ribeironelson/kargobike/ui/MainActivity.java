package ch.ribeironelson.kargobike.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.util.DeliveriesRecyclerViewAdapter;
import ch.ribeironelson.kargobike.viewmodel.DeliveriesListViewModel;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "Main Activity";

    private EditText searchEditText;
    private RecyclerView recyclerView;

    private DeliveriesRecyclerViewAdapter adapter;
    private List<DeliveryEntity> mdeliveryEntities;
    private DeliveriesListViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
        navigationView.setCheckedItem(R.id.nav_home);

        searchEditText = findViewById(R.id.search_deliveries);
        recyclerView = findViewById(R.id.recyclerView_deliveries);

        adapter = new DeliveriesRecyclerViewAdapter(mdeliveryEntities,MainActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        setupViewModel();
    }

    private void setupViewModel() {
        DeliveriesListViewModel.Factory factory = new DeliveriesListViewModel.Factory(
                getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(DeliveriesListViewModel.class);
        viewModel.getDeliveries().observe(this, deliveryEntities -> {
            if (deliveryEntities != null) {
                mdeliveryEntities = deliveryEntities;
                Log.d(TAG,"Deliveries Not null");
                adapter.updateData(mdeliveryEntities);
            }
        });
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
    }
}
