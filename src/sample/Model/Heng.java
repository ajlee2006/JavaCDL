package sample.Model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Heng extends Stroke {
    boolean hook;

    public Heng(ArrayList<Point> points) {
        this(points, true);
    }

    public Heng(ArrayList<Point> points, boolean hook) {
        super(points);
        this.hook = hook;
    }

    public Heng(Point[] points) {
        this(points, true);
    }

    public Heng(Point[] points, boolean hook) {
        super(points);
        this.hook = hook;
    }

    public Heng(Point p1, Point p2) {
        this(p1,p2,true);
    }

    public Heng(Point p1, Point p2, boolean hook) {
        super(p1, p2);
        this.hook = hook;
    }

    public boolean getHook() {
        return hook;
    }

    public void setHook(boolean hook) {
        this.hook = hook;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(0);
        double x1 = points.get(0).getX(), x2 = (1-extent)*points.get(0).getX() + extent*points.get(1).getX(),
                y1 = points.get(0).getY(), y2 = (1-extent)*points.get(0).getY() + extent*points.get(1).getY();
        //gc.strokeLine(points.get(0).getX(), points.get(0).getY(), points.get(1).getX(), points.get(1).getY());
        if (hook && extent == 1.0)
            gc.fillPolygon(new double[]{x1-1.5, x1+1.5, x2-1.5, x2+1.5, (x1+7*x2)/8+1.2, (x1+7*x2)/8-5},
                new double[]{y1-2, y1+2, y2+2, y2-2, (y1+7*y2)/8-Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))/25-4, (y1+5*y2)/6-2}, 6);
        else
            gc.fillPolygon(new double[]{x1-1.5, x1+1.5, x2-1.5, x2+1.5},
                    new double[]{y1-2, y1+2, y2+2, y2-2}, 4);
    }

    @Override
    public String toString() {
        return "H " + points.toString() + " " + hook;
    }

    @Override
    public void drawSimple(GraphicsContext gc) {
        super.draw(gc);
    }
}
