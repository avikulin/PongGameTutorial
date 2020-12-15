package com.gametutorial;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class Score extends Rectangle {
    static int GAME_WIDTH;
    static int GAME_HEIGHT;
    int player1;
    int player2;

    public Score(int GAME_WIDTH, int GAME_HEIGHT){
        Score.GAME_WIDTH = GAME_WIDTH;
        Score.GAME_HEIGHT = GAME_HEIGHT;

    }

    public void draw(Graphics g){
        g.setColor(Color.YELLOW);
        g.setFont( new Font("Consolas", Font.PLAIN, 60));
        g.drawLine(GAME_WIDTH/2, 0, GAME_WIDTH/2, GAME_HEIGHT);
        g.drawArc(GAME_WIDTH/2-100, GAME_HEIGHT/2-100, 200,200, 0, 360);

        g.setColor(Color.GREEN);
        g.drawString("Игрок А", 50,50);
        g.drawString("Игрок Б", GAME_WIDTH-220-50,50);

        g.drawString(String.format("%02d",player1), (GAME_WIDTH/2)-85,50);
        g.drawString(String.format("%02d",player2), (GAME_WIDTH/2)+20,50);
    }
}
