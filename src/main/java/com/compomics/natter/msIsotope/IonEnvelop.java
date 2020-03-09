package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class IonEnvelop {
   private static Logger logger = Logger.getLogger(IonEnvelop.class);
   private double iIntensity1;
   private double[] iIntMax;
   private double[] iIntTot;

   public IonEnvelop(double aIntensity1, double[] aIntMax, double[] aIntTot) {
      this.iIntensity1 = aIntensity1;
      this.iIntMax = aIntMax;
      this.iIntTot = aIntTot;
   }

   public double getTotaleIntensity() {
      double totaleInt = 0.0D;

      for(int i = 0; i < this.iIntTot.length; ++i) {
         totaleInt += this.iIntensity1 / this.iIntTot[0] * this.iIntTot[i];
      }

      return totaleInt;
   }

   public double getIntensityAtPeak(int aPeak) {
      double intentsity = 0.0D;
      intentsity = this.iIntensity1 / this.iIntTot[0] * this.iIntTot[aPeak - 1];
      return intentsity;
   }
}
