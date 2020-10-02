package nl.inholland;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private TextField description;
    private ObservableList<Item> toDoList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {

        window.setHeight(500);
        window.setMinWidth(300);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        List<Item> items = new ArrayList<>();
        items.add(new Item("Feed the cat", false));
        toDoList = FXCollections.observableArrayList(items);

        MenuBar bar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem loadItem = new MenuItem("Load...");
        loadItem.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        List<Item> loadedItems = readItems();
                        toDoList.clear();
                        toDoList.addAll(loadedItems);
                    }
                });
        MenuItem saveItem = new MenuItem("Save...");
        saveItem.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        writeItems(toDoList);
                    }
                });
        fileMenu.getItems().addAll(loadItem, saveItem);
        bar.getMenus().add(fileMenu);

        TableView<Item> todos = new TableView<>();
        TableColumn<Item, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Item, Boolean> isCompleteColumn = new TableColumn<>("Complete");
        isCompleteColumn.setCellValueFactory(new PropertyValueFactory<>("complete"));
        isCompleteColumn.setCellFactory(isc ->
                new TableCell<Item, Boolean>() {
                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty ? null :
                                item.booleanValue() ? "Done" : " Todo");
                    }
                });


        todos.getColumns().addAll(descriptionColumn, isCompleteColumn);

        todos.setItems(toDoList);

        description = new TextField();
        description.setPromptText("Description");
        Button addButton = new Button("Add");
        addButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        String descriptionInput = description.getText();
                        Item item = new Item(descriptionInput, false);
                        toDoList.add(item);
                        description.clear();
                    }
                });

        Button uselessButton = new Button("Useless");
        uselessButton.getStyleClass().add("useless");
        todos.setRowFactory(
                new Callback<TableView<Item>, TableRow<Item>>() {
                    @Override
                    public TableRow<Item> call(TableView<Item> itemTableView) {
                        TableRow<Item> row = new TableRow<>();
                        row.setOnMouseClicked(
                                new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent mouseEvent) {
                                        if (mouseEvent.getClickCount() == 2 && !row.isEmpty()) {
                                            Item item = row.getItem();
                                            item.setComplete(!item.isComplete());
                                            todos.refresh();
                                        }
                                    }
                                });
                        return row;
                    }
                });

        HBox form = new HBox(10);
        form.getChildren().addAll(description, addButton, uselessButton);
        layout.getChildren().addAll(bar, todos, form);


        uselessButton.getStyleClass().setAll("btn", "btn-primary");
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
    }

    private List<Item> readItems() {
        List<Item> readItems = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("src/resources/inputfiles/items.dat");
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            while (true) {
                try {
                    Item item = (Item) ois.readObject();
                    readItems.add(item);
                } catch (EOFException eofe) {
                    break;
                }
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return readItems;
    }

    private void writeItems(ObservableList<Item> toDoList) {
        try (FileOutputStream fos = new FileOutputStream(new File("output/items.dat"));

             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            for (Item item : toDoList) {
                oos.writeObject(item);
            }
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
