package ch.ribeironelson.kargobike.ui.Delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.database.entity.UserEntity;
import ch.ribeironelson.kargobike.ui.About;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.DeliveriesListViewModel;
import ch.ribeironelson.kargobike.viewmodel.DeliveryViewModel;
import ch.ribeironelson.kargobike.viewmodel.UsersListViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddDeliveryActivity extends BaseActivity {

    private static final String TAG = "Add Delivery Activity";
    private UsersListViewModel viewModelUsers;
    private DeliveryViewModel deliveryViewModel;
    private ListAdapter<String> adpaterUserList;
    private ListAdapter<String> adapterProductList;
    private Spinner spinnerUsers;
    private Spinner spinnerProducts;
    private List<String> products = new ArrayList<String>();

    private EditText DateData;
    private EditText DescriptionData;
    private EditText DeparturePlaceData;
    private EditText finalDestinationData;
    private EditText NumberData;
    private EditText ClientData;

    private String date;
    private String description;
    private String departure;
    private String destination;
    private long nbProducts;
    private String client;
    private String user;
    private String product;

    private Button addDeliveryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_delivery, frameLayout);
        navigationView.setCheckedItem(R.id.nav_delivery);

        DateData = findViewById(R.id.DateData);
        DescriptionData = findViewById(R.id.DescriptionData);
        DeparturePlaceData = findViewById(R.id.DeparturePlaceData);
        finalDestinationData = findViewById(R.id.finalDestinationData);
        NumberData = findViewById(R.id.NumberData);
        ClientData = findViewById(R.id.ClientData);
        addDeliveryBtn = findViewById(R.id.btnCreateDelivery);
        addDeliveryBtn.setOnClickListener(v -> verifyUserInputs());
        nbProducts = 0;

        products.add("Product 1");
        products.add("Product 2");
        products.add("Product 3");
        products.add("Product 4");
        products.add("Product 5");
        products.add("Product 6");

        //Spinner
        //Spinner for Products
        spinnerProducts = (Spinner) findViewById(R.id.spinnerProducts);
        adapterProductList = new ListAdapter<>(AddDeliveryActivity.this, R.layout.row_list, products);
        spinnerProducts.setAdapter(adapterProductList);


        //Spinner for Users
        spinnerUsers = (Spinner) findViewById(R.id.spinnerUsers);
        adpaterUserList = new ListAdapter<>(AddDeliveryActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerUsers.setAdapter(adpaterUserList);
        setupViewModels();
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

    private void updateAdapterUserList(List<String> userNames) {
        adpaterUserList.updateData(new ArrayList<>(userNames));
    }

    private void verifyUserInputs(){
        date = DateData.getText().toString();
        description = DescriptionData.getText().toString();
        departure = DeparturePlaceData.getText().toString();
        destination = finalDestinationData.getText().toString();
        nbProducts = Long.parseLong(NumberData.getText().toString());
        client = ClientData.getText().toString();

        user = (String) spinnerUsers.getSelectedItem();
        product = (String) spinnerProducts.getSelectedItem();

        if(!isAnyEditEmpty()){

            String currentString = "Fruit: they taste good";
            String[] separated = user.split(" ");
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
            /*DeliveryEntity newDelivery = new DeliveryEntity(idUser[0], description, nbProducts, date,
                    departure, destination, "", "", product);*/
            DeliveryEntity newDelivery = null;
            DeliveryViewModel.Factory factoryD = new DeliveryViewModel.Factory(getApplication(), "0");
            deliveryViewModel = ViewModelProviders.of(this, factoryD).get(DeliveryViewModel.class);
            deliveryViewModel.createDelivery(newDelivery, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "createDelivery: success");
                    Toast toast = Toast.makeText(AddDeliveryActivity.this, "Created a new Delivery", Toast.LENGTH_LONG);
                    toast.show();
                    Intent h = new Intent(AddDeliveryActivity.this, DeliveryActivity.class);
                    startActivity(h);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "create article: failure", e);
                }
            });
        }
    }

    private boolean isAnyEditEmpty(){
        if(date.length()<1){
            DateData.setError("You mut enter a Date !");
            return true;
        }
        if(description.length()<1){
            DescriptionData.setError("You mut enter a Description !");
            return true;
        }
        if(departure.length()<1){
            DeparturePlaceData.setError("You mut enter a Departure !");
            return true;
        }
        if(destination.length()<1){
            finalDestinationData.setError("You mut enter a Destination !");
            return true;
        }
        if(nbProducts==0){
            NumberData.setError("You mut enter the Count of the Products !");
            return true;
        }
        if(client.length()<1){
            ClientData.setError("You mut ente the Client Name !");
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
