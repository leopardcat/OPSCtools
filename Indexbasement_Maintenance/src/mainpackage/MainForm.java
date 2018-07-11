package mainpackage;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Event;

import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentListener;

public class MainForm extends JFrame{

	private JPanel contentPane;
	private JTextField manager;
	private JTextField CVEnum1;
	private JTextField pronametext;
	private JTextField vendortext;
	private JTextField versiontext;
	private JTextField downloadtext;
	private JTextField fileformaltext;
	private JTextField reasontext;
	private JTextField iptext;
	private JTextField ratetext;
	private JTextField sampletext;
	private JTextArea proitem;
	private static JTextArea completeitem;
	private static JTextArea description;
	private JTextArea textArea;
	private JCheckBox OorNcheck;
	private JCheckBox GorNcheck;
	private JCheckBox Judgeablecheck;
	public static String Ipaddress;
	public static Unexisted unex=new Unexisted();//for autofilling those blank
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JScrollPane sp1;
	private JScrollPane sp2;
	private JScrollPane sp3;
	private JScrollPane sp4;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm frame = new MainForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainForm() {
		super("漏洞索引库维护软件"); 
		setTitle("\u6F0F\u6D1E\u7D22\u5F15\u5E93\u7EF4\u62A4\u8F6F\u4EF6");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1451, 746);
		contentPane = new JPanel();
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		java.awt.Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if(((KeyEvent)event).getKeyCode()==Event.F1) {
					FuncClass.autofill(unex, pronametext, vendortext, versiontext,sampletext,textArea,downloadtext,fileformaltext);
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
		
		JLabel label = new JLabel("\u8BF7\u8F93\u5165\u59D3\u540D");
		label.setFont(new Font("楷体", Font.PLAIN, 15));
		label.setBounds(652, 16, 78, 24);
		contentPane.add(label);
		
		manager = new JTextField();
		manager.addKeyListener(new KeyAdapter() {//按回车获取今日任务
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==Event.ENTER) {
					Ipaddress=iptext.getText();
					String manname=manager.getText();
					if(!manname.equals("")) {
						String write="0"+"@"+manname+"\n"+"itsoverhaha";
						Thread mth=new Thread(new Mythread(0,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
						mth.start();
					}
				}
			}
		});
		manager.setBounds(740, 18, 78, 21);
		contentPane.add(manager);
		manager.setColumns(10);
		
		JButton gettask = new JButton("\u9886\u53D6\u4ECA\u65E5\u4EFB\u52A1");
		gettask.addActionListener(new ActionListener() {//点击获取今日任务
			public void actionPerformed(ActionEvent e) {
				Ipaddress=iptext.getText();
				String manname=manager.getText();
				if(!manname.equals("")) {
					String write="0"+"@"+manname+"\n"+"itsoverhaha";
					Thread mth=new Thread(new Mythread(0,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
					mth.start();
				}
			}
		});
		gettask.setFont(new Font("宋体", Font.PLAIN, 10));
		gettask.setBounds(828, 19, 102, 21);
		contentPane.add(gettask);
		
		JLabel lblcve = new JLabel("NEXT CVE ITEM:");
		lblcve.setFont(new Font("楷体", Font.PLAIN, 15));
		lblcve.setBounds(10, 27, 112, 24);
		contentPane.add(lblcve);
		
		CVEnum1 = new JTextField();
		CVEnum1.setEditable(false);
		CVEnum1.setColumns(10);
		CVEnum1.setBounds(10, 61, 189, 25);
		contentPane.add(CVEnum1);
		
		proitem = new JTextArea();
		proitem.setEditable(false);
		proitem.setFont(new Font("华文楷体", Font.PLAIN, 15));
		proitem.setBounds(10, 104, 559, 696);
		sp1=new JScrollPane(proitem);
		getContentPane().add(sp1);
		sp1.setBounds(10, 104, 669, 415);
		sp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JButton nextCVEitem = new JButton("\u4E0B\u4E00\u4E2Acve\u6761\u76EE");
		nextCVEitem.setFont(new Font("宋体", Font.PLAIN, 10));
		nextCVEitem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!(manager.getText().toString().equals("")||CVEnum1.getText().toString().equals(""))) {
					Ipaddress=iptext.getText();
				String write="1"+"@"+manager.getText()+"\n"+"@"+CVEnum1.getText()+"\n"+"itsoverhaha";
				Thread mth=new Thread(new Mythread(1,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
				mth.start();
				}else {JOptionPane.showMessageDialog(contentPane, "请先填写姓名并获取最新任务");}
			}
		});
		nextCVEitem.setBounds(202, 61, 101, 25);
		contentPane.add(nextCVEitem);
		
		JButton completebtn = new JButton("\u5B8C\u6210\u672C\u6B21\u52A8\u4F5C");
		completebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FuncClass.completeitem(contentPane,OorNcheck,GorNcheck,Judgeablecheck,manager,
						CVEnum1,pronametext,vendortext,versiontext,downloadtext,fileformaltext,reasontext,completeitem,ratetext,proitem,sampletext,textArea);
			}
		});
		completebtn.setFont(new Font("楷体", Font.PLAIN, 13));
		completebtn.setBounds(918, 650, 220, 47);
		contentPane.add(completebtn);
		
		JLabel label_1 = new JLabel("\u5236\u4F5C\u65B0\u6761\u76EE\uFF1A");
		label_1.setFont(new Font("楷体", Font.PLAIN, 15));
		label_1.setBounds(689, 118, 90, 24);
		contentPane.add(label_1);
		
		JLabel label_2 = new JLabel("\u9879\u76EE\u540D");
		label_2.setFont(new Font("楷体", Font.PLAIN, 15));
		label_2.setBounds(689, 154, 90, 24);
		contentPane.add(label_2);
		
		pronametext = new JTextField();
		pronametext.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==Event.ENTER) {
					String write="6"+"@"+pronametext.getText()+"\n"+"itsoverhaha";
					EventQueue.invokeLater(new Mythread(6,write,CVEnum1,ratetext,proitem,completeitem,sampletext,textArea));
			}}});
		pronametext.setBounds(789, 156, 276, 21);
		contentPane.add(pronametext);
		pronametext.setColumns(10);
		
		JLabel label_3 = new JLabel("\u4F9B\u5E94\u5546");
		label_3.setFont(new Font("楷体", Font.PLAIN, 15));
		label_3.setBounds(697, 450, 90, 24);
		contentPane.add(label_3);
		
		vendortext = new JTextField();
		vendortext.setColumns(10);
		vendortext.setBounds(789, 452, 276, 21);
		contentPane.add(vendortext);
		
		OorNcheck = new JCheckBox("\u662F\u5426\u5F00\u6E90");
		OorNcheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Object obj=e.getItem();
				FuncClass.settextbox(obj,OorNcheck,OorNcheck,GorNcheck,Judgeablecheck,vendortext,versiontext,downloadtext,fileformaltext,reasontext);
			}
		});
		OorNcheck.setFont(new Font("楷体", Font.PLAIN, 15));
		OorNcheck.setBounds(774, 418, 103, 23);
		contentPane.add(OorNcheck);
		
		GorNcheck = new JCheckBox("\u662F\u5426\u627E\u5230\u6765\u6E90");
		GorNcheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Object obj=e.getItem();
				FuncClass.settextbox(obj,GorNcheck,OorNcheck,GorNcheck,Judgeablecheck,vendortext,versiontext,downloadtext,fileformaltext,reasontext);
			}
		});
		GorNcheck.setFont(new Font("楷体", Font.PLAIN, 15));
		GorNcheck.setBounds(887, 418, 121, 23);
		contentPane.add(GorNcheck);
		
		Judgeablecheck = new JCheckBox("\u65E0\u6CD5\u5224\u65AD");
		Judgeablecheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Object obj=e.getItem();
				if(obj.equals(Judgeablecheck)) {
					FuncClass.settextbox(obj,Judgeablecheck,OorNcheck,GorNcheck,Judgeablecheck,vendortext,versiontext,downloadtext,fileformaltext,reasontext);
				}
			}
		});
		Judgeablecheck.setFont(new Font("楷体", Font.PLAIN, 15));
		Judgeablecheck.setBounds(1019, 418, 103, 23);
		contentPane.add(Judgeablecheck);
		
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(679, 263, 251, 120);
		contentPane.add(textArea);
		sp3=new JScrollPane(textArea);
		getContentPane().add(sp3);
		sp3.setBounds(789, 267, 251, 120);
		sp3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel label_4 = new JLabel("\u7D22\u5F15\u5E93\u4E2D\u5DF2\u6709");
		label_4.setFont(new Font("楷体", Font.PLAIN, 15));
		label_4.setBounds(689, 311, 90, 24);
		contentPane.add(label_4);
		
		JLabel label_5 = new JLabel("\u7248\u672C\u53F7");
		label_5.setFont(new Font("楷体", Font.PLAIN, 15));
		label_5.setBounds(697, 484, 90, 24);
		contentPane.add(label_5);
		
		versiontext = new JTextField();
		versiontext.setEditable(false);
		versiontext.setColumns(10);
		versiontext.setBounds(789, 486, 276, 21);
		contentPane.add(versiontext);
		
		JLabel label_6 = new JLabel("\u4E0B\u8F7D\u5730\u5740");
		label_6.setFont(new Font("楷体", Font.PLAIN, 15));
		label_6.setBounds(697, 518, 90, 24);
		contentPane.add(label_6);
		
		downloadtext = new JTextField();
		downloadtext.setEditable(false);
		downloadtext.setColumns(10);
		downloadtext.setBounds(789, 520, 276, 21);
		contentPane.add(downloadtext);
		Document document = downloadtext.getDocument();
		
		JLabel label_7 = new JLabel("\u6587\u4EF6\u683C\u5F0F");
		label_7.setFont(new Font("楷体", Font.PLAIN, 15));
		label_7.setBounds(697, 552, 90, 24);
		contentPane.add(label_7);
		
		fileformaltext = new JTextField();
		fileformaltext.setEditable(false);
		fileformaltext.setColumns(10);
		fileformaltext.setBounds(789, 554, 276, 21);
		contentPane.add(fileformaltext);
		
		JRadioButton rdbtnzip = new JRadioButton(".zip");
		rdbtnzip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileformaltext.setText(rdbtnzip.getText());
			}
		});
		buttonGroup.add(rdbtnzip);
		rdbtnzip.setBounds(815, 590, 57, 23);
		contentPane.add(rdbtnzip);
		
		JRadioButton rdbtntar = new JRadioButton(".tar.gz");
		rdbtntar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileformaltext.setText(rdbtntar.getText());
			}
		});
		buttonGroup.add(rdbtntar);
		rdbtntar.setBounds(874, 590, 67, 23);
		contentPane.add(rdbtntar);
		
		JRadioButton rdbtn7zip = new JRadioButton(".tar.xz");
		rdbtn7zip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileformaltext.setText(rdbtn7zip.getText());
			}
		});
		buttonGroup.add(rdbtn7zip);
		rdbtn7zip.setBounds(943, 590, 78, 23);
		contentPane.add(rdbtn7zip);
		
		JRadioButton rdbtnjar = new JRadioButton(".tar.bz2");
		rdbtnjar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileformaltext.setText(rdbtnjar.getText());
			}
		});
		buttonGroup.add(rdbtnjar);
		rdbtnjar.setBounds(1019, 590, 94, 23);
		contentPane.add(rdbtnjar);
		
		JLabel label_9 = new JLabel("(\u4E00\u5B9A\u8981\u5224\u65AD\u5B8C\u6240\u6709\u6761\u76EE\u518D\u70B9\uFF01\u8BF7\u8D1F\u8D23\uFF01)");
		label_9.setFont(new Font("楷体", Font.PLAIN, 14));
		label_9.setBounds(305, 61, 269, 24);
		contentPane.add(label_9);
		
		JButton makeclear = new JButton("\u6E05\u7A7A");
		makeclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				downloadtext.setText(null);
			}
		});
		makeclear.setFont(new Font("楷体", Font.PLAIN, 13));
		makeclear.setBounds(1066, 520, 72, 21);
		contentPane.add(makeclear);
		
		reasontext = new JTextField();
		reasontext.setEditable(false);
		reasontext.setColumns(10);
		reasontext.setBounds(789, 619, 349, 21);
		contentPane.add(reasontext);
		
		JLabel label_10 = new JLabel("\u539F\u56E0\uFF1A");
		label_10.setFont(new Font("楷体", Font.PLAIN, 15));
		label_10.setBounds(697, 619, 67, 24);
		contentPane.add(label_10);
		
		completeitem = new JTextArea();
		completeitem.setFont(new Font("华文楷体", Font.PLAIN, 15));
		completeitem.setBounds(1030, 104, 444, 712);
		sp2=new JScrollPane(completeitem);
		getContentPane().add(sp2);
		sp2.setBounds(1144, 60, 291, 648);
		sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		
		JLabel label_11 = new JLabel("\u672C\u6B21\u5DF2\u5B8C\u6210\uFF1A");
		label_11.setFont(new Font("楷体", Font.PLAIN, 15));
		label_11.setBounds(1144, 18, 90, 24);
		contentPane.add(label_11);
		
		JLabel label_12 = new JLabel("\u53C2\u8003\u94FE\u63A5");
		label_12.setFont(new Font("楷体", Font.PLAIN, 15));
		label_12.setBounds(689, 200, 90, 24);
		contentPane.add(label_12);
		
		sampletext = new JTextField();
		sampletext.setEditable(false);
		sampletext.setColumns(10);
		sampletext.setBounds(789, 202, 276, 21);
		contentPane.add(sampletext);

		description = new JTextArea();
		description.setFont(new Font("华文楷体", Font.PLAIN, 15));
		description.setBounds(10, 407, 559, 279);
		description.setEditable(false);
		sp4=new JScrollPane(description);
		sp4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sp4.setBounds(10, 529, 669, 157);
		getContentPane().add(sp4);
		
		JLabel label_8 = new JLabel("\u4EFB\u52A1\u5B8C\u6210\u5EA6");
		label_8.setFont(new Font("楷体", Font.PLAIN, 15));
		label_8.setBounds(652, 50, 78, 24);
		contentPane.add(label_8);
		
		ratetext = new JTextField();
		ratetext.setEditable(false);
		ratetext.setColumns(10);
		ratetext.setBounds(740, 49, 84, 21);
		contentPane.add(ratetext);
		
		JLabel lblip = new JLabel("\u670D\u52A1\u5668ip");
		lblip.setFont(new Font("楷体", Font.PLAIN, 15));
		lblip.setBounds(344, 16, 78, 24);
		contentPane.add(lblip);
		
		iptext = new JTextField();
		iptext.setColumns(10);
		iptext.setBounds(424, 18, 145, 21);
		contentPane.add(iptext);
		iptext.setText("162.105.89.210");
		
		
		JButton autofillbtn = new JButton("\u81EA\u52A8\u586B\u5199");
		autofillbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FuncClass.autofill(unex, pronametext, vendortext, versiontext,sampletext,textArea,downloadtext,fileformaltext);
			}
		});
		autofillbtn.setFont(new Font("楷体", Font.PLAIN, 13));
		autofillbtn.setBounds(689, 650, 219, 47);
		contentPane.add(autofillbtn);
		
		JButton copytourlbtn = new JButton("\u590D\u5236\u5230\u4E0B\u8F7D\u5730\u5740\u680F");
		copytourlbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(downloadtext.isEditable()) {
					downloadtext.setText(sampletext.getText().toString());
				}
			}
		});
		copytourlbtn.setFont(new Font("楷体", Font.PLAIN, 14));
		copytourlbtn.setBounds(908, 236, 157, 21);
		contentPane.add(copytourlbtn);
	}
}
