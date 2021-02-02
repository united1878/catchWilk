package list;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import DTO.infoDTO;
import DTO.p1DTO;
import Game.printPlayers;
import Game.rule;
import Game.rule.MyKeyListener;
import jdbc.DAO;

/* ��ŷ ���� ��ư ��� ȭ�� */
public class Ranking extends Canvas{
	
	private DAO db = null;
	private printPlayers pp = null;
	private infoDTO info = null;
	private p1DTO p = null;
	
	
	//	�۾�
	private Font font2 = new Font("Gulim",Font.PLAIN,20);
	private int width;
	private int height;
	private FontMetrics metrics = null;
	
	// �̹���
	private Graphics buffg; // ������۸�
	private Image bimg = null;
	Image ranks = new ImageIcon(this.getClass().getResource("../img/ranking.png")).getImage();
	
	public Ranking(printPlayers pp){
		this.pp=pp;
		this.db=pp.db;
		System.out.println("���");
		repaint();
	}
	
	
	public void update(Graphics g) {
		buffg.drawImage(ranks, 0, 0, null);
		metrics = g.getFontMetrics(font2);
		setSpeed();
		setPower();
		setMostWin();
		setMostLose();
		setFastWin();
		setMyRank();
		g.drawImage(bimg,0,0,this);
	}
//	������۸�
	public void paint(Graphics g) {
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(new MyKeyListener(this));
		if(buffg == null) {
			bimg = createImage(800,700);
			if(bimg == null) {
				System.out.println("������۸� ���� ����");
			}else {
				buffg = bimg.getGraphics();
			}
			update(g);
		}
	}
	
	public void setSpeed() {
		info=db.maxSpeed();
		buffg.setFont(font2);
		String move = info.getName()+", ("+String.valueOf(info.getMove())+"s)";
		width = metrics.stringWidth(move);
		buffg.drawString(move, 400/2-width/2, 180);
		
	}
	public void setPower() {
		info=db.maxPower();
		buffg.setFont(font2);
		String power = info.getName()+", ("+String.valueOf(info.getPower())+"p)";
		width = metrics.stringWidth(power);
		buffg.drawString(power, 1200/2-width/2, 180);
		
	}
	
	public void setMostWin() {
		info=db.mostWin();
		buffg.setFont(font2);
		String win = info.getName()+", "+info.getWinCnt()+"ȸ �¸� ("+info.getWinAsB()+"/"+info.getWinAsW()+")";
		width = metrics.stringWidth(win);
		buffg.drawString(win, 400/2-width/2, 413);
	}
	
	public void setMostLose() {
		info=db.mostLose();
		buffg.setFont(font2);
		String lose = info.getName()+", "+info.getLoseCnt()+"ȸ �й� ("+info.getLoseAsB()+"/"+info.getLoseAsW()+")";
		width = metrics.stringWidth(lose);
		buffg.drawString(lose, 1200/2-width/2, 413);
	}
	
	public void setFastWin() {
		info=db.fastWin();
		buffg.setFont(font2);
		String fast = info.getName()+", "+info.getMinTime()+"s";
		width = metrics.stringWidth(fast);
		buffg.drawString(fast, 400/2-width/2, 637);
	}
	
	public void setMyRank() {
		if(pp.getB().getOutcome().equals("win")) {
			info = db.myRank(pp.getB().getName());
			buffg.setFont(font2);
			String rank = pp.getB().getName()+"�� ���� : "+info.getTimeRank()+"��";
			width = metrics.stringWidth(rank);
			String rank2 = "�� ������ "+info.getDiff()+"s ����";
			buffg.drawString(rank, 1200/2-width/2, 587);
			buffg.drawString(rank2, 1200/2-width/2, 637);
			
		}else if(pp.getB().getOutcome().equals("lose")) {
			info = db.myRank(pp.getB().getEnemy());
			buffg.setFont(font2);
			String rank = pp.getB().getEnemy()+"�� ���� : "+info.getTimeRank()+"��";
			width = metrics.stringWidth(rank);
			String rank2 = "�� ������ "+info.getDiff()+"s ����";
			buffg.drawString(rank, 1090/2-width/2, 567);
			buffg.drawString(rank2, 1090/2-width/2, 617);
		}
	}
	
	public class MyKeyListener implements KeyListener{
		Ranking r = null;
		MyKeyListener(Ranking r){
			this.r=r;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.getKeyCode()==27) {
				JOptionPane.showMessageDialog(null, "������ �����մϴ�.", "����", JOptionPane.PLAIN_MESSAGE);
				System.exit(0);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
