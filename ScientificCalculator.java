import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;

public class ScientificCalculator {
    private JFrame frame;
    private JTextField textField;
    private JPanel buttonPanel;
    private String currentInput = "";

    public ScientificCalculator() {
        // Create the frame
        frame = new JFrame("Scientific Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        // Create the display text field
        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.BOLD, 24));
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setEditable(false);
        frame.add(textField, BorderLayout.NORTH);

        // Create the button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 4, 5, 5));
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Add buttons
        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", ".", "=", "+",
                "sin", "cos", "tan", "sqrt",
                "log", "ln", "^", "(", ")", "C"
        };

        for (String button : buttons) {
            JButton btn = new JButton(button);
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            btn.addActionListener(new ButtonClickListener());
            buttonPanel.add(btn);
        }

        // Show the frame
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();

            switch (command) {
                case "C":
                    currentInput = "";
                    textField.setText("");
                    break;
                case "=":
                    try {
                        double result = evaluate(currentInput);
                        textField.setText(String.valueOf(result));
                        currentInput = String.valueOf(result);
                    } catch (Exception ex) {
                        textField.setText("Error");
                        currentInput = "";
                    }
                    break;
                default:
                    currentInput += command;
                    textField.setText(currentInput);
                    break;
            }
        }
    }

    private double evaluate(String expression) {
        // Evaluation logic (simplified for demonstration purposes)
        // You can replace this with a more advanced parsing algorithm
        Stack<Double> stack = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--;
                stack.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    stack.push(applyOp(ops.pop(), stack.pop(), stack.pop()));
                }
                ops.pop();
            } else if ("+-*/^".indexOf(c) != -1) {
                while (!ops.isEmpty() && precedence(c) <= precedence(ops.peek())) {
                    stack.push(applyOp(ops.pop(), stack.pop(), stack.pop()));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            stack.push(applyOp(ops.pop(), stack.pop(), stack.pop()));
        }

        return stack.pop();
    }

    private int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }

    private double applyOp(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            case '^' -> Math.pow(a, b);
            default -> 0;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ScientificCalculator::new);
    }
}
