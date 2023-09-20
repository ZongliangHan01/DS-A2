package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

public class GamePanel extends JPanel  implements ActionListener {


    JPanel title_panel = new JPanel();
    JPanel button_panel = new JPanel();
    JLabel textfield = new JLabel();
    JButton playAgainBtn = new JButton("Play Again");
    JButton[] buttons = new JButton[9];
    App client;
    public GamePanel() throws NotBoundException, RemoteException, InterruptedException {
        textfield.setBackground(new Color(25,25,25));
        textfield.setForeground(new Color(25,255,0));
        textfield.setFont(new Font("Ink Free",Font.BOLD,75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);

        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0,0,800,100);
        title_panel.add(textfield);

        button_panel.setLayout(new GridLayout(3,3));
        button_panel.setBackground(new Color(150,150,150));

        for(int i=0;i<9;i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli",Font.BOLD,120));
            buttons[i].setFocusable(false);
            int row = i / 3;
            int col = i % 3;
            buttons[i].putClientProperty("value", new int[]{row, col});
            buttons[i].addActionListener(this);
        }
        add(title_panel, BorderLayout.NORTH);
        add(button_panel, BorderLayout.SOUTH);

        playAgainBtn.setEnabled(false);
        playAgainBtn.setVisible(false);
        add(playAgainBtn);
        playAgainBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    reset();
                    initGame();
                } catch (NotBoundException ex) {
                    throw new RuntimeException(ex);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Call your method here
                try {
                    App client = new App();
                    GamePanel.this.client = client;
                    initGame();

                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
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
        int matchId = this.client.hasMatch(client.getPlayerName());
        textfield.setText("Waiting for another player...");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String opponent = null;
                try {
                    opponent = client.joinMatch(client.getPlayerName(), matchId);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                while (opponent == null) {
                    try {
                        opponent = client.joinMatch(client.getPlayerName(), matchId);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
//                    System.out.println("test test test");
//                    textfield.setText("Waiting for another player...");
                }
                textfield.setText("Match ID: " + matchId + " Opponent: " + opponent);
                String winner = null;
                try {
                    winner = client.getWinner(client.getPlayerName(), client.getMatchId());

                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                char[][] board;

                while (winner.equals("No winner")) {

                    try {
                        board = client.getBoard(client.getPlayerName());
                        printBoard(board);
                        winner = client.getWinner(client.getPlayerName(), client.getMatchId());

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    board = client.getBoard(client.getPlayerName());
                    printBoard(board);
                    for(int i=0;i<9;i++) {
                        buttons[i].setEnabled(false);
                    }
                    textfield.setText("Winner: " + winner);
                    playAgainBtn.setEnabled(true);
                    playAgainBtn.setVisible(true);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

            }

        });
        thread.start();
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
            client.makeMove(client.getPlayerName(), row, col);
            char[][] board = client.getBoard(client.getPlayerName());

            printBoard(board);
//            String winner = client.getWinner(client.getPlayerName(), client.getMatchId());
//            if (!winner.equals("No winner")) {
//                System.out.println("winner: " + winner);
//                textfield.setText("Winner: " + winner);
//            }

        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
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
