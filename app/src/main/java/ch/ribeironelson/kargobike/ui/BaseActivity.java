package ch.ribeironelson.kargobike.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.ui.Checkpoint.ModifyCheckpointsActivity;
import ch.ribeironelson.kargobike.ui.Customer.ModifyCustomerActivity;
import ch.ribeironelson.kargobike.ui.Delivery.AddDeliveryActivity;
import ch.ribeironelson.kargobike.ui.Delivery.DeliveryActivity;
import ch.ribeironelson.kargobike.ui.Product.ModifyProductActivity;
import ch.ribeironelson.kargobike.ui.Riders.RidersList;
import ch.ribeironelson.kargobike.ui.WorkingZones.ModifyWorkingZoneActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.util.TimeStamp;
import ch.ribeironelson.kargobike.viewmodel.SchedulesListViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "BaseActivity";
    protected FrameLayout frameLayout;

    protected DrawerLayout drawerLayout;

    protected NavigationView navigationView;

    protected static int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        frameLayout = findViewById(R.id.flContent);

        drawerLayout = findViewById(R.id.base_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.base_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        BaseActivity.position = 0;
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == BaseActivity.position) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }
        BaseActivity.position = id;
        Intent intent = null;

        navigationView.setCheckedItem(item);

        if (id == R.id.nav_delivery) {
            intent=new Intent (BaseActivity.this, DeliveryActivity.class);
        } else if (id == R.id.nav_riders){
            intent = new Intent(BaseActivity.this, RidersList.class);
        }else if (id == R.id.nav_about) {
            intent=new Intent (BaseActivity.this, About.class);
        } else if (id == R.id.nav_logout){
            intent= new Intent(BaseActivity.this, LoginActivity.class);
            finishSchedule();
        } else if (id == R.id.nav_add_delivery){
            intent = new Intent(BaseActivity.this, AddDeliveryActivity.class);
        }
        else if (id == R.id.nav_checkpoints){
            intent = new Intent(BaseActivity.this, ModifyCheckpointsActivity.class);
            intent.putExtra("mode", "add");
        }
        else if (id == R.id.nav_products){
            intent = new Intent(BaseActivity.this, ModifyProductActivity.class);
            intent.putExtra("mode", "add");
        }
        else if (id == R.id.nav_customers){
            intent = new Intent(BaseActivity.this, ModifyCustomerActivity.class);
            intent.putExtra("mode", "add");
        }
        else if (id == R.id.nav_working_zones){
            intent = new Intent(BaseActivity.this, ModifyWorkingZoneActivity.class);
            intent.putExtra("mode", "add");
        }
        if (intent != null) {
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NO_ANIMATION
            );
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void finishSchedule() {
        SchedulesListViewModel.Factory factory2 = new SchedulesListViewModel.Factory(
                getApplication());
        SchedulesListViewModel schedulesListViewModel = ViewModelProviders.of(this, factory2).get(SchedulesListViewModel.class);
        schedulesListViewModel.getSchedules().observe(this, schedulesEntities -> {
            if (schedulesEntities != null) {
                updateScheduleFinishDate(schedulesEntities);
            }
        });
    }

    private void updateScheduleFinishDate(List<SchedulesEntity> schedulesEntities) {
         for(SchedulesEntity sch : schedulesEntities){
            if(DeliveryActivity.isTodaySchedule(sch) && sch.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                sch.setEndingDateTime(TimeStamp.getTimeStamp());
                SchedulesRepository.getInstance().updateSchedules(sch, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"Schedule ended properly");
                        LoginActivity.Logout();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG,"Schedule end error");
                    }
                });
            }
        }
    }
}
