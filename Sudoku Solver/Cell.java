/* Author: Susie Mueller
 * Purpose: Lab 4
 * Data: 11/11/23
 * File: Cell.java
 */


// Libraries used. 
import java.awt.Color;
import java.awt.Graphics;

// The purpose of the Cell class is to act as one location in a Sudoku board.
public class Cell {

    private int value; 
    private int row; 
    private int col;
    private boolean locked;
    

    // Constructor sets all values to 0 or false
    public Cell() {

        this.row = 0; 
        this.col = 0; 
        this.value = 0; 
        this.locked = false;  
    }
    

    // Constructor initializes row, column, and value fields to given parameter values. Sets locked field to false.
    public Cell (int row, int col, int value){

        this.row = row;
        this.col = col;
        this.value = value;
        this.locked = false; 
    }


    // Constructor initializes all Cell fields given the parameters
    public Cell(int row, int col, int value, boolean locked) {
        
        this.row = row;
        this.col = col; 
        this.value = value; 
        this.locked = false; 
    }


    // Returns the Cell's row index
    public int getRow(){

        return this.row; 
    }


    // Returns the Cell's column index
    public int getCol() {
        
        return this.col; 
    }

    
    // Returns the Cell's value
    public int getValue() {

        return this.value;
    }


    // Sets the Cell's value
    public void setValue(int newval) {
        
        this.value = newval;
    }


    // Returns the value of the locked field
    public boolean isLocked() {

        return this.locked;
    }


    // Sets the Cell's locked field to the new value
    public void setLocked(boolean lock) {

        this.locked = lock;
    }


    // Returns a string representation of Cell's numeric value
    public String toString() {

        String result = "Cell's Value: " + getValue();
        return result; 
    }


    // Draws the Cell's number.
    public void draw(Graphics g, int x0, int y0, int scale) {

        char toDraw = (char) ((int) '0' + getValue());
        g.setColor(isLocked()? Color.BLUE : Color.RED);
        g.drawChars(new char[] {toDraw}, 0, 1, x0, y0);
    }


    public static void main (String[] args) {
        
        // tests methods
        Cell testCell = new Cell(1, 1, 7);
        System.out.println(testCell);
        testCell.setValue(9);
        System.out.println(testCell);
        System.out.println("Row: " + testCell.getCol());
        System.out.println("Col: " + testCell.getRow());
        System.out.println("Cell Locked? " + testCell.isLocked());
    }

}
