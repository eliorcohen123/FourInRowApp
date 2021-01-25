package com.example.fourinrowapp.UtilsPackage;

import java.util.Arrays;

public class GameMainComputer {

    // Static board definition
    public static final byte SPACE = 0; // Set SPACE - the square with no X or Y
    public static final byte WHITE = 1; // Set WHITE color
    public static final byte BLACK = 2; // Set BLACK color
    public static final byte RUNNING = 0; // Set RUNNING - when some element is running
    public static final byte DRAW = 3; // Set DRAW - when the game end in draw
    private static final byte ROWS = 7; // Set ROWS - 7 rows
    private static final byte COLUMNS = 7; // Set COLUMNS - 7 rows
    private static final byte[] INITIAL_BOARD = new byte[ROWS * COLUMNS]; // Set INITIAL_BOARD - initial of board
    private static final byte[] INITIAL_TOP = new byte[COLUMNS]; // Set INITIAL_TOP - initial of the top of the board

    public static byte opposite(byte color) {
        return (byte) (3 - color);
    }

    // Static definitions for chains, specific to a 7x7 board
    private static final byte[] UL0 = new byte[]{0, 6, 12, 18};
    private static final byte[] UL1 = new byte[]{-6, 0, 6, 12};
    private static final byte[] UL2 = new byte[]{-12, -6, 0, 6};
    private static final byte[] UL3 = new byte[]{-18, -12, -6, 0};
    private static final byte[] U0 = new byte[]{0, 7, 14, 21};
    private static final byte[] U1 = new byte[]{-7, 0, 7, 14};
    private static final byte[] U2 = new byte[]{-14, -7, 0, 7};
    private static final byte[] U3 = new byte[]{-21, -14, -7, 0};
    private static final byte[] UR0 = new byte[]{0, 8, 16, 24};
    private static final byte[] UR1 = new byte[]{-8, 0, 8, 16};
    private static final byte[] UR2 = new byte[]{-16, -8, 0, 8};
    private static final byte[] UR3 = new byte[]{-24, -16, -8, 0};
    private static final byte[] R0 = new byte[]{0, 1, 2, 3};
    private static final byte[] R1 = new byte[]{-1, 0, 1, 2};
    private static final byte[] R2 = new byte[]{-2, -1, 0, 1};
    private static final byte[] R3 = new byte[]{-3, -2, -1, 0};
    private static final byte[][] C00 = new byte[][]{U0, UR0, R0};
    private static final byte[][] C01 = new byte[][]{U0, UR0, R0, R1};
    private static final byte[][] C02 = new byte[][]{U0, UR0, R0, R1, R2};
    private static final byte[][] C03 = new byte[][]{UL0, U0, UR0, R0, R1, R2, R3};
    private static final byte[][] C04 = new byte[][]{UL0, U0, R1, R2, R3};
    private static final byte[][] C05 = new byte[][]{UL0, U0, R2, R3};
    private static final byte[][] C06 = new byte[][]{UL0, U0, R3};
    private static final byte[][] C10 = new byte[][]{U0, U1, UR0, R0};
    private static final byte[][] C11 = new byte[][]{U0, U1, UR0, UR1, R0, R1};
    private static final byte[][] C12 = new byte[][]{UL1, U0, U1, UR0, UR1, R0, R1, R2};
    private static final byte[][] C13 = new byte[][]{UL0, UL1, U0, U1, UR0, UR1, R0, R1, R2, R3};
    private static final byte[][] C14 = new byte[][]{UL0, UL1, U0, U1, UR1, R1, R2, R3};
    private static final byte[][] C15 = new byte[][]{UL0, UL1, U0, U1, R2, R3};
    private static final byte[][] C16 = new byte[][]{UL0, U0, U1, R3};
    private static final byte[][] C20 = new byte[][]{U0, U1, U2, UR0, R0};
    private static final byte[][] C21 = new byte[][]{UL2, U0, U1, U2, UR0, UR1, R0, R1};
    private static final byte[][] C22 = new byte[][]{UL1, UL2, U0, U1, U2, UR0, UR1, UR2, R0, R1, R2};
    private static final byte[][] C23 = new byte[][]{UL0, UL1, UL2, U0, U1, U2, UR0, UR1, UR2, R0, R1, R2, R3};
    private static final byte[][] C24 = new byte[][]{UL0, UL1, UL2, U0, U1, U2, UR1, UR2, R1, R2, R3};
    private static final byte[][] C25 = new byte[][]{UL0, UL1, U0, U1, U2, UR2, R2, R3};
    private static final byte[][] C26 = new byte[][]{UL0, U0, U1, U2, R3};
    private static final byte[][] C30 = new byte[][]{UL3, U0, U1, U2, U3, UR0, R0};
    private static final byte[][] C31 = new byte[][]{UL2, UL3, U0, U1, U2, U3, UR0, UR1, R0, R1};
    private static final byte[][] C32 = new byte[][]{UL1, UL2, UL3, U0, U1, U2, U3, UR0, UR1, UR2, R0, R1, R2};
    private static final byte[][] C33 = new byte[][]{UL0, UL1, UL2, UL3, U0, U1, U2, U3, UR0, UR1, UR2, UR3, R0, R1, R2, R3};
    private static final byte[][] C34 = new byte[][]{UL0, UL1, UL2, U0, U1, U2, U3, UR1, UR2, UR3, R1, R2, R3};
    private static final byte[][] C35 = new byte[][]{UL0, UL1, U0, U1, U2, U3, UR2, UR3, R2, R3};
    private static final byte[][] C36 = new byte[][]{UL0, U0, U1, U2, U3, UR3, R3};
    private static final byte[][] C40 = new byte[][]{UL3, U1, U2, U3, R0};
    private static final byte[][] C41 = new byte[][]{UL2, UL3, U1, U2, U3, UR1, R0, R1};
    private static final byte[][] C42 = new byte[][]{UL1, UL2, UL3, U1, U2, U3, UR1, UR2, R0, R1, R2};
    private static final byte[][] C43 = new byte[][]{UL1, UL2, UL3, U1, U2, U3, UR1, UR2, UR3, R0, R1, R2, R3};
    private static final byte[][] C44 = new byte[][]{UL1, UL2, U1, U2, U3, UR1, UR2, UR3, R1, R2, R3};
    private static final byte[][] C45 = new byte[][]{UL1, U1, U2, U3, UR2, UR3, R2, R3};
    private static final byte[][] C46 = new byte[][]{U1, U2, U3, UR3, R3};
    private static final byte[][] C50 = new byte[][]{UL3, U2, U3, R0};
    private static final byte[][] C51 = new byte[][]{UL2, UL3, U2, U3, R0, R1};
    private static final byte[][] C52 = new byte[][]{UL2, UL3, U2, U3, UR2, R0, R1, R2};
    private static final byte[][] C53 = new byte[][]{UL2, UL3, U2, U3, UR2, UR3, R0, R1, R2, R3};
    private static final byte[][] C54 = new byte[][]{UL2, U2, U3, UR2, UR3, R1, R2, R3};
    private static final byte[][] C55 = new byte[][]{U2, U3, UR2, UR3, R2, R3};
    private static final byte[][] C56 = new byte[][]{U2, U3, UR3, R3};
    private static final byte[][] C60 = new byte[][]{UL3, U3, R0};
    private static final byte[][] C61 = new byte[][]{UL3, U3, R0, R1};
    private static final byte[][] C62 = new byte[][]{UL3, U3, R0, R1, R2};
    private static final byte[][] C63 = new byte[][]{UL3, U3, UR3, R0, R1, R2, R3};
    private static final byte[][] C64 = new byte[][]{U3, UR3, R1, R2, R3};
    private static final byte[][] C65 = new byte[][]{U3, UR3, R2, R3};
    private static final byte[][] C66 = new byte[][]{U3, UR3, R3};

