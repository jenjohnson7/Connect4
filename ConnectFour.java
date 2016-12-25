import java.awt.Color;
import java.util.ArrayList;

/** 
 * Class to play a game of Connect 4.
 * Computer plays black, human plays white.
 */
public class ConnectFour {

	//ArrayList three and two for storing possible moves found by the pre-heuristic search function
	public static ArrayList<Integer> three = new ArrayList<Integer>();
	public static ArrayList<Integer> two = new ArrayList<Integer>();
	public static boolean gameOver = false;
	
	//Booleans used by the pre-heuristic search function
	public static boolean threeInARowComp = false;
	public static boolean threeInARowHum = false;
	public static boolean twoInARowComp = false;
	public static boolean twoInARowHum = false;

	//stores the best column for a 3-in-a-row move and a 2-in-a-row move
	public static int bestThreeInCol = -1;
	public static int bestTwoInCol = -1;
	
    /** Number of columns on the board. */
    public static final int COLUMNS = 7;

    /** Number of rows on the board. */
    public static final int ROWS = 6;
    
    /** Color for computer player's pieces */
    public static final Color COMPUTER = Color.BLACK;

    /** Color for human player's pieces */
    public static final Color HUMAN = Color.RED;

    /** Color for blank spaces. */
    public static final Color NONE = Color.WHITE;


    /**
     * Drops a piece of given Color in column.  Modifies the board
     * array. Assumes the move is legal.
     *
     * @param board The game board.
     * @param color Color of piece to drop.
     * @param column Column in which to drop the piece.
     */
    public static void dropPiece(Color[][] board, Color color, int column) {
    	for (int row = 0; row < ROWS ; row ++) {
    		if( board [ row ][ column ] == NONE ) {
    			board [row ][ column ] = color ;
    			break ;
    		}
    	}		
    }

    /** 
     * Checks if the board is full.
     * @param board The game board.
     * @return True if board is full, false if not.
     */
    public static boolean isFull(Color[][] board) {
    	for (int column = 0; column < COLUMNS ; column ++) {
    		if( board [ROWS -1][ column ] == NONE ) return false ;
    	}
    	return true ;
    }
    
    /** 
     * Checks if dropping a piece into the given column would be a
     * legal move.
     *
     * @param board The game board.
     * @param column The column to check.
     * @return true if column is neither off the edge of the board nor full.
     */
    public static boolean isLegal(Color[][] board, int column) {
    	return column >= 0 &&
    			column < COLUMNS &&
    			board [ROWS -1][ column ] == NONE ;
    }

    /**
     * Given the color of a player, return the color for the opponent.
     * Returns human player color when given computer player color.
     * Returns computer player color when given human player color.
     * @param color Player color.
     * @return Opponent color.
     */
    public static Color getOpposite(Color color) {
    	if( color == HUMAN ) return COMPUTER ;
    	else return HUMAN ;
    }

    /**
     * Check for a win starting at a given location and heading in a
     * given direction.
     * 
     * Returns the Color of the player with four in a row starting at
     * row r, column c and advancing rowOffset, colOffset each step.
     * Returns NONE if no player has four in a row here, or if there
     * aren't four spaces starting here.
     * 
     * For example, if rowOffset is 1 and colOffset is 0, we would
     * advance the row index by 1 each step, checking for a vertical
     * win. Similarly, if rowOffset is 0 and colOffset is 1, we would
     * check for a horizonal win.
     * @param board The game board.
     * @param r Row index of where win check starts
     * @param c Column index of where win check starts
     * @param rowOffset How much to move row index at each step
     * @param colOffset How much to move column index at each step
     * @return Color of winner from given location in given direction
     *         or NONE if no winner there.
     */
    public static Color findLocalWinner(Color[][] board, int r, int c,
                                        int rowOffset, int colOffset) {
    	Color color = board [r][c];
    	for (int i = 0; i < 4; i++) {
    		int row = r + i* rowOffset ;
    		int col = c + i* colOffset ;
    		
    		if( row < 0 || row >= ROWS ||
    			col < 0 || col >= COLUMNS ||
    			board [row ][ col] != color ) {
    			color = NONE ;
    			break ;
    		}
    	}
    	return color ;
        
    }

    
    public static double calculatepower(double n){
    	return Math.pow(4, n);	
    }
        
    
    public static boolean validBounds(Color[][]board, int row, int col){
    	if (row >= ROWS || row < 0){
    		return false;
    	}
    	if (col >= COLUMNS || col < 0){
    		return false;
    	}
    	if (board[row][col] != NONE){
    		return false;
    	}
    	return true;
    }
    

