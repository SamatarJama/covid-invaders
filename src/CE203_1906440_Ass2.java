import javax.swing.JFrame;
import java.io.IOException;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.event.*;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import java.util.*;
import java.util.List;


public class CE203_1906440_Ass2 extends JFrame {


    public CE203_1906440_Ass2() throws IOException {
        add(new Canvas());
        setTitle("1906440");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750,750);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) throws IOException {
        new CE203_1906440_Ass2();
    }
}
