package server;

import java.util.ArrayList;
import java.util.Random;

public class Match {
    int id;
    Player player1;
    Player player2;
    Player currentPlayer;
    boolean ready;
    char[][] board;

    ArrayList<String> messages = new ArrayList<>();
    ArrayList<String> messageBuffer = new ArrayList<>();
    boolean player1Finished;
    boolean player2Finished;

    boolean player1Exit;
    boolean player2Exit;

    int countDown = 5;



    public int getCountDown(String name) {
        return this.countDown;
    }

    public void resetCountDown() {
        this.countDown = 5;
    }

    public Match(Player player1) {
        this.player1 = player1;
        this.ready = false;
        this.id = new Random().nextInt(1000)+1;
        this.player1Finished = false;
        this.player2Finished = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!player1Finished && !player2Finished) {
                        System.out.println("count down: " + countDown);
                        Thread.sleep(1000); // 1 second

                        if (countDown>0 && player2 != null && player1 != null) {
                            countDown--;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();
    }

    public void playerExit(Player player) {
        if (this.player1 == player) {
            this.player1Exit = true;
//            if (this.player2 == null) {
//                this.player2Finished = true;
//            }
        }
        else {
            this.player2Exit = true;
        }
    }

    public void addMessage(String message) {
        System.out.println("message added: " + message);
        this.messages.add(message);
    }

    public ArrayList<String> getMessages(Player player) {
        ArrayList<String> playerMessages = player.getMessages();
        ArrayList<String> newMessages = new ArrayList<>();
        if (this.messages.size() > playerMessages.size()) {
            for (int i = playerMessages.size(); i < this.messages.size(); i++) {
                newMessages.add(this.messages.get(i));
                playerMessages.add(this.messages.get(i));
            }

        }

        return newMessages;
    }

    public void finishPlayer(String name) {
        if (this.player1.getName().equals(name)) {
            this.player1Finished = true;
            this.player1.cleanMessages();
        }
        else {
            this.player2Finished = true;
            this.player2.cleanMessages();
        }
    }

    public Player[] getPlayers() {
        Player[] players = {this.player1, this.player2};
        return players;
    }
    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }
    public boolean isFinished() {
        if (this.player1Finished && this.player2Finished) {
            System.out.println("match finished test test test");
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

        if (this.player1Exit) {
            finishPlayer(name);
            return this.player2.getName();
        }
        else if (this.player2Exit) {
            finishPlayer(name);
            return this.player1.getName();
        }

        for (int i = 0; i < 3; i++) {
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
                winner = board[0][0];
                break;
            }
            if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
                winner = board[0][2];
                break;
            }

            // Check rows
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
                winner = board[i][0];
                break;
            }

            // Check columns
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') {
                winner = board[0][i];
                break;
            }
        }

        // Check diagonals
//        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
//            winner = board[0][0];
//        }
//        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
//            winner = board[0][2];
//        }



        // No winner found
        if (winner == ' ' && !isBoardFull(board)) {
            return "No winner";
        }
        else if (winner==' ' && isBoardFull(board)) {
            finishPlayer(name);
            System.out.println("finish player"+name);
            return "Draw";
        }
        else if (winner == 'X') {
            finishPlayer(name);
            System.out.println("finish player"+name);
            System.out.println("winner is X");
            return this.player1.getName();
        }
        else if (winner == 'O'){
            finishPlayer(name);
            System.out.println("finish player"+name);
            System.out.println("winner is O");
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
                if (board[i][j] == 0) {
                    System.out.println(board[i][j]);
                    return false; // Empty cell found
                }
            }
        }
        System.out.println("board full");
        return true; // All cells are filled
    }


}
