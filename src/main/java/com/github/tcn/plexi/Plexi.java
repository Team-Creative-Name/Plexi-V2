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