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

/**
 * Created by samuel on 4/20/2016.
 */
public class ScoreboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);

        //textviews
        TextView currentWordText = (TextView)findViewById(R.id.correct_words_text);
        TextView skippedWordText = (TextView)findViewById(R.id.skipped_words_text);
        TextView scoreText = (TextView)findViewById(R.id.score_text);

        //gets the portions of text that populate the textviews
        final int score = getIntent().getIntExtra("points", -1);
        String correctWords = getIntent().getStringExtra("correctWords");
        String skippedWords = getIntent().getStringExtra("skippedWords");

        Button replayButton = (Button)findViewById(R.id.replay_button);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

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
