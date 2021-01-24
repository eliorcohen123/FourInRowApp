package com.example.fourinrowapp.PagesPackage;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fourinrowapp.R;
import com.example.fourinrowapp.UtilsPackage.GameMainComputer;

import java.util.LinkedList;

// AppCompatActivity - Give you access to use the LifeCycle of Activity
// Runnable - Used to create a thread
public class GameStartComputerActivity extends AppCompatActivity implements Runnable {

    private final GameMainComputer gameMainComputer = new GameMainComputer(); // Initialize GameMainComputer

    private byte gameState; // Set gameState - check if the game is start
    private byte color; // Set color - the color of us
    private byte computerPlayer; // Set computerPlayer - the color of the computer
    private boolean playBelowAchievementLevel; // Set playBelowAchievementLevel - check if the level game is below the achievement level
    private TextView messageView; // Set messageView - TextView that show message
    private View busyIndicator; // Set busyIndicator - ProgressBar that showing when the computer playing
    private View playingField; // Set playingField - RelativeLayout that surround the board of the game
    private final ImageView[][] images = new ImageView[7][7]; // Set images - the squares of the board
    private final Drawable[] drawables = new Drawable[3]; // Set drawables - the images of X or O
    private final int[] colors = new int[3]; // Set colors - the colors of X or O
    private ValueAnimator winAnimation; // Set winAnimation - do animation when we win in the game
    // Initialize displayMetrics - A structure describing general information about a display, such as its size, density, and font scaling
    private final DisplayMetrics displayMetrics = new DisplayMetrics();
    private MediaPlayer mpWon; // Set mpWon - playing song when we win in the game
    private MediaPlayer mpLost; // Set mpLost - playing song when we lose in the game
    private MediaPlayer mpLevelUp0; // Set mpLevelUp0 - playing song when we not rise level
    private MediaPlayer mpLevelUp1; // Set mpLevelUp1 - playing song when we rise level
    private ViewGroup box; // Set ViewGroup - show the board
    private final ImageView[] flashing = new ImageView[49]; // ImageView of squares
    private int coordinateNumber; // Counting the squares
    private final LinkedList<Integer> drops = new LinkedList<>(); // List<Integer> of drops in the game
    private boolean canDrop = true; // Boolean tha give us check if we are can drop X or Y or not

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate - Called when Activity is first created
        super.onCreate(savedInstanceState);
        ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        setContentView(R.layout.activity_game_start_computer); // Give you to see the design of your Activity, Give you access to use the ids of the elements

        initUI();

        // Create the squares of the board
        for (int row = 0; row < 7; row++) {
            ViewGroup rowView = (ViewGroup) box.getChildAt(6 - row);
            for (int column = 0; column < 7; column++)
                images[row][column] = (ImageView) rowView.getChildAt(column);
        }

        // Initialize the images of X and Y to us and to the computer
        drawables[GameMainComputer.WHITE] = ContextCompat.getDrawable(this, R.drawable.naught);
        drawables[GameMainComputer.BLACK] = ContextCompat.getDrawable(this, R.drawable.cross);

        // Initialize the colors of X and Y to us and to the computer
        colors[GameMainComputer.WHITE] = ContextCompat.getColor(this, R.color.f);
        colors[GameMainComputer.BLACK] = ContextCompat.getColor(this, R.color.m);

        winAnimation = ValueAnimator.ofFloat(1F, 0F); // Start the animation at full opacity to empty opacity
        winAnimation.setDuration(500); // Set the duration of the animation to 0.5 second
        winAnimation.setRepeatMode(ValueAnimator.REVERSE); // Set the repeat of the animation to reverse mode
        winAnimation.setRepeatCount(ValueAnimator.INFINITE); // Set the repeat of the animation infinity times

        mpWon = MediaPlayer.create(this, R.raw.won); // Initialize mpWon - playing song when we win in the game
        mpLost = MediaPlayer.create(this, R.raw.lost); // Initialize mpLost - playing song when we lose in the game
        mpLevelUp0 = MediaPlayer.create(this, R.raw.level_up_0); // Initialize mpLevelUp0 - playing song when we not rise level
        mpLevelUp1 = MediaPlayer.create(this, R.raw.level_up_1); // Initialize mpLevelUp1 - playing song when we rise level

        gameState = 4; // Start the game
        color = GameMainComputer.WHITE; // Set the color of us to white
        computerPlayer = GameMainComputer.BLACK; // Set the color of the computer to black

