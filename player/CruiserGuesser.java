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

/**
 * CruiserGuesser class for Probabilistic guess player (task C).
 * Please implement this class.
 *
 * @author Skeleton provided by Youhan Xia, Jeffrey Chan, function implemented by Jyh-woei Yang (s3613252) and YuJue Zou (s3666814)
 */

class CruiserGuesser{
	public Cell[][] board;
    public boolean isSunk;
    private int calNumRow; //playerWorld.numRow;
    private int calNumColumn; //playerWorld.numColumn;
    private LinkedList<Cell> prob4;
    private LinkedList<Cell> prob3;
    private LinkedList<Cell> prob2;

	CruiserGuesser(){
		this.calNumRow = 10;
        this.calNumColumn = 10;
        this.board = new Cell[calNumRow][calNumColumn];
        this.isSunk = false;

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

        //prob4 elements
        for(int i = 1; i <= calNumRow-2; i++)
            for(int j = 1; j <= calNumColumn-2; j++)  
                setCellValue(i, j, 4);

        //prob3 elements
        for(int i = 1; i <= calNumRow-2; i++)
            setCellValue(i, 0, 3);

        for(int i = 1; i <= calNumColumn-2; i++)
            setCellValue(0, i, 3);

        for(int i = 1; i <= calNumColumn-2; i++)
            setCellValue(calNumRow-1, i, 3);

        for(int i = 1; i <= calNumRow-2; i++)
            setCellValue(i, calNumColumn-1, 3);

        //prob2 elements
        setCellValue(0, 0, 1);
        setCellValue(0, calNumColumn-1, 1);
        setCellValue(calNumRow-1, 0, 1);
        setCellValue(calNumRow-1, calNumColumn-1, 1);

        //setElements into list
            for(int i = 0; i < calNumRow; i++)
                for (int j = 0; j < calNumColumn; j++)
                {
                    if (getCellValue(i,j) == 4)
                        prob4.add(board[i][j]);
                    else if (getCellValue(i,j) == 3)
                        prob3.add(board[i][j]);
                    else if (getCellValue(i,j) == 1)
                        prob2.add(board[i][j]);
                }
        System.out.println("=CruiserGuesser=");        
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