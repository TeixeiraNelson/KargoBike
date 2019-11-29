package ch.ribeironelson.kargobike.ui.Delivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.DeliveryEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.viewmodel.DeliveriesListViewModel;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AddDeliveryActivity extends BaseActivity {

    private static final String TAG = "Add Delivery Activity";
    private DeliveriesListViewModel viewModel;
    private ListAdapter<String> adpaterUserList;
    private ListAdapter<String> adapterProductList;
    private Spinner spinnerUsers;
    private Spinner spinnerProducts;
    private List<String> products = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_delivery, frameLayout);
        navigationView.setCheckedItem(R.id.nav_delivery);

        products.add("Automobile");
        products.add("Business Services");
        products.add("Computers");
        products.add("Education");
        products.add("Personal");
        products.add("Travel");

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
        DeliveriesListViewModel.Factory factory = new DeliveriesListViewModel.Factory(
                getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(DeliveriesListViewModel.class);
        viewModel.getDeliveries().observe(this, deliveryEntities -> {
            if (deliveryEntities != null) {
                Log.d(TAG,"Usernames Not null");
                //Array shopnames
                ArrayList<String> userNames = new ArrayList<String>();
                for (DeliveryEntity d : deliveryEntities
                ) {
                    userNames.add(d.getDescription());
                }
                updateAdapterUserList(userNames);
            }
        });

    }

    private void updateAdapterUserList(List<String> userNames) {
        adpaterUserList.updateData(new ArrayList<>(userNames));
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
