package com.compomics.natter.people.petra.nat;

import com.compomics.mascotdatfile.util.mascot.MascotDatfile_Index;
import com.compomics.rover.general.fileio.files.DatFile;
import com.compomics.rover.general.fileio.readers.QuantitationXmlReader;
import com.compomics.rover.general.interfaces.PeptideIdentification;
import com.compomics.rover.general.quantitation.RatioGroupCollection;
import com.compomics.rover.general.quantitation.source.distiller.DistillerPeptide;
import com.compomics.rover.general.quantitation.source.distiller.DistillerRatioGroup;
import com.compomics.util.enumeration.CompomicsTools;
import com.compomics.util.interfaces.Flamable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.compomics.util.io.PropertiesManager;
import java.util.logging.Level;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

import javax.swing.*;

public class RovFile {

    private static Logger logger = Logger.getLogger(RovFile.class);
    private File iOriginalRovFile;
    private Flamable iFlamable;
    private File iQuantitationXmlFile;
    private DatFile iMascotIdentificationFile;
    private MascotDatfile_Index iMascotDatFile;
    private RatioGroupCollection iRatioGroupCollection;
    private double iThreshold = 0.05D;
    private String iFilePath;

    public RovFile(File aOriginalRovFile, Flamable lFlamable) {
        this.iOriginalRovFile = aOriginalRovFile;
        this.iFilePath = this.iOriginalRovFile.getAbsolutePath();
        this.iFlamable = lFlamable;
    }

    public boolean unzipRovFile(String aSubFolder) {
        boolean lNeededFilesFound = false;

        try {
            File lTempfolder = File.createTempFile("temp", "temp").getParentFile();
            File lTempRovFolder = new File(lTempfolder, "natter");
            if (!lTempRovFolder.exists()) {
                lTempRovFolder.mkdir();
            }

            File lSubFolder = null;
            if (aSubFolder != null) {
                lSubFolder = new File(lTempRovFolder, aSubFolder);
                if (!lSubFolder.exists()) {
                    lSubFolder.mkdir();
                }

                lSubFolder.deleteOnExit();
            }

            if (!lTempRovFolder.exists()) {
                lTempRovFolder.mkdir();
            }

            File lTempUnzippedRovFileFolder;
            if (lSubFolder == null) {
                lTempUnzippedRovFileFolder = new File(lTempRovFolder, this.iOriginalRovFile.getName());
            } else {
                lTempUnzippedRovFileFolder = new File(lSubFolder, this.iOriginalRovFile.getName());
            }

            lTempUnzippedRovFileFolder.deleteOnExit();
            int count;
            if (!lTempUnzippedRovFileFolder.exists()) {
                if (!lTempUnzippedRovFileFolder.mkdir()) {
                    this.iFlamable.passHotPotato(new Throwable("Unable to create temporary directory ' " + lTempUnzippedRovFileFolder.getName() + "' for distiller rov project '" + this.iOriginalRovFile.getName() + "'!!"));
                    return false;
                }

                BufferedOutputStream out = null;
                ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(this.iOriginalRovFile)));

                while (true) {
                    ZipEntry entry;
                    if ((entry = in.getNextEntry()) == null) {
                        in.close();
                        break;
                    }

                    byte[] data = new byte[1000];
                    out = new BufferedOutputStream(new FileOutputStream(lTempUnzippedRovFileFolder.getPath() + "/" + entry.getName()), 1000);

                    while ((count = in.read(data, 0, 1000)) != -1) {
                        out.write(data, 0, count);
                    }

                    out.flush();
                    out.close();
                }
            }

            File[] lUnzippedRovFiles = lTempUnzippedRovFileFolder.listFiles();
            boolean lQuantFound = false;
            boolean lDatFileFound = false;

