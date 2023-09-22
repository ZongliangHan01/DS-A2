package client;

import src.main.java.remote.IRemoteGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;

public class ClientGUI  {
    Random random = new Random();
    JFrame frame = new JFrame();
//    JPanel gamePanel = new JPanel();
//
//    JPanel chatPanel = new ChatPanel();

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
        frame.setSize(1100,1000);
        frame.getContentPane().setBackground(new Color(50,50,50));
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        frame.setVisible(true);


        App client = new App();
        JPanel chatPanel = new ChatPanel(client);
//        chatPanel.setSize(300, 1000);

        JPanel gamePanel = new GamePanel(client, chatPanel);
//        gamePanel.setSize(700, 1000);

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(gamePanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(chatPanel, gbc);
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
