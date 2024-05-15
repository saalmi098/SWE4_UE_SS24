package swe4.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class PreferencesDialog {

  private final Stage       dialogStage = new Stage();
  private final Preferences preferences;
  private final TextField   segmentLengthField;

  // Dependency Injection -> Preferences wird übergeben
  public PreferencesDialog(Window owner, Preferences preferences) {
    this.preferences = preferences;

    segmentLengthField = new TextField();
    segmentLengthField.setPromptText("enter segment length");

    Button btnOk = new Button("Ok");
    btnOk.setId("ok-button");
    btnOk.setDefaultButton(true);
    btnOk.setOnAction(this::handleOk);

    Button btnCancel = new Button("Cancel");
    btnCancel.setId("cancel-button");
    btnCancel.setOnAction(this::handleCancel);

    HBox buttonBar = new HBox(20);
    buttonBar.setId("button-bar");
    buttonBar.getChildren().addAll(btnOk, btnCancel);

    GridPane prefPane = new GridPane();
    prefPane.setId("pref-pane");
    prefPane.add(new Label("Segment Length:"), 0, 0);
    prefPane.add(segmentLengthField, 1, 0);
    prefPane.add(buttonBar, 0, 1, 2, 1);

    Scene dialogScene = new Scene(prefPane);
    dialogScene.getStylesheets()
               .add(getClass().getResource("css/preferences-dialog.css")
                              .toExternalForm());

    dialogStage.setScene(dialogScene);
    dialogStage.setTitle("Preferences Dialog");
    dialogStage.initModality(Modality.WINDOW_MODAL);
    dialogStage.initStyle(StageStyle.UTILITY);
    dialogStage.initOwner(owner);
    dialogStage.setResizable(false);
  }

  public void show() {
    segmentLengthField.setStyle("-fx-border-width: 0px;");
    Platform.runLater(segmentLengthField::requestFocus);

    segmentLengthField.setText(String.valueOf(preferences.getSegmentLength()));

    dialogStage.show();
  }

  private void handleOk(ActionEvent event) {
    try {
      int length = Integer.parseInt(segmentLengthField.getText());
      preferences.setSegmentLength(length);
      dialogStage.hide();
    }
    catch (NumberFormatException ex) {
      segmentLengthField.setStyle("-fx-border-color: red; " +
                                  "-fx-border-width: 2px; " +
                                  "-fx-border-radius: 3px;");
    }
  }

  private void handleCancel(ActionEvent event) {
    dialogStage.hide();
  }

}
