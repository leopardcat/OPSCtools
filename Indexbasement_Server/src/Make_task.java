import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Make_task {
	//声明Connection对象
    public static Connection con = null;
    //声明Statement对象
    private static Statement st=null;
    //声明Result集
    private static ResultSet rs=null;
    //驱动程序名
    private static String driver = "com.mysql.jdbc.Driver";

    //MySQL配置时的用户名
    private static String user = "root";
    //MySQL配置时的密码
    private static String password = "123456";
    static Logger log=Logger.getLogger(Make_task.class);
    private static String tablename="cve_info_2014";

	
    public static JSONObject getCVEitems(String manager,String databaseurl){//行为0，从数据表中选择最近的一条没人选过的条目
    	String nu="null";
    	JSONObject undercve=new JSONObject();//一共四个key：cve_num,reference,exist,items
    	JSONArray proitems=new JSONArray();//装载cve下所有项目所有版本的信息的数组
    	JSONArray existvs=new JSONArray();//装载所有索引库没有项目和对应版本号
    	ResultSet rsif=null;
    	try{
    		Class.forName(driver);
        	con=DriverManager.getConnection(databaseurl,user,password);
        	if(!con.isClosed())
        		System.out.println("Succeeded connecting to the Database~0");
        	Statement statementif=con.createStatement();
        	String sqlif="select DISTINCT * from "+tablename+" where tag='"+manager+"' limit 1";
        	rsif=statementif.executeQuery(sqlif);
        	if(rsif.first()){
        		int cve_year=rsif.getInt(1);int cve_id=rsif.getInt(2);String cve_idstr=rsif.getString(2);
    			String cve_num="cve-"+String.valueOf(cve_year)+"-"+cve_idstr;
    			String description=rsif.getString(11);
        		undercve.put("cve_num", cve_num);//装载cve_num
        		undercve.put("description", description);//装载reference
        		ResultSet allcveitems=null;//存放根据cve编号查出的所有对应cve条目
        		String sql1="SELECT DISTINCT * FROM "+tablename+" WHERE cve_id='"+cve_idstr+"'";//查cve编号对应的所有条目
        		Statement statement1=con.createStatement();
        		allcveitems=statement1.executeQuery(sql1);//执行sql语句
        		while(allcveitems.next()){
            		JSONObject item=new JSONObject();
        			String proname=allcveitems.getString(3);String vendor=allcveitems.getString(4);String version=allcveitems.getString(5);
        			item.put("pro_name", proname);item.put("vendor", vendor);item.put("version", version);
        			System.out.println(proname);
        			proitems.put(item);
        		}
        		existvs=searchexist(proitems,databaseurl);
        		String rate=calculaterate(databaseurl);
        		undercve.put("rate", rate);
                undercve.put("exist", existvs);
        		undercve.put("items", proitems);
        	}
    	}catch(ClassNotFoundException | SQLException e){
    		e.printStackTrace();
    	}
    	return undercve;
    }
    
    public static JSONObject remarkandgoon(String manager,String cve_year,String cve_id,String databaseurl){//行为1
    	String done="done";
    	String nu="null";
    	JSONObject undercve=new JSONObject();//一共四个key：cve_num,reference,exist,items
    	JSONArray proitems=new JSONArray();//装载cve下所有项目所有版本的信息的数组
    	JSONArray existvs=new JSONArray();//装载索引库里已有的版本信息
    	ResultSet rs=null;
    	try{
    		Class.forName(driver);
    		con=DriverManager.getConnection(databaseurl,user,password);
    		if(!con.isClosed())
    			System.out.println("Succeeded connecting to the Database~1");
    		Statement statement1=con.createStatement();Statement statement2=con.createStatement();
    		String sql1="update "+tablename+" set tag='"+done+"',manager='"+manager+"' where cve_id='"+cve_id+"'";//换标签
    		statement1.executeUpdate(sql1);
    		String sql2="SELECT * FROM "+tablename+" WHERE tag='"+nu+"' and closeSource='"+nu+"' order by cve_year desc,cve_id desc limit 1";
    		rs=statement2.executeQuery(sql2);
    		while(rs.next()){
        		int cve_yearnew=rs.getInt(1); 
        		int cve_idnew=rs.getInt(2);
        		String cve_idnewstr=rs.getString(2);
        		String description=rs.getString(11);
        		String cve_num="cve-"+String.valueOf(cve_yearnew)+"-"+cve_idnewstr;
        		undercve.put("cve_num", cve_num);//装载cve_num
        		undercve.put("description", description);//装载reference
        		ResultSet allcveitems=null;//存放根据cve编号查出的所有对应cve条目
        		String sql3="SELECT DISTINCT * FROM "+tablename+" WHERE cve_id='"+cve_idnewstr+"'";//查cve编号对应的所有条目
        		String sql4="UPDATE "+tablename+" SET tag='"+manager+"' WHERE cve_id='"+cve_idnewstr+"'";//把cve编号对应所有条目打nametag
        		Statement statement3=con.createStatement();Statement statement4=con.createStatement();
        		allcveitems=statement3.executeQuery(sql3);statement4.executeUpdate(sql4);//执行sql语句
        		
        		while(allcveitems.next()){
            		JSONObject item=new JSONObject();
        			String proname=allcveitems.getString(3);String vendor=allcveitems.getString(4);String version=allcveitems.getString(5);
        			item.put("pro_name", proname);item.put("vendor", vendor);item.put("version", version);
        			System.out.println(proname);
        			proitems.put(item);
        		}
        		existvs=searchexist(proitems,databaseurl);
        		String rate=calculaterate(databaseurl);
        		undercve.put("rate", rate);
                undercve.put("exist", existvs);
        		undercve.put("items", proitems);
    		}
    	}catch(ClassNotFoundException | SQLException e){
    		e.printStackTrace();
//    		System.out.println("Sorry,can`t find the Driver!~1");
    	}
    	return undercve;
    }
    
    public static JSONArray searchexist(JSONArray proiteml,String databaseurl){//如果索引库里有，则加入json数组
    	JSONArray proitem=proiteml;
    	JSONArray existproitems=new JSONArray();
    	try {
			Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	Statement statement=con.createStatement();
	    	for(int i=0;i<proitem.length();i++){
	    		JSONObject item=new JSONObject();
	    		item=proitem.getJSONObject(i);
	    		String proname=item.getString("pro_name");
	    		String version=item.getString("version");
	    		String sqlv="select * from nvdindex_database where project_name='"+proname+"' and version='"+version+"'";
	    		ResultSet rs=statement.executeQuery(sqlv);
	    		if(rs.next()){
	    			existproitems.put(item);
	    		}
	    	}
		}catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();

		}
    	return existproitems;
    }
    public static String calculaterate(String databaseurl) {//计算任务完成度
    	List<String> alllist=new ArrayList<String>();List<String> completedlist=new ArrayList<String>();
    	String rate=null,nu="null";int items2016=92746,completednum=0;
		try {
			Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	Statement statement=con.createStatement();
	    	String sql1="select * from "+tablename+" where manager!='"+nu+"'";
	    	rs=statement.executeQuery(sql1);
	    	while(rs.next()) {
	    		completednum++;
	    	}
	    	NumberFormat numberformat=NumberFormat.getInstance();
	    	numberformat.setMaximumFractionDigits(2);
	    	rate=numberformat.format((float)completednum/(float)items2016*100);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rate;
    }
    
    public static String tagcloseSource(String manager,String proname,String vendor,String databaseurl,String ip){//行为2
    	String flag = "failed";
    	String closeSource=manager;
    	try {
			Class.forName(driver);
	    	con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~2");
	    	Statement statement=con.createStatement();
        	String sql="UPDATE "+tablename+" SET closeSource='"+closeSource+"',manager='"+manager+"' WHERE project_name='"+proname+"' and vendor='"+vendor+"'";
        	statement.executeUpdate(sql);
        	flag=proname+":"+"已标记闭源"+"\n";
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			log.info(e);
			log.error(e+":"+manager+":"+proname+":"+"判断闭源");
		}
    	
    	return flag;
    }
    
    public static String tagNFound(String manager,String proname,String version,String databaseurl,String ip){//行为3
    	String flag = "failed";
    	String notfound="Y";
    	try {
			Class.forName(driver);
	    	con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~3");
	    	Statement statement=con.createStatement();
        	String sql="UPDATE "+tablename+" SET notfound='"+notfound+"' WHERE project_name='"+proname+"' and version='"+version+"'";
        	statement.executeUpdate(sql);
        	flag=proname+":"+"已标记未找到"+"\n"+"itsoverhaha";
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			log.info(e);
			log.error(e+":"+manager+":"+proname+":"+"判断未找到");
		}
    	return flag;
    }
    
    /**行为4，先检查库里有没有，若没有则下载
     * @param manager
     * @param rootpath
     * @param cvenum
     * @param proname
     * @param vendor
     * @param version
     * @param downloadurl
     * @param fileformat
     * @param databaseurl
     * @return
     */
    public static String insertanddown(String manager,String rootpath,String cvenum,String proname,String vendor,String version,String downloadurl,String filepath,String fileformat,String date,String databaseurl){
    	String flag = "failed";
    	String path=filepath.replace("\\","\\\\");
    	try {
			Class.forName(driver);
	    	con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~4");
	    	Statement statement1=con.createStatement(),statement2=con.createStatement(),statement3=con.createStatement();
	    	String procheck="select * from nvdindex_database where project_name='"+proname+"' and version='"+version+"'";
	    	ResultSet rs=statement1.executeQuery(procheck);
	    	if(rs.first()){
	    		String deletesql="DELETE FROM nvdindex_database WHERE project_name='"+proname+"' and version='"+version+"'";
		    	String insertsql="INSERT INTO nvdindex_database VALUES ('"+cvenum+"','"+proname+"','"+vendor+"','"+version+"','"+fileformat+"','"+downloadurl+"','"+manager+"','"+path+"','"+date+"','"+null+"','"+null+"')";
	    		statement2.executeUpdate(deletesql);
	    		statement3.executeUpdate(insertsql);
	    		flag="downloading..."+version;
	    	}else{
		    	String insertsql="INSERT INTO nvdindex_database VALUES ('"+cvenum+"','"+proname+"','"+vendor+"','"+version+"','"+fileformat+"','"+downloadurl+"','"+manager+"','"+path+"','"+date+"','"+null+"','"+null+"')";
		    	statement3.executeUpdate(insertsql);
		    	flag="downloading..."+version;
	    	}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	return flag;
    }
    
    public static String insertProblemitem(String manager,String cvenum,String proname,String version,String reason,String date,String databaseurl,String ip){
    	String flag = "failed";
    	String problem="problem";
    	try {
			Class.forName(driver);
	    	con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~5");
	    	Statement statement1=con.createStatement(),statement2=con.createStatement();
	    	String procheck="select * from nvdindex_database where project_name='"+proname+"' and version='"+version+"'";
	    	ResultSet rs=statement1.executeQuery(procheck);
	    	if(rs.first()){
	    		flag=version+" of project:"+proname+" is already existed";
	    	}else{
		    	String insertsql="INSERT INTO nvdindex_database VALUES ('"+cvenum+"','"+proname+"','"+null+"','"+version+"','"+null+"','"+null+"','"+manager+"','"+null+"','"+date+"','"+problem+"','"+reason+"')";
		    	statement2.executeUpdate(insertsql);
		    	flag=proname+":"+"已插入问题条目"+"\n"+"itsoverhaha";
	    	}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	return flag;
    }
    
    public static String searchversion(String proname,String databaseurl){//行为6,查询
    	StringBuilder versionsb=new StringBuilder();
    	String sampurl="";
    	String vs="";
    	try {
			Class.forName(driver);
			con=DriverManager.getConnection(databaseurl,user,password);
	    	if(!con.isClosed())
	    		System.out.println("Succeeded connecting to the Database~6");
	    	Statement statement1=con.createStatement();
	    	Statement statement2=con.createStatement();
	    	String sqlv="select version from nvdindex_database where project_name='"+proname+"'";
	    	ResultSet rs=statement1.executeQuery(sqlv);
	    	while(rs.next()){
	    		String version=rs.getString(1);
	    		versionsb.append(version+"   "+"@");
	    	}
	    	String sqld="select download_url from nvdindex_database where project_name='"+proname+"'";
	    	ResultSet rs1=statement2.executeQuery(sqld);
	    	while(rs1.next()){
	    		sampurl=rs1.getString(1);
	    	}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();

		}
    	String versions=versionsb.toString();
    	vs=sampurl+"#"+versions;
    	return vs;
    }
    
	
	
}
