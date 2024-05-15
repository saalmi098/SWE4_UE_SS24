package swe4.gui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ScribbleCanvas extends Pane {

  private final GraphicsContext gc;
  private final Canvas          canvas       = new Canvas();
  private final List<Point2D>   positionList = new ArrayList<>();
  private final List<Color>     colorList    = new ArrayList<>();
  private final Preferences     preferences;
  private       Point2D         currentPos   = new Point2D(1, 1);

  public ScribbleCanvas(Preferences preferences) {
    this.preferences = preferences;
    positionList.add(currentPos);

    gc = canvas.getGraphicsContext2D();
    gc.setLineWidth(2);

    this.getChildren().add(canvas);
    canvas.widthProperty().bind(this.widthProperty());
    canvas.heightProperty().bind(this.heightProperty());

    // register for resize events
    canvas.widthProperty().addListener(event -> redraw());
    canvas.heightProperty().addListener(event -> redraw());
  }

  public void addLineSegment(Point2D newPosition) {
    gc.strokeLine(currentPos.getX(), currentPos.getY(),
                  newPosition.getX(), newPosition.getY());
    currentPos = newPosition;
    positionList.add(currentPos);
    colorList.add(getLineColor());
  }

  public void addLineSegment(Direction direction) {
    int len = preferences.getSegmentLength();
    Point2D newPos = switch (direction) {
      case UP -> new Point2D(currentPos.getX(),
                             Math.max(1, currentPos.getY() - len));
      case DOWN -> new Point2D(currentPos.getX(),
                               Math.min((float)canvas.getHeight() - 1, currentPos.getY() + len));
      case LEFT -> new Point2D(Math.max(1, currentPos.getX() - len),
                               currentPos.getY());
      case RIGHT -> new Point2D(Math.min((float)canvas.getWidth() - 1,
                                         currentPos.getX() + len),
                                currentPos.getY());
    };

    addLineSegment(newPos);
  }

  private void redraw() {
    Color currColor = getLineColor();

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    for (int i = 0; i < positionList.size() - 1; i++) {
      Point2D from = positionList.get(i);
      Point2D to   = positionList.get(i + 1);
      setLineColor(colorList.get(i));
      gc.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
    }

    setLineColor(currColor);
  }

  public Color getLineColor() {
    return (Color)gc.getStroke();
  }

  public void setLineColor(Color color) {
    gc.setStroke(color);
  }
}
