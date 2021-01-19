package com.example.fourinrowapp.PagesPackage;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fourinrowapp.UtilsPackage.GameMainHuman;
import com.example.fourinrowapp.R;
import com.example.fourinrowapp.DataPackage.TypeDBOpenHelper;

// AppCompatActivity - Give you access to use the LifeCycle of Activity.
public class GameStartHumanActivity extends AppCompatActivity {

    private static final Paint background = new Paint(); // Set Paint of background - paint light blue color on the background of the screen
    private static final Paint linesColor = new Paint(); // Set Paint of linesColor - paint lines with blue color
    private static final Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG); // Set Paint of foreground - paint X or O with blue or red color
    private TypeDBOpenHelper typeDBOpenHelper; // Set TypeDBOpenHelper - SQLiteDBOpenHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) { // onCreate - Called when Activity is first created
        super.onCreate(savedInstanceState);
        setContentView(new GraphicsView(this)); // Give you to see the design of your Activity with Canvas

    }

    // Class that contains the design of the Activity
    public class GraphicsView extends View {
        private float width, height; // Set width - width of the screen, Set height - height of the screen
        // Set x - width, Set y - height, Set count - check if X or O, Set touchStartFlag - check if the game is start, Set count2 - check if X or Y wins
        private int x, y, count = 1, touchStartFlag = 0, count2 = 0;
        private String print = ""; // Set print - contains Strings of X or O to draw them on the screen
        private final GameMainHuman game = new GameMainHuman(); // Set GameMainHuman - class that help to GameStartActivity and GraphicsView to perform the tasks

        // Constructor of GraphicsView
        public GraphicsView(Context context) {
            super(context);

            typeDBOpenHelper = new TypeDBOpenHelper(context); // Initialize TypeDBOpenHelper - SQLiteDBOpenHelper
        }

        // Draw on the screen
        protected void onDraw(Canvas canvas) {
            int i, j, k;
            // Set color of light blue to the background of the screen
            background.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.background));
            // Set color of blue to the lines on the screen
            linesColor.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.text_color));

            // Draw light blue rectangle on all the screen
            canvas.drawRect(0, 0, getWidth(), getHeight(), background);
            width = getWidth() / 7f; // Divide the width of the screen with 7
            height = getHeight() / 7f; // Divide the height of the screen with 7

            // Draw the blue lines on the screen 7 height and width lines
            for (i = 1; i <= 7; i++) {
                canvas.drawLine(i * width, 0, i * width, getHeight(), linesColor);
                canvas.drawLine((i * width) + 1, 0, (i * width) + 1, getHeight(), linesColor);
                canvas.drawLine((i * width) + 2, 0, (i * width) + 2, getHeight(), linesColor);
            }

            for (i = 1; i <= 7; i++) {
                canvas.drawLine(0, i * height, getHeight(), i * height, linesColor);
                canvas.drawLine(0, (i * height) + 1, getHeight(), (i * height) + 1, linesColor);
                canvas.drawLine(0, (i * height) + 2, getHeight(), (i * height) + 2, linesColor);
            }

            // Check if the game is start
            if (touchStartFlag == 1) {
                String result = game.gameEndCheck(y, x, print); // Return String of the shape winner(X or O)
                foreground.setStyle(Style.FILL); // Fill the shape(X or O)
                foreground.setTextSize(height * 0.75f); // Multiplier the size of the shape(X or O) with 0.75
                foreground.setTextScaleX(width / height); // Divide the scale to the shape(X or O) with width / height
                foreground.setTextAlign(Paint.Align.CENTER); // Center the shape(X or O)
                FontMetrics fm = foreground.getFontMetrics();
                float x1 = width / 2;
                float y1 = height / 2 - (fm.ascent + fm.descent) / 2;
                // Run for loop on all the squares
                for (i = 0; i < 7; i++) {
                    for (j = 0; j < 7; j++) {
                        a:
                        if (game.gameEnd == 1) { // Check if the turn is end
                            for (k = 0; k < 4; k++) {
                                if (game.pos[k][0] == i && game.pos[k][1] == j) {
                                    count2++; // Add count that check if X or Y wins
                                    break a;
                                } else {
                                    foreground.setColor(getResources().getColor(R.color.text_color)); // Set blue color to the shapes(X and O)
                                }
                            }
                            canvas.drawText(game.board[i][j], j * width + x1, i * height + y1, foreground); // Draw the shapes(X or/and O)
                        } else {
                            foreground.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.text_color)); // Set blue color to the shapes(X or O)
                            canvas.drawText(game.board[i][j], j * width + x1, i * height + y1, foreground); // Draw the shapes(X or/and O)
                        }
                        if (count2 == 4) { // Check if any shape(X or O) wins - get 4 in a kind and in a row
                            for (k = 0; k < 4; k++) { // Run for loop on all the shapes that wins
                                foreground.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.colourWin)); // Set red color to the shapes that wins(X or O)
                                canvas.drawText(game.board[game.pos[k][0]][game.pos[k][1]], game.pos[k][1] * width + x1, game.pos[k][0] * height + y1, foreground); // Draw the shapes that wins(X or O)
                            }

                            if (j == 6) {
                                typeDBOpenHelper.addType(result); // Add the String of the winner(X or Y) to TypeDBOpenHelper - SQLiteOpenHelper
                            }

                            Toast.makeText(getContext(), result + " won the game", Toast.LENGTH_SHORT).show(); // Pop Toast with the String of the winner(X or Y)
                        }
                    }
                }
            }
        }

        // Method that detects if and how the user touch on your screen
        public boolean onTouchEvent(MotionEvent event) {
            // Check if the user touch(like click, it's down on the screen) on the screen - ACTION_DOWN
            if (event.getAction() != MotionEvent.ACTION_DOWN)
                return super.onTouchEvent(event);
            if (game.gameEnd != 1) {
                x = (int) (event.getX() / width);
                y = (int) (event.getY() / height);
                float tempX = (event.getX() / width);
                float tempY = (event.getY() / height);
                touchStartFlag = 1;
                String TAG = "check1";
                Log.d(TAG, "Touched " + x + "," + y);
                Log.d(TAG, "Touched " + tempX + "," + tempY);
                if (count % 2 != 0) {
                    print = "X";
                } else {
                    print = "O";
                }
                count++;
                if (game.isInTheAir(x, y) == 0) {
                    if (game.isAlreadyFilled(x, y) == 0) {
                        game.inputData(x, y, print);
                        invalidate();
                    } else {
                        Toast.makeText(getContext(), "Already selected box", Toast.LENGTH_SHORT).show();
                        count--;
                    }
                } else {
                    Toast.makeText(getContext(), "In the air", Toast.LENGTH_SHORT).show();
                    count--;
                }
            }
            return true;
        }
    }

}
