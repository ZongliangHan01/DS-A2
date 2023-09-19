package src.main.java.client;

import src.main.java.remote.IRemoteGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

public class ClientGUI {
    Random random = new Random();
    JFrame frame = new JFrame();
    JPanel gamePanel = new JPanel();


    public static void main(String[] args) throws NotBoundException, RemoteException, InterruptedException {

        ClientGUI clientGUI = new ClientGUI();
//        App client = new App();
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (NullPointerException e) {
                throw new RuntimeException(e);
            }

        });

    }

    public static void createAndShowGUI() throws NotBoundException, RemoteException, InterruptedException {
        JFrame frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.getContentPane().setBackground(new Color(50,50,50));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        JPanel gamePanel = new GamePanel();
        frame.add(gamePanel);
//        String matchInfo = client.joinMatch(client.getPlayerName());
//        if (matchInfo == null) {
//            matchInfo = client.joinMatch(client.getPlayerName());
//        }

    }
    public ClientGUI() throws RemoteException, NotBoundException, InterruptedException {

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(800,800);
//        frame.getContentPane().setBackground(new Color(50,50,50));
//        frame.setLayout(new BorderLayout());
//        frame.setVisible(true);
//
//        frame.add(gamePanel);
    }

}
