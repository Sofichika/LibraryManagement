import GUI.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementUI ui = new LibraryManagementUI();
            ui.setVisible(true);
        });
    }
}