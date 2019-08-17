import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.SystemTray;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class GUIMain extends JFrame {
   private JTextPane textArea;
   private JPanel sendArea;
   private JTextArea sendText;
   private JButton send;
   private GUIMain.Handler handler;
   private final String filePathData = "C:\\Users\\prave\\Desktop\\doorbell_text.txt";
   private ObjectOutputStream output;
   private static final String TEXT_SUBMIT = "text-submit";
   private static final String INSERT_BREAK = "insert-break";
   private ObjectInputStream input;
   private String message = "";
   private JFileChooser fileChooser;
   private String serverIP;
   private Socket connection;
   boolean connectionStatus = false;
   private String username;

   public GUIMain(String title) {
      super(title + "Doorbell Messenger");
      this.username = title.trim();
      this.serverIP = "127.0.0.1";
      this.fileChooser = new JFileChooser();
      this.setLayout(new BorderLayout());
      this.handler = new GUIMain.Handler((GUIMain.Handler)null);
      JMenuBar menu = new JMenuBar();
      JMenu settings = new JMenu("Settings");
      JMenuItem log = new JMenuItem("Log Session...");
      log.addActionListener(this.handler);
      settings.add(log);
      JMenuItem exit = new JMenuItem("Exit");
      exit.addActionListener(this.handler);
      settings.add(exit);
      menu.add(settings);
      this.setJMenuBar(menu);
      JMenu color = new JMenu("Change Colors");
      menu.add(color);
      JMenuItem changeBack = new JMenuItem("Change Background Color...");
      JMenuItem changeFore = new JMenuItem("Change Text Color...");
      changeBack.addActionListener(this.handler);
      changeFore.addActionListener(this.handler);
      color.add(changeBack);
      color.add(changeFore);
      JMenu others = new JMenu("Other");
      JMenuItem font = new JMenuItem("Change Font...");
      font.addActionListener(this.handler);
      others.add(font);
      menu.add(others);
      this.textArea = new JTextPane();
      this.textArea.setBackground(new Color(230, 230, 230));
      this.textArea.setEditable(false);
      JScrollPane scroll = new JScrollPane(this.textArea);
      this.add(scroll, "Center");
      this.sendArea = new JPanel();
      this.sendArea.setLayout(new BorderLayout());
      this.sendText = new JTextArea();
      this.sendText.setLineWrap(true);
      this.sendText.setWrapStyleWord(true);
      this.send = new JButton("Send");
      this.send.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            GUIMain.this.sendMessage(GUIMain.this.sendText.getText());
            GUIMain.this.sendText.setText("");
         }
      });
      InputMap input = this.sendText.getInputMap();
      KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
      KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
      input.put(shiftEnter, "insert-break");
      input.put(enter, "text-submit");
      ActionMap actions = this.sendText.getActionMap();
      actions.put("text-submit", new AbstractAction() {
         public void actionPerformed(ActionEvent e) {
            GUIMain.this.sendMessage(GUIMain.this.sendText.getText());
            GUIMain.this.sendText.setText("");
         }
      });
      this.sendArea.add(this.sendText, "Center");
      this.sendArea.add(this.send, "East");
      this.add(this.sendArea, "South");
   }

   public void startRunning() {
      try {
         this.connectToServer();
         this.setupStreams();
         this.whileChatting();
      } catch (EOFException var6) {
         this.showMessage("\n Client terminated the connection");
      } catch (IOException var7) {
         var7.printStackTrace();
      } finally {
         this.closeConnection();
      }

   }

   private void connectToServer() throws IOException {
      this.showMessage("Attempting connection...");
      this.connection = new Socket(InetAddress.getByName(this.serverIP), 6889);
      this.showMessage("Connection Established! Connected to: " + this.connection.getInetAddress().getHostName());
   }

   private void setupStreams() throws IOException {
      this.output = new ObjectOutputStream(this.connection.getOutputStream());
      this.output.flush();
      this.input = new ObjectInputStream(this.connection.getInputStream());
      this.showMessage("Connected to Doorbell!");
   }

   private void whileChatting() throws IOException {
      this.ableToType(true);

      do {
         try {
            this.message = (String)this.input.readObject();
            this.showMessage(this.message);
         } catch (ClassNotFoundException var2) {
            this.showMessage("Unknown data received!");
         }
      } while(!this.message.equals("SERVER - END"));

   }

   private void closeConnection() {
      this.showMessage("\nClosing the connection!");
      this.ableToType(false);

      try {
         this.output.close();
         this.input.close();
         this.connection.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   private void sendMessage(String message) {
      try {
         this.output.writeObject("HLAA - " + message);
         this.output.flush();
         this.showMessage(this.username + " - " + message);
      } catch (IOException var3) {
         JOptionPane.showMessageDialog((Component)null, "Something Went Wrong!!!");
      }

   }

   private void showMessage(final String message) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            if (SystemTray.isSupported()) {
               try {
                  WindowsPopUp.displayTray(message);
               } catch (AWTException | MalformedURLException var3) {
                  var3.printStackTrace();
               }
            } else {
               System.err.println("System tray not supported!");
            }

            GUIMain.this.toFront();
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            GUIMain.this.textArea.setText(GUIMain.this.textArea.getText() + message + "         " + dateFormat.format(date) + "\n");
         }
      });
   }

   public void toggleConnectionStatus(boolean s) {
      this.connectionStatus = s;
   }

   private void ableToType(final boolean tof) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            GUIMain.this.sendText.setEditable(tof);
         }
      });
   }

   private class Handler implements ActionListener {
      private Handler() {
      }

      public void actionPerformed(ActionEvent event) {
         Color initialForeground;
         Color foreground;
         if (event.getActionCommand().equals("Change Background Color...")) {
            initialForeground = GUIMain.this.textArea.getBackground();
            foreground = JColorChooser.showDialog((Component)null, "Choose Background Color...", initialForeground);
            if (foreground != null) {
               GUIMain.this.textArea.setBackground(foreground);
            }
         } else if (event.getActionCommand().equals("Change Text Color...")) {
            initialForeground = GUIMain.this.textArea.getForeground();
            foreground = JColorChooser.showDialog((Component)null, "Choose Text Color...", initialForeground);
            if (foreground != null) {
               GUIMain.this.textArea.setForeground(foreground);
            }
         } else if (event.getActionCommand().equals("Exit")) {
            GUIMain.this.closeConnection();
            GUIMain.this.dispose();
         } else {
            String text;
            if (event.getActionCommand().equals("Refresh Manually")) {
               System.out.println("here");
               GUIMain.this.textArea.setText("");
               File fileName = new File("C:\\Users\\prave\\Desktop\\doorbell_text.txt");

               try {
                  Scanner sc = new Scanner(fileName);

                  for(text = ""; sc.hasNext(); text = text + sc.nextLine() + "\n") {
                     ;
                  }

                  sc.close();
                  GUIMain.this.textArea.setText(GUIMain.this.textArea.getText() + text);
               } catch (FileNotFoundException var9) {
                  var9.printStackTrace();
               }
            } else if (event.getActionCommand().equals("Check Connection")) {
               if (GUIMain.this.connectionStatus) {
                  JOptionPane.showMessageDialog((Component)null, "Connected, Good to Go!!!");
               } else {
                  JOptionPane.showMessageDialog((Component)null, "Not Connected. Restart Program");
               }
            } else if (event.getActionCommand().equals("Change Font...")) {
               JFontChooser fd = new JFontChooser(GUIMain.this.textArea.getFont());
               fd.show();
               if (fd.getReturnStatus() == 1) {
                  GUIMain.this.textArea.setFont(fd.getFont());
               }

               fd.dispose();
            } else if (event.getActionCommand().equals("Log Session...")) {
               JOptionPane.showMessageDialog((Component)null, "Please select a folder to store your logged session.");
               GUIMain.this.fileChooser.setCurrentDirectory(new File("C:\\"));
               GUIMain.this.fileChooser.setSelectedFile(new File("Doorbell Logs.txt"));
               int result = GUIMain.this.fileChooser.showSaveDialog((Component)null);
               if (result == 0) {
                  File file = new File(GUIMain.this.fileChooser.getSelectedFile().getPath());
                  if (!file.exists()) {
                     try {
                        file.createNewFile();
                     } catch (IOException var8) {
                        var8.printStackTrace();
                     }
                  }

                  text = GUIMain.this.textArea.getText();
                  text = text.replaceAll("(?!\\r)\\n", "\r\n");

                  try {
                     FileWriter writer = new FileWriter(file, true);
                     BufferedWriter bw = new BufferedWriter(writer);
                     bw.write("------NEW SESSION------");
                     bw.newLine();
                     bw.write(text);
                     bw.newLine();
                     bw.newLine();
                     bw.newLine();
                     bw.close();
                     writer.close();
                     JOptionPane.showMessageDialog((Component)null, "Done, saved in " + file.toString());
                  } catch (IOException var7) {
                     var7.printStackTrace();
                     JOptionPane.showMessageDialog((Component)null, "Error, did not log conversation");
                  }
               } else if (result == 1) {
                  JOptionPane.showMessageDialog((Component)null, "You selected nothing.");
               } else if (result == -1) {
                  JOptionPane.showMessageDialog((Component)null, "An error occurred.");
               }
            }
         }

      }

      // $FF: synthetic method
      Handler(GUIMain.Handler var2) {
         this();
      }
   }
}
    