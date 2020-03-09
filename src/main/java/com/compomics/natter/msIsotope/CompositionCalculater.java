package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class CompositionCalculater {
   private static Logger logger = Logger.getLogger(CompositionCalculater.class);
   private Element[] iElements;
   private MsIsotopeResult[] iSequences;

   public CompositionCalculater(Element[] aElements, MsIsotopeResult[] aSequences) {
      this.iElements = aElements;
      this.iSequences = aSequences;
      this.calculate();
   }

   private void calculate() {
      for(int i = 0; i < this.iSequences.length; ++i) {
         MsIsotopeResult res = this.iSequences[i];
         String[] seq = res.getISequence();
         int H = 0;
         int C = 0;
         int N = 0;
         int O = 0;
         int S = 0;
         int HDeut = 0;
         int C13 = 0;
         int N15 = 0;
         int O18 = 0;

         for(int k = 0; k < seq.length; ++k) {
            boolean foundElement = false;

            for(int j = 0; j < this.iElements.length; ++j) {
               Element ele = this.iElements[j];
               String eleName = ele.getIName();
               if (seq[k].equalsIgnoreCase(eleName)) {
                  H += ele.getH();
                  C += ele.getC();
                  N += ele.getN();
                  O += ele.getO();
                  S += ele.getS();
                  HDeut += ele.getHDeut();
                  C13 += ele.getC13();
                  N15 += ele.getN15();
                  O18 += ele.getO18();
                  foundElement = true;
               }
            }

            if (!foundElement) {
            }
         }

         res.setH(H);
         res.setC(C);
         res.setN(N);
         res.setO(O);
         res.setS(S);
         res.setHDeut(HDeut);
         res.setC13(C13);
         res.setN15(N15);
         res.setO18(O18);
         this.iSequences[i] = res;
      }

   }

   public MsIsotopeResult[] getISequences() {
      return this.iSequences;
   }
}
