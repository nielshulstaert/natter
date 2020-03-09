package com.compomics.natter.msIsotope;

import com.compomics.natter.people.petra.nat.ModifiedSequenceElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.commons.math.distribution.BinomialDistributionImpl;

public class IsotopeCalculator {
   private List<Double> iPercTot;
   private List<Double> iPercMax;

   public IsotopeCalculator(int lC, int lN, int lH, int lO, int lS) {
      this.iPercTot = new ArrayList<>();
      this.iPercMax = new ArrayList<>();
      List<List<Double>> lPerc = new ArrayList<>();
      List<Integer> lNumbers = new ArrayList<>();
      List<Double> lPercC = new ArrayList<>();
      List<Double> lPercN = new ArrayList<>();
      List<Double> lPercH = new ArrayList<>();
      List<Double> lPercO = new ArrayList<>();
      List<Double> lPercO17 = new ArrayList<>();
      List<Double> lPercS34 = new ArrayList<>();
      List<Double> lPercS33 = new ArrayList<>();
      List<Double> lPercS36 = new ArrayList<>();
      if (lC > 0) {
         lNumbers.add(lC);
         lPerc.add(lPercC);
      }

      if (lN > 0) {
         lNumbers.add(lN);
         lPerc.add(lPercN);
      }

      if (lH > 0) {
         lNumbers.add(lH);
         lPerc.add(lPercH);
      }

      if (lO > 0) {
         lNumbers.add(lO);
         lNumbers.add(lO);
         lPerc.add(lPercO);
         lPerc.add(lPercO17);
      }

      if (lS > 0) {
         lNumbers.add(lS);
         lNumbers.add(lS);
         lNumbers.add(lS);
         lPerc.add(lPercS34);
         lPerc.add(lPercS33);
         lPerc.add(lPercS36);
      }

      BinomialDistributionImpl lBinomC = new BinomialDistributionImpl(lC, 0.01107D);
      BinomialDistributionImpl lBinomH = new BinomialDistributionImpl(lH, 1.5E-4D);
      BinomialDistributionImpl lBinomN = new BinomialDistributionImpl(lN, 0.003663D);
      BinomialDistributionImpl lBinomO = new BinomialDistributionImpl(lO, 0.002036D);
      BinomialDistributionImpl lBinomO17 = new BinomialDistributionImpl(lO, 3.74E-4D);
      BinomialDistributionImpl lBinomS34 = new BinomialDistributionImpl(lS, 0.0421D);
      BinomialDistributionImpl lBinomS33 = new BinomialDistributionImpl(lS, 0.0075D);
      BinomialDistributionImpl lBinomS36 = new BinomialDistributionImpl(lS, 2.0E-4D);

      for(int i = 0; i < 10; ++i) {
         lPercC.add(lBinomC.probability(i) * (double)lC);
         lPercN.add(lBinomN.probability(i) * (double)lN);
         lPercH.add(lBinomH.probability(i) * (double)lH);
         if (i % 2 == 0) {
            lPercO.add(lBinomO.probability(i / 2) * (double)lO);
            lPercS34.add(lBinomS34.probability(i / 2) * (double)lS);
         } else {
            lPercO.add(0.0D);
            lPercS34.add(0.0D);
         }

         if (i % 4 == 0) {
            lPercS36.add(lBinomS36.probability(i / 4) * (double)lS);
         } else {
            lPercS36.add(0.0D);
         }

         lPercO17.add(lBinomO17.probability(i) * (double)lO);
         lPercS33.add(lBinomS33.probability(i) * (double)lS);
      }

      List<Double> lPercTotal = new ArrayList<>();
      int k;
      for(int i = 0; i < lPerc.size(); ++i) {
         if (i == 0) {
            for(int j = 0; j < lPerc.get(i).size(); ++j) {
               lPercTotal.add((lPerc.get(i)).get(j));
            }
         } else if ((Integer)lNumbers.get(i) != 0) {
            Vector<Double> lTempTotal = new Vector();

            for(k = 1; k <= lPercTotal.size(); ++k) {
               double lTempValue = 0.0D;

               for(int l = 0; l < k; ++l) {
                  if (lPercTotal.get(l) != 0.0D && (Double)((Vector)lPerc.get(i)).get(k - l - 1) != 0.0D) {
                     lTempValue += lPercTotal.get(l) * (lPerc.get(i)).get(k - l - 1);
                  }
               }

               lTempTotal.add(lTempValue);
            }

            lPercTotal = lTempTotal;
         }
      }

      double lMax = 0.0D;

      for(k = 0; k < lPercTotal.size(); ++k) {
         for(int e = 0; e < lNumbers.size(); ++e) {
            lPercTotal.set(k, lPercTotal.get(k) / (double) lNumbers.get(e));
         }

         if (lPercTotal.get(k) > lMax) {
            lMax = lPercTotal.get(k);
         }
      }

      this.iPercTot = lPercTotal;

      for(k = 0; k < this.iPercTot.size(); ++k) {
         this.iPercMax.add(this.iPercTot.get(k) / lMax);
      }

   }

   public IsotopeCalculator(ModifiedSequenceElement lComposition) {
      this(lComposition.getC(), lComposition.getN(), lComposition.getH(), lComposition.getO(), lComposition.getS());
   }

   public Double[] getPercMax() {
      Double[] lReturn = new Double[this.iPercMax.size()];
      this.iPercMax.toArray(lReturn);
      return lReturn;
   }

   public Double[] getPercTot() {
      Double[] lReturn = new Double[this.iPercTot.size()];
      this.iPercTot.toArray(lReturn);
      return lReturn;
   }

   public static void main(String[] args) {
      new IsotopeCalculator(60, 13, 86, 13, 2);
   }
}
