package server;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Player {
    @JsonProperty("username")
    private String name;

    private int matchId;
    @JsonProperty("score")
    private double score = 0;
    @JsonProperty("rank")
    private int rank;


    public Player(@JsonProperty("username") String username, @JsonProperty("score") int score, @JsonProperty("rank") int rank) {
        this.name = username;
        this.score = score;
        this.rank = rank;
        this.matchId = -1;
    }



    private ArrayList<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void cleanMessages() {
        this.messages.clear();
    }
    public ArrayList<String> getMessages() {
        return this.messages;
    }
    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getMatchId() {
        return this.matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }


    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getMessage(String message) {
        System.out.println(message);
        return message;
    }
}
