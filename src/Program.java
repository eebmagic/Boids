import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import javafx.scene.Group;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Program extends Application {

    
    
    final int screen_width = 1500;
    final int screen_height = 1000;
    final int boid_size = 10;
    final int total_boids = 250;

    // Flight Parameters
    final int COLLISION_AVOID_ANGLE = 5;
    final int COLLISION_DIST = boid_size;
    final int GROUP_DIST = 25;                  // Distance at which a boid is considered in a group
    final double MANUVERABILITY = (double)1/30;
    final int ANGLE_AVERAGE_RADIUS = 100;        // Distance at which another boid should be angle matched
    final int MOVEMENT_DIST = 5;
    final int VIEW_ANGLE = 45;


    /** ============================================= **/

    private ArrayList<Boid> boidList = new ArrayList<>();

    private void makeBoids(int total) {
        Random rand = new Random();
        for (int i = 0; i < total; i++) {
            Boid b = new Boid(rand.nextInt(screen_width), rand.nextInt(screen_height), rand.nextInt(360));
            boidList.add(b);
            // boidList.add(new Boid(5, 10));
        }
    }

    private void printBoids() {
        for (Boid boid: boidList) {
            System.out.println(boid);
            System.out.print("    x: ");
            System.out.println(boid.getx());
            System.out.print("    y: ");
            System.out.println(boid.gety());
            System.out.print("    angle: ");
            System.out.println(boid.getangle());
        }
    }

    @Override
    public void start(Stage primaryStage) {

        /** ============================================= **/
        // setup inital points
        makeBoids(total_boids);
        printBoids();


        /** ============================================= **/
        // setup screen
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(screen_width, screen_height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setTitle("Boids Simulation");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        /** ============================================= **/
        // Animation loop

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                // printBoids();

                // redraw boidlist objects
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                for (Boid b : boidList) {
                    double end_x = b.getx() + Math.cos(Math.toRadians(b.getangle())) * boid_size * 2;
                    double end_y = b.gety() + Math.sin(Math.toRadians(b.getangle())) * boid_size * 2;
                    
                    double left_x = b.getx() + Math.cos(Math.toRadians(b.getangle() + VIEW_ANGLE)) * boid_size * 1.3;
                    double left_y = b.gety() + Math.sin(Math.toRadians(b.getangle() + VIEW_ANGLE)) * boid_size * 1.3;

                    double right_x = b.getx() + Math.cos(Math.toRadians(b.getangle() - VIEW_ANGLE)) * boid_size * 1.3;
                    double right_y = b.gety() + Math.sin(Math.toRadians(b.getangle() - VIEW_ANGLE)) * boid_size * 1.3;

                    gc.strokeLine(b.getx(), b.gety(), end_x, end_y);
                    // gc.strokeLine(b.getx(), b.gety(), left_x, left_y);
                    // gc.strokeLine(b.getx(), b.gety(), right_x, right_y);
                    gc.strokeOval(b.getx()-(boid_size / 2), b.gety()-(boid_size / 2), boid_size, boid_size);
                }

                // move all boids
                for (Boid b : boidList) {
                    b.move_for(MOVEMENT_DIST, screen_width, screen_height);

                    // find angle and distance to nearest Boid
                    Boid nearest = b.nearestBoid(boidList, VIEW_ANGLE);
                    if (nearest != null) {
                        double target_angle = b.angle_to(nearest);
                        double target_dist = b.distance_to(nearest);

                        // System.out.println(nearest);
                        // System.out.println(target_angle);
                        // System.out.println(target_dist);

                        // move towards or with
                        if (target_dist > GROUP_DIST) {
                            // turn towards Boid if nearest Boid is too far away
                            b.turn((target_angle - b.getangle()) * MANUVERABILITY);
                        } else {
                            // match average angle of group of Boids in radius
                            b.turn((b.avg_angle_in_range(boidList, ANGLE_AVERAGE_RADIUS) - b.getangle()) * MANUVERABILITY);
                        }

                        // prevent collision
                        // if (target_dist < 15) {
                        //     CoordPair left = b.nextPoint(MOVEMENT_DIST, COLLISION_AVOID_ANGLE, screen_width, screen_height);
                        //     CoordPair right = b.nextPoint(MOVEMENT_DIST, -COLLISION_AVOID_ANGLE, screen_width, screen_height);
                        //     double dist_left = b.distance_to(left);
                        //     double dist_right = b.distance_to(right);
                        //     if (dist_left > dist_right) {
                        //         b.turn(COLLISION_AVOID_ANGLE);
                        //     } else {
                        //         b.turn(-COLLISION_AVOID_ANGLE);
                        //     }
                        // }
                    }
                }

                // Pause for next frame
                try {
                    Thread.sleep(7);
                    // Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Caught try block for timing.");
                }
            }
        }.start();

    }

    public static void main(String[] args) {
        launch(args);
    }
}