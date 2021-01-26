package Game;

import java.util.ArrayList;
import java.util.Random;

import DTO.itemDTO;
import DTO.p1DTO;
import DTO.p2DTO;

public class GameRoom {
	
	// �� ���谡 �ʹ� �����..�ФФ�
	private printPlayers pp = null;
	private gameGUI gg = null;
	
	
	//	�÷��̾� ��ü
	p1DTO p1 = null;
	p2DTO p2 = null;
	
	//	������
	private Random r = new Random();
	private String[] items = {"powerup","speedup","powerdown","speeddown"};
	ArrayList<itemDTO> itemList = new ArrayList<>();
	
	GameRoom(){
		init();
		gameGo();
	}
	
	private void init() {
		pp = new printPlayers(this);
		gg = new gameGUI(this,pp);
	}
	
	private void gameGo() {
		//	��
		while(true) {
			System.out.println(gg.itemStart);
			boolean start = gg.itemStart;
			try {
				if(start) {	//	���� ���۵� ���� ������ ���� ����
					Thread.sleep(2000);
					int k = r.nextInt(items.length);
					itemDTO item = new itemDTO();
					item.setName(items[k]);
					item.setX(r.nextInt(770));
					item.setY(r.nextInt(650));
					itemList.add(item);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(itemList.size()>=10) {
				itemList.remove(0);
			}
		}
	}
}
