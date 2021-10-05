package sample.Model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Dian extends Stroke {
    public Dian(ArrayList<Point> points) {
        super(points);
    }

    public Dian(Point[] points) {
        super(points);
    }

    public Dian(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public String toString() {
        return "D " + points.toString();
    }

    @Override
    public void drawSimple(GraphicsContext gc) {
        super.draw(gc);
    }

    @Override
    public void draw(GraphicsContext gc) {
        drawSimple(gc);
    }
}
