// BetaFunctionGUI.java
// SOEN 6011 – D3: Beta Function GUI (Swing), Google Style compliant (reduced warnings).

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Simple Swing GUI to compute the Beta function B(x, y) for x > 0, y > 0.
 * <p>Accessibility: AccessibleName/Description are set for core widgets.
 * Error handling: input validation with helpful dialogs.
 * Styling: tuned for Google Java Style / Checkstyle.
 */
@SuppressWarnings("serial")
public class BetaFunctionGUI extends JFrame {

    /** Semantic Version (D3 requirement). */
    private static final String VERSION = "1.0.0";

    /** Window title base. */
    private static final String TITLE = "Beta Function (B(x, y)) — v";

    /** Text field column width. */
    private static final int FIELD_COLUMNS = 12;

    /** Layout gap for insets. */
    private static final int GAP = 8;

    /** Decimal format for results. */
    private static final DecimalFormat FORMAT = new DecimalFormat("0.############");

    private final JTextField xField = new JTextField(FIELD_COLUMNS);
    private final JTextField yField = new JTextField(FIELD_COLUMNS);
    private final JLabel resultLabel = new JLabel("Result: ");

    /** Constructs the GUI window. */
    public BetaFunctionGUI() {
        super(TITLE + VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Accessibility
        xField.getAccessibleContext().setAccessibleName("X Input Field");
        xField.getAccessibleContext().setAccessibleDescription("Enter a positive value for x.");
        yField.getAccessibleContext().setAccessibleName("Y Input Field");
        yField.getAccessibleContext().setAccessibleDescription("Enter a positive value for y.");
        resultLabel.getAccessibleContext().setAccessibleName("Result Label");
        resultLabel
                .getAccessibleContext()
                .setAccessibleDescription("Displays the computed value of the Beta function.");

        // UI elements
        JLabel xLabel = new JLabel("x:");
        xLabel.getAccessibleContext().setAccessibleName("Label for x");
        xLabel
                .getAccessibleContext()
                .setAccessibleDescription("Label describing the x input field.");

        JLabel yLabel = new JLabel("y:");
        yLabel.getAccessibleContext().setAccessibleName("Label for y");
        yLabel
                .getAccessibleContext()
                .setAccessibleDescription("Label describing the y input field.");

        JButton computeBtn = new JButton("Compute B(x, y)");
        computeBtn.getAccessibleContext().setAccessibleName("Compute Button");
        computeBtn
                .getAccessibleContext()
                .setAccessibleDescription("Compute Beta for the given inputs.");

        JButton clearBtn = new JButton("Clear");
        clearBtn.getAccessibleContext().setAccessibleName("Clear Button");
        clearBtn
                .getAccessibleContext()
                .setAccessibleDescription("Clear inputs and result.");

        xField.setToolTipText("Enter positive x (e.g., 0.5).");
        yField.setToolTipText("Enter positive y (e.g., 0.5).");

        // Layout
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(GAP, GAP, GAP, GAP);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(xLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        form.add(xField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(yLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        form.add(yField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(computeBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        form.add(clearBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        form.add(resultLabel, gbc);

        add(form, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        computeBtn.addActionListener(e -> onCompute());
        clearBtn.addActionListener(e -> onClear());
    }

    /** Handle Compute button: parse, validate, compute, and display. */
    private void onCompute() {
        try {
            double x = parseInput(xField.getText().trim(), "x");
            double y = parseInput(yField.getText().trim(), "y");

            if (x <= 0.0 || y <= 0.0) {
                throw new IllegalArgumentException(
                        "Domain error: require x > 0 and y > 0 for this implementation.");
            }

            double value = beta(x, y);
            resultLabel.setText("Result: " + FORMAT.format(value));
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Handle Clear button: reset fields and focus. */
    private void onClear() {
        xField.setText("");
        yField.setText("");
        resultLabel.setText("Result: ");
        xField.requestFocusInWindow();
    }

    /** Parse a double input with basic sanity checks. */
    private double parseInput(String s, String name) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Please enter a value for " + name + ".");
        }
        try {
            double x = Double.parseDouble(s);
            if (Double.isNaN(x) || Double.isInfinite(x)) {
                throw new NumberFormatException();
            }
            return x;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid number for " + name + ". Example: 0.5");
        }
    }

    /** Beta(x, y) = Γ(x) Γ(y) / Γ(x + y). */
    private double beta(double x, double y) {
        return gamma(x) * gamma(y) / gamma(x + y);
    }

    // ---------------- Gamma via Lanczos approximation ----------------

    // CHECKSTYLE:OFF MagicNumber
    // Lanczos coefficients (g = 7, n = 9) – acceptable accuracy for double precision.
    private static final double[] LANCZOS = {
            0.99999999999980993,
            676.5203681218851,
            -1259.1392167224028,
            771.32342877765313,
            -176.61502916214059,
            12.507343278686905,
            -0.13857109526572012,
            9.9843695780195716e-6,
            1.5056327351493116e-7
    };
    // CHECKSTYLE:ON

    /** Gamma function using Lanczos + reflection for z &lt; 0.5. */
    private double gamma(double z) {
        if (z < 0.5) {
            // Reflection: Γ(z)Γ(1−z) = π / sin(πz)
            return Math.PI / (Math.sin(Math.PI * z) * gamma(1.0 - z));
        }
        z -= 1.0;
        double sum = LANCZOS[0];
        for (int k = 1; k < LANCZOS.length; k++) {
            sum += LANCZOS[k] / (z + k);
        }
        double t = z + 7.5; // g + 0.5 with g = 7
        return Math.sqrt(2 * Math.PI) * Math.pow(t, z + 0.5) * Math.exp(-t) * sum;
    }
}
