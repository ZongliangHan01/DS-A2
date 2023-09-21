package server;

import src.main.java.remote.IRemoteGame;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;

public class RemoteGame extends UnicastRemoteObject implements IRemoteGame {
    private ArrayList<Player> players;
    private ArrayList<Match> matches;

    public RemoteGame() throws RemoteException {
        this.players = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    private Player getPlayerByName(String name) {
        for (Player player : this.players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    private  Match getMatchById(int id) {
        for (Match match : this.matches) {
            if (match.getId() == id) {
                return match;
            }
        }
        return null;
    }

    @Override
    public char[][] getBoard(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
        Match match = player.getMatch();
        if (match == null) {
            return null;
        }
        return match.getBoard();
    }

    @Override
    public int move(String name, int x, int y) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return -1;
        }
        Match match = player.getMatch();
        if (match == null) {
            return -1;
        }
        if (match.getCurrentPlayer() != player) {
            return -1;
        }
        int status =  match.move(x, y);

        if (status == 1) {
            match.changePlayer();
        }

        return status;
    }


    @Override
    public Boolean isMyTurn(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
        Match match = player.getMatch();
        if (match == null) {
            return null;
        }
        if (match.getCurrentPlayer() == player) {
            return true;
        }
        return false;
    }

    @Override
    public int addPlayer(String name) throws RemoteException {
        for (Player player : this.players) {
            if (player.getName().equals(name)) {
                return 0;
            }
        }
        Player player = new Player(name);
        this.players.add(player);
        return 1;
    }

    public int hasMatch(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        for (Match match : this.matches) {

            if (match.waitingForPlayer() && !match.playerInMatch(player)) {
                match.joinMatch(player);
                player.setMatch(match);
                match.setReady(true);
                for (Match m : this.matches) {
                    System.out.println("match id: " + m.getId() + " Player 1: " + m.getPlayer1().getName() + " Player 2: " + m.getPlayer2().getName());
                }
                System.out.println("***********************************");
                return match.getId();
            }
        }
        Match newMatch = new Match(player);
        this.matches.add(newMatch);
        player.setMatch(newMatch);
        for (Match match : this.matches) {
            System.out.println("match id: " + match.getId() + " Player 1: " + match.getPlayer1().getName());
        }
        System.out.println("***********************************");
        return newMatch.getId();
    }

    @Override
    public boolean matchReady(int matchId) throws RemoteException {
//        System.out.println("Waiting for another player");
        Match match = getMatchById(matchId);
        if (match == null) {
            return false;
        }
        return match.isReady();
    }

    @Override
    public int joinMatch(String name, int matchId) throws RemoteException {
        Player player = getPlayerByName(name);
        Match match = getMatchById(matchId);
        if (match == null) {
            return -1;
        }
        if (match.waitingForPlayer()) {
            match.joinMatch(player);
            player.setMatch(match);
            return 1;
        }
        return -1;
    }

    public boolean matchFinished(int matchId) throws RemoteException {
        Match match = getMatchById(matchId);
        if (match == null) {
            return true;
        }
        return match.isFinished();
    }
    public String getWinner(int matchId, String name) throws RemoteException {
        Match match = getMatchById(matchId);
        return match.getWinner(name);
    }

    @Override
    public String getOpponent(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
        Match match = player.getMatch();
        if (match == null) {
            return null;
        }
        return match.getOpponent(player);
    }

    @Override
    public boolean waitingForPlayer(String name) throws RemoteException {
        return false;
    }

//    public void cleanUp() throws InterruptedException {
//        while (true) {
//            for (Match match : this.matches) {
//                if (match.isFinished()) {
//                    System.out.println("Match " + match.getId() + " is finished");
//                    this.matches.remove(match);
//                }
//            }
//            Thread.sleep(2000);
//        }
//
//    }
    public void cleanUp() throws InterruptedException {
        while (true) {
            Iterator<Match> iterator = this.matches.iterator();
            while (iterator.hasNext()) {
                Match match = iterator.next();
                if (match.isFinished()) {
                    System.out.println("Match " + match.getId() + " is finished");
                    iterator.remove(); // Safely remove the match using the iterator
                }
            }
            Thread.sleep(2000);
        }
    }


    public void sendMessages( int matchId, String message) throws RemoteException {
        Match match = getMatchById(matchId);
        match.addMessage(message);
    }

    public ArrayList<String> getMessages(int matchId, String name) throws RemoteException {
        Match match = getMatchById(matchId);
        Player player = getPlayerByName(name);
        if (match == null || player == null) {
            return null;
        }
        return match.getMessages(player);
    }
}
