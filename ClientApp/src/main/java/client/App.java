package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import src.main.java.remote.IRemoteGame;

public class App {
    String playerName;
    int matchId = -1;
    String opponent;
    IRemoteGame remoteGame;

    int countDown = 5;
    public App(String ip) throws NotBoundException, RemoteException, InterruptedException {
        register(ip);

//        String name = addPlayer();
//        String matchInfo = joinMatch(name);
//        String[] split = matchInfo.split(" ");
//        int matchId = Integer.parseInt(split[0]);
//        String opponent = split[1];
//        this.playerName = name;
//        this.matchId = matchId;
//        this.opponent = opponent;
    }

    public int countDown() throws RemoteException {
//        this.countDown--;
//        return this.countDown;
        return this.remoteGame.countDown(this.matchId, this.playerName);
    }

    public void resetCountDown() throws RemoteException {
//        this.countDown = 5;
        this.remoteGame.resetCountDown(this.matchId);
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getMatchId() {
        return this.matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public boolean matchFinished() throws RemoteException {
        return this.remoteGame.matchFinished(this.matchId);
    }
    public String getOpponent() {
        return this.opponent;
    }


    public void register(String host) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host);

        IRemoteGame remoteGame = (IRemoteGame) registry.lookup("TicTacToe");
        this.remoteGame = remoteGame;
        addPlayer();
    }

    public void addPlayer() throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        int playerStatus = 0;


        // add player into the game
        while (playerStatus!=1) {
            playerStatus = this.remoteGame.addPlayer(name);

            if (playerStatus == 1) {
                System.out.println("You have joined the game");
            } else if (playerStatus == 0) {
                System.out.println("Change your name: ");
                name = scanner.nextLine();
            } else {
                System.out.println("error");
            }
        }
        this.playerName = name;
    }

    public int hasMatch() throws RemoteException {
        int matchId = this.remoteGame.hasMatch(playerName);
        this.matchId = matchId;
        return matchId;
    }
    public String joinMatch() throws RemoteException, InterruptedException {
        if (!this.remoteGame.matchReady(this.matchId)) {
            System.out.println("Waiting for another player");
            Thread.sleep(500);
            return null;
        }

        String opponent = this.remoteGame.getOpponent(playerName);
        System.out.println("Your opponent is " + opponent);
        System.out.println("Match id: " + this.matchId);
//        this.matchId = matchId;
        this.opponent = opponent;
        return opponent;
    }

    public String getWinner() throws RemoteException {
        return this.remoteGame.getWinner(this.matchId, playerName);
    }

    public boolean isMyTurn() throws RemoteException {
        return this.remoteGame.isMyTurn(playerName);
    }
    public int makeMove(int x, int y) throws RemoteException {
        if (this.remoteGame.isMyTurn(playerName)) {
            char[][] board = this.remoteGame.getBoard(playerName);
            for (int i = 0; i < 3; i++) {
                System.out.println(board[i]);
            }

//            int moveStatus = 0;
            System.out.println("your turn");

            int moveStatus = this.remoteGame.move(playerName, x, y);
            if (moveStatus == -1) {
                System.out.println("Invalid move");
                return moveStatus;
            }
            resetCountDown();
            System.out.println("move made, waiting for other player moving");
            board = this.remoteGame.getBoard(playerName);
            for (int i = 0; i < 3; i++) {
                System.out.println(board[i]);
            }
            return moveStatus;
        }
        return -1;
    }
            // get x, y from GUI here

    public char[][] getBoard() throws RemoteException {
        return this.remoteGame.getBoard(playerName);
    }

    public void sendMessages( String message) throws RemoteException {
        this.remoteGame.sendMessages(matchId, message);
    }

    public ArrayList<String> getMessages() throws RemoteException {
        return this.remoteGame.getMessages(matchId, playerName);
    }

    public void playerExit() throws RemoteException {
        this.remoteGame.playerExit(playerName);
    }

    public String getRank() throws RemoteException {
        return this.remoteGame.getRank(playerName);
    }

    public String getScore() throws RemoteException {
        return this.remoteGame.getScore(playerName);
    }
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        try {
//            //Connect to the rmiregistry that is running on localhost
//            String host = args[0];
//            System.out.println(host);
////            Registry registry = LocateRegistry.getRegistry("localhost");
//            Registry registry = LocateRegistry.getRegistry(host);
//
//            IRemoteGame remoteGame = (IRemoteGame) registry.lookup("TicTacToe");
//            //Call methods on the remote object as if it was a local object
//            System.out.print("Enter your name: ");
//            String name = scanner.nextLine();
//            int playerStatus = 0;
//
//
//            // add player into the game
//            while (playerStatus!=1) {
//                playerStatus = remoteGame.addPlayer(name);
//
//                if (playerStatus == 1) {
//                    System.out.println("You have joined the game");
//                } else if (playerStatus == 0) {
//                    System.out.println("Change your name: ");
//                    name = scanner.nextLine();
//                } else {
//                    System.out.println("error");
//                }
//            }
//
//            int matchId = remoteGame.hasMatch(name);
//            while (true) {
//                while (!remoteGame.matchReady(matchId)) {
//                    System.out.println("Waiting for another player");
//                    Thread.sleep(5000);
//                }
//
//                String opponent = remoteGame.getOpponent(name);
//                System.out.println("Your opponent is " + opponent);
//                System.out.println("Match id: " + matchId);
//                String winner = remoteGame.getWinner(matchId, name);
//                while (winner.equals("No winner")) {
//
//                    if (remoteGame.isMyTurn(name)) {
//                        char[][] board = remoteGame.getBoard(name);
//                        for (int i = 0; i < 3; i++) {
//                            System.out.println(board[i]);
//                        }
//
//                        winner = remoteGame.getWinner(matchId, name);
//                        if (!winner.equals("No winner")) {
//                            continue;
//                        }
//
//                        int moveStatus = 0;
//                        System.out.println("your turn");
//                        while (moveStatus != 1) {
//                            System.out.print("Enter X: ");
//                            int x = scanner.nextInt();
//                            System.out.print("Enter Y: ");
//                            int y = scanner.nextInt();
//                            moveStatus = remoteGame.move(name, x, y);
//                            if (moveStatus == -1) {
//                                System.out.println("Invalid move");
//                            }
//                        }
//
//                        System.out.println("move made, waiting for other player moving");
//                        board = remoteGame.getBoard(name);
//                        for (int i = 0; i < 3; i++) {
//                            System.out.println(board[i]);
//                        }
//                    }
//                    winner = remoteGame.getWinner(matchId, name);
////                    if (!winner.equals("No winner")) {
////                        continue;
////                    }
//
//
//                }
//
//                if (winner.equals("Draw")) {
//                    System.out.println("It is a Draw match");
//                }
//                else {
//                    System.out.println("Winner is " + winner);
//                }
//
//
//                scanner.nextLine();
//                System.out.print("Do you want to play again? (y/n): ");
//                String playAgain = scanner.nextLine();
//                if (playAgain.equals("y")) {
//                    matchId = remoteGame.hasMatch(name);
//                } else {
//                    System.out.println(playAgain);
//                    break;
//                }
//
//            }
//            // check if there is a match, if not, create a match
//
//
//
//
//
//        }catch(Exception e) {
//            e.printStackTrace();
//        }

//    }
}
