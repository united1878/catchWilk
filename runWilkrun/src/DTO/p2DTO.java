package DTO;

public class p2DTO {
	private String name = null;
	private int x = 380;
	private int y = 340;
	private int move = 6;
	private int power = 50; //�⺻ ü��
	private String outcome = null; //����
	private float time = -1;
	private String enemy=null;
	
	public p2DTO(){
		//	1�ʸ��� ü���� ������ ����
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
						running();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/* �ӵ� , ü�� */
	public int getPower() {
		return power;
	}
	
	public int getSpeed() {
		return move;
	}
	
//	�� ������ power+10
	public void plusPower() {
		power=power+10;
	}
	
	public void minusPower() {
		power = power-10;
	}
	
	//	�ӵ�
	public void plusMove() {
		move=move+3;
	}
	
	public void minusMove() {
		if(move>=0) {
		move=move-3;
		}
	}
	public void running() {
		power=power-1;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
	// ------------- ����Ű�� ������
	public void goUp() {
		if(y>0) {
			y=y-move;
		}
	}
	public void goDown() {
		if(y<690) {
			y=y+move;
		}
	}
	public void goLeft() {
		if(x>0) {
			x=x-move;
		}
	}
	public void goRight() {
		if(x<790) {
			x=x+move;
		}
	}
	
	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public String getEnemy() {
		return enemy;
	}

	public void setEnemy(String enemy) {
		this.enemy = enemy;
	}

	public int getMove() {
		return move;
	}
}
