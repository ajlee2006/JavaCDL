package sample.Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;

public class Na extends Stroke {
    public Na(ArrayList<Point> points) {
        super(points);
    }

    public Na(Point[] points) {
        super(points);
    }

    public Na(Point p1, Point p2) {
        super(p1, p2);
    }

    @Override
    public String toString() {
        return "N " + points.toString();
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
        gc.strokeArc(x1,y1-(y2-y1)*2,(x2-x1)*3,(y2-y1)*3,-110,-50, ArcType.OPEN);
        //gc.strokeLine(x1,y1,x2,y2);
    }
}