            for (count = 0; count < lUnzippedRovFiles.length; ++count) {
                File lUnzippedRovFile = lUnzippedRovFiles[count];
                if (lUnzippedRovFile.getName().toLowerCase().indexOf("rover_data+bb8") != -1) {
                    lQuantFound = true;
                    try {
                        LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(lUnzippedRovFile)));
                        if (lnr.readLine().contains("xml version")) {
                            //no problem
                            this.iQuantitationXmlFile = lUnzippedRovFile;
                        } else {
                            String convertedFileName = "rover_data+bb8_edited";
                            this.editRovFile(lTempUnzippedRovFileFolder, convertedFileName);
                            this.iQuantitationXmlFile = new File(lTempUnzippedRovFileFolder, convertedFileName);
                        }
                    } catch (IOException ioe) {
                        System.out.println("--------------");
                    }
                }

                if (lUnzippedRovFile.getName().toLowerCase().indexOf("rover_data+bb9") != -1) {
                    lQuantFound = true;
                    try {
                        LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(lUnzippedRovFile)));
                        if (lnr.readLine().contains("xml version")) {
                            //no problem
                            this.iQuantitationXmlFile = lUnzippedRovFile;
                        } else {
                            String convertedFileName = "rover_data+bb9_edited";
                            this.editRovFile(lTempUnzippedRovFileFolder, convertedFileName);
                            this.iQuantitationXmlFile = new File(lTempUnzippedRovFileFolder, convertedFileName);
                        }

                    } catch (IOException ioe) {
                        System.out.println("--------------");
                    }
                }

                if (lUnzippedRovFile.getName().toLowerCase().indexOf("mdro_search_status+1") != -1) {
                    lDatFileFound = true;
                    this.iMascotIdentificationFile = new DatFile(lUnzippedRovFile, (Flamable) null);
                }
            }

            if (lQuantFound && lDatFileFound) {
                lNeededFilesFound = true;
            }
        } catch (IOException var12) {
            System.err.println("Failing!");
            var12.printStackTrace();
        }

        return lNeededFilesFound;
    }

    public void readQuantitationXmlFile() {
        QuantitationXmlReader lReader = new QuantitationXmlReader(this.iQuantitationXmlFile, this.iFlamable, this.iOriginalRovFile.getName());
        this.iRatioGroupCollection = lReader.getRatioGroupCollection();
    }

    public void match() {
        Vector<PeptideIdentification> lIds = new Vector();
        Collection<PeptideIdentification> peptideIdentificationCollection = this.iMascotIdentificationFile.extractDatfilePeptideIdentification(this.iThreshold).values();
        Iterator iterator = peptideIdentificationCollection.iterator();

        while (iterator.hasNext()) {
            lIds.add((PeptideIdentification) iterator.next());
        }

        for (int i = 0; i < this.iRatioGroupCollection.size(); ++i) {
            DistillerRatioGroup lRatioGroup = (DistillerRatioGroup) this.iRatioGroupCollection.get(i);
            Vector<DistillerPeptide> lDistillerPeptides = lRatioGroup.getParentHit().getDistillerPeptides();

            for (int j = 0; j < lIds.size(); ++j) {
                for (int p = 0; p < lDistillerPeptides.size(); ++p) {
                    for (int k = 0; k < lRatioGroup.getDatfileQueries().length; ++k) {
                        if (((DistillerPeptide) lDistillerPeptides.get(p)).getQuery() == lRatioGroup.getDatfileQueries()[k] && (long) ((DistillerPeptide) lDistillerPeptides.get(p)).getQuery() == ((PeptideIdentification) lIds.get(j)).getDatfile_query() && lRatioGroup.getPeptideSequence().equalsIgnoreCase(((PeptideIdentification) lIds.get(j)).getSequence())) {
                            lRatioGroup.addIdentification((PeptideIdentification) lIds.get(j), ((DistillerPeptide) lDistillerPeptides.get(p)).getComposition());
                        }
                    }
                }
            }
        }

    }

    public String getRovFilePath() {
        return this.iFilePath;
    }

    public RatioGroupCollection getRatioGroupCollection() {
        return this.iRatioGroupCollection;
    }

    public String toString() {
        return this.iOriginalRovFile.getName();
    }

    public void editRovFile(File lTempRovFolder, String convertedFileName) {
        try {
            String distillerLocation = mcdChecker();

            //ProcessBuilder processBuilder = new ProcessBuilder("touch", tempFolder.getAbsolutePath() + File.separator + fileName);
            //ProcessBuilder processBuilder = new ProcessBuilder(distillerLocation, "-i", "C:\\Users\\compomics\\Desktop\\Niels\\rawfiles\\raw\\small.RAW");
            ProcessBuilder processBuilder = new ProcessBuilder(distillerLocation, iOriginalRovFile.getAbsolutePath(), "-batch", "-saveQuantXml", "quantout", lTempRovFolder.getAbsolutePath() + File.separator + convertedFileName);

            processBuilder.redirectErrorStream(true);
            processBuilder.redirectOutput();

            // Start the process.
            Process process = processBuilder.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            //Runtime rt = Runtime.getRuntime();
            //Process proc = rt.exec("\"" + distillerlocation + "\" \"" + iOriginalRovFile.getAbsolutePath() + "\" -batch -saveQuantXml -quantout \"" + lTempRovFolder.getParent() + "\\rover_data+bb8_edited\"");
            //Process proc = rt.exec("touch \"" + lTempRovFolder.getParent() + File.separator + "rover_data+bb8_edited\"");

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RovFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(RovFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String mcdChecker() {
        Path distillerPath = Paths.get(ConfigHolder.getInstance().getString("distiller_location"));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new mcdFileFilter());
        if (!Files.exists(distillerPath)) {
            JOptionPane.showMessageDialog(null, "The location of Mascot Distiller does not seem to exist anymore, please select the new location of Mascot Distiller");
            int returnVal = fileChooser.showOpenDialog(new JFrame());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                if (fileChooser.getSelectedFile().exists()) {
                    ConfigHolder.getInstance().setProperty("distiller_location", fileChooser.getSelectedFile().getAbsolutePath());
                    try {
                        ConfigHolder.getInstance().save();
                    } catch (ConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return ConfigHolder.getInstance().getString("distiller_location");
    }

    private class mcdFileFilter extends javax.swing.filechooser.FileFilter {

        @Override
        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".exe");
        }

        @Override
        public String getDescription() {
            return ".exe files";
        }
    }

}
