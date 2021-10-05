package sample.Model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Gou extends Stroke {

    public Gou(ArrayList<Point> points) {
        super(points);
    }

    public Gou(Point[] points) {
        super(points);
    }

    public Gou(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public String toString() {
        return "G " + points.toString();
    }

    @Override
    public void drawSimple(GraphicsContext gc) {
        super.draw(gc);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(0);
        double x1 = points.get(0).getX(), x2 = (1-extent)*points.get(0).getX() + extent*points.get(1).getX(),
                y1 = points.get(0).getY(), y2 = (1-extent)*points.get(0).getY() + extent*points.get(1).getY();
        gc.fillPolygon(new double[]{x1, x2, x1}, new double[]{y1, y2, y2-0.1*(y2-y1)}, 3);
    }
}
