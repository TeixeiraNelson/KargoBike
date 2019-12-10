package ch.ribeironelson.kargobike.ui.Delivery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.TripEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;

public class ModifyDetailsActivity extends AppCompatActivity {
    private static final String TAG = "ModifyDetailsActivity";
    private EditText DateData;
    private EditText HoursData;
    //Description = description de la delivery
    private EditText DescriptionData;
    private EditText DepartData;
    private EditText ArrivalData;
    private EditText NumberData;
    private EditText ClientData;
    private Spinner CyclistData;
    private String CyclistText;
    private ImageView DeliveryPicture;
    private ImageView SignaturPicture;
    private ImageButton ButtonSignature;
    private ImageButton ButtonProof;
    private Button ButtonSave;
    private Button ButtonBack;
    private DeliveryEntity Delivery;
    private Spinner ProductData;
    private UsersListViewModel viewModelUsers;
    private ListAdapter<String> adpaterUserList;

    public ModifyDetailsActivity(DeliveryEntity Delivery) {
        this.Delivery = Delivery;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        Delivery= (DeliveryEntity) i.getSerializableExtra("DeliveryDetails");
        setContentView(R.layout.activity_modify_details);
        DateData = findViewById(R.id.DateData);
        HoursData = findViewById(R.id.HoursData);
        DescriptionData = findViewById(R.id.DescriptionData);
        DepartData = findViewById(R.id.DepartData);
        ArrivalData = findViewById(R.id.ArrivalData);
        NumberData = findViewById(R.id.NumberData);
        ClientData = findViewById(R.id.ClientData);
        CyclistData = findViewById(R.id.CyclistData);
        DeliveryPicture = findViewById(R.id.DeliveryPicture);
        SignaturPicture = findViewById(R.id.SignaturPicture);
        ProductData= findViewById(R.id.ProductSpinner);
        // Capture button clicks
        ButtonBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                // Start NewActivity.class
                Intent myIntent = new Intent(ModifyDetailsActivity.this,
                        DetailsActivity.class);
                startActivity(myIntent);
            }

        });
        ButtonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            verifyuserinput();
                // Start NewActivity.class


                Intent myIntent = new Intent(ModifyDetailsActivity.this,
                        DetailsActivity.class);
                startActivity(myIntent);
            }

        });
        //Spinner for Users
        CyclistData = (Spinner) findViewById(R.id.spinnerUsers);
        adpaterUserList = new ListAdapter<>(ModifyDetailsActivity.this, R.layout.row_list, new ArrayList<>());
        CyclistData.setAdapter(adpaterUserList);
        setupViewModels();
        CyclistText = (String) CyclistData.getSelectedItem();
    }

    private void verifyuserinput() {
        String Date;
        String Hours;
        String Description;
        String Depart;
        String Arrival;
        String Number;
        String Client;
        String Cyclist;
        String Product;

        Date = DateData.getText().toString();
        Hours = HoursData.getText().toString();
        Description = DescriptionData.getText().toString();
        Depart = DepartData.getText().toString();
        Arrival = ArrivalData.getText().toString();
        Number = NumberData.getText().toString();
        Client = ClientData.getText().toString();
        String[] separated = CyclistText.split(" ");
        final String[] idUser = new String[1];
        viewModelUsers.getAllUsers().observe(this, userEntities -> {
            if (userEntities != null) {
                Log.d(TAG,"Usernames Not null");
                //Array username
                for (UserEntity u : userEntities) {
                    if(u.getFirstname().equals(separated[0]) && u.getLastname().equals(separated[1])){
                        idUser[0] = u.getIdUser();
                        return;
                    }
                }
            }
        });
        if(Date == null || Hours == null || Description == null || Depart == null || Arrival == null || Number == null || Client == null )
            Toast.makeText(this, "A field is empty", Toast.LENGTH_LONG).show();
        else{
            java.util.Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            df.setTimeZone(TimeZone.getTimeZone("GMT+1"));
            String timestamp = df.format(c);
            DeliveryEntity newDelivery = new DeliveryEntity(Delivery.getActuallyAssignedUser(),Client,timestamp,Hours, Description, Integer.valueOf(Number) , Date,
                    Depart, Arrival, "", "","", new ArrayList<TripEntity>());

        }

    }

    private void setupViewModels() {
        UsersListViewModel.Factory factory = new UsersListViewModel.Factory(
                getApplication());
        viewModelUsers = ViewModelProviders.of(this, factory).get(UsersListViewModel.class);
        viewModelUsers.getAllUsers().observe(this, userEntities -> {
            if (userEntities != null) {
                Log.d(TAG,"Usernames Not null");
                //Array username
                ArrayList<String> userNames = new ArrayList<String>();
                for (UserEntity u : userEntities
                ) {
                    userNames.add(u.getFirstname() + " " + u.getLastname());
                }
                updateAdapterUserList(userNames);
            }
        });

    }

    private void updateAdapterUserList(ArrayList<String> userNames) {
        adpaterUserList.updateData(new ArrayList<>(userNames));
    }
}
