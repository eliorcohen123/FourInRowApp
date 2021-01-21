package com.example.fourinrowapp.PagesPackage;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
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

    private void setMessage() {
        if (gameState == GameMainComputer.RUNNING && color == computerPlayer) {
            messageView.setVisibility(View.INVISIBLE);
            busyIndicator.setVisibility(View.VISIBLE);
        } else if (canDrop) {
            messageView.setText(messageText[gameState]);
            messageView.setVisibility(View.VISIBLE);
            busyIndicator.setVisibility(View.INVISIBLE);
        } else {
            messageView.setVisibility(View.INVISIBLE);
            busyIndicator.setVisibility(View.INVISIBLE);
        }
    }

    private void executeMove(byte column) {
        drop(color, column, gameMainComputer.getTop(column));
        gameMainComputer.drop(column, color);
        gameState = gameMainComputer.winner(column);
        switch (gameState) {
            case GameMainComputer.RUNNING:
                color = GameMainComputer.opposite(color);
                if (color == computerPlayer)
                    new SearchTask().execute();
                break;
            case GameMainComputer.WHITE:
            case GameMainComputer.BLACK:
                messageView.postDelayed(this, 750);
                if (color == computerPlayer)
                    mpLost.start();
                else if (playBelowAchievementLevel)
                    mpWon.start();
                else {
                    (gameMainComputer.maxLevel % 2 == 0 ? mpLevelUp0 : mpLevelUp1).start();
                    playingField.setAnimation(getFadeOutAnimation());
                }
                break;
            default:
        }
        setMessage();
    }

    private final ImageView[] flashing = new ImageView[49];
    private int coordinateNumber;

    public void run() {
        coordinateNumber = 0;
        for (byte row = 0; row < 7; row++)
            for (byte column = 0; column < 7; column++)
                if (gameMainComputer.winner(row, column) == gameState)
                    flashing[coordinateNumber++] = images[row][column];
        winAnimation.addUpdateListener(animator -> {
            for (int i = 0; i < coordinateNumber; i++)
                flashing[i].setAlpha((float) animator.getAnimatedValue());
        });
        winAnimation.start();
    }

    private void clearBoard() {
        for (int row = 6; row >= 0; row--)
            for (int column = 0; column < 7; column++) {
                images[row][column].setAlpha(1F);
                images[row][column].setImageDrawable(null);
            }
    }

    private Animation getFadeOutAnimation() {
        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1f, 0f);
        fadeOutAnimation.setDuration(4000);
        fadeOutAnimation.setRepeatMode(Animation.REVERSE);
        fadeOutAnimation.setRepeatCount(1);
        return fadeOutAnimation;
    }

    private final Animation.AnimationListener fallAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            synchronized (drops) {
                Integer stone = drops.poll();
                if (stone == null) {
                    canDrop = true;
                    setMessage();
                } else
                    drop(stone);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation getFallAnimation(int row) {
        float deltaPixels = (6 - row) * 36 * displayMetrics.density;
        int deltaMillis = (int) Math.sqrt(50000D * (6 - row));
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(250);
        TranslateAnimation fallAnimation = new TranslateAnimation(0f, 0f, -deltaPixels, 0f);
        fallAnimation.setDuration(deltaMillis);
        fallAnimation.setInterpolator(new AccelerateInterpolator(8f));
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fadeInAnimation);
        animationSet.addAnimation(fallAnimation);
        animationSet.setAnimationListener(fallAnimationListener);
        return animationSet;
    }

    private final LinkedList<Integer> drops = new LinkedList<>();

    private boolean canDrop = true;

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
            if (canDrop) {
                canDrop = false;
                setMessage();
                drop(stone);
            } else
                drops.offer(stone);
        }
    }

    private void tryColumn(byte column) {
        if (gameState == 4)
            startGame();
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

    public void startGame() {
        winAnimation.removeAllUpdateListeners();
        if (winAnimation.isRunning())
            winAnimation.end();
        drops.clear();
        canDrop = true;
        gameMainComputer.init();
        gameState = GameMainComputer.RUNNING;
        color = GameMainComputer.WHITE;
        clearBoard();
        playBelowAchievementLevel = gameMainComputer.maxLevel < 1;
        if (color == computerPlayer)
            new SearchTask().execute();
    }

    public void click(View v) {
        if (gameState > GameMainComputer.RUNNING) {
            startGame();
            setMessage();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SearchTask extends AsyncTask<Void, Void, Byte> {
        @Override
        protected Byte doInBackground(Void... voids) {
            return gameMainComputer.search(computerPlayer);
        }

        @Override
        protected void onPostExecute(@Nullable Byte result) {
            if (result != null && gameMainComputer.isOption(result))
                executeMove(result);
            else {
                gameState = 5;
                setMessage();
            }
        }
    }

}
