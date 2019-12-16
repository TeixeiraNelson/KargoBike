package ch.ribeironelson.kargobike.ui.Delivery;

import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.TripEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.DeliveryViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

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
    private List<String> products = new ArrayList<String>();

    private EditText DateData;
    private EditText DescriptionData;
    private EditText DeparturePlaceData;
    private EditText finalDestinationData;
    private EditText NumberData;
    private EditText ClientData;
    private EditText TimeData;

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
    private Button btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private WorkingZoneEntity workingZoneEntity;

    private String deliveryId;
    final Calendar myCalender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_modify_details2, frameLayout);

        DateData = findViewById(R.id.DateData);
        TimeData = findViewById(R.id.DateTime);
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
        products.add("Product 1");
        products.add("Product 2");
        products.add("Product 3");
        products.add("Product 4");
        products.add("Product 5");
        products.add("Product 6");

        requestData();
        disableButtons();
    }

    private void disableButtons() {
        DateData.setEnabled(pageEnabled);
        TimeData.setEnabled(pageEnabled);
        DescriptionData.setEnabled(pageEnabled);
        DeparturePlaceData.setEnabled(pageEnabled);
        NumberData.setEnabled(pageEnabled);
        ClientData.setEnabled(pageEnabled);
        finalDestinationData.setEnabled(pageEnabled);
    }

    private void assignValues() {
        DateData.setText(delivery.getDeliveryDate());
        TimeData.setText(delivery.getDeliveryTime());
        DescriptionData.setText(delivery.getDescription());
        DeparturePlaceData.setText(delivery.getDeparturePlace());
        NumberData.setText(String.valueOf(delivery.getNbPackages()));
        ClientData.setText(delivery.getClientName());
        finalDestinationData.setText(delivery.getFinalDestination());
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
