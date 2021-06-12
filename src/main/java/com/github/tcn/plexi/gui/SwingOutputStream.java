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