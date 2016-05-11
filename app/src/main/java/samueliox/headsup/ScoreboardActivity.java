package samueliox.headsup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * This activity is for the end of the game. It shows the results and lets users start over.
 */
public class ScoreboardActivity extends AppCompatActivity {
    /**
     * Life cycle method called when score board is created
     * @param savedInstanceState any saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.scoreboard);

        //textviews showing results of the game
        TextView currentWordText = (TextView)findViewById(R.id.correct_words_text);
        TextView skippedWordText = (TextView)findViewById(R.id.skipped_words_text);
        TextView scoreText = (TextView)findViewById(R.id.score_text);

        //gets the portions of text that populate the textviews
        final int score = getIntent().getIntExtra("points", -1);
        final int category = getIntent().getIntExtra("category", 1);
        String correctWords = getIntent().getStringExtra("correctWords");
        String skippedWords = getIntent().getStringExtra("skippedWords");

        // button to reply last played category
        Button replayButton = (Button)findViewById(R.id.replay_button);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("category", category);
                startActivity(intent);
            }
        });

        // Button to go back to main menu
        Button mainMenuButton = (Button)findViewById(R.id.main_menu_button);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
            }
        });

        //sets the textviews
        currentWordText.setText(correctWords);
        skippedWordText.setText(skippedWords);
        scoreText.append("" + score);
    }
}
