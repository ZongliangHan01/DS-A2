package client;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextArea inputField;

    private Client client;

    public ChatPanel(Client client) throws NotBoundException, RemoteException, InterruptedException {
        this.client = client;
        setLayout(new GridBagLayout());
        setSize(300, 1000);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Player Chat");
        label.setFont(new Font("Ink Free",Font.BOLD,35));
        setSize(300, 100);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(label, gbc);

        chatArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret) chatArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
//        chatArea.setPreferredSize(new Dimension(300, 700));
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        chatArea.setFont(new Font("Ink Free",Font.PLAIN,20));

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(Color.white);
        chatPanel.setSize(300, 700);
        chatPanel.add(chatArea, BorderLayout.SOUTH);


//        JScrollPane scrollPane = new JScrollPane(chatArea);
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setPreferredSize(new Dimension(300, 700));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(scrollPane, gbc);

        inputField = new JTextArea();
        inputField.setFont(new Font("Ink Free",Font.PLAIN,15));
        inputField.setPreferredSize(new Dimension(200, 100));
        inputField.setWrapStyleWord(true);
        inputField.setLineWrap(true);

        JButton sendButton = new JButton("Send");
        sendButton.setSize(100, 100);
        sendButton.setFont(new Font("Ink Free",Font.PLAIN,20));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(inputField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(sendButton, gbc);

//        JPanel inputPanel = new JPanel(new BorderLayout());
//        inputPanel.setSize(300, 600);
//        inputPanel.add(inputField, BorderLayout.CENTER);
//        inputPanel.add(sendButton, BorderLayout.EAST);
//        add(inputPanel, BorderLayout.SOUTH);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (client.matchFinished()) {
                        Thread.sleep(100);
                    }
//                    synchronized (client) {

                    while (true) {
//                        while (!client.matchFinished()) {
//                            System.out.println("oppoent: " + client.getOpponent() + "match id: "+ client.getMatchId());
                        sendButton.setEnabled(true);
                        if (client.getMatchId() == -1 || client.getOpponent() == null) {
                            sendButton.setEnabled(false);
                            chatArea.setText("");
                        }
                        Thread.sleep(500);
                        if (client.getOpponent() != null) {
//                                System.out.println("thread is running");

                            ArrayList<String> messages = client.getMessages();
                            //                            System.out.println("Opponent: " + client.getOpponent() + " get messages");
//                            chatArea.setText("");
                            if (messages != null) {
                                for (String message : messages) {
//                                        System.out.println(message);
                                    chatArea.append(message);
                                }
                            }

                        }
                    }

                } catch (RemoteException e) {
                    System.out.println("remote exception");
//                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }



        });
        thread.start();
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String text = inputField.getText() + "\n";
                    if (text != null && !text.isEmpty()) {
//                        chatArea.append(client.getPlayerName()+": "+text + "\n");
                        inputField.setText("");
                        String message =  "Rank#" + client.getRank() + " " + client.getPlayerName()+": "+text + "\n";
                        client.sendMessages(message);
//                        System.out.println(message);
                    }
                } catch (RemoteException ex) {
                    System.out.println("remote exception");
//                    throw new RuntimeException(ex);
                }


            }
        });

//        startChat();

    }
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        chatArea.setText("");
//        startChat();
//        // Add your custom rendering logic for Panel2 here
//    }

    public void startChat() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (client.matchFinished()) {
                        Thread.sleep(100);
                    }
//                    synchronized (client) {


                    while (!client.matchFinished()) {
//                        System.out.println("oppoent: " + client.getOpponent() + "match id: "+ client.getMatchId());
                        Thread.sleep(1000);
                        if (client.getOpponent() != null) {
//                            System.out.println("thread is running");

                            ArrayList<String> messages = client.getMessages();
                            //                            System.out.println("Opponent: " + client.getOpponent() + " get messages");
//                            chatArea.setText("");
                            if (messages != null) {
                                for (String message : messages) {
//                                    System.out.println(message);
                                    chatArea.append(message);
                                }
                            }

                        }
                    }
//                    }
//                    sendButton.setEnabled(false);
                } catch (RemoteException e) {
                    System.out.println("remote exception");
//                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }



        });
        thread.start();
    }

}

