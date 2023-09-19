package src.main.java.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Scanner;

import src.main.java.remote.IRemoteGame;
import src.main.java.remote.IRemoteMath;
public class App {
    String playerName;
    int matchId;
    String opponent;
    IRemoteGame remoteGame;
    public App() throws NotBoundException, RemoteException, InterruptedException {
        register("localhost");

//        String name = addPlayer();
//        String matchInfo = joinMatch(name);
//        String[] split = matchInfo.split(" ");
//        int matchId = Integer.parseInt(split[0]);
//        String opponent = split[1];
//        this.playerName = name;
//        this.matchId = matchId;
//        this.opponent = opponent;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public int getMatchId() {
        return this.matchId;
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

    public int hasMatch(String name) throws RemoteException {
        int matchId = this.remoteGame.hasMatch(name);
        return matchId;
    }
    public String joinMatch(String name, int matchId) throws RemoteException, InterruptedException {
        /// this will always create new match
        if (!this.remoteGame.matchReady(matchId)) {
            System.out.println("Waiting for another player");
            Thread.sleep(5000);
            return null;
        }

        String opponent = this.remoteGame.getOpponent(name);
        System.out.println("Your opponent is " + opponent);
        System.out.println("Match id: " + matchId);
        this.matchId = matchId;
        this.opponent = opponent;
        return opponent;
    }

    public String getWinner(String name, int matchId) throws RemoteException {
        return this.remoteGame.getWinner(matchId, name);
    }

    public int makeMove(String name, int x, int y) throws RemoteException {
        if (this.remoteGame.isMyTurn(name)) {
            char[][] board = this.remoteGame.getBoard(name);
            for (int i = 0; i < 3; i++) {
                System.out.println(board[i]);
            }

//            int moveStatus = 0;
            System.out.println("your turn");

            int moveStatus = this.remoteGame.move(name, x, y);
            if (moveStatus == -1) {
                System.out.println("Invalid move");
                return moveStatus;
            }

            System.out.println("move made, waiting for other player moving");
            board = this.remoteGame.getBoard(name);
            for (int i = 0; i < 3; i++) {
                System.out.println(board[i]);
            }
            return moveStatus;
        }
        return -1;
    }
            // get x, y from GUI here

    char[][] getBoard(String name) throws RemoteException {
        return this.remoteGame.getBoard(name);
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
