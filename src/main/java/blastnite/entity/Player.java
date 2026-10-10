package blastnite.entity;

import java.awt.Color;
import java.awt.Graphics2D;


public class Player extends Entity {
    private static final double RADIUS = 14;
    private static final double RUN_SPEED = 260;   
    private static final double GRAVITY = 1800;    
    private static final double JUMP_SPEED = 700;  

    private int runDirection;     
    private double velocityY;     
    private boolean onGround;

    public Player(double x, double y) {
        super(x, y, RADIUS);
    }

    public void setRunDirection(int direction) {
        runDirection = direction;
    }

   
    public void jump() {
        if (onGround) {
            velocityY = -JUMP_SPEED;   
            onGround = false;
        }
    }

    @Override
    public void update(double dt) {
        x += runDirection * RUN_SPEED * dt;

        velocityY += GRAVITY * dt;   
        y += velocityY * dt;

        onGround = false;         
    }


    public void landOnFloor(double floorY) {
        if (y + radius >= floorY) {
            y = floorY - radius;
            velocityY = 0;
            onGround = true;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        int size = (int) (radius * 2);
        g.setColor(new Color(90, 170, 255));
        g.fillOval((int) (x - radius), (int) (y - radius), size, size);
        g.setColor(Color.WHITE);
        g.drawOval((int) (x - radius), (int) (y - radius), size, size);
    }
}