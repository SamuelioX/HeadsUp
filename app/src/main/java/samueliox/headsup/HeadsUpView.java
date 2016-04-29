package samueliox.headsup;

import java.util.*;

/**
 * Created by samuel on 4/14/2016.
 */
public class HeadsUpView implements Observer{
    private String currentWord, correctScoreboard, skippedScoreboard;
    private HeadsUpModel model;

    public HeadsUpView(HeadsUpModel m){
        model = m;
        currentWord = model.getCurrentWord();
        model.addObserver(this);
    }

    /**
     * Method that sets the current word to be displayed
     */
    public void setCurrentWordDisplay(){
        currentWord = model.getCurrentWord();
    }

    /**
     * Returns the current word as a display
     * @return the string being displayed in String form
     */
    public String getCurrentWordDisplay(){
        return currentWord;
    }

    /**
     * Returns the current word as a display
     * @return the string being displayed in String form
     */

    public String getCorrectScoreboardDisplay(){
        return correctScoreboard;
    }

    /**
     * Returns the current word as a display
     * @return the string being displayed in String form
     */
    public String getSkippedScoreboardDisplay(){
        return skippedScoreboard;
    }

    /**
     * Method that sets the scoreboard
     */
    public void setCorrectScoreboardDisplay(){
        StringBuffer sb = new StringBuffer();
        //gets the queue of correct words and removes them all
        Queue<String> allCorrectWords = model.getAllCorrectWords();
        sb.append("Correct Words:" +"\n");
        while(!allCorrectWords.isEmpty()){
            sb.append(allCorrectWords.remove() + "\n");
        }

        correctScoreboard = sb.toString();
    }

    /**
     * Method that sets the skipped scoreboard
     */
    public void setSkippedScoreboardDisplay(){
        StringBuffer sb = new StringBuffer();
        //would have to indicate which words were correct or skipped
        Queue<String> allWrongWords = model.getAllSkippedWords();
        sb.append("Skipped Words:" +"\n");
        while(!allWrongWords.isEmpty()){
            sb.append(allWrongWords.remove() + "\n");
        }
        skippedScoreboard = sb.toString();
    }
    @Override
    /**
     * Method that updates the current word
     */
    public void update(Observable observable, Object data) {
        setCurrentWordDisplay();
    }
}
