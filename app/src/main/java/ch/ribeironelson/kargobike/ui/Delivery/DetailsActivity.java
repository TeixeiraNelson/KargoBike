package ch.ribeironelson.kargobike.ui.Delivery;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.entity.TripEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.database.repository.SchedulesRepository;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.ui.DeliveryCompleteActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.util.TimeStamp;
import ch.ribeironelson.kargobike.viewmodel.CheckpointListViewModel;
import ch.ribeironelson.kargobike.viewmodel.DeliveryViewModel;
import ch.ribeironelson.kargobike.viewmodel.SchedulesListViewModel;
import ch.ribeironelson.kargobike.viewmodel.UserViewModel;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DetailsActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "Add Delivery Activity";

    private ListAdapter<String> adapterWorkingZoneList;
    private ListAdapter<String> adapterProductList;
    private List<WorkingZoneEntity> workingZoneEntities;
    private Spinner spinnerWorkingZones;
    private Spinner spinnerProducts;

    private CheckpointEntity selectedCheckpoint;
    private String selectedNextRider;

    private EditText DateData;
    private EditText DescriptionData;
    private EditText DeparturePlaceData;
    private EditText finalDestinationData;
    private EditText NumberData;
    private EditText ClientData;
    private EditText TimeData;
    private TextView nextCheckpoint;
    private SchedulesEntity riderSchedule;

    private Button nextCheckpointModify;
    private Button deleteButton;

    private DeliveryEntity delivery;

    private String date;
    private String description;
    private String departure;
    private String destination;
    private String time;
    private long nbProducts;
    private String client;
    private String user;
    private String product;
    private boolean pageEnabled = false;
    private WorkingZoneListViewModel listViewModelWorkingZone;
    private DeliveryViewModel deliveryViewModel;

    private Button addDeliveryBtn;
    private EditText commentData;
    private Button btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private WorkingZoneEntity workingZoneEntity;

    private String deliveryId;
    final Calendar myCalender = Calendar.getInstance();
    private SchedulesListViewModel schedulesListViewModel;
    private List<SchedulesEntity> mSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_modify_details2, frameLayout);

        DateData = findViewById(R.id.DateData);
        TimeData = findViewById(R.id.DateTime);
        deleteButton = findViewById(R.id.btn_delete_delivery);
        nextCheckpoint = findViewById(R.id.nextCheckpoint);
        nextCheckpointModify = findViewById(R.id.btn_update_checkpoint);
        DescriptionData = findViewById(R.id.DescriptionText2);
        DeparturePlaceData = findViewById(R.id.DeparturePlaceData);
        finalDestinationData = findViewById(R.id.finalDestinationData);
        NumberData = findViewById(R.id.NumberData);
        ClientData = findViewById(R.id.ClientData);
        addDeliveryBtn = findViewById(R.id.btnCreateDelivery);
        addDeliveryBtn.setOnClickListener(v -> verifyUserInputs());
        nbProducts = 0;
        btnDatePicker=findViewById(R.id.btn_date);
        btnTimePicker=findViewById(R.id.btn_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        deliveryId = getIntent().getStringExtra("deliveryId");


        requestData();
        disableButtons();

        deleteButton.setOnClickListener(v-> {deleteButtonAction();});
    }

    private void deleteButtonAction() {
        UserViewModel.Factory factory3 = new UserViewModel.Factory(getApplication(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        UserViewModel userviewmodel = ViewModelProviders.of(this,factory3).get(UserViewModel.class);

        userviewmodel.getUser().observe(this, user -> {
            if (user != null) {
                // Getting user role
                verifyCredentials(user);
            }
        });
    }

    private void verifyCredentials(UserEntity user) {
        if(Integer.valueOf(user.getIdRole())>=2){
            DeliveryRepository.getInstance().delete(delivery, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(DetailsActivity.this, "Delivery deleted !", Toast.LENGTH_LONG).show();
                    DetailsActivity.this.finish();
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, "You have not permission to delete a delivery!", Toast.LENGTH_LONG).show();
        }
    }

    private void disableButtons() {
        DateData.setEnabled(pageEnabled);
        TimeData.setEnabled(pageEnabled);
        deleteButton.setEnabled(pageEnabled);
        nextCheckpointModify.setEnabled(pageEnabled);
        DescriptionData.setEnabled(pageEnabled);
        DeparturePlaceData.setEnabled(pageEnabled);
        NumberData.setEnabled(pageEnabled);
        ClientData.setEnabled(pageEnabled);
        finalDestinationData.setEnabled(pageEnabled);
    }

    private void assignValues() {
        String str ="";

        if(delivery.getNextPlaceToGo().getLocation()!=null)
            str = " - " + delivery.getNextPlaceToGo().getLocation().getLocation();

        DateData.setText(delivery.getDeliveryDate());
        TimeData.setText(delivery.getDeliveryTime());
        DescriptionData.setText(delivery.getDescription());
        DeparturePlaceData.setText(delivery.getDeparturePlace());
        NumberData.setText(String.valueOf(delivery.getNbPackages()));
        ClientData.setText(delivery.getClientName());
        nextCheckpoint.setText(delivery.getNextPlaceToGo().getName());
        finalDestinationData.setText(delivery.getFinalDestination());

        nextCheckpointModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(DetailsActivity.this);
                dialog.setContentView(R.layout.popup_window);
                dialog.setTitle("Next step");

                // set the custom dialog components - text, image and button
                Button dialogButton = (Button) dialog.findViewById(R.id.button_popup);
                CheckBox checkBox = dialog.findViewById(R.id.checkBox);
                Spinner nextRider = dialog.findViewById(R.id.spinner_next_rider);
                Spinner nextCheckpoint = dialog.findViewById(R.id.spinner_checkpoint);
                commentData = dialog.findViewById(R.id.comment_content);

                if(!delivery.isLoaded()){
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
                        updateActualCheckpoint(v);
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });
    }

    private void loadDataSchedules() {
        SchedulesListViewModel.Factory factory2 = new SchedulesListViewModel.Factory(
                getApplication());
        schedulesListViewModel = ViewModelProviders.of(this, factory2).get(SchedulesListViewModel.class);
        schedulesListViewModel.getSchedules().observe(this, schedulesEntities -> {
            if (schedulesEntities != null) {
                Log.d(TAG,"Deliveries Not null");
                mSchedules = schedulesEntities;
                verificateSchedule();
            }
        });
    }

    private void updateActualCheckpoint(View v) {
        if(delivery!=null){
            if(selectedCheckpoint!=null){
                delivery.setNextPlaceToGo(selectedCheckpoint);
            }
            if(selectedNextRider!=null && selectedNextRider.length()>1)
                delivery.setActuallyAssignedUser(selectedNextRider);

            if(delivery.getNextPlaceToGo().getName().equals("Final Destination") && !delivery.isLoaded()){
                delivery.setActuallyAssignedUser("Delivery Finished");
            }


            DeliveryRepository.getInstance().update(delivery, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d("Delivery status", "Delivery checkpoint added !");
                    if(delivery.getActuallyAssignedUser().equals("Delivery Finished")){
                        riderSchedule.addDelivery();
                        SchedulesRepository.getInstance().updateSchedules(riderSchedule, new OnAsyncEventListener() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(DetailsActivity.this, DeliveryCompleteActivity.class);
                                intent.putExtra("DeliveryEntity",delivery);
                                DetailsActivity.this.startActivity(intent);
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


    private void loadDataCheckpoints(Spinner nextCheckpoint) {
        CheckpointListViewModel.Factory factory = new CheckpointListViewModel.Factory(
                DetailsActivity.this.getApplication());
        CheckpointListViewModel viewModel = ViewModelProviders.of((DetailsActivity) DetailsActivity.this, factory).get(CheckpointListViewModel.class);
        viewModel.getCheckpoints().observe((LifecycleOwner) DetailsActivity.this, deliveryEntities -> {
            if (deliveryEntities != null) {
                addToSpinner(deliveryEntities, nextCheckpoint);
            }
        });
    }

    private void addToSpinnerRider(List<UserEntity> deliveryEntities, Spinner nextRider) {
        List<String> list = new ArrayList<>();
        for(UserEntity c : deliveryEntities){
            list.add(c.getFirstname()+ " " + c.getLastname());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DetailsActivity.this,
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

    private void addToSpinner(List<CheckpointEntity> deliveryEntities, Spinner nextCheckpoint) {
        List<String> list = new ArrayList<>();
        for(CheckpointEntity c : deliveryEntities){
            list.add(c.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DetailsActivity.this,
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

    private void loadDataNextRider(Spinner nextRider) {
        UsersListViewModel.Factory factory = new UsersListViewModel.Factory(
                DetailsActivity.this.getApplication());
        UsersListViewModel viewModel = ViewModelProviders.of((DetailsActivity) DetailsActivity.this, factory).get(UsersListViewModel.class);
        viewModel.getAllUsers().observe((LifecycleOwner) DetailsActivity.this, deliveryEntities -> {
            if (deliveryEntities != null) {
                addToSpinnerRider(deliveryEntities, nextRider);
            }
        });
    }

    private void requestData() {
        DeliveryViewModel.Factory factory3 = new DeliveryViewModel.Factory(getApplication(), deliveryId);
        deliveryViewModel = ViewModelProviders.of(this,factory3).get(DeliveryViewModel.class);

        deliveryViewModel.getDelivery().observe(this, deliveryEntity -> {
            if (deliveryEntity != null) {
                this.delivery = deliveryEntity;
                assignValues();
            }
        });


        WorkingZoneListViewModel.Factory factory4 = new WorkingZoneListViewModel.Factory(getApplication());
        listViewModelWorkingZone = ViewModelProviders.of(this,factory4).get(WorkingZoneListViewModel.class);

        listViewModelWorkingZone.getAllWorkingZones().observe(this, workingZoneEntityList -> {
            if (workingZoneEntityList != null) {
                //Array
                ArrayList<String> workingZoneNames = new ArrayList<String>();
                workingZoneEntities = new ArrayList<>();
                for (WorkingZoneEntity u : workingZoneEntityList) {
                    Log.d("WORKING ZONE", u.toString());
                    if(!u.getWorkingZoneId().equals("0")) {
                        workingZoneNames.add(u.getLocation());
                        workingZoneEntities.add(u);
                    }
                }
            }
        });

        loadDataSchedules();


    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            DateData.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            TimeData.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }


    private void verifyUserInputs(){
        if(!pageEnabled){
            pageEnabled = true;
            disableButtons();
            addDeliveryBtn.setText("SAVE");
        } else {

            date = DateData.getText().toString();
            time = TimeData.getText().toString();
            description = DescriptionData.getText().toString();
            departure = DeparturePlaceData.getText().toString();
            destination = finalDestinationData.getText().toString();
            nbProducts = Long.parseLong(NumberData.getText().toString());
            client = ClientData.getText().toString();

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            String timestamp = df.format(c);

            Log.d("timestamp : ", timestamp);

            if(!isAnyEditEmpty()){

                DeliveryEntity updatedDelivery = this.delivery;
                updatedDelivery.setDeliveryDate(date);
                updatedDelivery.setDeliveryTime(time);
                updatedDelivery.setDescription(description);
                updatedDelivery.setDeparturePlace(departure);
                updatedDelivery.setFinalDestination(destination);
                updatedDelivery.setNbPackages(nbProducts);
                updatedDelivery.setClientName(client);

                DeliveryRepository.getInstance().update(updatedDelivery, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "createDelivery: success");
                        Toast toast = Toast.makeText(DetailsActivity.this, "Delivery updated!", Toast.LENGTH_LONG);
                        toast.show();
                        pageEnabled=false;
                        disableButtons();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        }

    }

    private boolean isAnyEditEmpty(){
        if(date.length()<1){
            DateData.setError("You must enter a date !");
            return true;
        }
        if(description.length()<1){
            DescriptionData.setError("You must enter a description !");
            return true;
        }
        if(departure.length()<1){
            DeparturePlaceData.setError("You must enter a departure !");
            return true;
        }
        if(destination.length()<1){
            finalDestinationData.setError("You must enter a destination !");
            return true;
        }
        if(nbProducts==0){
            NumberData.setError("You must enter the count of the products !");
            return true;
        }
        if(client.length()<1){
            ClientData.setError("You must enter the client name !");
            return true;
        }
        return false;
    }

    private boolean isMySchedule(SchedulesEntity sch) {
        if(sch.getUserID().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            return true;

        return false;
    }

    private boolean verificateSchedule() {

        for(SchedulesEntity sch : mSchedules){
            if(isTodaySchedule(sch) && isMySchedule(sch)){
                riderSchedule = sch;
                return true;
            }
        }
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_delivery);
    }

}
