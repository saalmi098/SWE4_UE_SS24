package swe4.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ScribbleFX extends Application {

    // Constants for border and background color in canvas
    private static final Border DEFAULT_BORDER = new Border(new BorderStroke(Color.DIMGREY, BorderStrokeStyle.SOLID, null, null));
    private static final Background CANVAS_BACKGROUND = new Background(new BackgroundFill(Color.CORNSILK, null, null));

    // ListView für Nachrichten-Trace hinzufügen
    private ListView<String> messageList;
    private final ObservableList<String> messages = FXCollections.observableArrayList();

    private Button leftButton;
    private Button rightButton;
    private Button upButton;
    private Button downButton;
    private ColorPicker colorPicker;

    private Pane buttonPane;
    private Pane canvas;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(javafx.stage.Stage stage) {
        stage.setTitle("ScribbleFX");

        Pane controlPane = createControlPane();
        messageList = createMessageList();
        canvas = createCanvas();

        Pane topPane = new HBox(controlPane, messageList);
        topPane.setId("top-pane");

        VBox rootPane = new VBox(topPane, canvas);
        rootPane.setId("root-pane");

        Scene scene = new Scene(rootPane, 500, 500);

        // Load style sheet
        scene.getStylesheets().add(getClass().getResource("css/scribble-fx.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    private Pane createControlPane() {
        leftButton = createIconButton("left-button", "css/button-left.png");
        rightButton = createIconButton("right-button", "css/button-right.png");
        upButton = createIconButton("up-button", "css/button-up.png");
        downButton = createIconButton("down-button", "css/button-down.png");

        ButtonEventHandler buttonHandler = new ButtonEventHandler();

        // Var. 1:
        /*leftButton.addEventHandler(ActionEvent.ACTION, buttonHandler);
        // leftButton.setOnAction(buttonHandler); // alternative
        rightButton.addEventHandler(ActionEvent.ACTION, buttonHandler);
        upButton.addEventHandler(ActionEvent.ACTION, buttonHandler);
        downButton.addEventHandler(ActionEvent.ACTION, buttonHandler);*/

        GridPane buttonPane = new GridPane();
        buttonPane.setId("button-pane");
        buttonPane.add(leftButton, 0, 1);
        buttonPane.add(rightButton, 2, 1);
        buttonPane.add(upButton, 1, 0);
        buttonPane.add(downButton, 1, 2);

        // Var. 2
        // buttonPane.addEventHandler(ActionEvent.ACTION, buttonHandler);

        // Var. 3
        // Instanz einer "anonymen" Klasse, die EventHandler implementiert wird erstellt
        // Anm.: Wenn anonyme Klasse sehr lange wird, ist eine echte Klasse sinnvoller
        /*buttonPane.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleButtonEvent(event);
            }
        });*/

        // Var. 4
        // EventHandler ist ein FunctionalInterface -> Interface, das nur 1 Methode implementiert
        // Compiler kann dadurch selbst ableiten, welche Methode zu überschreiben ist
        // (Anm.: Wären mehrere Methoden zu implementieren, funktioniert eine Lambda nicht -> anonyme Klasse verwenden)
        //buttonPane.addEventHandler(ActionEvent.ACTION, (ActionEvent event) -> handleButtonEvent(event));

        // Var. 5
        //buttonPane.addEventHandler(ActionEvent.ACTION, event -> handleButtonEvent(event));

        // Var. 6: Methodenreferenz
        buttonPane.addEventHandler(ActionEvent.ACTION, this::handleButtonEvent);

        colorPicker = new ColorPicker();
        colorPicker.setId("color-picker");
        colorPicker.setOnAction((ActionEvent event) -> appendMessage("Color '%s' selected".formatted(colorPicker.getValue())));

        HBox colorPane = new HBox(new Label("Color: "), colorPicker);
        colorPane.setId("color-pane");

        // styling: (not needed if using css)
        //colorPane.setSpacing(5);
        //colorPane.setPadding(new Insets(10));

        VBox controlPane = new VBox(buttonPane, colorPane);
        controlPane.setId("control-pane");

        // styling:
        //controlPane.setPadding(new Insets(10));
        //controlPane.setBorder(DEFAULT_BORDER);

        return controlPane;
    }

    private Pane createCanvas() {
        Pane canvas = new Pane();
        canvas.setId("canvas");
        VBox.setVgrow(canvas, Priority.ALWAYS); // canvas will get remaining space in VBox
        VBox.setMargin(canvas, new Insets(10));

        // styling:
        //canvas.setBorder(DEFAULT_BORDER);
        //canvas.setBackground(CANVAS_BACKGROUND);

        return canvas;
    }

    // Create buttons with text labels
    private Button createTextButton(String id, String caption) {
        Button button = new Button(caption);
        button.setId(id);

        // styling:
        //button.setPadding(new Insets(5));
        //button.setPrefSize(50, 40);

        return button;
    }

    // Create buttons with icons
    private Button createIconButton(String id, String iconFile) {
        Button button = new Button();
        button.setId(id);

        // styling
        //button.setPadding(new Insets(5));

        Image icon = new Image(getClass().getResourceAsStream(iconFile));
        button.setGraphic(new ImageView(icon));

        return button;
    }

    private ListView<String> createMessageList() {
        messageList = new ListView<String>();
        messageList.setItems(messages);

        messageList.setId("message-list");
        HBox.setHgrow(messageList, Priority.ALWAYS); // messageList will get remaining space in HBox.
        // styling:
        //messageList.setPrefHeight(100);
        //messageList.setBorder(new Border(new BorderStroke(Color.DIMGREY, BorderStrokeStyle.SOLID, null, null)));

        return messageList;
    }

    // Add a line to the tail of the message list
    private void appendMessage(String message) {
        messages.add(message);
        messageList.scrollTo(messages.size());
    }

    private void handleButtonEvent(ActionEvent event) {
        if (event.getTarget() instanceof Button button) {
            appendMessage("Button '%s' pressed".formatted(button.getId()));
        }
    }

    private class ButtonEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            handleButtonEvent(event);

            // event.consume(); // same as in C#: e.Handled = true;
        }
    }
}