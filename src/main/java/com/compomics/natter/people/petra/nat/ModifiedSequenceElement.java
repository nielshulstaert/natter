package com.compomics.natter.people.petra.nat;

import com.compomics.natter.msIsotope.Element;
import com.compomics.natter.msIsotope.SequenceSplitter;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;

public class ModifiedSequenceElement {
   private static Logger logger = Logger.getLogger(ModifiedSequenceElement.class);
   private int H = 1;
   private int C = 0;
   private int N = 0;
   private int O = 0;
   private int S = 0;
   private int HDeut = 0;
   private int C13 = 0;
   private int N15 = 0;
   private int O18 = 0;
   private String iModifiedSequence;
   private Element[] iElements;
   private String[] iSplitted;
   private JTextArea iText;

   public ModifiedSequenceElement(String aModifiedSequence, Element[] aElements, JTextArea lText) {
      this.iText = lText;
      this.iModifiedSequence = aModifiedSequence;
      this.iElements = aElements;
      SequenceSplitter split = new SequenceSplitter(this.iModifiedSequence);
      this.iSplitted = split.getSequenceSplit();
      this.calculate();
   }

   public ModifiedSequenceElement() {
   }

   public void calculate() {
      int lH = 0;
      int lC = 0;
      int lN = 0;
      int lO = 0;
      int lS = 0;
      int lHDeut = 0;
      int lC13 = 0;
      int lN15 = 0;
      int lO18 = 0;

      for(int k = 0; k < this.iSplitted.length; ++k) {
         boolean foundElement = false;

         for(int j = 0; j < this.iElements.length; ++j) {
            Element ele = this.iElements[j];
            String eleName = ele.getIName();
            if (this.iSplitted[k].equalsIgnoreCase(eleName)) {
               lH += ele.getH();
               lC += ele.getC();
               lN += ele.getN();
               lO += ele.getO();
               lS += ele.getS();
               lHDeut += ele.getHDeut();
               lC13 += ele.getC13();
               lN15 += ele.getN15();
               lO18 += ele.getO18();
               foundElement = true;
            }
         }

         if (!foundElement) {
            this.iText.append("\n" + this.iSplitted[k] + " was not found in the element file\n");
         }
      }

      this.setH(lH);
      this.setC(lC);
      this.setN(lN);
      this.setO(lO);
      this.setS(lS);
      this.setHDeut(lHDeut);
      this.setC13(lC13);
      this.setN15(lN15);
      this.setO18(lO18);
   }

   public void setH(int h) {
      this.H = h;
   }

   public void setC(int c) {
      this.C = c;
   }

   public void setN(int n) {
      this.N = n;
   }

   public void setO(int o) {
      this.O = o;
   }

   public void setS(int s) {
      this.S = s;
   }

   public void setHDeut(int HDeut) {
      this.HDeut = HDeut;
   }

   public void setC13(int c13) {
      this.C13 = c13;
   }

   public void setN15(int n15) {
      this.N15 = n15;
   }

   public void setO18(int o18) {
      this.O18 = o18;
   }

   public String getElementalComposition() {
      String comp = "";
      if (this.C > 0) {
         comp = comp + " C" + this.C;
      }

      if (this.H > 0) {
         comp = comp + " H" + this.H;
      }

      if (this.N > 0) {
         comp = comp + " N" + this.N;
      }

      if (this.O > 0) {
         comp = comp + " O" + this.O;
      }

      if (this.S > 0) {
         comp = comp + " S" + this.S;
      }

      if (this.C13 > 0) {
         comp = comp + " 13C" + this.C13;
      }

      if (this.HDeut > 0) {
         comp = comp + " D" + this.HDeut;
      }

      if (this.N15 > 0) {
         comp = comp + " 15N" + this.N15;
      }

      if (this.O18 > 0) {
         comp = comp + " 18O" + this.O18;
      }

      return comp;
   }

   public int getH() {
      return this.H;
   }

   public int getC() {
      return this.C;
   }

   public int getN() {
      return this.N;
   }

   public int getO() {
      return this.O;
   }

   public int getS() {
      return this.S;
   }

   public int getHDeut() {
      return this.HDeut;
   }

   public int getC13() {
      return this.C13;
   }

   public int getN15() {
      return this.N15;
   }

   public int getO18() {
      return this.O18;
   }
}
