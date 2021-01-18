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

public class GameStartHumanActivity extends AppCompatActivity {

    private static final Paint background = new Paint();
    private static final Paint linesColor = new Paint();
    private static final Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TypeDBOpenHelper typeDBOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GraphicsView(this));

    }

    public class GraphicsView extends View {
        private float width;
        private float height;
        private int x, y, count = 1, touchStartFlag = 0, count2 = 0;
        private String print = "";
        private final GameMainHuman game = new GameMainHuman();

        public GraphicsView(Context context) {
            super(context);

            typeDBOpenHelper = new TypeDBOpenHelper(context);
        }

        protected void onDraw(Canvas canvas) {
            int i, j, k;
            background.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.background));
            linesColor.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.text_color));

            canvas.drawRect(0, 0, getWidth(), getHeight(), background);
            width = getWidth() / 7f;
            height = getHeight() / 7f;

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

            if (touchStartFlag == 1) {
                String result = game.gameEndCheck(y, x, print);
                foreground.setStyle(Style.FILL);
                foreground.setTextSize(height * 0.75f);
                foreground.setTextScaleX(width / height);
                foreground.setTextAlign(Paint.Align.CENTER);
                FontMetrics fm = foreground.getFontMetrics();
                float x1 = width / 2;
                float y1 = height / 2 - (fm.ascent + fm.descent) / 2;
                for (i = 0; i < 7; i++) {
                    for (j = 0; j < 7; j++) {
                        a:
                        if (game.gameEnd == 1) {
                            for (k = 0; k < 4; k++) {
                                if (game.pos[k][0] == i && game.pos[k][1] == j) {
                                    count2++;
                                    break a;
                                } else {
                                    foreground.setColor(getResources().getColor(R.color.text_color));
                                }
                            }
                            canvas.drawText(game.board[i][j], j * width + x1, i * height + y1, foreground);
                        } else {
                            foreground.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.text_color));
                            canvas.drawText(game.board[i][j], j * width + x1, i * height + y1, foreground);
                        }
                        if (count2 == 4) {
                            for (k = 0; k < 4; k++) {
                                foreground.setColor(ContextCompat.getColor(GameStartHumanActivity.this, R.color.colourWin));
                                canvas.drawText(game.board[game.pos[k][0]][game.pos[k][1]], game.pos[k][1] * width + x1, game.pos[k][0] * height + y1, foreground);
                            }

                            if (j == 6) {
                                typeDBOpenHelper.addType(result);
                            }

                            Toast.makeText(getContext(), result + " won the game", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }

        public boolean onTouchEvent(MotionEvent event) {
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
