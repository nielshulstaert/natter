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

public class MsIsotopeGui extends JFrame {
   private static Logger logger = Logger.getLogger(MsIsotopeGui.class);
   private Connection iConn = null;
   private String iDBName;
   private ExcelColumn[] columns;
   private JTextField txtLocation = null;
   private JTextField txtLocationEle = null;
   private JComboBox cmbModSeq = null;
   private JComboBox cmbCharge = null;
   private JComboBox cmbAceInt = null;
   private JComboBox cmbAcD3Int = null;
   private JComboBox cmbName = null;
   private JButton btnSelectLocation = new JButton();
   private JButton btnSelectLocationEle = new JButton();
   private JButton btnStart = new JButton();
   private JButton btnCancel = new JButton();
   private JButton btnReadExcelFile = new JButton();
   private JButton btnProcessExcelFile = new JButton();
   private JPanel jpanModSeq;
   private JPanel jpanParametes;
   private JPanel jpanAce;
   private JPanel jpanAcD3;
   private JPanel jpanName;
   private Workbook excelFile;
   private WritableWorkbook excelSave;
   private WritableSheet sheetSave;
   private Sheet sheet;
   private MsIsotopeResult[] sequences;
   private double[] aceInts;
   private double[] acD3Ints;
   private PartialAceCouple[] couples;

   public MsIsotopeGui(String aTitle) {
      super(aTitle);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
            MsIsotopeGui.this.close();
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
      this.cmbCharge = new JComboBox(this.columns);
      this.cmbCharge.setMaximumSize(new Dimension(this.cmbCharge.getPreferredSize().width, this.cmbCharge.getPreferredSize().height));
      this.cmbAceInt = new JComboBox(this.columns);
      this.cmbAceInt.setMaximumSize(new Dimension(this.cmbAceInt.getPreferredSize().width, this.cmbAceInt.getPreferredSize().height));
      this.cmbAcD3Int = new JComboBox(this.columns);
      this.cmbAcD3Int.setMaximumSize(new Dimension(this.cmbAcD3Int.getPreferredSize().width, this.cmbAcD3Int.getPreferredSize().height));
      this.cmbName = new JComboBox(this.columns);
      this.cmbName.setMaximumSize(new Dimension(this.cmbName.getPreferredSize().width, this.cmbName.getPreferredSize().height));
   }

