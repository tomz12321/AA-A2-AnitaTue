package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.Scanner;
import world.World;
import world.World.Coordinate;
import world.OppWorld;

/**
 * Probabilistic guess player (task C).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class ProbabilisticGuessPlayer extends Guesser implements Player{

    public List<Guess> checkerBoardGuesses;

    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
        this.myWorld = world;
        this.opponentsWorld = new OppWorld(world.numRow, world.numColumn, true);
        this.hitsToMyFleet = new ArrayList<>();
        this.checkerBoardGuesses = new ArrayList<>();
        enumerateGuesses(checkerBoardGuesses);
    } // end of initialisePlayer()

    private void enumerateGuesses(List<Guess> list) {
        for(int row = 0; row < myWorld.numRow; ++row){
            for(int col = 0; col < myWorld.numColumn; ++col){
                Guess g = new Guess();
                g.row = row;
                g.column = col;
                list.add(g);
            }
        }
    }

    //@Override
    //public Answer getAnswer(Guess guess) {
        // To be implemented.

        // dummy return
    //    return null;
    //} // end of getAnswer()


    @Override
    public Guess makeGuess() {
        // To be implemented.
        System.out.println(checkerBoardGuesses.size());
        // ** Targeting mode **
        if ( opponentsWorld.possibleTargets.size() > 0 )
        {   
            Guess g = new Guess();
            Coordinate tempCoord = myWorld.new Coordinate();
            tempCoord = opponentsWorld.possibleTargets.remove(0);

            g.row = tempCoord.row;
            g.column = tempCoord.column;
            
            /*
             *  Check if this possible target is in the checkBoardGuesses list
             *  This is to avoid duplicate guesses
             */
            Guess tempGuess = new Guess();
            for ( int i = 0; i < checkerBoardGuesses.size(); i++ ) 
            {
                tempGuess = checkerBoardGuesses.get(i);
                if ( ( tempGuess.row == g.row ) && ( tempGuess.column == g.column ) )
                {
                    checkerBoardGuesses.remove(i);
                    i = checkerBoardGuesses.size(); // TO EXIT LOOP
                }
            }
            return g;
        }

        /** hunting mode **/
        Guess tempGuess = new Guess();
        Boolean isHunting = false;

        if (checkerBoardGuesses.size() == 0)
        {
            isHunting = true;
            System.out.println("No more hunting!");
            // ** Hunting mode **
            Random random = new Random();
            //temp solution
            int index = 0;
            if (checkerBoardGuesses.size() == 0){
                index = 0;
                System.out.println("return dummy value here!");
                Guess g = new Guess();

                g.row = random.nextInt(10)+1;
                g.column = random.nextInt(10)+1;
                System.out.println(g.row+","+g.column);

                return g;
            }
            else if(checkerBoardGuesses.size() != 0){
                index = random.nextInt(1000) % checkerBoardGuesses.size();
            }
            return checkerBoardGuesses.remove(index);
        }


        while(isHunting != true)
        {
            //initialised

            tempGuess.row = 5;
            tempGuess.column = 5;

            int gridsize = myWorld.numRow * myWorld.numColumn;
            //direction = 0,1,2,3,4 means stay,east,south,west,north
            //max
            int max = myWorld.numRow;
            int min = 1;

            //Initialised Iteratormap
            //ArrayList<Actors> actorsList = new ArrayList<Actors>();
            //actorsList.add();
            List<Integer> iteratormap = new ArrayList<Integer>();
            iteratormap.add(0);
            iteratormap.add(1);
            iteratormap.add(2);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(4);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(1);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(2);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);
            iteratormap.add(3);

            int mapindex;
            if (tempGuess.row == 5 && tempGuess.row == 5)
                mapindex = 0;
            else {
                mapindex = iteratormap.size() - 99;
            }
            System.out.println(iteratormap.get(mapindex));
            for (int i = 0 ; i < 100 - checkerBoardGuesses.size(); i++)
            {
                System.out.println("index's value:"+iteratormap.get(i));
                switch(iteratormap.get(i))
                {
                    case 0:
                        tempGuess.row = 5;
                        tempGuess.column = 5;
                    case 1:
                        tempGuess.column = tempGuess.column + 1;
                    case 2:
                        tempGuess.row = tempGuess.row + 1;
                    case 3:
                        tempGuess.column = tempGuess.column - 1;
                    case 4:
                        tempGuess.row = tempGuess.row - 1;
                    default:
                        tempGuess.row = 5;
                        tempGuess.column = 5;
                }
            }
                // dummy return
                isHunting = true;
                //iteratormap.remove(mapindex);
                System.out.println(checkerBoardGuesses.size());
                //mapindex = mapindex++;
        
        }

        //return tempGuess;
        int index = checkerBoardGuesses.size() -1;
        if (index != 100)
            checkerBoardGuesses.remove(index);
        return tempGuess;
    } // end of makeGuess()


    @Override
    public void update(Guess guess, Answer answer) {
        // To be implemented.
    } // end of update()


    //@Override
    //public boolean noRemainingShips() {
        // To be implemented.

        // dummy return
    //    return true;
    //} // end of noRemainingShips()

} // end of class ProbabilisticGuessPlayer
