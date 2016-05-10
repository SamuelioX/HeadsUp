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

public class MainMenuActivity extends AppCompatActivity {
    //constants
    public static final int CELEBCATEGORY = 0;
    public static final int ANIMALCATEGORY = 1;
    public static final int CARTOONCATEGORY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //buttons that set which game mode is played
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
        Button celebButton = (Button)findViewById(R.id.category_button1);
        celebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("category", CELEBCATEGORY);
                startActivity(intent);
            }
        });
        Button animalButton = (Button)findViewById(R.id.category_button2);
        animalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("category", ANIMALCATEGORY);
                startActivity(intent);
            }
        });
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
