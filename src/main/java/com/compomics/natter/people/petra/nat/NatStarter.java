package com.compomics.natter.people.petra.nat;

import com.compomics.util.sun.SwingWorker;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.log4j.Logger;

public class NatStarter extends JFrame {
   private static Logger logger = Logger.getLogger(NatStarter.class);
   private JButton setRovFolderButton;
   private JTextField textField1;
   private JTextArea textArea1;
   private JButton startButton;
   private JTextField textField2;
   private JPanel jpanContent;
   private JButton exitButton;
   private Vector<String> iFolders = new Vector();

   public NatStarter() {
      this.$$$setupUI$$$();

      try {
         File lTempfolder = File.createTempFile("temp", "temp").getParentFile();
         File lTempRovFolder = new File(lTempfolder, "natter");
         if (lTempRovFolder.exists()) {
            deleteDir(lTempRovFolder);
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
            System.exit(0);
         }
      });
      this.setRovFolderButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(1);
            if (!NatStarter.this.iFolders.isEmpty()) {
               chooser.setCurrentDirectory(new File(((String)NatStarter.this.iFolders.get(NatStarter.this.iFolders.size() - 1)).substring(0, ((String)NatStarter.this.iFolders.get(NatStarter.this.iFolders.size() - 1)).lastIndexOf("\\"))));
            }

            int returnVal = chooser.showOpenDialog(new JFrame());
            if (returnVal == 0) {
               NatStarter.this.textField1.setText(chooser.getSelectedFile().getAbsolutePath());
               NatStarter.this.iFolders.add(chooser.getSelectedFile().getAbsolutePath());
               NatStarter.this.textArea1.append("Added folder nr. " + NatStarter.this.iFolders.size() + " (" + chooser.getSelectedFile().getAbsolutePath() + ")\n");
               NatStarter.this.startButton.setEnabled(true);
            }

         }
      });
      this.startButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            SwingWorker lStarter = new SwingWorker() {
               public Boolean construct() {
                  String lSaveLocation = (String)NatStarter.this.iFolders.get(0);
                  String lTitle = NatStarter.this.textField2.getText();
                  if (lTitle.equalsIgnoreCase("Title")) {
                     NatStarter.this.textArea1.append("Select a valid title!\n");
                     return true;
                  } else {
                     NatStarter.this.textArea1.append("Your result will be saved to " + lSaveLocation + "\\multiRovOutput" + lTitle + ".csv");
                     String[] lFolders = new String[NatStarter.this.iFolders.size()];
                     NatStarter.this.iFolders.toArray(lFolders);
                     HashMap<String, String> lFormulas = new HashMap();
                     lFormulas.put("0,0", "0,0,0,0,0,0,0,0,0");
                     lFormulas.put("0,1", "-3,-2,0,0,0,3,2,0,0");
                     lFormulas.put("0,2", "0,-6,-4,0,0,0,6,4,0");
                     lFormulas.put("0,3", "-3,-8,-4,0,0,3,8,4,0");
                     lFormulas.put("1,0", "3,2,0,0,0,-3,-2,0,0");
                     lFormulas.put("1,1", "0,0,0,0,0,0,0,0,0");
                     lFormulas.put("1,2", "3,-4,-4,0,0,-3,4,4,0");
                     lFormulas.put("1,3", "0,-6,-4,0,0,0,6,4,0");
                     lFormulas.put("2,0", "0,6,4,0,0,0,-6,-4,0");
                     lFormulas.put("2,1", "-3,4,4,0,0,3,-4,-4,0");
                     lFormulas.put("2,2", "0,0,0,0,0,0,0,0,0");
                     lFormulas.put("2,3", "-3,-2,0,0,0,3,2,0,0");
                     lFormulas.put("3,0", "3,8,4,0,0,-3,-8,-4,0");
                     lFormulas.put("3,1", "0,6,4,0,0,0,-6,-4,0");
                     lFormulas.put("3,2", "3,2,0,0,0,-3,-2,0,0");
                     lFormulas.put("3,3", "0,0,0,0,0,0,0,0,0");
                     Object[] lValues = lFormulas.values().toArray();
                     NatStarter.this.textArea1.append("\nLoading recalculation formulae\n");

                     for(int i = 0; i < lValues.length; ++i) {
                        NatStarter.this.textArea1.append(lValues[i].toString() + "\n");
                     }

                     NatStarter.this.textArea1.append("\n");
                     new CompareStarter(lFolders, lSaveLocation, lFormulas, lTitle, NatStarter.this.textArea1);
                     return true;
                  }
               }

               public void finished() {
               }
            };
            lStarter.start();
         }
      });
      this.exitButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            System.exit(0);
         }
      });
      this.setTitle("Nat data extractor");
      this.setContentPane(this.jpanContent);
      this.setSize(850, 450);
      this.setLocation(150, 150);
      this.setVisible(true);
   }

   public static boolean deleteDir(File dir) {
      if (dir.isDirectory()) {
         String[] children = dir.list();

         for(int i = 0; i < children.length; ++i) {
            boolean success = deleteDir(new File(dir, children[i]));
            if (!success) {
               return false;
            }
         }
      }

      return dir.delete();
   }

   public static void main(String[] args) {
      new NatStarter();
   }

   private void $$$setupUI$$$() {
      this.jpanContent = new JPanel();
      this.jpanContent.setLayout(new GridBagLayout());
      this.setRovFolderButton = new JButton();
      this.setRovFolderButton.setText("Set rov folder");
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.fill = 2;
      gbc.insets = new Insets(5, 5, 5, 5);
      this.jpanContent.add(this.setRovFolderButton, gbc);
      this.textField1 = new JTextField();
      this.textField1.setEditable(false);
      this.textField1.setEnabled(true);
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1.0D;
      gbc.anchor = 17;
      gbc.fill = 2;
      gbc.insets = new Insets(5, 5, 5, 5);
      this.jpanContent.add(this.textField1, gbc);
      this.textField2 = new JTextField();
      this.textField2.setEditable(true);
      this.textField2.setEnabled(true);
      this.textField2.setText("Title");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.weightx = 1.0D;
      gbc.anchor = 17;
      gbc.fill = 2;
      gbc.insets = new Insets(5, 5, 5, 5);
      this.jpanContent.add(this.textField2, gbc);
      this.startButton = new JButton();
      this.startButton.setEnabled(false);
      this.startButton.setText("Start");
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 2;
      gbc.fill = 2;
      gbc.insets = new Insets(5, 5, 5, 5);
      this.jpanContent.add(this.startButton, gbc);
      JScrollPane scrollPane1 = new JScrollPane();
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 3;
      gbc.gridwidth = 2;
      gbc.weighty = 1.0D;
      gbc.fill = 1;
      gbc.insets = new Insets(5, 5, 5, 5);
      this.jpanContent.add(scrollPane1, gbc);
      this.textArea1 = new JTextArea();
      scrollPane1.setViewportView(this.textArea1);
      JLabel label1 = new JLabel();
      label1.setText("Title");
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.insets = new Insets(5, 5, 5, 5);
      this.jpanContent.add(label1, gbc);
      this.exitButton = new JButton();
      this.exitButton.setText("Exit");
      gbc = new GridBagConstraints();
      gbc.gridx = 1;
      gbc.gridy = 4;
      gbc.fill = 2;
      this.jpanContent.add(this.exitButton, gbc);
   }

   public JComponent $$$getRootComponent$$$() {
      return this.jpanContent;
   }
}
