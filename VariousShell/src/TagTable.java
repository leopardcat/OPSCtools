import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TagTable {
	public static Connection con = null;
    private static Statement st=null;
    private static Statement st1=null;
    private static Statement st2=null;
    private static ResultSet rs=null;
    private static String driver = "com.mysql.jdbc.Driver";
    private static String user = "root";
    private static String password = "123456";
    private static String closeSource="wyl";
    private static String manager1="wyl",manager2="wyz",manager3="wq",manager4="bhr",manager5="ymz";
    static String databaseurl="jdbc:mysql://localhost:3306/cve_infosource?useSSL=true";
    static String tablename="cve_info_2014";
    static String tablename1="nvdindex_database";
    static String tabletestname="cve_info_2014";
    
    public static void tagcloseSource() throws SQLException {
    	try {
    		Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~");
			st=con.createStatement();
			String vendor="email_address",vendor1="email_address_module_project";
			String sql="UPDATE "+tablename+" SET closeSource='"+closeSource+"',manager='"+manager1+"' WHERE vendor='"+vendor+"'";
	    	st.executeUpdate(sql);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
    
    public static void tagAllClose(){
    	FileReader file;
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~");
	    	st=con.createStatement();
			file = new FileReader(new File("W:\\pku_pro\\close_vendor.txt"));
			BufferedReader rd=new BufferedReader(file);
			String str=null;
			StringBuilder sb=new StringBuilder();
			while((str=rd.readLine())!=null){
				sb.append(str);
			}
			String ssb=sb.toString();
			String[] allvendor=ssb.split("~");
			for(int i=0;i<allvendor.length;i++){
				String vendor=allvendor[i];
				String sql="update "+tablename+" set closeSource='"+closeSource+"' where vendor='"+vendor+"'";
				st.executeUpdate(sql);
				System.out.println(vendor);
			}
		} catch (IOException | SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void tagfirmware(){
    	try {
			Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~");
	    	st=con.createStatement();
	    	String sql1="select * from "+tablename;
	    	ResultSet rs=st.executeQuery(sql1);
	    	while(rs.next()){
	    		String project_name=rs.getString(3);
	    		if(project_name.contains("firmware")){
	    			st1=con.createStatement();
	    			String sql2="UPDATE "+tablename+" SET closeSource='"+closeSource+"',manager='"+manager1+"' WHERE project_name='"+project_name+"'";
	    			st1.executeUpdate(sql2);
	    			System.out.println(project_name);
	    		}
	    	}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    
    public static void caculate(){
    	try {
    		Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~");
			st=con.createStatement();
			List<String> managers=new ArrayList<String>();
			int totlecount=0;
			managers.add(manager1);managers.add(manager2);managers.add(manager3);managers.add(manager4);managers.add(manager5);
			for(String manager:managers){
				String sql="select * from "+tablename1+" WHERE manager='"+manager+"'";
				ResultSet rs=st.executeQuery(sql);
				rs.last();
				int rowcount=rs.getRow();
				totlecount=totlecount+rowcount;
				System.out.println(manager+":"+rowcount);
			}
			System.out.println(totlecount);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }
}
