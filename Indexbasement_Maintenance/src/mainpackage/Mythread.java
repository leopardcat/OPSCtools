package mainpackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

public class Mythread implements Runnable {

	public static String completestr="今日已完成：\n";
	public int port = 20001;
	private String Ipaddress=MainForm.Ipaddress;
	private String write;
	private String proname;
	private String version;
	private JTextField CVEnum1;
	private JTextField ratetext;
	private JTextArea proitem;
	private JTextArea completeitem;
	private JTextField sampletext;
	private JTextArea textArea;
	private JTextField downloadtext;
	private JTextField fileformaltext;
	
	private int id;
	
	public Mythread(int id,String write,JTextField CVEnum1,JTextField ratetext,JTextArea proitem,JTextArea completeitem,JTextField sampletext,JTextArea textArea) {
		this.id=id;
		this.write=write;
		this.CVEnum1=CVEnum1;
		this.ratetext=ratetext;
		this.proitem=proitem;
		this.completeitem=completeitem;
		this.sampletext=sampletext;
		this.textArea=textArea;
	}
	public Mythread(int id,String write,JTextField CVEnum1,JTextField ratetext,JTextArea proitem,JTextArea completeitem,JTextField sampletext,JTextArea textArea,String proname,String version) {
		this.id=id;
		this.write=write;
		this.CVEnum1=CVEnum1;
		this.ratetext=ratetext;
		this.proitem=proitem;
		this.completeitem=completeitem;
		this.sampletext=sampletext;
		this.textArea=textArea;
		this.proname=proname;
		this.version=version;
	}
	public Mythread(int id,String write,JTextField sampletext,JTextArea textArea,JTextField downloadtext,String version,JTextField fileformaltext){
		this.id=id;
		this.write=write;
		this.sampletext=sampletext;
		this.textArea=textArea;
		this.downloadtext=downloadtext;
		this.version=version;
		this.fileformaltext=fileformaltext;
	}

     public void run() {
    	 Socket s;
		try {
			s = new Socket(Ipaddress,port);
			BufferedWriter buffout=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			BufferedReader buffin=new BufferedReader(new InputStreamReader(s.getInputStream()));
			buffout.write(write);buffout.newLine();//写入三连
			buffout.flush();
			
			if(id==0||id==1) {
				JSONObject undercve=new JSONObject(buffin.readLine());
				String cve_num=undercve.getString("cve_num");CVEnum1.setText(cve_num);
				String sb=FuncClass.analysisJSON(undercve);
				String rate=undercve.getString("rate");
				ratetext.setText(rate+"%");
				proitem.setText(sb);
			}else if(id==2||id==3||id==5) {
				StringBuilder sb=new StringBuilder();
				String line =null;
				  while ((line=buffin.readLine())!=null){ 
					  if(line.contains("itsoverhaha"))
						  break;
					  sb.append(line);
				}
				String ssb=sb.toString();
				completestr=completestr+ssb+"\n";
				completeitem.setText(completestr);
				completeitem.setCaretPosition(completeitem.getDocument().getLength());
			}else if(id==4) {
				completestr=completestr+proname+"--"+version+" is downloading..."+"\n";
				completeitem.setText(completestr);
				completeitem.setCaretPosition(completeitem.getDocument().getLength());
				StringBuilder sb=new StringBuilder();
				String line =null;
				  while ((line=buffin.readLine())!=null){ 
					  if(line.contains("itsoverhaha"))
						  break;
					  sb.append(line);
				}
				String ssb=sb.toString();
				completestr=completestr+ssb+"\n";
				completeitem.setText(completestr);
				completeitem.setCaretPosition(completeitem.getDocument().getLength());
			}else if(id==6) {
				String sb=buffin.readLine().toString();
				String sampurl="";String sb1="";String[] ssb = null;
				if(!sb.equals("#")) {
					String[] sb12=sb.split("#");
					sampurl=sb12[0];
					sb1=sb12[1];
					ssb=sb1.split("@");
				}
				sampletext.setText(sampurl);
				String sssb="";
				if(ssb!=null) {
					for(int i=0;i<ssb.length;i++) {
						if(i==0||i%5!=0)
							sssb=sssb+ssb[i];
						else if(i%5==0&i!=0)
							sssb=sssb+"\n"+ssb[i];
					}
				}
				textArea.setText(sssb);
				String newurl=FuncClass.sub_string(sampurl,version);
				if(newurl.contains("zip")){
					fileformaltext.setText(".zip");
				}else if(newurl.contains("tar.gz")||newurl.contains("tgz")){
					fileformaltext.setText(".tar.gz");
				}else if(newurl.contains("tar.xz")){
					fileformaltext.setText(".tar.xz");
				}else if(newurl.contains("tar.bz2")){
					fileformaltext.setText(".tar.bz2");
				}
				downloadtext.setText(newurl);
				downloadtext.setCaretPosition(downloadtext.getText().length());
			}
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
     }
     
}
