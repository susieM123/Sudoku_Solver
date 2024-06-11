/* Author: Susie Mueller
 * Purpose: Project 4
 * Date: 10/15/23
 * File: Sudoku.java
 * Notes on how to complile and run at the bottom of the file
 */


// Libraries used.
import java.util.Stack;
import java.lang.Thread;

 // The primary purpose of the Sudoku class is to solve Sudoku puzzles.
public class Sudoku {

    private Board board;
    private LandscapeDisplay ld;


    // Constructor creates new Board w/ some pre-determined randomly placed values.
    public Sudoku(int numInitialCells) {

        board = new Board(numInitialCells);
        // ld = new LandscapeDisplay(board);
    }


    // Locates and returns next value for a Cell
    public int findNextValue(Cell cell) {

        int row = cell.getRow(); 
        int col = cell.getCol();
        int currValue = cell.getValue();

        // Searches for valid values from current value + 1
        for (int value = currValue + 1; value <= Board.SIZE; value++) {
            if (board.validValue(row, col, value)) {
                return value;
            }
        }

        return 0; // No valid value found so mark as 0
    }


    // Locates and returns next cell
    public Cell findNextCell() {

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = board.get(row, col);
                if (cell.getValue() == 0) {
                    int nextValue = findNextValue(cell);
                    if (nextValue != 0) {
                        cell.setValue(nextValue);
                        return cell;
                    } else {
                        return null; // If no valid value is found for this cell
                    }
                }
            }
        }

        return null; // No more cells to fill
    }


    // Uses stack to keep track of solution and allow backtracking when stuck
    public boolean solve(int delay) {

        Stack<Cell> cellStack = new Stack<>(); // Allocates empty stack

        while (cellStack.size() < (Board.SIZE * Board.SIZE - board.numLocked())) { // While stack size is less than num of unspecified cells

            if (delay > 0)
                try {
                    Thread.sleep(delay); // Allows timely visual of solving
                }
                catch(InterruptedException ex) {
                    System.out.println("Interrupted");
                }
                
            if (ld != null)
                ld.repaint(); // Displays Sudoku

            Cell next = findNextCell(); // Creates a cell called next by calling findNextCell

            while (next == null && !cellStack.isEmpty()) { // While next is null and stack is non-empty

                Cell poppedCell = cellStack.pop(); // Pops a cell off the stack
                int poppedValue = findNextValue(poppedCell); // Calls findNextValue on this cell
                poppedCell.setValue(poppedValue); // Sets its value to the result

                if (poppedValue != 0) { // If cell value is no longer 0
                    next = poppedCell; // Set next to this popped cell
                }
            }
            
            if (next == null) { 
                return false; // Stack is empty and no valid cell found - give up
            } else {
                cellStack.push(next); // Push next onto the stack
            }
        }
        
        board.setFinished(true);
        return true; // Board contains solution
    }


    public static void main(String[] args) {

        // Usage Statement
        if (args.length != 1) {
            System.out.println("Usage: java Sudoku <numInitialCells>");
            return;
        }
    
        int numInitialCells = Integer.parseInt(args[0]); // Command line argument
        int numSimulations = 50; // Number of simulations of Suduko
        int solvedCount = 0; // Tracks number of solved boards
        long totalElapsedTime = 0; // Total elapsed time for all simulations


        // For loop used to run 50 simulations of Sudoku 
        for (int i = 0; i < numSimulations; i++) {

            Sudoku sudoku = new Sudoku(numInitialCells); // Creates board
            // System.out.println("Initial Board:");
            // System.out.println(sudoku.board);

            long startTime = System.currentTimeMillis(); // Tracks start time

            if (sudoku.solve(0)) {
                // System.out.println("Final Board:");
                // System.out.println(sudoku.board);
                
                if (sudoku.board.validSolution()) {
                    System.out.println("Simulation " + (i+1) + ": The board is solved.");
                    solvedCount++;
                    
                } else {
                    System.out.println("Simulation " + (i+1) + ": The board is NOT solved.");
                }

            } else {
                System.out.println("Simulation " + (i+1) + ": No solution found.");
            }

            long endTime = System.currentTimeMillis(); // Tracks end time
            totalElapsedTime += (endTime - startTime); // Calculates total elapsed time to solve
        }

        long averageTime = totalElapsedTime / solvedCount; // Averages time to solve over 50 simulations
        
        // Prints the results
        System.out.println("\nNumber of Puzzles Solved: " + solvedCount);
        System.out.println("Average Time to Solve: " + averageTime + "ms");
    }
}

/* 
 * How to compile: javac Sudoku.java
 * How to run: java Sudoku <numInitialCells>
 */
