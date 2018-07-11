import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class PropertiesReader {

	public static ArrayList<String> getPropertiesReader() throws IOException{//�������ļ��ж�ȡ��Ŀ¼������ԴĿ¼
//      InputStream inStream=PropertiesReader.class.getClassLoader().getResourceAsStream("config.properties");
      Properties properties=new Properties();
		String propertyFilePath=PropertiesReader.class.getClassLoader().getResource("//").getPath()+"/config.properties";
		FileInputStream fis;
		ArrayList<String> cache=new ArrayList<String>();
      try {
      	fis=new FileInputStream(propertyFilePath);
          properties.load(fis);//����������
          Enumeration enumeration=properties.propertyNames();//ȡ�������ļ������е�keyֵ
          while(enumeration.hasMoreElements()){
              String key=(String) enumeration.nextElement();
//              System.out.println("�����ļ����keyֵ��"+key+"=====>�����ļ����valueֵ��"+properties.getProperty(key));//���keyֵ
          }
          cache.add(properties.getProperty("root_path"));
      } catch (IOException e) {
          e.printStackTrace();
      }
		return cache;
		}
	
	public static void writeProperties(String root_path) throws IOException {//ÿ��������·�����ı������ļ�
		Properties properties=new Properties(); 
		String propertyFilePath=PropertiesReader.class.getClassLoader().getResource("//").getPath()+"/config.properties";
		FileInputStream fis;
		try {
			fis=new FileInputStream(propertyFilePath);
			properties.setProperty("root_path",root_path);
			FileOutputStream fos = new FileOutputStream(propertyFilePath);
			properties.store(fos, null);
			fos.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void saveschedule(String root_path,String data_path,String row_num) {
		Properties properties=new Properties(); 
		String propertyFilePath=PropertiesReader.class.getClassLoader().getResource("//").getPath()+"/config.properties";
		FileInputStream fis;
		try {
			fis=new FileInputStream(propertyFilePath);
			properties.setProperty("root_path",root_path);
			properties.setProperty("data_path", data_path);
			properties.setProperty("row_num", row_num);
			FileOutputStream fos = new FileOutputStream(propertyFilePath);
			properties.store(fos, null);
			fos.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
