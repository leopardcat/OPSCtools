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
	//����Connection����
    public static Connection con = null;
    //����Statement����
    private static Statement st=null;
    //����Result��
    private static ResultSet rs=null;
    //����������
    private static String driver = "com.mysql.jdbc.Driver";

    //MySQL����ʱ���û���
    private static String user = "root";
    //MySQL����ʱ������
    private static String password = "123456";
    static Logger log=Logger.getLogger(Make_task.class);
    private static String tablename="cve_info_2014";

	
    public static JSONObject getCVEitems(String manager,String databaseurl){//��Ϊ0�������ݱ���ѡ�������һ��û��ѡ������Ŀ
    	String nu="null";
    	JSONObject undercve=new JSONObject();//һ���ĸ�key��cve_num,reference,exist,items
    	JSONArray proitems=new JSONArray();//װ��cve��������Ŀ���а汾����Ϣ������
    	JSONArray existvs=new JSONArray();//װ������������û����Ŀ�Ͷ�Ӧ�汾��
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
        		undercve.put("cve_num", cve_num);//װ��cve_num
        		undercve.put("description", description);//װ��reference
        		ResultSet allcveitems=null;//��Ÿ���cve��Ų�������ж�Ӧcve��Ŀ
        		String sql1="SELECT DISTINCT * FROM "+tablename+" WHERE cve_id='"+cve_idstr+"'";//��cve��Ŷ�Ӧ��������Ŀ
        		Statement statement1=con.createStatement();
        		allcveitems=statement1.executeQuery(sql1);//ִ��sql���
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
    
    public static JSONObject remarkandgoon(String manager,String cve_year,String cve_id,String databaseurl){//��Ϊ1
    	String done="done";
    	String nu="null";
    	JSONObject undercve=new JSONObject();//һ���ĸ�key��cve_num,reference,exist,items
    	JSONArray proitems=new JSONArray();//װ��cve��������Ŀ���а汾����Ϣ������
    	JSONArray existvs=new JSONArray();//װ�������������еİ汾��Ϣ
    	ResultSet rs=null;
    	try{
    		Class.forName(driver);
    		con=DriverManager.getConnection(databaseurl,user,password);
    		if(!con.isClosed())
    			System.out.println("Succeeded connecting to the Database~1");
    		Statement statement1=con.createStatement();Statement statement2=con.createStatement();
    		String sql1="update "+tablename+" set tag='"+done+"',manager='"+manager+"' where cve_id='"+cve_id+"'";//����ǩ
    		statement1.executeUpdate(sql1);
    		String sql2="SELECT * FROM "+tablename+" WHERE tag='"+nu+"' and closeSource='"+nu+"' order by cve_year desc,cve_id desc limit 1";
    		rs=statement2.executeQuery(sql2);
    		while(rs.next()){
        		int cve_yearnew=rs.getInt(1); 
        		int cve_idnew=rs.getInt(2);
        		String cve_idnewstr=rs.getString(2);
        		String description=rs.getString(11);
        		String cve_num="cve-"+String.valueOf(cve_yearnew)+"-"+cve_idnewstr;
        		undercve.put("cve_num", cve_num);//װ��cve_num
        		undercve.put("description", description);//װ��reference
        		ResultSet allcveitems=null;//��Ÿ���cve��Ų�������ж�Ӧcve��Ŀ
        		String sql3="SELECT DISTINCT * FROM "+tablename+" WHERE cve_id='"+cve_idnewstr+"'";//��cve��Ŷ�Ӧ��������Ŀ
        		String sql4="UPDATE "+tablename+" SET tag='"+manager+"' WHERE cve_id='"+cve_idnewstr+"'";//��cve��Ŷ�Ӧ������Ŀ��nametag
        		Statement statement3=con.createStatement();Statement statement4=con.createStatement();
        		allcveitems=statement3.executeQuery(sql3);statement4.executeUpdate(sql4);//ִ��sql���
        		
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
    
    public static JSONArray searchexist(JSONArray proiteml,String databaseurl){//������������У������json����
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
    public static String calculaterate(String databaseurl) {//����������ɶ�
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
    
    public static String tagcloseSource(String manager,String proname,String vendor,String databaseurl,String ip){//��Ϊ2
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
        	flag=proname+":"+"�ѱ�Ǳ�Դ"+"\n";
		} catch (ClassNotFoundException | SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			log.info(e);
			log.error(e+":"+manager+":"+proname+":"+"�жϱ�Դ");
		}
    	
    	return flag;
    }
    
    public static String tagNFound(String manager,String proname,String version,String databaseurl,String ip){//��Ϊ3
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
        	flag=proname+":"+"�ѱ��δ�ҵ�"+"\n"+"itsoverhaha";
		} catch (ClassNotFoundException | SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			log.info(e);
			log.error(e+":"+manager+":"+proname+":"+"�ж�δ�ҵ�");
		}
    	return flag;
    }
    
    /**��Ϊ4���ȼ�������û�У���û��������
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
			// TODO �Զ����ɵ� catch ��
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
		    	flag=proname+":"+"�Ѳ���������Ŀ"+"\n"+"itsoverhaha";
	    	}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
    	return flag;
    }
    
    public static String searchversion(String proname,String databaseurl){//��Ϊ6,��ѯ
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();

		}
    	String versions=versionsb.toString();
    	vs=sampurl+"#"+versions;
    	return vs;
    }
    
	
	
}
