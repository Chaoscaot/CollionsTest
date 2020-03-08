package de.chaos.test;

import java.awt.*;

public class Block {

    public double v;
    public double m;
    public double x;
    public int width;

    public Block(double x, int width, double v, double m) {
        this.v = v * -1;
        this.m = m;
        this.x = x;
        this.width = width;
    }

    public double distance(Block block) {
        return Math.sqrt(((this.x + this.width) - block.x) * ((this.x + this.width) - block.x));
    }

    public boolean collide(Block other){
        return !(this.x + this.width < other.x || this.x > other.x + other.width);
    }

    public boolean hitWall(){
        return this.x <= 0;
    }

    public void reverse() {
        this.v = this.v * -1;
    }

    public double bounce(Block other){
        double sumM = this.m + other.m;
        double newV = ((this.m - other.m) / sumM) * this.v;
        newV += ((2 * other.m) / sumM) * other.v;
        return newV;
    }

    public void update(long timeSteps) {
        this.x += this.v / timeSteps;
    }

    public void draw(Graphics2D g, int height) {
        g.setColor(Color.gray);
        g.fillRect((int)x,height - width, width, width);
    }
}
