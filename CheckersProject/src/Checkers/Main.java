package Checkers;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import api.ChipType;

public class Main extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel outputText;
	
    public static void main(String[] args) {
        JFrame window = new JFrame("Checkers in Java"); // GUI name
        Main content = new Main();
        window.setContentPane(content);
        window.pack();
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation( (screensize.width - window.getWidth()) / 2, (screensize.height - window.getHeight()) / 2 ); // places GUI in center of screen
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); //Ends program upon exit of GUI
        window.setVisible(true);
        window.setResizable(true);
    }
    
    public Main() {
        setLayout(null); 
        setPreferredSize( new Dimension(792,792) );
        setBackground(new Color(200,0,0));  //RGB back round color (NOT PIXEL SIZE)
        Board board = new Board();  
        add(board);
        add(outputText);
        board.setBounds(150,150,492,492);
        outputText.setBounds(85, 600, 600, 150); //placement of outputInfo (Pixels)
    }
    
    private static class CheckersMove {
   
        int oldRow;
        int oldColumn;  
        int newRow;
        int newColumn; 
        
        CheckersMove(int oldRow, int oldColumn, int newRow, int newColumn) {
            this.oldRow = oldRow;
            this.oldColumn = oldColumn;
            this.newRow = newRow;
            this.newColumn = newColumn;
        }
        
        boolean isJump() {  
            return (oldRow - newRow == 2 || oldRow - newRow == -2); // change in row and or colum is greater then 1 or less then -1
        }
    } 
    
    private class Board extends JPanel implements MouseListener {

		private static final long serialVersionUID = 1L; // default serial verision ID
		boolean gameInProgress; 
		int selectedRow;
		int selectedCol;   
        ChipType currentPlayer;      
        CheckersData board; 
        CheckersMove[] legalMoves;  
        
        Board() {
            addMouseListener(this);
            outputText = new JLabel("",JLabel.CENTER);
            outputText.setFont(new  Font("", Font.BOLD, 30));
            outputText.setForeground(Color.WHITE);
            board = new CheckersData();
            newGame();
        }
        
        void newGame() {
            board.setNewGame();   // Sets pieces on board 
            currentPlayer = CheckersData.RED;   // Red moves first
            legalMoves = board.getLegalMoves(CheckersData.RED);  // Get Red's legal moves
            selectedRow = -1;   // Red player has not yet selected a piece to move
            gameInProgress = true;
            repaint();
        }

        void gameOver(String str) {
            outputText.setText(str);
            gameInProgress = false;
        }

        void selectedPiece(int row, int col) {

            for (int i = 0; i < legalMoves.length; i++) {
                if (legalMoves[i].oldRow == row && legalMoves[i].oldColumn == col) {
                    selectedRow = row;
                    selectedCol = col;
                    repaint();
                    return;
                }
            }

            for (int i = 0; i < legalMoves.length; i++) {
                if (legalMoves[i].oldRow == selectedRow && legalMoves[i].oldColumn == selectedCol && legalMoves[i].newRow == row && legalMoves[i].newColumn == col) {
                    executeMove(legalMoves[i]);
                    return;
                }
            }
        }  
        
        void executeMove(CheckersMove move) {

            board.moveCheckersPiece(move);
            /*
             * for double jump scenario, method will be called and enter first if statment, then return and be called again for the 
             * next move but selected piece will still remain as piece just moved
             */
            if (move.isJump()) { // change in row and or colum is greater then 1 or less then -1 
                legalMoves = board.getLegalJumps(currentPlayer, move.newRow, move.newColumn);
                if (legalMoves != null) {
                    selectedRow = move.newRow;  
                    selectedCol = move.newColumn;
                    repaint();
                    return;
                }
            }

            if (currentPlayer == CheckersData.RED) {
                currentPlayer = CheckersData.BLACK;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null) { // no legal moves means all pieces are gone, or a piece is "blocked" in
                    gameOver("Red wins");
                }
            }else {
                currentPlayer = CheckersData.RED;
                legalMoves = board.getLegalMoves(currentPlayer);
                if (legalMoves == null) {
                    gameOver("Black wins");
                }
            }

            selectedRow = -1; // unselects square upon next move, same as newGame();
            repaint();
        }  
        
        public void paintComponent(Graphics g) {
            
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.black);
            g.fillRect(0,0,getSize().width,getSize().height); // covers up our repaint error

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                	
                    if ( row % 2 == col % 2 ) {   // Checkers board set up
                        g.setColor(Color.LIGHT_GRAY);
                    }else {
                        g.setColor(Color.GRAY);
                    }
                    
                    g.fillRect(6 + col * 60, 6 + row*60, 60, 60);
                    g.setFont(new Font("", Font.BOLD, 30)); // sets size and font for "K" painted on king
                   
                    if(board.pieceAt(row, col) == ChipType.RED){ // iterates through grid and repaints GUI after every move, selection, or new game
                        g.setColor(Color.RED);
                        g.fillOval(12 + col * 60, 12 + row * 60, 45, 45);
                    } else if(board.pieceAt(row, col) == ChipType.BLACK){
                        g.setColor(Color.BLACK);
                        g.fillOval(12 + col * 60, 12 + row * 60, 45, 45);
                    } else if(board.pieceAt(row, col) == ChipType.RED_KING){
                        g.setColor(Color.RED);
                        g.fillOval(12 + col * 60, 12 + row * 60, 45, 45);
                        g.setColor(Color.WHITE);
                        g.drawString("K", 23 + col * 60, 45 + row * 60);
                    } else if(board.pieceAt(row, col) == ChipType.BLACK_KING){
                        g.setColor(Color.BLACK);
                        g.fillOval(12 + col * 60, 12 + row * 60, 45, 45);
                        g.setColor(Color.WHITE);
                        g.drawString("K", 23 + col * 60, 45 + row * 60);
                    } 
          
                }
            }
            
            if (gameInProgress) {
                
                g.setColor(Color.cyan); // selectable/movable pieces
                
                for (int i = 0; i < legalMoves.length; i++) {
                    g.drawRect(6 + legalMoves[i].oldColumn * 60, 6 + legalMoves[i].oldRow * 60, 57, 57);
                    g.drawRect(9 + legalMoves[i].oldColumn * 60, 9 + legalMoves[i].oldRow * 60, 51, 51);
                }
               
                if (selectedRow >= 0) {
                    g.setColor(Color.white); // current selected piece
                    g.drawRect(6 + selectedCol * 60, 6 + selectedRow * 60, 57, 57);
                    g.drawRect(9 + selectedCol * 60, 9 + selectedRow * 60, 51, 51);
                    
                    g.setColor(Color.green); // viabale spaces to move to
                    
                    for (int i = 0; i < legalMoves.length; i++) {
                        if (legalMoves[i].oldColumn == selectedCol && legalMoves[i].oldRow == selectedRow) {
                            g.drawRect(6 + legalMoves[i].newColumn*60, 6 + legalMoves[i].newRow*60, 57, 57);
                            g.drawRect(9 + legalMoves[i].newColumn*60, 9 + legalMoves[i].newRow*60, 51, 51);
                        }
                    }
                }
                
            }

        }  
        
        public void mousePressed(MouseEvent evt) {
            if (gameInProgress == true) {
            	int col = (evt.getX() - 6) / 60;
                int row = (evt.getY() - 6) / 60;
                if (col >= 0 && col < 8 && row >= 0 && row < 8)
                    selectedPiece(row,col);
            }
        }
        
        public void mouseReleased(MouseEvent evt) { }
        public void mouseClicked(MouseEvent evt) { }
        public void mouseEntered(MouseEvent evt) { }
        public void mouseExited(MouseEvent evt) { }
    }
    
    private static class CheckersData {

    	static final ChipType EMPTY = ChipType.EMPTY;
    	static final ChipType RED = ChipType.RED;
    	static final ChipType RED_KING = ChipType.RED_KING;
    	static final ChipType BLACK = ChipType.BLACK;
    	static final ChipType BLACK_KING = ChipType.BLACK_KING;
    	ChipType[][] board; 
    	
    	CheckersData() {
    		board = new ChipType[8][8]; //board dimensions
    		setNewGame();
    	}
    	
    	void setNewGame() { // places pieces on ChipType 2d array board
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if ( row % 2 == col % 2 ) {
                        if (row < 3)
                            board[row][col] = BLACK;
                        else if (row > 4)
                            board[row][col] = RED;
                        else
                            board[row][col] = EMPTY;
                    }
                    else {
                        board[row][col] = EMPTY;
                    }
                }
            }
        }  
    	
        ChipType pieceAt(int row, int col) {
            return board[row][col];
        }

        void moveCheckersPiece(CheckersMove move) {
            movePiece(move.oldRow, move.oldColumn, move.newRow, move.newColumn);
        }

        void movePiece(int row, int column, int toRow, int toColumn) {
        	board[toRow][toColumn] = board[row][column]; // moves piece
            board[row][column] = EMPTY; // sets old space to empty
            
            if (row - toRow == 2 || row - toRow == -2) {
                int jumpRow = (row + toRow) / 2;  // Row of the jumped piece
                int jumpCol = (column + toColumn) / 2;  // Column of the jumped piece
                board[jumpRow][jumpCol] = EMPTY;
            }
            
            if (toRow == 0 && board[toRow][toColumn] == RED) { // sets standard red piece to king red piece if it makes it to the other side
                board[toRow][toColumn] = RED_KING;
            }
            if (toRow == 7 && board[toRow][toColumn] == BLACK) { // sets standard blakc piece to king black piece if it makes it to the other side
                board[toRow][toColumn] = BLACK_KING;
            }
        }

        CheckersMove[] getLegalMoves(ChipType currentPlayer) {

            if (currentPlayer != RED && currentPlayer != BLACK) {
                return null;
            }

            ChipType playerKing;  // The constant representing a King belonging to player
            
            if (currentPlayer == RED) { // sets currentPlayer as KING so canJump and canMove knows that piece can move any direction
                playerKing = RED_KING;  
            }else {
                playerKing = BLACK_KING;
            }

            ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // Moves will be stored in a list
            
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    if (board[row][col] == currentPlayer || board[row][col] == playerKing) { // if space isnt empty, but is the color of currentPlayer
                        if (canJump(currentPlayer, row, col, row + 1, col + 1, row + 2, col + 2)) // 4 possible jump situations
                            moves.add(new CheckersMove(row, col, row + 2, col + 2));
                        if (canJump(currentPlayer, row, col, row - 1, col + 1, row - 2, col + 2))
                            moves.add(new CheckersMove(row, col, row - 2, col + 2));
                        if (canJump(currentPlayer, row, col, row + 1, col - 1, row + 2, col - 2))
                            moves.add(new CheckersMove(row, col, row + 2, col - 2));
                        if (canJump(currentPlayer, row, col, row - 1, col - 1, row - 2, col - 2))
                            moves.add(new CheckersMove(row, col, row - 2, col - 2));
                    }
                }
            }
            
            for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        if (board[row][col] == currentPlayer || board[row][col] == playerKing) { // if space isnt empty, but is the color of currentPlayer
                            if (canMove(currentPlayer,row,col,row + 1,col + 1)) // 4 possible move situations
                                moves.add(new CheckersMove(row,col,row + 1,col + 1));
                            if (canMove(currentPlayer,row,col,row - 1,col + 1))
                                moves.add(new CheckersMove(row,col,row - 1,col + 1));
                            if (canMove(currentPlayer,row,col,row + 1,col - 1))
                                moves.add(new CheckersMove(row,col,row+1,col - 1));
                            if (canMove(currentPlayer,row,col,row - 1,col - 1))
                                moves.add(new CheckersMove(row,col,row - 1,col - 1));
                        }
                    }
            }
           
            if (moves.size() == 0) { // // no legal moves means all pieces are gone, or a piece is "blocked" in
                return null;
            }else {
                CheckersMove[] moveArray = new CheckersMove[moves.size()];
                for (int i = 0; i < moves.size(); i++) {
                    moveArray[i] = moves.get(i);
                }
                return moveArray;
            }

        }  
        CheckersMove[] getLegalJumps(ChipType currentPlayer, int row, int col) { // repeat method of getLegalMoves() but only for jumps, allows for double jump rule
        	
            if (currentPlayer != RED && currentPlayer != BLACK) {
                return null;
            }
            
            ChipType playerKing;  // The constant representing a King belonging to player.
            
            if (currentPlayer == RED) { // sets currentPlayer as KING so canJump knows it can move any direction
                playerKing = RED_KING;
            }else {
                playerKing = BLACK_KING;
            }
            
            ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();  // The legal jumps will be stored in this list.
            
            if (board[row][col] == currentPlayer || board[row][col] == playerKing) { // 4 possible jump situations
                if (canJump(currentPlayer, row, col, row + 1, col + 1, row + 2, col + 2))
                    moves.add(new CheckersMove(row, col, row + 2, col + 2));
                if (canJump(currentPlayer, row, col, row - 1, col + 1, row - 2, col + 2))
                    moves.add(new CheckersMove(row, col, row - 2, col + 2));
                if (canJump(currentPlayer, row, col, row + 1, col - 1, row + 2, col - 2))
                    moves.add(new CheckersMove(row, col, row + 2, col - 2));
                if (canJump(currentPlayer, row, col, row - 1, col - 1, row - 2, col - 2))
                    moves.add(new CheckersMove(row, col, row - 2, col - 2));
            }
            
            if (moves.size() == 0) {
                return null;
            }else {
                CheckersMove[] moveArray = new CheckersMove[moves.size()];
                for (int i = 0; i < moves.size(); i++) { // ArrayList to array
                    moveArray[i] = moves.get(i); 
                }
                return moveArray;
            }
        }  
        
        private boolean canJump(ChipType currentPlayer, int row, int column, int rowAjacent, int columnAjacent, int rowJump, int columnJump) {

            if (rowJump < 0 || rowJump > 7 || columnJump < 0 || columnJump > 7 || board[rowJump][columnJump] != EMPTY) {
            	return false;  // ajacent square is off board or already containts a piece
            }
                 
            if (currentPlayer == RED) {
                if (board[row][column] == RED && rowJump > row) {
                    return false;  // Regular red piece can only move up
                }
                else if (board[rowAjacent][columnAjacent] != BLACK && board[rowAjacent][columnAjacent] != BLACK_KING) {
                    return false;  // There is no black piece to jump
                }else {  
                	return true;  // jump is legal
                }
            }
            else {
                if (board[row][column] == BLACK && rowJump < row) {
                    return false;  // Regular black piece can only move down
                }
                if (board[rowAjacent][columnAjacent] != RED && board[rowAjacent][columnAjacent] != RED_KING) {
                    return false;  // no red piece to jump
                }else {
                	return true;  // jump is legal
                }
            }

        }  

        private boolean canMove(ChipType currentPlayer, int row, int column, int rowAjacent, int columnAjacent) {

            if (rowAjacent < 0 || rowAjacent > 7 || columnAjacent < 0 || columnAjacent > 7 || board[rowAjacent][columnAjacent] != EMPTY) {
                return false;  // ajacent square is off board or already containts a piece
            }
            
            if (currentPlayer == RED) {
                if (board[row][column] == RED && rowAjacent > row) {
                    return false;  // non-king red piece can only move down
                }else {
                    return true;  // move is legal
                }
            }
            else {
                if (board[row][column] == BLACK && rowAjacent < row) {
                    return false;  // non-king black piece can only move up.
                }else {
                	return true;  // move is legal
                }
            }
        } 
    }

}

