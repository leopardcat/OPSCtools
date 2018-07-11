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

	public static ArrayList<String> getPropertiesReader() throws IOException{//从配置文件中读取根目录和数据源目录
//      InputStream inStream=PropertiesReader.class.getClassLoader().getResourceAsStream("config.properties");
      Properties properties=new Properties();
		String propertyFilePath=PropertiesReader.class.getClassLoader().getResource("//").getPath()+"/config.properties";
		FileInputStream fis;
		ArrayList<String> cache=new ArrayList<String>();
      try {
      	fis=new FileInputStream(propertyFilePath);
          properties.load(fis);//载入输入流
          Enumeration enumeration=properties.propertyNames();//取得配置文件里所有的key值
          while(enumeration.hasMoreElements()){
              String key=(String) enumeration.nextElement();
//              System.out.println("配置文件里的key值："+key+"=====>配置文件里的value值："+properties.getProperty(key));//输出key值
          }
          cache.add(properties.getProperty("root_path"));
      } catch (IOException e) {
          e.printStackTrace();
      }
		return cache;
		}
	
	public static void writeProperties(String root_path) throws IOException {//每当设置新路径，改变配置文件
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
