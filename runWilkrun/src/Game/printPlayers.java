package Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.swing.ImageIcon;

import DTO.gameDTO;
import DTO.itemDTO;
import DTO.p1DTO;
import DTO.p2DTO;
import jdbc.DAO;

public class printPlayers extends Canvas{
	
	private GameRoom gr = null;
	private p1DTO bread = null;
	private p2DTO wilk = null;
	private int pSize = 30; 	// player Size
	private boolean gaming = true;
	private gameDTO game = null; 	//	������
	
	//�ð�
	private float time = 0;
	private String prtTime;
	private float timeTaken;
	private Calendar cal;
	
	//	������
	private ArrayList<itemDTO> iList = null;
	private Random r = new Random();
	private int p1X;
	private int p1Y;
	private int p2X;
	private int p2Y;
	
	// �̹���
	private Graphics buffg; // ������۸�
	private Image bimg = null;
	private Image breadImg = new ImageIcon(this.getClass().getResource("../img/bread_30x30.png")).getImage();
	private Image wilkImg = new ImageIcon(this.getClass().getResource("../img/wilk_30x30.png")).getImage();
	private Image butterImg = new ImageIcon(this.getClass().getResource("../img/butter_50x68.png")).getImage();
	
	printPlayers(GameRoom gr){
//		System.out.println(this.getClass().getResource("../img/bread_30x30.png"));
		this.gr=gr;
		this.setBackground(Color.pink);
		this.game = new gameDTO();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				cal = Calendar.getInstance();
				while(gaming) {
					try {
						Thread.sleep(30);
						if(gr.start) {	//	�����ϰ� �� ����
							time = (float)(time+0.03);
							prtTime = String.valueOf((float)time);
						}
						repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(!gaming) {
					gameover();
				}
			}
		}).start();
	}
	
	public void update(Graphics g) {
		buffg.clearRect(0,00,800,700);
		init();
		buffg.drawImage(breadImg, bread.getX(), bread.getY(),null);
		buffg.drawImage(wilkImg, wilk.getX(), wilk.getY(),null);
		buffg.drawString(prtTime, 600, 75);
		if(iList!=null) {
			printItems();
		}
		printPower();
		g.drawImage(bimg, 0,0,this);
		
		//	���ӿ���
		if(catchWilk()) {	//	������ ������ ������ �����ϴ°�?
			gaming=false;	// repaint�� ���� �ִ� while���� ����
			timeTaken = time;
			System.out.println(time);
		}
	}
	public void init() { 
		this.bread = gr.p1;
		this.wilk = gr.p2;
		this.iList=gr.itemList;
	}
	
	//	������۸�
	public void paint(Graphics g) {
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
	
	public void printItems() {
		if(iList!=null) {
			for(int i=0;i<iList.size();i++) {
				if(iList.get(i).getName().equals("speedup")) {
					buffg.drawImage(butterImg, iList.get(i).getX(), iList.get(i).getY(),null);
				}else {
					buffg.drawString(iList.get(i).getName(), iList.get(i).getX(), iList.get(i).getY());
				}
				p1X = gr.p1.getX();
				p1Y = gr.p1.getY();
				p2X = gr.p2.getX();
				p2Y = gr.p2.getY();
				int iX = iList.get(i).getX();
				int iY = iList.get(i).getY();
				
				//	�۸Ա� ����
				if(iList!=null) {
					if(eat(p1X,iX,p1Y,iY,50,68)) {
						if(iList.get(i).getName().equals("speedup")) {
							gr.p1.plusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("speeddown")) {
							gr.p1.minusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerup")) {
							gr.p1.plusPower();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerdown")) {
							gr.p1.minusPower();
							iList.remove(i);
						}
					}
				}
				if(iList!=null) {
					if(eat(p2X,iX,p2Y,iY,50,68)) {
						if(iList.get(i).getName().equals("speedup")) {
							gr.p2.plusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("speeddown")) {
							gr.p2.minusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerup")) {
							gr.p2.plusPower();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerdown")) {
							gr.p2.minusPower();
							iList.remove(i);
						}
					}
				}
			}
		}
	}
	
	//	4���� �𼭸��� �� ���� �� �ְ�..
	public boolean eat(int px,int ix,int py,int iy,int iWidth,int iHeight) {
		if((px<=ix+iWidth&&px>=ix)&&(py<=iy+iHeight&&py>=iy)) {
			return true;
		}else if((px+pSize<=ix+iWidth&&px+pSize>=ix)&&(py<=iy+iHeight&&py>=iy)) {
			return true;
		}else if((px+pSize<=ix+iWidth&&px+pSize>=ix)&&(py+pSize<=iy+iHeight&&py+pSize>=iy)) {
			return true;
		}else if((px<=ix+iWidth&&px>=ix)&&(py+pSize<=iy+iHeight&&py+pSize>=iy)) {
			return true;
		}
		return false;
	}
	
	
	//	�Ŀ�, ���ǵ� ���
	public void printPower() {
		String info1 = "1P\n"+gr.p1.getPower()+" �Ŀ�!\n"+gr.p1.getSpeed()+" ���ǵ�!";
		buffg.drawString(info1, 20, 580);
		
		String info2 = "2P\n"+gr.p2.getPower()+" �Ŀ�!\n"+gr.p2.getSpeed()+" ���ǵ�!";
		buffg.drawString(info2, 680, 580);
	}
	
	//	2p�� ����
	public boolean catchWilk() {
		int pointerX = bread.getX()+15;
		int pointerY = bread.getY()+15;
		
		if((pointerX>=wilk.getX()&&pointerX<=wilk.getX()+pSize)&&(pointerY>=wilk.getY()&&pointerY<=wilk.getY()+pSize)) {
			bread.setOutcome("win");	//	�극�尡 �̱� ���
			wilk.setOutcome("lose");
			game.setWinner("P1");
			return true;
		}else if(bread.getPower()<=0) {
			wilk.setOutcome("win");	//	��ũ�� �̱� ���
			bread.setOutcome("lose");
			game.setWinner("P2");
			return true;
		}else if(wilk.getPower()<=0) {
			bread.setOutcome("win");	//	�극�尡 �̱� ���
			wilk.setOutcome("lose");
			game.setWinner("P1");
			return true;
		}else if(wilk.getPower()>=160) {
			wilk.setOutcome("win");	//	��ũ�� �̱� ���
			bread.setOutcome("lose");
			game.setWinner("P2");
			return true;
		}else if(wilk.getSpeed()<=0&&bread.getSpeed()<=0) {	//	�� �� ������ �� �����Ƿ� ���º�
			wilk.setOutcome("draw");
			bread.setOutcome("draw");
		}
		return false;
	}
	
	/* ���ӿ��� �� �׼� -> DB�� ���� �־��ֱ� */
	public void gameover() {
		System.out.println("�׿���");
		bread.setTime(time);
		bread.setEnemy(wilk.getName());
		wilk.setTime(time);
		wilk.setEnemy(bread.getName());
		DAO db = new DAO();
		//	db ���̺� (player1, player2) insert
		db.insert(bread);
		db.insert(wilk);
		
		//���Ӹ���Ʈ
		setCalendar();
		game.setP1(bread.getName());
		game.setP2(wilk.getName());
		game.setRunning(time);
		db.insert(game);
		
	}
	
	/* ���� �÷����� �ð� ��*/
	public void setCalendar() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
		String datestr = sdf.format(cal.getTime());
		game.setPlayTime(datestr);
	}
}
