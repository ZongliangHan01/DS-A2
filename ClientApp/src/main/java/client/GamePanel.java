package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.abs;

public class GamePanel extends JPanel  implements ActionListener {


    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JTextArea textfield = new JTextArea();

    JLabel label = new JLabel();
    JTextField scoreField = new JTextField();
    JTextField rankField = new JTextField();

    JTextArea countDownField = new JTextArea();

    JTextArea crashField = new JTextArea();
    JButton playAgainBtn = new JButton("Play Again");

    JButton exitBtn = new JButton("Exit");
    JButton[] buttons = new JButton[9];
    App client;
    public GamePanel(App client, JPanel chatPanel) throws NotBoundException, RemoteException, InterruptedException {
//        setSize(700,1000);
        setPreferredSize(new Dimension(800, 800));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel ticTacToePanel = new JPanel();
        ticTacToePanel.setLayout(new GridBagLayout());
        ticTacToePanel.setPreferredSize(new Dimension(600, 800));
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(ticTacToePanel, gbc);


        textfield.setBackground(new Color(25,25,25));
        textfield.setForeground(new Color(25,255,0));

        textfield.setFont(new Font("Ink Free",Font.BOLD,25));
//        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);
        textfield.setLineWrap(true); // Enable text wrapping
        textfield.setWrapStyleWord(true);
        textfield.setEditable(false);
        textfield.setPreferredSize(new Dimension(600, 100));
        gbc.gridx = 0;
        gbc.gridy = 0;
        ticTacToePanel.add(textfield);


        button_panel.setLayout(new GridLayout(3,3));
        button_panel.setBackground(new Color(150,150,150));
        button_panel.setPreferredSize(new Dimension(600, 600));

        for(int i=0;i<9;i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli",Font.BOLD,120));
            buttons[i].setFocusable(false);
            buttons[i].setSize(200,200);
            int row = i / 3;
            int col = i % 3;
            buttons[i].putClientProperty("value", new int[]{row, col});
            buttons[i].addActionListener(this);
        }


        gbc.gridx = 0;
        gbc.gridy = 1;
        ticTacToePanel.add(button_panel, gbc);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setPreferredSize(new Dimension(200, 800));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(infoPanel, gbc);

        label.setText("<html>Welcome<br>" + client.getPlayerName() + "</html>");
        label.setFont(new Font("MV Boli",Font.BOLD | Font.ITALIC,30));
        label.setPreferredSize(new Dimension(200, 200));
        label.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
//        gbc.insets.set(10, 10, 50, 10);
        infoPanel.add(label, gbc);

        countDownField.setSize(200,200);
        countDownField.setFont(new Font("MV Boli",Font.BOLD,30));
        countDownField.setText("20");
        countDownField.setOpaque(true);
        countDownField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets.set(50, 10, 50, 10);
        infoPanel.add(countDownField, gbc);

        scoreField.setSize(200,100);
        scoreField.setFont(new Font("MV Boli",Font.PLAIN,20));
        scoreField.setText("Score: ");
        scoreField.setOpaque(true);
        scoreField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets.set(0, 10, 0, 10);
        infoPanel.add(scoreField, gbc);

        rankField.setSize(200,200);
        rankField.setFont(new Font("MV Boli",Font.PLAIN,20));
        rankField.setText("Rank: ");
        rankField.setOpaque(true);
        rankField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
//        gbc.insets.set(10, 10, 50, 10);
        infoPanel.add(rankField, gbc);

        playAgainBtn.setEnabled(false);
        playAgainBtn.setVisible(false);
        playAgainBtn.setFont(new Font("MV Boli",Font.BOLD,20));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.insets.set(30, 10, 10, 10);
        infoPanel.add(playAgainBtn, gbc);
        playAgainBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    reset();
                    initGame();
//                    chatPanel.startChat();
                } catch (NotBoundException ex) {
                    throw new RuntimeException(ex);
                } catch (RemoteException ex) {
//                    throw new RuntimeException(ex);
                    System.out.println("remote exception");

                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        exitBtn.setEnabled(true);
        exitBtn.setVisible(true);
        exitBtn.setSize(100, 100);
        exitBtn.setFont(new Font("MV Boli",Font.BOLD,20));
        gbc.gridx = 0;
        gbc.gridy = 5;
//        gbc.insets.set(10, 10, 10, 10);
        infoPanel.add(exitBtn, gbc);
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.playerExit();
                    while (!client.matchFinished()) {}
                    System.exit(0);
                } catch (RemoteException ex) {
                    System.out.println("remote exception");
//                    throw new RuntimeException(ex);
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Call your method here
                try {
//                    App client = new App();
                    GamePanel.this.client = client;
                    initGame();

                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
//                    throw new RuntimeException(e);
                    System.out.println("remote exception");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

//        initGame(client);
    }


    public void initGame() throws NotBoundException, RemoteException, InterruptedException {
//        App client = new App();
//        this.client = client;
        int matchId = this.client.hasMatch();
        rankField.setText("Rank: " + client.getRank());
        scoreField.setText("Score: " + client.getScore());
        textfield.setText("Waiting for another player...");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String opponent = null;
                try {
                    String rank = client.getRank();

                    opponent = client.joinMatch();
                    while (opponent == null) {
                        opponent = client.joinMatch();
                    }
                    String opponentRank = client.getOpponentRank();
                    textfield.setText("Match ID: " + matchId + " Opponent: Rank#" + opponentRank + " " + opponent);
                    String winner = null;
                    winner = client.getWinner();
                    char[][] board;

                    while (winner.equals("No winner")) {
                        Thread.sleep(200);

                        board = client.getBoard();
                        printBoard(board);
                        winner = client.getWinner();
                        while (client.opponentCrashed()) {
                            textfield.setText("Opponent crashed " + abs(32- client.crashTime()));
                        }
                        if (client.isMyTurn()) {
                            textfield.setText("Match ID: " + matchId + " Opponent: Rank#" + opponentRank + " " + opponent  +"\nYour turn");
                        } else {
                            textfield.setText("Match ID: " + matchId + " Opponent: Rank#" + opponentRank + " " + opponent  +"\nRank#"+ opponentRank + " "+ opponent+"'s turn");
                        }
                    }

                    board = client.getBoard();
                    printBoard(board);
                    for(int i=0;i<9;i++) {
                        buttons[i].setEnabled(false);
                    }
                    if (winner.equals("Draw")) {
                        textfield.setText("Draw Game");
                    } else if (winner.equals(client.getPlayerName())) {
                        textfield.setText("You Win! \nWinner: Rank#" + client.getRank() + " " + winner);
                    } else {
                        textfield.setText("You Lose! \nWinner: Rank#" + client.getOpponentRank() + " " + winner);
                    }
//                    textfield.setText("Winner: " + winner);
                    rankField.setText("Rank: " + client.getRank());
                    scoreField.setText("Score: " + client.getScore());
                    playAgainBtn.setEnabled(true);
                    playAgainBtn.setVisible(true);
                    client.setMatchId(-1);

                } catch (RemoteException e) {
                    System.out.println("remote exception");
                    for (int i=0; i<=5; i++) {
                        try {
                            Thread.sleep(1000);
                            textfield.setText("Server unavailable. \nClose in " + (5-i) + " seconds");
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    System.exit(0);
//                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        thread.start();

        Thread countDownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("count down thread running*************************");
                try {
                    while (true) {
//                        if (client.getOpponent() == null) {
//                            continue;
//                        }
                        Thread.sleep(300);
                        if (client.getOpponent() != null) {
//                            System.out.println(client.getOpponent() + " is ready");
                            int countDown = client.countDown();
//                            countDownField.setText(countDown);
                            countDownField.setText(String.valueOf(countDown));
//                            System.out.println("count down: " + countDown);
                            if (countDown == 0) {
//                                Thread.sleep(700);
                                int[] value = randomMove(client.getBoard());
//                                if (value == null) {
//                                    System.out.println("do not find empty cell");
//                                    client.resetCountDown();
//                                    continue;
//
//                                }
                                if (client.isMyTurn()) {
                                    client.resetCountDown();
                                }
                                int row = value[0];
                                int col = value[1];
                                client.makeMove(row, col);
                                char[][] board = client.getBoard();
                                printBoard(board);
//                                Thread.sleep(200);


                            }
                        }
                    }

                } catch (RemoteException ex) {
//                    throw new RuntimeException(ex);
                    System.out.println("remote exception");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        countDownThread.start();


        Thread heartBeatThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(500);
                        client.sendHeartBeat();
                    }
                } catch (RemoteException e) {
//                    throw new RuntimeException(e);
//                    System.out.println("remote exception");
                    for (int i=0; i<=5; i++) {
                        try {
                            Thread.sleep(1000);
                            textfield.setText("Server unavailable. \nClose in " + (5-i) + " seconds");
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    System.exit(0);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        heartBeatThread.start();
    }

    public int[] randomMove(char[][] board) {
        // print out yhe board
        for (int i=0; i<3; i++) {
            System.out.println(board[i]);
        }
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                if (board[i][j] == '\u0000') {
                    return new int[]{i, j};
//                    System.out.println("random move: " + i + " " + j);
                }
            }
        }
        return null;
    }

    public void reset() {
        for (int i=0; i<9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
            playAgainBtn.setEnabled(false);
            playAgainBtn.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] value = (int[]) ((JButton) e.getSource()).getClientProperty("value");
        int row = value[0];
        int col = value[1];
        System.out.println(row + " " + col);
        try {
            client.makeMove( row, col);
            char[][] board = client.getBoard();

            printBoard(board);
//            String winner = client.getWinner(client.getPlayerName(), client.getMatchId());
//            if (!winner.equals("No winner")) {
//                System.out.println("winner: " + winner);
//                textfield.setText("Winner: " + winner);
//            }

        } catch (RemoteException ex) {
//            throw new RuntimeException(ex);
            System.out.println("remote exception");
        }



//        boolean player1_turn = false;
//        for(int i=0;i<9;i++) {
//            if(e.getSource()==buttons[i]) {
//                if(player1_turn) {
//                    if(buttons[i].getText()=="") {
//                        buttons[i].setForeground(new Color(255,0,0));
//                        buttons[i].setText("X");
//                        player1_turn=false;
//                        textfield.setText("O turn");
////                        check();
//                    }
//                }
//                else {
//                    if(buttons[i].getText()=="") {
//                        buttons[i].setForeground(new Color(0,0,255));
//                        buttons[i].setText("O");
//                        player1_turn=true;
//                        textfield.setText("X turn");
////                        check();
//                    }
//                }
//            }
//        }
    }

    public void printBoard(char[][] board) {
        if (board == null) {
            return;
        }
        for (int i=0; i<9; i++) {
            int r = i / 3;
            int c = i % 3;
            if (board[r][c] == 'X') {
                buttons[i].setForeground(new Color(255,0,0));
                buttons[i].setText("X");
            } else if (board[r][c] == 'O') {
                buttons[i].setForeground(new Color(0,0,255));
                buttons[i].setText("O");
            }
        }
    }
}
