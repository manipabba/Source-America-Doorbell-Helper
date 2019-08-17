import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class JFontChooser extends JDialog {
   public static final int RET_CANCEL = 0;
   public static final int RET_OK = 1;
   private Font font;
   private JPanel fontPanel;
   private JScrollPane jScrollPane1;
   private JLabel jLabel1;
   private JLabel jLabel3;
   private JLabel jLabel2;
   private JList lstSize;
   private JButton okButton;
   private JList lstFont;
   private JScrollPane jScrollPane2;
   private JList lstStyle;
   private JPanel mainPanel;
   private JButton cancelButton;
   private JPanel previewPanel;
   private JLabel lblPreview;
   private JPanel buttonPanel;
   private JScrollPane jScrollPane3;
   private int returnStatus = 0;

   public JFontChooser(Frame parent, Font font) {
      super(parent);
      this.font = font;
      this.initComponents();
      this.lblPreview.setFont(font);
   }

   public JFontChooser(Frame parent) {
      super(parent);
      this.font = new Font("Dialog", 0, 12);
      this.initComponents();
      this.lblPreview.setFont(this.font);
   }

   public JFontChooser(Font font) {
      super((Frame)null);
      this.font = font;
      this.initComponents();
      this.lblPreview.setFont(font);
   }

   public JFontChooser() {
      super((Frame)null);
      this.font = new Font("Dialog", 0, 12);
      this.initComponents();
      this.lblPreview.setFont(this.font);
   }

   public Font getFont() {
      return this.font;
   }

   public int getReturnStatus() {
      return this.returnStatus;
   }

   private void initComponents() {
      this.mainPanel = new JPanel();
      this.fontPanel = new JPanel();
      this.jLabel1 = new JLabel();
      this.jLabel2 = new JLabel();
      this.jLabel3 = new JLabel();
      this.jScrollPane1 = new JScrollPane();
      this.lstFont = new JList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
      this.jScrollPane2 = new JScrollPane();
      this.lstStyle = new JList();
      this.jScrollPane3 = new JScrollPane();
      this.lstSize = new JList();
      this.previewPanel = new JPanel();
      this.lblPreview = new JLabel();
      this.buttonPanel = new JPanel();
      this.okButton = new JButton();
      this.cancelButton = new JButton();
      this.setTitle("Select Font");
      this.setModal(true);
      this.setResizable(false);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            JFontChooser.this.closeDialog(evt);
         }
      });
      this.mainPanel.setLayout(new GridLayout(2, 1));
      this.fontPanel.setLayout(new GridBagLayout());
      this.jLabel1.setText("Font");
      GridBagConstraints gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(1, 1, 1, 1);
      gridBagConstraints.weightx = 2.0D;
      this.fontPanel.add(this.jLabel1, gridBagConstraints);
      this.jLabel2.setText("Style");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(1, 1, 1, 1);
      this.fontPanel.add(this.jLabel2, gridBagConstraints);
      this.jLabel3.setText("Size");
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.fill = 2;
      gridBagConstraints.insets = new Insets(1, 1, 1, 1);
      gridBagConstraints.weightx = 0.2D;
      this.fontPanel.add(this.jLabel3, gridBagConstraints);
      this.lstFont.setSelectionMode(0);
      this.lstFont.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent evt) {
            JFontChooser.this.lstFontValueChanged(evt);
         }
      });
      this.jScrollPane1.setViewportView(this.lstFont);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 0;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 1;
      gridBagConstraints.insets = new Insets(1, 1, 1, 1);
      gridBagConstraints.weightx = 2.0D;
      this.fontPanel.add(this.jScrollPane1, gridBagConstraints);
      this.lstStyle.setModel(new AbstractListModel() {
         String[] strings = new String[]{"Plain", "Bold", "Italic", "Bold Italic"};

         public int getSize() {
            return this.strings.length;
         }

         public Object getElementAt(int i) {
            return this.strings[i];
         }
      });
      this.lstStyle.setSelectionMode(0);
      this.lstStyle.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent evt) {
            JFontChooser.this.lstStyleValueChanged(evt);
         }
      });
      this.jScrollPane2.setViewportView(this.lstStyle);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 1;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 1;
      gridBagConstraints.insets = new Insets(1, 1, 1, 1);
      this.fontPanel.add(this.jScrollPane2, gridBagConstraints);
      this.lstSize.setModel(new AbstractListModel() {
         String[] strings = new String[]{"8", "10", "11", "12", "14", "16", "20", "24", "28", "36", "48", "72", "96"};

         public int getSize() {
            return this.strings.length;
         }

         public Object getElementAt(int i) {
            return this.strings[i];
         }
      });
      this.lstSize.setSelectionMode(0);
      this.lstSize.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent evt) {
            JFontChooser.this.lstSizeValueChanged(evt);
         }
      });
      this.jScrollPane3.setViewportView(this.lstSize);
      gridBagConstraints = new GridBagConstraints();
      gridBagConstraints.gridx = 2;
      gridBagConstraints.gridy = 1;
      gridBagConstraints.fill = 2;
      gridBagConstraints.ipadx = 1;
      gridBagConstraints.insets = new Insets(1, 1, 1, 1);
      gridBagConstraints.weightx = 0.2D;
      this.fontPanel.add(this.jScrollPane3, gridBagConstraints);
      this.mainPanel.add(this.fontPanel);
      this.previewPanel.setLayout(new BorderLayout());
      this.previewPanel.setBorder(new TitledBorder((Border)null, "Preview", 0, 0, new Font("Dialog", 0, 12)));
      this.lblPreview.setFont(new Font("Dialog", 0, 12));
      this.lblPreview.setText("ABCDEFG abcdefg");
      this.previewPanel.add(this.lblPreview, "Center");
      this.mainPanel.add(this.previewPanel);
      this.getContentPane().add(this.mainPanel, "Center");
      this.buttonPanel.setLayout(new FlowLayout(2));
      this.okButton.setText("OK");
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            JFontChooser.this.okButtonActionPerformed(evt);
         }
      });
      this.buttonPanel.add(this.okButton);
      this.cancelButton.setText("Cancel");
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            JFontChooser.this.cancelButtonActionPerformed(evt);
         }
      });
      this.buttonPanel.add(this.cancelButton);
      this.getContentPane().add(this.buttonPanel, "South");
      this.pack();
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(new Dimension(443, 429));
      this.setLocation((screenSize.width - 443) / 2, (screenSize.height - 429) / 2);
   }

   private void lstStyleValueChanged(ListSelectionEvent evt) {
      int style = -1;
      String selStyle = (String)this.lstStyle.getSelectedValue();
      if (selStyle == "Plain") {
         style = 0;
      }

      if (selStyle == "Bold") {
         style = 1;
      }

      if (selStyle == "Italic") {
         style = 2;
      }

      if (selStyle == "Bold Italic") {
         style = 3;
      }

      this.font = new Font(this.font.getFamily(), style, this.font.getSize());
      this.lblPreview.setFont(this.font);
   }

   private void lstSizeValueChanged(ListSelectionEvent evt) {
      int size = Integer.parseInt((String)this.lstSize.getSelectedValue());
      this.font = new Font(this.font.getFamily(), this.font.getStyle(), size);
      this.lblPreview.setFont(this.font);
   }

   private void lstFontValueChanged(ListSelectionEvent evt) {
      this.font = new Font((String)this.lstFont.getSelectedValue(), this.font.getStyle(), this.font.getSize());
      this.lblPreview.setFont(this.font);
   }

   private void okButtonActionPerformed(ActionEvent evt) {
      this.doClose(1);
   }

   private void cancelButtonActionPerformed(ActionEvent evt) {
      this.doClose(0);
   }

   private void closeDialog(WindowEvent evt) {
      this.doClose(0);
   }

   private void doClose(int retStatus) {
      this.returnStatus = retStatus;
      this.setVisible(false);
   }

   public static void main(String[] args) {
      (new JFontChooser()).show();
   }
}
    