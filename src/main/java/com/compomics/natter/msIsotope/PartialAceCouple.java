package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class PartialAceCouple {
   private static Logger logger = Logger.getLogger(PartialAceCouple.class);
   private MsIsotopeResult ace;
   private MsIsotopeResult acD3;
   private double iAceIntM = 0.0D;
   private double iAcD3IntM = 0.0D;
   private double AceIntTot = 0.0D;
   private double AcD3IntTot = 0.0D;
   private double IntAceP3 = 0.0D;
   private double IntAcD3P0 = 0.0D;
   private double intTot = 0.0D;
   private double percAce = 0.0D;
   private double percAcD3 = 0.0D;

   public PartialAceCouple(MsIsotopeResult ace, MsIsotopeResult acD3, double iAceIntM, double iAcD3IntM) {
      this.ace = ace;
      this.acD3 = acD3;
      this.iAceIntM = iAceIntM;
      this.iAcD3IntM = iAcD3IntM;
   }

   public void setAceIntTot(double aceIntTot) {
      this.AceIntTot = aceIntTot;
   }

   public void setAcD3IntTot(double acD3IntTot) {
      this.AcD3IntTot = acD3IntTot;
   }

   public void setIntAceP3(double intAceP3) {
      this.IntAceP3 = intAceP3;
   }

   public void setIntAcD3P0(double intAcD3P0) {
      this.IntAcD3P0 = intAcD3P0;
   }

   public void setIntTot(double intTot) {
      this.intTot = intTot;
   }

   public void setPercAce(double percAce) {
      this.percAce = percAce;
   }

   public void setPercAcD3(double percAcD3) {
      this.percAcD3 = percAcD3;
   }

   public double getIAceIntM() {
      return this.iAceIntM;
   }

   public double getIAcD3IntM() {
      return this.iAcD3IntM;
   }

   public MsIsotopeResult getAce() {
      return this.ace;
   }

   public MsIsotopeResult getAcD3() {
      return this.acD3;
   }

   public double getAceIntTot() {
      return this.AceIntTot;
   }

   public double getAcD3IntTot() {
      return this.AcD3IntTot;
   }

   public double getIntAceP3() {
      return this.IntAceP3;
   }

   public double getIntAcD3P0() {
      return this.IntAcD3P0;
   }

   public double getIntTot() {
      return this.intTot;
   }

   public double getPercAce() {
      return this.percAce;
   }

   public double getPercAcD3() {
      return this.percAcD3;
   }
}
