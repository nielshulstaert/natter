package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class YeastNatElement {
   private static Logger logger = Logger.getLogger(YeastNatElement.class);
   private String[] iSequenceSplit;
   private String iSequence;
   private int iCharge;
   private String iModSeq;
   private String iTitle;
   private double[] percContAceTot;
   private double[] percContAceMax;
   private double[] percContAcD3Tot;
   private double[] percContAcD3Max;
   private double[] percHumAceTot;
   private double[] percHumAceMax;
   private double[] percHumAcD3Tot;
   private double[] percHumAcD3Max;
   private double[] percYeaAceTot;
   private double[] percYeaAceMax;
   private double[] percYeaAcD3Tot;
   private double[] percYeaAcD3Max;
   private double[] percIonTot;
   private double[] percIonMax;
   private IonEnvelop contAce;
   private IonEnvelop contAcD3;
   private IonEnvelop humAce;
   private IonEnvelop humAcD3;
   private IonEnvelop yeastAce;
   private IonEnvelop yeastAcD3;
   private IonEnvelop contAceIso;
   private IonEnvelop contAcD3Iso;
   private IonEnvelop humAceIso;
   private IonEnvelop humAcD3Iso;
   private IonEnvelop yeastAceIso;
   private IonEnvelop yeastAcD3Iso;
   private double iContAce;
   private double iContAcD3;
   private double iHumAce;
   private double iHumAcD3;
   private double iYeaAce;
   private double iYeaAcD3;
   private double iIso1;
   private double iIso2;
   private double iIso3;
   private double iIso4;
   private double iIso5;
   private double iIso6;
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

   public YeastNatElement(String aSequence, String[] aSequenceSplit, int aCharge, String aModSeq, String aTitle, double aContAce, double aContAcD3, double aHumAce, double aHumAcD3, double aYeaAce, double aYeaAcD3) {
      this.iSequence = aSequence;
      this.iSequenceSplit = aSequenceSplit;
      this.iCharge = aCharge;
      this.iModSeq = aModSeq;
      this.iTitle = aTitle;
      this.iContAce = aContAce;
      this.iContAcD3 = aContAcD3;
      this.iHumAce = aHumAce;
      this.iHumAcD3 = aHumAcD3;
      this.iYeaAce = aYeaAce;
      this.iYeaAcD3 = aYeaAcD3;
   }

   public YeastNatElement(String iSequence, String[] iSequenceSplit, int iCharge, String iModSeq, String iTitle, double iContAce, double iContAcD3, double iHumAce, double iHumAcD3, double iYeaAce, double iYeaAcD3, double iIso1, double iIso2, double iIso3, double iIso4, double iIso5, double iIso6) {
      this.iSequenceSplit = iSequenceSplit;
      this.iSequence = iSequence;
      this.iCharge = iCharge;
      this.iModSeq = iModSeq;
      this.iTitle = iTitle;
      this.iContAce = iContAce;
      this.iContAcD3 = iContAcD3;
      this.iHumAce = iHumAce;
      this.iHumAcD3 = iHumAcD3;
      this.iYeaAce = iYeaAce;
      this.iYeaAcD3 = iYeaAcD3;
      this.iIso1 = iIso1;
      this.iIso2 = iIso2;
      this.iIso3 = iIso3;
      this.iIso4 = iIso4;
      this.iIso5 = iIso5;
      this.iIso6 = iIso6;
   }

   public void calculateIons() {
      double max = 0.0D;
      double sum = 0.0D;
      if (max < this.iIso1) {
         max = this.iIso1;
      }

      sum += this.iIso1;
      if (max < this.iIso2) {
         max = this.iIso2;
      }

      sum += this.iIso2;
      if (max < this.iIso3) {
         max = this.iIso3;
      }

      sum += this.iIso3;
      if (max < this.iIso4) {
         max = this.iIso4;
      }

      sum += this.iIso4;
      if (max < this.iIso5) {
         max = this.iIso5;
      }

      sum += this.iIso5;
      if (max < this.iIso6) {
         max = this.iIso6;
      }

      sum += this.iIso6;
      this.percIonTot = new double[6];
      this.percIonMax = new double[6];
      this.percIonMax[0] = this.iIso1 / max;
      this.percIonTot[0] = this.iIso1 / sum;
      this.percIonMax[1] = this.iIso2 / max;
      this.percIonTot[1] = this.iIso2 / sum;
      this.percIonMax[2] = this.iIso3 / max;
      this.percIonTot[2] = this.iIso3 / sum;
      this.percIonMax[3] = this.iIso4 / max;
      this.percIonTot[3] = this.iIso5 / sum;
      this.percIonMax[4] = this.iIso5 / max;
      this.percIonTot[4] = this.iIso5 / sum;
      this.percIonMax[5] = this.iIso6 / max;
      this.percIonTot[5] = this.iIso6 / sum;
      this.contAceIso = new IonEnvelop(this.iContAce, this.percIonMax, this.percIonTot);
      this.contAcD3Iso = new IonEnvelop(this.iContAcD3 - this.contAceIso.getIntensityAtPeak(4), this.percIonMax, this.percIonTot);
      this.humAceIso = new IonEnvelop(this.iHumAce - this.contAcD3Iso.getIntensityAtPeak(4), this.percIonMax, this.percIonTot);
      this.humAcD3Iso = new IonEnvelop(this.iHumAcD3 - this.humAceIso.getIntensityAtPeak(4), this.percIonMax, this.percIonTot);
      this.yeastAceIso = new IonEnvelop(this.iYeaAce - this.humAcD3Iso.getIntensityAtPeak(2) - this.humAceIso.getIntensityAtPeak(5), this.percIonMax, this.percIonTot);
      this.yeastAcD3Iso = new IonEnvelop(this.iYeaAcD3 - this.yeastAceIso.getIntensityAtPeak(4) - this.humAcD3Iso.getIntensityAtPeak(5), this.percIonMax, this.percIonTot);
   }

   public void setPercContAceTot(double[] percContAceTot) {
      this.percContAceTot = percContAceTot;
      this.contAce = new IonEnvelop(this.iContAce, this.percContAceMax, percContAceTot);
   }

   public void setPercContAceMax(double[] percContAceMax) {
      this.percContAceMax = percContAceMax;
   }

   public void setPercContAcD3Tot(double[] percContAcD3Tot) {
      this.percContAcD3Tot = percContAcD3Tot;
      this.contAcD3 = new IonEnvelop(this.iContAcD3 - this.contAce.getIntensityAtPeak(4), this.percContAcD3Max, percContAcD3Tot);
   }

   public void setPercContAcD3Max(double[] percContAcD3Max) {
      this.percContAcD3Max = percContAcD3Max;
   }

   public void setPercHumAceTot(double[] percHumAceTot) {
      this.percHumAceTot = percHumAceTot;
      this.humAce = new IonEnvelop(this.iHumAce - this.contAcD3.getIntensityAtPeak(4) - this.contAce.getIntensityAtPeak(7), percHumAceTot, this.percHumAceMax);
   }

   public void setPercHumAceMax(double[] percHumAceMax) {
      this.percHumAceMax = percHumAceMax;
   }

   public void setPercHumAcD3Tot(double[] percHumAcD3Tot) {
      this.percHumAcD3Tot = percHumAcD3Tot;
      this.humAcD3 = new IonEnvelop(this.iHumAcD3 - this.humAce.getIntensityAtPeak(4) - this.contAcD3.getIntensityAtPeak(7), percHumAcD3Tot, this.percHumAcD3Max);
   }

   public void setPercHumAcD3Max(double[] percHumAcD3Max) {
      this.percHumAcD3Max = percHumAcD3Max;
   }

   public void setPercYeaAceTot(double[] percYeaAceTot) {
      this.percYeaAceTot = percYeaAceTot;
      this.yeastAce = new IonEnvelop(this.iYeaAce - this.humAcD3.getIntensityAtPeak(2) - this.humAce.getIntensityAtPeak(5) - this.contAcD3.getIntensityAtPeak(8), percYeaAceTot, this.percYeaAceMax);
   }

   public void setPercYeaAceMax(double[] percYeaAceMax) {
      this.percYeaAceMax = percYeaAceMax;
   }

   public void setPercYeaAcD3Tot(double[] percYeaAcD3Tot) {
      this.percYeaAcD3Tot = percYeaAcD3Tot;
      this.yeastAcD3 = new IonEnvelop(this.iYeaAcD3 - this.yeastAce.getIntensityAtPeak(4) - this.humAcD3.getIntensityAtPeak(5) - this.humAce.getIntensityAtPeak(8), percYeaAcD3Tot, this.percYeaAcD3Max);
   }

   public void setPercYeaAcD3Max(double[] percYeaAcD3Max) {
      this.percYeaAcD3Max = percYeaAcD3Max;
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

   public String[] getSequenceSplit() {
      return this.iSequenceSplit;
   }

   public String getTitle() {
      return this.iTitle;
   }

   public String getIModSeq() {
      return this.iModSeq;
   }

   public String getSequence() {
      return this.iSequence;
   }

   public int getICharge() {
      return this.iCharge;
   }

   public double[] getPercContAceTot() {
      return this.percContAceTot;
   }

   public double[] getPercContAceMax() {
      return this.percContAceMax;
   }

   public double[] getPercContAcD3Tot() {
      return this.percContAcD3Tot;
   }

   public double[] getPercContAcD3Max() {
      return this.percContAcD3Max;
   }

   public double[] getPercHumAceTot() {
      return this.percHumAceTot;
   }

   public double[] getPercHumAceMax() {
      return this.percHumAceMax;
   }

   public double[] getPercHumAcD3Tot() {
      return this.percHumAcD3Tot;
   }

   public double[] getPercHumAcD3Max() {
      return this.percHumAcD3Max;
   }

   public double[] getPercYeaAceTot() {
      return this.percYeaAceTot;
   }

   public double[] getPercYeaAceMax() {
      return this.percYeaAceMax;
   }

   public double[] getPercYeaAcD3Tot() {
      return this.percYeaAcD3Tot;
   }

   public double[] getPercYeaAcD3Max() {
      return this.percYeaAcD3Max;
   }

   public String getElementalCompositionBasic() {
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

   public String getElementalCompositionContAce() {
      String comp = "";
      if (this.C > 0) {
         comp = comp + " C" + (this.C + 2);
      }

      if (this.H > 0) {
         comp = comp + " H" + (this.H + 2);
      }

      if (this.N > 0) {
         comp = comp + " N" + this.N;
      }

      if (this.O > 0) {
         comp = comp + " O" + (this.O + 1);
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

   public String getElementalCompositionContAcD3() {
      String comp = "";
      if (this.C > 0) {
         comp = comp + " C" + (this.C + 2);
      }

      if (this.H > 0) {
         comp = comp + " H" + (this.H - 1);
      }

      if (this.N > 0) {
         comp = comp + " N" + this.N;
      }

      if (this.O > 0) {
         comp = comp + " O" + (this.O + 1);
      }

      if (this.S > 0) {
         comp = comp + " S" + this.S;
      }

      if (this.C13 > 0) {
         comp = comp + " 13C" + this.C13;
      }

      comp = comp + " D" + (this.HDeut + 3);
      if (this.N15 > 0) {
         comp = comp + " 15N" + this.N15;
      }

      if (this.O18 > 0) {
         comp = comp + " 18O" + this.O18;
      }

      return comp;
   }

   public String getElementalCompositionHumAce() {
      String comp = "";
      if (this.C > 0) {
         comp = comp + " C" + (this.C + 2 - 6);
      }

      if (this.H > 0) {
         comp = comp + " H" + (this.H + 2);
      }

      if (this.N > 0) {
         comp = comp + " N" + this.N;
      }

      if (this.O > 0) {
         comp = comp + " O" + (this.O + 1);
      }

      if (this.S > 0) {
         comp = comp + " S" + this.S;
      }

      comp = comp + " 13C" + (this.C13 + 6);
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

   public String getElementalCompositionHumAcD3() {
      String comp = "";
      if (this.C > 0) {
         comp = comp + " C" + (this.C + 2 - 6);
      }

      if (this.H > 0) {
         comp = comp + " H" + (this.H - 1);
      }

      if (this.N > 0) {
         comp = comp + " N" + this.N;
      }

      if (this.O > 0) {
         comp = comp + " O" + (this.O + 1);
      }

      if (this.S > 0) {
         comp = comp + " S" + this.S;
      }

      comp = comp + " 13C" + (this.C13 + 6);
      comp = comp + " D" + (this.HDeut + 3);
      if (this.N15 > 0) {
         comp = comp + " 15N" + this.N15;
      }

      if (this.O18 > 0) {
         comp = comp + " 18O" + this.O18;
      }

      return comp;
   }

   public String getElementalCompositionYeaAce() {
      String comp = "";
      if (this.C > 0) {
         comp = comp + " C" + (this.C + 2 - 6);
      }

      if (this.H > 0) {
         comp = comp + " H" + (this.H + 2);
      }

      if (this.N > 0) {
         comp = comp + " N" + (this.N - 4);
      }

      if (this.O > 0) {
         comp = comp + " O" + (this.O + 1);
      }

      if (this.S > 0) {
         comp = comp + " S" + this.S;
      }

      comp = comp + " 13C" + (this.C13 + 6);
      if (this.HDeut > 0) {
         comp = comp + " D" + this.HDeut;
      }

      comp = comp + " 15N" + (this.N15 + 4);
      if (this.O18 > 0) {
         comp = comp + " 18O" + this.O18;
      }

      return comp;
   }

   public String getElementalCompositionYeaAcD3() {
      String comp = "";
      if (this.C > 0) {
         comp = comp + " C" + (this.C + 2 - 6);
      }

      if (this.H > 0) {
         comp = comp + " H" + (this.H - 1);
      }

      if (this.N > 0) {
         comp = comp + " N" + (this.N - 4);
      }

      if (this.O > 0) {
         comp = comp + " O" + (this.O + 1);
      }

      if (this.S > 0) {
         comp = comp + " S" + this.S;
      }

      comp = comp + " 13C" + (this.C13 + 6);
      comp = comp + " D" + (this.HDeut + 3);
      comp = comp + " 15N" + (this.N15 + 4);
      if (this.O18 > 0) {
         comp = comp + " 18O" + this.O18;
      }

      return comp;
   }

   public double getContAcePartialPerc() {
      double perc = 0.0D;
      perc = this.contAce.getTotaleIntensity() / (this.contAce.getTotaleIntensity() + this.contAcD3.getTotaleIntensity());
      return perc;
   }

   public double getContAcD3PartialPerc() {
      double perc = 0.0D;
      perc = this.contAcD3.getTotaleIntensity() / (this.contAce.getTotaleIntensity() + this.contAcD3.getTotaleIntensity());
      return perc;
   }

   public double getHumAcePartialPerc() {
      double perc = 0.0D;
      perc = this.humAce.getTotaleIntensity() / (this.humAce.getTotaleIntensity() + this.humAcD3.getTotaleIntensity());
      return perc;
   }

   public double getHumAcD3PartialPerc() {
      double perc = 0.0D;
      perc = this.humAcD3.getTotaleIntensity() / (this.humAce.getTotaleIntensity() + this.humAcD3.getTotaleIntensity());
      return perc;
   }

   public double getYeastAcePartialPerc() {
      double perc = 0.0D;
      perc = this.yeastAce.getTotaleIntensity() / (this.yeastAce.getTotaleIntensity() + this.yeastAcD3.getTotaleIntensity());
      return perc;
   }

   public double getYeastAcD3PartialPerc() {
      double perc = 0.0D;
      perc = this.yeastAcD3.getTotaleIntensity() / (this.yeastAce.getTotaleIntensity() + this.yeastAcD3.getTotaleIntensity());
      return perc;
   }

   public double getContAcePartialPercIso() {
      double perc = 0.0D;
      perc = this.contAceIso.getTotaleIntensity() / (this.contAceIso.getTotaleIntensity() + this.contAcD3Iso.getTotaleIntensity());
      return perc;
   }

   public double getContAcD3PartialPercIso() {
      double perc = 0.0D;
      perc = this.contAcD3Iso.getTotaleIntensity() / (this.contAceIso.getTotaleIntensity() + this.contAcD3Iso.getTotaleIntensity());
      return perc;
   }

   public double getHumAcePartialPercIso() {
      double perc = 0.0D;
      perc = this.humAceIso.getTotaleIntensity() / (this.humAceIso.getTotaleIntensity() + this.humAcD3Iso.getTotaleIntensity());
      return perc;
   }

   public double getHumAcD3PartialPercIso() {
      double perc = 0.0D;
      perc = this.humAcD3Iso.getTotaleIntensity() / (this.humAceIso.getTotaleIntensity() + this.humAcD3Iso.getTotaleIntensity());
      return perc;
   }

   public double getYeastAcePartialPercIso() {
      double perc = 0.0D;
      perc = this.yeastAceIso.getTotaleIntensity() / (this.yeastAceIso.getTotaleIntensity() + this.yeastAcD3Iso.getTotaleIntensity());
      return perc;
   }

   public double getYeastAcD3PartialPercIso() {
      double perc = 0.0D;
      perc = this.yeastAcD3Iso.getTotaleIntensity() / (this.yeastAceIso.getTotaleIntensity() + this.yeastAcD3Iso.getTotaleIntensity());
      return perc;
   }

   public IonEnvelop getContAceIonEnvelop() {
      return this.contAce;
   }

   public IonEnvelop getContAcD3IonEnvelop() {
      return this.contAcD3;
   }

   public IonEnvelop getHumAceIonEnvelop() {
      return this.humAce;
   }

   public IonEnvelop getHumAcD3IonEnvelop() {
      return this.humAcD3;
   }

   public IonEnvelop getYeastAceIonEnvelop() {
      return this.yeastAce;
   }

   public IonEnvelop getYeastAcD3IonEnvelop() {
      return this.yeastAcD3;
   }

   public String getChargeAsString() {
      String charge = Integer.valueOf(this.iCharge).toString();
      return charge;
   }
}
