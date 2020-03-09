package com.compomics.natter.msIsotope;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.log4j.Logger;

public class PetraYeastNat extends JFrame {
   private static Logger logger = Logger.getLogger(PetraYeastNat.class);
   private Connection iConn = null;
   private String iDBName;
   private ExcelColumn[] columns;
   private JTextField txtLocation = null;
   private JTextField txtLocationEle = null;
   private JComboBox cmbModSeq = null;
   private JComboBox cmbSeq = null;
   private JComboBox cmbCharge = null;
   private JComboBox cmbContAce = null;
   private JComboBox cmbContAcD3 = null;
   private JComboBox cmbHumAce = null;
   private JComboBox cmbHumAcD3 = null;
   private JComboBox cmbYea_NatAce = null;
   private JComboBox cmbYea_NatAcD3 = null;
   private JComboBox cmbName = null;
   private JComboBox cmbIso1 = null;
   private JComboBox cmbIso2 = null;
   private JComboBox cmbIso3 = null;
   private JComboBox cmbIso4 = null;
   private JComboBox cmbIso5 = null;
   private JComboBox cmbIso6 = null;
   private JButton btnSelectLocation = new JButton();
   private JButton btnSelectLocationEle = new JButton();
   private JButton btnStart = new JButton();
   private JButton btnCancel = new JButton();
   private JButton btnReadExcelFile = new JButton();
   private JButton btnProcessExcelFile = new JButton();
   private JPanel jpanModSeq;
   private JPanel jpanSeq;
   private JPanel jpanParametes;
   private JPanel jpanContAce;
   private JPanel jpanContAcD3;
   private JPanel jpanHumAce;
   private JPanel jpanHumAcD3;
   private JPanel jpanYeaAce;
   private JPanel jpanYeaAcD3;
   private JPanel jpanIso1;
   private JPanel jpanIso2;
   private JPanel jpanIso3;
   private JPanel jpanIso4;
   private JPanel jpanIso5;
   private JPanel jpanIso6;
   private JPanel jpanName;
   private Workbook excelFile;
   private WritableWorkbook excelSave;
   private WritableSheet sheetSave;
   private Sheet sheet;
   private YeastNatElement[] sequences;
   private double[] aceContInts;
   private double[] acD3ContInts;
   private double[] aceHumInts;
   private double[] acD3HumInts;
   private double[] aceYeaInts;
   private double[] acD3YeaInts;
   private double[] iso1;
   private double[] iso2;
   private double[] iso3;
   private double[] iso4;
   private double[] iso5;
   private double[] iso6;
   private PartialAceCouple[] couples;

