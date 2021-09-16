package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import sample.Model.Stroke;

import java.io.*;
import java.util.*;

public class Controller {
    @FXML
    private ListView<Stroke> listView;
    @FXML
    private ComboBox<String> comboBox;
    private ObservableList<Stroke> strokes;
    private final ObservableList<String> typeNames = FXCollections.observableArrayList("Héng (横)","Shù (竖)","Piě (撇)","Nà (捺)");
    private final String[] types = {"H","S","P","N"};
    private final double[][] typeDefaultCoords = {{10.0,10.0,20.0,10.0},{10.0,10.0,10.0,20.0},{20.0,10.0,10.0,20.0},{10.0,10.0,20.0,20.0}};

    public void initialise() {
        strokes = FXCollections.observableArrayList();
        listView.setItems(strokes);
        comboBox.setItems(typeNames);
    }

    public void importCSV(ActionEvent exception) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open file");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV files", "*.csv"), new FileChooser.ExtensionFilter("All files", "*"));
            File selectedFile = chooser.showOpenDialog(((Button) exception.getSource()).getScene().getWindow());
            strokes.removeAll();
            if (selectedFile != null) {
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                String s;
                do {
                    s = br.readLine();
                    if (s != null) {
                        String[] tokens = s.split(",");
                        ArrayList<Double> coords = new ArrayList<>();
                        for (int i=1;i<tokens.length;i++) coords.add(Double.parseDouble(tokens[i]));
                        strokes.add(new Stroke(tokens[0],coords));
                    }
                } while (s != null);
            }
        } catch (Exception actionEvent) {
            alertError(actionEvent, "Invalid file!");
        }
    }

    public void exportCSV(ActionEvent e) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save file");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV files", "*.csv"), new FileChooser.ExtensionFilter("All files", "*"));
            File selectedFile = chooser.showSaveDialog(((Button) e.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                PrintWriter pw = new PrintWriter(new FileWriter(selectedFile, false));
                for (Stroke i: strokes) {
                    pw.print(i.getType());
                    for (double j: i.getCoords()) {
                        pw.print(","+j);
                    } pw.println();
                }
                pw.close();
            }
        } catch (Exception actionEvent) {
            alertError(actionEvent);
        }
    }

    public void listViewUp(ActionEvent e) {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            Stroke selected = listView.getSelectionModel().getSelectedItem();
            strokes.remove(index);
            strokes.add(index - 1, selected);
            listView.getSelectionModel().select(index-1);
        }
    }

    public void listViewDown(ActionEvent e) {
        Stroke selected = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < strokes.size()-1) {
            strokes.remove(index);
            strokes.add(index + 1, selected);
            listView.getSelectionModel().select(index+1);
        }
    }

    public void newStroke(ActionEvent e) {
        int index = comboBox.getSelectionModel().getSelectedIndex();
        strokes.add(new Stroke(types[index],typeDefaultCoords[index]));
    }

    public void alertError(Exception e) {
        alertError(e,"Error!");
    }

    public void alertError(Exception e, String message) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(e.getClass().getSimpleName());
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public void alertWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText("Error!");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void alertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alert");
        alert.setHeaderText("Notification");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
