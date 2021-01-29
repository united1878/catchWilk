package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DTO.p1DTO;
import DTO.p2DTO;
import list.timeRank;

public class gameGUI extends JFrame implements ActionListener, KeyListener{
	
	//	�̹���
	private Image background = new ImageIcon(this.getClass().getResource("../img/start.png")).getImage();
	private File startIcon = new File("./src/img/startBtn.png");
	
	//��, �ؽ�Ʈ�ʵ�, �����̳�
	private JTextField input1P = new JTextField(); ;
	private JTextField input2P = new JTextField();
	private JButton startBtn = new JButton(new ImageIcon(startIcon.getAbsolutePath()));
	private JPanel startPanel = new JPanel();
	private JLabel scondLabel = new JLabel(); //�ʼ���
	
	//��ü
	
	private printPlayers pp = null; // ���� ĵ����
	private GameRoom gr = null;
	private Key key = null;
	private File file = new File("./src/sound/ingameBGM.wav");
	
	//����
	boolean itemStart = false;
	
	//	sound
	AudioInputStream ais;
	Clip clip;
	
	
	gameGUI(GameRoom gr,printPlayers pp){
		this.pp=pp;
		this.gr=gr;
		addLis();
		setFrame();
		startFrame(); //	ù ���� ȭ��
//		this.requestFocus();
//		this.addKeyListener(new Key(gr,this)); // �� ������Ʈ�� �̺�Ʈ�� ���� �� �ֵ��� �Ѵ�. 
	}


	private void setFrame() {
		this.setBounds(270,10,800,700);
		
		this.setUndecorated(true);
		this.getContentPane().setLayout(new java.awt.BorderLayout(0,0));
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		
	}
	
	public void startFrame() {
		
		startPanel.setBackground(new Color(255,0,0,0));
		startPanel.setBounds(10, 30, 600, 600);
		this.add(startPanel, BorderLayout.CENTER);
		startPanel.setLayout(null);
		
		//	input�� ����ִ� panel
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(200, 235, 400, 200);
		startPanel.add(panel_1);
		panel_1.setLayout(null);
		
		//	input 1P / 2P
		input1P.setBounds(160, 36, 190, 30);
		panel_1.add(input1P);
		input1P.setColumns(20);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 0, 346, 300);
		startPanel.add(panel_2);
		panel_2.setLayout(null);
		
		input2P.setBounds(160, 108, 190, 30);
		panel_1.add(input2P);
		input2P.setColumns(20);
		
		//	�׽��� ��ư
		startBtn.setBounds(365, 480, 80, 80);
		startPanel.setBackground(new Color(255,0,0,0));
		startPanel.setOpaque(false);
		startBtn.setBorderPainted(false);
		startBtn.setContentAreaFilled(false);
		startBtn.setFocusPainted(false);
		startBtn.setOpaque(false);
		startPanel.add(startBtn);
	}
	
	private void inGame() {
		// 3,2,1 ���� �κ��� ������Ʈ�� �ʿ��ϴ� printPlayers ���� �ϴ� ������... ���� ���� ����..
		this.remove(startPanel);
		this.add(pp,"Center");
		pp.requestFocus();	// �� ������Ʈ�� �̺�Ʈ�� ���� �� �ֵ��� �Ѵ�. 
		pp.addKeyListener(new Key(gr,this,pp));  // pp�� �̺�Ʈ�� �޾ƾ��ϹǷ�. �߿�!!!
		pp.ingameBGM();
		this.setVisible(true);
		itemStart = true;
	}
	
	public void paint(Graphics g) {
		g.drawImage(background, 0,0,null);
	}
	
	public void addLis() {
		startBtn.addActionListener(this);
		this.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(startBtn)) {
			// �г����� �޾Ƽ� 1p, 2p ��ü�� �Ѱ��ֱ�
			boolean p1_chk = false; // �̸��� �ԷµǾ����� üũ 
			boolean p2_chk = false;
			
			gr.p1 = new p1DTO();
			if(input1P.getText().equals("")) { 	//	�ؽ�Ʈ�ʵ忡 �ƹ��͵� �ԷµǾ� ���� ������ null�� �ƴ� ""
				System.out.println("�÷��̾�1�� �̸��� �Է��ϼ���.");
			}else {
				gr.p1.setName(input1P.getText());
				p1_chk=true;
			}
			
			gr.p2 = new p2DTO();
			if(input2P.getText().equals("")) {
				System.out.println("�÷��̾�1�� �̸��� �Է��ϼ���.");
			}else {
				gr.p2.setName(input2P.getText());
				p2_chk=true;
			}
			
			if(p1_chk&&p2_chk) {
				System.out.println("���� ��~~��!");
//				key = new Key(gr);
				inGame(); //	�׽���
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// 	ESC ���� �� ����
		if(e.getKeyCode()==27) {
	    	  System.exit(0);
	      }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void AfterGame(printOver po) {
		this.remove(pp);
		System.out.println("����ġ��");
		this.add(po,"Center");
		this.setVisible(true);
		System.out.println("�ǿ��߰�");
	}
}
