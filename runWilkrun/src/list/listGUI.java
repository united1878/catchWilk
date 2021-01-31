package list;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/* ��ŷȭ�� gui */
public class listGUI extends JFrame implements KeyListener{
	
	//	����Ʈ
	private timeRank tr = null;

	public listGUI(){
		tr = new timeRank();
		setFrame();
		setTimeRank();
	}

	private void setFrame() {
		this.addKeyListener(this);	// Ű ������ 
		this.setBounds(720,130,350,550);
		this.setUndecorated(true);
		this.getContentPane().setLayout(new java.awt.BorderLayout(0,0));
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
	}
	
	private void setTimeRank(){
		this.add(tr,"Center");
		this.setVisible(true);
	}
	
	private void setP1List(){
		this.add(tr,"Center");
		this.setVisible(true);
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
//	 	ESC ���� �� ����
		if(e.getKeyCode()==27) {
	    	  System.exit(0);
	      }
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
