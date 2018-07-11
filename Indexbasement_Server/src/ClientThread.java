import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class ClientThread implements Runnable{

	private Socket s;
	private String number1="wyl",number2="wyz",number3="wq",number4="ymz",number5="bhr";
	ClientThread(Socket s){
		this.s=s;
	}
	public void run(){
	    Logger log=Logger.getLogger(ClientThread.class);
		String ip=s.getInetAddress().getHostAddress();
		try{
			ArrayList<String> cache=PropertiesReader.getPropertiesReader();
			String rootpath=cache.get(0);//��ʼ�������ļ�
			BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			
			StringBuilder sb=new StringBuilder();
			String line =null;
			  while ((line=br.readLine())!=null){ 
				  if(line.contains("itsoverhaha"))
					  break;
				  sb.append(line);
			}
			String ssb=sb.toString();
			String[] ssbb=ssb.split("@");
			String databaseurl="jdbc:mysql://localhost:3306/cve_infosource?useSSL=true";
			
			if(ssbb[0].equals("0")){//���������������ֺ�����ѱ���Լ����ֵ�cve
				if(authentication(ssbb)){
					String manager=ssbb[1];
					System.out.println(ssbb[1]);
					JSONObject undercve=Make_task.getCVEitems(manager,databaseurl);
					System.out.println(undercve.toString());
					bw.write(undercve.toString());
					bw.flush();
					s.close();
				}else{s.close();}
			}else if(ssbb[0].equals("1")){//�����ȡ��һ��cve��Ŀ�������ı�����cve��Ŷ�Ӧ����Ŀ������Ϊdone���ظ�0����
				if(authentication(ssbb)){
					String manager=ssbb[1];String cve_num=ssbb[2];String[] cve=cve_num.split("-");String cve_year=cve[1];String cve_id=cve[2];
					JSONObject undercve=Make_task.remarkandgoon(manager, cve_year, cve_id, databaseurl);
					System.out.println(undercve.toString());
					bw.write(undercve.toString());
					bw.flush();
					s.close();
				}else{s.close();}
			}else if(ssbb[0].equals("2")){//������Ŀ���͹�Ӧ�̲�ѯԭ��������Ӧ�������ǲ���Դ��NOS��
				if(authentication(ssbb)){
					String manager=ssbb[1];String proname=ssbb[2];String vendor=ssbb[3];
					String flag=Make_task.tagcloseSource(manager, proname, vendor, databaseurl,ip);
					bw.write(flag);bw.write("itsoverhaha");bw.newLine();bw.flush();
					s.close();
				}
			}else if(ssbb[0].equals("3")){//����CVE��ţ���Ŀ������Ӧ�̺Ͱ汾�ţ���ѯԭ����Ӧ��Ŀ����δ�ҵ���ǣ�NFound��
				if(authentication(ssbb)){
					String manager=ssbb[1];String cve_num=ssbb[2];String proname=ssbb[3];String version=ssbb[4];
					String flag=Make_task.tagNFound(manager,proname,version,databaseurl,ip);
					bw.write(flag);bw.newLine();bw.flush();
					s.close();
				}else{s.close();}
			}else if(ssbb[0].equals("4")){//����������Ϣ�����������д�����Ŀ������
				if(authentication(ssbb)){
					String manager=ssbb[1];String cve_num=ssbb[2];String proname=ssbb[3];String vendor=ssbb[4];String version=ssbb[5];String downloadurl=ssbb[6];String fileformat=ssbb[7];
					System.out.println(version);
					String filepath=rootpath+"\\"+proname+"\\";
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
					String date=df.format(new Date());
					new Thread(new DownUtil(s,downloadurl,rootpath,proname,version,fileformat,ip,manager,cve_num,vendor,filepath,date,databaseurl)).start();
				}else{s.close();}
			}else if(ssbb[0].equals("5")){
				if(authentication(ssbb)){
					String manager=ssbb[1];String cve_num=ssbb[2];String proname=ssbb[3];String version=ssbb[4];String reason=ssbb[5];
					System.out.println(version);
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
					String date=df.format(new Date());
					String flag=Make_task.insertProblemitem(manager, cve_num, proname, version,reason, date, databaseurl,ip);
					bw.write(flag);bw.newLine();bw.flush();
					s.close();
				}
			}else if(ssbb[0].equals("6")){//������Ŀ���Ƽ�������������а汾������
				String proname=ssbb[1];
				String versions=Make_task.searchversion(proname, databaseurl);
				bw.write(versions);bw.newLine();bw.flush();
				s.close();
			}else if(ssbb[0].equals("7")){//��ѯ����Ա��ʱ����ڶ������������������ף�������ÿ�����Ѿ��ж�cve��Ŀ������
				String manager=ssbb[1],datefrom=ssbb[2],dateto=ssbb[3];
			}
		}catch(Exception e){
			log.info(e+":"+ip+"����ʧ��");
			log.error(e+":"+ip+"����ʧ��");
			throw new RuntimeException(ip+"����ʧ��");
		}
	}
	
	public boolean authentication(String[] ssbb){
		boolean falg = ssbb[1].equals(number1)||ssbb[1].equals(number2)||ssbb[1].equals(number3)||ssbb[1].equals(number4)||ssbb[1].equals(number5);
		return falg;
	}
	
}
