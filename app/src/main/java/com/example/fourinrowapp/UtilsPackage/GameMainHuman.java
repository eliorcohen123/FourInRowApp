package com.example.fourinrowapp.UtilsPackage;

public class GameMainHuman {

    public String[][] board = new String[7][7];
    public int[][] pos = new int[4][2];
    public int gameEnd = 0;

    public GameMainHuman() {
        int i, j;
        for (i = 0; i < 7; i++) {
            for (j = 0; j < 7; j++) {
                board[i][j] = "";
            }
        }
    }

    public void inputData(int x, int y, String str) {
        board[y][x] = str;
    }

    public int isAlreadyFilled(int x, int y) {
        if (x != y) {
            if (board[y][x].equals("X") || board[y][x].equals("O")) {
                return 1;
            }
        } else {
            if (board[x][y].equals("X") || board[x][y].equals("O")) {
                return 1;
            }
        }
        return 0;
    }

    public int isInTheAir(int x, int y) {
        int downX;
        downX = ++y;
        if (downX <= 6) {
            if (board[downX][x].equals("")) {
                return 1;
            } else if (board[downX][x].equals("X") || board[downX][x].equals("O")) {
                return 0;
            }
        } else {
            return 0;
        }
        return 0;
    }

    public String gameEndCheck(int x, int y, String str) {
        if (isOutOfBound(x - 1, y - 1) == 0 && board[x - 1][y - 1].equals(str)) {
            if (isOutOfBound(x - 2, y - 2) == 0 && board[x - 2][y - 2].equals(str)) {
                if (isOutOfBound(x - 3, y - 3) == 0 && board[x - 3][y - 3].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x - 1;
                    pos[1][1] = y - 1;
                    pos[2][0] = x - 2;
                    pos[2][1] = y - 2;
                    pos[3][0] = x - 3;
                    pos[3][1] = y - 3;
                    gameEnd = 1;
                    return str;
                } else if (isOutOfBound(x + 1, y + 1) == 0 && board[x + 1][y + 1].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x - 1;
                    pos[1][1] = y - 1;
                    pos[2][0] = x - 2;
                    pos[2][1] = y - 2;
                    pos[3][0] = x + 1;
                    pos[3][1] = y + 1;
                    gameEnd = 1;
                    return str;
                }
            } else if (isOutOfBound(x + 1, y + 1) == 0 && board[x + 1][y + 1].equals(str)) {
                if (isOutOfBound(x + 2, y + 2) == 0 && board[x + 2][y + 2].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x - 1;
                    pos[1][1] = y - 1;
                    pos[2][0] = x + 1;
                    pos[2][1] = y + 1;
                    pos[3][0] = x + 2;
                    pos[3][1] = y + 2;
                    gameEnd = 1;
                    return str;
                }
            }
        } else if (isOutOfBound(x + 1, y + 1) == 0 && board[x + 1][y + 1].equals(str)) {
            if (isOutOfBound(x + 2, y + 2) == 0 && board[x + 2][y + 2].equals(str)) {
                if (isOutOfBound(x + 3, y + 3) == 0 && board[x + 3][y + 3].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x + 1;
                    pos[1][1] = y + 1;
                    pos[2][0] = x + 2;
                    pos[2][1] = y + 2;
                    pos[3][0] = x + 3;
                    pos[3][1] = y + 3;
                    gameEnd = 1;
                    return str;
                }
            }
        }

        if (isOutOfBound(x, y - 1) == 0 && board[x][y - 1].equals(str)) {
            if (isOutOfBound(x, y - 2) == 0 && board[x][y - 2].equals(str)) {
                if (isOutOfBound(x, y - 3) == 0 && board[x][y - 3].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x;
                    pos[1][1] = y - 1;
                    pos[2][0] = x;
                    pos[2][1] = y - 2;
                    pos[3][0] = x;
                    pos[3][1] = y - 3;
                    gameEnd = 1;
                    return str;
                } else if (isOutOfBound(x, y + 1) == 0 && board[x][y + 1].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x;
                    pos[1][1] = y - 1;
                    pos[2][0] = x;
                    pos[2][1] = y - 2;
                    pos[3][0] = x;
                    pos[3][1] = y + 1;
                    gameEnd = 1;
                    return str;
                }
            } else if (isOutOfBound(x, y + 1) == 0 && board[x][y + 1].equals(str)) {
                if (isOutOfBound(x, y + 2) == 0 && board[x][y + 2].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x;
                    pos[1][1] = y - 1;
                    pos[2][0] = x;
                    pos[2][1] = y + 1;
                    pos[3][0] = x;
                    pos[3][1] = y + 2;
                    gameEnd = 1;
                    return str;
                }
            }
        } else if (isOutOfBound(x, y + 1) == 0 && board[x][y + 1].equals(str)) {
            if (isOutOfBound(x, y + 2) == 0 && board[x][y + 2].equals(str)) {
                if (isOutOfBound(x, y + 3) == 0 && board[x][y + 3].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x;
                    pos[1][1] = y + 1;
                    pos[2][0] = x;
                    pos[2][1] = y + 2;
                    pos[3][0] = x;
                    pos[3][1] = y + 3;
                    gameEnd = 1;
                    return str;
                }
            }
        }

        if (isOutOfBound(x - 1, y + 1) == 0 && board[x - 1][y + 1].equals(str)) {
            if (isOutOfBound(x - 2, y + 2) == 0 && board[x - 2][y + 2].equals(str)) {
                if (isOutOfBound(x - 3, y + 3) == 0 && board[x - 3][y + 3].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x - 1;
                    pos[1][1] = y + 1;
                    pos[2][0] = x - 2;
                    pos[2][1] = y + 2;
                    pos[3][0] = x - 3;
                    pos[3][1] = y + 3;
                    gameEnd = 1;
                    return str;
                } else if (isOutOfBound(x + 1, y - 1) == 0 && board[x + 1][y - 1].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x - 1;
                    pos[1][1] = y + 1;
                    pos[2][0] = x - 2;
                    pos[2][1] = y + 2;
                    pos[3][0] = x + 1;
                    pos[3][1] = y - 1;
                    gameEnd = 1;
                    return str;
                }
            } else if (isOutOfBound(x + 1, y - 1) == 0 && board[x + 1][y - 1].equals(str)) {
                if (isOutOfBound(x + 2, y - 2) == 0 && board[x + 2][y - 2].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x - 1;
                    pos[1][1] = y + 1;
                    pos[2][0] = x + 1;
                    pos[2][1] = y - 1;
                    pos[3][0] = x + 2;
                    pos[3][1] = y - 2;
                    gameEnd = 1;
                    return str;
                }
            }
        } else if (isOutOfBound(x + 1, y - 1) == 0 && board[x + 1][y - 1].equals(str)) {
            if (isOutOfBound(x + 2, y - 2) == 0 && board[x + 2][y - 2].equals(str)) {
                if (isOutOfBound(x + 3, y - 3) == 0 && board[x + 3][y - 3].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x + 1;
                    pos[1][1] = y - 1;
                    pos[2][0] = x + 2;
                    pos[2][1] = y - 2;
                    pos[3][0] = x + 3;
                    pos[3][1] = y - 3;
                    gameEnd = 1;
                    return str;
                }
            }
        }

        if (isOutOfBound(x + 1, y) == 0 && board[x + 1][y].equals(str)) {
            if (isOutOfBound(x + 2, y) == 0 && board[x + 2][y].equals(str)) {
                if (isOutOfBound(x + 3, y) == 0 && board[x + 3][y].equals(str)) {
                    pos[0][0] = x;
                    pos[0][1] = y;
                    pos[1][0] = x + 1;
                    pos[1][1] = y;
                    pos[2][0] = x + 2;
                    pos[2][1] = y;
                    pos[3][0] = x + 3;
                    pos[3][1] = y;
                    gameEnd = 1;
                    return str;
                }

            }
        }
        return null;
    }

    public int isOutOfBound(int x, int y) {
        if (x >= 7 || x < 0 || y >= 7 || y < 0) {
            return 1;
        }
        return 0;
    }

}