    // Set CHAINS - create the squares
    private static final byte[][][] CHAINS = new byte[][][]{
            C00, C01, C02, C03, C04, C05, C06,
            C10, C11, C12, C13, C14, C15, C16,
            C20, C21, C22, C23, C24, C25, C26,
            C30, C31, C32, C33, C34, C35, C36,
            C40, C41, C42, C43, C44, C45, C46,
            C50, C51, C52, C53, C54, C55, C56,
            C60, C61, C62, C63, C64, C65, C66
    };

    // Static definitions for valuation
    private static final int VAL4 = 0x10000000;
    private static final int VAL3 = 0x100000;
    private static final int VAL2 = 0x1000;
    private static final int VAL1 = 0x10;
    private static final int VAL0 = 0x0;
    private static final int[] VALUE = new int[]{VAL0, VAL1, VAL2, VAL3, VAL4};

    // Instance variables and methods
    public byte maxLevel; // Set maxLevel

    public void init() {
        System.arraycopy(INITIAL_BOARD, 0, board, 0, INITIAL_BOARD.length);
        System.arraycopy(INITIAL_TOP, 0, top, 0, INITIAL_TOP.length);
    }

    public byte search(byte color) {
        long result = search(color, Integer.MIN_VALUE, Integer.MAX_VALUE, (byte) 1);
        return (byte) (result & 0xF);
    }

