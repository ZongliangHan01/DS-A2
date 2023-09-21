package src.main.java.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteGame extends Remote {
    // return a list of points of given name on the board
    public char[][] getBoard(String name) throws RemoteException;

    // set a point on the board with given name
    public int move( String name, int x, int y) throws RemoteException;

    // get the name of the player who's turn it is
    public Boolean isMyTurn(String name) throws RemoteException;

    public int addPlayer(String name) throws RemoteException;

    public int joinMatch(String name, int matchId) throws RemoteException;

    public boolean waitingForPlayer(String name) throws RemoteException;

    // get the name of winner
//    public String getWinner() throws RemoteException;

    public String getWinner(int matchId, String name) throws RemoteException;

    public String getOpponent(String name) throws RemoteException;
    public int hasMatch(String name) throws RemoteException;

    public boolean matchReady(int matchId) throws RemoteException;

    public void sendMessages( int matchId, String message) throws RemoteException;

    public ArrayList<String> getMessages(int matchId, String name) throws RemoteException;

    public boolean matchFinished(int matchId) throws RemoteException;
}
