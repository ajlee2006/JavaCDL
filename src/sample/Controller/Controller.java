package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import sample.Model.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.*;

public class Controller {
    @FXML
    private ListView<Stroke> listView;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private CheckBox gridlines, simple, hook, visible;
    @FXML
    private Canvas canvas;
    @FXML
    private Label mousePos;
    private GraphicsContext gc;
    private ObservableList<Stroke> strokes;
    private Point selectedPoint;
    private int playSelectedStrokeIndex;
    private double oldMouseX, oldMouseY;
    private final ObservableList<String> typeNames = FXCollections.observableArrayList("Héng (横)","Shù (竖)","Piě (撇)","Nà (捺)","Diǎn (点)","Gōu (钩)","Stroke");
    private final String[] types = {"H","S","P","N","D","G","X"};
    private final double[][] typeDefaultCoords = {{10.0,10.0,100.0,10.0},{10.0,10.0,10.0,100.0},{100.0,10.0,10.0,100.0},{10.0,10.0,100.0,100.0},{50.0,50.0,100.0,100.0},{100.0,100.0,50.0,50.0},{10.0,10.0,100.0,10.0}};

    public void initialise() {
        strokes = FXCollections.observableArrayList();
        listView.setItems(strokes);
        comboBox.setItems(typeNames);
        canvas.setWidth((int) canvas.getWidth());
        canvas.setHeight((int) canvas.getHeight());
        gc = canvas.getGraphicsContext2D();
        gridlines.setSelected(true);
        drawBackground();
    }

    public void drawBackground() {
        gc.setStroke(Color.LIGHTGREEN);
        gc.setLineWidth(10);
        gc.strokeLine(0,0,0,canvas.getHeight());
        gc.strokeLine(canvas.getWidth(),0,canvas.getWidth(),canvas.getHeight());
        gc.strokeLine(0,0,canvas.getWidth(),0);
        gc.strokeLine(0,canvas.getHeight(),canvas.getWidth(),canvas.getHeight());
        gc.setLineDashes(7);
        gc.setLineWidth(2);
        gc.strokeLine(0,0,canvas.getWidth(),canvas.getHeight());
        gc.strokeLine(0,canvas.getHeight(),canvas.getWidth(),0);
        gc.strokeLine(canvas.getWidth()/2,0,canvas.getWidth()/2,canvas.getHeight());
        gc.strokeLine(0,canvas.getHeight()/2,canvas.getWidth(),canvas.getHeight()/2);
        gc.setLineDashes();
    }

