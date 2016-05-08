package gui;

import model.StructureType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pawel on 07.05.16.
 */
public class MainFrame extends JFrame {

    private static final int PANEL_SIZE = 500;
    public static Timer timer;
    private static int selectedStructureIndex = 0;
    private BoardPanel boardPanel;
    private boolean periodic = false;

    public MainFrame() {
        super("Project");
        this.setLayout(new BorderLayout());
        boardPanel = new BoardPanel(PANEL_SIZE, PANEL_SIZE);
        this.add(boardPanel, BorderLayout.CENTER);

        timer = new Timer(500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.updateGrid();
                boardPanel.repaint();
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout());


        JButton startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonListener());

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ClearButtonListener());

        JCheckBox checkBox = new JCheckBox("Periodic");
        checkBox.addActionListener(new CheckBoxListener(checkBox));

        JComboBox choiceComboBox = new JComboBox(StructureType.values());
        choiceComboBox.addActionListener(e -> {
            this.selectedStructureIndex = choiceComboBox.getSelectedIndex();
            boardPanel.setSelectedIndex(this.selectedStructureIndex);
        });
        bottomPanel.add(startButton);
        bottomPanel.add(stopButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(checkBox);
        bottomPanel.add(choiceComboBox);


        this.add(bottomPanel, BorderLayout.SOUTH);


    }

    public static void main(String[] args) {

        JFrame frame = new MainFrame();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);


    }


    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!timer.isRunning())
                timer.start();
        }
    }

    private class StopButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (timer.isRunning())
                timer.stop();
        }
    }

    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boardPanel.clearAll();
        }
    }

    private class CheckBoxListener implements ActionListener {

        JCheckBox checkBox;

        public CheckBoxListener(JCheckBox checkBox) {
            this.checkBox = checkBox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            periodic = (checkBox.isSelected()) ? true : false;
            boardPanel.setPeriod(periodic);
        }
    }
}
