package samueliox.headsup;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
    private Sensor mySensor;
    private SensorManager SM;
    private int category;
    public final double ACTIONTHRESHOLD = 8.5;
    public final double UNBLOCKTHRESHOLD = 3.5;
    public boolean actionsAreBlocked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.game);
        category = getIntent().getIntExtra("category", -1);

        gameText = (TextView) findViewById(R.id.current_word_text);
        model = new HeadsUpModel(category);
        gameText.setText(model.getCurrentWord());

        playGame();


    }

    /**
     * Method that starts the game, setups up buttons and actions
     */
    public void playGame() {
        Button correctButton = (Button) findViewById(R.id.correct_button);
        Button skipButton = (Button) findViewById(R.id.skip_button);
        final MediaPlayer correctSoundMP = MediaPlayer.create(this, R.raw.correct);
        final MediaPlayer passSoundMP = MediaPlayer.create(this, R.raw.pass);

        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct();
                correctSoundMP.start();

            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass();
                passSoundMP.start();
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
        intent.putExtra("points", model.getScore());
        intent.putExtra("category", category);
        intent.putExtra("correctWords", model.getAllCorrectWords().toString());
        intent.putExtra("skippedWords", model.getAllSkippedWords().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final MediaPlayer correctSoundMP = MediaPlayer.create(this, R.raw.correct);
        final MediaPlayer passSoundMP = MediaPlayer.create(this, R.raw.pass);
        double zForce = event.values[2];
        if (zForce >= ACTIONTHRESHOLD && !isBlocked()) {
            pass();
            passSoundMP.start();
        } else if (zForce <= -ACTIONTHRESHOLD && !isBlocked()) {
            correct();
            correctSoundMP.start();
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

        if (model.checkGameOver()) {
            //TODO gameover functionality
            endGame();
            return;
        }
        gameText.setText(nextWord);

    }
}