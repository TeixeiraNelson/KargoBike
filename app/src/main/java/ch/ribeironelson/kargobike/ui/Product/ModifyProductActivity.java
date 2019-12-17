package ch.ribeironelson.kargobike.ui.Product;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import ch.ribeironelson.kargobike.R;
import ch.ribeironelson.kargobike.adapter.ListAdapter;
import ch.ribeironelson.kargobike.database.entity.CheckpointEntity;
import ch.ribeironelson.kargobike.database.entity.ProductEntity;
import ch.ribeironelson.kargobike.database.entity.WorkingZoneEntity;
import ch.ribeironelson.kargobike.ui.BaseActivity;
import ch.ribeironelson.kargobike.ui.Checkpoint.ModifyCheckpointsActivity;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;
import ch.ribeironelson.kargobike.viewmodel.CheckpointListViewModel;
import ch.ribeironelson.kargobike.viewmodel.CheckpointViewModel;
import ch.ribeironelson.kargobike.viewmodel.ProductListViewModel;
import ch.ribeironelson.kargobike.viewmodel.ProductViewModel;
import ch.ribeironelson.kargobike.viewmodel.WorkingZoneListViewModel;

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

public class ModifyProductActivity extends BaseActivity {
    private static final String TAG = "ModifyProductActivity";
    private String mode = "add";

    private TextView description;
    private EditText productName;
    private EditText productPrice;
    private Button btnSaveProduct;
    private Button btnDeleteProduct;
    private Spinner spinnerProducts;
    FloatingActionButton fab;

    private String productname;
    private String productprice;
    private ProductEntity product;

    private ListAdapter<String> adapterProductList;

    private ProductViewModel viewModelProduct;
    private ProductListViewModel viewModelProductList;

    private List<ProductEntity> productEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_modify_product, frameLayout);
        navigationView.setCheckedItem(R.id.nav_products);

        mode = getIntent().getExtras().getString("mode","add");
        fab = findViewById(R.id.floatingActionButton);
        description = findViewById(R.id.textProductDescription);
        productName = findViewById(R.id.ProductNameData);
        productPrice = findViewById(R.id.ProductPriceData);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        spinnerProducts = findViewById(R.id.spinner_products);
        btnSaveProduct.setOnClickListener(v -> verifyUserInputs());
        btnDeleteProduct.setOnClickListener(v -> deleteProduct());

        adapterProductList = new ListAdapter<>(ModifyProductActivity.this, R.layout.row_list, new ArrayList<>());
        spinnerProducts.setAdapter(adapterProductList);

        initComponents();

    }

    private void startEditingMode(){
        Intent intent = new Intent(ModifyProductActivity.this, ModifyProductActivity.class);
        intent.putExtra("mode", "modify");
        startActivity(intent);
        this.finish();
    }

    private void startAddMode(){
        Intent intent = new Intent(ModifyProductActivity.this, ModifyProductActivity.class);
        intent.putExtra("mode", "add");
        startActivity(intent);
        this.finish();
    }

    //init the Components and create the viewModel
    public void initComponents(){

        if(mode.equals("add")){
            description.setText("Add a new Product");
            btnDeleteProduct.setVisibility(View.INVISIBLE);
            spinnerProducts.setVisibility(View.INVISIBLE);
            btnSaveProduct.setEnabled(true);
            productName.setEnabled(true);
            productPrice.setEnabled(true);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startEditingMode();
                }
            });

        }
        if(mode.equals("modify")) {
            description.setText("Modify a Product");
            btnDeleteProduct.setVisibility(View.VISIBLE);
            spinnerProducts.setVisibility(View.VISIBLE);
            FloatingActionButton fab = findViewById(R.id.floatingActionButton);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_circle_black_24dp));
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
        spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product = productEntities.get(position);
                ProductViewModel.Factory factory = new ProductViewModel.Factory(getApplication(),product.getIdProduct());
                viewModelProduct = ViewModelProviders.of(ModifyProductActivity.this, factory).get(ProductViewModel.class);
                if(mode.equals("modify")){
                    productName.setText(product.getName());
                    productPrice.setText(Long.toString(product.getPrice()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillSpinners() {
        ProductListViewModel.Factory factory2 = new ProductListViewModel.Factory(getApplication());
        viewModelProductList = ViewModelProviders.of(this, factory2).get(ProductListViewModel.class);

        viewModelProductList.getProducts().observe(this, productEntitiesList -> {
            if (productEntitiesList != null) {
                //Array
                ArrayList<String> productNames = new ArrayList<String>();
                productEntities = new ArrayList<>();
                for (ProductEntity u : productEntitiesList) {
                    Log.d("ModifyProduct", u.getName());
                    productNames.add(u.getName() + " - " + Long.toString(u.getPrice()));
                    productEntities.add(u);
                }
                adapterProductList.updateData(new ArrayList<>(productNames));
            }
        });
    }

    //Method to delete a checkpoint
    private void deleteProduct(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Delete Product");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Are your sure you want to delete this Product");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
            viewModelProduct.deleteProduct(product, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d("MODIFYPRODUCTS","Product successfully deleted.");
                    Toast toast = Toast.makeText(ModifyProductActivity.this, "Product deleted!", Toast.LENGTH_LONG);
                    toast.show();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d("MODIFYPRODUCTS","Product delete error." + e.getStackTrace());
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
        productname = productName.getText().toString();
        productprice = productPrice.getText().toString();
        if(viewModelProduct==null){
            ProductViewModel.Factory factory = new ProductViewModel.Factory(getApplication(),"");
            viewModelProduct = ViewModelProviders.of(ModifyProductActivity.this, factory).get(ProductViewModel.class);
        }
        if(!isAnyEditEmpty()){
            if(mode.equals("add")){
                ProductEntity newProduct = new ProductEntity(productname, Long.valueOf(productprice));
                viewModelProduct.createProduct(newProduct, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyProductActivity.this, "Created a new Product", Toast.LENGTH_LONG);
                        toast.show();
                        productName.setText(null);
                        productPrice.setText(null);
                    }
                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
            if(mode.equals("modify")){
                if(productname.length()>1)
                    product.setName(productname);
                if(productPrice.length()>1)
                    product.setPrice(Long.valueOf(productname));

                viewModelProduct.updateProduct(product, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(ModifyProductActivity.this, "Updated Product", Toast.LENGTH_LONG);
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
        if (productName.length() < 1) {
            productName.setError("You must enter a product name !");
            return true;
        }
        if (productPrice.length() < 1) {
            productPrice.setError("You must enter a product price !");
            return true;
        }
        return false;
    }
}
