package com.compomics.natter.msIsotope;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.log4j.Logger;

public class MsIsotopeYeast {
   private static Logger logger = Logger.getLogger(MsIsotopeYeast.class);
   private YeastNatElement iSeqToFindIsotopes;

   public MsIsotopeYeast(YeastNatElement aSeqToFindIsotopes) {
      this.iSeqToFindIsotopes = aSeqToFindIsotopes;

      for(int i = 0; i < 6; ++i) {
         this.iSeqToFindIsotopes = this.getMsIsotopeInfo(this.iSeqToFindIsotopes, i);
      }

   }

   public YeastNatElement getMsIsotopeInfo(YeastNatElement res, int peak) {
      String html = "";

      try {
         URL weatherInfo = new URL("http://prospector.ucsf.edu/cgi-bin/mssearch.cgi");
         URLConnection connection = weatherInfo.openConnection();
         connection.setDoOutput(true);
         PrintStream out = new PrintStream(connection.getOutputStream());
         out.print("nterm=" + URLEncoder.encode(""));
         out.print("&");
         out.print("sequence=" + URLEncoder.encode(""));
         out.print("&");
         out.print("cterm=" + URLEncoder.encode(""));
         out.print("&");
         out.print("const_mod=" + URLEncoder.encode(""));
         out.print("&");
         out.print("user_aa_composition=" + URLEncoder.encode(""));
         out.print("&");
         out.print("user_aa_2_composition=" + URLEncoder.encode(""));
         out.print("&");
         out.print("user_aa_3_composition=" + URLEncoder.encode(""));
         out.print("&");
         out.print("user_aa_4_composition=" + URLEncoder.encode(""));
         out.print("&");
         if (peak == 0) {
            out.print("elemental_composition=" + URLEncoder.encode(res.getElementalCompositionContAce().trim()));
         }

         if (peak == 1) {
            out.print("elemental_composition=" + URLEncoder.encode(res.getElementalCompositionContAcD3().trim()));
         }

         if (peak == 2) {
            out.print("elemental_composition=" + URLEncoder.encode(res.getElementalCompositionHumAce().trim()));
         }

         if (peak == 3) {
            out.print("elemental_composition=" + URLEncoder.encode(res.getElementalCompositionHumAcD3().trim()));
         }

         if (peak == 4) {
            out.print("elemental_composition=" + URLEncoder.encode(res.getElementalCompositionYeaAce().trim()));
         }

         if (peak == 5) {
            out.print("elemental_composition=" + URLEncoder.encode(res.getElementalCompositionYeaAcD3().trim()));
         }

         out.print("&");
         out.print("averagine_mass=" + URLEncoder.encode("1000.0"));
         out.print("&");
         out.print("distribution_type=" + URLEncoder.encode("Elemental Composition"));
         out.print("&");
         out.print("profile_type=" + URLEncoder.encode("Guassian"));
         out.print("&");
         out.print("parent_charge=" + URLEncoder.encode(res.getChargeAsString()));
         out.print("&");
         out.print("resolution=" + URLEncoder.encode("10000.0"));
         out.print("&");
         out.print("delailed_report=" + URLEncoder.encode("1"));
         out.print("&");
         out.print("output_type=" + URLEncoder.encode("HTML"));
         out.print("&");
         out.print("result_to_file=" + URLEncoder.encode("1"));
         out.print("&");
         out.print("output_filename=" + URLEncoder.encode("lastres"));
         out.print("&");
         out.print("percent_C13=" + URLEncoder.encode("100"));
         out.print("&");
         out.print("percent_N15=" + URLEncoder.encode("100"));
         out.print("&");
         out.print("percent_O18=" + URLEncoder.encode("100"));
         out.print("&");
         out.print("report_title=" + URLEncoder.encode("MS-Isotope"));
         out.print("&");
         out.print("search_name=" + URLEncoder.encode("msisotope"));
         out.close();

         DataInputStream in;
         String temp_data;
         for(in = new DataInputStream(connection.getInputStream()); (temp_data = in.readLine()) != null; html = html + temp_data) {
         }

         in.close();
      } catch (MalformedURLException var9) {
         System.out.println("Illegal URL: " + var9);
      } catch (IOException var10) {
         System.out.println("IOException: " + var10);
      } catch (Exception var11) {
         System.out.println("Error: " + var11);
      }

      res = this.getResult(html, res, peak);
      return res;
   }

   public YeastNatElement getResult(String result, YeastNatElement res, int peak) {
      String Table = result.substring(result.indexOf("<table><tr><th>Isotope<br "), result.indexOf("</table></td><td valign=\"bottom\" align=\"right\">"));
      String[] rows = new String[8];
      int start = 0;

      for(int i = 0; i < 8; ++i) {
         rows[i] = result.substring(result.indexOf("<tr><td align=\"left\">", start), result.indexOf("</tr>", result.indexOf("<tr><td align=\"left\">", start)));
         start = result.indexOf("<tr><td align=\"left\">", start) + 5;
      }

      double[] percTot = new double[rows.length];
      double[] percMax = new double[rows.length];

      for(int i = 0; i < rows.length; ++i) {
         String row = rows[i];
         row = row.substring(row.indexOf("<td align=\"right\">") + 18);
         percTot[i] = Double.valueOf(row.substring(row.indexOf("<td align=\"right\">") + 18, row.indexOf("</td>", row.indexOf("<td align=\"right\">"))));
         row = row.substring(row.indexOf("<td align=\"right\">") + 18);
         percMax[i] = Double.valueOf(row.substring(row.indexOf("<td align=\"right\">") + 18, row.indexOf("</td>", row.indexOf("<td align=\"right\">"))));
      }

      if (peak == 0) {
         res.setPercContAceMax(percMax);
         res.setPercContAceTot(percTot);
      }

      if (peak == 1) {
         res.setPercContAcD3Max(percMax);
         res.setPercContAcD3Tot(percTot);
      }

      if (peak == 2) {
         res.setPercHumAceMax(percMax);
         res.setPercHumAceTot(percTot);
      }

      if (peak == 3) {
         res.setPercHumAcD3Max(percMax);
         res.setPercHumAcD3Tot(percTot);
      }

      if (peak == 4) {
         res.setPercYeaAceMax(percMax);
         res.setPercYeaAceTot(percTot);
      }

      if (peak == 5) {
         res.setPercYeaAcD3Max(percMax);
         res.setPercYeaAcD3Tot(percTot);
      }

      return res;
   }
}
