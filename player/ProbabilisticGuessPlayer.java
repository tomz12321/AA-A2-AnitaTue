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
 * Probabilistic guess player (task C).
 * Please implement this class.
 *
 * @author Skeleton provided by Youhan Xia, Jeffrey Chan, function implemented by Jyh-woei Yang (s3613252) and YuJue Zou (s3666814)
 */
public class ProbabilisticGuessPlayer implements Player{
    
    public AirCraftGuesser airCraftGuesser;
    public CruiserGuesser cruiserGuesser;
    public FrigateGuesser frigateGuesser;
    public SubmarineGuesser submarineGuesser;
    public PatrolCraftGuesser patrolCraftGuesser;

    private int numRow;
    private int numColumn;
    
    private Cell[][] board;
    private LinkedList<Cell> huntGuess;

    private List<OwnShip> ships;
    private final int numShip = 5;
    
    private boolean isHit;
    private LinkedList<Cell> target;
    
    //if (ishit == true) 1/5 airCraftGuesser, 1/5 cruiserGuesser, 1/5 frigateGuesser, 1/5 submarineGuesser, 1/5 patrolCraftGuesser
    private int initShipState;

    @Override
    public void initialisePlayer(World world) {
        this.numRow = world.numRow;
        this.numColumn = world.numColumn;
        
        this.board = new Cell[numRow][numColumn];
        
        this.ships = new ArrayList<>(numShip);
        this.huntGuess = new LinkedList<>();
        this.isHit = false;
        this.target = new LinkedList();

        this.initShipState = 2310;

        airCraftGuesser = new AirCraftGuesser();
        //apply cruiserGuesser prob map
        airCraftGuesser.initialGuesserMap(world);

        cruiserGuesser = new CruiserGuesser();
        //apply cruiserGuesser prob map
        cruiserGuesser.initialGuesserMap(world);

        frigateGuesser = new FrigateGuesser();
        //apply frigateGuesser prob map
        frigateGuesser.initialGuesserMap(world);

        submarineGuesser = new SubmarineGuesser();
        //apply submarineGuesser prob map
        submarineGuesser.initialGuesserMap(world);

        patrolCraftGuesser = new PatrolCraftGuesser();
        //apply patrolCraftGuesser prob map
        patrolCraftGuesser.initialGuesserMap(world);

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
        
        //add adjacent Cells for cells [control targeting mode here]
        for(int i = 0; i < numRow; i++) {
            for(int j = 0; j < numColumn-1; j++) { //numColumn-1
                board[i][j].adjC.add(board[i][j+1]);
                board[j][i].adjC.add(board[j+1][i]);
                //add some adj, +2 let checkerboard deal with it
                //board[i][j].adjC.add(board[i][j+2]);
                //board[j][i].adjC.add(board[j+2][i]);
                //board[i][j].adjC.add(board[i][j+3]);
                //board[j][i].adjC.add(board[j+3][i]);

                board[i][numColumn-1-j].adjC.add(board[i][numColumn-2-j]);
                board[numColumn-1-j][i].adjC.add(board[numColumn-2-j][i]);

                //board[i][numColumn-1-j].adjC.add(board[i][numColumn-4-j]);
                //board[numColumn-1-j][i].adjC.add(board[numColumn-4-j][i]);
            }
        }
        
        // 32 - 9
        for (int i = 32; i>=9 ; i--)
            initHuntguess(i);

        // initHuntguess(32);
        // ...
        // initHuntguess(16);
        // initHuntguess(15);
        // initHuntguess(14);
        // initHuntguess(13);
        // initHuntguess(12);
        // initHuntguess(11);
        // initHuntguess(10);
        // initHuntguess(9);
        // initHuntguess(8);
        // initHuntguess(7);
        // initHuntguess(6);
        // initHuntguess(5);
        //initHuntguess(4);
        //initHuntguess(3);
        //initHuntguess(2);
        //initialize huntGuess prob = 8
        // for(int i = 0; i < numRow; i++) {
        //     for(int j = 0; j < numColumn; j++) {
        //         if((i+j) % 2 == 0)
        //         {
        //             if(frigateGuesser.getCellValue(i,j) == 8)
        //             {
        //                 System.out.println("AddCell: ("+i+","+j+")="+frigateGuesser.getCellValue(i,j));
        //                 this.huntGuess.add(board[i][j]);
        //             }
        //         }
        //     }
        // }

        //shuffle huntGuess--> add some random
        //Collections.shuffle(huntGuess);
        
    } // end of initialisePlayer()


    //method to initHuntguess
    public void initHuntguess(int probWeight)
    {
        //initialize huntGuess prob = 8
        for(int i = 0; i < numRow; i++) {
            for(int j = 0; j < numColumn; j++) {
                if((i + j) % 2 == 0)
                {
                    if(getShipState(i,j,initShipState) == probWeight)
                    {
                        //(airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j))
                        System.out.println("AddCell: ("+i+","+j+")="+getShipState(i,j,initShipState));
                        System.out.println("ShipState Now: "+initShipState);
                        this.huntGuess.add(board[i][j]);
                        if (probWeight == 32)
                            Collections.shuffle(huntGuess);
                    }
                }
            }
        }
    }

    //this.huntGuess = null;
    //sumTime(11,7,5,3,2) = 2310

    //C5/4 => 5x4x3x2 / 4x3x2x1 = 5
    //sumTime(7,5,3,2) = 210
    //sumTime(11,5,3,2) = 330
    //sumTime(11,7,3,2) = 462
    //sumTime(11,5,3,2) = 770
    //sumTime(11,7,5,3) = 1155

