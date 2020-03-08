package de.chaos.test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Main extends Canvas {

    public static Block block1;
    public static Block block2;
    public static JFrame frame;

    public static Main main;

    static int count = 0;
    static int digits = 2;
    static int timeSteps = 1 * (digits - 1);

    public static void main(String[] args) {
        main = new Main();
    }
    public Main(){

        block1 = new Block( 100 , 20, 0, 1);
        block2 = new Block(600 , 50, 0.01 , 1);

        frame = new JFrame();
        frame.setTitle("LightChess");
        frame.setName("LightChess");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        frame.add(this);
        frame.validate();

        Runnable runnable = () -> {
            int i = 0;
            while (true) {
                Main.main.tick();
                try {
                    Thread.sleep(1000/200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();;
                }
            }
        };
        Runnable runnable1 = () ->{
            while (true){
                Main.main.draw();
                try {
                    Thread.sleep(1000/60);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();;
                }
            }
        };
        Thread t = new Thread(runnable);
        t.setName("Game-Tick");
        t.start();
        Thread t2 = new Thread(runnable1);
        t2.setName("Renderer");
        t2.start();
    }

    public void tick(){
        for (int i = 0; i < timeSteps; i++) {
            if (block1.collide(block2)) {
                double v1 = block1.bounce(block2);
                double v2 = block2.bounce(block1);
                block1.v = v1;
                block2.v = v2;
                count++;
            }

            if (block1.hitWall()) {
                block1.reverse();
                count++;
            }

            block1.update();
            block2.update();
        }
        System.out.println(count);
    }
    public void draw(){
        BufferStrategy bs = getBufferStrategy();
        if(bs == null){
            createBufferStrategy(2);
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.setColor(Color.lightGray);
        g.fillRect(0,0, frame.getWidth(), frame.getHeight());

        block1.draw(g);
        block2.draw(g);

        g.dispose();
        bs.show();
    }
}
