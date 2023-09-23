package server;

import src.main.java.remote.IRemoteGame;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
//        Match match = player.getMatch();
        Match match = getMatchById(player.getMatchId());
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
//        Match match = player.getMatch();
        Match match = getMatchById(player.getMatchId());
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
//        Match match = player.getMatch();
        Match match = getMatchById(player.getMatchId());
        if (match == null) {
            return null;
        }
        if (match.getCurrentPlayer() == player) {
            return true;
        }
        return false;
    }

    @Override
    public synchronized int addPlayer(String name) throws RemoteException {
        for (Player player: this.players) {
            if (player.getName().equals(name)) {
                return 0;
            }
        }
        File jsonFile = new File("/Users/zonglianghan/Desktop/DS/DS-A2/serverApp/src/main/java/server/Database.json");
        List<Player> users = loadDatabase(jsonFile);
        for (Player user : users) {
            if (user.getName().equals(name)) {
//                return 0;
                this.players.add(user);
                return 1;

            }
        }
        Player user = new Player(name, 0, users.size()+1);
        this.players.add(user);
//        users.add(user);
        writeIntoDatabase(jsonFile, users, user);
        return 1;
    }

    public int hasMatch(String name) throws RemoteException {
        File jsonFile = new File("/Users/zonglianghan/Desktop/DS/DS-A2/serverApp/src/main/java/server/Database.json");
        List<Player> users = loadDatabase(jsonFile);

        Player player = getPlayerByName(name);

        for (Match match : this.matches) {

            if (match.waitingForPlayer() && !match.playerInMatch(player)) {
                match.joinMatch(player);
//                player.setMatch(match);
                player.setMatchId(match.getId());
                writeIntoDatabase(jsonFile, users, player);
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
//        player.setMatch(newMatch);
        player.setMatchId(newMatch.getId());
        writeIntoDatabase(jsonFile, users, player);
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
//            player.setMatch(match);
            player.setMatchId(match.getId());
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
        String winner = match.getWinner(name);
        if (!winner.equals("No winner")) {
            File jsonFile = new File("/Users/zonglianghan/Desktop/DS/DS-A2/serverApp/src/main/java/server/Database.json");
            List<Player> users = loadDatabase(jsonFile);
            Player player;
            if (winner.equals(name)) {
                player = getPlayerByName(winner);
            } else {
                player = getPlayerByName(name);
            }

            writeIntoDatabase(jsonFile, users, player);
        }

        return winner;
    }

    @Override
    public String getOpponent(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
//        Match match = player.getMatch();
        Match match = getMatchById(player.getMatchId());
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

    public void playerExit(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return;
        }

//        Match match = player.getMatch();
        Match match = getMatchById(player.getMatchId());
        if (match == null) {
            this.players.remove(player);
            return;
        }
        if (match.getPlayer2() == null) {
            this.matches.remove(match);
            this.players.remove(player);
            return;
        }
        match.playerExit(player);
        this.players.remove(player);
    }

    @Override
    public int countDown(int matchId, String name) throws RemoteException {
        Match match = getMatchById(matchId);
        if (match == null) {
            return 30;
        }
        return match.getCountDown(name);
    }

    @Override
    public void resetCountDown(int matchId) throws RemoteException {
        getMatchById(matchId).resetCountDown();
    }

    public String getRank(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
        return String.valueOf(player.getRank());
    }

    public String getScore(String name) throws RemoteException {
        Player player = getPlayerByName(name);
        if (player == null) {
            return null;
        }
        return String.valueOf(player.getScore());
    }

    private List<Player> loadDatabase(File jsonFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//            File jsonFile = new File("src/main/java/server/dictionary.json");
//            Player[] players = objectMapper.readValue(jsonFile, Player[].class);
            List<Player> players = Arrays.asList(objectMapper.readValue(jsonFile, Player[].class));
            for (Player player : players) {
                System.out.println(player.getName());
            }
            return players;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void writeIntoDatabase(File jsonFile, List<Player> players, Player player) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            List<Player> updatedPlayers = new ArrayList<>(players);
            for (Player p : players) {
                if (p.getName().equals(player.getName())) {
                    updatedPlayers.remove(p);
                }
            }
            updatedPlayers.add(player);
            updateRank(updatedPlayers);
            objectMapper.writeValue(jsonFile, updatedPlayers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRank(List<Player> players) {
        // Sort the players by score in descending order
        Collections.sort(players, (player1, player2) -> Double.compare(player2.getScore(), player1.getScore()));

        // Assign ranks based on the sorted order
        int rank = 1;
        for (Player player : players) {
            player.setRank(rank);
            rank++;
        }
    }
}