    //C5/3 => 5x4x3 / 3x2x1 = 10
    //sumTime(11,7,5) = 385
    //sumTime(11,7,3) = 231
    //sumTime(11,7,2) = 154
    //sumTime(11,5,3) = 165
    //sumTime(11,5,2) = 110
    //sumTime(11,3,2) = 66
    //sumTime(7,5,3) = 105
    //sumTime(7,5,2) = 70
    //sumTime(7,3,2) = 42
    //sumTime(5,3,2) = 30

    //C5/2 => 5x4 / 2x1 = 10
    //sumTime(11,7) = 77
    //sumTime(11,5) = 55
    //sumTime(11,3) = 33
    //sumTime(11,2) = 22
    //sumTime(7,5) = 35
    //sumTime(7,3) = 21
    //sumTime(7,2) = 14
    //sumTime(5,3) = 15
    //sumTime(5,2) = 10
    //sumTime(3,2) = 6 

    //C5/1 => 5 / 1 = 5
    //sumTime(2)
    //sumTime(3)
    //sumTime(5)
    //sumTime(7)
    //sumTime(11)
    //airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j)

    //function to showShipState
    public int getShipState(int i, int j, int shipflag)
    {
        if (shipflag == 2310)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 210)
            return frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 330)
            return airCraftGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 462)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 770)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 1155)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 385)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j);
        else if (shipflag == 231)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 154)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 165)
            return airCraftGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 110)
            return airCraftGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 66)
            return airCraftGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 105)
            return frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 70)
            return frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 42)
            return frigateGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 30)
            return submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 77)
            return airCraftGuesser.getCellValue(i,j)+frigateGuesser.getCellValue(i,j);
        else if (shipflag == 55)
            return airCraftGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j);
        else if (shipflag == 33)
            return airCraftGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 22)
            return airCraftGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 35)
            return frigateGuesser.getCellValue(i,j)+submarineGuesser.getCellValue(i,j);
        else if (shipflag == 21)
            return frigateGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 14)
            return frigateGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 15)
            return submarineGuesser.getCellValue(i,j)+cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 10)
            return submarineGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 6)
            return cruiserGuesser.getCellValue(i,j)+patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 2)
            return patrolCraftGuesser.getCellValue(i,j);
        else if (shipflag == 3)
            return cruiserGuesser.getCellValue(i,j);
        else if (shipflag == 5)
            return submarineGuesser.getCellValue(i,j);
        else if (shipflag == 7)
            return frigateGuesser.getCellValue(i,j);
        else if (shipflag == 11)
            return airCraftGuesser.getCellValue(i,j);
        //default return 0;
        return 0;   
    }

    @Override
    public Answer getAnswer(Guess guess) {
        // implemented by Jyh Woei.
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
                        if(answer.shipSunk.name().equals("AircraftCarrier"))
                            this.initShipState = initShipState / 11;
                        else if(answer.shipSunk.name().equals("Frigate"))
                            this.initShipState = initShipState / 7;
                        else if(answer.shipSunk.name().equals("Submarine"))
                            this.initShipState = initShipState / 5;
                        else if(answer.shipSunk.name().equals("Cruiser"))
                            this.initShipState = initShipState / 3;
                        else if(answer.shipSunk.name().equals("PatrolCraft"))
                            this.initShipState = initShipState / 2;

                        //
                        this.huntGuess.clear();
                        //reset Prob
                        for (int i = 32; i>=1 ; i--)
                            initHuntguess(i);
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

    //debug
    //System.out.println(c.row+","+c.colunm+":"+target.isEmpty()+","+this.isHit+","+huntGuess.isEmpty());
    @Override
    public Guess makeGuess() {
        Cell c = null;
        System.out.println(huntGuess.size()+":"+huntGuess.get(0).row+","+huntGuess.get(0).colunm);
        if(!this.isHit || (this.isHit && target.isEmpty())) {
            do {
                c = huntGuess.poll();
            }while(c.isHit);
            
            c.isHit = true;
            Guess g = c.guess;
            //set probValue = 0;
            airCraftGuesser.setCellValue(c.row,c.colunm,0);
            frigateGuesser.setCellValue(c.row,c.colunm,0);
            submarineGuesser.setCellValue(c.row,c.colunm,0);
            cruiserGuesser.setCellValue(c.row,c.colunm,0);
            patrolCraftGuesser.setCellValue(c.row,c.colunm,0);
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
            //set probValue = 0;
            airCraftGuesser.setCellValue(ac.row,ac.colunm,0);
            frigateGuesser.setCellValue(ac.row,ac.colunm,0);
            submarineGuesser.setCellValue(ac.row,ac.colunm,0);
            cruiserGuesser.setCellValue(ac.row,ac.colunm,0);
            patrolCraftGuesser.setCellValue(ac.row,ac.colunm,0);
            return ac.guess;
            }
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // implemented by Jyh Woei.
        if(answer == null) return;
        if(answer.isHit) {
            this.isHit = true;
            if(answer.shipSunk == null) {
                // find the relevant Cell
                
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
                    if(target.isEmpty()) 
                        break;
                }while(target.peek().adjC.isEmpty());
            }

            
        }
    } // end of update()

  
    @Override
    public boolean noRemainingShips() {
        // implemented by Jyh Woei.
        return this.ships.isEmpty();
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
            if(this == o) 
                return true;
            if(o == null || o.getClass() != this.getClass()) 
                return false;
            
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

} // end of class ProbabilisticGuessPlayer