    public static double findLocalWinnerFor2ndHeuristic(Color[][] board, int r, int c, int rowOffset, int colOffset) {
    	
    	double result = 0;
    	
    	Color color = board [r][c];
    	
    	for (int i = 0; i < 3; i++) {
    		int row = r + i* rowOffset ;
    		int col = c + i* colOffset ;


    		if( row < 0 || row >= ROWS ||
    			col < 0 || col >= COLUMNS ||
    			board [row ][ col] != color ) {
    			
    			color = NONE ;
    			break ;
    		}
    		if (i==0 && validBounds(board, r+ (i+1)*rowOffset, c + (i+1)*colOffset)){ //one in a row
    			if (color == COMPUTER){
    				result = calculatepower(1);
    			}
    			else if (color == HUMAN){
    				result = calculatepower(1) * (-2);
    			}
    		}
    		
    		if (i == 1 && validBounds(board, r+ (i+1)*rowOffset, c + (i+1)*colOffset)){ //two in a row
    			if (color == COMPUTER){
    				result = calculatepower(2);
    			}
    			else if (color == HUMAN){
    				result = calculatepower(2) * (-2);
    			}
    			
    		}
    		
    		if ( i==2 && validBounds(board, r+ (i+1)*rowOffset, c + (i+1)*colOffset)){ //three in a row 
    			if (color == COMPUTER){
    				result = calculatepower(3);
    			}
    			else if (color == HUMAN){
    				result = calculatepower(3) * (-2);
    			}
    		}
    		
    		if (i == 3 && validBounds(board, r+ (i+1)*rowOffset, c + (i+1)*colOffset)){ //four in a row
    			if (color == COMPUTER){
    				result = calculatepower(4);
    			}
    			else if (color == HUMAN){
    				result = calculatepower(4) * (-2);
    			}
    		}
    		
    	}

    	return result;

    }
    

    public static double findWinnerFor2ndHeuristic(Color[][] board) {

    	double result = 0;

    	
    	for (int row = 0; row < ROWS ; row ++) {
    		for (int col = 0; col < COLUMNS ; col ++) {
    			
    			result += findLocalWinnerFor2ndHeuristic (board , row , col , 0, 1);
		
    			result += findLocalWinnerFor2ndHeuristic (board , row , col , 1, 0); //up
    				
    			result+=findLocalWinnerFor2ndHeuristic (board , row , col , 1, 1); //up diagonal

    			result +=	findLocalWinnerFor2ndHeuristic (board , row , col , -1, 1); //down diagonal
				
    		}				
    	}
					
    	return result;	
      
    }

    /**
     * Checks entire board for a win (4 in a row).
     * 
     * @param board The game board.
     * @return color (HUMAN or COMPUTER) of the winner, or NONE if no
     * winner yet.
     */
    public static Color findWinner(Color[][] board) {
    	
    	Color winner = NONE ;
    	for (int row = 0; row < ROWS ; row ++) {
    		for (int col = 0; col < COLUMNS ; col ++) {
    			
    			//the offsets (last 2 parameters) determine whether the horizontal, vertical, up diagonal, or down diagonal is being checked
    			
    			if( winner == NONE ) {
    				winner = findLocalWinner (board , row , col , 0, 1);
    			}
    			
    			if( winner == NONE ) {
    				winner = findLocalWinner (board , row , col , 1, 0);
    			}
 
    			if( winner == NONE ) {
    				winner = findLocalWinner (board , row , col , 1, 1);
    			}
    			
    			if( winner == NONE ) {
    				winner = findLocalWinner (board , row , col , -1, 1);
    			}
    			
    		}
    	}
    	return winner ;
 
    }

    /**
     * Returns computer player's best move.
     * @param board The game board.
     * @param maxDepth Maximum search depth.
     * @return Column index for computer player's best move.
     */

        public static int bestMoveForComputer(Color[][] board, int maxDepth, int aa) {

        //initialize alpha and beta values for pruning
    	double alpha = -100000;
    	double beta = 1000000;
       
    	int bestCol = -1;
    	double bestResult =  Double.NEGATIVE_INFINITY;
    	for (int c = 0; c < COLUMNS ; c++) {
    		if ( isLegal (board , c)) {
    			dropPiece(board , COMPUTER , c);
    			
    			Color winner = findWinner ( board );
    	    	double result; 
    	    	
    			if( winner == COMPUTER ){
    	    		result = Double.POSITIVE_INFINITY;
    	    		return c;
    	    		
    	    	}
    	    	else if( winner == HUMAN ){
    	    		
    	    		result = Double.NEGATIVE_INFINITY;
    	    	}
    	    	else{
    	    		result = min(board , maxDepth , 0, alpha, beta);
    	    		//recursively call min
    	    	}
    			
    			
    			undoDrop (board , c);
    			if ( result > bestResult ) {
    				bestResult = result ;
    				bestCol = c;
    	
    			}
    			//alpha-beta pruning
    			if(result > alpha) {
 				   alpha = result; 
 				}

    		}
    		
    	}
 
    	return bestCol ;
    }

