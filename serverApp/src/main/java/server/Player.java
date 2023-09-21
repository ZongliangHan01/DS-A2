package server;

import java.util.ArrayList;

public class Player {
    private String name;
    private Match match;
    private long timeStamp;

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
        this.match = null;
    }

    public String getName() {
        return this.name;
    }

    public Match getMatch() {
        return this.match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage(String message) {
        System.out.println(message);
        return message;
    }
}
