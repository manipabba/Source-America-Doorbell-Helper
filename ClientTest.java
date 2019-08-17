import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClientTest {
   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var2) {
         var2.printStackTrace();
      }

      GUIMain hlaa = new GUIMain(JOptionPane.showInputDialog("What is Your Name?"));
      hlaa.setSize(500, 450);
      hlaa.setVisible(true);
      hlaa.setDefaultCloseOperation(3);
      hlaa.startRunning();
   }
}