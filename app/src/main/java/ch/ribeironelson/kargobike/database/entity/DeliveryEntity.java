package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryEntity implements Serializable {
    String idDelivery;
    String description;
    long nbPackages;
    boolean loaded = false;
    String deliveryDate;
    String departurePlace;
    String finalDestination;
    String clientName;
    String signatureImageName;
    String proofPictureName;
    String idProduct;
    String deliveryTime;
    CheckpointEntity nextPlaceToGo;
    String insertionTime;
    String actuallyAssignedUser;
    List<TripEntity> checkpoints;

    public DeliveryEntity(String idUser,String clientName,String insertionTime,String deliveryTime , String idDelivery, String description, long nbPackages, String deliveryDate, String departurePlace, String finalDestination, String signatureImageName, String proofPictureName, String idProduct, List<TripEntity> checkpoints) {
        this.idDelivery = idDelivery;
        this.description = description;
        this.nbPackages = nbPackages;
        this.deliveryDate = deliveryDate;
        this.departurePlace = departurePlace;
        this.finalDestination = finalDestination;
        this.signatureImageName = signatureImageName;
        this.proofPictureName = proofPictureName;
        nextPlaceToGo = new CheckpointEntity("noID", departurePlace);
        this.clientName = clientName;
        this.idProduct = idProduct;
        this.deliveryTime = deliveryTime;
        actuallyAssignedUser = idUser;
        this.insertionTime = insertionTime;
        this.checkpoints = checkpoints;
    }

    public DeliveryEntity(String idUser,String clientName,String insertionTime,String deliveryTime, String description, long nbPackages, String deliveryDate, String departurePlace, String finalDestination, String signatureImageName, String proofPictureName, String idProduct, List<TripEntity> checkpoints) {
        this.description = description;
        this.clientName = clientName;
        this.insertionTime = insertionTime;
        this.nbPackages = nbPackages;
        this.deliveryTime = deliveryTime;
        this.deliveryDate = deliveryDate;
        this.departurePlace = departurePlace;
        this.finalDestination = finalDestination;
        this.signatureImageName = signatureImageName;
        this.proofPictureName = proofPictureName;
        nextPlaceToGo = new CheckpointEntity("noID", departurePlace);
        this.idProduct = idProduct;
        actuallyAssignedUser = idUser;
        this.checkpoints = checkpoints;
    }

    public String getInsertionTime() {
        return insertionTime;
    }

    public void setInsertionTime(String insertionTime) {
        this.insertionTime = insertionTime;
    }

    public String getActuallyAssignedUser() {
        return actuallyAssignedUser;
    }

    public void setActuallyAssignedUser(String actuallyAssignedUser) {
        this.actuallyAssignedUser = actuallyAssignedUser;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean isLoaded) {
        this.loaded = isLoaded;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public DeliveryEntity(){}

    public List<TripEntity> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<TripEntity> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public CheckpointEntity getNextPlaceToGo() {
        return nextPlaceToGo;
    }

    public void setNextPlaceToGo(CheckpointEntity nextPlaceToGo) {
        this.nextPlaceToGo = nextPlaceToGo;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(String idDelivery) {
        this.idDelivery = idDelivery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getNbPackages() {
        return nbPackages;
    }

    public void setNbPackages(long nbPackages) {
        this.nbPackages = nbPackages;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getSignatureImageName() {
        return signatureImageName;
    }

    public void setSignatureImageName(String signatureImageName) {
        this.signatureImageName = signatureImageName;
    }

    public String getProofPictureName() {
        return proofPictureName;
    }

    public void setProofPictureName(String proofPictureName) {
        this.proofPictureName = proofPictureName;
    }

    public void addCheckpoint(TripEntity trip){
        if(checkpoints==null)
            checkpoints = new ArrayList<>();

        checkpoints.add(trip);
    }

    @Override
    public String toString() {
        return description + deliveryDate +departurePlace + finalDestination;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idDelivery", idDelivery);
        result.put("actuallyAssignedUser", actuallyAssignedUser);
        result.put("clientName",clientName);
        result.put("loaded", loaded);
        result.put("insertionTime",insertionTime);
        result.put("description", description);
        result.put("nbPackages", nbPackages);
        result.put("nextPlaceToGo",nextPlaceToGo);
        result.put("deliveryDate", deliveryDate);
        result.put("deliveryTime", deliveryTime);
        result.put("departurePlace", departurePlace);
        result.put("finalDestination", finalDestination);
        result.put("signatureImageName", signatureImageName);
        result.put("proofPictureName", proofPictureName);
        result.put("idProduct", idProduct);
        result.put("checkpoints", checkpoints);

        return result;
    }
}
