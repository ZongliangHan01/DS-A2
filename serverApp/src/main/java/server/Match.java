package src.main.java.server;

import java.util.Random;

public class Match {
    int id;
    Player player1;
    Player player2;
    Player currentPlayer;
    boolean ready;
    char[][] board;
    boolean player1Finished;
    boolean player2Finished;

    public Match(Player player1) {
        this.player1 = player1;
        this.ready = false;
        this.id = new Random().nextInt(1000)+1;
        this.player1Finished = false;
        this.player2Finished = false;
    }

    public void finishPlayer(String name) {
        if (this.player1.getName().equals(name)) {
            this.player1Finished = true;
        }
        else {
            this.player2Finished = true;
        }
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }
    public boolean isFinished() {
        if (this.player1Finished && this.player2Finished) {
            return true;
        }
        return false;
    }

//    public void setFinished(boolean finished) {
//        this.finished = finished;
//    }

    public boolean isReady() {
        return this.ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
    public int getId() {
        return this.id;
    }
    public boolean waitingForPlayer() {
        return this.player2 == null;
    }

    public void joinMatch(Player player2) {
        this.player2 = player2;
        this.currentPlayer = this.player1;
        this.board = new char[3][3];
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int move(int x, int y) {
        if (checkMoveValid(x, y)) {
            if (this.currentPlayer == this.player1) {
                this.board[x][y] = 'X';
            } else {
                this.board[x][y] = 'O';
            }
            return 1;
        }
        return -1;
    }

    public boolean checkMoveValid(int x, int y) {
        if (x < 0 || x > 2 || y < 0 || y > 2) {
            return false;
        }

        if (this.board[x][y] == 'X' || this.board[x][y] == 'O') {
            return false;
        }
        return true;
    }

    public char[][] getBoard() {
        return this.board;
    }

    public boolean playerInMatch(Player player) {
        if (this.player1 == player || this.player2 == player) {
            return true;
        }
        return false;
    }

    public void changePlayer() {
        if (this.currentPlayer == this.player1) {
            this.currentPlayer = this.player2;
        } else {
            this.currentPlayer = this.player1;
        }
    }

    public String getWinner(String name) {
        char[][] board = this.board;
        char winner = ' ';
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
                winner = board[i][0];
            }

            // Check columns
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') {
                winner = board[0][i];
            }
        }

        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            winner = board[0][0];
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            winner = board[0][2];
        }



        // No winner found
        if (winner == ' ' && !isBoardFull(board)) {
            return "No winner";
        }
        else if (winner==' ' && isBoardFull(board)) {
            finishPlayer(name);
            return "Draw";
        }
        else if (winner == 'X') {
            finishPlayer(name);
            return this.player1.getName();
        }
        else if (winner == 'O'){
            finishPlayer(name);
            return this.player2.getName();
        }
        return "No winner";
    }

    public String getOpponent(Player player) {
        if (this.player1 == player) {
            return this.player2.getName();
        }
        else {
            return this.player1.getName();
        }
    }
    private boolean isBoardFull(char[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // Empty cell found
                }
            }
        }
        return true; // All cells are filled
    }


}