    // Board management
    private final byte[] board = new byte[INITIAL_BOARD.length]; // Set board - length of INITIAL_BOARD
    private final byte[] top = new byte[INITIAL_TOP.length]; // Set top - length of INITIAL_TOP

    private int index(byte row, byte column) {
        return COLUMNS * row + column;
    }

    public void drop(byte column, byte color) {
        board[index(top[column]++, column)] = color;
    }

    public void revert(byte column) {
        board[index(--top[column], column)] = SPACE;
    }

    public byte getTop(byte column) {
        return column >= 0 && column < COLUMNS ? top[column] : 0;
    }

    public boolean isOption(byte column) {
        return column >= 0 && column < COLUMNS && top[column] < ROWS;
    }

    // Board valuation
    // Method that check who is the winner of the game
    private byte winner(int base, byte[] chain) {
        byte white = 0;
        byte black = 0;
        for (byte f = 0; f < 4; f++) {
            byte stone = board[base + chain[f]];
            // Check if stone equals to WHITE - 1
            if (stone == WHITE)
                white++; // white + 1
                // Check if stone equals to BLACK - 2
            else if (stone == BLACK)
                black++; // white + 1
        }
        // Check if white equals to 4
        if (white == 4)
            return WHITE; // Return WHITE - 1
        // Check if black equals to 4
        if (black == 4)
            return BLACK; // Return BLACK - 2
        return RUNNING; // Return RUNNING - 0
    }

    // Method that check who is the winner of the game
    public byte winner(byte row, byte column) {
        int base = index(row, column);
        byte[][] chains = CHAINS[base];
        for (byte[] chain : chains) {
            byte winner = winner(base, chain);
            // Check if winner not equals to RUNNING - 0
            if (winner != RUNNING)
                return winner;
        }
        return RUNNING; // Return RUNNING - 0
    }

    // Method that check who is the winner of the game
    public byte winner(byte column) {
        byte winner = winner((byte) (top[column] - 1), column);
        // Check if winner not equals to RUNNING - 0
        if (winner != RUNNING)
            return winner;
        boolean allColumnsFull = true; // Set allColumnsFull to true
        // Run on all the COLUMNS
        for (byte c = 0; c < COLUMNS; c++)
            allColumnsFull &= top[c] >= ROWS;
        return allColumnsFull ? DRAW : RUNNING; // Return DRAW if allColumnsFull is true, Return RUNNING if allColumnsFull is false
    }

    private int value(byte color, int base, byte[] chain) {
        byte num = 0;
        for (byte f = 0; f < 4; f++) {
            byte stone = board[base + chain[f]];
            // Check if stone equals to color
            if (stone == color)
                num++; // num + 1
                // Check if stone equals to WHITE - 1 or check if stone equals to BLACK - 2
            else if (stone == WHITE || stone == BLACK)
                return VAL0;
        }
        return VALUE[num];
    }

    private int value(byte color, int base) {
        int value = 0;
        byte[][] chains = CHAINS[base];
        for (byte[] chain : chains) {
            int chainValue = value(color, base, chain);
            // Check if stone equals to color
            if (chainValue > value)
                value = chainValue; // Set value to chainValue
        }
        return value;
    }

    private int value(byte row, byte column, byte[] lowestWhite, byte[] lowestBlack) {
        int base = index(row, column);
        int valueWhite = value(WHITE, base);
        int valueBlack = value(BLACK, base);
        // Check if valueWhite big from and equals to VAL3 and lowestWhite[column] small from 0
        if (valueWhite >= VAL3 && lowestWhite[column] < 0)
            lowestWhite[column] = row; // Set lowestWhite[column] to row
        // Check if valueBlack big from and equals to VAL3 and lowestBlack[column] small from 0
        if (valueBlack >= VAL3 && lowestBlack[column] < 0)
            lowestBlack[column] = row; // Set lowestBlack[column] to row
        return valueWhite - valueBlack;
    }

