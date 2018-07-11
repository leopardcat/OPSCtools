import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class CVEjsonanaly {

    public static Connection con = null;
    private static Statement st=null;
    private static ResultSet rs=null;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String user = "root";
    private static String password = "123456";
    static String databaseurl="jdbc:mysql://localhost:3306/cve_infosource?useSSL=true";
    static String path="W:\\pku_pro\\nvdcve-1.0-2014.json";
    static String tablename="cve_info_2014";
    
    public static void jstranstodb() throws SQLException {
    	try {
    		Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~");
	    	
    		JsonObject wholeobj=readfile();
    		JsonArray CVE_Items=wholeobj.getAsJsonArray("CVE_Items");
    		for(JsonElement element:CVE_Items) {
    			JsonObject cveitem=element.getAsJsonObject();//every element in the jsonarray
    			
    			JsonObject cve=cveitem.getAsJsonObject("cve");//json named cve which supplys basic info
    			
    			JsonObject CVE_data_meta=cve.getAsJsonObject("CVE_data_meta");//json named affects supply project info
    			String ID=CVE_data_meta.getAsJsonPrimitive("ID").toString();String[] IDs=ID.split("-");
    			String cve_year=IDs[1],cve_id=IDs[2].replace("\"", "");//get first two field cve_year and cve_id
    			JsonObject descriptionobj=cve.getAsJsonObject("description");
    			JsonArray description_data=descriptionobj.getAsJsonArray("description_data");
    			
    			String description=null;
    			for(JsonElement element4:description_data) {
    				JsonObject obj4=element4.getAsJsonObject();
    				String value=obj4.getAsJsonPrimitive("value").toString();//get the last field description
    				value.replaceAll("", "").replaceAll("\"", "").replaceAll("\'", "");
    				description=value;
    			}
    			
    			JsonObject affects=cve.getAsJsonObject("affects");//json named affects supply project info
    			JsonObject vendorobj=affects.getAsJsonObject("vendor");
    			JsonArray vendor_data=vendorobj.getAsJsonArray("vendor_data");//the array contains all projects and versions
    			if(vendor_data.size()!=0) {
    				for(JsonElement element1:vendor_data) {
    					JsonObject obj1=element1.getAsJsonObject();//every element in the vendor_data array
    					
    					String vendor_name=obj1.getAsJsonPrimitive("vendor_name").toString().replaceAll("\"", "").replaceAll("\'", "");//get the forth field vendor
    					JsonObject product=obj1.getAsJsonObject("product");
    					JsonArray product_data=product.getAsJsonArray("product_data");
    					for(JsonElement element2:product_data) {
    						JsonObject obj2=element2.getAsJsonObject();//every element in the product_data array
    						String product_name=obj2.getAsJsonPrimitive("product_name").toString().replaceAll("\"", "").replaceAll("\'", "");//get the third field product_name
    						JsonObject versionobj=obj2.getAsJsonObject("version");
    						JsonArray version_data=versionobj.getAsJsonArray("version_data");
    						for(JsonElement element3:version_data) {
    							JsonObject obj3=element3.getAsJsonObject();
    							String version_value=obj3.getAsJsonPrimitive("version_value").toString().replaceAll("\"", "");//get the 5th field version
    							String nu="null";
    							Statement statement=con.createStatement();
    							String sql="Insert into "+tablename+" values ('"+cve_year+"','"+cve_id+"','"+product_name+"','"+vendor_name+"','"+version_value+"','"+nu+"','"+nu+"','"+nu+"','"+nu+"','"+nu+"','"+nu+"')";
    							System.out.println(sql);
    							statement.executeUpdate(sql);
    						}
    					}
    					System.out.println(vendor_name);
    					System.out.println(description);
    					System.out.println(cve_id);
    				}
    				
    			}
    			
    		}
			
	    	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    
    
    public static JsonObject readfile() {
    	JsonObject wholeobj=new JsonObject();
    	File file= new File(path);
    	if(file.isFile()&&file.exists()) {
    		try {
				InputStreamReader read=new InputStreamReader(new FileInputStream(file));
				BufferedReader buff=new BufferedReader(read);
				StringBuilder sb=new StringBuilder();String line=null;
				while((line=buff.readLine())!=null) {if(line.contains("itsover"))break;sb.append(line);}
				String jsstr=sb.toString();
				JsonParser parser=new JsonParser();
				wholeobj=parser.parse(jsstr).getAsJsonObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
		return wholeobj;
    }
}
