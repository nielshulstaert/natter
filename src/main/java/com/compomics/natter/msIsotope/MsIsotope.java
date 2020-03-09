package com.compomics.natter.msIsotope;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import org.apache.log4j.Logger;

public class MsIsotope {
   private static Logger logger = Logger.getLogger(MsIsotope.class);
   private String iCharge;
   private String iComposition;
   private double[] iPercMax;
   private double[] iPercTot;

   public MsIsotope(String aComposition, String aCharge) {
      this.iCharge = aCharge;
      this.iComposition = aComposition;
      String aHtml = this.post();
      this.parseHtml(aHtml);
   }

   private String post() {
      String html = "";

      try {
         URL weatherInfo = new URL("http://prospector.ucsf.edu/prospector/cgi-bin/mssearch.cgi");
         URLConnection connection = weatherInfo.openConnection();
         connection.setDoInput(true);
         connection.setDoOutput(true);
         connection.setUseCaches(false);
         connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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
         out.print("elemental_composition=" + URLEncoder.encode(this.iComposition.trim()));
         out.print("&");
         out.print("averagine_mass=" + URLEncoder.encode("1000.0"));
         out.print("&");
         out.print("distribution_type=" + URLEncoder.encode("Elemental Composition"));
         out.print("&");
         out.print("profile_type=" + URLEncoder.encode("Guassian"));
         out.print("&");
         out.print("parent_charge=" + URLEncoder.encode(this.iCharge));
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
      } catch (MalformedURLException var7) {
         System.out.println("Illegal URL: " + var7);
         html = this.post();
      } catch (IOException var8) {
         System.out.println("IOException: " + var8);
         html = this.post();
      } catch (Exception var9) {
         System.out.println("Error: " + var9);
         html = this.post();
      }

      return html;
   }

   private void parseHtml(String html) {
      String[] rows = new String[7];
      int start = 0;

      for(int i = 0; i < 7; ++i) {
         rows[i] = html.substring(html.indexOf("<tr><td align=\"left\">", start), html.indexOf("</tr>", html.indexOf("<tr><td align=\"left\">", start)));
         start = html.indexOf("<tr><td align=\"left\">", start) + 5;
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

      this.setPercMax(percMax);
      this.setPercTot(percTot);
   }

   private void setPercTot(double[] percTot) {
      this.iPercTot = percTot;
   }

   private void setPercMax(double[] percMax) {
      this.iPercMax = percMax;
   }

   public double[] getPercMax() {
      return this.iPercMax;
   }

   public double[] getPercTot() {
      return this.iPercTot;
   }
}
