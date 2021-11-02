/*
 *  Copyright (C) 2021 Team Creative Name, https://github.com/Team-Creative-Name
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.tcn.plexi.gui;


import com.github.tcn.plexi.discordBot.DiscordBot;
import com.github.tcn.plexi.Settings;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Objects;

public class MainView extends JFrame {

    //create objects to be in the GUI
    private final JTextArea textArea;
    //This button will have different labels depending on current state of bot - "Start" or "Stop" - default to "Start"
    private final JButton buttonState = new JButton("Start");

    //get reference to settings obj
    Settings settings = Settings.getInstance();

    //get reference to plexi obj
    DiscordBot botInstance = DiscordBot.getInstance();

    public MainView() {
        //set title of window
        if(!settings.getBranchName().matches("main")){
            setTitle("Plexi " + settings.getVersionNumber() + "  [" + settings.getBranchName() + ":" +settings.getParentHash() +"]");
        }else{
            setTitle("Plexi " + settings.getVersionNumber());
        }

        //create textarea - console output (not editable)
        textArea = new JTextArea(50, 10);
        textArea.setEditable(false);
        PrintStream guiOut = new PrintStream(new SwingOutputStream(textArea));

        //Create Icon array so we can scale our icons to look decent
        ArrayList<Image> icons = new ArrayList<>();
        icons.add(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/originalIcons/16.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/originalIcons/32.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/originalIcons/64.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/originalIcons/128.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/originalIcons/256.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/originalIcons/512.png")));
        icons.add(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/assets/originalIcons/1024.png")));

        //add that array to our program
        setIconImages(icons);

        //set system output
        //System.setErr(guiOut); //Disabled for now to bypass issue on certain platforms 
        System.setOut(guiOut);


        //Create GUI
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST; //I love how this uses cardinal directions instead of up/down/left/right

        //Add buttons
        add(buttonState, constraints);

        //add text box
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(new JScrollPane(textArea), constraints);

        //add event handler for start button
        buttonState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                startStopButton();
            }
        });

        //set default action to happen when closing window
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //register WindowListener for the window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //we only need to prompt the user for conformation if plexi is currently running
                if (botInstance.isRunning()) {
                    int usrChoice = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit? This will stop plexi.");
                    if (usrChoice == 0) {
                        //we need to properly shut the bot down at this point
                        botInstance.stopBot();
                    } else {
                        //this means that the user decided to avoid shutdown. Print to log and return.
                        Settings.getInstance().getLogger().debug("Exit attempted but avoided by user");
                        return;
                    }
                }
                //shut down Jframe and exit program
                super.windowClosing(e);
                dispose();
                LoggerFactory.getLogger("Plexi: GUI").info("GUI Closed. Exiting");
                System.exit(0);
            }
        });

        //set window properties
        setSize(400, 355);
        setMinimumSize(new Dimension(400,355));
        setLocationRelativeTo(null);

    }

    private void startStopButton() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (botInstance.isRunning()) {
                    //ask the user if they really want to shutdown
                    int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to stop Plexi?");
                    if (choice == 0) {
                        botInstance.stopBot();
                        //now that the bot is off, change button label
                        if (!botInstance.isRunning()) {
                            buttonState.setText("Start");
                        } else {
                            Settings.getInstance().getLogger().error("Stop button failed to stop Plexi");
                        }
                    }
                } else {
                    botInstance.startBot();
                    //now that the bot is on, change button label
                    if (botInstance.isRunning()) {
                        buttonState.setText("Stop");
                    }
                }
            }
        });
        thread.start();
    }

    public void disableStart() {
        buttonState.setEnabled(false);
    }

}
