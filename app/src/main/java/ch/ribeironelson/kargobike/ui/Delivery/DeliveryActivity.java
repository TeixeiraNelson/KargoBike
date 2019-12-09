package ch.ribeironelson.kargobike.ui.Delivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.DeliveriesRecyclerViewAdapter;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.viewmodel.DeliveriesListViewModel;

public class DeliveryActivity extends BaseActivity {

    private static final String TAG = "Delivery Activity";

    private EditText searchEditText;
    private RecyclerView recyclerView;

    private DeliveriesRecyclerViewAdapter adapter;
    private List<DeliveryEntity> mdeliveryEntities;
    private DeliveriesListViewModel viewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_delivery, frameLayout);
        navigationView.setCheckedItem(R.id.nav_delivery);

        searchEditText = findViewById(R.id.search_deliveries);
        recyclerView = findViewById(R.id.recyclerView_deliveries);

        adapter = new DeliveriesRecyclerViewAdapter(mdeliveryEntities,DeliveryActivity.this, DeliveryActivity.this.getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeliveryActivity.this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a new Delivery", Snackbar.LENGTH_LONG);
                Intent intent=new Intent (DeliveryActivity.this, AddDeliveryActivity.class);
                startActivity(intent);
            }
        });

        setupViewModel();
    }

    private void setupViewModel() {
        DeliveriesListViewModel.Factory factory = new DeliveriesListViewModel.Factory(
                getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(DeliveriesListViewModel.class);
        viewModel.getDeliveries().observe(this, deliveryEntities -> {
            if (deliveryEntities != null) {
                filterDeliveriesByUserAndDate(deliveryEntities);
                Log.d(TAG,"Deliveries Not null");
                adapter.updateData(mdeliveryEntities);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_addDelivery) {
            Intent intent = new Intent(this, AddDeliveryActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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

    private void filterDeliveriesByUserAndDate(List<DeliveryEntity> deliveryEntities) {
        mdeliveryEntities = new ArrayList<>();

        for( DeliveryEntity d : deliveryEntities){
            if(isToday(d.getDeliveryDate())){
                if(isMyDelivery(d.getActuallyAssignedUser())){
                    mdeliveryEntities.add(d);
                }
            }
        }

    }

    private boolean isMyDelivery(String assignedUser) {
        String loggedUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(assignedUser.equals(loggedUserId))
            return true;

        Log.d(TAG,"returning false is my delivery");
        return false;
    }

    private boolean isToday(String deliveryDate) {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c);

        if(deliveryDate.equals(formattedDate))
            return true;

        return false;
    }


}
