package samueliox.headsup;

import java.util.*;

/**
 * This class is a model that holds the state of the game.
 */
public class HeadsUpModel extends Observable {
    // Animals for animal category
    private static final String[] animalList = {"cat", "dog", "rat", "moose", "reindeer", "ant",
            "bee", "hawk", "pigeon", "seagull", "raven", "mongoose", "snake", "bear"};

    // Celebrities for celebrity category
    private static final String[] celebList = {"Tom Hanks", "Ellen", "George Clooney",
            "Tina Fey", "Alec Baldwin", "Harrison Ford", "Brad Pitt", "Lindsay Lohan", "Ryan Gosling",
            "Adele", "Halle Barry", "Michael Jackson", "Taylor Swift", "Paul McCartney",
            "Ringo Starr", "John Lennon", "George Harrison"};

    // Cartoons for cartoon category
    private static final String[] cartoonList = {"The Simpsons", "Futurama", "Family Guy",
            "Bob's Burgers", "Archer", "Aqua Teen Hunger Force", "The Flintstones",
            "The Jetsons", "Dragon Ball Z", "Pokemon", "Teenage Mutant Ninja Turtles"};

    // Keeps track of the score
    private int scoreCounter;

    // Keeps track of the current index in the shuffled array of words
    private int listTracker;

    // The current word
    private String currentWord;

    // The shuffled list of words
    private List<String> shuffledLibrary;

    // Keeps track of correct words, and skipped words
    private StringBuilder allCorrectWords, allSkippedWords;

    public HeadsUpModel(int category) {
        super();
        //keeps score
        scoreCounter = 0;
        //keeps the place in the list
        listTracker = 0;
        //the current word in the game
        currentWord = "";
        //the list of words used for the game that are shuffled
        shuffledLibrary = null;
        //list of words that were correct and Skipped are added to a queue
        allCorrectWords = new StringBuilder();
        allSkippedWords = new StringBuilder();
        startGame(category);
        notifyObservers();
    }

    /**
     * Method that starts a new game, starts game timer
     */
    public void startGame(int category){
        //first reset game
        resetGame();

        //shuffles the library
        shuffledLibrary = shuffleList(category);

        allCorrectWords.append("Correct Words: \n");
        allSkippedWords.append("Skipped Words: \n");
        //set currentWord
        setCurrentWord();
    }

    /**
     * Method that shuffles a library based on the category given
     * @param category which category a person chooses
     * @return the shuffled library based on the category chosen
     * @throws IllegalArgumentException
     */
    public List<String> shuffleList(int category){
        if(category < 0 || category > 2){
            throw new IllegalArgumentException("The category number is not valid");
        }
        List<String> shuffledList = new ArrayList<>(Arrays.asList(getLibrary(category)));
        Collections.shuffle(shuffledList);
        return shuffledList;
    }

    public List<String> getShuffledLibrary(){
        return shuffledLibrary;
    }
    
    /**
     * Method that adds the score up, and strikes the current word
     * from the library used
     */
    public void addCorrectWord(){
        scoreCounter++;
        allCorrectWords.append(currentWord + "\n");
        setCurrentWord();
    }

    /**
     * Method that goes to the next card in the list
     */
    public void skipCurrentWord(){
        allSkippedWords.append(currentWord + "\n");
        setCurrentWord();
    }

    /**
     * Method that gets the score
     */
    public int getScore(){
        return scoreCounter;
    }
    
    /**
     * Method that gets a library of words under a category
     * @param category the category that was selected
     */
    public String[] getLibrary(int category){
        switch(category) {
            case 0:
                return celebList;
            case 1:
                return animalList;
            case 2:
                return cartoonList;
        }
        return null;
    }

    /**
     * Method that gets the word currently in use for the game
     * @return the word in play
     */
    public String getCurrentWord(){
        return currentWord;
    }

    /**
     * Method that sets the current word in use
     */
    private void setCurrentWord(){
        //find a way to end the game based on timer here too if we want
        if(!checkGameOver()) {
            currentWord = shuffledLibrary.get(listTracker++);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Method that checks if the game is over by comparing the length  of the list tracker
     * @return true if the listracker is greater or equal to the shuffled list
     */
    public boolean checkGameOver(){
        return listTracker >= getShuffledLibrary().size();
    }

    /**
     * Method that gets the queue with all the correct words to be
     * displayed in the end scorecard
     * @return queue of all the words that were correct
     */
    public StringBuilder getAllCorrectWords(){
        return allCorrectWords;
    }

    /**
     * Method that gets the queue with all the Skipped words to be
     * displayed in the end scorecard
     * @return queue of all the words that were incorrect
     */
    public StringBuilder getAllSkippedWords(){
        return allSkippedWords;
    }

    /**
     * Method that returns where in the library we are
     * @return place where we left off
     */
    public int getListTracker(){
        return listTracker;
    }
    /**
     * Method that resets the game state so another game can be played
     */
    public void resetGame(){
        scoreCounter = 0;
        listTracker = 0;
    }
}
