package samueliox.headsup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.Random;

/**
 * This activity is for the main menu. Users select the category to start the game with.
 */
public class MainMenuActivity extends AppCompatActivity {
    // Celebrity category id
    public static final int CELEBCATEGORY = 0;

    // Animal category id
    public static final int ANIMALCATEGORY = 1;

    // Cartoon category id
    public static final int CARTOONCATEGORY = 2;

    /**
     * Life cycle method that is called to initialize this activity
     * @param savedInstanceState any saves state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to start the game with a random category
        Button playButton = (Button)findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                //uses random to get 3 different numbers for category use
                Random rand = new Random();
                intent.putExtra("category", rand.nextInt(2));
                startActivity(intent);
            }
        });

        // Button to start game with celeb category
        Button celebButton = (Button)findViewById(R.id.category_button1);
        celebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("category", CELEBCATEGORY);
                startActivity(intent);
            }
        });

        // Button to start game with anmimal category
        Button animalButton = (Button)findViewById(R.id.category_button2);
        animalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("category", ANIMALCATEGORY);
                startActivity(intent);
            }
        });

        // Button to start game with animal activyt
        Button cartoonButton = (Button)findViewById(R.id.category_button3);
        cartoonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("category", CARTOONCATEGORY);
                startActivity(intent);
            }
        });


    }
}
