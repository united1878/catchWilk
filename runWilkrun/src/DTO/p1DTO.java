package DTO;

public class p1DTO {
	private boolean flag = false;
	private String name = null;
	private int x = 400;
	private int y = 350;
	private int move = 6;
	private int power = 30; //�⺻ ü�� *�극��� �̱�� ��찡 2�����̹Ƿ� ü�¿� �г�Ƽ
	private String outcome = null; //����
	private float time = -1;
	private String enemy=null;
	
	private int num=-1;
	private int winCnt = 0; //	�̱�Ƚ��
	private int loseCnt = 0; //	��Ƚ��
	private float minTime = -1;
	
	private int maxPower=30;
	private int maxMove = 6;
	
	public p1DTO(){
		//	1�ʸ��� ü���� ������ ����
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
						max();
						if(isFlag()) {
							running();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	public float getMinTime() {
		return minTime;
	}



	public void setMinTime(float minTime) {
		this.minTime = minTime;
	}



	public int getNum() {
		return num;
	}



	public void setNum(int num) {
		this.num = num;
	}



	public int getWinCnt() {
		return winCnt;
	}



	public void setWinCnt(int winCnt) {
		this.winCnt = winCnt;
	}



	public int getLoseCnt() {
		return loseCnt;
	}



	public void setLoseCnt(int loseCnt) {
		this.loseCnt = loseCnt;
	}



	public void setMove(int move) {
		this.move = move;
	}



	public void setPower(int power) {
		this.power = power;
	}



	/* �ӵ� , ü�� */
	public int getPower() {
		return power;
	}
	
	public int getSpeed() {
		return move;
	}
	
	public void plusPower() {
		power=power+10;
	}
	
	public void minusPower() {
		power = power-10;
	}
	
	public void plusMove() {
		move=move+3;
	}
	
	public void minusMove() {
		if(move>=0) {
		move=move-3;
		}
	}
	
	/* �ƽ��Ŀ�, �ƽ����ǵ� �޼��� */
	public void max() {
		if(maxPower<power) {
			maxPower=power;
		}
		if(maxMove<move) {
			maxMove=move;
		}
	}
	public int getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(int maxPower) {
		this.maxPower = maxPower;
	}

	public int getMaxMove() {
		return maxMove;
	}

	public void setMaxMove(int maxMove) {
		this.maxMove = maxMove;
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


	public boolean isFlag() {
		return flag;
	}


	public void setFlag() {
		this.flag = true;
	}
	
	
}
