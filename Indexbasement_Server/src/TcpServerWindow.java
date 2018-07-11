import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerWindow extends JFrame {

	private JPanel contentPane;
	private JTextField manager;
	private static String managerwyl="Œ‚”Í¬◊";
	private static String managerget;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerSocket ss=new ServerSocket(20001);
					while(true){
						Socket s=ss.accept();
						new Thread(new ClientThread(s)).start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TcpServerWindow() {
		setWindowIcon();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		manager = new JTextField();
		manager.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				managerget=manager.getText();
			}
		});
		manager.setBounds(10, 10, 77, 21);
		panel.add(manager);
		manager.setColumns(10);
		
	}

	public void setWindowIcon()  
    {  
        ImageIcon imageIcon = new ImageIcon("images/service.jpg");  
        this.setIconImage(imageIcon.getImage());  
    } 
}
