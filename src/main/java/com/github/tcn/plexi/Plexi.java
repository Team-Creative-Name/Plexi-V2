package com.github.tcn.plexi;

import com.github.tcn.plexi.gui.MainView;

import javax.swing.*;

public class Plexi {

    public static void main(String[] args) {

        //call getInstance in order to ensure that it is loaded before anything else
        Settings.getInstance();


        String logo =   "               /$$                     /$$\n" +
                "              | $$                    |__/\n" +
                "      /$$$$$$ | $$  /$$$$$$  /$$   /$$ /$$\n" +
                "     /$$__  $$| $$ /$$__  $$|  $$ /$$/| $$\n" +
                "    | $$  \\ $$| $$| $$$$$$$$ \\  $$$$/ | $$\n" +
                "    | $$  | $$| $$| $$_____/  >$$  $$ | $$\n" +
                "    | $$$$$$$/| $$|  $$$$$$$ /$$/\\  $$| $$\n" +
                "    | $$____/ |__/ \\_______/|__/  \\__/|__/\n" +
                "    | $$                                    \n" +
                "    | $$   Created by Team Creative Name\n" +
                "    |__/   Version " + Settings.getInstance().getVersionNumber();





        SwingUtilities.invokeLater(() -> {
            new MainView().setVisible(true);

            System.out.println(logo);
            System.out.println("\n\nPress the start button to start Plexi.");
        });
    }
}

//http://www.jsonschema2pojo.org is great!