package com.compomics.natter.msIsotope;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.log4j.Logger;

public class MsIsotopePost {
   private static Logger logger = Logger.getLogger(MsIsotopePost.class);
   private MsIsotopeResult[] iSeqToFindIsotopes;

   public MsIsotopePost(MsIsotopeResult[] aSeqToFindIsotopes) {
      this.iSeqToFindIsotopes = aSeqToFindIsotopes;

      for(int i = 0; i < this.iSeqToFindIsotopes.length; ++i) {
         this.iSeqToFindIsotopes[i] = this.getMsIsotopeInfo(this.iSeqToFindIsotopes[i]);
      }

   }

   public MsIsotopeResult getMsIsotopeInfo(MsIsotopeResult res) {
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
         out.print("elemental_composition=" + URLEncoder.encode(res.getElementalComposition().trim()));
         out.print("&");
         out.print("averagine_mass=" + URLEncoder.encode("1000.0"));
         out.print("&");
         out.print("distribution_type=" + URLEncoder.encode("Elemental Composition"));
         out.print("&");
         out.print("profile_type=" + URLEncoder.encode("Guassian"));
         out.print("&");
         out.print("parent_charge=" + URLEncoder.encode(res.getIChargeAsString()));
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
      } catch (MalformedURLException var8) {
         System.out.println("Illegal URL: " + var8);
      } catch (IOException var9) {
         System.out.println("IOException: " + var9);
      } catch (Exception var10) {
         System.out.println("Error: " + var10);
      }

      res = this.getResult(html, res);
      return res;
   }

   public MsIsotopeResult getResult(String result, MsIsotopeResult res) {
      String Table = result.substring(result.indexOf("<table><tr><th>Isotope<br "), result.indexOf("</table></td><td valign=\"bottom\" align=\"right\">"));
      String[] rows = new String[5];
      int start = 0;

      for(int i = 0; i < 5; ++i) {
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

      res.setPercMax(percMax);
      res.setPercTot(percTot);
      return res;
   }

   public MsIsotopeResult[] getMsIsotopeResults() {
      return this.iSeqToFindIsotopes;
   }
}
