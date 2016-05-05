package samueliox.headsup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *
 * @author Samuel No
 * @version 4/20/2016
 */
public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private HeadsUpModel model;
    private HeadsUpView view;
    private TextView gameText;
    private RelativeLayout background;
    private Sensor mySensor;
    private SensorManager SM;
    public final double ACTIONTHRESHOLD = 8.5;
    public final double UNBLOCKTHRESHOLD = 3.5;
    public boolean actionsAreBlocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        gameText = (TextView) findViewById(R.id.current_word_text);
        model = new HeadsUpModel(1);
        view = new HeadsUpView(model);
        gameText.setText(view.getCurrentWordDisplay());
        playGame();
    }

    /**
     * Method that starts the game, setups up buttons and actions
     */
    public void playGame() {
        Button correctButton = (Button) findViewById(R.id.correct_button);
        Button skipButton = (Button) findViewById(R.id.skip_button);
        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.addCorrectWord();
                gameText.setText(view.getCurrentWordDisplay());
                if (model.getScore() == 2 || model.getListTracker() == model.getShuffledLibrary().size()) {
                    endGame();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.getListTracker() == model.getShuffledLibrary().size()) {
                    endGame();
                } else {
                    model.skipCurrentWord();
                    gameText.setText(view.getCurrentWordDisplay());
                }
            }
        });

        //Create our SensorManager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Method that ends the game and brings up the scoreboard activity
     */
    public void endGame() {
        //sets the end view based on the string in the model
        view.setCorrectScoreboardDisplay();
        view.setSkippedScoreboardDisplay();

        //send intent into scoreboard activity
        Intent intent = new Intent(getApplicationContext(), ScoreboardActivity.class);
        intent.putExtra("points", model.getScore());
        intent.putExtra("correctWords", view.getCorrectScoreboardDisplay());
        intent.putExtra("skippedWords", view.getSkippedScoreboardDisplay());
        startActivity(intent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        double zForce = event.values[2];
        if (zForce >= ACTIONTHRESHOLD && !isBlocked()) {
            pass();
        } else if (zForce <= -ACTIONTHRESHOLD && !isBlocked()) {
            correct();
        } else if (zForce < UNBLOCKTHRESHOLD && zForce > -UNBLOCKTHRESHOLD && isBlocked()) {
            unblock();
        }
    }

    private boolean isBlocked() {
        return actionsAreBlocked;
    }

    private void unblock() {
        actionsAreBlocked = false;
    }

    private void correct() {
        model.addCorrectWord();
        gameText.setText(view.getCurrentWordDisplay());
        if (model.getScore() == 2 || model.getListTracker() == model.getShuffledLibrary().size()) {
            endGame();
        }
        actionsAreBlocked = true;
    }

    private void pass() {
        if (model.getListTracker() == model.getShuffledLibrary().size()) {
            endGame();
        } else {
            model.skipCurrentWord();
            gameText.setText(view.getCurrentWordDisplay());
        }
        actionsAreBlocked = true;
    }
}