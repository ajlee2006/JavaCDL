package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sample.Controller.Controller;

public class Main extends Application {

    private Stage primaryStage;
    private VBox splashLayout;

    @Override
    public void init() {
        Label label = new Label(" JavaCDL ⿻ ");
        label.setStyle("-fx-text-fill:green;-fx-font-family:Copperplate,serif;-fx-font-size:100px;");
        splashLayout = new VBox();
        splashLayout.getChildren().add(label);
        splashLayout.setEffect(new DropShadow());
        splashLayout.setStyle("-fx-background-color:lightcyan");
        splashLayout.setMinHeight(200);
        splashLayout.setAlignment(Pos.CENTER);
    }

    @Override
    public void start(final Stage initStage) throws Exception {
        final Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                Thread.sleep(3000);
                return "";
            }
        };
        showSplash(initStage,task, this::showMainStage);
        new Thread(task).start();
    }

    private void showMainStage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.initialise();
        primaryStage = new Stage(StageStyle.DECORATED);
        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setTitle("JavaCDL");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler ich) {
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                try {
                    ich.complete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.UNDECORATED);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.getIcons().add(new Image("icon.png"));
        initStage.setX(bounds.getMinX()+bounds.getWidth()/2 - 200);
        initStage.setY(bounds.getMinY()+bounds.getHeight()/2 - 75);
        initStage.show();
    }

    public interface InitCompletionHandler {
        public void complete() throws Exception;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
