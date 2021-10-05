package sample.Model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Arrays;

public class Stroke {
    ArrayList<Point> points;
    boolean visible;
    double extent;

    public Stroke(ArrayList<Point> points) {
        this.points = points;
        visible = true;
        extent = 1;
    }

    public Stroke(Point[] points) {
        this(new ArrayList<>(Arrays.asList(points)));
    }

    public Stroke(Point p1, Point p2) {
        this(new ArrayList<Point>(Arrays.asList(p1,p2)));
    }

    @Override
    public String toString() {
        return "Stroke " + points.toString();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(5);
        double x1 = points.get(0).getX(), x2 = (1-extent)*points.get(0).getX() + extent*points.get(1).getX(),
                y1 = points.get(0).getY(), y2 = (1-extent)*points.get(0).getY() + extent*points.get(1).getY();
        gc.strokeLine(x1,y1,x2,y2);
    }

    public void drawSimple(GraphicsContext gc) {
        draw(gc);
    }
}