   public void constructScreen() {
      this.btnSelectLocation.setText("Select excel file location");
      this.btnSelectLocation.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            MsIsotopeGui.this.selectLocationBtnActionPerformed(evt);
         }
      });
      this.btnSelectLocationEle.setText("Select elements file location");
      this.btnSelectLocationEle.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            MsIsotopeGui.this.selectLocationBtnEleActionPerformed(evt);
         }
      });
      this.btnReadExcelFile.setText("Read excel file");
      this.btnReadExcelFile.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent evt) {
            MsIsotopeGui.this.readExcelBtnActionPerformed();
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
            MsIsotopeGui.this.startTriggered();
         }
      });
      this.btnStart.setEnabled(false);
      this.btnCancel = new JButton("Cancel");
      this.btnCancel.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            MsIsotopeGui.this.dispose();
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
      this.jpanParametes.setBorder(BorderFactory.createTitledBorder("Save parameters"));
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
         CompositionCalculater calcComp = new CompositionCalculater(elements, this.sequences);
         this.sequences = calcComp.getISequences();
         MsIsotopePost post = new MsIsotopePost(this.sequences);
         this.sequences = post.getMsIsotopeResults();
         MsIsotopeResult[] sequencesCouples = new MsIsotopeResult[this.sequences.length];

         for(int i = 0; i < this.sequences.length; ++i) {
            MsIsotopeResult res = this.sequences[i];
            String title = res.getTitle();
            String[] seqArray = res.getISequence();
            String modSeq = res.getIModSeq();
            int charge = res.getICharge();
            if (seqArray[0].equalsIgnoreCase("ace-")) {
               seqArray[0] = "AcD3-";
               modSeq = "AcD3-" + modSeq.substring(modSeq.indexOf("-") + 1);
            } else {
               seqArray[0] = "Ace-";
               modSeq = "Ace-" + modSeq.substring(modSeq.indexOf("-") + 1);
            }

            MsIsotopeResult resCouple = new MsIsotopeResult(seqArray, charge, modSeq, title);
            sequencesCouples[i] = resCouple;
         }

         CompositionCalculater calcCompCouple = new CompositionCalculater(elements, sequencesCouples);
         sequencesCouples = calcCompCouple.getISequences();
         MsIsotopePost postCouple = new MsIsotopePost(sequencesCouples);
         sequencesCouples = postCouple.getMsIsotopeResults();
         this.couples = new PartialAceCouple[this.sequences.length];

         for(int i = 0; i < this.sequences.length; ++i) {
            MsIsotopeResult res = this.sequences[i];
            String[] seqArray = res.getISequence();
            String modSeq = res.getIModSeq();
            int charge = res.getICharge();
            MsIsotopeResult ace;
            MsIsotopeResult acD3;
            if (seqArray[0].equalsIgnoreCase("ace-")) {
               ace = res;
               acD3 = sequencesCouples[i];
            } else {
               acD3 = res;
               ace = sequencesCouples[i];
            }

            PartialAceCouple couple = new PartialAceCouple(ace, acD3, this.aceInts[i], this.acD3Ints[i]);
            couple = this.calculateIntesities(couple);
            this.couples[i] = couple;
         }

         this.createExcelFile(this.txtLocation.getText());
         this.writeInfoInExcel();

         try {
            this.excelSave.write();
            this.excelSave.close();
         } catch (IOException var17) {
            var17.printStackTrace();
         } catch (Exception var18) {
            var18.printStackTrace();
         }

         JOptionPane.showMessageDialog(this, "Done, jippie", "Create successufl!", 1);
         this.dispose();
      } else {
         JOptionPane.showMessageDialog(this, "Element textfile??????", "?", 0);
      }
   }

   public PartialAceCouple calculateIntesities(PartialAceCouple couple) {
      double aceIntM = couple.getIAceIntM();
      double acD3IntM = couple.getIAcD3IntM();
      MsIsotopeResult ace = couple.getAce();
      MsIsotopeResult acD3 = couple.getAcD3();
      double[] percTotAce = ace.getPercTot();
      double[] percMaxAce = ace.getPercMax();
      double[] percTotAcD3 = acD3.getPercTot();
      double[] percMaxAcD3 = acD3.getPercMax();
      double aceIntTot = aceIntM / (percTotAce[0] / 100.0D);
      double intAceP3 = aceIntTot * (percTotAce[3] / 100.0D);
      double intAcD3P0 = acD3IntM - intAceP3;
      double acD3IntTot = intAcD3P0 / (percTotAcD3[0] / 100.0D);
      double intTot = aceIntTot + acD3IntTot;
      double percAce = aceIntTot / intTot * 100.0D;
      double percAcD3 = acD3IntTot / intTot * 100.0D;
      couple.setAcD3IntTot(acD3IntTot);
      couple.setAceIntTot(aceIntTot);
      couple.setIntAcD3P0(intAcD3P0);
      couple.setIntAceP3(intAceP3);
      couple.setIntTot(intTot);
      couple.setPercAcD3(percAcD3);
      couple.setPercAce(percAce);
      return couple;
   }

   private void createExcelFile(String location) {
      try {
         location = location.substring(0, location.indexOf(".xls")) + "update.xls";
         this.excelSave = Workbook.createWorkbook(new File(location));
         this.sheetSave = this.excelSave.createSheet("Sheet 1", 0);
         Label label1 = new Label(0, 0, "AcD3- modified sequence");
         Label label2 = new Label(1, 0, "Ace- modified sequence");
         Label label3 = new Label(2, 0, "AcD3- composition");
         Label label4 = new Label(3, 0, "Ace- composition");
         Label label5 = new Label(4, 0, "Ace measured intensity");
         Label label6 = new Label(5, 0, "AcD3 measured intensity");
         Label label7 = new Label(6, 0, "% Ace ");
         Label label8 = new Label(7, 0, "% AcD3");
         Label label9 = new Label(8, 0, "intensity Ace p3");
         Label label10 = new Label(9, 0, "intentisity AcD3 p0");
         Label label11 = new Label(10, 0, "filename");
         Label label12 = new Label(11, 0, "");
         Label label13 = new Label(12, 0, "% Tot O Ace");
         Label label14 = new Label(13, 0, "% Tot 1 Ace");
         Label label15 = new Label(14, 0, "% Tot 2 Ace");
         Label label16 = new Label(15, 0, "% Tot 3 Ace");
         Label label17 = new Label(16, 0, "% Tot 4 Ace");
         Label label18 = new Label(17, 0, "% Max 0 Ace");
         Label label19 = new Label(18, 0, "% Max 1 Ace");
         Label label20 = new Label(19, 0, "% Max 2 Ace");
         Label label21 = new Label(20, 0, "% Max 3 Ace");
         Label label22 = new Label(21, 0, "% Max 4 Ace");
         Label label23 = new Label(22, 0, "% Tot O AcD3");
         Label label24 = new Label(23, 0, "% Tot 1 AcD3");
         Label label25 = new Label(24, 0, "% Tot 2 AcD3");
         Label label26 = new Label(25, 0, "% Tot 3 AcD3");
         Label label27 = new Label(26, 0, "% Tot 4 AcD3");
         Label label28 = new Label(27, 0, "% Max 0 AcD3");
         Label label29 = new Label(28, 0, "% Max 1 AcD3");
         Label label30 = new Label(29, 0, "% Max 2 AcD3");
         Label label31 = new Label(30, 0, "% Max 3 AcD3");
         Label label32 = new Label(31, 0, "% Max 4 AcD3");
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
         this.sheetSave.addCell(label28);
         this.sheetSave.addCell(label29);
         this.sheetSave.addCell(label30);
         this.sheetSave.addCell(label31);
         this.sheetSave.addCell(label32);
      } catch (IOException var34) {
         var34.printStackTrace();
      } catch (WriteException var35) {
         var35.printStackTrace();
      }

   }

   public void writeInfoInExcel() {
      try {
         for(int i = 0; i < this.couples.length; ++i) {
            PartialAceCouple couple = this.couples[i];
            double aceIntM = couple.getIAceIntM();
            double acD3IntM = couple.getIAcD3IntM();
            MsIsotopeResult ace = couple.getAce();
            MsIsotopeResult acD3 = couple.getAcD3();
            double[] percTotAce = ace.getPercTot();
            double[] percMaxAce = ace.getPercMax();
            double[] percTotAcD3 = acD3.getPercTot();
            double[] percMaxAcD3 = acD3.getPercMax();
            Label label1 = new Label(0, i + 1, ace.getIModSeq());
            Label label2 = new Label(1, i + 1, acD3.getIModSeq());
            Label label3 = new Label(2, i + 1, ace.getElementalComposition());
            Label label4 = new Label(3, i + 1, acD3.getElementalComposition());
            Label label5 = new Label(4, i + 1, String.valueOf(couple.getIAceIntM()));
            Label label6 = new Label(5, i + 1, String.valueOf(couple.getIAcD3IntM()));
            Label label7 = new Label(6, i + 1, String.valueOf(couple.getPercAce()));
            Label label8 = new Label(7, i + 1, String.valueOf(couple.getPercAcD3()));
            Label label9 = new Label(8, i + 1, String.valueOf(couple.getIntAceP3()));
            Label label10 = new Label(9, i + 1, String.valueOf(couple.getIntAcD3P0()));
            Label label11 = new Label(10, i + 1, ace.getTitle());
            Label label12 = new Label(11, i + 1, "");
            Label label13 = new Label(12, i + 1, String.valueOf(percTotAce[0]));
            Label label14 = new Label(13, i + 1, String.valueOf(percTotAce[1]));
            Label label15 = new Label(14, i + 1, String.valueOf(percTotAce[2]));
            Label label16 = new Label(15, i + 1, String.valueOf(percTotAce[3]));
            Label label17 = new Label(16, i + 1, String.valueOf(percTotAce[4]));
            Label label18 = new Label(17, i + 1, String.valueOf(percMaxAce[0]));
            Label label19 = new Label(18, i + 1, String.valueOf(percMaxAce[1]));
            Label label20 = new Label(19, i + 1, String.valueOf(percMaxAce[2]));
            Label label21 = new Label(20, i + 1, String.valueOf(percMaxAce[3]));
            Label label22 = new Label(21, i + 1, String.valueOf(percMaxAce[4]));
            Label label23 = new Label(22, i + 1, String.valueOf(percTotAcD3[0]));
            Label label24 = new Label(23, i + 1, String.valueOf(percTotAcD3[1]));
            Label label25 = new Label(24, i + 1, String.valueOf(percTotAcD3[2]));
            Label label26 = new Label(25, i + 1, String.valueOf(percTotAcD3[3]));
            Label label27 = new Label(26, i + 1, String.valueOf(percTotAcD3[4]));
            Label label28 = new Label(27, i + 1, String.valueOf(percMaxAcD3[0]));
            Label label29 = new Label(28, i + 1, String.valueOf(percMaxAcD3[1]));
            Label label30 = new Label(29, i + 1, String.valueOf(percMaxAcD3[2]));
            Label label31 = new Label(30, i + 1, String.valueOf(percMaxAcD3[3]));
            Label label32 = new Label(31, i + 1, String.valueOf(percMaxAcD3[4]));
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
            this.sheetSave.addCell(label28);
            this.sheetSave.addCell(label29);
            this.sheetSave.addCell(label30);
            this.sheetSave.addCell(label31);
            this.sheetSave.addCell(label32);
         }
      } catch (WriteException var45) {
         var45.printStackTrace();
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
      this.cmbCharge = new JComboBox(this.columns);
      this.cmbCharge.setMaximumSize(new Dimension(this.cmbCharge.getPreferredSize().width, this.cmbCharge.getPreferredSize().height));
      this.cmbAceInt = new JComboBox(this.columns);
      this.cmbAceInt.setMaximumSize(new Dimension(this.cmbAceInt.getPreferredSize().width, this.cmbAceInt.getPreferredSize().height));
      this.cmbAcD3Int = new JComboBox(this.columns);
      this.cmbAcD3Int.setMaximumSize(new Dimension(this.cmbAcD3Int.getPreferredSize().width, this.cmbAcD3Int.getPreferredSize().height));
      this.cmbName = new JComboBox(this.columns);
      this.cmbName.setMaximumSize(new Dimension(this.cmbName.getPreferredSize().width, this.cmbName.getPreferredSize().height));
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
      JLabel lblAce = new JLabel("   Choose Ace intensity column ");
      lblAce.setPreferredSize(new Dimension(lblModSeq.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanAce = new JPanel();
      this.jpanAce.setLayout(new BoxLayout(this.jpanAce, 0));
      this.jpanAce.add(lblAce);
      this.jpanAce.add(Box.createHorizontalGlue());
      this.jpanAce.add(this.cmbAceInt);
      this.jpanAce.add(Box.createHorizontalGlue());
      JLabel lblAcD3 = new JLabel("   Choose AcD3 intesity column ");
      lblAcD3.setPreferredSize(new Dimension(lblAcD3.getPreferredSize().width, this.cmbModSeq.getPreferredSize().height));
      this.jpanAcD3 = new JPanel();
      this.jpanAcD3.setLayout(new BoxLayout(this.jpanAcD3, 0));
      this.jpanAcD3.add(lblAcD3);
      this.jpanAcD3.add(Box.createHorizontalGlue());
      this.jpanAcD3.add(this.cmbAcD3Int);
      this.jpanAcD3.add(Box.createHorizontalGlue());
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
            MsIsotopeGui.this.processExcelBtnActionPerformed();
         }
      });
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanModSeq);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(jpanScore);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanAce);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanAcD3);
      this.jpanParametes.add(Box.createVerticalStrut(5));
      this.jpanParametes.add(this.jpanName);
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
         ExcelColumn ace = (ExcelColumn)this.cmbAceInt.getSelectedItem();
         int columnAce = ace.getColumn();
         ExcelColumn AcD3 = (ExcelColumn)this.cmbAcD3Int.getSelectedItem();
         int columnAcD3 = AcD3.getColumn();
         ExcelColumn title = (ExcelColumn)this.cmbName.getSelectedItem();
         int columnTitle = title.getColumn();
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

         this.aceInts = new double[rowCount];
         this.acD3Ints = new double[rowCount];

         for(i = 1; i < rowCount + 1; ++i) {
            modCell = this.sheet.getCell(columnModSeq, i);
            String mod = modCell.getContents();
            Cell titCell = this.sheet.getCell(columnTitle, i);
            String name = titCell.getContents();
            SequenceSplitter split = new SequenceSplitter(mod);
            String[] modSeqSplit = split.getSequenceSplit();
            Cell accessCharge = this.sheet.getCell(columnCharge, i);
            int chargE = Integer.valueOf(accessCharge.getContents());
            Cell accessAce = this.sheet.getCell(columnAce, i);
            double aceInt = Double.valueOf(accessAce.getContents());
            Cell accessAceD3 = this.sheet.getCell(columnAcD3, i);
            double acD3Int = Double.valueOf(accessAceD3.getContents());
            this.aceInts[i - 1] = aceInt;
            this.acD3Ints[i - 1] = acD3Int;
            seq.add(new MsIsotopeResult(modSeqSplit, chargE, mod, name));
         }

         this.sequences = new MsIsotopeResult[seq.size()];
         seq.toArray(this.sequences);
      } catch (NumberFormatException var29) {
         var29.printStackTrace();
      }

      this.btnStart.setEnabled(true);
   }

   private void close() {
      this.setVisible(false);
      this.dispose();
   }

   public static void main(String[] args) {
      MsIsotopeGui ms = new MsIsotopeGui("Find isotope envelopes");
      ms.setVisible(true);
   }
}
