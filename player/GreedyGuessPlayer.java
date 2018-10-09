package player;

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
 * Greedy guess player (task B).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class GreedyGuessPlayer  implements Player{

	private int numRow;
	private int numColumn;
	
	private Cell[][] board;
	private LinkedList<Cell> huntGuess;

	private List<OwnShip> ships;
	private final int numShip = 5;
	
	private boolean isHit;
	private LinkedList<Cell> target;
	@Override
    public void initialisePlayer(World world) {
        this.numRow = world.numRow;
    	this.numColumn = world.numColumn;
    	
    	this.board = new Cell[numRow][numColumn];
    	
    	this.ships = new ArrayList<>(numShip);
    	this.huntGuess = new LinkedList<>();
    	this.isHit = false;
    	this.target = new LinkedList();

    	//initialize ShipLoc
    	for(ShipLocation sl : world.shipLocations) {
    		ships.add(new OwnShip(sl.ship, sl.coordinates));
    	}
    	
    	//initialize board
    	for(int i = 0; i < numRow; i++) {
    		for(int j = 0; j < numColumn; j++) {
    			board[i][j] = new Cell(i, j);
    		}
    	}
    	
    	//add adjacent Cells for cells
		for(int i = 0; i < numRow; i++) {
			for(int j = 0; j < numColumn-1; j++) {
				board[i][j].adjC.add(board[i][j+1]);
				board[j][i].adjC.add(board[j+1][i]);
				board[i][numColumn-1-j].adjC.add(board[i][numColumn-2-j]);
				board[numColumn-1-j][i].adjC.add(board[numColumn-2-j][i]);
			}
		}
		
		//initialize huntGuess
		for(int i = 0; i < numRow; i++) {
			for(int j = 0; j < numColumn; j++) {
				if((i+j) % 2 == 0) this.huntGuess.add(board[i][j]);
			}
		}
		//shuffle huntGuess--> add some random
		Collections.shuffle(huntGuess);
		
    } // end of initialisePlayer()

    @Override
    public Answer getAnswer(Guess guess) {
        // To be implemented.
    	if(guess == null) return null;
    	Answer answer = new Answer();
    	Coordinate co = new World().new Coordinate();
    	co.column = guess.column;
    	co.row = guess.row;
    	
    	for(OwnShip os : this.ships) {
    		for(World.Coordinate wc : os.shipLoc.keySet())
    		{
    			if(co.row == wc.row && co.column == wc.column) {
    				answer.isHit = true;
    				os.shipLoc.put(wc, Boolean.TRUE);
        			if(os.isSunk()) {
        				answer.shipSunk = os.ship;
        			}
        			break;
    			}
    		}
    	}
    	
    	OwnShip toBeD = null;
    	if(answer.shipSunk != null) {
    		for(OwnShip os : this.ships) {
    			if(os.ship.name().equals(answer.shipSunk.name())) toBeD = os;
    		}
    		if(toBeD != null)
    			this.ships.remove(toBeD);    		
    	}
    	
        return answer;
    } // end of getAnswer()


    @Override
    public Guess makeGuess() {
    	Cell c = null;
    	if(!this.isHit || (this.isHit && target.isEmpty())) {
    		do {
    			c = huntGuess.poll();
    		}while(c.isHit);
    		
    		c.isHit = true;
    		Guess g = c.guess;
    		return g;
    	}else {    			
    		while(target.peek().adjC.isEmpty()) {
    			target.pop();
    			if(target.isEmpty()) {
    				return makeGuess();
    			}
    		}
    		c = target.peek();

    		Cell ac = null;
    		do {
    			ac = c.adjC.poll();
    			if(ac == null) {
    				target.pop();
    				return makeGuess();
    			}
    		}while(ac.isHit);
    		ac.isHit = true;
    		return ac.guess;
    		}
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    	if(answer == null) return;
    	if(answer.isHit) {
    		this.isHit = true;
    		if(answer.shipSunk == null) {
    			//find the relevant Cell
    			
    			for(Cell[] c : this.board) {
    				for(Cell cell : c) {
    					if(cell.row == guess.row && cell.colunm == guess.column) {
    		    			this.target.add(cell);
    		    			return;
    					}
    				}
    			}
    			
    		}else {
    			do {
    				target.pop();
    				if(target.isEmpty()) break;
    			}while(target.peek().adjC.isEmpty());
    		}

    		
    	}
    } // end of update()

  
    @Override
    public boolean noRemainingShips() {
        // To be implemented.
    	return this.ships.isEmpty();
        // dummy return
    } // end of noRemainingShips()

    
    private class OwnShip{
    	private Ship ship;
    	
    	HashMap<World.Coordinate, Boolean> shipLoc;
    	
    	public OwnShip(Ship ship) {
    		this.ship = ship;
    	}
    	
    	
    	public OwnShip(Ship ship, List<World.Coordinate> location) {
    		this.ship = ship;
    		shipLoc = new HashMap();
    		
    		//initialize ship location and set all of them as not being hit
    		//true means being hit
    		//false means not being hit
    		for(Coordinate wc : location) {
    			this.shipLoc.put(wc, Boolean.FALSE);
    		}
    	}
    	
    	public boolean isSunk() {
    		boolean issunk = true;
    		for(World.Coordinate wc : shipLoc.keySet()) {
    			if(!shipLoc.get(wc).booleanValue()) {
       				issunk = false;
    			}
    		}
       		return issunk;
    	}
    	
    	@Override
    	public boolean equals(Object o) {
    		if(this == o) return true;
    		if(o == null || o.getClass() != this.getClass()) return false;
    		
    		OwnShip os = (OwnShip) o;
    		return this.ship == os.ship;
    	}
    	
    	@Override
    	public int hashCode() {
    		return Objects.hash(ship);
    	}
    }
    
    
    class Cell{
    	int row, colunm;
    	LinkedList<Cell> adjC;
    	Guess guess;
    	Coordinate coor;
    	boolean isHit;
    	
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
    	}
    	
    	public void addAdj(Cell cell) {
    		this.adjC.add(cell);
    	}
    	
    	public LinkedList<Cell> getAdj(){
    		return this.adjC;
    	}
    }
    
} // end of class GreedyGuessPlayer
    
