package ch.ribeironelson.kargobike.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class DeliveryEntity {
    String idUser;
    String idDelivery;
    String description;
    Long nbPackages;
    String deliveryDateTime;
    String departurePlace;
    String finalDestination;
    String signatureImageName;
    String proofPictureName;

    public DeliveryEntity(){}

    public DeliveryEntity(String idUser, String idDelivery, String description, long nbPackages, String deliveryDateTime, String departurePlace, String finalDestination, String signatureImageName, String proofPictureName) {
        this.idUser = idUser;
        this.idDelivery = idDelivery;
        this.description = description;
        this.nbPackages = nbPackages;
        this.deliveryDateTime = deliveryDateTime;
        this.departurePlace = departurePlace;
        this.finalDestination = finalDestination;
        this.signatureImageName = signatureImageName;
        this.proofPictureName = proofPictureName;
    }

    public String getIdUser() { return idUser; }

    public void setIdUser(String idUser) { this.idUser = idUser; }

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

    public Long getNbPackages() {
        return nbPackages;
    }

    public void setNbPackages(Long nbPackages) {
        this.nbPackages = nbPackages;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
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

    @Override
    public String toString() {
        return description + deliveryDateTime+departurePlace + finalDestination;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idDelivery", idDelivery);
        result.put("idUser", idUser);
        result.put("description", description);
        result.put("nbPackages", nbPackages);
        result.put("deliveryDateTime", deliveryDateTime);
        result.put("departurePlace", departurePlace);
        result.put("finalDestination", finalDestination);
        result.put("signatureImageName", signatureImageName);
        result.put("proofPictureName", proofPictureName);

        return result;
    }
}
