package sample.Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class Pie extends Stroke {

    public Pie(ArrayList<Point> points) {
        super(points);
    }

    public Pie(Point[] points) {
        super(points);
    }

    public Pie(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public String toString() {
        return "P " + points.toString();
    }

    @Override
    public void drawSimple(GraphicsContext gc) {
        super.draw(gc);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(5);
        double x1 = points.get(0).getX(), x2 = (1-extent)*points.get(0).getX() + extent*points.get(1).getX(),
                y1 = points.get(0).getY(), y2 = (1-extent)*points.get(0).getY() + extent*points.get(1).getY();
        if (extent != 0.0)
            gc.strokeArc(3*x2-2*x1,3*y1-2*y2,3*(x1-x2),3*(y2-y1),290,50, ArcType.OPEN);
    }
}
