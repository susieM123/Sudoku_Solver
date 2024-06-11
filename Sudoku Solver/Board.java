/* Author: Susie Mueller
 * Purpose: Lab 4
 * Date: 10/12/23
 * File: Board.java
 */


// Libraries used. 
import java.io.*;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

// The purpose of the Board class is to hold an array of Cells that make up the Sudoku board.
public class Board {

    public static final int SIZE = 9;
    private Cell[][] board;
    private boolean finished;


    // Default contructor creates a 9x9 2D array of Cells, all initialized to have value 0.
    public Board() {

        board = new Cell[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = new Cell(row, col, 0); 
            }
        }
    }


    // Generates board specifying number of initial cells to be locked.
    public Board(int numInitialCells) {

        this();

        // Check number of initial cells to lock is valid number
        if (numInitialCells < 0 || numInitialCells > SIZE*SIZE) {
            System.out.println("Invalid number of initial cells.");
            return;
        }

        boolean[][] lockedCell = new boolean[SIZE][SIZE];
        Random random = new Random(); 

        while (numInitialCells > 0) {

            // Selects random rows and cols
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);

            // Checks if cell is locked or not
            if (!lockedCell[row][col]) { 
                int value = random.nextInt(9) + 1; // Random value between 1 and 9
                if (validValue(row, col, value)) {
                    set(row, col, value, true); // Sets the value as locked
                    lockedCell[row][col] = true;
                    numInitialCells--; // Decreases number initial cells by one
                }
            }
        }
    }


    // Auxiliary constructor.
    public Board(String filename) {

        this(); // Calls default constructor
        read(filename);
    }


    // Returns the number of columns.
    public int getCols() {

        return SIZE;
    }


    // Returns the number of rows. 
    public int getRows() {

        return SIZE;
    }


    // Returns the Cell at the given row and col.
    public Cell get(int row, int col) {

        return board[row][col];
    }


    // Returns whether the Cell at row, col, is locked.
    public boolean isLocked(int row, int col) {
        
        return board[row][col].isLocked();
    }


    // Returns the number of locked Cells on the board.
    public int numLocked() {

        int count = 0; 
        for (int row = 0; row < SIZE; row++){
            for (int col = 0; col < SIZE; col++){
                if (isLocked(row, col)) {
                    count++;
                }
            }
        }
        return count;
    }


    // Returns value of Cell row, col. 
    public int value(int row, int col) {

        return board[row][col].getValue();
    }


    // Sets the value of the Cell at row,col. 
    public void set(int row, int col, int value) {

        board[row][col].setValue(value);
    }


    // Sets the value and locked fields of Cell at row,col. 
    public void set(int row, int col, int value, boolean locked) {

        board[row][col].setValue(value);
        board[row][col].setLocked(locked);
    }  


    // Tests if given value is valid value at given row and col
    public boolean validValue(int row, int col, int value) {

        // Checks if value within range [1,9]
        if (value < 1 || value > 9) {
            return false;
        }

        // Checks for uniqueness within same row
        for (int r = 0; r < SIZE; r++) {
            if (r != row && value == value(r, col)) { 
                return false;
            }
        }

        // Checks for uniqueness within same col
        for (int c = 0; c < SIZE; c++) {
            if (c != col && value == value(row, c)) {
                return false;
            }
        }

        // Checks for uniqueness in local 3x3 square
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3; 
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if ((r != row || c != col) && value == value(r,c)) {
                    return false;
                }
            }
        }

        return true; // Value is valid and unique in its row, col, and local square

    }


    // Returns true if the board is solved
    public boolean validSolution() {

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int cellValue = value(row, col);

                // Checks if any value on board is 0 or not valid
                if (cellValue == 0 || !validValue(row, col, cellValue)) {
                    return false;
                }
            }
        }

        return true; 
    }


    // Loops over board and calls draw method of each Cell.
    public void draw(Graphics g, int scale) {

        for(int i = 0; i<getRows(); i++){
            for(int j = 0; j<getCols(); j++){
                get(i, j).draw(g, j*scale+5, i*scale+10, scale);
            }
        } if(finished){
            if(validSolution()){
                g.setColor(new Color(0, 127, 0));
                g.drawChars("Hurray!".toCharArray(), 0, "Hurray!".length(), scale*3+5, scale*10+10);
            } else {
                g.setColor(new Color(127, 0, 0));
                g.drawChars("No solution!".toCharArray(), 0, "No Solution!".length(), scale*3+5, scale*10+10);
            }
        }
    }

    // Set the sudoku board to finished
    public void setFinished(boolean finished) {

        this.finished = finished;
    }

    
    // Generates a multi-line string representation of the board.
    public String toString() {

        String result = "";

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                if (col != 0) { 
                    result += " "; // append a space between values
                }
                
                result += board[row][col].getValue(); // append cell value

                if (col == 8) { 
                    result += "\n"; // skips line after last col
                } else if ((col + 1) % 3 == 0) {
                    result += "  "; // extra space after every third col
                }
            }
            if (row != 8 && (row + 1) % 3 == 0) { // if it's every third row but not the last
                result += "- - - - - - - - - - -\n"; 
            }
        }
    
        return result;
    }


    // Reads board from a given file
    public boolean read(String filename) {

        try {
            // assign to a variable of type FileReader a new FileReader object, passing filename to the constructor
            FileReader fr = new FileReader(filename);
            // assign to a variable of type BufferedReader a new BufferedReader, passing the FileReader variable to the constructor
            BufferedReader br = new BufferedReader(fr);
            
            // assign to a variable of type String line the result of calling the readLine method of your BufferedReader object.
            String line = br.readLine();
            // start a while loop that loops while line isn't null
            while(line != null){
                // print line
            System.out.println( line );
                // assign to an array of Strings the result of splitting the line up by spaces (line.split("[ ]+"))
                String[] arr = line.split( "[ ]+" );
                // let's see what this array holds: 
                System.out.println("the first item in arr: " + arr[0] + ", the second item in arr: " + arr[1]);
                // print the size of the String array (you can use .length)
                System.out.println( arr.length );
                // use the line to set various Cells of this Board accordingly
                // assign to line the result of calling the readLine method of your BufferedReader object.
                line = br.readLine();
            }
            // call the close method of the BufferedReader
            br.close();
            return true;
        }
        catch(FileNotFoundException ex) {
            System.out.println("Board.read():: unable to open file " + filename );
        }
        catch(IOException ex) {
            System.out.println("Board.read():: error reading file " + filename);
        }

        return false;
        }

    
    public static void main(String[] args) {
        
        // Test methods ...
        if (args.length != 1) {
            System.out.println("Usage: java Main <filename>");
            return;
        }

        String filename = args[0]; // Takes in filename as command line argument
        Board board = new Board(); // Creates new board

        if (board.read(filename)) {
            System.out.println(board.read(filename)); // Prints read board
        }

        // Decides if valid solution is found
        if (board.validSolution()) {
            System.out.println("The board is solved."); 

        } else {
            System.out.println("The board is NOT solved.");
        }
    }
}