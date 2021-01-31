package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DTO.gameDTO;
import DTO.infoDTO;
import DTO.p1DTO;
import DTO.p2DTO;

public class DAO {
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver"; //����̹�����. final : ��ĥ �� ���ٴ� ��
	private final String DB_URL = "jdbc:mysql://localhost/catchWilk?&serverTimezone=UTC"; //serverTimezone�� �׳� �����ؼ� ���°�
	private final String USER_NAME = "root"; //���̵�
	private final String PASSWORD = "1111"; //��ȣ
	
	private Connection conn=null; //DB�� �����ϱ� ���� ��ü
	private PreparedStatement pstmt = null; //�������� �����͸� ?,?,? �� mapping ���ֱ� ���� ��ü�̴�. 
	private Statement stmt = null; //����ǥ ���� ���� Statement, ����ǥ�� ������ Prepared
	private ResultSet rs = null; //���� ����� Ʃ���� 2���� �迭 ����ó�� �������� ��ü

	public DAO(){
		//�ʱ� �غ� �۾�
		try {
			Class.forName(JDBC_DRIVER); //Ŭ������ ������ �޾ƿ��� Ŭ����
			System.out.println("����̹� �ε� ����");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} //DB�ν�
	}
	
	private Connection getConn() {
		try{
			conn=DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
			System.out.println("���Ἲ��");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return conn; //connection�̶�� ��ü�� ����Ͷ� return�ض�
	}
	
	/* �÷��̾�1 �μ�Ʈ */
	public void insert(p1DTO p1) {
		if(getConn()!=null) {
			try {
				String sql = "insert into player1(num,name,time,outcome,move,power,enemy) values(0,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,p1.getName());
				pstmt.setFloat(2, p1.getTime());
				pstmt.setString(3, p1.getOutcome());
				pstmt.setInt(4, p1.getMaxMove());
				pstmt.setInt(5, p1.getMaxPower());
				pstmt.setString(6, p1.getEnemy());
				int rs = pstmt.executeUpdate();
				System.out.println("P1 ���� "+rs+"�� �Է� ����");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* �÷��̾�2 �μ�Ʈ */
	public void insert(p2DTO p2) {
		if(getConn()!=null) {
			try {
				String sql = "insert into player2(num,name,time,outcome,move,power,enemy) values(0,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,p2.getName());
				pstmt.setFloat(2, p2.getTime());
				pstmt.setString(3, p2.getOutcome());
				pstmt.setInt(4, p2.getMaxMove());
				pstmt.setInt(5, p2.getMaxPower());
				pstmt.setString(6, p2.getEnemy());
				int rs = pstmt.executeUpdate();
				System.out.println("P2 ���� "+rs+"�� �Է� ����");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* �������� �μ�Ʈ */
	public void insert(gameDTO game) {
		if(getConn()!=null) {
			try {
				String sql = "insert into games(num,p1,p2,playtime,running,winner) values(0,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,game.getP1());
				pstmt.setString(2,game.getP2());
				pstmt.setString(3,game.getPlayTime());
				pstmt.setFloat(4, game.getRunning());
				pstmt.setString(5, game.getWinner());
				int rs = pstmt.executeUpdate();
				System.out.println("���� ���� "+rs+"�� �Է� ����");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* �ð� ���� */
	public ArrayList<gameDTO> timeRank(){
		ArrayList<gameDTO> tr = new ArrayList<>();
		if(getConn()!=null) {
			try {
				String sql = "select *, rank() over(order by running) as ���� from games;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				while(rs.next()) {
					gameDTO g = new gameDTO();
					g.setNum(rs.getInt("num"));
					g.setP1(rs.getString("p1"));
					g.setP2(rs.getString("p2"));
					g.setPlayTime(rs.getString("playtime"));
					g.setRunning(rs.getFloat("running"));
					g.setWinner(rs.getString("winner"));
					g.setTimeRank(rs.getString("����"));
					tr.add(g);
				}
			} catch (Exception e) {
			}
		}
		return tr;
	}
	
	/* p1�� ���� */
	
	public ArrayList<p1DTO> p1List(){
		ArrayList<p1DTO> p1List = new ArrayList<>();
		if(getConn()!=null) {
			try {
				//1.	name�� �ߺ����� �ʰ� ����
				String sql = "select distinct name from player1; ";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				while(rs.next()) {
					p1DTO p = new p1DTO();
					p.setName(rs.getString("name"));
					p1List.add(p);
				}
				
				//2.	�̱� Ƚ��/�� Ƚ�� set
				sql = "select count(*) from player1 where outcome='win' and name=?;";
				pstmt = conn.prepareStatement(sql);
				
				for(int i=0;i<p1List.size();i++) {
					pstmt.setString(1, p1List.get(i).getName());
					rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
					if(rs.next()) {	//	rs.next()�� �־�� ����� �����Ѵ�
						int cntRs = rs.getInt("count(*)");
						p1List.get(i).setWinCnt(cntRs);
					}
				}
				
				sql = "select count(*) from player1 where outcome='lose' and name=?;";
				pstmt = conn.prepareStatement(sql);
				for(int i=0;i<p1List.size();i++) {
					pstmt.setString(1, p1List.get(i).getName());
					rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
					if(rs.next()) {
						p1List.get(i).setLoseCnt(rs.getInt("count(*)"));
					}
				}
				
				//3.	�̱��, ���� �ּ��� �ð�
				sql = "select min(time) from player1 where outcome='win' and name=?;";
				pstmt = conn.prepareStatement(sql);
				for(int i=0;i<p1List.size();i++) {
					pstmt.setString(1, p1List.get(i).getName());
					rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
					if(rs.next()) {
						p1List.get(i).setMinTime(rs.getFloat("min(time)"));
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return p1List;
	}
	
	/* p2�� ���� */
	
	public ArrayList<p2DTO> p2List(){
		ArrayList<p2DTO> p2List = new ArrayList<>();
		if(getConn()!=null) {
			try {
				//1.	name�� �ߺ����� �ʰ� ����
				String sql = "select distinct name from player2; ";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				while(rs.next()) {
					p2DTO p = new p2DTO();
					p.setName(rs.getString("name"));
					p2List.add(p);
				}
				
				//2.	�̱� Ƚ��/�� Ƚ�� set
				sql = "select count(*) from player2 where outcome='win' and name=?;";
				pstmt = conn.prepareStatement(sql);
				
				for(int i=0;i<p2List.size();i++) {
					pstmt.setString(1, p2List.get(i).getName());
					rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
					if(rs.next()) {	//	rs.next()�� �־�� ����� �����Ѵ�
						int cntRs = rs.getInt("count(*)");
						p2List.get(i).setWinCnt(cntRs);
					}
				}
				
				sql = "select count(*) from player2 where outcome='lose' and name=?;";
				pstmt = conn.prepareStatement(sql);
				for(int i=0;i<p2List.size();i++) {
					pstmt.setString(1, p2List.get(i).getName());
					rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
					if(rs.next()) {
						p2List.get(i).setLoseCnt(rs.getInt("count(*)"));
					}
				}
				
				//3.	�̱��, ���� �ּ��� �ð�
				sql = "select min(time) from player2 where outcome='win' and name=?;";
				pstmt = conn.prepareStatement(sql);
				for(int i=0;i<p2List.size();i++) {
					pstmt.setString(1, p2List.get(i).getName());
					rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
					if(rs.next()) {
						p2List.get(i).setMinTime(rs.getFloat("min(time)"));
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return p2List;
	}
	
	/*�ִ�ӵ� ���ϱ�*/
	public infoDTO maxSpeed(){
		infoDTO i = new infoDTO();
		if(getConn()!=null) {
			try {
				String sql = "select name, max(move) from (select * from player1 union select * from player2)t;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				while(rs.next()) {
					i.setName(rs.getString("name"));
					i.setMove(rs.getInt("max(move)"));
				}
			} catch (Exception e) {
			}
		}
		return i;
	}
	
	/*�ִ��Ŀ� ���ϱ�*/
	public infoDTO maxPower(){
		infoDTO i = new infoDTO();
		if(getConn()!=null) {
			try {
				String sql = "select name, max(power) from (select * from player1 union select * from player2)t;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				while(rs.next()) {
					i.setName(rs.getString("name"));
					i.setPower(rs.getInt("max(power)"));
				}
			} catch (Exception e) {
			}
		}
		return i;
	}
	
	/*���� ���� �̱� ���*/
	public infoDTO mostWin(){
		infoDTO i = new infoDTO();
		if(getConn()!=null) {
			try {
				String sql = "select name,time,max(ranking) from (select name,time,count(*) as ranking from (select * from player1 union select * from player2)t where outcome='win' group by name order by count(*) desc)t2;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				if(rs.next()) {
					i.setName(rs.getString("name"));
					i.setWinCnt(rs.getInt("max(ranking)"));
					}
				sql = "select * from player1 where outcome='win' and name=?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, i.getName());
				rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
				int cnt = 0;
				while(rs.next()) {
					cnt++;
				}
				i.setWinAsB(cnt);
				
				sql = "select * from player2 where outcome='win' and name=?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, i.getName());
				rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
				cnt = 0;
				while(rs.next()) {
					cnt++;
				}
				i.setWinAsW(cnt);
			} catch (Exception e) {
			}
		}
		return i;
	}
	
	/*���� ���� �� ���*/
	public infoDTO mostLose(){
		infoDTO i = new infoDTO();
		if(getConn()!=null) {
			try {
				String sql = "select name,time,max(ranking) from (select name,time,count(*) as ranking from (select * from player1 union select * from player2)t where outcome='lose' group by name order by count(*) desc)t2;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				while(rs.next()) {
					i.setName(rs.getString("name"));
					i.setLoseCnt(rs.getInt("max(ranking)"));
				}
				
				sql = "select * from player1 where outcome='lose' and name=?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, i.getName());
				rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
				int cnt = 0;
				while(rs.next()) {
					cnt++;
				}
				i.setLoseAsB(cnt);
				sql = "select * from player2 where outcome='lose' and name=?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, i.getName());
				rs = pstmt.executeQuery(); //rs�� ������ �� �ʿ���.
				cnt = 0;
				while(rs.next()) {
					cnt++;
				}
				i.setLoseAsW(cnt);
			} catch (Exception e) {
			}
		}
		return i;
	}
	
	/*�ּӽ¸�*/
	public infoDTO fastWin(){
		infoDTO i = new infoDTO();
		if(getConn()!=null) {
			try {
				String sql = "select name,min(time) from (select * from player1 union select * from player2 where outcome='win' order by time)t;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				if(rs.next()) {
					i.setName(rs.getString("name"));
					i.setMinTime(rs.getFloat("time"));
				}
			} catch (Exception e) {
			}
		}
		return i;
	}
	
	/* ���� �̱� �ӵ� ���� */
	public infoDTO myRank(String n) {
		infoDTO i = new infoDTO();
		float diff = -1;
		if(getConn()!=null) {
			try {
				String sql = "select name, time from (select * from player1 union select * from player2)t where outcome = 'win' order by time;";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql); //rs�� ������ �� �ʿ���.
				int cnt = 0;
				while(rs.next()) {
					cnt++;
					if(rs.getString("name").equals(n)) {
						i.setTimeRank(cnt);
						i.setDiff(rs.getFloat("time")-diff);
						break;
					}
					diff = rs.getFloat("time");
				}
			} catch (Exception e) {
			}
		}
		return i;
		
	}
}
