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

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

public class SwingOutputStream extends OutputStream {

    private final JTextArea newOutput;

    public SwingOutputStream(JTextArea newOutput) {
        newOutput.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        this.newOutput = newOutput;
    }

    @Override
    public void write(int i) throws IOException {
        newOutput.append(String.valueOf((char) i));
        newOutput.setCaretPosition(newOutput.getDocument().getLength());
    }
}