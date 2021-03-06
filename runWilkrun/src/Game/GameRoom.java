package Game;

import java.util.ArrayList;
import java.util.Random;

import DTO.itemDTO;
import DTO.p1DTO;
import DTO.p2DTO;
import list.Ranking;

public class GameRoom {
	
	//	제어
	boolean start=true;	//	pp에서도 씀
	
	// 이 관계가 너무 어려워..ㅠㅠㅠ
	private printPlayers pp = null;
	private gameGUI gg = null;
	
	
	//	플레이어 객체
	p1DTO p1 = null;
	p2DTO p2 = null;
	
	//	아이템
	private Random r = new Random();
	private String[] items = {"powerup","speedup","powerdown","speeddown"};
	ArrayList<itemDTO> itemList = new ArrayList<>();
	
	GameRoom(){
		init();
	}
	
	private void init() {
		pp = new printPlayers(this);
		gg = new gameGUI(this,pp);
		pp.getGG(gg);
	}
	
	public void gameGo() {
		//	템
		while(start) {
			try {
				Thread.sleep(2000);
				int k = r.nextInt(items.length);
				itemDTO item = new itemDTO();
				item.setName(items[k]);
				item.setX(r.nextInt(770));
				item.setY(r.nextInt(650));
				itemList.add(item);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(itemList.size()>=10) {
				itemList.remove(0);
			}
		}
	}
}
