package Game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
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
import list.Ranking;

public class printPlayers extends Canvas{
	
	private GameRoom gr = null;
	private p1DTO bread = null;
	private p2DTO wilk = null;
	private int pSize = 30; 	// player Size
	private boolean gaming = true;
	private gameDTO game = null; 	//	겜정보
	private gameGUI gg = null;
	public printOver po = null;
	public DAO db = null; //	다오
	
	//	폰트
	private String temp;
	private Font timeFont = new Font("Dotum",Font.PLAIN,25);
	private int FontWidth;
	private FontMetrics metrics = null;
	private Font nameFont = new Font("Gulim",Font.PLAIN,12);
	private Font speedFont = new Font("serif",Font.BOLD,18);
	//시간
	private float time = 0;
	private String prtTime;
	private Calendar cal;
	
	//	아이템
	private ArrayList<itemDTO> iList = null;
	private Random r = new Random();
	private int p1X;
	private int p1Y;
	private int p2X;
	private int p2Y;
	
	
	// 이미지
	private Graphics buffg; // 더블버퍼링
	private Image bimg = null;
	
	private Image back = new ImageIcon(this.getClass().getResource("../img/background.png")).getImage();
	private Image breadImg = new ImageIcon(this.getClass().getResource("../img/bread_30x30.png")).getImage();
	private Image wilkImg = new ImageIcon(this.getClass().getResource("../img/wilk_30x30.png")).getImage();
	private Image speedupImg = new ImageIcon(this.getClass().getResource("../img/speedup_70x70.png")).getImage();
	private Image speeddownImg = new ImageIcon(this.getClass().getResource("../img/speeddown_70x70.png")).getImage();
	private Image powerupImg = new ImageIcon(this.getClass().getResource("../img/powerup_70x70.png")).getImage();
	private Image powerdownImg = new ImageIcon(this.getClass().getResource("../img/powerdown_70x70.png")).getImage();
	private Image gameoverImg = new ImageIcon(this.getClass().getResource("../img/gameover.png")).getImage();
	private Image breadPower = new ImageIcon(this.getClass().getResource("../img/BreadPower.png")).getImage();
	private Image wilkPower = new ImageIcon(this.getClass().getResource("../img/WilkPower.png")).getImage();
	private Image powerImg = new ImageIcon(this.getClass().getResource("../img/power.png")).getImage();
	private BufferedImage newBread = null;
	private BufferedImage newWilk = null;
	private Image resizePower = null;
	//	bgm
	private boolean bgm = true;
	private File eatGood = new File("./src/sound/eatGood.wav");
	private File eat = new File("./src/sound/eat.wav");
	private File gameOverSnd = new File("./src/sound/gameoverSound.wav");
	private File file = new File("./src/sound/ingameBGM.wav");
	private AudioInputStream bgmAis;
	private Clip bgmClip;
	
	printPlayers(GameRoom gr){
		this.gr=gr;
		this.setBackground(Color.white);
		this.game = new gameDTO();
		
	}
	
