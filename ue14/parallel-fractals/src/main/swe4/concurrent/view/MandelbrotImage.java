package swe4.concurrent.view;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class MandelbrotImage extends ImageView {

    private final int nrCols;
    private final int nrRows;
    private final int maxIterations;
    private Color[] colors;
    private final PixelWriter pixelWriter;

    public MandelbrotImage(int nrCols, int nrRows, int maxIterations) {
        WritableImage image = new WritableImage(nrCols, nrRows);
        this.setImage(image);
        this.setPreserveRatio(true);
        this.setSmooth(true);
        this.pixelWriter = image.getPixelWriter();

        this.nrCols = nrCols;
        this.nrRows = nrRows;
        this.maxIterations = maxIterations;

        initColors();
        clear();
    }

    private void initColors() {
        colors = new Color[maxIterations];
        for (int i = 0; i < maxIterations; i++) {
            colors[i] = Color.hsb(360f * i / maxIterations, 1, i / (i + 8f));
        }
    }

    public void drawRow(int row, int[] iterations) {
        for (int col = 0; col < nrCols; col++) {
            pixelWriter.setColor(col, row, colors[iterations[col]]);
        }
    }

    public void drawRows(int fromRow, int[][] iterations) {
        int n = iterations.length;
        for (int row = fromRow; row < fromRow + n; row++) {
            for (int col = 0; col < nrCols; col++) {
                pixelWriter.setColor(col, row, colors[iterations[row - fromRow][col]]);
            }
        }
    }

    public void drawRows(int[] rows, int[][] iterations) {
        int n = iterations.length;
        for (int i = 0; i < rows.length; i++) {
            for (int col = 0; col < nrCols; col++) {
                pixelWriter.setColor(col, rows[i], colors[iterations[i][col]]);
            }
        }
    }

    public void draw(int[][] iterations) {
        drawRows(0, iterations);
    }

    public void clear() {
        for (int row = 0; row < nrRows; row++) {
            for (int col = 0; col < nrCols; col++) {
                pixelWriter.setColor(col, row, Color.WHITE);
            }
        }
    }
}