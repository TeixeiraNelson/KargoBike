package ch.ribeironelson.kargobike.ui.Customer;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.CustomerEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.CustomerListViewModel;
import ch.ribeironelson.kargobike.viewmodel.CustomerViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ModifyCustomerActivity extends BaseActivity {
    private static final String TAG = "ModifyProductActivity";
    private String mode = "add";

    private TextView description;
    private EditText customerFirstname;
    private EditText customerLastname;
    private EditText customerEmail;
    private EditText customerPhone;
    private EditText customerAddress;
    private Button btnSaveCustomer;
    private Button btnDeleteCustomer;
    private Spinner spinnerCustomers;
    FloatingActionButton fab;

    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String address;

    private CustomerEntity customer;

    private ListAdapter<String> adapterCustomerList;

    private CustomerViewModel viewModelCustomer;
    private CustomerListViewModel viewModelCustomerList;

    private List<CustomerEntity> customerEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_modify_customer, frameLayout);
        navigationView.setCheckedItem(R.id.nav_customers);

        mode = getIntent().getExtras().getString("mode","add");
        fab = findViewById(R.id.floatingActionButton);
        description = findViewById(R.id.textCustomerDescription);
        customerFirstname = findViewById(R.id.CustomerFirstnameData);
        customerLastname = findViewById(R.id.CustomerLastnameData);
        customerAddress = findViewById(R.id.CustomerLocationData);
        customerEmail = findViewById(R.id.CustomerEmailData);
        customerPhone = findViewById(R.id.CustomerPhoneData);
        btnSaveCustomer = findViewById(R.id.btnSaveCustomer);
        btnDeleteCustomer = findViewById(R.id.btnDeleteCustomer);
        spinnerCustomers = findViewById(R.id.spinner_customers);
        btnSaveCustomer.setOnClickListener(v -> verifyUserInputs());
        btnDeleteCustomer.setOnClickListener(v -> deleteCustomer());

        adapterCustomerList = new ListAdapter<>(ModifyCustomerActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerCustomers.setAdapter(adapterCustomerList);

        initComponents();

    }

    private void startEditingMode(){
        Intent intent = new Intent(ModifyCustomerActivity.this, ModifyCustomerActivity.class);
        intent.putExtra("mode", "modify");
        startActivity(intent);
        this.finish();
    }

    private void startAddMode(){
        Intent intent = new Intent(ModifyCustomerActivity.this, ModifyCustomerActivity.class);
        intent.putExtra("mode", "add");
        startActivity(intent);
        this.finish();
    }

    //init the Components and create the viewModel
    public void initComponents(){

        if(mode.equals("add")){
            description.setText("Add a new Product");
            btnDeleteCustomer.setVisibility(View.INVISIBLE);
            spinnerCustomers.setVisibility(View.INVISIBLE);
            btnSaveCustomer.setEnabled(true);
            customerFirstname.setEnabled(true);
            customerLastname.setEnabled(true);
            customerAddress.setEnabled(true);
            customerEmail.setEnabled(true);
            customerPhone.setEnabled(true);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startEditingMode();
                }
            });

        }
        if(mode.equals("modify")) {
            description.setText("Modify a Product");
            btnDeleteCustomer.setVisibility(View.VISIBLE);
            spinnerCustomers.setVisibility(View.VISIBLE);
            FloatingActionButton fab = findViewById(R.id.floatingActionButton);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_circle_red_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startAddMode();
                }
            });
        }

        fillSpinners();
        assignSpinnerActions();
    }

    private void assignSpinnerActions() {
        spinnerCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customer = customerEntities.get(position);
                CustomerViewModel.Factory factory = new CustomerViewModel.Factory(getApplication(),customer.getIdCustomer());
                viewModelCustomer = ViewModelProviders.of(ModifyCustomerActivity.this, factory).get(CustomerViewModel.class);
                if(mode.equals("modify")){
                    customerFirstname.setText(customer.getFirstname());
                    customerLastname.setText(customer.getLastname());
                    customerAddress.setText(customer.getAddress());
                    customerPhone.setText(customer.getPhone());
                    customerEmail.setText(customer.getEmail());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillSpinners() {
        CustomerListViewModel.Factory factory2 = new CustomerListViewModel.Factory(getApplication());
        viewModelCustomerList = ViewModelProviders.of(this, factory2).get(CustomerListViewModel.class);

        viewModelCustomerList.getCustomers().observe(this, customerEntitiesList -> {
            if (customerEntitiesList != null) {
                //Array
                ArrayList<String> customerNames = new ArrayList<String>();
                customerEntities = new ArrayList<>();
                for (CustomerEntity u : customerEntitiesList) {
                    Log.d("ModifyCustomer", u.getFirstname());
                    customerNames.add(u.getFirstname() + " " + u.getLastname());
                    customerEntities.add(u);
                }
                adapterCustomerList.updateData(new ArrayList<>(customerNames));
            }
        });
    }

    //Method to delete a checkpoint
    private void deleteCustomer(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete Customer");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are your sure you want to delete this Customer");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            viewModelCustomer.deleteCustomer(customer, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d("MODIFYCUTOMERS","Customer successfully deleted.");
                    Toast toast = Toast.makeText(ModifyCustomerActivity.this, "Customer deleted!", Toast.LENGTH_LONG);
                    toast.show();
                    customerFirstname.setText(null);
                    customerLastname.setText(null);
                    customerAddress.setText(null);
                    customerEmail.setText(null);
                    customerPhone.setText(null);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MODIFYCUTOMERS","Product delete error." + e.getStackTrace());
                }
            });

        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    //Verify the UserInputs and create or Update a Checkpoint
    private void verifyUserInputs(){
        firstname = customerFirstname.getText().toString();
        lastname = customerLastname.getText().toString();
        address = customerAddress.getText().toString();
        phone = customerPhone.getText().toString();
        email = customerEmail.getText().toString();
        if(viewModelCustomer==null){
            CustomerViewModel.Factory factory = new CustomerViewModel.Factory(getApplication(),"");
            viewModelCustomer = ViewModelProviders.of(ModifyCustomerActivity.this, factory).get(CustomerViewModel.class);
        }
        if(!isAnyEditEmpty()){
            if(mode.equals("add")){
                CustomerEntity newCustomer = new CustomerEntity(firstname, lastname, email, phone, address);
                viewModelCustomer.createCustomer(newCustomer, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyCustomerActivity.this, "Created a new Customer", Toast.LENGTH_LONG);
                        toast.show();
                        customerFirstname.setText(null);
                        customerLastname.setText(null);
                        customerAddress.setText(null);
                        customerEmail.setText(null);
                        customerPhone.setText(null);
                    }
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
            if(mode.equals("modify")){
                if(firstname.length()>1)
                    customer.setFirstname(firstname);
                if(lastname.length()>1)
                    customer.setLastname(lastname);
                if(address.length()>1)
                    customer.setAddress(address);
                if(phone.length()>1)
                    customer.setPhone(phone);
                if(email.length()>1)
                    customer.setEmail(email);

                viewModelCustomer.updateCustomer(customer, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyCustomerActivity.this, "Updated Customer", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        }
    }

    private boolean isAnyEditEmpty() {
        if (customerFirstname.length() < 1) {
            customerFirstname.setError("You must enter firstname !");
            return true;
        }
        if (customerLastname.length() < 1) {
            customerLastname.setError("You must enter lastname !");
            return true;
        }
        if (customerAddress.length() < 1) {
            customerAddress.setError("You must enter a address for the customer !");
            return true;
        }
        if (customerEmail.length() < 1) {
            customerEmail.setError("You must enter a email !");
            return true;
        }
        if (customerPhone.length() < 1) {
            customerPhone.setError("You must enter a phone number !");
            return true;
        }
        return false;
    }
}
