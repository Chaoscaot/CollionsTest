package de.chaos.test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.math.BigInteger;

public class Main extends Canvas {

    public static Block block1;
    public static Block block2;
    public static JFrame frame;

    public static Main mainInstance;


    static BigInteger count = BigInteger.ZERO;
    static int digits = 6;
    static int timeSteps = (int)Math.pow(10, digits);

    public static void main(String[] args) {
        mainInstance = new Main();
    }

    public Main() {
        block1 = new Block( 100 , 20, 0, 1);
        block2 = new Block(600 , 20 + digits * 20, 1 , Math.pow(100, digits));

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
            while (true) {
                if (Main.mainInstance != null) {
                    Main.mainInstance.tick();
                }
                try {
                    Thread.sleep(1000/200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();;
                }
            }
        };
        Runnable runnable1 = () ->{
            while (true){
                if (Main.mainInstance != null) {
                    Main.mainInstance.draw();
                }
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

    public void tick() {
        int addition = 0;
        if (digits == 2) {
            addition++;
        }
        int t = (int)(timeSteps * (1 / block1.distance(block2)) * (digits + addition));
        if (t <= 0) {
            t = 1;
        }

        for (int i = 0; i < t; i++) {
            block1.update(t);
            block2.update(t);

            if (block1.collide(block2)) {
                double v1 = block1.bounce(block2);
                double v2 = block2.bounce(block1);
                block1.v = v1;
                block2.v = v2;
                count = count.add(BigInteger.ONE);
            }

            if (block1.hitWall()) {
                block1.reverse();
                count = count.add(BigInteger.ONE);
            }
        }
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

        g.setColor(new Color(20, 20, 20));
        int height = block2.width + 10;
        g.fillRect(0, height, frame.getWidth(), 2);

        block1.draw(g, height);
        block2.draw(g, height);

        String pi = "3141592653589793238462643393279502884197169399375105";

        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        g.drawString("Collisions: " + count, 10, 100 + height);
        g.drawString("Pi:         ", 10, 120 + height);
        g.setColor(Color.orange);
        g.drawString("            " + pi.substring(0, digits + 1), 10, 120 + height);
        g.setColor(Color.gray);
        g.drawString("            " + repeat(' ', digits + 1) + pi.substring(digits + 1), 10, 120 + height);
        g.drawString("Timesteps:  " + (int)(timeSteps * (1 / block1.distance(block2)) * digits), 10, 140 + height);
        g.drawString("            " + timeSteps, 10, 160 + height);

        g.dispose();
        bs.show();
    }

    private String repeat(char c, int repeat) {
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < repeat; i++) {
            st.append(c);
        }
        return st.toString();
    }

}
