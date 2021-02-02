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
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import DTO.p1DTO;
import DTO.p2DTO;
import list.Ranking;

public class gameGUI extends JFrame implements ActionListener, KeyListener{
	
	//	�̹���
	private Image howto = new ImageIcon(this.getClass().getResource("../img/howto.png")).getImage();
	private Image background = new ImageIcon(this.getClass().getResource("../img/start.png")).getImage();
	private File startIcon = new File("./src/img/startBtn.png");
	
	//��, �ؽ�Ʈ�ʵ�, �����̳�
	private JTextField input1P = new JTextField(); 
	private JTextField input2P = new JTextField();
	private JButton startBtn = new JButton(new ImageIcon(startIcon.getAbsolutePath()));
	private JPanel startPanel = new JPanel();
	
	public JPanel after = new JPanel();
	private JTextField search = new JTextField();
	private JButton searchBtn = new JButton("�˻�");
	
	//��ü
	
	private printPlayers pp = null; // ���� ĵ����
	private GameRoom gr = null;
	private File file = new File("./src/sound/ingameBGM.wav");
	private count c = null;
	private rule r = null;
	
	//����
	boolean itemStart = false;
	boolean btn = false;
	boolean gameOn = false;
	
	//	sound
	private File startSnd = new File("./src/sound/startSound.wav");
	private File enterSnd = new File("./src/sound/enterSound.wav");
	AudioInputStream ais;
	Clip clip;
	AudioInputStream aisE;
	public Clip clipE;
	
	
	gameGUI(GameRoom gr,printPlayers pp){
		this.pp=pp;
		this.gr=gr;
		setFrame();
		startBGM();
		startFrame(); //	ù ���� ȭ��
	}


	private void setFrame() {
		this.setBounds(270,10,800,700);
		this.addLis();
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
		addLis();
		
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
	
	public void inGame() {
		// 3,2,1 ���� �κ��� ������Ʈ�� �ʿ��ϴ� printPlayers ���� �ϴ� ������... ���� ���� ����..
		this.remove(c);
		itemStart = true;
		this.add(pp,"Center");
		pp.requestFocus();	// �� ������Ʈ�� �̺�Ʈ�� ���� �� �ֵ��� �Ѵ�. 
		pp.addKeyListener(new Key(gr,this,pp));  // pp�� �̺�Ʈ�� �޾ƾ��ϹǷ�. �߿�!!!
		pp.ingameBGM();
//		clip.stop();
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		g.drawImage(background, 0,0,null);
	}
	
	public void addLis() {
		startBtn.addActionListener(this);
		this.addKeyListener(this);
		input1P.addKeyListener(this);
		input2P.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(startBtn)) {
			clickBtn();
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
	      }else if(e.getKeyCode()==13) { //	����Ű ������
	    	  clickBtn();
		}else if(e.getKeyCode()==10) {
			clickBtn();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/*���ӿ���ȭ��*/
	public void AfterGame(printOver po) {
		this.remove(pp);
		this.add(po,"Center");
		this.setVisible(true);
	}
	
	/*��ŷȭ�� */
	public void showRank(printOver po,Ranking rk) {
		this.remove(po);
		this.add(rk);
		this.setVisible(true);
	}
	
	/*ī��Ʈȭ��*/
	public void showCnt() {
		clip.stop();
		this.remove(r);
		c = new count(this);
		this.add(c,"Center");
		this.setVisible(true);
	}
	public void enterSound() {
		try {
			aisE = AudioSystem.getAudioInputStream(enterSnd);
			clipE = AudioSystem.getClip();
			clipE.open(aisE);
			clipE.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startBGM() {
		try {
			if(ais==null) {
				ais = AudioSystem.getAudioInputStream(startSnd);
				clip = AudioSystem.getClip();
				clip.open(ais);
				clip.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*���ӷ�ȭ��*/
	private void showRule() {
		this.remove(startPanel);
		r = new rule(this);
		this.add(r);
		this.setVisible(true);
	}
	
	private void clickBtn() {
	// �г����� �޾Ƽ� 1p, 2p ��ü�� �Ѱ��ֱ�
		boolean p1_chk = false; // �̸��� �ԷµǾ����� üũ 
		boolean p2_chk = false;
		
//		gr.p1 = new p1DTO();
		gr.p1 = p1DTO.getInstance();	// �̱���
		if(input1P.getText().equals("")) { 	//	�ؽ�Ʈ�ʵ忡 �ƹ��͵� �ԷµǾ� ���� ������ null�� �ƴ� ""
			System.out.println("�÷��̾�1�� �̸��� �Է��ϼ���.");
			JOptionPane.showMessageDialog(null, "�÷��̾�1�� �̸��� �Է��ϼ���.");
		}else {
			gr.p1.setName(input1P.getText());
			p1_chk=true;
		}
		
		gr.p2 = new p2DTO();
		if(input2P.getText().equals("")) {
			System.out.println("�÷��̾�1�� �̸��� �Է��ϼ���.");
			JOptionPane.showMessageDialog(null, "�÷��̾�2�� �̸��� �Է��ϼ���.");
		}else {
			gr.p2.setName(input2P.getText());
			p2_chk=true;
		}
		
		if(p1_chk&&p2_chk) {
			if(gameOn==false) {
				enterSound();
				System.out.println("���� ��~~��!");
				gameOn=true;
				showRule();
			}
		}
	}
}
