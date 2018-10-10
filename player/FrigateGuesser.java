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

class FrigateGuesser{
	public Cell[][] board;
	public boolean isSunk;
    private int calNumRow;//playerWorld.numRow;
    private int calNumColumn;//playerWorld.numColumn;
    private LinkedList<Cell> prob8;
    private LinkedList<Cell> prob7;
    private LinkedList<Cell> prob6;
    private LinkedList<Cell> prob5;
    private LinkedList<Cell> prob4;
    private LinkedList<Cell> prob3;
    private LinkedList<Cell> prob2;

	FrigateGuesser(){
        this.calNumRow = 10;
        this.calNumColumn = 10;
        this.board = new Cell[calNumRow][calNumColumn];
        this.isSunk = false;
        this.prob8 = new LinkedList<>();
        this.prob7 = new LinkedList<>();
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

        //prob8 elements
        for(int i = 3; i <= 6; i++)
        {
            for(int j = 3; j <= 6; j++)  
                setCellValue(i, j, 8);
        }

        //pro7 elements
        for(int i = 3; i <= 6; i++)
            setCellValue(i, 2, 7);

        for(int i = 3; i <= 6; i++)
            setCellValue(i, 7, 7);

        for(int i = 3; i <= 6; i++)
            setCellValue(2, i, 7);

        for(int i = 3; i <= 6; i++)
            setCellValue(7, i, 7);

        //prob6 elements
            setCellValue(7,2,6);
            setCellValue(2,7,6);
            setCellValue(2,2,6);
            setCellValue(7,7,6);
        for(int i = 3; i <= 6; i++)
            setCellValue(i, 1, 6);

        for(int i = 3; i <= 6; i++)
            setCellValue(i, 8, 6);

        for(int i = 3; i <= 6; i++)
            setCellValue(1, i, 6);

        for(int i = 3; i <= 6; i++)
            setCellValue(8, i, 6);

        //prob5 elements
            setCellValue(1,2,5);
            setCellValue(2,1,5);
            setCellValue(7,8,5);
            setCellValue(8,7,5);
            setCellValue(8,2,5);
            setCellValue(7,1,5);
            setCellValue(2,8,5);
            setCellValue(1,7,5);      

        for(int i = 3; i <= 6; i++)
            setCellValue(i, 0, 6);

        for(int i = 3; i <= 6; i++)
            setCellValue(i, 9, 6);

        for(int i = 3; i <= 6; i++)
            setCellValue(0, i, 6);

        for(int i = 3; i <= 6; i++)
            setCellValue(9, i, 6);

        //prob4 elements
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
            setCellValue(0,0,2);
            setCellValue(0,9,2);
            setCellValue(9,0,2);
            setCellValue(9,9,2);

        //setElements into list
            for(int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                {
                    if(getCellValue(i,j) == 8)
                        prob8.add(board[i][j]);
                    else if (getCellValue(i,j) == 7)
                        prob7.add(board[i][j]);
                    else if (getCellValue(i,j) == 6)
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
        System.out.println("=FrigateGuesser=");
        System.out.println(prob8.size());
        System.out.println(prob7.size());
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