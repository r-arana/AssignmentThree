package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Restaurant;
import structure.BinarySearchTree;
import view.RestaurantListJavaFXView;

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

        searchResults.clear();

        errorLabel.setVisible(false);
        noMatchesLabel.setVisible(false);

        key = searchBar.getText().trim();
        // If the key is not null, not empty, and it contains a comma.
        if (key != null && (!key.isEmpty()) && key.indexOf(",") != -1){

            container = key.split(",");
            // Need to make sure that we actually have 2 elements in our container, or we'll be accessing a
            // null pointer.
            if (container.length == 2 && containsCoordinates(container[0].trim(), container[1].trim())){
            // If they're actually coordinates, and not some random input with a comma.

                latitude = container[0].trim();
                longitude = container[1].trim();
                System.out.println(searchBar.getText());

                Restaurant restaurantKey = new Restaurant(latitude, longitude);

                if (restaurantApp.getBinarySearchTree().contains(restaurantKey)){
                    System.out.println("Found it");

                    // We add our search result to our new observable list
                    searchResults.add(restaurantApp.getBinarySearchTree().get(restaurantKey));

                    // Update the information displayed
                    restaurantTable.setItems(searchResults);

                }
                else{
                    System.out.println("Didn't find it.");
                    noMatchesLabel.setVisible(true);
                }
            }
            else{
                System.out.println("Improper coordinates.");
                errorLabel.setVisible(true);
            }
        }
        else{
            errorLabel.setVisible(true);
        }
    }

    // We're just checking if the text they enter matches the typical format for coordinates
    private boolean containsCoordinates(String coordinate1, String coordinate2){
        return Pattern.matches("^-?[0-9]+\\.-?[0-9]+$", coordinate1) &&
               Pattern.matches("^-?[0-9]+\\.-?[0-9]+$", coordinate2);
    }
}
