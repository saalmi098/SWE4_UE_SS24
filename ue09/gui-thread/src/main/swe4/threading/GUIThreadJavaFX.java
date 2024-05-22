// VM options to start application:
//   --module-path ${SWE4_HOME}\lib\javafx-sdk-21.0.2\lib --add-modules javafx.controls

package swe4.threading;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import swe4.util.Util;

public class GUIThreadJavaFX extends Application {

    private Button startButton;
    private ProgressIndicator progressIndicator;

    private class Worker extends Thread {
        @Override
        public void run() {
            for (int i = 0; i <= 100; i++) {

                // Falsch!! GUI-Elemente dürfen nur im JavaFX-Thread verändert werden (sind nicht thread-safe)
                //progressIndicator.setProgress(i / 100.0);

                // Richtig: runLater + final Variable
                final double progress = i / 100.0;
                Platform.runLater(() -> progressIndicator.setProgress(progress));

                Util.sleep(100);
            }
//            startButton.setDisable(false);
            Platform.runLater(() -> startButton.setDisable(false));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        progressIndicator = new ProgressIndicator(0);
        progressIndicator.setPadding(new Insets(15));
        progressIndicator.setMaxWidth(Double.MAX_VALUE);
        progressIndicator.setMaxHeight(Double.MAX_VALUE);

        startButton = new Button("Start");
        startButton.setOnAction(e -> {
            // Var. 1
            /*startButton.setDisable(true);
            for (int i = 0; i <= 100; i++) {
                progressIndicator.setProgress(i / 100.0);
                Util.sleep(100);
            }
            startButton.setDisable(false);*/

            // Var. 2: using a thread
            Worker worker = new Worker();
            worker.setDaemon(true); // worker soll auch beendet werden, wenn das Hauptprogramm beendet wird
            startButton.setDisable(true);
            worker.start();
        });

        GridPane rootPane = new GridPane();
        rootPane.add(progressIndicator, 0, 0);
        rootPane.add(startButton, 0, 1);

        GridPane.setVgrow(progressIndicator, Priority.ALWAYS);
        GridPane.setHgrow(progressIndicator, Priority.ALWAYS);
        GridPane.setHalignment(startButton, HPos.CENTER);
        GridPane.setMargin(startButton, new Insets(0, 10, 10, 10));

        Scene scene = new Scene(rootPane, 200, 200);

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(140);
        primaryStage.setMinHeight(160);
        primaryStage.setTitle("Thread Test");
        primaryStage.show();
    }
}