    public void draw(boolean background) {
        gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
        if (background) drawBackground();
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.BLACK);
        if (simple.isSelected()) {
            gc.setLineWidth(5);
            for (Stroke i: strokes) if (i.isVisible()) i.drawSimple(gc);
        } else {
            for (Stroke i: strokes) if (i.isVisible()) i.draw(gc);
        }

    }

    public void draw() {
        draw(gridlines.isSelected());
    }

    @FXML
    public void play(ActionEvent e) {
        for (int i=playSelectedStrokeIndex = 0;i<strokes.size();i++) {
            strokes.get(i).setExtent(0);
        }
        startTask();
    }

    public void startTask() {
        Runnable task = this::runTask;
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    public void runTask() {
        for (int i=playSelectedStrokeIndex; i<strokes.size(); i++) {
            playSelectedStrokeIndex = i;
            double r = Math.round(strokes.get(i).getExtent()*50)/50.0;
            while (r < 1) {
                strokes.get(i).setExtent(r+0.02);
                r = Math.round(strokes.get(i).getExtent()*50)/50.0;
                draw();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        playSelectedStrokeIndex = 0;
    }

    @FXML
    public void stop(ActionEvent e) {
        for (Stroke i: strokes) {
            i.setExtent(1);
        }
        playSelectedStrokeIndex = 0;
    }

    @FXML
    public void checkBoxOnAction(ActionEvent e) {
        try {
            Stroke selected = listView.getSelectionModel().getSelectedItem();
            if (selected instanceof Heng) ((Heng) selected).setHook(hook.isSelected());
            selected.setVisible(visible.isSelected());
        } catch (Exception actionEvent) {
            hook.setSelected(false);
            visible.setSelected(true);
        }
        draw();
    }

    @FXML
    public void canvasOnClick(MouseEvent e) {
        for (Stroke i: strokes) {
            for (Point j: i.getPoints()) {
                if ((j.getX()-e.getX())*(j.getX()-e.getX())+(j.getY()-e.getY())*(j.getY()-e.getY()) <= 30) {
                    selectedPoint = j;
                    listView.getSelectionModel().select(i);
                    oldMouseX = e.getX();
                    oldMouseY = e.getY();
                    return;
                }
            }
        }
        selectedPoint = null;
    }

    @FXML
    public void canvasOnDrag(MouseEvent e) {
        if (selectedPoint != null) {
            selectedPoint.setX(selectedPoint.getX() - oldMouseX + e.getX());
            selectedPoint.setY(selectedPoint.getY() - oldMouseY + e.getY());
            oldMouseX = e.getX();
            oldMouseY = e.getY();
            draw();
            listView.refresh();
        }
    }

    @FXML
    public void canvasOnMove(MouseEvent e) {
        mousePos.setText("" + canvas.getWidth() + ", " + canvas.getHeight() + "; " + e.getX() + ", " + e.getY());
    }

    @FXML
    public void listViewOnClick(MouseEvent e) {
        Stroke selected = listView.getSelectionModel().getSelectedItem();
        if (selected instanceof Heng) {
            hook.setDisable(false);
            hook.setSelected(((Heng) selected).getHook());
        }
        else {
            hook.setDisable(true);
            hook.setSelected(false);
        }
        visible.setSelected(selected.isVisible());
    }

    @FXML
    public void importCSV(ActionEvent exception) {
        try {
            if (strokes.size() > 0) alertMessage("Your current work will be lost.");
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open file");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV files", "*.csv"), new FileChooser.ExtensionFilter("All files", "*"));
            File selectedFile = chooser.showOpenDialog(((Button) exception.getSource()).getScene().getWindow());
            strokes.removeAll();
            if (selectedFile != null) {
                BufferedReader br = new BufferedReader(new FileReader(selectedFile));
                String s;
                if (strokes.size() > 0) strokes.subList(0, strokes.size()).clear();
                do {
                    s = br.readLine();
                    if (s != null) {
                        String[] tokens = s.split(",");
                        ArrayList<Point> points = new ArrayList<>();
                        for (int i=1;i<tokens.length;i+=2) points.add(new Point(Double.parseDouble(tokens[i]),Double.parseDouble(tokens[i+1])));
                        if (tokens[0].equals("H")) strokes.add(new Heng(points));
                        else if (tokens[0].equals("Hn")) strokes.add(new Heng(points,false));
                        else if (tokens[0].equals("S")) strokes.add(new Shu(points));
                        else if (tokens[0].equals("P")) strokes.add(new Pie(points));
                        else if (tokens[0].equals("N")) strokes.add(new Na(points));
                        else if (tokens[0].equals("D")) strokes.add(new Dian(points));
                        else if (tokens[0].equals("G")) strokes.add(new Gou(points));
                        else strokes.add(new Stroke(points));
                    }
                } while (s != null);
            }
            draw();
        } catch (Exception actionEvent) {
            alertError(actionEvent, "Invalid file!");
        }
    }

    @FXML
    public void exportCSV(ActionEvent e) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save file");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV files", "*.csv"), new FileChooser.ExtensionFilter("All files", "*"));
            File selectedFile = chooser.showSaveDialog(((Button) e.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                PrintWriter pw = new PrintWriter(new FileWriter(selectedFile, false));
                for (Stroke i: strokes) {
                    if (i instanceof Heng) {
                        pw.print("H");
                        if (!((Heng) i).getHook()) pw.print("n");
                    }
                    else if (i instanceof Shu) pw.print("S");
                    else if (i instanceof Pie) pw.print("P");
                    else if (i instanceof Na) pw.print("N");
                    else if (i instanceof Dian) pw.print("D");
                    else if (i instanceof Gou) pw.print("G");
                    else pw.print("X");
                    for (Point j: i.getPoints()) {
                        pw.print(","+j.getX()+","+j.getY());
                    } pw.println();
                }
                pw.close();
            }
        } catch (Exception actionEvent) {
            alertError(actionEvent);
        }
    }

    @FXML
    public void listViewUp(ActionEvent e) {
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            Stroke selected = listView.getSelectionModel().getSelectedItem();
            strokes.remove(index);
            strokes.add(index - 1, selected);
            listView.getSelectionModel().select(index-1);
        }
        draw();
    }

    @FXML
    public void listViewDown(ActionEvent e) {
        Stroke selected = listView.getSelectionModel().getSelectedItem();
        int index = listView.getSelectionModel().getSelectedIndex();
        if (index < strokes.size()-1) {
            strokes.remove(index);
            strokes.add(index + 1, selected);
            listView.getSelectionModel().select(index+1);
        }
        draw();
    }

    @FXML
    public void newStroke(ActionEvent e) {
        int index = comboBox.getSelectionModel().getSelectedIndex();
        Point[] points = new Point[]{new Point(typeDefaultCoords[index][0], typeDefaultCoords[index][1]), new Point(typeDefaultCoords[index][2], typeDefaultCoords[index][3])};
        if (types[index].equals("H")) strokes.add(new Heng(points));
        else if (types[index].equals("S")) strokes.add(new Shu(points));
        else if (types[index].equals("P")) strokes.add(new Pie(points));
        else if (types[index].equals("N")) strokes.add(new Na(points));
        else if (types[index].equals("D")) strokes.add(new Dian(points));
        else if (types[index].equals("G")) strokes.add(new Gou(points));
        else strokes.add(new Stroke(points));
        listView.getSelectionModel().select(strokes.size()-1);
        draw();
    }

    @FXML
    public void delete(ActionEvent e) {
        strokes.remove(listView.getSelectionModel().getSelectedIndex());
        draw();
    }

    @FXML
    public void saveImage(ActionEvent e) {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save file");
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG files", "*.png"), new FileChooser.ExtensionFilter("All files", "*"));
            File selectedFile = chooser.showSaveDialog(((Button) e.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                WritableImage wi = new WritableImage((int) canvas.getWidth(),(int) canvas.getHeight());
                draw();
                canvas.snapshot(null,wi);
                RenderedImage ri = SwingFXUtils.fromFXImage(wi,null);
                ImageIO.write(ri,"png",selectedFile);
            }
        } catch (Exception actionEvent) {
            alertError(actionEvent);
        }
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
