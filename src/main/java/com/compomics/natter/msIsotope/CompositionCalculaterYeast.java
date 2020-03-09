package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class CompositionCalculaterYeast {
   private static Logger logger = Logger.getLogger(CompositionCalculaterYeast.class);
   private Element[] iElements;
   private YeastNatElement iYeastElement;

   public CompositionCalculaterYeast(Element[] aElements, YeastNatElement aYeastElement) {
      this.iElements = aElements;
      this.iYeastElement = aYeastElement;
      this.calculate();
   }

   public void calculate() {
      String seq = this.iYeastElement.getSequence();
      String[] aas = this.iYeastElement.getSequenceSplit();
      int H = 0;
      int C = 0;
      int N = 0;
      int O = 0;
      int S = 0;
      int HDeut = 0;
      int C13 = 0;
      int N15 = 0;
      int O18 = 0;

      int k;
      for(k = 0; k < seq.length(); ++k) {
         boolean foundElement = false;

         for(int j = 0; j < this.iElements.length; ++j) {
            Element ele = this.iElements[j];
            String eleName = ele.getIName();
            if (String.valueOf(seq.charAt(k)).equalsIgnoreCase(eleName)) {
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

      for(k = 0; k < aas.length; ++k) {
         if (aas[k].indexOf("Dam") >= 0) {
            --H;
            --N;
            ++O;
         }
      }

      this.iYeastElement.setH(H);
      this.iYeastElement.setC(C);
      this.iYeastElement.setN(N);
      this.iYeastElement.setO(O);
      this.iYeastElement.setS(S);
      this.iYeastElement.setHDeut(HDeut);
      this.iYeastElement.setC13(C13);
      this.iYeastElement.setN15(N15);
      this.iYeastElement.setO18(O18);
   }
}
