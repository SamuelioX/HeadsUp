package samueliox.headsup;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * Activity for when the game is underway.
 */
public class GameActivity extends AppCompatActivity implements SensorEventListener, Observer {
    // Model which holds the state of the current game
    private HeadsUpModel model;

    // The category being played
    private int category;

    // Text box that shows the current word
    private TextView gameText;

    // Sensor that reads accelerometer input
    private Sensor mySensor;

    // Constants for thresholds from the accelerometers
    public final double ACTIONTHRESHOLD = 8.5;
    public final double UNBLOCKTHRESHOLD = 3.5;

    // Used in logic that calms down the accelerometer actions.
    public boolean actionsAreBlocked = false;

    // A reference to the timer.
    private CountDownTimer countDownTimer;

    // Used for playing sound effects.
    private MediaPlayer correctSoundMP;
    private MediaPlayer passSoundMP;


    /**
     * Life cycle method that initializes this activity
     * @param savedInstanceState any saves state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.game);

        // gets category id and build model with it.
        category = getIntent().getIntExtra("category", -1);
        model = new HeadsUpModel(category);
        model.addObserver(this);

        // starts the game
        initializeGame();
    }

    /**
     * Method that starts the game, setups up buttons and actions
     */
    private void initializeGame() {
        // Initialize the first word
        gameText = (TextView) findViewById(R.id.current_word_text);
        gameText.setText(model.getCurrentWord());

        // Initialize sound effects
        correctSoundMP = MediaPlayer.create(this, R.raw.correct);
        passSoundMP = MediaPlayer.create(this, R.raw.pass);

        //adds timer to the game. Ticks every tenth of a second.
        final TextView timer = (TextView) findViewById(R.id.countDownTextView);
        countDownTimer = new CountDownTimer(60000, 100) {
            public void onTick(long millisUntilFinished) {
                // Format the time left to a single decimal place and update textbox.
                DecimalFormat df = new DecimalFormat("0.0");
                String timeLeft = df.format(millisUntilFinished / 1000d);
                timer.setText("Time Remaining: " + timeLeft);
            }
            // When the timer is over, the game ends.
            public void onFinish() {
                endGame();
            }
        };
        countDownTimer.start();

        // Setup correct button
        Button correctButton = (Button) findViewById(R.id.correct_button);
        correctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correct();
                correctSoundMP.start();

            }
        });

        // Setup pass button
        Button skipButton = (Button) findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass();
                passSoundMP.start();
            }
        });

        // Setup sensor for receiving accelerometer input
        SensorManager SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Method that ends the game and brings up the scoreboard activity
     */
    private void endGame() {
        // Stop timer in case it hasn't trigger yet.
        countDownTimer.cancel();

        //send intent into scoreboard activity
        Intent intent = new Intent(getApplicationContext(), ScoreboardActivity.class);
        intent.putExtra("points", model.getScore());
        intent.putExtra("category", category);
        intent.putExtra("correctWords", model.getAllCorrectWords().toString());
        intent.putExtra("skippedWords", model.getAllSkippedWords().toString());
        startActivity(intent);
        finish();
    }

    /**
     * Required for accelerometer interface. Not used.
     * @param sensor info for sensor
     * @param accuracy amount of accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not in use
    }

    /**
     * Call back method to give the game accelerometer info
     * @param event state of the accelerometer
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get acceleromter from z-axis
        int zAxis = 2;
        double zForce = event.values[zAxis];

        // if phone is tilted up
        if (zForce >= ACTIONTHRESHOLD && !isBlocked()) {
            pass();
            passSoundMP.reset();
            passSoundMP.start();
        }
        // If phone is tlted down
        else if (zForce <= -ACTIONTHRESHOLD && !isBlocked()) {
            correct();
            correctSoundMP.reset();
            correctSoundMP.start();
        }
        // When phone it tilted back straight. Prevents unintended actions.
        else if (zForce < UNBLOCKTHRESHOLD && zForce > -UNBLOCKTHRESHOLD && isBlocked()) {
            unblock();
        }
    }

    // Determines if the phone has returned to normal angle after it's been tilted up or down.
    private boolean isBlocked() {
        return actionsAreBlocked;
    }

    // Unblock future actions. Called when phone is tilted back straight.
    private void unblock() {
        actionsAreBlocked = false;
    }

    // Used to mark the current work correct
    private void correct() {
        model.addCorrectWord();
        actionsAreBlocked = true;
    }

    // Used the pass the current word
    private void pass() {
        model.skipCurrentWord();
        actionsAreBlocked = true;
    }

    // This is the observer code. When the model changes state, it will trigger this method
    // so that the UI gets updated.
    @Override
    public void update(Observable observable, Object data) {
        // See if the game is over.
        if (model.checkGameOver()) {
            endGame();
            return;
        }

        // Update the current word
        String nextWord = model.getCurrentWord();
        gameText.setText(nextWord);
    }
}