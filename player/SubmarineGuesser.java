package player;

import java.util.Scanner;
import world.World;
import java.lang.Math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ship.Ship;
import world.World;
import world.World.Coordinate;
import world.World.ShipLocation;

class SubmarineGuesser{
	public Cell[][] board;
    public boolean isSunk;
    private int calNumRow;//playerWorld.numRow;
    private int calNumColumn;//playerWorld.numColumn;

    private LinkedList<Cell> prob6;
    private LinkedList<Cell> prob5;
    private LinkedList<Cell> prob4;
    private LinkedList<Cell> prob3;
    private LinkedList<Cell> prob2;

	SubmarineGuesser(){
		this.calNumRow = 10;
        this.calNumColumn = 10;
        this.board = new Cell[calNumRow][calNumColumn];
        this.isSunk = false;
        
        this.prob6 = new LinkedList<>();
        this.prob5 = new LinkedList<>();
        this.prob4 = new LinkedList<>();
        this.prob3 = new LinkedList<>();
        this.prob2 = new LinkedList<>();
	}

    //initialised guesser map
    public void initialGuesserMap(World world){
        //implement me
        this.calNumRow = world.numRow;
        this.calNumColumn = world.numColumn;
        this.board = new Cell[calNumRow][calNumColumn];
        this.isSunk = false;

        //initialize board
        for(int i = 0; i < calNumRow; i++) {
            for(int j = 0; j < calNumColumn; j++) {
                board[i][j] = new Cell(i, j);
            }
        }

        //prob6 elements
        for(int i = 2; i <= 7; i++)
            for(int j = 2; j <= 7; j++)  
                setCellValue(i, j, 6);

        //prob5 elements
        for(int i = 2; i <= 7; i++)
            setCellValue(i, 1, 5);

        for(int i = 2; i <= 7; i++)
            setCellValue(1, i, 5);

        for(int i = 2; i <= 7; i++)
            setCellValue(8, i, 5);

        for(int i = 2; i <= 7; i++)
            setCellValue(i, 8, 5);

        //prob4 elements, size 24(6*4) + 12(3*4) = 36
        for(int i = 2; i <= 7; i++)
            setCellValue(i, 0, 4);

        for(int i = 2; i <= 7; i++)
            setCellValue(0, i, 4);

        for(int i = 2; i <= 7; i++)
            setCellValue(9, i, 4);

        for(int i = 2; i <= 7; i++)
            setCellValue(i, 9, 4);

            setCellValue(1,1,4);
            setCellValue(8,8,4);
            setCellValue(1,8,4);
            setCellValue(8,1,4);
            setCellValue(2,0,4);
            setCellValue(0,2,4);
            setCellValue(2,9,4);
            setCellValue(0,7,4);
            setCellValue(9,2,4);
            setCellValue(7,0,4);
            setCellValue(9,7,4);
            setCellValue(7,9,4);

        //prob3 elements
            setCellValue(1,0,3);
            setCellValue(0,1,3);
            setCellValue(9,8,3);
            setCellValue(8,9,3);
            setCellValue(9,1,3);
            setCellValue(8,0,3);
            setCellValue(0,8,3);
            setCellValue(1,9,3);

        //prob2 elements
        setCellValue(0, 0, 2);
        setCellValue(0, 9, 2);
        setCellValue(9, 0, 2);
        setCellValue(9, 9, 2);

        //setElements into list
            for(int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                {
                    if (getCellValue(i,j) == 6)
                        prob6.add(board[i][j]);
                    else if (getCellValue(i,j) == 5)
                        prob5.add(board[i][j]);
                    else if (getCellValue(i,j) == 4)
                        prob4.add(board[i][j]);
                    else if (getCellValue(i,j) == 3)
                        prob3.add(board[i][j]);
                    else if (getCellValue(i,j) == 2)
                        prob2.add(board[i][j]);
                }
        System.out.println("=SubmarineGuesser=");        
        System.out.println(prob6.size());
        System.out.println(prob5.size());
        System.out.println(prob4.size());
        System.out.println(prob3.size());
        System.out.println(prob2.size());
    }

	class Cell{
    	int row, colunm;
    	LinkedList<Cell> adjC;
    	Guess guess;
    	Coordinate coor;
    	boolean isHit;
    	public int probValue;
    	
    	public Cell(int row, int column) {
    		this.row = row;
    		this.colunm = column;
    		this.adjC = new LinkedList<>();
    		this.guess = new Guess();
    		this.guess.row = row;
    		this.guess.column = colunm;
    		this.coor = new World().new Coordinate();
    		coor.row = row;
    		coor.column = column;
    		isHit = false;
            this.probValue = 0; //initialise as 0, assign it later
    	}
    	
    	public void addAdj(Cell cell) {
    		this.adjC.add(cell);
    	}
    	
    	public LinkedList<Cell> getAdj(){
    		return this.adjC;
    	}
    }

    //getter
    public int getCellValue(int vrow, int vcolumn){
    	return board[vrow][vcolumn].probValue;
    }
    public boolean getIsSunk(){
    	return isSunk;
    }
    //setter
    public void setCellValue(int vrow, int vcolumn, int setProbValue){
    	this.board[vrow][vcolumn].probValue = setProbValue;
    }
    public void setIsSunk(boolean setIsSunk){
    	this.isSunk = setIsSunk;
    }

}