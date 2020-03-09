package com.compomics.natter.msIsotope;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.compomics.natter.people.petra.nat.ResourceUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public class ReadElementFile {
    private static Logger logger = Logger.getLogger(ReadElementFile.class);
    private List lines = new ArrayList<>();
    private List elements = new ArrayList();

    public ReadElementFile() {
        String line;
        try {
//         line = "" + this.getClass().getProtectionDomain().getCodeSource().getLocation();
//         line = line.substring(5, line.lastIndexOf("/"));
//         if (line.endsWith("/lib")) {
//            line = line.substring(0, line.length() - 4);
//         }
//
//         line = line + "/resources/elementMsIsotope.txt";
//         line = line.replace("%20", " ");
//         File file = new File(line);
//         if (!file.exists()) {
//            file = new File(ClassLoader.getSystemResource("elementMsIsotope.txt").toString().substring(6));
//         }
            Resource elementsResource = ResourceUtils.getResourceByRelativePath("elementMsIsotope.txt");

            FileReader freader = new FileReader(elementsResource.getFile());
            LineNumberReader lnreader = new LineNumberReader(freader);

            while ((line = lnreader.readLine()) != null) {
                this.lines.add(line);
            }
        } catch (FileNotFoundException var15) {
            var15.printStackTrace();
        } catch (IOException var16) {
            var16.printStackTrace();
        }

        for (int i = 0; i < this.lines.size(); ++i) {
            line = (String) this.lines.get(i);
            if (!line.startsWith("//")) {
                String ele = line.substring(0, line.indexOf("=")).trim();
                Element element = new Element(ele);
                line = line.substring(line.indexOf("=") + 1);
                int H = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int C = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int N = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int O = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int S = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int HDeut = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int C13 = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int N15 = Integer.valueOf(line.substring(0, line.indexOf(",")));
                line = line.substring(line.indexOf(",") + 1);
                int O18 = Integer.valueOf(line);
                element.setH(H);
                element.setC(C);
                element.setN(N);
                element.setO(O);
                element.setS(S);
                element.setHDeut(HDeut);
                element.setC13(C13);
                element.setN15(N15);
                element.setO18(O18);
                this.elements.add(element);
            }
        }

    }

    public Element[] getElements() {
        Element[] eles = new Element[this.elements.size()];
        this.elements.toArray(eles);
        return eles;
    }
}
