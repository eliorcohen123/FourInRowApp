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
import android.view.animation.ScaleAnimation;
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

public class GameStartComputerActivity extends AppCompatActivity implements Runnable {

    private final GameMainComputer gameMainComputer = new GameMainComputer();

    private byte gameState;
    private byte color;
    private byte computerPlayer;
    private boolean playBelowAchievementLevel;

    private TextView messageView;
    private View busyIndicator;
    private View playingField;
    private final ImageView[][] images = new ImageView[7][7];

    private final Drawable[] drawables = new Drawable[3];
    private final int[] colors = new int[3];

    private ValueAnimator winAnimation;

    private final DisplayMetrics displayMetrics = new DisplayMetrics();

    private MediaPlayer mpWon;
    private MediaPlayer mpLost;
    private MediaPlayer mpLevelUp0;
    private MediaPlayer mpLevelUp1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        setContentView(R.layout.game);

        messageView = findViewById(R.id.message);
        busyIndicator = findViewById(R.id.busy);
        playingField = findViewById(R.id.playingField);
        ViewGroup box = findViewById(R.id.box);
        for (int row = 0; row < 7; row++) {
            ViewGroup rowView = (ViewGroup) box.getChildAt(6 - row);
            for (int column = 0; column < 7; column++)
                images[row][column] = (ImageView) rowView.getChildAt(column);
        }
        drawables[GameMainComputer.WHITE] = ContextCompat.getDrawable(this, R.drawable.naught);
        drawables[GameMainComputer.BLACK] = ContextCompat.getDrawable(this, R.drawable.cross);
        colors[GameMainComputer.WHITE] = ContextCompat.getColor(this, R.color.f);
        colors[GameMainComputer.BLACK] = ContextCompat.getColor(this, R.color.m);
        winAnimation = ValueAnimator.ofFloat(1F, 0F);
        winAnimation.setDuration(500);
        winAnimation.setRepeatMode(ValueAnimator.REVERSE);
        winAnimation.setRepeatCount(ValueAnimator.INFINITE);
        mpWon = MediaPlayer.create(this, R.raw.won);
        mpLost = MediaPlayer.create(this, R.raw.lost);
        mpLevelUp0 = MediaPlayer.create(this, R.raw.level_up_0);
        mpLevelUp1 = MediaPlayer.create(this, R.raw.level_up_1);

        gameState = 4;
        color = GameMainComputer.WHITE;
        computerPlayer = GameMainComputer.BLACK;
        setMessage();
    }

    private static final int[] messageText = {
            R.string.msg_turn,
            R.string.msg_won,
            R.string.msg_lost,
            R.string.msg_draw,
            R.string.msg_welcome
    };

    @SuppressLint("SetTextI18n")
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

    private final Animation.AnimationListener growAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            gameMainComputer.maxLevel = 1;
            invalidateOptionsMenu();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    private Animation getGrowAnimation() {
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0f, 1f);
        fadeInAnimation.setDuration(4000);
        fadeInAnimation.setRepeatMode(Animation.REVERSE);
        fadeInAnimation.setRepeatCount(1);
        ScaleAnimation growAnimation = new ScaleAnimation(0f, 1.8f, 0f, 1.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        growAnimation.setDuration(8000);
        growAnimation.setInterpolator(new AccelerateInterpolator(8f));
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fadeInAnimation);
        animationSet.addAnimation(growAnimation);
        animationSet.setAnimationListener(growAnimationListener);
        return animationSet;
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
