package com.example.android.a2048;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static int[][] fieldMatrix = {
            {1,0,0,0,0},
            {2,0,0,0,0},
            {2,0,0,0,0},
            {2,0,0,0,0},
            {0,0,0,0,0}
    };

    public static int touchX;
    public static int touchY;
    public static String swipeDirection;

    public static int currentScore = 0;
    public static int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Playing Field
        TableLayout playField = findViewById(R.id.field);
        playField.setOnTouchListener(onTouchListener());

        // Temporary starting configuration
        TextView startView = findViewById(R.id.field_00);
        startView.setBackgroundColor(Color.rgb(255,0,0));

        String result = String.valueOf(fieldMatrix[0][0]) + " " + String.valueOf(fieldMatrix[0][1]) + " " + String.valueOf(fieldMatrix[0][2]) + " " + String.valueOf(fieldMatrix[0][3]) + " " + String.valueOf(fieldMatrix[0][4]) + "\n"
                + String.valueOf(fieldMatrix[1][0]) + " " + String.valueOf(fieldMatrix[1][1]) + " " + String.valueOf(fieldMatrix[1][2]) + " " + String.valueOf(fieldMatrix[1][3]) + " " + String.valueOf(fieldMatrix[1][4]) + "\n"
                + String.valueOf(fieldMatrix[2][0]) + " " + String.valueOf(fieldMatrix[2][1]) + " " + String.valueOf(fieldMatrix[2][2]) + " " + String.valueOf(fieldMatrix[2][3]) + " " + String.valueOf(fieldMatrix[2][4]) + "\n"
                + String.valueOf(fieldMatrix[3][0]) + " " + String.valueOf(fieldMatrix[3][1]) + " " + String.valueOf(fieldMatrix[3][2]) + " " + String.valueOf(fieldMatrix[3][3]) + " " + String.valueOf(fieldMatrix[3][4]) + "\n"
                + String.valueOf(fieldMatrix[4][0]) + " " + String.valueOf(fieldMatrix[4][1]) + " " + String.valueOf(fieldMatrix[4][2]) + " " + String.valueOf(fieldMatrix[4][3]) + " " + String.valueOf(fieldMatrix[4][4]);

        Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();

        updateField();
    }


    // Swiping Detection
    public OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        // Save start coordinates
                        touchX = x;
                        touchY = y;

                        //Toast.makeText(getBaseContext(),String.valueOf(touchY),Toast.LENGTH_SHORT).show();
                        break;

                    case MotionEvent.ACTION_UP:

                        // Determine distance to new coordinates
                        int distanceX = x - touchX;
                        int distanceY = y - touchY;

                        // Calculate effective direction
                        determineSwipeDirection( distanceX , distanceY );

                        // Adapt the underlying matrix
                        adaptMatrix();

                        // Update the playing field
                        updateField();

                        //Toast.makeText(getBaseContext(),String.valueOf(y),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext(),String.valueOf(distanceY),Toast.LENGTH_SHORT).show();

                        break;

                    case MotionEvent.ACTION_MOVE:
                        break;
                }
                //mainLayout.invalidate();
                return true;
            }
        };
    }


    // Determining the swipe direction
    public void determineSwipeDirection(int x, int y){

        // getting the absolutes of the swipe distances
        int absX = Math.abs(x);
        int absY = Math.abs(y);

        // stronger swipe in x-direction
        if (absX > absY){
            if (x > 0){
                swipeDirection = "right";
                Toast.makeText(getBaseContext(),swipeDirection,Toast.LENGTH_SHORT).show();
            } else {
                swipeDirection = "left";
                Toast.makeText(getBaseContext(),swipeDirection,Toast.LENGTH_SHORT).show();
            }
        }

        // stronger swipe in y-direction
        else if (absX < absY){
            if (y > 0){
                swipeDirection = "bottom";
                Toast.makeText(getBaseContext(),swipeDirection,Toast.LENGTH_SHORT).show();
            } else{
                swipeDirection = "top";
                Toast.makeText(getBaseContext(),swipeDirection,Toast.LENGTH_SHORT).show();
            }
        }

        // equally strong in x- and y-direction
        else {
            swipeDirection = "unclear";
            Toast.makeText(getBaseContext(),swipeDirection,Toast.LENGTH_SHORT).show();
        }

    }


    // Movement procedure
    public void adaptMatrix(){

        if(swipeDirection == "bottom"){
            for(int j = 0; j <= 4; j++){
                for(int k = 0; k < 4; k++){ // Moving everything in the same direction
                    for (int i = 3; i >= 0; i--) {

                        if (fieldMatrix[i + 1][j] == 0) {
                            fieldMatrix[i + 1][j] = fieldMatrix[i][j];
                            fieldMatrix[i][j] = 0;
                        }
                        else {
                            collision(i, j, i+1, j);
                        }

                    }
                }
            }
        }

        else if(swipeDirection == "top"){
            for(int j = 0; j <= 4; j++){
                for(int k = 0; k < 4; k++){ // Moving everything in the same direction
                    for (int i = 1; i <= 4; i++) {

                        if (fieldMatrix[i - 1][j] == 0) {
                            fieldMatrix[i - 1][j] = fieldMatrix[i][j];
                            fieldMatrix[i][j] = 0;
                        }
                        else {
                            collision(i, j, i-1, j);
                        }

                    }
                }
            }
        }

        else if(swipeDirection == "right"){
            for(int i = 0; i <= 4; i++){
                for(int k = 0; k < 4; k++){ // Moving everything in the same direction
                    for (int j = 3/*-k*/; j >= 0; j--) {

                        if (fieldMatrix[i][j + 1] == 0) {
                            fieldMatrix[i][j + 1] = fieldMatrix[i][j];
                            fieldMatrix[i][j] = 0;
                        }
                        else {
                            collision(i, j, i, j+1);
                        }

                    }
                }
            }
        }

        else if(swipeDirection == "left"){
            for(int i = 0; i <= 4; i++){
                for(int k = 0; k < 4; k++){ // Moving everything in the same direction
                    for (int j = 1/*+k*/; j <= 4; j++) {

                        if (fieldMatrix[i][j - 1] == 0) {
                            fieldMatrix[i][j - 1] = fieldMatrix[i][j];
                            fieldMatrix[i][j] = 0;
                        }
                        else {
                            collision(i, j, i, j-1);
                        }

                    }
                }
            }
        }

        // Spawn an additional "1" at the opposite side
        add1();


        String result = String.valueOf(fieldMatrix[0][0]) + " " + String.valueOf(fieldMatrix[0][1]) + " " + String.valueOf(fieldMatrix[0][2]) + " " + String.valueOf(fieldMatrix[0][3]) + " " + String.valueOf(fieldMatrix[0][4]) + "\n"
                + String.valueOf(fieldMatrix[1][0]) + " " + String.valueOf(fieldMatrix[1][1]) + " " + String.valueOf(fieldMatrix[1][2]) + " " + String.valueOf(fieldMatrix[1][3]) + " " + String.valueOf(fieldMatrix[1][4]) + "\n"
                + String.valueOf(fieldMatrix[2][0]) + " " + String.valueOf(fieldMatrix[2][1]) + " " + String.valueOf(fieldMatrix[2][2]) + " " + String.valueOf(fieldMatrix[2][3]) + " " + String.valueOf(fieldMatrix[2][4]) + "\n"
                + String.valueOf(fieldMatrix[3][0]) + " " + String.valueOf(fieldMatrix[3][1]) + " " + String.valueOf(fieldMatrix[3][2]) + " " + String.valueOf(fieldMatrix[3][3]) + " " + String.valueOf(fieldMatrix[3][4]) + "\n"
                + String.valueOf(fieldMatrix[4][0]) + " " + String.valueOf(fieldMatrix[4][1]) + " " + String.valueOf(fieldMatrix[4][2]) + " " + String.valueOf(fieldMatrix[4][3]) + " " + String.valueOf(fieldMatrix[4][4]);

        Toast.makeText(getBaseContext(),result,Toast.LENGTH_SHORT).show();

    }


    // Process Collisions
    public void collision(int i1, int j1, int i2, int j2){

        // Read out the values in the colliding fields
        int value1 = fieldMatrix[i1][j1];
        int value2 = fieldMatrix[i2][j2];

        // If they are identical, delete the moving element and double the receiving
        if (value1 == value2){
            fieldMatrix[i1][j1] = 0;
            fieldMatrix[i2][j2] = 2 * value1;
            updateCurrentScore(2*value1);
        }

        // Special cases with *2 and /2
        if (value1 == -1 && value2 == -1 || value1 == -2 && value2 == -2) {
            fieldMatrix[i1][j1] = value1;
            fieldMatrix[i2][j2] = value2;
        } else if (value1 == -1){
            fieldMatrix[i1][j1] = 0;
            fieldMatrix[i2][j2] = 2 * value2;
            updateCurrentScore(2*value2);
        } else if (value2 == -1) {
            fieldMatrix[i1][j1] = 0;
            fieldMatrix[i2][j2] = 2 * value1;
            updateCurrentScore(2*value1);
        } else if (value1 == -2){
            fieldMatrix[i1][j1] = 0;
            fieldMatrix[i2][j2] = value2/2;
            updateCurrentScore(2*value2);
        } else if (value2 == -2){
            fieldMatrix[i1][j1] = 0;
            fieldMatrix[i2][j2] = value1/2;
            updateCurrentScore(2*value1);
        }

    }


    // Add a "1"
    public void add1(){

        Random rand = new Random();

        // Check for Lose
        if (LoseCheck()){
            Lose();
        } else {

            // Which number to spawn
            int spawnedNumber = numberSpawn();

            if (swipeDirection == "bottom") {

                // Find a position to put the "1"
                boolean test = true;
                while (test) {
                    int randomNumber = rand.nextInt(4);
                    if (fieldMatrix[0][randomNumber] == 0) {
                        fieldMatrix[0][randomNumber] = spawnedNumber;
                        test = false;
                    }
                }
            } else if (swipeDirection == "top") {


                // Find a position to put the "1"
                boolean test = true;
                while (test) {
                    int randomNumber = rand.nextInt(4);
                    if (fieldMatrix[4][randomNumber] == 0) {
                        fieldMatrix[4][randomNumber] = spawnedNumber;
                        test = false;
                    }
                }
            } else if (swipeDirection == "left") {


                // Find a position to put the "1"
                boolean test = true;
                while (test) {
                    int randomNumber = rand.nextInt(4);
                    if (fieldMatrix[randomNumber][4] == 0) {
                        fieldMatrix[randomNumber][4] = spawnedNumber;
                        test = false;
                    }
                }
            } else if (swipeDirection == "right") {


                // Find a position to put the "1"
                boolean test = true;
                while (test) {
                    int randomNumber = rand.nextInt(4);
                    if (fieldMatrix[randomNumber][0] == 0) {
                        fieldMatrix[randomNumber][0] = spawnedNumber;
                        test = false;
                    }
                }
            }
        }

    }


    // Which number to spawn
    public int numberSpawn(){

        // Getting a random number between 1 and 100 for the propability distribution
        Random rand = new Random();
        int randomNumber = rand.nextInt(99)+1;

        // Determine the actual number
        if (randomNumber <= 60){
            return 1;
        } else if (randomNumber <= 70){
            return 2;
        } else if (randomNumber <= 90){
            return 4;
        } else if (randomNumber <= 96){
            return 8;
        } else if (randomNumber <= 98){
            return 16;
        } else if (randomNumber <= 99){
            return -1; // Equivalent to *2
        } else {
            return -2; // Equivalent to /2
        }

    }


    // Checking for a Lose
    public boolean LoseCheck(){

        if (swipeDirection == "bottom") {

            int testing = 5;
            for (int j = 0; j <= 4; j++) {
                if (fieldMatrix[0][j] != 0) {
                    testing -= 1;
                }
            }
            if (testing == 1) {
                return true;
            }
        } else if (swipeDirection == "top") {

            int testing = 5;
            for (int j = 0; j <= 4; j++) {
                if (fieldMatrix[4][j] != 0) {
                    testing -= 1;
                }
            }
            if (testing == 1) {
                return true;
            }
        } else if (swipeDirection == "left") {

            int testing = 5;
            for (int j = 0; j <= 4; j++) {
                if (fieldMatrix[j][4] != 0) {
                    testing -= 1;
                }
            }
            if (testing == 1) {
                return true;
            }
        } else if (swipeDirection == "right") {

            int testing = 5;
            for (int j = 0; j <= 4; j++) {
                if (fieldMatrix[j][0] != 0) {
                    testing -= 1;
                }
            }
            if (testing == 1) {
                return true;
            }
        }
        return false;
    }


    // Lose
    public void Lose(){

        Restart();

        // Loosing Message
        Toast.makeText(this,"You messed up!",Toast.LENGTH_LONG).show();

    }

    public void Restart(){
        // Delete the Playing Field
        for (int i = 0; i <= 4; i++){
            for (int j = 0; j <= 4; j++){
                fieldMatrix[i][j] = 0;
            }
        }
        // Re-Add the start values
        for (int i = 0; i < 5; i++){
            fieldMatrix[i][i] = 0;
        }
    }


    // Update the whole playing field
    public void updateField() {

        // iterate through the playing field and update the positions
        for (int i = 0; i <= 4; i++){
            for(int j = 0; j <= 4; j++){
                updatePlayField(i,j);
            }
        }

    }

    // Update a single playing field part
    public void updatePlayField(int i, int j){

        int value = fieldMatrix[i][j];

        // Get the correct TextView
        String TextFieldID = "field_" + String.valueOf(i) + String.valueOf(j);
        TextView field = findViewById(getResources().getIdentifier(TextFieldID, "id", getPackageName()));
        //TextView field = findViewById(R.id.field_00);

        //Toast.makeText(this, String.valueOf(value),Toast.LENGTH_SHORT).show();
        if(value >= 0) {
            field.setText(String.valueOf(value));
        } else if(value == -1){
            field.setText("x2");
        } else if(value == -2){
            field.setText("/2");
        }

        if(value == 0){
            field.setBackgroundColor(Color.rgb(255,0,0));
        } else if(value == 1){
            field.setBackgroundColor(Color.rgb(255,165,0));
        } else if(value == 2){
            field.setBackgroundColor(Color.rgb(165,255,0));
        } else if(value == 4){
            field.setBackgroundColor(Color.rgb(0,255,0));
        } else if(value == 8){
            field.setBackgroundColor(Color.rgb(0,255,165));
        } else if(value == 16){
            field.setBackgroundColor(Color.rgb(0,165,255));
        } else if(value == 32){
            field.setBackgroundColor(Color.rgb(0,0,255));
        } else if(value == 64){
            field.setBackgroundColor(Color.rgb(100,100,100));
        } else if(value == 128){
            field.setBackgroundColor(Color.rgb(200,100,100));
        } else if(value == 256){
            field.setBackgroundColor(Color.rgb(100,200,100));
        } else if(value == 512){
            field.setBackgroundColor(Color.rgb(100,100,200));
        } else if(value == 1024){
            field.setBackgroundColor(Color.rgb(50,50,50));
        } else if(value == 2048){
            field.setBackgroundColor(Color.rgb(0,0,0));
        } else if(value == -1){
            field.setBackgroundColor(Color.rgb(33,33,33));
        } else if(value == -2){
            field.setBackgroundColor(Color.rgb(200,200,200));
        }

        // notes = (TextView)findViewById(getResources().getIdentifier(VIEW_NAME, "id", getPackageName()))

    }


    public void updateCurrentScore(int add){
        currentScore += add;
        TextView currentScoreField = findViewById(R.id.currentScoreField);
        currentScoreField.setText( String.valueOf(currentScore) );
        updateHighScore();
    }

    public void updateHighScore(){
        highScore = currentScore;
        TextView highScoreField = findViewById(R.id.highScoreField);
        highScoreField.setText( String.valueOf(highScore) );
    }


}
