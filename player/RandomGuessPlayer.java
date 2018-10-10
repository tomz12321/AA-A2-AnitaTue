package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.Scanner;
import world.World;
import java.lang.Math;

/**
 * Random guess player (task A).
 * Please implement this class.
 *
 * @author Youhan Xia, Jeffrey Chan
 */
public class RandomGuessPlayer extends Guesser implements Player{

    public List<Guess> allAvailableGuesses;

    @Override
    public void initialisePlayer(World world) {
        // To be implemented.
        this.myWorld = world;
        this.hitsToMyFleet = new ArrayList<>();
        this.allAvailableGuesses = new ArrayList<>();
        enumerateGuesses(allAvailableGuesses);
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
        // int maximumNumber = 10; 
        // int rand = 1 + (int) (Math.random() * maximumNumber);

        // {
        //     System.out.println("Random Number:" + rand);
        // }

        //get a random index and pop a guess object from the list
        Random random = new Random();
        int index = random.nextInt(1000) % allAvailableGuesses.size();
        return allAvailableGuesses.remove(index);

        // dummy return
        //return null;
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

} // end of class RandomGuessPlayer
