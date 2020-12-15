package com.gametutorial;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int)(GAME_WIDTH*(5.0d/9));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    public GamePanel(){
        newPaddles();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new EventListener());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall(){
        random = new Random();
        int xBias = random.nextInt(100) - 100;
        int yBias = random.nextInt(100) - 100;

        ball = new Ball((GAME_WIDTH - BALL_DIAMETER)/2 + xBias, (GAME_HEIGHT - BALL_DIAMETER)/2 + yBias,
                BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddles(){
        paddle1 = new Paddle(0, (GAME_HEIGHT - PADDLE_HEIGHT)/2, PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT - PADDLE_HEIGHT)/2,
                PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g){
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0,0, this);
    }

    public void draw(Graphics g){
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move(){
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    private void increaseSpeed(){
        boolean xPositive = (ball.xVelocity > 0);
        boolean yPositive = (ball.yVelocity > 0);

        if (xPositive)
            ball.xVelocity++;
        else
            ball.xVelocity--;

        if (yPositive)
            ball.yVelocity++;
        else
            ball.yVelocity--;
    }

    public void checkCollision(){
        //bounce ball off top & bottom edges
        if (ball.y<=0) ball.setYDirection(-ball.yVelocity);
        if (ball.y>=GAME_HEIGHT - BALL_DIAMETER) ball.setYDirection(-ball.yVelocity);

        //bounce paddles
        if (ball.intersects(paddle1)){
            ball.xVelocity = Math.abs(ball.xVelocity);
        }

        if (ball.intersects(paddle2)){
            ball.xVelocity = -Math.abs(ball.xVelocity);
        }

        //stops paddles at the window edges
        if (paddle1.y<=0) paddle1.y = 0;
        if (paddle1.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;

        if (paddle2.y<=0) paddle2.y = 0;
        if (paddle2.y >= GAME_HEIGHT - PADDLE_HEIGHT) paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

        //set player scores
        boolean gameOver = (ball.x < 0)||(ball.x > GAME_WIDTH);
        if (ball.x < 0) score.player2++;
        if (ball.x > GAME_WIDTH) score.player1++;

        if (gameOver) {
            newBall();
            newPaddles();
//            System.out.println(String.format("Player 1: %s   Player2:%s", score.player1, score.player2));
        }
    }

    public void run(){
        //basic game loop
        long lastTime= System.nanoTime();
        double amountOfTicks = 60d;
        double ns = 1_000_000_000d/amountOfTicks;
        double delta = 0;

        while(true){
            long now = System.nanoTime();
            delta += (now - lastTime)/ns;
            lastTime = now;
            if (delta >= 1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }

    }

    public class EventListener extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }

}
