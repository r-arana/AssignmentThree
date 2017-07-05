package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * Created by REA on 7/2/2017.
 *
 * This was redone using the formatting advice from http://docs.oracle.com/javafx/2/fxml_get_started/fxml_tutorial_intermediate.htm
 * to use the SimpleStringProperty instead of String
 */
public class Restaurant implements Comparable<Restaurant>, Serializable{

    private SimpleStringProperty name;
    private SimpleStringProperty streetAddress;
    private SimpleStringProperty city;
    private SimpleStringProperty state;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty latitude;
    private SimpleStringProperty longitude;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty photo;

    public Restaurant(){
        this.name = new SimpleStringProperty("");
        this.streetAddress = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.zipCode = new SimpleStringProperty("");
        this.latitude = new SimpleStringProperty("");
        this.longitude = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.photo = new SimpleStringProperty("");
    }

    public Restaurant(String name, String streetAddress, String city, String state, String zipCode, String latitude, String longitude, String phoneNumber, String photo) {
        this.name = new SimpleStringProperty(name);
        this.streetAddress = new SimpleStringProperty(streetAddress);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.latitude = new SimpleStringProperty(latitude);
        this.longitude = new SimpleStringProperty(longitude);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.photo = new SimpleStringProperty(photo);
    }

    // Used for searching through our BST
    public Restaurant(String latitude, String longitude){
        this.name = new SimpleStringProperty("");
        this.streetAddress = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.zipCode = new SimpleStringProperty("");
        this.latitude = new SimpleStringProperty(latitude);
        this.longitude = new SimpleStringProperty(longitude);
        this.phoneNumber = new SimpleStringProperty("");
        this.photo = new SimpleStringProperty("");
    }

    public String getName() {
        return name.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty nameProperty(){
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStreetAddress() {
        return streetAddress.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty streetAddressProperty(){
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress.set(streetAddress);
    }

    public String getCity() {
        return city.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty cityProperty(){
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getState() {
        return state.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty stateProperty(){
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty zipCodeProperty(){
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public String getLatitude() {
        return latitude.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty latitudeProperty(){
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude.set(latitude);
    }

    public String getLongitude() {
        return longitude.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty longitudeProperty(){
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude.set(longitude);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty phoneNumberProperty(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getPhoto() {
        return photo.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty photoProperty(){
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo.set(photo);
    }

    public String toString(){
        return "Restaurant{" +
                "Name: " + getName() + "/ " +
                "Address: " + getStreetAddress() + "/ " + getCity() + ", " + getState() + ", " +  getZipCode() + "/ " +
                "Latitude: " + getLatitude() + "/ " +
                "Longitude: " + getLongitude() + "/ " +
                "Phone Number: " + getPhoneNumber() + "/ " +
                "Image: " + getPhoto() + "}" +
                "\n";
    }


    // Taken from your video https://www.youtube.com/watch?v=IewSoYs68bk at about 7 minutes
    // An explanation of the ?: operator comes from http://www.terminally-incoherent.com/blog/2006/04/21/java-operator/
    // (boolean expression) ? (Evaluate and return if true): (Evaluate and return if false)

    @Override
    public int compareTo(Restaurant o) {
        int compare = this.latitude.get().compareTo(o.getLatitude());
        // If our latitude values are equivalent then we move on to comparing the longitude of
        // our coordinates.  Otherwise, we can just return our initial comparison.
        return (compare == 0) ? this.longitude.get().compareTo(o.getLongitude()) : compare;
    }
}
