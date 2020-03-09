package com.compomics.natter.msIsotope;

import java.util.Vector;
import org.apache.log4j.Logger;

public class SequenceSplitter {
   private static Logger logger = Logger.getLogger(SequenceSplitter.class);
   private String[] sequenceSplit;

   public SequenceSplitter(String sequence) {
      Object[] seq = this.sequenceToArray(sequence);
      this.sequenceSplit = new String[seq.length];

      for(int i = 0; i < seq.length; ++i) {
         this.sequenceSplit[i] = seq[i].toString();
      }

   }

   public Object[] sequenceToArray(String sequence) {
      Vector sequenceVector = new Vector();
      String sub1 = "";
      if (sequence.indexOf("N-term") >= 0) {
         sequenceVector.add(sequence.substring(0, sequence.indexOf("-", sequence.indexOf("-") + 1) + 1));
         sub1 = sequence.substring(sequence.indexOf("-", sequence.indexOf("-") + 1) + 1);
      } else {
         sequenceVector.add(sequence.substring(0, sequence.indexOf("-") + 1));
         sub1 = sequence.substring(sequence.indexOf("-") + 1);
      }

      for(int i = 0; i < sub1.length(); ++i) {
         String eleMod;
         if (sub1.charAt(i + 1) == '<') {
            eleMod = sub1.substring(i, sub1.indexOf(">", i) + 1);
            if (eleMod.indexOf("Pyr") > 0) {
               sequenceVector.add(eleMod);
            } else {
               sequenceVector.add(eleMod.substring(0, eleMod.indexOf("<")));
               sequenceVector.add(eleMod.substring(eleMod.indexOf("<") + 1, eleMod.indexOf(">")));
            }

            i = sub1.indexOf(">", i);
         } else if (sub1.charAt(i + 1) != '-' && sub1.charAt(i) != '-') {
            sequenceVector.add(sub1.charAt(i));
         } else {
            eleMod = sub1.substring(i);
            if (eleMod.substring(0, eleMod.indexOf("-")).length() > 0) {
               sequenceVector.add(eleMod.substring(0, eleMod.indexOf("-")));
            }

            sequenceVector.add(eleMod.substring(eleMod.indexOf("-") + 1));
            i = sub1.length() - 1;
         }
      }

      Object[] sequenceArray = new Object[sequenceVector.size()];
      sequenceVector.toArray(sequenceArray);
      return sequenceArray;
   }

   public String[] getSequenceSplit() {
      return this.sequenceSplit;
   }
}
