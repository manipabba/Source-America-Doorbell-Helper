import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ServerTest {
   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException var2) {
         ;
      }

      TouchscreenGUIMain door = new TouchscreenGUIMain("Welcome to HLAA");
      door.setDefaultCloseOperation(3);
      door.setVisible(true);
      door.setSize(300, 250);
      door.startRunning();
   }
}