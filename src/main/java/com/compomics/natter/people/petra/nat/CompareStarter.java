package com.compomics.natter.people.petra.nat;

import com.compomics.natter.msIsotope.Element;
import com.compomics.natter.msIsotope.IsotopeCalculator;
import com.compomics.natter.msIsotope.ReadElementFile;
import com.compomics.rover.general.interfaces.PeptideIdentification;
import com.compomics.rover.general.quantitation.RatioGroup;
import com.compomics.rover.general.quantitation.RatioGroupCollection;
import com.compomics.rover.general.quantitation.source.distiller.DistillerRatio;
import com.compomics.rover.general.quantitation.source.distiller.DistillerRatioGroup;
import com.compomics.util.interfaces.Flamable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;

public class CompareStarter implements Flamable {
   private static Logger logger = Logger.getLogger(CompareStarter.class);
   private String[] iFolderToFindFiles;
   private Flamable iFlamable;
   private String iSaveLocation;
   private Element[] iElements;
   private HashMap iFormulas;
   private JTextArea iTextArea;

   public CompareStarter(String[] aFoldersToFindFiles, String aSaveLocation, HashMap aFormulas, String aTitle, JTextArea aTextArea) {
      try {
         this.iFlamable = this;
         this.iTextArea = aTextArea;
         this.iFormulas = aFormulas;
         ReadElementFile lElementsFile = new ReadElementFile();
         this.iElements = lElementsFile.getElements();
         this.iSaveLocation = aSaveLocation;
         this.iFlamable = this;
         this.iFolderToFindFiles = aFoldersToFindFiles;
         File[] lFolders = new File[this.iFolderToFindFiles.length];

         for(int i = 0; i < this.iFolderToFindFiles.length; ++i) {
            lFolders[i] = new File(this.iFolderToFindFiles[i]);
         }

         File lFirstFolder = lFolders[0];
         File[] lRovFiles = lFirstFolder.listFiles();
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(this.iSaveLocation, "multiRovOutput" + aTitle + ".csv"))));
         String sep = ";";

         for(int i = 0; i < lRovFiles.length; ++i) {
            System.out.println(i + 1 + " of " + lRovFiles.length);
            if (this.iTextArea != null) {
               this.iTextArea.append("\n" + (i + 1) + " of " + lRovFiles.length + "\n");
            }

            int lCounter = 0;
            if (lRovFiles[i].getAbsolutePath().endsWith(".rov")) {
               File[] lRovFileGroup = new File[lFolders.length];
               String lRofFileTitle = lRovFiles[i].getName();
               if (lRofFileTitle.contains(".RAW")) {
                  lRofFileTitle = lRofFileTitle.substring(0, lRofFileTitle.lastIndexOf(".RAW"));
               } else {
                  lRofFileTitle = lRofFileTitle.substring(0, lRofFileTitle.lastIndexOf(".raw"));
               }

               for(int j = 0; j < lFolders.length; ++j) {
                  File[] lTempRovFiles = lFolders[j].listFiles();

                  for(int k = 0; k < lTempRovFiles.length; ++k) {
                     String lTempTitle = lTempRovFiles[k].getName();
                     if (lTempTitle.endsWith(".rov")) {
                        if (lTempTitle.contains(".RAW")) {
                           lTempTitle = lTempTitle.substring(0, lTempTitle.lastIndexOf(".RAW"));
                        } else {
                           lTempTitle = lTempTitle.substring(0, lTempTitle.lastIndexOf(".raw"));
                        }

                        if (lTempTitle.equalsIgnoreCase(lRofFileTitle)) {
                           lRovFileGroup[j] = lTempRovFiles[k];
                        }
                     }
                  }
               }

               RatioGroupCollection[] lCollections = new RatioGroupCollection[lRovFileGroup.length];
               boolean lDifferentRovFilesOk = true;

               for(int k = 0; k < lRovFileGroup.length; ++k) {
                  if (lRovFileGroup[k] != null) {
                     RovFile lRovFile = new RovFile(lRovFileGroup[k], this.iFlamable);
                     boolean allOk = lRovFile.unzipRovFile(String.valueOf(k));
                     if (allOk) {
                        lRovFile.readQuantitationXmlFile();
                        lRovFile.match();
                        lCollections[k] = lRovFile.getRatioGroupCollection();
                     } else {
                        lDifferentRovFilesOk = false;
                        System.out.println("Problem with the rov file : '" + lRovFileGroup[k].getAbsolutePath() + "'!");
                        if (this.iTextArea != null) {
                           this.iTextArea.append("Problem with the rov file : '" + lRovFileGroup[k].getAbsolutePath() + "'!\n");
                        }
                     }
                  }
               }

               if (!lDifferentRovFilesOk) {
                  System.out.println("No data will be available for :  " + lRofFileTitle);
                  if (this.iTextArea != null) {
                     this.iTextArea.append("No data will be available for :  " + lRofFileTitle + "\n");
                  }
               } else {
                  HashMap<String, MultiRatioGroup> lMultiRatioGroupMap = new HashMap();

                  int e;
                  String lFileName;
                  for(int c = 0; c < lCollections.length; ++c) {
                     if (lCollections[c] != null) {
                        for(int d = 0; d < lCollections[c].size(); ++d) {
                           RatioGroup lRatioGroup = lCollections[c].get(d);

                           for(e = 0; e < lRatioGroup.getNumberOfIdentifications(); ++e) {
                              PeptideIdentification lIds = lRatioGroup.getIdentification(e);
                              lFileName = lIds.getSpectrumFileName();
                              MultiRatioGroup lMulti;
                              if (lMultiRatioGroupMap.containsKey(lFileName)) {
                                 lMulti = (MultiRatioGroup)lMultiRatioGroupMap.get(lFileName);
                                 lMulti.addRatioGroup(lRatioGroup, c);
                              } else {
                                 lMulti = new MultiRatioGroup(lFileName);
                                 lMulti.addRatioGroup(lRatioGroup, c);
                                 lMultiRatioGroupMap.put(lFileName, lMulti);
                              }
                           }
                        }
                     }
                  }

                  Collection<MultiRatioGroup> lMultiRatioGroups = lMultiRatioGroupMap.values();
                  bw.write("filename" + sep + "idType" + sep + "protein accession" + sep + "description" + sep + "sequence" + sep + "precursor mass" + sep + "charge" + sep + "start" + sep + "end");
                  List<List<String>> lComponents = new ArrayList<List<String>>();
                  List<List<String>> lTypes = new ArrayList<List<String>>();

                  for(e = 0; e < lCollections.length; ++e) {
                     if (lCollections[e] != null) {
                        RatioGroupCollection lCol = lCollections[e];
                        List<String> lComps = lCol.getComponentTypes();
                        lComponents.add(lComps);
                        Vector<String> lTys = lCol.getRatioTypes();
                        lTypes.add(lTys);
                        bw.write(sep + "mod_seq" + sep + "identified_type");

                        int o;
                        for(o = 0; o < lComps.size(); ++o) {
                           bw.write(sep + (String)lComps.get(o));
                        }

                        if (lComps.size() == 2) {
                           for(o = 0; o < lComps.size(); ++o) {
                              bw.write(sep + (String)lComps.get(o) + " norm");
                           }
                        }

                        bw.write(sep + "Fraction");
                        bw.write(sep + "Correlation");

                        for(o = 0; o < lTys.size(); ++o) {
                           bw.write(sep + (String)lTys.get(o));
                           bw.write(sep + (String)lTys.get(o) + " valid");
                           bw.write(sep + (String)lTys.get(o) + " quality");
                        }
                     }
                  }

                  bw.write("\n");
                  Iterator i$ = lMultiRatioGroups.iterator();

                  while(i$.hasNext()) {
                     MultiRatioGroup lMulti = (MultiRatioGroup)i$.next();
                     lFileName = lMulti.getFileName();
                     String lResultFileName;
                     if (lFileName.contains(".RAW")) {
                        lResultFileName = lFileName.substring(lFileName.lastIndexOf("\\") + 1, lFileName.lastIndexOf(".RAW"));
                     } else {
                        lResultFileName = lFileName.substring(lFileName.lastIndexOf("\\") + 1, lFileName.lastIndexOf(".raw"));
                     }

                     lResultFileName = lResultFileName + "_" + lFileName.substring(0, lFileName.indexOf(":")) + "_";
                     String lSum = "1";
                     if (lFileName.contains("Sum of ")) {
                        lSum = lFileName.substring(lFileName.indexOf("Sum of ") + 7, lFileName.lastIndexOf(" scans "));
                        Integer lBeginScan = Integer.valueOf(lFileName.substring(lFileName.indexOf("range ") + 6, lFileName.indexOf(" (rt=")));
                        Integer lEndScan = Integer.valueOf(lFileName.substring(lFileName.indexOf(") to ") + 5, lFileName.lastIndexOf(" (rt=")));
                        lResultFileName = lResultFileName + lBeginScan + "." + lEndScan + "_" + lSum + "_";
                     } else {
                        lResultFileName = lResultFileName + lFileName.substring(lFileName.indexOf("Scan") + 5, lFileName.indexOf(" ", lFileName.indexOf("Scan") + 5)) + "_1_";
                     }

                     bw.write(lResultFileName);
                     boolean lIdentificationInfoPrinted = false;

                     int c;
                     for(c = 0; c < lCollections.length; ++c) {
                        if (lCollections[c] != null && !lIdentificationInfoPrinted && lMulti.getRatioGroupForFolder(c) != null) {
                           PeptideIdentification lid = lMulti.getRatioGroupForFolder(c).getIdentification(0);
                           String lDes = lid.getDescription();
                           lDes = lDes.replace(sep, "_");
                           bw.write(sep + String.valueOf(c + 1) + sep + lid.getAccession() + sep + lDes + sep + lid.getSequence() + sep + lid.getPrecursor() + sep + lid.getCharge() + sep + lid.getStart() + sep + lid.getEnd());
                           lIdentificationInfoPrinted = true;
                        }
                     }

                     for(c = 0; c < lCollections.length; ++c) {
                        if (lCollections[c] != null) {
                           if (lMulti.getRatioGroupForFolder(c) != null) {
                              DistillerRatioGroup lRatioGroup = (DistillerRatioGroup)lMulti.getRatioGroupForFolder(c);
                              bw.write(sep + lRatioGroup.getIdentification(0).getModified_sequence() + sep + lRatioGroup.getIdentification(0).getType());
                              Double[] intensities = lRatioGroup.getAbsoluteIntensities();
                              if (intensities.length == 2) {
                                 PeptideIdentification lIdentification = lRatioGroup.getIdentification(0);
                                 String lType = lIdentification.getType();
                                 int lComponentNumber = 0;

                                 for(int k = 0; k < lCollections[c].getComponentTypes().size(); ++k) {
                                    if (lType.equalsIgnoreCase((String)lCollections[c].getComponentTypes().get(k))) {
                                       lComponentNumber = k;
                                    }
                                 }

                                 ModifiedSequenceElement[] lCompositions = this.calculateCompositions(lIdentification.getModified_sequence(), lComponentNumber, lCollections[c].getComponentTypes().size(), this.iFormulas);
                                 IsotopeCalculator[] lIsotopeCalculator = new IsotopeCalculator[lCompositions.length];

                                 for(int p = 0; p < lCompositions.length; ++p) {
                                    lIsotopeCalculator[p] = new IsotopeCalculator(lCompositions[p]);
                                    ++lCounter;
                                    if (lCounter % 4 == 0) {
                                    }

                                    if (lCounter % 1000 == 0) {
                                    }
                                 }

                                 ArrayList<Double> lNormInt = new ArrayList(Arrays.asList(intensities));
                                 if (lNormInt.size() == 2) {
                                    for(int p = 1; p < lNormInt.size(); ++p) {
                                       double intensity1 = intensities[p];
                                       double intensity2 = intensities[p - 1];
                                       double pearson = lIsotopeCalculator[p - 1].getPercMax()[5];
                                       lNormInt.set(p, intensity1 - intensity2 * pearson / 100.0D);
                                       if ((Double)lNormInt.get(p) < 0.0D) {
                                          lNormInt.set(p, Double.valueOf("0"));
                                       }
                                    }
                                 } else {
                                    lNormInt.set(1, intensities[1] - (Double)lNormInt.get(0) * lIsotopeCalculator[0].getPercMax()[5] / 100.0D);
                                    if ((Double)lNormInt.get(1) < 0.0D) {
                                       lNormInt.set(1, Double.valueOf("0"));
                                    }
                                 }

                                 Vector<Double> lTempInt = new Vector();

                                 int j;
                                 for(j = 0; j < lRatioGroup.getAbsoluteIntensities().length; ++j) {
                                    lTempInt.add(lRatioGroup.getAbsoluteIntensities()[j]);
                                 }

                                 for(j = 0; j < lNormInt.size(); ++j) {
                                    lTempInt.add(lNormInt.get(j));
                                 }

                                 Double[] lAllInt = new Double[lTempInt.size()];
                                 lTempInt.toArray(lAllInt);
                                 lRatioGroup.setRatioGroupAbsoluteIntensities(lAllInt);
                              }

                              int j;
                              for(j = 0; j < lRatioGroup.getAbsoluteIntensities().length; ++j) {
                                 bw.write(sep + lRatioGroup.getAbsoluteIntensities()[j]);
                              }

                              bw.write(sep + lRatioGroup.getFraction());
                              bw.write(sep + lRatioGroup.getCorrelation());

                              for(j = 0; j < ((Vector)lTypes.get(c)).size(); ++j) {
                                 bw.write(sep + lRatioGroup.getRatioByType((String)((Vector)lTypes.get(c)).get(j)).getRatio(false));
                                 bw.write(sep + lRatioGroup.getRatioByType((String)((Vector)lTypes.get(c)).get(j)).getValid());
                                 bw.write(sep + ((DistillerRatio)lRatioGroup.getRatioByType((String)((Vector)lTypes.get(c)).get(j))).getQuality());
                              }
                           } else {
                              int j;
                              for(j = 0; j < ((Vector)lComponents.get(c)).size(); ++j) {
                                 bw.write(sep + " ");
                              }

                              if (lCollections[c].getComponentTypes().size() == 4) {
                                 for(j = 0; j < ((Vector)lComponents.get(c)).size(); ++j) {
                                    bw.write(sep + " ");
                                 }
                              }

                              for(j = 0; j < ((Vector)lTypes.get(c)).size(); ++j) {
                                 bw.write(sep + " " + sep + " " + sep + " ");
                              }
                           }
                        }
                     }

                     bw.write("\n");
                  }

                  bw.flush();
               }
            }
         }

         bw.flush();
         bw.close();
         System.out.println("Done");
         if (this.iTextArea != null) {
            this.iTextArea.append("Done\n");
         }
      } catch (IOException var44) {
         var44.printStackTrace();
      }

   }

   public void passHotPotato(Throwable throwable) {
      System.out.println(throwable.getMessage());
      if (this.iTextArea != null) {
         this.iTextArea.append(throwable.getMessage() + "\n");
      }

   }

   public void passHotPotato(Throwable throwable, String s) {
      System.out.println(throwable.getMessage());
      if (this.iTextArea != null) {
         this.iTextArea.append(throwable.getMessage() + "\n");
      }

   }

   private ModifiedSequenceElement[] calculateCompositions(String aModifiedSequence, int aSequencePosition, int lNumberOfCompositions, HashMap lFormulas) {
      ModifiedSequenceElement lModSeqEle = new ModifiedSequenceElement(aModifiedSequence, this.iElements, this.iTextArea);
      ModifiedSequenceElement[] lCompositions = new ModifiedSequenceElement[lNumberOfCompositions];

      for(int i = 0; i < lNumberOfCompositions; ++i) {
         ModifiedSequenceElement lTempElements = new ModifiedSequenceElement();
         lTempElements.setC(lModSeqEle.getC());
         lTempElements.setH(lModSeqEle.getH());
         lTempElements.setO(lModSeqEle.getO());
         lTempElements.setS(lModSeqEle.getS());
         lTempElements.setN(lModSeqEle.getN());
         lTempElements.setC13(lModSeqEle.getC13());
         lTempElements.setHDeut(lModSeqEle.getHDeut());
         lTempElements.setO18(lModSeqEle.getO18());
         lTempElements.setN15(lModSeqEle.getN15());
         String lKey = aSequencePosition + "," + i;
         String lFormula = (String)lFormulas.get(lKey);
         int H = Integer.valueOf(lFormula.substring(0, lFormula.indexOf(",")));
         String sub = lFormula.substring(lFormula.indexOf(",") + 1);
         int C = Integer.valueOf(sub.substring(0, sub.indexOf(",")));
         sub = sub.substring(sub.indexOf(",") + 1);
         int N = Integer.valueOf(sub.substring(0, sub.indexOf(",")));
         sub = sub.substring(sub.indexOf(",") + 1);
         int O = Integer.valueOf(sub.substring(0, sub.indexOf(",")));
         sub = sub.substring(sub.indexOf(",") + 1);
         int S = Integer.valueOf(sub.substring(0, sub.indexOf(",")));
         sub = sub.substring(sub.indexOf(",") + 1);
         int HDeut = Integer.valueOf(sub.substring(0, sub.indexOf(",")));
         sub = sub.substring(sub.indexOf(",") + 1);
         int C13 = Integer.valueOf(sub.substring(0, sub.indexOf(",")));
         sub = sub.substring(sub.indexOf(",") + 1);
         int N15 = Integer.valueOf(sub.substring(0, sub.indexOf(",")));
         sub = sub.substring(sub.indexOf(",") + 1);
         int O18 = Integer.valueOf(sub);
         lTempElements.setC(lTempElements.getC() + C);
         lTempElements.setH(lTempElements.getH() + H);
         lTempElements.setO(lTempElements.getO() + O);
         lTempElements.setS(lTempElements.getS() + S);
         lTempElements.setN(lTempElements.getN() + N);
         lTempElements.setC13(lTempElements.getC13() + C13);
         lTempElements.setHDeut(lTempElements.getHDeut() + HDeut);
         lTempElements.setO18(lTempElements.getO18() + O18);
         lTempElements.setN15(lTempElements.getN15() + N15);
         lCompositions[i] = lTempElements;
      }

      return lCompositions;
   }

   public static void main(String[] args) {
      String[] lFolders = new String[]{"C:/users/Davy/Desktop/natter"};
      String lSaveLocation = "C:/users/Davy/Desktop/natter";
      HashMap<String, String> lFormulas = new HashMap();
      lFormulas.put("0,0", "0,0,0,0,0,0,0,0,0");
      lFormulas.put("0,1", "-3,-2,0,0,0,3,2,0,0");
      lFormulas.put("0,2", "0,-6,-4,0,0,0,6,4,0");
      lFormulas.put("0,3", "-3,-8,-4,0,0,3,8,4,0");
      lFormulas.put("1,0", "3,2,0,0,0,-3,-2,0,0");
      lFormulas.put("1,1", "0,0,0,0,0,0,0,0,0");
      lFormulas.put("1,2", "3,-4,-4,0,0,-3,4,4,0");
      lFormulas.put("1,3", "0,-6,-4,0,0,0,6,4,0");
      lFormulas.put("2,0", "0,6,4,0,0,0,-6,-4,0");
      lFormulas.put("2,1", "-3,4,4,0,0,3,-4,-4,0");
      lFormulas.put("2,2", "0,0,0,0,0,0,0,0,0");
      lFormulas.put("2,3", "-3,-2,0,0,0,3,2,0,0");
      lFormulas.put("3,0", "3,8,4,0,0,-3,-8,-4,0");
      lFormulas.put("3,1", "0,6,4,0,0,0,-6,-4,0");
      lFormulas.put("3,2", "3,2,0,0,0,-3,-2,0,0");
      lFormulas.put("3,3", "0,0,0,0,0,0,0,0,0");
      new CompareStarter(lFolders, lSaveLocation, lFormulas, "temp", (JTextArea)null);
   }
}
