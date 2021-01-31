package list;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import DTO.gameDTO;
import DTO.p1DTO;
import jdbc.DAO;

public class timeRank extends Canvas{
	
	//	�̹���
	private Graphics buffg; // ������۸�
	private Image bimg = null;
	
	
	//	����Ʈ
	private ArrayList<gameDTO> games = null;
	private ArrayList<p1DTO> players = null;
	private DAO dao = new DAO();
	
	public timeRank(){
//		showTime();
		showP1();
		repaint();
	}
	
	public void update(Graphics g) {
		int y = 30;
//		for(int i = 0;i<games.size();i++) {
//			System.out.println("�ð�:"+games.get(i).getRunning());
//			String time = "�ð�:"+games.get(i).getRunning();
//			buffg.drawString(time, 100, y);
//			y=y+12;
//		}
		
		for(int i = 0;i<players.size();i++) {
			String time = "�г��� : "+players.get(i).getName()+" �̱�Ƚ��:"+players.get(i).getWinCnt()+" ��Ƚ��:"+players.get(i).getLoseCnt()+" �ּҽð�:"+players.get(i).getMinTime();
			buffg.drawString(time, 10, y);
			y=y+12;
		}
		g.drawImage(bimg, 0,0,this);
	}
	
	//	������۸�
	public void paint(Graphics g) {
		if(buffg == null) {
			bimg = createImage(350,550);
			if(bimg == null) {
				System.out.println("������۸� ���� ����");
			}else {
				buffg = bimg.getGraphics();
			}
			update(g);
		}
	}
	
	public void showTime() {
		games = dao.timeRank();
		
	}
	
	public void showP1() {
		players = dao.p1List();
	}
}
