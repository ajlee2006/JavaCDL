package sample.Model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Shu extends Stroke {
    public Shu(ArrayList<Point> points) {
        super(points);
    }

    public Shu(Point[] points) {
        super(points);
    }

    public Shu(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(0);
        double x1 = points.get(0).getX(), x2 = (1-extent)*points.get(0).getX() + extent*points.get(1).getX(),
                y1 = points.get(0).getY(), y2 = (1-extent)*points.get(0).getY() + extent*points.get(1).getY();
        if (extent != 0.0)
            gc.fillPolygon(new double[]{x1 - 6, x2 - 6, x2 + 6, x1 + 6, x1+10+0.01*(y2-y1)},
                new double[]{y1, y2+1, y2-1, y1+10,y1+3}, 5);
    }

    @Override
    public String toString() {
        return "S " + points.toString();
    }

    @Override
    public void drawSimple(GraphicsContext gc) {
        super.draw(gc);
    }
}
