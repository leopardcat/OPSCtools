import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;

import org.apache.log4j.Logger;

public class DownUtil implements Runnable{

	private String downloadurl,rootpath,proname,version,fileformat,ip,manager,cvenum,vendor,filepath,date,databaseurl;
	private Socket s;
	Logger log=Logger.getLogger(DownUtil.class);
	DownUtil(Socket s,String downloadurl,String rootpath,String proname,String version,String fileformat,String ip,String manager,
			String cvenum,String vendor,String filepath,String date,String databaseurl){
		this.s=s;
		this.downloadurl=downloadurl;
		this.rootpath=rootpath;
		this.proname=proname;
		this.version=version;
		this.fileformat=fileformat;
		this.ip=ip;
		this.manager=manager;
		this.cvenum=cvenum;
		this.vendor=vendor;
		this.filepath=filepath;
		this.date=date;
		this.databaseurl=databaseurl;
	}
	
	
	@Override
	public void run() {
		try{
			File saveDir = new File(rootpath+File.separator+proname);
	        File zipfile = new File(saveDir+File.separator+proname+"-"+version+fileformat); //����������ļ��ľ���·��     
	        if(!saveDir.exists()){
	            saveDir.mkdir();
	        }
			InputStream inStream=null;
			FileOutputStream fs =null;
			URL url=new URL(downloadurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
	        conn.setRequestProperty("Connection", "Keep-Alive");
	        conn.setRequestProperty("Charset", "UTF-8");
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        conn.setUseCaches(false);

			if(conn.getResponseCode()==200){
				//������
			    conn.connect();
			    //��ȡ���ݳ���
			    int contentLength = conn.getContentLength();
				inStream = conn.getInputStream();//�õ�������
				byte[] getData = readInputStream(inStream);//��readinputstream���������������γɶ����Ʊ�����
				//�ļ�����λ��
			    FileOutputStream fos = new FileOutputStream(zipfile);//��ͨ�ļ����ͨ��
			    fos.write(getData);//�Ѷ����Ʊ�����ͨ�����ͨ��д����Ӧ·��
			    if(fos!=null){
			        fos.close();
			    }  
			    if(inStream!=null){  
			        inStream.close();  
			    }
			    String flag=Make_task.insertanddown(manager,rootpath, cvenum, proname, vendor, version, downloadurl,filepath,fileformat,date, databaseurl);
			    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			    bw.write(proname+"--"+version+"�����"+"\n");bw.write("itsoverhaha");bw.newLine();bw.flush();
			    s.close();
			}else{
//				HttpURLConnection conn1 = redirect(conn);
				String location=conn.getHeaderField("Location");
				URL url1=new URL(location);
				HttpURLConnection conn1=(HttpURLConnection) url1.openConnection();
				conn1.setRequestMethod("GET");
				conn1.setConnectTimeout(5000);
		        conn1.setRequestProperty("Connection", "Keep-Alive");
		        conn1.setRequestProperty("Charset", "UTF-8");
		        conn1.setDoOutput(true);
		        conn1.setDoInput(true);
		        conn1.setUseCaches(false);
		        conn1.connect();
			    //��ȡ���ݳ���
			    int contentLength = conn1.getContentLength();
				inStream = conn1.getInputStream();//�õ�������
				byte[] getData = readInputStream(inStream);//��readinputstream���������������γɶ����Ʊ�����
				//�ļ�����λ��
			    FileOutputStream fos = new FileOutputStream(zipfile);//��ͨ�ļ����ͨ��
			    fos.write(getData);//�Ѷ����Ʊ�����ͨ�����ͨ��д����Ӧ·��
			    if(fos!=null){
			        fos.close();
			    }  
			    if(inStream!=null){  
			        inStream.close();  
			    }
			    String flag=Make_task.insertanddown(manager,rootpath, cvenum, proname, vendor, version, downloadurl,filepath,fileformat,date, databaseurl);
			    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			    bw.write(proname+"--"+version+"�����"+"\n");bw.write("itsoverhaha");bw.newLine();bw.flush();
			    s.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			log.info(e);
			log.error(proname+"-"+version+"-"+manager+"---"+"����ʧ��");
			try {
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				bw.write(proname+"--"+version+"����ʧ��"+"\n");bw.write("itsoverhaha");bw.flush();
				s.close();
			} catch (IOException e1) {
				// TODO �Զ����ɵ� catch ��
				e1.printStackTrace();
			}

		}
	}
	
	public HttpURLConnection redirect(HttpURLConnection conn){
		try {
			String location=conn.getHeaderField("Location");
			URL url1=new URL(location);
			HttpURLConnection conn1=(HttpURLConnection) url1.openConnection();
			if(conn1.getResponseCode()==200){
				
			}else{
				redirect(conn1);
				conn=conn1;
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return conn;
	}
    
	public static  byte[] readInputStream(InputStream inputStream) throws IOException {  //���������л�ȡ�ֽ�����
        byte[] buffer = new byte[8192];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
    }
	

}