	public void gameRun() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				cal = Calendar.getInstance();
				while(gaming) {
					try {
						Thread.sleep(30);
						time = (float)(time+0.03);
						prtTime = String.valueOf((float)time);
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
		if(gaming) {	//	게임중일 때 
			buffg.clearRect(0,00,800,700);
			init();
			buffg.drawImage(breadImg, bread.getX(), bread.getY(),null);
			buffg.drawImage(wilkImg, wilk.getX(), wilk.getY(),null);
			if(prtTime!=null) {
				metrics = g.getFontMetrics(timeFont);
				FontWidth = metrics.stringWidth(prtTime);
			}
			if(iList!=null) {
				printItems();
			}
			printPower();
			buffg.setFont(timeFont);
			buffg.setColor(Color.black);
			buffg.drawString(prtTime, 800/2-FontWidth/2, 75);
			g.drawImage(bimg, 0,0,this);
			
			//	게임오버
			if(catchWilk()) {	//	게임이 끝나는 조건을 만족하는가?
				gaming=false;	// repaint가 돌고 있는 while문을 끝냄
				gameoverSound();
				gr.start=false;
			}
		}else {	//	게임이 끝났을 때
			try {
				Thread.sleep(1000);
				buffg.drawImage(gameoverImg, 0, 0,null);
				g.drawImage(bimg, 0,0,this);
			} catch (Exception e) {
			}
		}
	}
	public void init() { 
		gr.p1.setFlag();
		gr.p2.setFlag();
		this.bread = gr.p1;
		this.wilk = gr.p2;
		this.iList=gr.itemList;
		buffg.drawImage(back, 0, 0,null);
	}
	
	//	더블버퍼링
	public void paint(Graphics g) {
		if(buffg == null) {
			bimg = createImage(800,700);
			if(bimg == null) {
				System.out.println("더블버퍼링 적용 실패");
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
				
				//	템먹기 구현
				if(iList!=null) {
					if(eat(p1X,iX,p1Y,iY,70,70)) {
						if(iList.get(i).getName().equals("speedup")) {
							eatGoodSound();
							gr.p1.plusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("speeddown")) {
							eatSound();
							gr.p1.minusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerup")) {
							eatGoodSound();
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
							eatGoodSound();
							gr.p2.plusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("speeddown")) {
							eatSound();
							gr.p2.minusMove();
							iList.remove(i);
						}else if(iList.get(i).getName().equals("powerup")) {
							eatGoodSound();
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

	//	4개의 모서리로 다 먹을 수 있게..
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
	
	
	//	파워, 스피드 출력
	public void printPower() {
		buffg.drawImage(powerImg, 140, 600, null);
		buffg.setFont(nameFont);
		buffg.setColor(Color.red);
		buffg.drawString("브레드", 95, 635);
		buffg.setColor(Color.blue);
		buffg.drawString("윌크", 95, 665);
		if(bread.getPower()>0) {
			resizePower = breadPower.getScaledInstance(14*bread.getPower(), 15, Image.SCALE_SMOOTH);
			newBread = new BufferedImage(14*bread.getPower(), 15,BufferedImage.TYPE_INT_RGB);
			buffg.drawImage(newBread, 135,625,null);
			buffg.setFont(speedFont);
			buffg.setColor(Color.red);
			temp = "브레드 SPEED "+bread.getMove();
			buffg.drawString(temp, 55, 565);
		}
		if(wilk.getPower()>0) {
			resizePower = wilkPower.getScaledInstance(8*wilk.getPower(), 15, Image.SCALE_SMOOTH);
			newWilk = new BufferedImage(8*wilk.getPower(), 15,BufferedImage.TYPE_INT_RGB);
			buffg.drawImage(newWilk, 135,655,null);
			buffg.setFont(speedFont);
			buffg.setColor(Color.blue);
			temp = "윌크 SPEED "+wilk.getMove();
			buffg.drawString(temp, 630, 565);
		}
	}
	
	//	승패
	public boolean catchWilk() {
		int pointerX = bread.getX()+15;
		int pointerY = bread.getY()+15;
		
		if((pointerX>=wilk.getX()&&pointerX<=wilk.getX()+pSize)&&(pointerY>=wilk.getY()&&pointerY<=wilk.getY()+pSize)) {
			bread.setOutcome("win");	//	브레드가 이긴 경우
			wilk.setOutcome("lose");
			game.setWinner("P1");
			
			return true;
		}else if(bread.getPower()<=0) {
			wilk.setOutcome("win");	//	윌크가 이긴 경우
			bread.setOutcome("lose");
			game.setWinner("P2");
			return true;
		}else if(wilk.getPower()<=0) {
			bread.setOutcome("win");	//	브레드가 이긴 경우
			wilk.setOutcome("lose");
			game.setWinner("P1");
			return true;
		}else if(wilk.getPower()>=160) {
			wilk.setOutcome("win");	//	윌크가 이긴 경우
			bread.setOutcome("lose");
			game.setWinner("P2");
			return true;
		}else if(wilk.getSpeed()<=0&&bread.getSpeed()<=0) {	//	둘 다 움직일 수 없으므로 무승부
			wilk.setOutcome("draw");
			bread.setOutcome("draw");
		}
		return false;
	}
	
	/* 게임오버 후 액션 -> DB에 정보 넣어주기 */
	public void gameover() {
		System.out.println("겜오버");
		bread.setTime(time);
		bread.setEnemy(wilk.getName());
		wilk.setTime(time);
		wilk.setEnemy(bread.getName());
		db = new DAO();
		//	db 테이블에 (player1, player2) insert
		db.insert(bread);
		db.insert(wilk);
		
		//게임리스트
		setCalendar();
		game.setP1(bread.getName());
		game.setP2(wilk.getName());
		game.setRunning(time);
		db.insert(game);
		
		po = new printOver(gr,this);	//	겜후 화면 출력
		po.getGG(gg);
		gg.AfterGame(po);
//		rk = new Ranking(this); /*임시*/
//		po.addKeyListener(new Key2(rk,po,gg));
//		po.requestFocus();
	}
	
	/* 게임 플레이한 시간 셋*/
	public void setCalendar() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
		String datestr = sdf.format(cal.getTime());
		game.setPlayTime(datestr);
	}
	
	public void eatSound() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(eat);
			Clip clip = AudioSystem.getClip();
			clip.open(ais);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void eatGoodSound() {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(eatGood);
			Clip clip = AudioSystem.getClip();
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
			if(bgmAis==null) {
				bgmAis = AudioSystem.getAudioInputStream(file);
				bgmClip = AudioSystem.getClip();
				bgmClip.open(bgmAis);
				bgmClip.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public p1DTO getB() {
		return bread;
	}
	
	
}
