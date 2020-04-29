import java.util.ArrayList;

public class Boid {
    private int x_pos;
    private int y_pos;
    private double angle;

    /**
     *  Moves the Boid forward at its current angle
     *  @param dist the distance by which the Boid should move forward
     */
    public void move_for(int dist, int width, int height) {
        double angle_rad = Math.toRadians(this.angle);
        // CAH | cos(angle) = x_added/dist | cos(angle) * dist = x_added
        this.x_pos += (Math.cos(angle_rad) * dist);
        // SOH | sin(angle) = y_added/dist | sin(angle) * dist = y_added
        this.y_pos += (Math.sin(angle_rad) * dist);

        this.x_pos = x_pos % width;
        if (x_pos < 0) {
            this.x_pos = width + x_pos;
        }
        this.y_pos = y_pos % height;
        if (y_pos < 0) {
            this.y_pos = height + y_pos;
        }
    }

    /**
     * adjusts the Boid's current angle
     * @param adjust the amount by which the angle should in crease
     * (positive to move counterclockwise, negative to move clockwise)
     */
    public void turn(double adjust) {
        this.angle = (this.angle + adjust) % 360;
        if (angle < 0) {
            this.angle = 360 + angle;
        }
    }

    public void set_angle(double new_angle) {
        this.angle = new_angle;
    }

    /**
     * find the distance / hypotenuse to a Boid from this Boid
     * @param b the Boid to find the distance to
     */
    public double distance_to(Boid b) {
        int x_diff = Math.abs(x_pos - b.getx());
        int y_diff = Math.abs(y_pos - b.gety());
        return Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));
    }

    public double distance_to(CoordPair pair) {
        int x_diff = Math.abs(x_pos - pair.x);
        int y_diff = Math.abs(y_pos - pair.y);
        return Math.sqrt(Math.pow(x_diff, 2) + Math.pow(y_diff, 2));   
    }

    /**
     * finds the angle to a specified Boid
     * @param b the boid to find the angle to
     * @param dist the distance to that boid (the hypotenuse)
     */
    public double angle_to(Boid b) {
        // System.out.println(b);
        // System.out.println(b.getx());
        // System.out.println(b.gety());

        double y_diff = b.gety() - y_pos;
        double x_diff = b.getx() - x_pos;
        return Math.toDegrees(Math.atan2(y_diff, x_diff));
    }

    /**
     * returns the angle at which the nearest Boid is at
     * @param boidList the ArrayList of Boids this Boid should check against
     * @param width the width from the current viewing angle that the nearest Boid can be
     */
    public Boid nearestBoid(ArrayList<Boid> boidList, int width) {
        double min_found_distance = Integer.MAX_VALUE;
        Boid nearest = null;

        // find closeset Boid
        for (Boid b : boidList) {    
            if (b != this) {
                double distance = this.distance_to(b);
                double angle_off = Math.abs(this.angle_to(b) - this.angle);
                // if (distance < min_found_distance && (angle_off < width)) {
                if (distance < min_found_distance) {
                    min_found_distance = distance;
                    nearest = b;
                } 
            } else {
                // do nothing, Boid ignores itself
            }
        }

        return nearest;
    }

    public double avg_angle_in_range(ArrayList<Boid> boidList, double range) {
        double total = this.angle;
        int count = 1;
        for (Boid b : boidList) {
            if (this.distance_to(b) < range) {
                total += b.getangle();
                count++;
            }
        }

        return total / count;
    }

    public CoordPair nextPoint(int dist, double turn, int width, int height) {
        CoordPair out = new CoordPair(this.x_pos, this.y_pos);

        double angle_rad = Math.toRadians(this.angle + turn);
        // CAH | cos(angle) = x_added/dist | cos(angle) * dist = x_added
        out.x += (Math.cos(angle_rad) * dist);
        // SOH | sin(angle) = y_added/dist | sin(angle) * dist = y_added
        out.y += (Math.sin(angle_rad) * dist);

        out.x = x_pos % width;
        if (x_pos < 0) {
            out.x = width + x_pos;
        }
        out.y = y_pos % height;
        if (y_pos < 0) {
            out.y = height + y_pos;
        }

        return out;
    }


    /* ==================================================================== */
    // Constructors and var getters //

    /**
     * Constructor for a Boid object, sets angle to 0
     * @param x the initial x position
     * @param y the initial y position
     * @param angle the inital angle positoin
     */
    public Boid(int x, int y, double angle) {
        this.x_pos = x;
        this.y_pos = y;
        this.angle = angle;
    }

    /**
     * Constructor for a Boid object, sets angle to 0
     * @param x the initial x position
     * @param y the initial y position
     */
    public Boid(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Constructor for a Boid object, sets angle to 0, position = 0,0
     */
    public Boid() {
        this(0, 0, 0);
    }

    public Boid(Boid source) {
        this(source.getx(), source.gety(), source.getangle());
    }

    public int getx() {
        return this.x_pos;
    }

    public int gety() {
        return this.y_pos;
    }

    public double getangle() {
        return this.angle;
    }
}