package Checkers;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class CheckersGraphics extends Canvas{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public static void main(String[] args) {
		
	   JFrame frame = new JFrame("My Drawing");
       Canvas canvas = new CheckersGraphics();
       canvas.setSize(512, 512);
       frame.add(canvas);
       frame.pack();
       frame.setVisible(true);
   }

	int x = 0;
	int y = 0;
	int count = 0;

   public void paint(Graphics g) {
	   
	   for(x = 0; x <= 512; x += 64) {
		   for(y = 0; y <= 512; y += 64){
			   if(count % 2 != 0) {
				   g.setColor(Color.PINK);
				   g.fillRect(x, y, 64, 64);
			   }
			   		count++;
		   }
	   }
   }
}


