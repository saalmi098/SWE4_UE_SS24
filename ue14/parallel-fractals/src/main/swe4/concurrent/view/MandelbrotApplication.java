package swe4.concurrent.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import swe4.concurrent.logic.MandelbrotSet;
import swe4.concurrent.logic.Region;
import swe4.util.MathExt;
import swe4.util.MathExt.Range;
import swe4.util.MathExt.Set;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MandelbrotApplication extends Application {

    private static final int ALGORITHM_VERSION = 4;
    private static final int PARALLELISM = 8;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int RESOLUTION = 1000;
    private static final int MAX_ITERATIONS = 1000;

    private final Region currentRegion = new Region(-2.5, 1.5, -2, 2);

    private ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    private static record MandelbrotRowRangeResult(int fromRow, int toRow, int[][] iterations) {}
    private static record MandelbrotScatteredRowResult(int[] rows, int[][] iterations) {}

    public static void main(String[] args) {
        launch(args);
    }

    private void handleZoomEvent(MouseEvent event, MandelbrotImage mandelbrotImage, Label infoLabel) {
        // Transform the bounds of mandelBrotImage to the coordinate system of stackPanel (parent).
        double imageMinX = mandelbrotImage.getBoundsInParent().getMinX();
        double imageMinY = mandelbrotImage.getBoundsInParent().getMinY();
        double imageMaxX = mandelbrotImage.getBoundsInParent().getMaxX();
        double imageMaxY = mandelbrotImage.getBoundsInParent().getMaxY();

        // (event.getX(), event.getX()) is the new center of the mandelbrot region (world coordinates).
        // This point is specified in the coordinate system of stackPanel. Transform it to the world coordinate system
        // and set the new center of the mandelbrot region.
        currentRegion.setCenter(
                MathExt.transform(event.getX(), imageMinX, imageMaxX, currentRegion.getMinX(), currentRegion.getMaxX()),
                MathExt.transform(event.getY(), imageMinY, imageMaxY, currentRegion.getMinY(), currentRegion.getMaxY()));

        // Zoom into (left button) or zoom out of mandelbrot region.
        double scale = event.getButton() == MouseButton.PRIMARY ? 0.5 : 2;
        currentRegion.scale(scale);

        computeMandelbrotSet(ALGORITHM_VERSION,
                new MandelbrotSet(currentRegion, RESOLUTION, MAX_ITERATIONS), mandelbrotImage, infoLabel);
    }

    private void computeMandelbrotSet1(MandelbrotSet mandelbrotSet,
                                       MandelbrotImage mandelbrotImage, Label infoLabel) {
        Platform.runLater(mandelbrotImage::clear);

        threadPool.execute(() -> {
            long startTime = System.nanoTime();
            int[][] iterations = mandelbrotSet.compute();
            long finishTime = System.nanoTime();

            Platform.runLater(() -> {
                mandelbrotImage.draw(iterations);
                displayInfo(infoLabel, startTime, finishTime);
            });
        });
    }

    private void computeMandelbrotSet2(MandelbrotSet mandelbrotSet,
                                       MandelbrotImage mandelbrotImage, Label infoLabel) {
        Platform.runLater(mandelbrotImage::clear);

        Range[] rowRanges = MathExt.partition(0, mandelbrotSet.getNrRows() - 1, PARALLELISM);
        List<Task<MandelbrotRowRangeResult>> tasks = new ArrayList<>();
        AtomicLong maxFinishTime = new AtomicLong(0);
        AtomicInteger nrFinishedTasks = new AtomicInteger(0);
        long startTime = System.nanoTime();

        for (Range rowRange : rowRanges) {
            var task = new Task<MandelbrotRowRangeResult>() {
                @Override
                protected MandelbrotRowRangeResult call() {
                    int[][] iterations = mandelbrotSet.computeRows(rowRange.from(), rowRange.to());
                    maxFinishTime.updateAndGet(value -> Math.max(value, System.nanoTime()));
                    return new MandelbrotRowRangeResult(rowRange.from(), rowRange.to(), iterations);
                }
            };

            task.setOnSucceeded(event -> {
                nrFinishedTasks.incrementAndGet();
                var result = (MandelbrotRowRangeResult) event.getSource().getValue();
                mandelbrotImage.drawRows(result.fromRow(), result.iterations());
                displayInfo(infoLabel, startTime, maxFinishTime.get());
            });

            tasks.add(task);
        }

        tasks.forEach(task -> threadPool.execute(task));
    }

    private void computeMandelbrotSet3(MandelbrotSet mandelbrotSet,
                                       MandelbrotImage mandelbrotImage, Label infoLabel) {
        Platform.runLater(mandelbrotImage::clear);

        Range[] rowRanges = MathExt.partition(0, mandelbrotSet.getNrRows() - 1, PARALLELISM);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicLong maxFinishTime = new AtomicLong(0);
        long startTime = System.nanoTime();

        for (Range rowRange : rowRanges) {
            var future = CompletableFuture
                    .supplyAsync(() -> {
                        // compute
                        int[][] iterations = mandelbrotSet.computeRows(rowRange.from(), rowRange.to());
                        maxFinishTime.updateAndGet(value -> Math.max(value, System.nanoTime()));
                        return new MandelbrotRowRangeResult(rowRange.from(), rowRange.to(), iterations);
                    })
                    .thenAccept(result -> {
                        // visualize
                        Platform.runLater(() -> {
                            mandelbrotImage.drawRows(result.fromRow(), result.iterations());
                        });
                    });

            futures.add(future);
        }

        // update infoLabel
        // allFuture ist die Future, die dann completed wird, wenn alle Futures in der Liste fertig sind
        var allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.thenRun(() -> Platform.runLater(() -> displayInfo(infoLabel, startTime, maxFinishTime.get())));
    }


    private void computeMandelbrotSet4(MandelbrotSet mandelbrotSet,
                                       MandelbrotImage mandelbrotImage, Label infoLabel) {
        Platform.runLater(mandelbrotImage::clear);

        Set[] rowSets = MathExt.randomPartition(0, mandelbrotSet.getNrRows() - 1, PARALLELISM);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        AtomicLong maxFinishTime = new AtomicLong(0);
        long startTime = System.nanoTime();

        for (Set rowSet : rowSets) {
            var future = CompletableFuture
                    .supplyAsync(() -> {
                        // compute
                        int[][] iterations = mandelbrotSet.computeRows(rowSet.elements());
                        maxFinishTime.updateAndGet(value -> Math.max(value, System.nanoTime()));
                        return new MandelbrotScatteredRowResult(rowSet.elements(), iterations);
                    })
                    .thenAccept(result -> {
                        // visualize
                        Platform.runLater(() -> {
                            mandelbrotImage.drawRows(result.rows(), result.iterations());
                        });
                    });

            futures.add(future);
        }

        // update infoLabel
        // allFuture ist die Future, die dann completed wird, wenn alle Futures in der Liste fertig sind
        var allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.thenRun(() -> Platform.runLater(() -> displayInfo(infoLabel, startTime, maxFinishTime.get())));
    }

    private void computeMandelbrotSet(int version, MandelbrotSet mandelbrotSet,
                                      MandelbrotImage mandelbrotImage, Label infoLabel) {
        switch (version) {
            case 1 -> computeMandelbrotSet1(mandelbrotSet, mandelbrotImage, infoLabel);
            case 2 -> computeMandelbrotSet2(mandelbrotSet, mandelbrotImage, infoLabel);
            case 3 -> computeMandelbrotSet3(mandelbrotSet, mandelbrotImage, infoLabel);
            case 4 -> computeMandelbrotSet4(mandelbrotSet, mandelbrotImage, infoLabel);
            default -> throw new IllegalArgumentException("Invalid version: " + version);
        }
    }

    private void displayInfo(Label infoLabel, long startTime, long finishTime) {
        infoLabel.setText(
                "Version: %d, Parallelism: %d, Time: %.4f s"
                        .formatted(ALGORITHM_VERSION, PARALLELISM, (finishTime - startTime) / 1e9));
    }

    @Override
    public void start(Stage primaryStage) {
        MandelbrotSet mandelbrotSet = new MandelbrotSet(currentRegion, RESOLUTION, MAX_ITERATIONS);
        MandelbrotImage mandelbrotImage = new MandelbrotImage(
                mandelbrotSet.getNrCols(), mandelbrotSet.getNrRows(), mandelbrotSet.getMaxIterations());

        Label infoLabel = new Label();
        infoLabel.setTextFill(Color.WHITE);

        mandelbrotImage.fitWidthProperty().bind(primaryStage.widthProperty());

        StackPane stackPane = new StackPane();

        stackPane.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleZoomEvent(event, mandelbrotImage, infoLabel));

        stackPane.getChildren().addAll(mandelbrotImage, infoLabel);
        StackPane.setMargin(infoLabel, new Insets(10));
        StackPane.setAlignment(infoLabel, Pos.BOTTOM_RIGHT);

        primaryStage.setTitle("Mandelbrot Set");
        primaryStage.setScene(new Scene(stackPane, 750, 750));
        primaryStage.show();

        primaryStage.setOnCloseRequest(__ -> {
            Platform.exit();
            System.exit(0);
        });

        computeMandelbrotSet(ALGORITHM_VERSION, mandelbrotSet, mandelbrotImage, infoLabel);
    }
}
