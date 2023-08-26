import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Calculator extends JFrame implements ActionListener {
    private JTextField textField;
    private String expression = "";

    public Calculator() {
        setTitle("Scientific Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 24));
        textField.setEditable(false);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "=", "+", "Clear"
        };

        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.setFont(new Font("Arial", Font.PLAIN, 18));
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        setLayout(new BorderLayout());
        add(textField, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "=":
                try {
                    double result = evaluateExpression(expression);
                    textField.setText(String.valueOf(result));
                } catch (NumberFormatException | ArithmeticException ex) {
                    textField.setText("Error");
                }
                expression = "";
                break;
            case "Clear":
                expression = "";
                textField.setText("");
                break;
            default:
                expression += command;
                textField.setText(expression);
        }
    }

    private double evaluateExpression(String expression) {
        return new EvalExpression(expression).getValue();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}

class EvalExpression {
    private String expression;
    private int position;

    public EvalExpression(String expression) {
        this.expression = expression;
        this.position = 0;
    }

    public double getValue() {
        double value = parseTerm();
        while (position < expression.length()) {
            char operator = expression.charAt(position);
            position++;
            double term = parseTerm();
            if (operator == '+') {
                value += term;
            } else if (operator == '-') {
                value -= term;
            }
        }
        return value;
    }

    private double parseTerm() {
        double value = parseFactor();
        while (position < expression.length()) {
            char operator = expression.charAt(position);
            if (operator != '*' && operator != '/') {
                break;
            }
            position++;
            double factor = parseFactor();
            if (operator == '*') {
                value *= factor;
            } else if (operator == '/') {
                value /= factor;
            }
        }
        return value;
    }

    private double parseFactor() {
        char ch = expression.charAt(position);
        if (ch >= '0' && ch <= '9') {
            return parseNumber();
        } else if (ch == '(') {
            position++;
            double value = getValue();
            if (expression.charAt(position) == ')') {
                position++;
            }
            return value;
        } else if (ch == '-') {
            position++;
            return -parseFactor();
        } else {
            throw new RuntimeException("Unexpected character: " + ch);
        }
    }

    private double parseNumber() {
        int start = position;
        while (position < expression.length() && (expression.charAt(position) >= '0' && expression.charAt(position) <= '9' || expression.charAt(position) == '.')) {
            position++;
        }
        return Double.parseDouble(expression.substring(start, position));
    }
}

public class SimpleCalculator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}
