package com.company;

import org.mariuszgromada.math.mxparser.*;

import java.text.MessageFormat;
import javax.swing.*;
import java.awt.event.*;


public class Form extends JFrame {
    private JPanel mainPanel;
    private JButton evaluateButton;
    private JList list1;
    private JTextArea textArea1;
    private JTextField textField1;

    private static JMenuBar mBar;

    private double lastResult;
    private String lastExpression;

    private Form() {
        setMenu();
        textArea1.setEditable(false);

        //LIST:
        EquationItem sinus = new EquationItem("Sinus", "sin()");
        EquationItem cosine = new EquationItem("Cosine", "cos()");
        EquationItem tangent = new EquationItem("Tangent", "tg()");
        EquationItem cotangent = new EquationItem("Cotangent", "ctg()");
        EquationItem naturalLogarithm = new EquationItem("Natural logarithm", "lg()");

        EquationItem golden = new EquationItem("Golden ratio", "[phi]");
        EquationItem euler = new EquationItem("Euler's number", "e");
        EquationItem pi = new EquationItem("Pi", "pi");

        EquationItem plus = new EquationItem("+", "+");
        EquationItem minus = new EquationItem("-", "-");
        EquationItem multip = new EquationItem("*", "*");

        EquationItem lastRes = new EquationItem("Last result", String.valueOf(lastResult));

        DefaultListModel<EquationItem> listModel = new DefaultListModel<>();

        listModel.addElement(sinus);
        listModel.addElement(cosine);
        listModel.addElement(tangent);
        listModel.addElement(cotangent);
        listModel.addElement(naturalLogarithm);

        listModel.addElement(euler);
        listModel.addElement(golden);
        listModel.addElement(pi);

        listModel.addElement(plus);
        listModel.addElement(minus);
        listModel.addElement(multip);

        listModel.addElement(lastRes);

        list1.setModel(listModel);

        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    EquationItem selectedItem = (EquationItem) list1.getSelectedValue();
                    textField1.setText(textField1.getText() + selectedItem.getSymbol());
                    textField1.grabFocus();
                    if (selectedItem.getSymbol().contains("()")) {
                        textField1.setCaretPosition(textField1.getCaretPosition() - 1);
                    }
                }
            }
        };

        list1.addMouseListener(mouseListener);

        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    evaluate();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    textField1.setText(lastExpression);
                }
                lastRes.setSymbol(String.valueOf(lastResult));
            }
        };

        textField1.addKeyListener(keyListener);

        evaluateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                evaluate();
            }
        });
    }

    private void evaluate () {
        lastExpression = textField1.getText();
        Expression ex = new Expression(lastExpression);
        lastResult = ex.calculate();
            if (ex.getSyntaxStatus()) {
                textArea1.append(MessageFormat.format("{0} = " + lastResult, ex.getExpressionString())+ "\n");
            } else JOptionPane.showMessageDialog(null, "Wrong expression!");
            textField1.setText(null);
        }

    private void setMenu() {
        mBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        mBar.add(menu);
        JMenuItem reset = new JMenuItem("Reset");
        JMenuItem exit = new JMenuItem("Exit");
        menu.add(reset);
        menu.add(exit);

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField1.setText(null);
                textArea1.setText(null);
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public class EquationItem {
        private String name;
        private String symbol;

        EquationItem(String name, String symbol) {
             this.name = name;
             this.symbol = symbol;
        }

        @Override
        public String toString() { return this.name; }
        private String getSymbol() { return symbol; }
        void setSymbol(String symbol){ this.symbol = symbol;}
    }

    public static void main (String[]args ){
            JFrame frame = new JFrame("Calculator");
            frame.setContentPane(new Form().mainPanel);

            frame.setJMenuBar(mBar);

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLocation(500, 250);
            frame.setVisible(true);
        }
}