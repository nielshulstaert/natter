package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class MsIsotopeResult {
   private static Logger logger = Logger.getLogger(MsIsotopeResult.class);
   private String[] iSequence;
   private int iCharge;
   private String iModSeq;
   private String iTitle;
   private double[] percTot;
   private double[] percMax;
   private int H = 1;
   private int C = 0;
   private int N = 0;
   private int O = 0;
   private int S = 0;
   private int HDeut = 0;
   private int C13 = 0;
   private int N15 = 0;
   private int O18 = 0;
   private String ElementalComposition;

   public MsIsotopeResult(String[] iSequence, int iCharge, String iModSeq, String aTitle) {
      this.iSequence = iSequence;
      this.iCharge = iCharge;
      this.iModSeq = iModSeq;
      this.iTitle = aTitle;
   }

   public void setPercTot(double[] percTot) {
      this.percTot = percTot;
   }

   public void setPercMax(double[] percMax) {
      this.percMax = percMax;
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

   public String getTitle() {
      return this.iTitle;
   }

   public String getIModSeq() {
      return this.iModSeq;
   }

   public String[] getISequence() {
      return this.iSequence;
   }

   public int getICharge() {
      return this.iCharge;
   }

   public double[] getPercTot() {
      return this.percTot;
   }

   public double[] getPercMax() {
      return this.percMax;
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

   public String getIChargeAsString() {
      String charge = Integer.valueOf(this.iCharge).toString();
      return charge;
   }
}
