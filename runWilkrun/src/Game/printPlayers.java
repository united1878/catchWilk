package Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
	private gameGUI gg = null;
	private printOver po = null;
	
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
	private Image back = new ImageIcon(this.getClass().getResource("../img/background.png")).getImage();
	private Image breadImg = new ImageIcon(this.getClass().getResource("../img/bread_30x30.png")).getImage();
	private Image wilkImg = new ImageIcon(this.getClass().getResource("../img/wilk_30x30.png")).getImage();
	private Image speedupImg = new ImageIcon(this.getClass().getResource("../img/speedup_70x70.png")).getImage();
	private Image speeddownImg = new ImageIcon(this.getClass().getResource("../img/speeddown_70x70.png")).getImage();
	private Image powerupImg = new ImageIcon(this.getClass().getResource("../img/powerup_70x70.png")).getImage();
	private Image powerdownImg = new ImageIcon(this.getClass().getResource("../img/powerdown_70x70.png")).getImage();
	private Image gameoverImg = new ImageIcon(this.getClass().getResource("../img/gameover.png")).getImage();
	
	//	bgm
	private boolean bgm = true;
	private File eat = new File("./src/sound/eat.wav");
	private File gameOverSnd = new File("./src/sound/gameoverSound.wav");
	private File file = new File("./src/sound/ingameBGM.wav");
	private AudioInputStream bgmAis;
	private Clip bgmClip;
	
	printPlayers(GameRoom gr){
//		System.out.println(this.getClass().getResource("../img/bread_30x30.png"));
		this.gr=gr;
		this.setBackground(Color.white);
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
					bgmClip.stop();
					repaint();
					gameover();
				}
			}
		}).start();
	}
	
	public void update(Graphics g) {
		if(gaming) {	//	�������� �� 
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
				gameoverSound();
				timeTaken = time;
			}
		}else {	//	������ ������ ��
			try {
				Thread.sleep(1000);
				buffg.drawImage(gameoverImg, 0, 0,null);
				g.drawImage(bimg, 0,0,this);
			} catch (Exception e) {
			}
		}
	}
	public void init() { 
		this.bread = gr.p1;
		this.wilk = gr.p2;
		this.iList=gr.itemList;
		buffg.drawImage(back, 0, 0,null);
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
					buffg.drawImage(speedupImg, iList.get(i).getX(), iList.get(i).getY(),null);
				}else if(iList.get(i).getName().equals("speeddown")) {
					buffg.drawImage(speeddownImg, iList.get(i).getX(), iList.get(i).getY(),null);
				}else if(iList.get(i).getName().equals("powerup")) {
					buffg.drawImage(powerupImg, iList.get(i).getX(), iList.get(i).getY(),null);
				}else if(iList.get(i).getName().equals("powerdown")) {
					buffg.drawImage(powerdownImg, iList.get(i).getX(), iList.get(i).getY(),null);
				}
				
				p1X = gr.p1.getX();
				p1Y = gr.p1.getY();
				p2X = gr.p2.getX();
				p2Y = gr.p2.getY();
				int iX = iList.get(i).getX();
				int iY = iList.get(i).getY();
				
				//	�۸Ա� ����
				if(iList!=null) {
					if(eat(p1X,iX,p1Y,iY,70,70)) {
						if(iList.get(i).getName().equals("speedup")) {
							eatSound();
							gr.p1.plusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("speeddown")) {
							eatSound();
							gr.p1.minusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerup")) {
							eatSound();
							gr.p1.plusPower();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerdown")) {
							eatSound();
							gr.p1.minusPower();
							iList.remove(i);
						}
					}
				}
				if(iList!=null) {
					if(eat(p2X,iX,p2Y,iY,70,70)) {
						if(iList.get(i).getName().equals("speedup")) {
							eatSound();
							gr.p2.plusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("speeddown")) {
							eatSound();
							gr.p2.minusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerup")) {
							eatSound();
							gr.p2.plusPower();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerdown")) {
							eatSound();
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
	
	//	����
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
		
		gg.itemStart=false;
		System.out.println(gg.itemStart);
		
		po = new printOver(gr);	//	���� ȭ�� ���
		
		gg.AfterGame(po);
	}
	
	/* ���� �÷����� �ð� ��*/
	public void setCalendar() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
		String datestr = sdf.format(cal.getTime());
		game.setPlayTime(datestr);
	}
	
	public void eatSound() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(eat);
			Clip clip = AudioSystem.getClip();
//			clip.stop();
			clip.open(ais);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gameoverSound() {
		
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(gameOverSnd);
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getGG(gameGUI gg) {
		this.gg=gg;
	}
	
	public void ingameBGM() {
		try {
			bgmAis = AudioSystem.getAudioInputStream(file);
			bgmClip = AudioSystem.getClip();
			bgmClip.open(bgmAis);
			bgmClip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
