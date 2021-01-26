package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import DTO.p1DTO;
import DTO.p2DTO;

public class gameGUI extends JFrame implements ActionListener{
	
	//��, �ؽ�Ʈ�ʵ�
	private JLabel title = new JLabel("������ ��ũ!!");
	private JTextField input1P = new JTextField(); ;
	private JTextField input2P = new JTextField();
	private JButton startBtn = new JButton("���� ����");
	private JPanel startPanel = new JPanel();
	private JLabel scondLabel = new JLabel(); //�ʼ���
	
	//��ü
	
	private printPlayers pp = null; // ���� ĵ����
	private GameRoom gr = null;
	private Key key = null;
	
	//����
	boolean itemStart = false;
	
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
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setBackground(new Color(64, 64, 64));
		title.setForeground(Color.RED);
		title.setFont(new Font("���Ĺ���", Font.BOLD, 31));
		this.add(title, BorderLayout.NORTH);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
	}
	
	public void startFrame() {
		
		startPanel.setBackground(SystemColor.textHighlight);
		this.add(startPanel, BorderLayout.CENTER);
		startPanel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(219, 201, 346, 24);
		startPanel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel player1Lbl = new JLabel("�÷��̾�1�� �̸� ");
		player1Lbl.setBounds(0, 0, 170, 24);
		panel_1.add(player1Lbl);
		player1Lbl.setFont(new Font("����ü", Font.PLAIN, 20));
		
		
		input1P.setBounds(175, 1, 171, 21);
		panel_1.add(input1P);
		input1P.setColumns(15);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(219, 300, 346, 25);
		startPanel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel player1Lbl_2 = new JLabel("�÷��̾�2�� �̸�");
		player1Lbl_2.setBounds(0, 0, 160, 24);
		panel_2.add(player1Lbl_2);
		player1Lbl_2.setFont(new Font("����ü", Font.PLAIN, 20));
		
		input2P.setBounds(175, 4, 171, 21);
		panel_2.add(input2P);
		input2P.setColumns(15);
		
		
		startBtn.setFont(new Font("11�Ե���Ʈ�帲Bold", Font.PLAIN, 32));
		startBtn.setBounds(293, 441, 197, 80);
		startPanel.add(startBtn);
	}
	
	private void inGame() {
		// 3,2,1 ���� �κ��� ������Ʈ�� �ʿ��ϴ� printPlayers ���� �ϴ� ������... ���� ���� ����..
		itemStart = true;
		this.remove(startPanel);
		this.add(pp,"Center");
		pp.requestFocus();	// �� ������Ʈ�� �̺�Ʈ�� ���� �� �ֵ��� �Ѵ�. 
		pp.addKeyListener(new Key(gr,this,pp));  // pp�� �̺�Ʈ�� �޾ƾ��ϹǷ�. �߿�!!!
		this.remove(title);
		this.setVisible(true);
	}
	
//	public void cntSecond(String cnt) {
//		scondLabel.setText(cnt);
//		scondLabel.setForeground(UIManager.getColor("TextArea.selectionBackground"));
//		scondLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		scondLabel.setFont(new Font("HY���B", Font.BOLD | Font.ITALIC, 99));
//		this.add(scondLabel, BorderLayout.CENTER);
//		this.setVisible(true); //	������-Ʈ�縦 �־�� panel�� ��������, ���ο� ȭ���� ����.
//	}
	
	public void addLis() {
		startBtn.addActionListener(this);
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
}
