package mainpackage;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

public class FuncClass {
   private static String IPaddress=MainForm.Ipaddress;
   public static int port = 20001;
   public static void settextbox(Object obj,JCheckBox forcheck,JCheckBox c1,JCheckBox c2,JCheckBox c3,JTextField vendortext,JTextField versiontext,JTextField downloadtext,JTextField fileformaltext,JTextField reasontext) {
      if(obj.equals(forcheck)){
         if(c3.isSelected()) {
            vendortext.setEditable(false);versiontext.setEditable(true);downloadtext.setEditable(false);fileformaltext.setEditable(false);reasontext.setEditable(true);
            vendortext.setText(null);downloadtext.setText(null);fileformaltext.setText(null);
         }else {
            reasontext.setEditable(false);
            if(c1.isSelected()&c2.isSelected()){
               vendortext.setEditable(true);versiontext.setEditable(true);downloadtext.setEditable(true);fileformaltext.setEditable(true);
               reasontext.setText(null);
            }else if(c1.isSelected()&!c2.isSelected()){
               vendortext.setEditable(false);versiontext.setEditable(true);downloadtext.setEditable(false);fileformaltext.setEditable(false);
               vendortext.setText(null);downloadtext.setText(null);fileformaltext.setText(null);reasontext.setText(null);
            }else if(!c1.isSelected()&!c2.isSelected()) {
               vendortext.setEditable(true);versiontext.setEditable(false);downloadtext.setEditable(false);fileformaltext.setEditable(false);
               versiontext.setText(null);downloadtext.setText(null);fileformaltext.setText(null);reasontext.setText(null);
            }else if(!c1.isSelected()&c2.isSelected()) {
               vendortext.setEditable(true);versiontext.setEditable(false);downloadtext.setEditable(false);fileformaltext.setEditable(false);
               versiontext.setText(null);downloadtext.setText(null);fileformaltext.setText(null);reasontext.setText(null);
            }
         }
      }
   }

