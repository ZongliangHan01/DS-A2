package src.main.java.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import src.main.java.remote.IRemoteGame;
import src.main.java.remote.IRemoteMath;
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            //Connect to the rmiregistry that is running on localhost
            String host = args[0];
            System.out.println(host);
//            Registry registry = LocateRegistry.getRegistry("localhost");
            Registry registry = LocateRegistry.getRegistry(host);

            IRemoteGame remoteGame = (IRemoteGame) registry.lookup("TicTacToe");
            //Call methods on the remote object as if it was a local object
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            int playerStatus = 0;


            // add player into the game
            while (playerStatus!=1) {
                playerStatus = remoteGame.addPlayer(name);

                if (playerStatus == 1) {
                    System.out.println("You have joined the game");
                } else if (playerStatus == 0) {
                    System.out.println("Change your name: ");
                    name = scanner.nextLine();
                } else {
                    System.out.println("error");
                }
            }

            int matchId = remoteGame.hasMatch(name);
            while (true) {
                while (!remoteGame.matchReady(matchId)) {
                    System.out.println("Waiting for another player");
                    Thread.sleep(5000);
                }

                String opponent = remoteGame.getOpponent(name);
                System.out.println("Your opponent is " + opponent);
                System.out.println("Match id: " + matchId);
                String winner = remoteGame.getWinner(matchId, name);
                while (winner.equals("No winner")) {

                    if (remoteGame.isMyTurn(name)) {
                        char[][] board = remoteGame.getBoard(name);
                        for (int i = 0; i < 3; i++) {
                            System.out.println(board[i]);
                        }

                        winner = remoteGame.getWinner(matchId, name);
                        if (!winner.equals("No winner")) {
                            continue;
                        }

                        int moveStatus = 0;
                        System.out.println("your turn");
                        while (moveStatus != 1) {
                            System.out.print("Enter X: ");
                            int x = scanner.nextInt();
                            System.out.print("Enter Y: ");
                            int y = scanner.nextInt();
                            moveStatus = remoteGame.move(name, x, y);
                            if (moveStatus == -1) {
                                System.out.println("Invalid move");
                            }
                        }

                        System.out.println("move made, waiting for other player moving");
                        board = remoteGame.getBoard(name);
                        for (int i = 0; i < 3; i++) {
                            System.out.println(board[i]);
                        }
                    }
                    winner = remoteGame.getWinner(matchId, name);
//                    if (!winner.equals("No winner")) {
//                        continue;
//                    }


                }

                if (winner.equals("Draw")) {
                    System.out.println("It is a Draw match");
                }
                else {
                    System.out.println("Winner is " + winner);
                }


                scanner.nextLine();
                System.out.print("Do you want to play again? (y/n): ");
                String playAgain = scanner.nextLine();
                if (playAgain.equals("y")) {
                    matchId = remoteGame.hasMatch(name);
                } else {
                    System.out.println(playAgain);
                    break;
                }

            }
            // check if there is a match, if not, create a match





        }catch(Exception e) {
            e.printStackTrace();
        }

    }
}
