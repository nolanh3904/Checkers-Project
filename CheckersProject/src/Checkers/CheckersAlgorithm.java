package Checkers;

import api.ChipType;

public class CheckersAlgorithm {

	private ChipType chipColor[][] = new ChipType[8][8];
	private boolean king_Status[][] = new boolean[8][8];
	
	public CheckersAlgorithm() { //starts the game
		
		int x_Cord;
		int y_Cord;
		int count = 0;
		
		for(x_Cord = 0; x_Cord < 8; x_Cord++) {
			   for(y_Cord = 0; y_Cord < 8; y_Cord++){
				   king_Status[x_Cord][y_Cord] = false;	
			   }
		}
		
		for(x_Cord = 0; x_Cord < 8; x_Cord++) {
			   for(y_Cord = 0; y_Cord < 3; y_Cord++){
				   if(count % 2 != 0) {
					   chipColor[x_Cord][y_Cord] = ChipType.BLACK;
				   }else {
					   chipColor[x_Cord][y_Cord] = ChipType.EMPTY;
				   }
				   		count++;
			   }
		}
		count = 0;
		for(x_Cord = 0; x_Cord < 8; x_Cord++) {
			   for(y_Cord = 5; y_Cord < 8; y_Cord++){
				   if(count % 2 == 0) {
					   chipColor[x_Cord][y_Cord] = ChipType.RED;
				   }else {
					   chipColor[x_Cord][y_Cord] = ChipType.EMPTY;
				   }
				   		count++;
			   }
		}
	}
	
	public ChipType getColor(int x, int y) {
		x = x / 64;
		y = y / 64 ;
		return chipColor[x][y];
	}
	
	
}