   public PetraYeastNat(String aTitle) {
      super(aTitle);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            PetraYeastNat.this.close();
         }
      });
      this.initializeComponents();
      this.constructScreen();
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      this.setLocation(screen.width / 4, screen.height / 10);
      this.pack();
   }

   public void initializeComponents() {
      this.txtLocation = new JTextField(40);
      this.txtLocation.setEditable(true);
      this.txtLocation.setMaximumSize(this.txtLocation.getPreferredSize());
      this.txtLocationEle = new JTextField(40);
      this.txtLocationEle.setEditable(true);
      this.txtLocationEle.setMaximumSize(this.txtLocationEle.getPreferredSize());
      this.columns = new ExcelColumn[1];
      this.columns[0] = new ExcelColumn("Load an excel file", 0);
      this.cmbModSeq = new JComboBox(this.columns);
      this.cmbModSeq.setMaximumSize(new Dimension(this.cmbModSeq.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.cmbSeq = new JComboBox(this.columns);
      this.cmbSeq.setMaximumSize(new Dimension(this.cmbSeq.getPreferredSize().width, this.cmbSeq.getPreferredSize().height));
      this.cmbCharge = new JComboBox(this.columns);
      this.cmbCharge.setMaximumSize(new Dimension(this.cmbCharge.getPreferredSize().width, this.cmbCharge.getPreferredSize().height));
      this.cmbContAce = new JComboBox(this.columns);
      this.cmbContAce.setMaximumSize(new Dimension(this.cmbContAce.getPreferredSize().width, this.cmbContAce.getPreferredSize().height));
      this.cmbContAcD3 = new JComboBox(this.columns);
      this.cmbContAcD3.setMaximumSize(new Dimension(this.cmbContAcD3.getPreferredSize().width, this.cmbContAcD3.getPreferredSize().height));
      this.cmbHumAce = new JComboBox(this.columns);
      this.cmbHumAce.setMaximumSize(new Dimension(this.cmbHumAce.getPreferredSize().width, this.cmbHumAce.getPreferredSize().height));
      this.cmbHumAcD3 = new JComboBox(this.columns);
      this.cmbHumAcD3.setMaximumSize(new Dimension(this.cmbHumAcD3.getPreferredSize().width, this.cmbHumAcD3.getPreferredSize().height));
      this.cmbYea_NatAce = new JComboBox(this.columns);
      this.cmbYea_NatAce.setMaximumSize(new Dimension(this.cmbYea_NatAce.getPreferredSize().width, this.cmbYea_NatAce.getPreferredSize().height));
      this.cmbYea_NatAcD3 = new JComboBox(this.columns);
      this.cmbYea_NatAcD3.setMaximumSize(new Dimension(this.cmbYea_NatAcD3.getPreferredSize().width, this.cmbYea_NatAcD3.getPreferredSize().height));
      this.cmbName = new JComboBox(this.columns);
      this.cmbName.setMaximumSize(new Dimension(this.cmbName.getPreferredSize().width, this.cmbName.getPreferredSize().height));
      this.cmbIso1 = new JComboBox(this.columns);
      this.cmbIso1.setMaximumSize(new Dimension(this.cmbIso1.getPreferredSize().width, this.cmbIso1.getPreferredSize().height));
      this.cmbIso2 = new JComboBox(this.columns);
      this.cmbIso2.setMaximumSize(new Dimension(this.cmbIso2.getPreferredSize().width, this.cmbIso2.getPreferredSize().height));
      this.cmbIso3 = new JComboBox(this.columns);
      this.cmbIso3.setMaximumSize(new Dimension(this.cmbIso3.getPreferredSize().width, this.cmbIso3.getPreferredSize().height));
      this.cmbIso4 = new JComboBox(this.columns);
      this.cmbIso4.setMaximumSize(new Dimension(this.cmbIso4.getPreferredSize().width, this.cmbIso4.getPreferredSize().height));
      this.cmbIso5 = new JComboBox(this.columns);
      this.cmbIso5.setMaximumSize(new Dimension(this.cmbIso5.getPreferredSize().width, this.cmbIso5.getPreferredSize().height));
      this.cmbIso6 = new JComboBox(this.columns);
      this.cmbIso6.setMaximumSize(new Dimension(this.cmbIso6.getPreferredSize().width, this.cmbIso6.getPreferredSize().height));
   }

   public void constructScreen() {
      this.btnSelectLocation.setText("Select excel file location");
      this.btnSelectLocation.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            PetraYeastNat.this.selectLocationBtnActionPerformed(evt);
         }
      });
      this.btnSelectLocationEle.setText("Select elements file location");
      this.btnSelectLocationEle.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            PetraYeastNat.this.selectLocationBtnEleActionPerformed(evt);
         }
      });
      this.btnReadExcelFile.setText("Read excel file");
      this.btnReadExcelFile.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            PetraYeastNat.this.readExcelBtnActionPerformed();
         }
      });
      this.btnReadExcelFile.setEnabled(false);
      JPanel jpanLocation = new JPanel();
      jpanLocation.setLayout(new BoxLayout(jpanLocation, 0));
      jpanLocation.add(Box.createHorizontalStrut(10));
      jpanLocation.add(this.btnSelectLocation);
      jpanLocation.add(Box.createHorizontalStrut(10));
      jpanLocation.add(this.txtLocation);
      jpanLocation.add(Box.createHorizontalStrut(10));
      jpanLocation.add(this.btnReadExcelFile);
      JPanel jpanLocationEle = new JPanel();
      jpanLocationEle.setLayout(new BoxLayout(jpanLocationEle, 0));
      jpanLocationEle.add(Box.createHorizontalStrut(10));
      jpanLocationEle.add(this.btnSelectLocationEle);
      jpanLocationEle.add(Box.createHorizontalStrut(10));
      jpanLocationEle.add(this.txtLocationEle);
      this.btnStart = new JButton("Start");
      this.btnStart.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            PetraYeastNat.this.startTriggered();
         }
      });
      this.btnStart.setEnabled(false);
      this.btnCancel = new JButton("Cancel");
      this.btnCancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            PetraYeastNat.this.dispose();
         }
      });
      JPanel jpanButton = new JPanel();
      jpanButton.setLayout(new BoxLayout(jpanButton, 0));
      jpanButton.add(Box.createHorizontalGlue());
      jpanButton.add(this.btnStart);
      jpanButton.add(Box.createHorizontalStrut(10));
      jpanButton.add(this.btnCancel);
      jpanButton.add(Box.createHorizontalStrut(10));
      jpanButton.setMaximumSize(new Dimension(jpanButton.getMaximumSize().width, this.btnStart.getPreferredSize().height));
      this.jpanParametes = new JPanel();
      this.jpanParametes.setLayout(new BoxLayout(this.jpanParametes, 1));
      this.jpanParametes.setBorder(BorderFactory.createTitledBorder("Parameters"));
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(jpanLocationEle);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(jpanLocation);
      JPanel jpanAll = new JPanel();
      jpanAll.setLayout(new BoxLayout(jpanAll, 1));
      jpanAll.add(this.jpanParametes);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      jpanAll.add(jpanButton);
      JPanel jpanMain = new JPanel();
      jpanMain.setLayout(new BoxLayout(jpanMain, 1));
      jpanMain.add(jpanAll);
      this.getContentPane().add(jpanMain, "Center");
   }

   public void selectLocationBtnActionPerformed(ActionEvent evt) {
      try {
         JFileChooser chooser = new JFileChooser();
         int returnVal = chooser.showOpenDialog(this);
         if (returnVal == 0) {
            if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".xls")) {
               this.txtLocation.setText("This is not an excel file");
            } else {
               this.txtLocation.setText(chooser.getSelectedFile().getAbsolutePath());
               this.excelFile = Workbook.getWorkbook(new File(chooser.getSelectedFile().getAbsolutePath()));
               this.btnReadExcelFile.setEnabled(true);
            }
         }
      } catch (BiffException var4) {
         var4.printStackTrace();
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public void selectLocationBtnEleActionPerformed(ActionEvent evt) {
      JFileChooser chooser = new JFileChooser();
      int returnVal = chooser.showOpenDialog(this);
      if (returnVal == 0) {
         if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".txt")) {
            this.txtLocation.setText("This is not an element file");
         } else {
            this.txtLocationEle.setText(chooser.getSelectedFile().getAbsolutePath());
         }
      }

   }

   private void startTriggered() {
      String eleLocation = this.txtLocationEle.getText();
      if (eleLocation.length() != 0 && eleLocation.endsWith(".txt")) {
         ReadElementFile fileReader = new ReadElementFile();
         Element[] elements = fileReader.getElements();

         for(int i = 0; i < this.sequences.length; ++i) {
            YeastNatElement yeast = this.sequences[i];
            yeast.calculateIons();
            new CompositionCalculaterYeast(elements, yeast);
            new MsIsotopeYeast(yeast);
            System.out.println(i + 1 + "/" + this.sequences.length);
         }

         this.createExcelFile(this.txtLocation.getText());
         this.writeInfoInExcel();

         try {
            this.excelSave.write();
            this.excelSave.close();
         } catch (IOException var8) {
            var8.printStackTrace();
         } catch (Exception var9) {
            var9.printStackTrace();
         }

         JOptionPane.showMessageDialog(this, "Done, jippie", "Create successufl!", 1);
         this.dispose();
      } else {
         JOptionPane.showMessageDialog(this, "Element textfile??????", "?", 0);
      }
   }

   private void createExcelFile(String location) {
      try {
         location = location.substring(0, location.indexOf(".xls")) + "update.xls";
         this.excelSave = Workbook.createWorkbook(new File(location));
         this.sheetSave = this.excelSave.createSheet("Sheet 1", 0);
         Label label1 = new Label(0, 0, "Sequence");
         Label label2 = new Label(1, 0, "Title");
         Label label3 = new Label(2, 0, "Totale Int controle Ace");
         Label label4 = new Label(3, 0, "Totale Int controle AcD3");
         Label label5 = new Label(4, 0, "Totale Int human NatA Ace");
         Label label6 = new Label(5, 0, "Totale Int human NatA AcD3");
         Label label7 = new Label(6, 0, "Totale Int yeast - NatA Ace");
         Label label8 = new Label(7, 0, "Totale Int yeast - NatA AcD3");
         Label label9 = new Label(8, 0, "% cont Ace");
         Label label10 = new Label(9, 0, "% cont AcD3");
         Label label11 = new Label(10, 0, "% hum Ace");
         Label label12 = new Label(11, 0, "% hum AcD3");
         Label label13 = new Label(12, 0, "% yeast-NatA Ace");
         Label label14 = new Label(13, 0, "% yeast-NatA AcD3");
         Label label15 = new Label(14, 0, "% cont Ace Iso");
         Label label16 = new Label(15, 0, "% cont AcD3 Iso");
         Label label17 = new Label(16, 0, "% hum Ace Iso");
         Label label18 = new Label(17, 0, "% hum AcD3 Iso");
         Label label19 = new Label(18, 0, "% yeast-NatA Ace Iso");
         Label label20 = new Label(19, 0, "% yeast-NatA AcD3 Iso");
         Label label21 = new Label(20, 0, "% charge");
         this.sheetSave.addCell(label1);
         this.sheetSave.addCell(label2);
         this.sheetSave.addCell(label3);
         this.sheetSave.addCell(label4);
         this.sheetSave.addCell(label5);
         this.sheetSave.addCell(label6);
         this.sheetSave.addCell(label7);
         this.sheetSave.addCell(label8);
         this.sheetSave.addCell(label9);
         this.sheetSave.addCell(label10);
         this.sheetSave.addCell(label11);
         this.sheetSave.addCell(label12);
         this.sheetSave.addCell(label13);
         this.sheetSave.addCell(label14);
         this.sheetSave.addCell(label15);
         this.sheetSave.addCell(label16);
         this.sheetSave.addCell(label17);
         this.sheetSave.addCell(label18);
         this.sheetSave.addCell(label19);
         this.sheetSave.addCell(label20);
         this.sheetSave.addCell(label21);
      } catch (IOException var23) {
         var23.printStackTrace();
      } catch (WriteException var24) {
         var24.printStackTrace();
      }

   }

   public void writeInfoInExcel() {
      try {
         for(int i = 0; i < this.sequences.length; ++i) {
            YeastNatElement yeastElement = this.sequences[i];
            Label label1 = new Label(0, i + 1, yeastElement.getSequence());
            Label label2 = new Label(1, i + 1, yeastElement.getTitle());
            Label label3 = new Label(2, i + 1, String.valueOf(yeastElement.getContAceIonEnvelop().getTotaleIntensity()));
            Label label4 = new Label(3, i + 1, String.valueOf(yeastElement.getContAcD3IonEnvelop().getTotaleIntensity()));
            Label label5 = new Label(4, i + 1, String.valueOf(yeastElement.getHumAceIonEnvelop().getTotaleIntensity()));
            Label label6 = new Label(5, i + 1, String.valueOf(yeastElement.getHumAcD3IonEnvelop().getTotaleIntensity()));
            Label label7 = new Label(6, i + 1, String.valueOf(yeastElement.getYeastAceIonEnvelop().getTotaleIntensity()));
            Label label8 = new Label(7, i + 1, String.valueOf(yeastElement.getYeastAcD3IonEnvelop().getTotaleIntensity()));
            Label label9 = new Label(8, i + 1, String.valueOf(yeastElement.getContAcePartialPerc()));
            Label label10 = new Label(9, i + 1, String.valueOf(yeastElement.getContAcD3PartialPerc()));
            Label label11 = new Label(10, i + 1, String.valueOf(yeastElement.getHumAcePartialPerc()));
            Label label12 = new Label(11, i + 1, String.valueOf(yeastElement.getHumAcD3PartialPerc()));
            Label label13 = new Label(12, i + 1, String.valueOf(yeastElement.getYeastAcePartialPerc()));
            Label label14 = new Label(13, i + 1, String.valueOf(yeastElement.getYeastAcD3PartialPerc()));
            Label label15 = new Label(14, i + 1, yeastElement.getElementalCompositionContAce());
            Label label16 = new Label(15, i + 1, yeastElement.getElementalCompositionContAcD3());
            Label label17 = new Label(16, i + 1, yeastElement.getElementalCompositionHumAce());
            Label label18 = new Label(17, i + 1, yeastElement.getElementalCompositionHumAcD3());
            Label label19 = new Label(18, i + 1, yeastElement.getElementalCompositionYeaAce());
            Label label20 = new Label(19, i + 1, yeastElement.getElementalCompositionYeaAcD3());
            Label label21 = new Label(20, i + 1, String.valueOf(yeastElement.getContAcePartialPercIso()));
            Label label22 = new Label(21, i + 1, String.valueOf(yeastElement.getContAcD3PartialPercIso()));
            Label label23 = new Label(22, i + 1, String.valueOf(yeastElement.getHumAcePartialPercIso()));
            Label label24 = new Label(23, i + 1, String.valueOf(yeastElement.getHumAcD3PartialPercIso()));
            Label label25 = new Label(24, i + 1, String.valueOf(yeastElement.getYeastAcePartialPercIso()));
            Label label26 = new Label(25, i + 1, String.valueOf(yeastElement.getYeastAcD3PartialPercIso()));
            Label label27 = new Label(26, i + 1, yeastElement.getChargeAsString());
            this.sheetSave.addCell(label1);
            this.sheetSave.addCell(label2);
            this.sheetSave.addCell(label3);
            this.sheetSave.addCell(label4);
            this.sheetSave.addCell(label5);
            this.sheetSave.addCell(label6);
            this.sheetSave.addCell(label7);
            this.sheetSave.addCell(label8);
            this.sheetSave.addCell(label9);
            this.sheetSave.addCell(label10);
            this.sheetSave.addCell(label11);
            this.sheetSave.addCell(label12);
            this.sheetSave.addCell(label13);
            this.sheetSave.addCell(label14);
            this.sheetSave.addCell(label15);
            this.sheetSave.addCell(label16);
            this.sheetSave.addCell(label17);
            this.sheetSave.addCell(label18);
            this.sheetSave.addCell(label19);
            this.sheetSave.addCell(label20);
            this.sheetSave.addCell(label21);
            this.sheetSave.addCell(label22);
            this.sheetSave.addCell(label23);
            this.sheetSave.addCell(label24);
            this.sheetSave.addCell(label25);
            this.sheetSave.addCell(label26);
            this.sheetSave.addCell(label27);
         }
      } catch (WriteException var30) {
         var30.printStackTrace();
      }

   }

   private void readExcelBtnActionPerformed() {
      this.sheet = this.excelFile.getSheet(0);
      Vector cells = new Vector();

      for(int i = 0; i < this.sheet.getColumns(); ++i) {
         Cell cell = this.sheet.getCell(i, 0);
         cells.add(new ExcelColumn(cell.getContents(), i));
      }

      this.columns = new ExcelColumn[cells.size()];
      cells.toArray(this.columns);
      this.cmbModSeq = new JComboBox(this.columns);
      this.cmbModSeq.setMaximumSize(new Dimension(this.cmbModSeq.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.cmbSeq = new JComboBox(this.columns);
      this.cmbSeq.setMaximumSize(new Dimension(this.cmbSeq.getPreferredSize().width, this.cmbSeq.getPreferredSize().height));
      this.cmbCharge = new JComboBox(this.columns);
      this.cmbCharge.setMaximumSize(new Dimension(this.cmbCharge.getPreferredSize().width, this.cmbCharge.getPreferredSize().height));
      this.cmbContAce = new JComboBox(this.columns);
      this.cmbContAce.setMaximumSize(new Dimension(this.cmbContAce.getPreferredSize().width, this.cmbContAce.getPreferredSize().height));
      this.cmbContAcD3 = new JComboBox(this.columns);
      this.cmbContAcD3.setMaximumSize(new Dimension(this.cmbContAcD3.getPreferredSize().width, this.cmbContAcD3.getPreferredSize().height));
      this.cmbHumAce = new JComboBox(this.columns);
      this.cmbHumAce.setMaximumSize(new Dimension(this.cmbHumAce.getPreferredSize().width, this.cmbHumAce.getPreferredSize().height));
      this.cmbHumAcD3 = new JComboBox(this.columns);
      this.cmbHumAcD3.setMaximumSize(new Dimension(this.cmbHumAcD3.getPreferredSize().width, this.cmbHumAcD3.getPreferredSize().height));
      this.cmbYea_NatAce = new JComboBox(this.columns);
      this.cmbYea_NatAce.setMaximumSize(new Dimension(this.cmbYea_NatAce.getPreferredSize().width, this.cmbYea_NatAce.getPreferredSize().height));
      this.cmbYea_NatAcD3 = new JComboBox(this.columns);
      this.cmbYea_NatAcD3.setMaximumSize(new Dimension(this.cmbYea_NatAcD3.getPreferredSize().width, this.cmbYea_NatAcD3.getPreferredSize().height));
      this.cmbName = new JComboBox(this.columns);
      this.cmbName.setMaximumSize(new Dimension(this.cmbName.getPreferredSize().width, this.cmbName.getPreferredSize().height));
      this.cmbIso1 = new JComboBox(this.columns);
      this.cmbIso1.setMaximumSize(new Dimension(this.cmbIso1.getPreferredSize().width, this.cmbIso1.getPreferredSize().height));
      this.cmbIso2 = new JComboBox(this.columns);
      this.cmbIso2.setMaximumSize(new Dimension(this.cmbIso2.getPreferredSize().width, this.cmbIso2.getPreferredSize().height));
      this.cmbIso3 = new JComboBox(this.columns);
      this.cmbIso3.setMaximumSize(new Dimension(this.cmbIso3.getPreferredSize().width, this.cmbIso3.getPreferredSize().height));
      this.cmbIso4 = new JComboBox(this.columns);
      this.cmbIso4.setMaximumSize(new Dimension(this.cmbIso4.getPreferredSize().width, this.cmbIso4.getPreferredSize().height));
      this.cmbIso5 = new JComboBox(this.columns);
      this.cmbIso5.setMaximumSize(new Dimension(this.cmbIso5.getPreferredSize().width, this.cmbIso5.getPreferredSize().height));
      this.cmbIso6 = new JComboBox(this.columns);
      this.cmbIso6.setMaximumSize(new Dimension(this.cmbIso6.getPreferredSize().width, this.cmbIso6.getPreferredSize().height));
      JLabel lblSeq = new JLabel("   Choose seq column ");
      lblSeq.setPreferredSize(new Dimension(lblSeq.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanSeq = new JPanel();
      this.jpanSeq.setLayout(new BoxLayout(this.jpanSeq, 0));
      this.jpanSeq.add(lblSeq);
      this.jpanSeq.add(Box.createHorizontalGlue());
      this.jpanSeq.add(this.cmbSeq);
      this.jpanSeq.add(Box.createHorizontalGlue());
      JLabel lblModSeq = new JLabel("   Choose modSeq column ");
      lblModSeq.setPreferredSize(new Dimension(lblModSeq.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanModSeq = new JPanel();
      this.jpanModSeq.setLayout(new BoxLayout(this.jpanModSeq, 0));
      this.jpanModSeq.add(lblModSeq);
      this.jpanModSeq.add(Box.createHorizontalGlue());
      this.jpanModSeq.add(this.cmbModSeq);
      this.jpanModSeq.add(Box.createHorizontalGlue());
      JLabel lblScore = new JLabel("   Choose charge column ");
      lblScore.setPreferredSize(new Dimension(lblScore.getPreferredSize().width, this.cmbCharge.getPreferredSize().height));
      JPanel jpanScore = new JPanel();
      jpanScore.setLayout(new BoxLayout(jpanScore, 0));
      jpanScore.add(lblScore);
      jpanScore.add(Box.createHorizontalGlue());
      jpanScore.add(this.cmbCharge);
      jpanScore.add(Box.createHorizontalGlue());
      JLabel lblAce = new JLabel("   Choose control Ace- intensity column ");
      lblAce.setPreferredSize(new Dimension(lblModSeq.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanContAce = new JPanel();
      this.jpanContAce.setLayout(new BoxLayout(this.jpanContAce, 0));
      this.jpanContAce.add(lblAce);
      this.jpanContAce.add(Box.createHorizontalGlue());
      this.jpanContAce.add(this.cmbContAce);
      this.jpanContAce.add(Box.createHorizontalGlue());
      JLabel lblAcD3 = new JLabel("   Choose control AcD3- intesity column ");
      lblAcD3.setPreferredSize(new Dimension(lblAcD3.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanContAcD3 = new JPanel();
      this.jpanContAcD3.setLayout(new BoxLayout(this.jpanContAcD3, 0));
      this.jpanContAcD3.add(lblAcD3);
      this.jpanContAcD3.add(Box.createHorizontalGlue());
      this.jpanContAcD3.add(this.cmbContAcD3);
      this.jpanContAcD3.add(Box.createHorizontalGlue());
      JLabel lblHumAce = new JLabel("   Choose human NatA Ace- intensity column ");
      lblHumAce.setPreferredSize(new Dimension(lblModSeq.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanHumAce = new JPanel();
      this.jpanHumAce.setLayout(new BoxLayout(this.jpanHumAce, 0));
      this.jpanHumAce.add(lblHumAce);
      this.jpanHumAce.add(Box.createHorizontalGlue());
      this.jpanHumAce.add(this.cmbHumAce);
      this.jpanHumAce.add(Box.createHorizontalGlue());
      JLabel lblHumAcD3 = new JLabel("   Choose human NatA AcD3- intesity column ");
      lblHumAcD3.setPreferredSize(new Dimension(lblHumAcD3.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanHumAcD3 = new JPanel();
      this.jpanHumAcD3.setLayout(new BoxLayout(this.jpanHumAcD3, 0));
      this.jpanHumAcD3.add(lblHumAcD3);
      this.jpanHumAcD3.add(Box.createHorizontalGlue());
      this.jpanHumAcD3.add(this.cmbHumAcD3);
      this.jpanHumAcD3.add(Box.createHorizontalGlue());
      JLabel lblYeaAce = new JLabel("   Choose Yeast-NatA Ace- intensity column ");
      lblYeaAce.setPreferredSize(new Dimension(lblYeaAce.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanYeaAce = new JPanel();
      this.jpanYeaAce.setLayout(new BoxLayout(this.jpanYeaAce, 0));
      this.jpanYeaAce.add(lblYeaAce);
      this.jpanYeaAce.add(Box.createHorizontalGlue());
      this.jpanYeaAce.add(this.cmbYea_NatAce);
      this.jpanYeaAce.add(Box.createHorizontalGlue());
      JLabel lblYeaAcD3 = new JLabel("   Choose Yeast-NatA AcD3- intesity column ");
      lblYeaAcD3.setPreferredSize(new Dimension(lblYeaAcD3.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanYeaAcD3 = new JPanel();
      this.jpanYeaAcD3.setLayout(new BoxLayout(this.jpanYeaAcD3, 0));
      this.jpanYeaAcD3.add(lblYeaAcD3);
      this.jpanYeaAcD3.add(Box.createHorizontalGlue());
      this.jpanYeaAcD3.add(this.cmbYea_NatAcD3);
      this.jpanYeaAcD3.add(Box.createHorizontalGlue());
      JLabel lblIso1 = new JLabel("   Choose iso1 ");
      lblIso1.setPreferredSize(new Dimension(lblIso1.getPreferredSize().width, this.cmbIso1.getPreferredSize().height));
      this.jpanIso1 = new JPanel();
      this.jpanIso1.setLayout(new BoxLayout(this.jpanIso1, 0));
      this.jpanIso1.add(lblIso1);
      this.jpanIso1.add(Box.createHorizontalGlue());
      this.jpanIso1.add(this.cmbIso1);
      this.jpanIso1.add(Box.createHorizontalGlue());
      JLabel lblIso2 = new JLabel("   Choose iso2 ");
      lblIso2.setPreferredSize(new Dimension(lblIso2.getPreferredSize().width, this.cmbIso2.getPreferredSize().height));
      this.jpanIso2 = new JPanel();
      this.jpanIso2.setLayout(new BoxLayout(this.jpanIso2, 0));
      this.jpanIso2.add(lblIso2);
      this.jpanIso2.add(Box.createHorizontalGlue());
      this.jpanIso2.add(this.cmbIso2);
      this.jpanIso2.add(Box.createHorizontalGlue());
      JLabel lblIso3 = new JLabel("   Choose iso3 ");
      lblIso3.setPreferredSize(new Dimension(lblIso3.getPreferredSize().width, this.cmbIso3.getPreferredSize().height));
      this.jpanIso3 = new JPanel();
      this.jpanIso3.setLayout(new BoxLayout(this.jpanIso3, 0));
      this.jpanIso3.add(lblIso3);
      this.jpanIso3.add(Box.createHorizontalGlue());
      this.jpanIso3.add(this.cmbIso3);
      this.jpanIso3.add(Box.createHorizontalGlue());
      JLabel lblIso4 = new JLabel("   Choose iso4 ");
      lblIso4.setPreferredSize(new Dimension(lblIso4.getPreferredSize().width, this.cmbIso4.getPreferredSize().height));
      this.jpanIso4 = new JPanel();
      this.jpanIso4.setLayout(new BoxLayout(this.jpanIso4, 0));
      this.jpanIso4.add(lblIso4);
      this.jpanIso4.add(Box.createHorizontalGlue());
      this.jpanIso4.add(this.cmbIso4);
      this.jpanIso4.add(Box.createHorizontalGlue());
      JLabel lblIso5 = new JLabel("   Choose iso5 ");
      lblIso5.setPreferredSize(new Dimension(lblIso5.getPreferredSize().width, this.cmbIso5.getPreferredSize().height));
      this.jpanIso5 = new JPanel();
      this.jpanIso5.setLayout(new BoxLayout(this.jpanIso5, 0));
      this.jpanIso5.add(lblIso5);
      this.jpanIso5.add(Box.createHorizontalGlue());
      this.jpanIso5.add(this.cmbIso5);
      this.jpanIso5.add(Box.createHorizontalGlue());
      JLabel lblIso6 = new JLabel("   Choose iso6 ");
      lblIso6.setPreferredSize(new Dimension(lblIso6.getPreferredSize().width, this.cmbIso6.getPreferredSize().height));
      this.jpanIso6 = new JPanel();
      this.jpanIso6.setLayout(new BoxLayout(this.jpanIso6, 0));
      this.jpanIso6.add(lblIso6);
      this.jpanIso6.add(Box.createHorizontalGlue());
      this.jpanIso6.add(this.cmbIso6);
      this.jpanIso6.add(Box.createHorizontalGlue());
      JLabel lblName = new JLabel("   Choose title column ");
      lblName.setPreferredSize(new Dimension(lblName.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanName = new JPanel();
      this.jpanName.setLayout(new BoxLayout(this.jpanName, 0));
      this.jpanName.add(lblName);
      this.jpanName.add(Box.createHorizontalGlue());
      this.jpanName.add(this.cmbName);
      this.jpanName.add(Box.createHorizontalGlue());
      this.btnProcessExcelFile.setText("Process excel file");
      this.btnProcessExcelFile.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            PetraYeastNat.this.processExcelBtnActionPerformed();
         }
      });
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanModSeq);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanSeq);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(jpanScore);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanContAce);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanContAcD3);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanHumAce);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanHumAcD3);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanYeaAce);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanYeaAcD3);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanName);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanIso1);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanIso2);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanIso3);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanIso4);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanIso5);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanIso6);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.btnProcessExcelFile);
      Graphics g = this.getGraphics();
      this.update(g);
      this.setSize(1000, 500);
   }

   public void processExcelBtnActionPerformed() {
      Vector seq = new Vector();

      try {
         ExcelColumn charge = (ExcelColumn)this.cmbCharge.getSelectedItem();
         int columnCharge = charge.getColumn();
         ExcelColumn modSeq = (ExcelColumn)this.cmbModSeq.getSelectedItem();
         int columnModSeq = modSeq.getColumn();
         ExcelColumn sequence = (ExcelColumn)this.cmbSeq.getSelectedItem();
         int columnSeq = sequence.getColumn();
         ExcelColumn contAce = (ExcelColumn)this.cmbContAce.getSelectedItem();
         int columnContAce = contAce.getColumn();
         ExcelColumn contAcD3 = (ExcelColumn)this.cmbContAcD3.getSelectedItem();
         int columnContAcD3 = contAcD3.getColumn();
         ExcelColumn humAce = (ExcelColumn)this.cmbHumAce.getSelectedItem();
         int columnHumAce = humAce.getColumn();
         ExcelColumn humAcD3 = (ExcelColumn)this.cmbHumAcD3.getSelectedItem();
         int columnHumAcD3 = humAcD3.getColumn();
         ExcelColumn yeaAce = (ExcelColumn)this.cmbYea_NatAce.getSelectedItem();
         int columnYeaAce = yeaAce.getColumn();
         ExcelColumn yeaAcD3 = (ExcelColumn)this.cmbYea_NatAcD3.getSelectedItem();
         int columnYeaAcD3 = yeaAcD3.getColumn();
         ExcelColumn title = (ExcelColumn)this.cmbName.getSelectedItem();
         int columnTitle = title.getColumn();
         ExcelColumn iso1Col = (ExcelColumn)this.cmbIso1.getSelectedItem();
         int columnIso1 = iso1Col.getColumn();
         ExcelColumn iso2Col = (ExcelColumn)this.cmbIso2.getSelectedItem();
         int columnIso2 = iso2Col.getColumn();
         ExcelColumn iso3Col = (ExcelColumn)this.cmbIso3.getSelectedItem();
         int columnIso3 = iso3Col.getColumn();
         ExcelColumn iso4Col = (ExcelColumn)this.cmbIso4.getSelectedItem();
         int columnIso4 = iso4Col.getColumn();
         ExcelColumn iso5Col = (ExcelColumn)this.cmbIso5.getSelectedItem();
         int columnIso5 = iso5Col.getColumn();
         ExcelColumn iso6Col = (ExcelColumn)this.cmbIso6.getSelectedItem();
         int columnIso6 = iso6Col.getColumn();
         int rowCount = 1;
         boolean nextRow = true;

         int i;
         Cell modCell;
         while(nextRow) {
            i = rowCount + 1;
            modCell = this.sheet.getCell(columnModSeq, i);
            if (modCell.getContents().equalsIgnoreCase(" ")) {
               nextRow = false;
            } else {
               rowCount = i;
            }
         }

         this.aceContInts = new double[rowCount];
         this.acD3ContInts = new double[rowCount];
         this.aceHumInts = new double[rowCount];
         this.acD3HumInts = new double[rowCount];
         this.aceYeaInts = new double[rowCount];
         this.acD3YeaInts = new double[rowCount];
         this.iso1 = new double[rowCount];
         this.iso2 = new double[rowCount];
         this.iso3 = new double[rowCount];
         this.iso4 = new double[rowCount];
         this.iso5 = new double[rowCount];
         this.iso6 = new double[rowCount];

         for(i = 1; i < rowCount + 1; ++i) {
            modCell = this.sheet.getCell(columnModSeq, i);
            String mod = modCell.getContents();
            Cell sequCell = this.sheet.getCell(columnSeq, i);
            String sequ = sequCell.getContents();
            Cell titCell = this.sheet.getCell(columnTitle, i);
            String name = titCell.getContents();
            SequenceSplitter split = new SequenceSplitter(mod);
            String[] modSeqSplit = split.getSequenceSplit();
            Cell accessCharge = this.sheet.getCell(columnCharge, i);
            int chargE = Integer.valueOf(accessCharge.getContents());
            Cell accessContAce = this.sheet.getCell(columnContAce, i);
            double aceContInt = Double.valueOf(accessContAce.getContents());
            Cell accessContAceD3 = this.sheet.getCell(columnContAcD3, i);
            double acD3ContInt = Double.valueOf(accessContAceD3.getContents());
            Cell accessHumAce = this.sheet.getCell(columnHumAce, i);
            double aceHumInt = Double.valueOf(accessHumAce.getContents());
            Cell accessHumAceD3 = this.sheet.getCell(columnHumAcD3, i);
            double acD3HumInt = Double.valueOf(accessHumAceD3.getContents());
            Cell accessYeaAce = this.sheet.getCell(columnYeaAce, i);
            double aceYeaInt = Double.valueOf(accessYeaAce.getContents());
            Cell accessYeaAceD3 = this.sheet.getCell(columnYeaAcD3, i);
            double acD3YeaInt = Double.valueOf(accessYeaAceD3.getContents());
            Cell IsoCell1 = this.sheet.getCell(columnIso1, i);
            double isoDouble1 = Double.valueOf(IsoCell1.getContents());
            Cell IsoCell2 = this.sheet.getCell(columnIso2, i);
            double isoDouble2 = Double.valueOf(IsoCell2.getContents());
            Cell IsoCell3 = this.sheet.getCell(columnIso3, i);
            double isoDouble3 = Double.valueOf(IsoCell3.getContents());
            Cell IsoCell4 = this.sheet.getCell(columnIso4, i);
            double isoDouble4 = Double.valueOf(IsoCell4.getContents());
            Cell IsoCell5 = this.sheet.getCell(columnIso5, i);
            double isoDouble5 = Double.valueOf(IsoCell5.getContents());
            Cell IsoCell6 = this.sheet.getCell(columnIso6, i);
            double isoDouble6 = Double.valueOf(IsoCell6.getContents());
            this.aceContInts[i - 1] = aceContInt;
            this.acD3ContInts[i - 1] = acD3ContInt;
            this.aceHumInts[i - 1] = aceHumInt;
            this.acD3HumInts[i - 1] = acD3HumInt;
            this.aceYeaInts[i - 1] = aceYeaInt;
            this.acD3YeaInts[i - 1] = acD3YeaInt;
            this.iso1[i - 1] = isoDouble1;
            this.iso2[i - 1] = isoDouble2;
            this.iso3[i - 1] = isoDouble3;
            this.iso4[i - 1] = isoDouble4;
            this.iso5[i - 1] = isoDouble5;
            this.iso6[i - 1] = isoDouble6;
            seq.add(new YeastNatElement(sequ, modSeqSplit, chargE, mod, name, aceContInt, acD3ContInt, aceHumInt, acD3HumInt, aceYeaInt, acD3YeaInt, isoDouble1, isoDouble2, isoDouble3, isoDouble4, isoDouble5, isoDouble6));
         }

         this.sequences = new YeastNatElement[seq.size()];
         seq.toArray(this.sequences);
      } catch (NumberFormatException var83) {
         var83.printStackTrace();
      }

      this.btnStart.setEnabled(true);
   }

   private void close() {
      this.setVisible(false);
      this.dispose();
   }

   public static void main(String[] args) {
      PetraYeastNat ms = new PetraYeastNat("Find isotope envelopes");
      ms.setVisible(true);
   }
}
