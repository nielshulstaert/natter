package com.compomics.natter.msIsotope;

import org.apache.log4j.Logger;

public class ExcelColumn {
   private static Logger logger = Logger.getLogger(ExcelColumn.class);
   private String title;
   private int column;

   public ExcelColumn(String title, int column) {
      this.title = title;
      this.column = column;
   }

   public String getTitle() {
      return this.title;
   }

   public int getColumn() {
      return this.column;
   }

   public String toString() {
      return this.title;
   }
}
