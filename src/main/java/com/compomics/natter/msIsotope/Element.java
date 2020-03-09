package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class Element {
   private static Logger logger = Logger.getLogger(Element.class);
   private String iName;
   int H = 0;
   int C = 0;
   int N = 0;
   int O = 0;
   int S = 0;
   int HDeut = 0;
   int C13 = 0;
   int N15 = 0;
   int O18 = 0;

   public Element(String aName) {
      this.iName = aName;
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

   public String getIName() {
      return this.iName;
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
