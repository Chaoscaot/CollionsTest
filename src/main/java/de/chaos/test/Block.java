package de.chaos.test;

import java.awt.*;

public class Block extends Rectangle {

    double v;
    public int m;

    public Block(int x, int width, double v, int m) {
        super(x, 0, width, width);
        this.v = v*-1;
        this.m = m;
    }

    public boolean collide(Block other){
        return !(this.x + this.width < other.x || this.x > other.x + other.width);
    }

    public boolean hitWall(){
        return this.x <= 0;
    }

    public void reverse() {
        this.v *= -1;
    }

    public double bounce(Block other){
        double sumM = this.m + other.m;
        double newV = ((this.m - other.m) / sumM) * this.v;
        newV += ((2 * other.m) / sumM) * other.v;
        return newV;
    }

    public void update(){
        this.x += this.v;
    }

    public void draw(Graphics2D g){
        g.setColor(Color.gray);
        g.fillRect(x,y, width, height);
    }
}
