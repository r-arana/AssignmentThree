package controller;

import interfaces.BinaryTreeInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import model.Restaurant;
import structure.BinarySearchTree;
import view.IndividualRestaurantJavaFXView;
import view.RestaurantListJavaFXView;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by REA on 7/4/2017.  The part with lambda expressions is something I need to understand better.
 * The videos provided were helpful in understanding the logic.
 */


public class RestaurantListController {

    @FXML
    private TableView<Restaurant> restaurantTable;
    @FXML
    private TableColumn<Restaurant, String> nameColumn;
    @FXML
    private TableColumn<Restaurant, String> streetAddressColumn;
    @FXML
    private TableColumn<Restaurant, String> cityColumn;
    @FXML
    private TableColumn<Restaurant, String> stateColumn;
    @FXML
    private TableColumn<Restaurant, String> zipCodeColumn;
    @FXML
    private TableColumn<Restaurant, String> latitudeColumn;
    @FXML
    private TableColumn<Restaurant, String> longitudeColumn;
    @FXML
    private TableColumn<Restaurant, String> phoneNumberColumn;
    @FXML
    private TableColumn<Restaurant, String> photoColumn;
    @FXML
    private TextField searchBar;
    @FXML
    private Label errorLabel;
    @FXML
    private Label noMatchesLabel;
    @FXML
    private Button resetButton;
    @FXML
    private ImageView restaurantImageView;

    private enum Search {COORDINATES, PHONE_NUMBER, NAME}

    private RestaurantListJavaFXView restaurantApp;
    ObservableList<Restaurant> searchResults = FXCollections.observableArrayList();

    public void initialize(){
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        streetAddressColumn.setCellValueFactory(cellData -> cellData.getValue().streetAddressProperty());
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        zipCodeColumn.setCellValueFactory(cellData -> cellData.getValue().zipCodeProperty());
        latitudeColumn.setCellValueFactory(cellData -> cellData.getValue().latitudeProperty());
        longitudeColumn.setCellValueFactory(cellData -> cellData.getValue().longitudeProperty());
        phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        photoColumn.setCellValueFactory(cellData -> cellData.getValue().photoProperty());

        restaurantTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && restaurantTable.getSelectionModel().getSelectedItem() != null){
                try{
                    System.out.println("Attempting to open IndividualRestaurantJavaFXView");
                    new IndividualRestaurantJavaFXView(restaurantTable.getSelectionModel().getSelectedItem());
                }
                catch (IOException e){
                    System.err.println("Error opening IndividualRestaurantJavaFXView");
                }
            }
        });

    }

    public void setRestaurantApp(RestaurantListJavaFXView restaurantApp){
        this.restaurantApp = restaurantApp;

        // Set our observable list to the table
        restaurantTable.setItems(restaurantApp.getRestaurantData());
    }

    public void resetButtonPressed(){
        restaurantTable.setItems(restaurantApp.getRestaurantData());
    }

    public void handleEnterPressed(){
        String latitude = "";
        String longitude = "";
        String key = "";
        String[] container;

        errorLabel.setVisible(false);
        noMatchesLabel.setVisible(false);

        key = searchBar.getText().trim();
        // If the key is not null and not empty
        if (key != null && (!key.isEmpty())){


            if (key.indexOf(",") != -1) {
                container = key.split(",");
                // Need to make sure that we actually have 2 elements in our container, or we'll be accessing a
                // null pointer.
                if (container.length == 2 && containsCoordinates(container[0].trim(), container[1].trim())) {
                    // If they're actually coordinates, and not some random input with a comma.

                    latitude = container[0].trim();
                    longitude = container[1].trim();
                    System.out.println(searchBar.getText());

                    Restaurant restaurantKey = new Restaurant(latitude, longitude);
                    search(Search.COORDINATES, restaurantKey);

                } else {
                    System.out.println("Improper coordinates.");
                    errorLabel.setVisible(true);
                }
            }
            // Handle phone numbers
            // This is more restricting than checking for names, so it should be done first.
            else if (containsPhoneNumber(key)){
                Restaurant searchKey = new Restaurant();
                searchKey.setPhoneNumber(key);
                search(Search.PHONE_NUMBER, searchKey);
            }
            // Handle names
            else if (mightContainName(key)){
                Restaurant searchKey = new Restaurant();
                searchKey.setName(key);
                search(Search.NAME, searchKey);
            }
            else{
                errorLabel.setVisible(true);
            }
        }
        else{
            errorLabel.setVisible(true);
        }
    }

    private void search(Search searchType, Restaurant searchKey){
        boolean found = false;
        searchResults.clear();
        Restaurant newKey;
        BinarySearchTree<Restaurant> tree = restaurantApp.getBinarySearchTree();

        if (searchType.equals(Search.COORDINATES) && restaurantApp.getBinarySearchTree().contains(searchKey)) {
            System.out.println("Searching for coordinates.");
            searchResults.add(restaurantApp.getBinarySearchTree().get(searchKey));
            found = true;
        }
        else if (searchType.equals(Search.NAME)){
            System.out.println("Searching for a name.");
            int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

            while (numberOfElements > 0){
                newKey = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);

                if (newKey.compareNames(searchKey) == 0){

                    // We add our search result to our new observable list
                    searchResults.add(restaurantApp.getBinarySearchTree().get(newKey));

                    found = true;
                }
                numberOfElements--;
            }
        }
        else if (searchType.equals(Search.PHONE_NUMBER)){

            System.out.println("Searching for a phone number.");
            int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

            while (numberOfElements > 0){
                newKey = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);

                if (newKey.comparePhoneNumbers(searchKey) == 0){

                    // We add our search result to our new observable list
                    searchResults.add(restaurantApp.getBinarySearchTree().get(newKey));

                    found = true;
                }
                numberOfElements--;
            }
        }

        if (found){
            System.out.println("Found it.");

            // Update the information displayed
            restaurantTable.setItems(searchResults);
        }
        else {
            System.out.println("Didn't find it.");
            noMatchesLabel.setVisible(true);
        }
    }
    //http://www.zparacha.com/phone_number_javascript_regex/
    private boolean containsPhoneNumber(String potentialPhoneNumber){
        return Pattern.matches("^\\(?[0-9]{3}\\)? ?[0-9]{3}-? ?[0-9]{4}$", potentialPhoneNumber);
    }

    private boolean mightContainName(String potentialName){
        return (potentialName.length() > 2);
        //return Pattern.matches("^[a-zA-Z0-9\\W]++$", potentialName);
    }

    // We're just checking if the text they enter matches the typical format for coordinates
    private boolean containsCoordinates(String coordinate1, String coordinate2){
        return Pattern.matches("^-?[0-9]+\\.-?[0-9]+$", coordinate1) &&
               Pattern.matches("^-?[0-9]+\\.-?[0-9]+$", coordinate2);
    }

    public TableView<Restaurant> getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(TableView<Restaurant> restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

}
