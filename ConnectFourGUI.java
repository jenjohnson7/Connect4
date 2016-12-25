import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

/**
 * This is the graphical user interface for the Connect 4 game.
 */
public class ConnectFourGUI {

    public static final int ROWS = ConnectFour.ROWS;
    public static final int COLUMNS = ConnectFour.COLUMNS;

    public static final Color COMPUTER = ConnectFour.COMPUTER;
    public static final Color HUMAN = ConnectFour.HUMAN;
    public static final Color NONE = ConnectFour.NONE;
    public static final Color BOARD_COLOR = Color.YELLOW;

    /**
     * Construct and display GUI for a Connect Four game.
     */
    public static void showGUI(final Color[][] board, 
                               final Color firstPlayer,
                               final int depth) {
        // For thread safety, invoke GUI code on event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ConnectFourGUI gui =
                        new ConnectFourGUI(board, firstPlayer, depth);
                    gui.startGame();
                }
            });
    }

    /**
     * Draw the game board.
     */
    private class BoardPanel extends JPanel {

        public BoardPanel() {
            setBackground(BOARD_COLOR);
        }

        public int getRowHeight() {
            return getHeight() / ConnectFour.ROWS;
        }

        public int getColumnWidth() {
            return getWidth() / ConnectFour.COLUMNS;
        }

        @Override
        public void paintComponent( Graphics g ) {
            super.paintComponent(g);

            int rowHeight = getRowHeight();
            int colWidth = getColumnWidth();
            int rowOffset = rowHeight / 8;
            int colOffset = colWidth / 8;

            for(int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    // Flipping columns vertically so row 0 will be at bottom.
                    g.setColor(board[ROWS-row-1][col]);
                    g.fillOval( col * colWidth + colOffset,
                                row * rowHeight + rowOffset,
                                colWidth - 2*colOffset,
                                rowHeight - 2*rowOffset);
                }
            }
        }
    }

    /** Array for the game board. */
    private Color[][] board;
    /** Which player's turn is it? */
    private Color currentPlayer;
    /** How many moves ahead will the computer player search? */
    private int depth ;

    // GUI components
    private final JFrame boardFrame;
    private final BoardPanel boardPanel;
    private final JLabel statusLabel;

    /** 
     * Handles a mouse click on the board.
     * @param x X position of the click
     * @param y Y position of the click
     */ 
    private void doMouseClick(int x, int y) {
        int column = x / boardPanel.getColumnWidth();
                
        if( currentPlayer == HUMAN ) {
            if( ConnectFour.isLegal(board, column) ) {
                dropPiece(column);
            } else {
                System.out.println("Human attempted illegal move at column " + column);
            }
        } else {
            System.out.println("Ignoring click on computer's turn");
        }

    }

    /**
     * Drop a piece for the current player in the given column and
     * update for next player's turn.
     * @param column Column where piece should be dropped.
     */
    private void dropPiece(int column) {
        ConnectFour.dropPiece(board, currentPlayer,  column);
        currentPlayer = ConnectFour.getOpposite(currentPlayer);
        boardFrame.repaint();

        checkForWin();
    }

    /**
     * Have the computer make a move.
     */
    private void computerTurn() {
        if(currentPlayer == COMPUTER) {
            // Computer player may take a while so use worker thread
            // to think in the background instead of causing the GUI
            // to lock up.
            SwingWorker<Integer, ?> worker = new SwingWorker<Integer, Object>() {
                @Override
                public Integer doInBackground() {
                    // Make a copy of board for computer to play with
                    // so we won't refresh the GUI with the computer's
                    // thoughts.
                    Color[][] boardCopy = new Color[ROWS][COLUMNS];
                    for(int i = 0; i < board.length; i++) {
                        boardCopy[i] = java.util.Arrays.copyOf(board[i], board[i].length);
                    }
                    int alpha = 0;
                    
                    //run minimax algorithm with max depth 5 
                    
                    return ConnectFour.bestMoveForComputer(boardCopy, 5, alpha);
                    
                    
                    /* This is the preheuristic. 
                     * Store column numbers to block 3-in-a-rows in ArrayList three
                     * Store column numbers to block 2-in-a-rows in ArrayList two
                     * 
                    ConnectFour.three = new ArrayList<Integer>();
                    ConnectFour.two = new ArrayList<Integer>();
                    
                    //call findWinner2, the heuristic function, on the board
                    
                    ConnectFour.findWinner2(boardCopy);

                    Random r = new Random();
                     
                   //if a 3-in-a-row move is available, do that
                    * if more than 1 3-in-a-row move is available, choose randomly from these moves
                    if (ConnectFour.bestThreeInCol != -1){
                    
                    	int len = ConnectFour.three.size();
                    	
                    	if (len == 0){
                    		return ConnectFour.bestThreeInCol;
                    	}
                    	else{
                    		
	                    	int index = r.nextInt(len);
	                    	return ConnectFour.three.get(index);
                    	}
                    }
                    
                    //if no 3-in-a-row move availble, check for 2-in-a-row moves
                     * if a 2-in-a-row move is available, do that
                     * if more than 1 2-in-a-row move is available, choose randomly from these moves
                    
                    else if (ConnectFour.bestTwoInCol != -1){
                    	
                    	int len = ConnectFour.two.size();
                    	if (len == 0){
                    		return ConnectFour.bestTwoInCol;
                    	}
                    	else{
	                    	int index = r.nextInt(len);
	                    	return ConnectFour.two.get(index);
                    	}
                    }
                    else{
                    	int len3 = ConnectFour.three.size();
                    	int len2 = ConnectFour.two.size();
                    	
                    	if (len3 != 0){
                    		int index = r.nextInt(len3);
	                    	return ConnectFour.three.get(index);
                    	}
                    	else if (len2 != 0){
                    		int index = r.nextInt(len2);
	                    	return ConnectFour.two.get(index);
                    	}
                    	
                    	//if no 3 or 2-in-a-row moves, search for the best move using minimax algorithm
                    	
                    	else{
                    	 return ConnectFour.bestMoveForComputer(boardCopy, depth, alpha);
                    	}
                    }
                    	
                   */
                }

                @Override
                protected void done() {
                    try {
                        int column = get();
                        if( ConnectFour.isLegal(board, column) ) {
                            dropPiece(column);
                        } else {
                            // Shouldn't happen if ConnectFour methods
                            // are valid, but complain just in case.
                            System.out.println("Computer attempted illegal move at column " + column);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            worker.execute();
        } else {
            System.out.println("Ignoring attempted computer play on human's turn.");
        }
    }

    /**
     * See if anyone has won and announce it if they have.
     */
    private void checkForWin() {
        statusLabel.setText("Checking for win...");

        SwingWorker<Color, ?> worker = new SwingWorker<Color, Object>() {
            @Override
            public Color doInBackground() {
                return ConnectFour.findWinner(board);
            }

            @Override
            protected void done() {
                Color winner = null;
                try {
                    winner = get();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                boolean gameOver = true;
                if( winner == HUMAN ) {
                    statusLabel.setText("Game over: Human Wins!");
                    JOptionPane.showMessageDialog ( null, "Human Wins!", "Game Over",
                                                    JOptionPane.INFORMATION_MESSAGE );
                } else if( winner == COMPUTER ) {
                    statusLabel.setText("Game over: Computer Wins!");
                    JOptionPane.showMessageDialog ( null, "Computer Wins!", "Game Over",
                                                    JOptionPane.INFORMATION_MESSAGE );
                } else if (ConnectFour.isFull(board)) {
                    statusLabel.setText("Game over: Draw");
                    JOptionPane.showMessageDialog ( null, "Draw Game!", "Game Over",
                                                    JOptionPane.INFORMATION_MESSAGE );
                } else {
                    gameOver = false;
                    updateStatusLabel();
                }

                if(gameOver) System.exit(0);
                else if(currentPlayer == COMPUTER) computerTurn();
            }
        };
        worker.execute();
    }

    /** Update label under board to say whose turn it is. */
    private void updateStatusLabel() {
        if(currentPlayer == HUMAN) {
            statusLabel.setText("Human player's turn");
        } else if(currentPlayer == COMPUTER) {
            statusLabel.setText("Computer player's turn");
        } else {
            // shouldn't happen, but just in case...
            statusLabel.setText("UNKNOWN STATUS");
        }
    }
    
    /**
     * Constructor for the GUI.
     */
    ConnectFourGUI(Color[][] board, Color player, int depth) {
        this.board = board;
        this.currentPlayer = player;
        this.depth = depth;
        boardFrame = new JFrame();
        boardFrame.setTitle("Connect Four");

        boardPanel = new BoardPanel();
        boardPanel.setPreferredSize( new Dimension(700, 600) );
        boardPanel.addMouseListener( new MouseAdapter() {
                @Override
                public void mouseClicked( MouseEvent e ) {
                    int x = e.getX();
                    int y = e.getY();
                    doMouseClick(x, y);
                }
            });

        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel();
        statusPanel.add(statusLabel);
        updateStatusLabel();

        boardFrame.add(boardPanel, BorderLayout.CENTER);
        boardFrame.add(statusPanel, BorderLayout.PAGE_END);

        boardFrame.pack();
        boardFrame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        boardFrame.setLocationRelativeTo ( null );
        //boardFrame.setResizable ( false );
        boardFrame.setVisible ( true ); 
    }

    /** Kick off the connect 4 game */
    void startGame() {
        // Not likely that we were handed a non-empty board, but check anyway
        checkForWin();              
    }

}