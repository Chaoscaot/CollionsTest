package de.chaos.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.math.BigInteger;

public class Main extends Canvas {

    public Block block1;
    public Block block2;
    public static JFrame frame;

    public static Main mainInstance;


    static BigInteger count = BigInteger.ZERO;
    static int digits = 0;
    static long timeSteps = (long)Math.pow(10, digits);

    public static void main(String[] args) {
        mainInstance = new Main();
    }

    public Main() {
        restart();

        frame = new JFrame();
        frame.setTitle("Collisions PI");
        frame.setName("Collisions PI");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        frame.setSize(1000, 1000);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        frame.add(this);
        frame.validate();
        frame.setFocusTraversalKeysEnabled(false);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == '+') {
                    digits++;
                    Main.mainInstance.restart();
                } else if (e.getKeyChar() == '-') {
                    digits--;
                    if (digits < 0) {
                        digits = 0;
                    }
                    if (digits > 38) {
                        digits = 0;
                    }
                    Main.mainInstance.restart();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

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

    private boolean breakLoop = false;
    private long timeStepsForDraw = 0;

    private void restart() {
        count = BigInteger.ZERO;
        timeSteps = (long)Math.pow(10, digits);

        block1 = new Block( 100 , 20, 0, 1);
        block2 = new Block(block1.x + (20 + digits * 20) + 20, 20 + digits * 20, 1 , Math.pow(100, digits));
        breakLoop = true;
    }

    public void tick() {
        long t = (long)(timeSteps * (1 / block1.distance(block2)) * (digits + 1));
        if (t <= 0) {
            t = 1;
        }
        if (t > timeSteps) {
            t = timeSteps;
        }
        timeStepsForDraw = t;

        for (long i = 0; i < t; i++) {
            if (breakLoop) {
                breakLoop = false;
                break;
            }
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

        String s = formatNumber((long)(timeSteps * (1 / block1.distance(block2)) * (digits + 1)));
        String t = formatNumber(timeSteps);
        g.drawString("Timesteps:  " + repeat(' ', t.length() - s.length()) + s, 10, 140 + height);
        g.drawString("            " + t, 10, 160 + height);
        g.drawString("Digits:     " + digits, 10, 180 + height);

        g.dispose();
        bs.show();
    }

    private String repeat(char c, long repeat) {
        StringBuilder st = new StringBuilder();
        for (long i = 0; i < repeat; i++) {
            st.append(c);
        }
        return st.toString();
    }

    public static String formatNumber(long l) {
        return addCommas(l + "");
    }

    private static String addCommas(String s) {
        if (s.length() <= 1) {
            return s;
        }
        char[] chars = s.toCharArray();
        StringBuilder st = new StringBuilder();
        for (int i = chars.length - 1; i >= 0; i--) {
            st.append(chars[i]);
            if ((chars.length - i) % 3 == 0 && i != 0) {
                st.append(',');
            }
        }
        return st.reverse().toString();
    }

}
