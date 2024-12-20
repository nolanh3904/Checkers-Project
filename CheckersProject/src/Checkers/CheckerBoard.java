package Checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;

//import api.ChipType;

public class CheckerBoard extends JPanel implements MouseListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame;
	boolean test;
	public CheckerBoard(){
	}
	public void paint(Graphics g){
		int count= 0;
	
		
		for(int x = 0; x < 512; x+= 64) {
			for(int y = 0; y < 512; y+=64) {
				if(count % 2 != 0){
					g.setColor(Color.BLUE);
					g.fillRect(x, y, 64, 64);
					if(y <= 128 || y >= 320) {
						g.setColor(Color.BLACK);
						g.fillOval(x+8, y+8, 48, 48);
						
					}
					if(y >= 320) {
						g.setColor(Color.RED);
						g.fillOval(x+8, y+8, 48, 48);
					}
				}
				count++;
			}
			count++;
		}
		
		
		
		if(test) {
			g.setColor(Color.BLACK);
			g.fillOval(getX()+8, getY()+8, 48, 48);
			System.out.println("test");
		}
		
	}
	
	public void mouseClicked(MouseEvent e) {
		CheckersAlgorithm game  = new CheckersAlgorithm();
		//System.out.println("mouse clicked at x = " + e.getX() + " y = " + e.getY());
		System.out.println(game.getColor(e.getX(), e.getY()));
		//boolean test = true;
		repaint();	
	}

	public void mousePressed(MouseEvent e) {
	
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}
	
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
}


