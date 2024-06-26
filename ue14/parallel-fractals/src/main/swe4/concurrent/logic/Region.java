package swe4.concurrent.logic;

public class Region {

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public Region(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public double getCenterX() {
        return minX + getWidth() / 2;
    }

    public double getCenterY() {
        return minY + getHeight() / 2;
    }

    public void setCenter(double centerX, double centerY) {
        double w2 = getWidth() / 2;
        minX = centerX - w2;
        maxX = centerX + w2;

        double h2 = getHeight() / 2;
        minY = centerY - h2;
        maxY = centerY + h2;
    }

    public void scale(double factor) {
        scale(factor, factor);
    }

    public void scale(double factorX, double factorY) {
        double wNew2 = getWidth() * factorX / 2;
        double cx = getCenterX();
        minX = cx - wNew2;
        maxX = cx + wNew2;

        double hNew2 = getHeight() * factorY / 2;
        double cy = getCenterY();
        minY = cy - hNew2;
        maxY = cy + hNew2;
    }

    public double getWidth() {
        return maxX - minX;
    }

    public double getHeight() {
        return maxY - minY;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
}
