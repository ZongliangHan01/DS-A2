package src.main.java.server;

public class Player {
    private String name;
    private Match match;
    private long timeStamp;

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
}