        setMessage();
    }

    private void initUI() {
        // Initialize id to the element
        messageView = findViewById(R.id.message);
        busyIndicator = findViewById(R.id.busy);
        playingField = findViewById(R.id.playingField);
        box = findViewById(R.id.box);
    }

    // List of Strings that contains messages
    private static final int[] messageText = {
            R.string.msg_turn,
            R.string.msg_won,
            R.string.msg_lost,
            R.string.msg_draw,
            R.string.msg_welcome
    };

    // Set message in messageView and visible and invisible messageView and busyIndicator
    private void setMessage() {
        // Check if gameState equals to computer player is running and if the color equals to computer player
        if (gameState == GameMainComputer.RUNNING && color == computerPlayer) {
            messageView.setVisibility(View.INVISIBLE); // Invisible messageView
            busyIndicator.setVisibility(View.VISIBLE); // Visible busyIndicator
            // Check if canDrop is true
        } else if (canDrop) {
            messageView.setText(messageText[gameState]); // Set text from messageText list to messageView
            messageView.setVisibility(View.VISIBLE); // Visible messageView
            busyIndicator.setVisibility(View.INVISIBLE); // Invisible busyIndicator
        } else {
            messageView.setVisibility(View.INVISIBLE); // Invisible messageView
            busyIndicator.setVisibility(View.INVISIBLE); // Invisible busyIndicator
        }
    }

    // Execute move of X or O
    private void executeMove(byte column) {
        drop(color, column, gameMainComputer.getTop(column));
        gameMainComputer.drop(column, color);
        gameState = gameMainComputer.winner(column); // Set number to gameState of the column that wins
        // Check which number write in gameState
        switch (gameState) {
            case GameMainComputer.RUNNING: // Number: 0
                color = GameMainComputer.opposite(color); // Set the color of opposite to color
                // Check if color equals to computerPlay
                if (color == computerPlayer)
                    new SearchTask().execute(); // Run the AsyncTASK 'SearchTask'
                break;
            case GameMainComputer.WHITE: // Number: 1
            case GameMainComputer.BLACK: // Number: 2
                messageView.postDelayed(this, 750); // Show messageView in delay of 0.75 second
                // Check if color equals to computerPlay
                if (color == computerPlayer)
                    mpLost.start(); // Playing mpLost
                    // Check if playBelowAchievementLevel if true
                else if (playBelowAchievementLevel)
                    mpWon.start(); // Playing mpWon
                else {
                    // If gameMainComputer.maxLevel % 2 equals 0 - playing mpLevelUp0, else - playing mpLevelUp1
                    (gameMainComputer.maxLevel % 2 == 0 ? mpLevelUp0 : mpLevelUp1).start();
                    playingField.setAnimation(getFadeOutAnimation()); // Set animation into playingField
                }
                break;
            default:
        }
        setMessage();
    }

    // Runnable method - run tasks on background
    @Override
    public void run() {
        coordinateNumber = 0; // Set coordinateNumber to 0
        // Run for loop on the rows and the columns of the board
        for (byte row = 0; row < 7; row++)
            for (byte column = 0; column < 7; column++)
                // Check if gameMainComputer.winner equals gameState
                if (gameMainComputer.winner(row, column) == gameState)
                    flashing[coordinateNumber++] = images[row][column]; // Set flashing[coordinateNumber + 1] to images[row][column]
        winAnimation.addUpdateListener(animator -> { // Update winAnimation and listen him
            for (int i = 0; i < coordinateNumber; i++)
                flashing[i].setAlpha((float) animator.getAnimatedValue()); // Update flashing[i] animation
        });
        winAnimation.start(); // Start winAnimation
    }

    // Clear the board
    private void clearBoard() {
        // Run for loop on the rows and the columns of the board
        for (int row = 6; row >= 0; row--)
            for (int column = 0; column < 7; column++) {
                images[row][column].setAlpha(1F); // Set images[row][column] white color on all squares
                images[row][column].setImageDrawable(null); // Remove X or O from the squares
            }
    }

    // Create animation
    private Animation getFadeOutAnimation() {
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1f, 0f); // Start animation with full opacity until empty opacity
        fadeOutAnimation.setDuration(4000); // Set duration of animation 4 seconds
        fadeOutAnimation.setRepeatMode(Animation.REVERSE); // Set repeat of animation to reverse mode
        fadeOutAnimation.setRepeatCount(1); // Count of repeat animation is 1
        return fadeOutAnimation;
    }

    // Create Animation listener
    private final Animation.AnimationListener fallAnimationListener = new Animation.AnimationListener() {

        // Run when the animation start
        @Override
        public void onAnimationStart(Animation animation) {

        }

        // Run when the animation end
        @Override
        public void onAnimationEnd(Animation animation) {
            synchronized (drops) {
                Integer stone = drops.poll();
                // Check if stone equals to null
                if (stone == null) {
                    canDrop = true; // Set canDrop to true
                    setMessage();
                } else
                    drop(stone);
            }
        }

        // Run when the animation int repeat
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    // Create animation
    private Animation getFallAnimation(int row) {
        float deltaPixels = (6 - row) * 36 * displayMetrics.density; // Set the pixels of the animation
        int deltaMillis = (int) Math.sqrt(50000D * (6 - row)); // Set the millis of the animation
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0f, 1f); // Start animation with empty opacity until full opacity
        fadeInAnimation.setDuration(250); // Set duration of animation 0.25 second
        // Set the place of the animation
        TranslateAnimation fallAnimation = new TranslateAnimation(0f, 0f, -deltaPixels, 0f);
        fallAnimation.setDuration(deltaMillis); // Set the duration of animation to deltaMillis
        fallAnimation.setInterpolator(new AccelerateInterpolator(8f));
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fadeInAnimation); // Set fadeInAnimation into animationSet
        animationSet.addAnimation(fallAnimation); // Set fallAnimation into animationSet
        animationSet.setAnimationListener(fallAnimationListener); // Set fallAnimationListener listener to animationSet
        return animationSet;
    }

    // Drop X or Y
    private void drop(@NonNull Integer stone) {
        byte color = (byte) (stone >> 16);
        byte column = (byte) ((stone >> 8) & 0xff);
        byte row = (byte) (stone & 0xff);
        ImageView image = images[row][column];
        image.setImageDrawable(drawables[color]);
        image.setColorFilter(colors[color]);
        image.startAnimation(getFallAnimation(row));
    }

    private void drop(byte color, byte column, byte row) {
        synchronized (drops) {
            Integer stone = (color << 16) | (column << 8) | row;
            // Check if canDrop is true
            if (canDrop) {
                canDrop = false; // Set canDrop to false
                setMessage();
                drop(stone);
            } else
                drops.offer(stone);
        }
    }

    private void tryColumn(byte column) {
        // Check if gameState equals 4
        if (gameState == 4)
            startGame();
        // Check if gameState equals to computer player is running and if the color equals to computer player and if gameMainComputer.isOption(column) is true
        if (gameState == GameMainComputer.RUNNING && color != computerPlayer && gameMainComputer.isOption(column))
            executeMove(column);
    }

    public void tryColumn0(View v) {
        tryColumn((byte) 0);
    }

    public void tryColumn1(View v) {
        tryColumn((byte) 1);
    }

    public void tryColumn2(View v) {
        tryColumn((byte) 2);
    }

    public void tryColumn3(View v) {
        tryColumn((byte) 3);
    }

    public void tryColumn4(View v) {
        tryColumn((byte) 4);
    }

    public void tryColumn5(View v) {
        tryColumn((byte) 5);
    }

    public void tryColumn6(View v) {
        tryColumn((byte) 6);
    }

    // Start game
    public void startGame() {
        winAnimation.removeAllUpdateListeners(); // Remove listener from winAnimation
        // Check if winAnimation.isRunning()
        if (winAnimation.isRunning())
            winAnimation.end(); // End winAnimation
        drops.clear(); // Clear all drops
        canDrop = true; // Set canDrop to true
        gameMainComputer.init();
        gameState = GameMainComputer.RUNNING; // Set gameState to 0
        color = GameMainComputer.WHITE; // Set color to white/0
        clearBoard();
        playBelowAchievementLevel = gameMainComputer.maxLevel < 1; // Set playBelowAchievementLevel to true if gameMainComputer.maxLevel is small from 1
        // Check if color equals to computerPlayer
        if (color == computerPlayer)
            new SearchTask().execute(); // Run the AsyncTASK 'SearchTask'
    }

    public void click(View v) {
        // Check if gameState more than 0
        if (gameState > GameMainComputer.RUNNING) {
            startGame();
            setMessage();
        }
    }

    // Conduct tasks
    private class SearchTask extends AsyncTask<Void, Void, Byte> {
        @Override
        protected Byte doInBackground(Void... voids) { // Perform tasks in background
            return gameMainComputer.search(computerPlayer); // Return number
        }

        @Override
        protected void onPostExecute(@Nullable Byte result) { // After "doInBackground" finish to executes the tasks the "onPostExecute" called
            // Check if result not equals to null and gameMainComputer.isOption(result) is true
            if (result != null && gameMainComputer.isOption(result))
                executeMove(result);
            else {
                gameState = 5; // Set gameState to 5
                setMessage();
            }
        }
    }

}
