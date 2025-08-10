public class Launcher {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            BetaFunctionGUI gui = new BetaFunctionGUI();
            gui.setVisible(true);
        });
    }
}
