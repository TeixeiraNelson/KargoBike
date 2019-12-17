package ch.ribeironelson.kargobike.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ch.ribeironelson.kargobike.database.entity.ProductEntity;
import ch.ribeironelson.kargobike.database.entity.SchedulesEntity;
import ch.ribeironelson.kargobike.database.repository.ProductRepository;
import ch.ribeironelson.kargobike.util.OnAsyncEventListener;

public class ProductListViewModel extends AndroidViewModel {
    private ProductRepository repository;

    private Application application;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ProductEntity>> observableProducts;
    private ProductEntity product;
    String idProduct;

    public ProductListViewModel(@NonNull Application application,
                                ProductRepository repository) {
        super(application);

        this.repository = repository;
        this.application = application;

        observableProducts = new MediatorLiveData<>();


        // set by default null, until we get data from the database.
        observableProducts.setValue(null);

        LiveData<List<ProductEntity>> products = repository.getAllProducts();

        // observe the changes of the entities from the database and forward them
        observableProducts.addSource(products, observableProducts::setValue);
    }

    public ProductListViewModel(@NonNull Application application,
                                  ProductRepository repository, String idProduct) {
        super(application);

        this.repository = repository;
        this.application = application;
        this.idProduct=idProduct;

        observableProducts = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableProducts.setValue(null);

        LiveData<List<ProductEntity>> products = repository.getAllProducts();

        // observe the changes of the entities from the database and forward them
        observableProducts.addSource(products, observableProducts::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final ProductRepository repository;

        private String idProduct ="";


        public Factory(@NonNull Application application, String idProduct) {
            this.application = application;
            repository = ProductRepository.getInstance();
            this.idProduct=idProduct;
        }
        public Factory(@NonNull Application application) {
            this.application = application;
            repository = ProductRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            if (idProduct.equals("")){
                return (T) new ProductListViewModel(application, repository);
            } else {
                return (T) new ProductListViewModel(application, repository, idProduct);
            }

        }
    }

    /**
     * Expose the LiveData ClientEntities query so the UI can observe it.
     */
    public LiveData<List<ProductEntity>> getProducts() {
        return observableProducts;
    }

    public ProductEntity getProduct(){
        return product;
    }
}
