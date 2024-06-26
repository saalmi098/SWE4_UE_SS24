package swe4.concurrent.logic;

public class MandelbrotSet {

    private final Region region;
    private final int resolution;
    private final int nrCols;
    private final int nrRows;
    private final int maxIterations;

    private final double delta;

    public MandelbrotSet(Region region, int resolution, int maxIterations) {
        this.region = region;
        this.resolution = resolution;
        this.maxIterations = maxIterations;

        delta = Math.max(region.getWidth(), region.getHeight()) / resolution;
        nrCols = (int) Math.ceil(region.getWidth() / delta);
        nrRows = (int) Math.ceil((region.getHeight()) / delta);
    }

    public int getNrCols() {
        return nrCols;
    }

    public int getNrRows() {
        return nrRows;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public int[][] compute() {
        int[][] set = new int[nrRows][];
        for (int row = 0; row < nrRows; row++) {
            set[row] = computeRow(row);
        }
        return set;
    }

    public int[][] computeRows(int fromRow, int toRow) {
        int[][] set = new int[toRow - fromRow + 1][];
        for (int row = fromRow; row <= toRow; row++) {
            set[row - fromRow] = computeRow(row);
        }
        return set;
    }

    public int[][] computeRows(int[] rows) {
        int[][] set = new int[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            set[i] = computeRow(rows[i]);
        }
        return set;
    }

    public int[] computeRow(int row) {
        int[] iterations = new int[nrCols];
        double ci = region.getMinY() + row * delta;

        for (int col = 0; col < nrCols; col++) {
            double cr = region.getMinX() + col * delta;
            double zr = 0.0, zi = 0.0;
            int i = 0;

            while (i < maxIterations && zr * zr + zi * zi < 4.0) {
                double zrNew = zr * zr - zi * zi + cr;
                zi = 2.0 * zr * zi + ci;
                zr = zrNew;
                i++;
            }

            iterations[col] = i < maxIterations ? i : 0;
        }

        return iterations;
    }
}