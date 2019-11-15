import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Shape {
    private Polygon shape;
    private int layers;
    private int xVelo;
    private int yVelo;
    private int sizeMuliplier = 30;
    private double offset;
    private Rectangle r = ShapeShooterGame.spawnArea;
    private int borderWidth = ShapeShooterGame.WIDTH;
    private int borderHeight = ShapeShooterGame.HEIGHT;
    private Rectangle worldBorder = new Rectangle(ShapeShooterGame.WIDTH, ShapeShooterGame.HEIGHT);

    public Shape(int sides, int xStart, int yStart, int xV, int yV) {
        int[] xVals = new int[sides];
        int[] yVals = new int[sides];
        offset = Math.random() * Math.PI * 2;
        double theta = 2 * Math.PI / sides;
        for (int i = 0; i < sides; i++) {
            double x = sizeMuliplier * Math.cos(theta * i + offset) + xStart;
            double y = sizeMuliplier * Math.sin(theta * i + offset) + yStart;
            xVals[i] = (int) x;
            yVals[i] = (int) y;
        }
        shape = new Polygon(xVals, yVals, sides);
        layers = sides;
        xVelo = xV;
        yVelo = yV;
    }

    public Shape stripLayer() {
        if (layers - 1 == 2) {
            return null;
        }
        int centerOfShapeX = 0;
        int centerOfShapeY = 0;
        for (int k = 0; k < layers; k++) {
            centerOfShapeX += shape.xpoints[k];
            centerOfShapeY += shape.ypoints[k];
        }
        centerOfShapeX /= layers;
        centerOfShapeY /= layers;
        return new Shape(layers - 1, centerOfShapeX, centerOfShapeY, xVelo, yVelo);
    }

    public boolean containsPellet(Pellet p) {
        return shape.contains(p.getLoc()) && !r.contains(p.getLoc());
    }

    public boolean containsBaby(Shield b) {
        if (!r.contains(b.getLocation())) {
            double size = b.getMass() / 4;
            int approximateSides = 100;
            int[] xVals = new int[approximateSides];
            int[] yVals = new int[approximateSides];
            // creates parallel arrays of ints to represent x,y pairs to check for
            // intersection
            offset = Math.random() * Math.PI * 2;
            double theta = 2 * Math.PI / 100;
            for (int i = 0; i < 100; i++) {
                double x = size * Math.cos(theta * i + offset) + b.getLocation().getX();
                double y = size * Math.sin(theta * i + offset) + b.getLocation().getY();
                xVals[i] = (int) x;
                yVals[i] = (int) y;
            }
            for (int k = 0; k < approximateSides; k++) {
                if (shape.contains(new Point(xVals[k], yVals[k]))) {
                    ShapeShooterGame.score += layers - 2;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsPlayer(Player p) {// check if the player went into the obstacle
        int size = ShapeShooterGame.playerSize / 4;
        int approximateSides = 100;
        int[] xVals = new int[approximateSides];
        int[] yVals = new int[approximateSides];
        // creates parallel arrays of ints to represent x,y pairs to check for
        // intersection
        offset = Math.random() * Math.PI * 2;
        double theta = 2 * Math.PI / 100;
        for (int i = 0; i < 100; i++) {
            double x = size * Math.cos(theta * i + offset) + p.getPosition().getX();
            double y = size * Math.sin(theta * i + offset) + p.getPosition().getY();
            xVals[i] = (int) x;
            yVals[i] = (int) y;
        }
        for (int k = 0; k < approximateSides; k++) {
            if (shape.contains(xVals[k], yVals[k])) {
                return true;
            }
        }
        return false;
    }

    public int getLayerCount() {
        return layers;
    }

    public void move() { // makes sure that it's not hitting the border
        for (int t = 0; t < borderWidth; t++) {
            if (shape.contains(t, borderHeight)) {
                yVelo *= -1;
            } else if (shape.contains(t, 0)) {
                yVelo *= -1;
            }
        }
        for (int k = 0; k < borderHeight; k++) {
            if (shape.contains(0, k)) { // top wall
                xVelo *= -1;
            } else if (shape.contains(borderWidth, k)) {
                xVelo *= -1;
            }
        }
        shape.translate(xVelo, yVelo);
    }

    public Polygon getPoly() {
        return shape;
    }

    /*
     * Shapes will start as triangle, square, penta, or hex and you have to shoot
     * them before they hit you.
     */
}