   public static void completeitem(JPanel contentPane,JCheckBox OorNcheck,JCheckBox GorNcheck,JCheckBox Judgeablecheck,
                           JTextField manager,JTextField CVEnum1,JTextField pronametext,JTextField vendortext,JTextField versiontext,JTextField downloadtext,
                           JTextField fileformaltext,JTextField reasontext,JTextArea completeitem,JTextField ratetext,JTextArea proitem,JTextField sampletext,JTextArea textArea) {
      if(Judgeablecheck.isSelected()) {
         String s1=manager.getText().toString(),s2=CVEnum1.getText().toString(),s3=pronametext.getText().toString(),s4=versiontext.getText().toString(),s5=reasontext.getText().toString();
         if(!(s1.equals(""))&&!(s2.equals(""))&&!(s3.equals(""))&&!(s4.equals(""))&&!(s5.equals(""))) {
            String write="5"+"@"+manager.getText()+"\n"+"@"+CVEnum1.getText()+"\n"+"@"+pronametext.getText()+"\n"+"@"+versiontext.getText()+"\n"+"@"+reasontext.getText()+"\n"+"itsoverhaha";
            EventQueue.invokeLater(new Mythread(5,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
         }else {JOptionPane.showMessageDialog(contentPane, "请先把必填项填写完");}
      }else {
         if((!OorNcheck.isSelected())&(!GorNcheck.isSelected())) {
            String s1=manager.getText().toString(),s2=pronametext.getText().toString(),s3=vendortext.getText().toString();
            if(!(s1.equals(""))&&!(s2.equals(""))&&!(s3.equals(""))) {
               String write="2"+"@"+manager.getText()+"\n"+"@"+pronametext.getText()+"\n"+"@"+vendortext.getText()+"\n"+"itsoverhaha";
               Thread mth=new Thread(new Mythread(2,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
               mth.start();
            }else {JOptionPane.showMessageDialog(contentPane, "请先把必填项填写完");}
         }else if(!OorNcheck.isSelected()&GorNcheck.isSelected()) {
            String s1=manager.getText().toString(),s2=pronametext.getText().toString(),s3=vendortext.getText().toString();
            if((!s1.equals(""))&&(!s2.equals(""))&&(!s3.equals(""))) {
               String write="2"+"@"+manager.getText()+"\n"+"@"+pronametext.getText()+"\n"+"@"+vendortext.getText()+"\n"+"itsoverhaha";
               Thread mth=new Thread(new Mythread(2,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
               mth.start();
            }else {JOptionPane.showMessageDialog(contentPane, "请先把必填项填写完");}
         }else if(OorNcheck.isSelected()&!GorNcheck.isSelected()) {
            String s1=manager.getText().toString(),s2=CVEnum1.getText().toString(),s3=pronametext.getText().toString(),s4=versiontext.getText().toString();
            if((!s1.equals(""))&&(!s2.equals(""))&&(!s3.equals(""))&&(!s4.equals(""))) {
               String write="3"+"@"+manager.getText()+"\n"+"@"+CVEnum1.getText()+"\n"+"@"+pronametext.getText()+"\n"+"@"+versiontext.getText()+"\n"+"itsoverhaha";
               Thread mth=new Thread(new Mythread(3,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
               mth.start();
            }else {JOptionPane.showMessageDialog(contentPane, "请先把必填项填写完");}
         }else if(OorNcheck.isSelected()&GorNcheck.isSelected()) {
            String s1=manager.getText().toString(),s2=CVEnum1.getText().toString(),s3=pronametext.getText().toString(),s4=vendortext.getText().toString(),
                  s5=versiontext.getText().toString(),s6=downloadtext.getText().toString(),s7=fileformaltext.getText().toString();
            if((!s1.equals(""))&&(!s2.equals(""))&&(!s3.equals(""))&&(!s4.equals(""))&&(!s5.equals(""))&&(!s6.equals(""))&&(!s7.equals(""))) {
               String write="4"+"@"+manager.getText()+"\n"+"@"+CVEnum1.getText()+"\n"+"@"+pronametext.getText()+"\n"+"@"+vendortext.getText()+"\n"+"@"+versiontext.getText()+"\n"+"@"+downloadtext.getText()+"\n"+"@"+fileformaltext.getText()+"\n"+"itsoverhaha";
               Thread mth=new Thread(new Mythread(4,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea,s3,s5));
               mth.start();
            }else {JOptionPane.showMessageDialog(contentPane, "请先把必填项填写完");}
         }
      }
   }

   public static String analysisJSON(JSONObject undercve) {
      JSONArray existvs=undercve.getJSONArray("exist");
      JSONArray proitems=undercve.getJSONArray("items");
      JSONArray unexproitems=new JSONArray();//for setting the var of Unexisted object
      List<String> notexlist=new ArrayList<>();
      List<String> exnamelist=new ArrayList<>();
      List<String> exversionlist=new ArrayList<>();

      StringBuilder sb=new StringBuilder();
      sb.append("本条cve所含版本:"+"\n");
      for(int i=0;i<proitems.length();i++) {
         JSONObject item=proitems.getJSONObject(i);
         String proname=item.getString("pro_name"),version=item.getString("version"),vendor=item.getString("vendor");
         String eachitem="项目名:"+proname+"   "+"供应商:"+vendor+"   "+"版本号:"+version;
         sb.append(eachitem+"\r\n");
         if(existvs.length()>0) {
            for(int j=0;j<existvs.length();j++) {
               JSONObject existitem=existvs.getJSONObject(j);
               String exproname=existitem.getString("pro_name");
               String exversion=existitem.getString("version");
               exnamelist.add(exproname);
               exversionlist.add(exversion);
            }
         }
         if(exnamelist.contains(proname)&&exversionlist.contains(version)) {
         }else {
            unexproitems.put(item);
            String exstr="项目名:"+proname+"  "+"版本号:"+version;
            if(!notexlist.contains(exstr)) {notexlist.add(exstr);}
         }
      }
      String title="索引库尚无版本:"+"\r\n";
      sb.append(title);
      if(notexlist.size()>0) {
         for(int i=0;i<notexlist.size();i++) {
            String notexitem=notexlist.get(i)+"\r\n";
            sb.append(notexitem);
         }
      }

      MainForm.unex.setUnexproitems(unexproitems);
      MainForm.unex.setCursor(0);
      return sb.toString();
   }

   public static void autofill(Unexisted unex,JTextField pronametext,JTextField vendortext,JTextField versiontext,JTextField sampletext,JTextArea textArea,JTextField downloadtext,JTextField fileformaltext) {
      JSONArray unexproitems=unex.getUnexproitems();
      int cursor=unex.getCursor();
      int arrsize=unexproitems.length();//the size of jsonarray
      if(arrsize>0) {
         JSONObject item=unexproitems.getJSONObject(cursor);
         String proname=item.getString("pro_name"),vendor=item.getString("vendor"),version=item.getString("version");
         String write="6"+"@"+proname+"\n"+"itsoverhaha";
         EventQueue.invokeLater(new Mythread(6,write,sampletext,textArea,downloadtext,version,fileformaltext));
         if(pronametext.isEditable()) {pronametext.setText(proname);}
         if(vendortext.isEditable()) {vendortext.setText(vendor);}
         if(versiontext.isEditable()) {versiontext.setText(version);}
         if((cursor+1)==arrsize) {
            unex.setCursor(0);
         }else {
            unex.setCursor(cursor+1);
         }
      }
   }

   public static String sub_string(String string,String newversion)
   {
	   String pattern = "\\d+\\.([a-z])\\-";
	   string = string.replaceAll(pattern,"");
	   String pattern1 = "(v)?\\d+(\\-)(\\d+)(\\-([a-z]|\\d+))?";
	   String pattern2 = "(v)?\\d+(\\_)(\\d+)(\\_([a-z]|\\d+))?";
	   String pattern3 = "(v)?\\d+(\\.)(\\d+)(\\.([a-s,u-x]|\\d+))?([a-z]\\d+)?";

      String newversion1 = newversion.replace('.','-');
      String newversion2 = newversion.replace('.','_');
      string = string.replaceAll(pattern1,newversion1).replaceAll(pattern2,newversion2).replaceAll(pattern3,newversion);

      return string;
   }
}