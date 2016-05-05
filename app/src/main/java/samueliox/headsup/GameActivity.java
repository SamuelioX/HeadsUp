package samueliox.headsup;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Samuel No
 * @version 4/20/2016
 */
public class GameActivity extends AppCompatActivity implements SensorEventListener, Observer {
    private HeadsUpModel model;
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
        gameText.setText(model.getCurrentWord());
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
                correct();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass();
            }
        });

        //Create our SensorManager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        //Accelerometer Sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register sensor Listener
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);

        model.addObserver(this);
    }

    /**
     * Method that ends the game and brings up the scoreboard activity
     */
    public void endGame() {

        //send intent into scoreboard activity
        Intent intent = new Intent(getApplicationContext(), ScoreboardActivity.class);
        intent.putExtra("model", "test");
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
        actionsAreBlocked = true;
    }

    private void pass() {

        model.skipCurrentWord();

        actionsAreBlocked = true;
    }

    @Override
    public void update(Observable observable, Object data) {
        String nextWord = model.getCurrentWord();

        if (nextWord == null) {
            //TODO gameover functionality

            return;
        }
        gameText.setText(nextWord);

    }
}