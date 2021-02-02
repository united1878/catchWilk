package Game;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.Timer;
import java.util.TimerTask;

public class Key extends Thread implements KeyListener{
   private GameRoom gr = null;
   private gameGUI gg = null;
   private printPlayers pp = null;
   
   //   Ű ���� ������ ���� ����
   private HashSet<Integer> pressedKeys = new HashSet<>(); //Ű�� ���� ������ ���� ���� �ؽü�
   private Iterator<Integer> i = null;
   private Timer timer;
   
   public Key(GameRoom gr, gameGUI gg, printPlayers pp){
      this.gr=gr;
      this.gg=gg;
      this.pp=pp;
      
      /* KeyPreesed()���� �߻��� Ű�ڵ带 HasSet�� �����Ͽ� n�� �������� Ű�ڵ带 Ȯ���Ͽ� ȭ���� �����Ѵ�.*/
      timer = new Timer(30,new ActionListener() {
    	  @Override
    	  public void actionPerformed(ActionEvent arg0) {
    		  if(!pressedKeys.isEmpty()) {
                i = pressedKeys.iterator();
                int n = 0;
                while(i.hasNext()) {
                   n=i.next();
                   if (n==39) gr.p1.goRight();
                   else if (n==37) gr.p1.goLeft();
                   else if (n==38) gr.p1.goUp();
                   else if (n==40) gr.p1.goDown();
                   else if (n=='w') gr.p2.goUp();
                   else if (n=='a') gr.p2.goLeft();
                   else if (n=='s') gr.p2.goDown();
                   else if (n=='d') gr.p2.goRight();
                }
             }else {
            	 timer.stop();
             }
    	  }
      });
   }

   @Override
   /// -------------------------------------- ��ũ �̵�
   
   /* *** ��,��,��,�� Ű�� ����ڰ� ����ũ�μ���Ʈ ���Ǳ��� "���ڿ�������" ����� ����ϴ����� ���� ������ ���� �� ����. */
   public void keyTyped(KeyEvent e) {
      if(e.getKeyChar()=='w'||e.getKeyChar()=='W'||e.getKeyChar()=='��') { // ����Ű ������ ������ �̵�
    	  int keyCode = 'w';
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }else if(e.getKeyChar()=='s'||e.getKeyChar()=='S'||e.getKeyChar()=='��') { //����Ű �Ʒ��� ������ �̵�
    	  int keyCode = 's';
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }else if(e.getKeyChar()=='a'||e.getKeyChar()=='A'||e.getKeyChar()=='��') { //   ����
    	  int keyCode = 'a';
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }else if(e.getKeyChar()=='d'||e.getKeyChar()=='D'||e.getKeyChar()=='��') {//   ������
    	  int keyCode = 'd';
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }
   }

   @Override
   /// -------------------------------------- �극�� �̵�
   public void keyPressed(KeyEvent e) {
      
      if(e.getKeyCode()==38) { // ����Ű ������ ������ �̵�
         int keyCode = e.getKeyCode();
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }else if(e.getKeyCode()==40) { //����Ű �Ʒ��� ������ �̵�
         int keyCode = e.getKeyCode();
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }else if(e.getKeyCode()==37) { //   ����
         int keyCode = e.getKeyCode();
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }else if(e.getKeyCode()==39) {//   ������
         int keyCode = e.getKeyCode();
         pressedKeys.add(keyCode);
         if(!timer.isRunning()) timer.start();
      }else if(e.getKeyCode()==27) {
    	  System.exit(0);
      }
   }

   @Override
   /// -------------------------------------- �극�� ����
   public void keyReleased(KeyEvent e) {
      if(e.getKeyCode()==38) { 
         int keyCode = e.getKeyCode();
         pressedKeys.remove(keyCode);
      }else if(e.getKeyCode()==40) { 
         int keyCode = e.getKeyCode();
         pressedKeys.remove(keyCode);
      }else if(e.getKeyCode()==37) { 
         int keyCode = e.getKeyCode();
         pressedKeys.remove(keyCode);
      }else if(e.getKeyCode()==39) {
         int keyCode = e.getKeyCode();
         pressedKeys.remove(keyCode);
      }
      
      /// -------------------------------------- ��ũ ����
      if(e.getKeyChar()=='w'||e.getKeyChar()=='W'||e.getKeyChar()=='��') { 
    	  int keyCode = 'w';
         pressedKeys.remove(keyCode);
      }else if(e.getKeyChar()=='s'||e.getKeyChar()=='S'||e.getKeyChar()=='��') { 
    	  int keyCode = 's';
         pressedKeys.remove(keyCode);
      }else if(e.getKeyChar()=='a'||e.getKeyChar()=='A'||e.getKeyChar()=='��') {
    	  int keyCode = 'a';
         pressedKeys.remove(keyCode);
      }else if(e.getKeyChar()=='d'||e.getKeyChar()=='D'||e.getKeyChar()=='��') {
    	  int keyCode = 'd';
         pressedKeys.remove(keyCode);
      }
   }
}