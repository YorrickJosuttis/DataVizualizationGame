package data.viz.game;
import javafx.scene.canvas.Canvas;
import java.util.List;
import java.util.ArrayList;
public class RandomPointsRectangle extends Canvas {

    private static final double BORDER_WIDTH = 2;

    private int circleDiameter;

    private List<double[]> points = new ArrayList<>();

    public RandomPointsRectangle(int width, int height, int circleDiameter) {
        super(width, height);
        this.circleDiameter = circleDiameter;
        drawBorder();
    }

    private void drawBorder() {
        var gc = getGraphicsContext2D();
        gc.setLineWidth(BORDER_WIDTH);
        gc.strokeRect(BORDER_WIDTH / 2, BORDER_WIDTH / 2, getWidth() - BORDER_WIDTH, getHeight() - BORDER_WIDTH);
    }

    private void addPoint(double x, double y) {
        var gc = getGraphicsContext2D();
        gc.fillOval(x, y, circleDiameter, circleDiameter);
        points.add(new double[]{x, y});
    }

    public void clearPoints() {
        hidePoints();
        points.clear();
    }

    // removes the points visually but keeps them for comparison
    public void hidePoints() {
        var gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
        drawBorder();
    }

    private boolean overlaps(double x, double y) {
        for (double[] p : points) {
            double dx = (p[0] + circleDiameter / 2.0) - (x + circleDiameter / 2.0);
            double dy = (p[1] + circleDiameter / 2.0) - (y + circleDiameter / 2.0);
            if (dx * dx + dy * dy < circleDiameter * circleDiameter) {
                return true;
            }
        }
        return false;
    }

    public void addRandomPoints(int numPoints) {
        int maxAttempts = numPoints * 100;
        int added = 0;
        int attempts = 0;
        while (added < numPoints && attempts < maxAttempts) {
            double x = Math.random() * (getWidth() - circleDiameter);
            double y = Math.random() * (getHeight() - circleDiameter);
            if (!overlaps(x, y)) {
                addPoint(x, y);
                added++;
            }
            attempts++;
        }
    }

    public void addPoints(double[][] pointsToDraw) {
        for (double[] point : pointsToDraw) {
            double x = point[0];
            double y = point[1];
            addPoint(x, y);
        }
    }

    public void addPoints(int numPoints) {
        for (int i = 0; i < numPoints; i++) {
            double x = Math.random() * getWidth();
            double y = Math.random() * getHeight();
            addPoint(x, y);
        }
    }

    public double[][] getPoints() {
        double[][] result = new double[points.size()][2];
        for (int i = 0; i < points.size(); i++) {
            result[i] = points.get(i);
        }
        return result;
    }

    public boolean equals(Object obj) {
        // only equal if the points are exactly the same
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        RandomPointsRectangle other = (RandomPointsRectangle) obj;
        if (points.size() != other.points.size()) return false;
        for (int i = 0; i < points.size(); i++) {
            double[] p1 = points.get(i);
            double[] p2 = other.points.get(i);
            if (p1[0] != p2[0] || p1[1] != p2[1]) return false;
        }
        return true;
    }
}
