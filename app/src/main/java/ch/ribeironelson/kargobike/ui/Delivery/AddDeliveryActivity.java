package ch.ribeironelson.kargobike.ui.Delivery;

import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.CustomerEntity;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.ProductEntity;
import ch.ribeironelson.kargobike.database.entity.TripEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.database.repository.DeliveryRepository;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.ui.WorkingZones.ModifyWorkingZoneActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.CustomerListViewModel;
import ch.ribeironelson.kargobike.viewmodel.DeliveryViewModel;
import ch.ribeironelson.kargobike.viewmodel.ProductListViewModel;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneViewModel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddDeliveryActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "Add Delivery Activity";

    private ListAdapter<String> adapterWorkingZoneList;
    private ListAdapter<String> adapterProductList;
    private ListAdapter<String> adaptreCustomerList;
    private List<WorkingZoneEntity> workingZoneEntities;
    private List<ProductEntity> productEntities;
    private List<CustomerEntity> customerEntities;
    private Spinner spinnerWorkingZones;
    private Spinner spinnerProducts;
    private Spinner spinnerCustomers;

    private EditText DateData;
    private EditText DescriptionData;
    private EditText DeparturePlaceData;
    private EditText finalDestinationData;
    private EditText NumberData;
    private EditText ClientData;
    private EditText TimeData;

    private String date;
    private String description;
    private String departure;
    private String destination;
    private String time;
    private long nbProducts;
    private WorkingZoneListViewModel listViewModelWorkingZone;
    private ProductListViewModel listViewModelProduct;
    private CustomerListViewModel listViewModelCustomer;

    private Button addDeliveryBtn;
    private Button btnDatePicker, btnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private WorkingZoneEntity workingZoneEntity;
    private ProductEntity productEntity;
    private CustomerEntity customerEntity;

    final Calendar myCalender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_delivery, frameLayout);
        navigationView.setCheckedItem(R.id.nav_add_delivery);

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


        //Spinner
        //Spinner for Products
        spinnerProducts = (Spinner) findViewById(R.id.spinnerProducts);
        adapterProductList = new ListAdapter<>(AddDeliveryActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerProducts.setAdapter(adapterProductList);


        //Spinner for zones
        spinnerWorkingZones = (Spinner) findViewById(R.id.spinnerWorkingZones);
        adapterWorkingZoneList = new ListAdapter<>(AddDeliveryActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerWorkingZones.setAdapter(adapterWorkingZoneList);

        //Spinner for customers
        spinnerCustomers = (Spinner) findViewById(R.id.spinnerCustomer);
        adaptreCustomerList = new ListAdapter<>(AddDeliveryActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerCustomers.setAdapter(adaptreCustomerList);
        fillSpinners();
        assignSpinnerActions();

    }

    private void fillSpinners() {

        WorkingZoneListViewModel.Factory factory3 = new WorkingZoneListViewModel.Factory(getApplication());
        listViewModelWorkingZone = ViewModelProviders.of(this,factory3).get(WorkingZoneListViewModel.class);

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
                adapterWorkingZoneList.updateData(new ArrayList<>(workingZoneNames));
            }
        });

        ProductListViewModel.Factory factory4 = new ProductListViewModel.Factory(getApplication());
        listViewModelProduct = ViewModelProviders.of(this,factory4).get(ProductListViewModel.class);

        listViewModelProduct.getProducts().observe(this, productEntityList -> {
            if (productEntityList != null) {
                //Array
                ArrayList<String> productNames = new ArrayList<String>();
                productEntities = new ArrayList<>();
                for (ProductEntity u : productEntityList) {
                    Log.d("Product", u.toString());
                        productNames.add(u.getName() + "-" + Long.valueOf(u.getPrice()));
                    productEntities.add(u);
                }
                adapterProductList.updateData(new ArrayList<>(productNames));
            }
        });

        CustomerListViewModel.Factory factory5 = new CustomerListViewModel.Factory(getApplication());
        listViewModelCustomer = ViewModelProviders.of(this,factory5).get(CustomerListViewModel.class);

        listViewModelCustomer.getCustomers().observe(this, customerEntityList -> {
            if (customerEntityList != null) {
                //Array
                ArrayList<String> customerNames = new ArrayList<String>();
                customerEntities = new ArrayList<>();
                for (CustomerEntity u : customerEntityList) {
                    Log.d("Customer", u.toString());
                    customerNames.add(u.getFirstname() + " " + u.getLastname());
                    customerEntities.add(u);
                }
                adaptreCustomerList.updateData(new ArrayList<>(customerNames));
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
                            String str ="";
                            if((monthOfYear+1)<10){
                                str="0";
                            }
                            DateData.setText(dayOfMonth + "." + str+(monthOfYear + 1) + "." + year);
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

    private void assignSpinnerActions() {
        spinnerWorkingZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                workingZoneEntity = workingZoneEntities.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                productEntity = productEntities.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinnerCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                customerEntity = customerEntities.get(position);
                finalDestinationData.setText(customerEntity.getAddress());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void verifyUserInputs(){
        date = DateData.getText().toString();
        time = TimeData.getText().toString();
        description = DescriptionData.getText().toString();
        departure = DeparturePlaceData.getText().toString();
        destination = finalDestinationData.getText().toString();
        nbProducts = Long.parseLong(NumberData.getText().toString());

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String timestamp = df.format(c);

        Log.d("timestamp : ", timestamp);

        if(!isAnyEditEmpty()){

            DeliveryEntity newDelivery = new DeliveryEntity(workingZoneEntity.getAssignedDispatcherId(),customerEntity.getIdCustomer(),timestamp,time, description, nbProducts, date,
                    departure, destination, "", "", productEntity.getIdProduct(), new ArrayList<TripEntity>());

            DeliveryRepository.getInstance().insert(newDelivery, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "createDelivery: success");
                    Toast toast = Toast.makeText(AddDeliveryActivity.this, "Created a new delivery!", Toast.LENGTH_LONG);
                    toast.show();
                    restartForm();
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    }

    private void restartForm() {
        DateData.setText("");
        TimeData.setText("");
        DescriptionData.setText("");
        DeparturePlaceData.setText("");
        finalDestinationData.setText("");
        NumberData.setText("");

        spinnerWorkingZones.setSelection(0);
        spinnerProducts.setSelection(0);
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
        if(time.length()<1){
            TimeData.setError("Yout must choose a delivery time !");
        }
        if(date.length()<1){
            DateData.setError("You must choose a delivery date");
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
