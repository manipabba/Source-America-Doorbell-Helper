import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class TouchscreenGUIMain extends JFrame {
   public JButton default1;
   public JButton default2;
   public JButton speech3;
   private ObjectOutputStream output;
   private ObjectInputStream input;
   private ServerSocket server;
   private Socket connection;

   public TouchscreenGUIMain(String name) {
      super(name);
      UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("ARIAL", 0, 24)));
      this.setLayout(new GridLayout(3, 1));
      this.default1 = new JButton("I'm here for a delivery");
      this.default1.setFont(new Font("Segoe UI", 0, 24));
      this.default2 = new JButton("I'm here for a meeting");
      this.default2.setFont(new Font("Segoe UI", 0, 24));
      this.speech3 = new JButton("Press to record a message");
      this.speech3.setFont(new Font("Segoe UI", 0, 24));
      this.default1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            TouchscreenGUIMain.this.sendMessage("I'm here for a delivery");
            JLabel label = new JLabel("Sent");
            label.setFont(new Font("Arial", 0, 24));
            JOptionPane.showMessageDialog((Component)null, label);
         }
      });
      this.default2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            TouchscreenGUIMain.this.sendMessage("I'm here for a meeting");
            JLabel label = new JLabel("Sent");
            label.setFont(new Font("Arial", 0, 24));
            JOptionPane.showMessageDialog((Component)null, label);
         }
      });
      this.speech3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            try {
               boolean textGood = false;
               String speechText = "";

               while(!textGood) {
                  int dialogButton = 0;
                  speechText = TouchscreenGUIMain.this.speechToText();
                  if (speechText.trim() == "" || speechText.length() == 0) {
                     speechText = "<no speech detected>";
                  }

                  JLabel labelx = new JLabel("Is this message correct?");
                  JLabel speechLabel = new JLabel(speechText);
                  labelx.setFont(new Font("Arial", 0, 24));
                  speechLabel.setFont(new Font("Arial", 0, 24));
                  JLabel[] arr = new JLabel[]{labelx, speechLabel};
                  int n = JOptionPane.showConfirmDialog((Component)null, arr, (String)null, dialogButton);
                  if (n == 0) {
                     textGood = true;
                  } else if (n == 1) {
                     textGood = false;
                  }
               }

               TouchscreenGUIMain.this.sendMessage(speechText);
               JLabel label = new JLabel("Sent Message");
               label.setFont(new Font("Arial", 0, 24));
               JOptionPane.showMessageDialog((Component)null, label);
            } catch (FileNotFoundException var9) {
               var9.printStackTrace();
            }

         }
      });
      this.add(this.default1);
      this.add(this.default2);
      this.add(this.speech3);
   }

   public String speechToText() throws FileNotFoundException {
      JLabel label = new JLabel("Click OK, wait a second, and start talking");
      label.setFont(new Font("Arial", 0, 24));
      JOptionPane.showMessageDialog((Component)null, label);

      try {
         Process p = Runtime.getRuntime().exec("python speechTest.py ");
         BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
         in.readLine();
      } catch (Exception var4) {
         ;
      }

      Scanner sc = new Scanner(new File("speechWords.txt"));

      String text;
      for(text = ""; sc.hasNextLine(); text = text + sc.nextLine()) {
         ;
      }

      return text;
   }

   public void startRunning() {
      try {
         this.server = new ServerSocket(6889, 100);

         while(true) {
            while(true) {
               try {
                  this.waitForConnection();
                  this.setupStreams();
                  this.whileChatting();
               } catch (EOFException var7) {
                  JLabel label = new JLabel("\n Server ended the connection! ");
                  label.setFont(new Font("Arial", 0, 24));
                  JOptionPane.showMessageDialog((Component)null, label);
                  this.closeConnection();
                  this.closeConnection();
               } finally {
                  this.closeConnection();
               }
            }
         }
      } catch (IOException var9) {
         var9.printStackTrace();
      }
   }

   private void whileChatting() throws IOException {
      String message = "You are now connected! ";

      JLabel label;
      try {
         this.output.writeObject("\nDoorbell - " + message);
         this.output.flush();
      } catch (IOException var5) {
         label = new JLabel("\n ERROR: CANNOT SEND MESSAGE, PLEASE RETRY IN SOME TIME");
         label.setFont(new Font("Arial", 0, 24));
         JOptionPane.showMessageDialog((Component)null, label);
      }

      do {
         try {
            message = (String)this.input.readObject();
            JLabel label1 = new JLabel(message);
            label1.setFont(new Font("Arial", 0, 24));
            JOptionPane.showMessageDialog((Component)null, label1);
         } catch (ClassNotFoundException var4) {
            label = new JLabel("The user has sent an unknown object!");
            label.setFont(new Font("Arial", 0, 24));
            JOptionPane.showMessageDialog((Component)null, label);
         }
      } while(!message.equals("CLIENT - END"));

   }

   private void waitForConnection() throws IOException {
      JLabel label = new JLabel(" Waiting for someone to connect... \n");
      label.setFont(new Font("Arial", 0, 24));
      JOptionPane.showMessageDialog((Component)null, label);
      this.connection = this.server.accept();
      label = new JLabel(" Now connected to " + this.connection.getInetAddress().getHostName());
      label.setFont(new Font("Arial", 0, 24));
      JOptionPane.showMessageDialog((Component)null, label);
   }

   private void setupStreams() throws IOException {
      this.output = new ObjectOutputStream(this.connection.getOutputStream());
      this.output.flush();
      this.input = new ObjectInputStream(this.connection.getInputStream());
      JLabel label = new JLabel("\n Streams are now setup \n");
      label.setFont(new Font("Arial", 0, 24));
      JOptionPane.showMessageDialog((Component)null, label);
   }

   public void closeConnection() {
      JLabel label = new JLabel("\n Closing Connections... \n");
      label.setFont(new Font("Arial", 0, 24));
      JOptionPane.showMessageDialog((Component)null, label);

      try {
         this.output.close();
         this.input.close();
         this.connection.close();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   private void sendMessage(String message) {
      try {
         this.output.writeObject("Doorbell - " + message);
         this.output.flush();
      } catch (IOException var4) {
         JLabel label = new JLabel("\n ERROR: CANNOT SEND MESSAGE, PLEASE RETRY IN SOME TIME");
         label.setFont(new Font("Arial", 0, 24));
         JOptionPane.showMessageDialog((Component)null, label);
      }

   }
}