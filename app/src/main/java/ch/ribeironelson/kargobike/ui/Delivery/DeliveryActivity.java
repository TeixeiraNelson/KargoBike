package ch.ribeironelson.kargobike.ui.Delivery;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
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
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListenerSchedule;
import ch.ribeironelson.kargobike.util.TimeStamp;
import ch.ribeironelson.kargobike.viewmodel.DeliveriesListViewModel;
import ch.ribeironelson.kargobike.viewmodel.SchedulesListViewModel;

public class DeliveryActivity extends BaseActivity {

    private static final String TAG = "Delivery Activity";

    private EditText searchEditText;
    private RecyclerView recyclerView;

    private DeliveriesRecyclerViewAdapter adapter;
    private List<DeliveryEntity> mdeliveryEntities;
    private DeliveriesListViewModel viewModel;
    private SchedulesListViewModel schedulesListViewModel;
    private List<SchedulesEntity> mSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_delivery, frameLayout);
        navigationView.setCheckedItem(R.id.nav_delivery);

        searchEditText = findViewById(R.id.search_deliveries);
        recyclerView = findViewById(R.id.recyclerView_deliveries);



        setupViewModel();


    }

    private void startVerificationProcess(){
        if(!hasUserAlreadyStartedShift())
            showPopupSafetyCheck();
        else
            displayDeliveries();
    }

    private void displayDeliveries() {
        adapter.updateData(mdeliveryEntities);
    }

    private boolean hasUserAlreadyStartedShift() {

        for(SchedulesEntity sch : mSchedules){
            if(isTodaySchedule(sch) && isMySchedule(sch)){
                System.out.println("BINDIN SCHEDULE");
                adapter.bindSchedule(sch);
                return true;
            }
        }
        return false;
    }

    private boolean isMySchedule(SchedulesEntity sch) {
        if(sch.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return true;

        return false;
    }

    public static boolean isTodaySchedule(SchedulesEntity sch) {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = df.format(c);

        Date c2 = null;
        try {
            c2 = df.parse(sch.getBeginningDateTime().substring(0,sch.getBeginningDateTime().indexOf("-")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String scheduleDate = df.format(c2);
        if(scheduleDate.equals(formattedDate))
            return true;

        return false;
    }

    private void showPopupSafetyCheck() {
        final Dialog dialog = new Dialog(DeliveryActivity.this);
        dialog.setContentView(R.layout.popup_window_safetycheck);
        dialog.setTitle("Next step");

        // set the custom dialog components - text, image and button
        Button dialogButton = (Button) dialog.findViewById(R.id.button_popup);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.button_popup2);
        CheckBox checkBox = dialog.findViewById(R.id.checkBox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                if(checked){
                    dialogButton.setEnabled(true);
                } else {
                    dialogButton.setEnabled(false);
                }
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SchedulesEntity schedule = new SchedulesEntity("",FirebaseAuth.getInstance().getCurrentUser().getUid(), TimeStamp.getTimeStamp(), "", true, Long.valueOf(0));
                SchedulesRepository.getInstance().insertSchedules(schedule, new OnAsyncEventListenerSchedule() {
                    @Override
                    public void onSuccess(SchedulesEntity sch) {
                        Log.d(TAG,"User " + FirebaseAuth.getInstance().getCurrentUser().getUid() + " started his shift and make the safety check.");
                        dialog.dismiss();
                        Toast.makeText(DeliveryActivity.this,"Your shift started at " + TimeStamp.getTimeStamp() + " !", Toast.LENGTH_LONG).show();
                        adapter.bindSchedule(sch);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG,"Was not able to start schedule, database error.");
                    }
                });

            }
        });

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DeliveryActivity.this.finish();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void setupViewModel() {
        DeliveriesListViewModel.Factory factory = new DeliveriesListViewModel.Factory(
                getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(DeliveriesListViewModel.class);
        viewModel.getDeliveries().observe(this, deliveryEntities -> {
            if (deliveryEntities != null) {
                filterDeliveriesByUserAndDate(deliveryEntities);
                Log.d(TAG,"Deliveries Not null");
            }
        });

        SchedulesListViewModel.Factory factory2 = new SchedulesListViewModel.Factory(
                getApplication());
        schedulesListViewModel = ViewModelProviders.of(this, factory2).get(SchedulesListViewModel.class);
        schedulesListViewModel.getSchedules().observe(this, schedulesEntities -> {
            if (schedulesEntities != null) {
                Log.d(TAG,"Deliveries Not null");
                mSchedules = schedulesEntities;
                startVerificationProcess();
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
        navigationView.setCheckedItem(R.id.nav_delivery);
    }

    private void filterDeliveriesByUserAndDate(List<DeliveryEntity> deliveryEntities) {
        mdeliveryEntities = new ArrayList<>();
        System.out.println("FILTERING DELIVERIES");
        for( DeliveryEntity d : deliveryEntities){
            if(isToday(d.getDeliveryDate())){
                System.out.println("IS TODAY");
                if(isMyDelivery(d.getActuallyAssignedUser())){
                    mdeliveryEntities.add(d);
                    System.out.println("ADDED : " + d.getIdDelivery());
                }
            }
        }

        adapter = new DeliveriesRecyclerViewAdapter(mdeliveryEntities,DeliveryActivity.this, DeliveryActivity.this.getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DeliveryActivity.this));
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
        System.out.println("TESTING DATE");
        System.out.println(formattedDate+"-"+deliveryDate);
        if(deliveryDate.equals(formattedDate)){
            System.out.println("RETURNING TRUE");
            return true;
        }


        return false;
    }


}
