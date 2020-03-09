package com.compomics.natter.people.petra.nat;

import com.compomics.rover.general.quantitation.RatioGroup;
import java.util.Vector;
import org.apache.log4j.Logger;

public class MultiRatioGroup {
   private static Logger logger = Logger.getLogger(MultiRatioGroup.class);
   private String iSpectrumFileName;
   private Vector<RatioGroup> iRatioGroups = new Vector();
   private Vector<Integer> iFolderNumbers = new Vector();

   public MultiRatioGroup(String aSpectrumFileName) {
      this.iSpectrumFileName = aSpectrumFileName;
   }

   public void addRatioGroup(RatioGroup aRatioGroup, int aFolderNumber) {
      this.iRatioGroups.add(aRatioGroup);
      this.iFolderNumbers.add(aFolderNumber);
   }

   public Vector<RatioGroup> getRatioGroups() {
      return this.iRatioGroups;
   }

   public RatioGroup getRatioGroupForFolder(int aFolderNumber) {
      for(int i = 0; i < this.iFolderNumbers.size(); ++i) {
         if ((Integer)this.iFolderNumbers.get(i) == aFolderNumber) {
            return (RatioGroup)this.iRatioGroups.get(i);
         }
      }

      return null;
   }

   public String getFileName() {
      return this.iSpectrumFileName;
   }
}
