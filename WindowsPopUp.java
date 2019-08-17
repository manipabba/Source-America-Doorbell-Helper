import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.net.MalformedURLException;

public class WindowsPopUp {
   public static void main(String[] args) {
      if (SystemTray.isSupported()) {
         try {
            displayTray("hello");
         } catch (AWTException | MalformedURLException var2) {
            var2.printStackTrace();
         }
      } else {
         System.err.println("System tray not supported!");
      }

   }

   public static void displayTray(String message) throws AWTException, MalformedURLException {
      SystemTray tray = SystemTray.getSystemTray();
      Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
      TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
      trayIcon.setImageAutoSize(true);
      trayIcon.setToolTip("System tray icon demo");
      tray.add(trayIcon);
      trayIcon.displayMessage("New Message", message, MessageType.INFO);
   }
}
    