    private int value(byte[] lowestWhite, byte[] lowestBlack) {
        int value = 0;
        // Run on all the COLUMNS and ROWS
        for (byte c = 0; c < COLUMNS; c++)
            for (byte r = top[c]; r < ROWS; r++)
                value += value(r, c, lowestWhite, lowestBlack);
        return value;
    }

    private int winValue(byte winner, byte level) {
        // Check if winner equals WHITE - 1, if yes - return VAL4 less (level << 4),
        // check if winner equals BLACK - 2, if yes - return (level << 4) less VAL4, if no - return 0
        return winner == WHITE ? VAL4 - (level << 4) : (winner == BLACK ? (level << 4) - VAL4 : 0);
    }

    // Search
    private int search(byte color, int alpha, int beta, byte level) {
        byte[] lowestOwn = new byte[]{-1, -1, -1, -1, -1, -1, -1};
        byte[] lowestOther = new byte[]{-1, -1, -1, -1, -1, -1, -1};
        // Set value - check if color equals to WHITE - 1, if yes - return value(lowestOwn, lowestOther), if no - value(lowestOther, lowestOwn)
        int value = color == WHITE ? value(lowestOwn, lowestOther) : value(lowestOther, lowestOwn);
        // build a sorted work list of moves
        int occupied = 1;
        int[] ratings = new int[]{0x00, 0x11, 0x22, 0x33, 0x24, 0x15, 0x06}; // low nybble equals column index
        // Run on all the COLUMNS
        for (byte c = 0; c < COLUMNS; c++) {
            byte t = top[c];
            occupied += t;
            // Check if t small than ROWS
            if (t < ROWS) { // legal move
                byte lowest = lowestOwn[c];
                // Check if lowest big from and equals from 0
                if (lowest >= 0) {
                    // Check if lowest equals to t
                    if (lowest == t) {
                        return winValue(color, level) | c;
                    } else
                        ratings[c] |= 0x200;
                }
                lowest = lowestOther[c];
                // Check if lowest big from and equals from 0
                if (lowest >= 0)
                    ratings[c] |= lowest == t ? 0x1000 : 0x100;
            } else // Illegal move
                ratings[c] = -1;
        }
        Arrays.sort(ratings); // Sort ratings to ascending numerical order
        int highestRating = ratings[COLUMNS - 1];
        byte column = (byte) (highestRating & 0xF);
        // Exit if draw
        // Check if occupied big from and equals to ROWS multiplication COLUMNS
        if (occupied >= ROWS * COLUMNS) {
            return column;
        }
        // Exit if maximum level reached and no quiescence search required
        // Check if level big from maxLevel and highestRating small from 0x1000
        if (level > maxLevel && highestRating < 0x1000) {
            return value | column;
        }
        // Go through all legal moves in the sorted list
        byte bestColumn = 0xF;
        int bestValue = color == WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE; // Set bestValue - check if color equals to WHITE - 1, if yes - Integer.MIN_VALUE, if no - Integer.MAX_VALUE
        // Run on all the COLUMNS - 1, reverse
        for (byte i = COLUMNS - 1; i >= 0; i--) { // from highest to lowest rating
            int rating = ratings[i];
            // Check if rating small from 0
            if (rating < 0) // Legal moves exhausted
                break;
            // Check if level small from and equals to maxLevel or check if rating big from and equals to 0x1000
            if (level <= maxLevel || rating >= 0x1000) { // Normal search or quiescence search
                column = (byte) (rating & 0xF);
                drop(column, color);
                value = search(opposite(color), alpha, beta, (byte) (level + 1)) & 0xFFFFFFF0;
                revert(column);
                // Check if color equals to WHITE - 1
                if (color == WHITE) {
                    // Check if value big than bestValue
                    if (value > bestValue) {
                        bestValue = value;
                        bestColumn = column;
                    }
                    // Check if value big than alpha
                    if (value > alpha)
                        alpha = value;
                } else {
                    // Check if value small than bestValue
                    if (value < bestValue) {
                        bestValue = value;
                        bestColumn = column;
                    }
                    // Check if value small than beta
                    if (value < beta)
                        beta = value;
                }
                // Check if alpha big than and equals to beta
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return bestValue | bestColumn;
    }

}