    /**
     * Returns the value of board with computer to move:
     *     1 if computer can force a win,
     *     -1 if computer cannot avoid a loss,
     *     0 otherwise.
     * 
     *  used in mutually recursive minimax algorithm
     * 
     * @param board The game board.
     * @param maxDepth Maximum search depth.
     * @param depth Current search depth.
     */
    public static double max(Color[][] board, int maxDepth, int depth, double alpha, double beta) {
  
    	Color winner = findWinner ( board );
    	if( winner == COMPUTER ){
    		return Double.POSITIVE_INFINITY;
    	}
   	else if( winner == HUMAN ){
    		return Double.NEGATIVE_INFINITY;
    	}
  
    	if( isFull ( board ) || ( depth == maxDepth )){
        	double r = findWinnerFor2ndHeuristic(board);
        	return r;
    	}
    	
    	
    	else {
    		double bestResult = Double.NEGATIVE_INFINITY;
    		for (int c = 0; c < COLUMNS ; c++) {
    			if ( isLegal (board , c)) {
    				dropPiece(board , COMPUTER , c);
    				double result = min (board , maxDepth , depth + 1, alpha, beta);
    				undoDrop (board , c);
    				if ( result > bestResult ){ 
    					bestResult = result ;
    				}
    				
    	//alpha-beta pruning
    				if(result > alpha) {
    					  alpha = result; 
    				}
    				if(alpha >= beta) {
    				   return alpha;
    				}
    			}
    			
    		}

    	return bestResult ;
    	}
        
    }

    /**
     * Returns the value of board with human to move: 
     *    1 if human cannot avoid a loss,
     *    -1 if human can force a win,
     *     0 otherwise.
     * 
     * used in mutually recursive minimax algorithm
     * 
     * @param board The game board.
     * @param maxDepth Maximum search depth.
     * @param depth Current search depth.
     */
    public static double min(Color[][] board, int maxDepth, int depth, double alpha, double beta) {

        // First, see if anyone is winning already
        Color winner = findWinner(board);
        if (winner == COMPUTER) {
            // computer is winning, so human is stuck
        	return Double.POSITIVE_INFINITY;
            
        } else if (winner == HUMAN) {
            // human already won, no chance for computer
            return Double.NEGATIVE_INFINITY;
       } 
    if (isFull(board) || (depth == maxDepth)) {
            // We either have a tie (full board) or we've searched as
            // far as we can go. Either way, call it a draw.
        	double r = findWinnerFor2ndHeuristic(board);
        	return r;

        } else {
            // At this point, we know there isn't a winner already and
            // that there must be at least one column still available
            // for play. We'll search all possible moves for the human
            // player and decide which one gives the lowest (best for
            // human) score, assuming that the computer would play
            // perfectly.

            // Start off with a value for best result that is larger
            // than any possible result.
            double bestResult = Double.POSITIVE_INFINITY;

            // Loop over all columns to test them in turn.
            for (int c = 0; c < COLUMNS; c++) {
                if (isLegal(board, c)) {
                    // This column is a legal move. We'll drop a piece
                    // there so we can see how good it is.
                    dropPiece(board, HUMAN, c);
                    // Call max to see what the value would be for the
                    // computer's best play. The max method will end
                    // up calling min in a similar fashion in order to
                    // figure out the best result for the computer's
                    // turn, assuming the human will play perfectly in
                    // response.
                    double result = max(board, maxDepth, depth + 1, alpha, beta);
                    // Now that we have the result, undo the drop so
                    // the board will be like it was before.
                    undoDrop(board, c);

                    if (result <= bestResult) {
                        // We've found a new best score. Remember it.
                        bestResult = result;

            //Alpha-beta pruning
                    }
                    if(result < beta) {
     				   beta = result;
     				}
     				if(alpha >= beta) {
     				   return beta;
     			    }
                }
              
            }
            
            return bestResult;
        }
    }


    /**
     * Removes the top piece from column. Modifies board. Assumes
     * column is not empty.
     * @param board The game board.
     * @param column Column with piece to remove.
     */
    public static void undoDrop(Color[][] board, int column) {
        // We'll start at the top and loop down the column until we
        // find a row with a piece in it. 
        int row = ROWS - 1;
        while(board[row][column] == NONE && row > 0) {
            row--;
        }

        // Set the top row that had a piece to empty again.
        board[row][column] = NONE;
    }

    /** Creates board array and starts game GUI. */
    public static void main(String[] args) {
        // create array for game board
    	
        Color[][] board = new Color[ROWS][COLUMNS];
        // fill board with empty spaces
        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = NONE;
            }
        }
        
        int n = 0;
        for (int i=0; i<n; i++){

        	ConnectFourGUI.showGUI(board, HUMAN, 5);
        }
        // show the GUI and start the game with depth 5
        ConnectFourGUI.showGUI(board, HUMAN, 5);
    }

}