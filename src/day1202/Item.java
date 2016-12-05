package day1202;

/*
   Class ����
   - Block : ����� X��ǥ Y��ǥ�� ������ ������ �ִ� ��� Ŭ����.
   - Item : ����� ������ ��Ʈ���� ������(���) �� �����. (�θ�Ŭ����)
   - Rect, OneThree, ThreeOne ... : ItemŬ������ ��ӹ޾� �������ġ������ �����Ѵ�.
*/

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

//��� Ŭ����
class Block
{
   private int x;
   private int y;
   //������
   public Block(){
   }
   public Block(int x, int y){
      this.x = x;
      this.y = y;
   }
   //�ش� ����Ʈ��ŭ ����
   public void move(int xPlus, int yPlus){
      this.x += xPlus;
      this.y += yPlus;
   }
   //X����Ʈ ��ȯ
   public int getX(){
      return this.x;
   }
   //Y����Ʈ ��ȯ
   public int getY(){
      return this.y;
   }
   //�ڽ� ��ȯ
   public Block getBlock(){
      return this;
   }
   //XY����
   public void setXY(int x, int y){
      this.x = x;
      this.y = y;
   }
}

//������ Ŭ����
public class Item 
{
   JPanel[] panel;      //�ǳ�
   Block[] block;      //��������Ʈ(x,y��)
   Block[][] block_info;      //�� ������ ����Ʈ����
   //���ǹ迭 0~3 ���� 0-0�� 1-90�� 2-180�� 3-270��
   //���� �迭�� �ǳ� ����
   Block currentXY;
   int cnt;            //���ǳڰ���
   int angle;            //�Ѱ�������
   int current_angle;      //���簢����
   int xCnt;            //���ΰ�

   Color color;      //��
   int area;         //����
   
   //�簢������� ������� -> �������� ���ڷ� 20, 1, 4, 14�� ����
   public Item(int area, int angle, int cnt, int xCnt){
      this.angle = angle;                     //������ ����
      this.cnt = cnt;                        //����ĭ�� ���� -> �������� ���� ���� ---- �̴ϱ� 4���� ������� �̷����, �� ������ 4���� �̷����
      this.panel = new JPanel[cnt];            //�ǳڰ��� ����
      this.block = new Block[cnt];            //����Ʈ ����
      this.block_info = new Block[angle][cnt];   //����Ʈ ����, ��������
      this.area = area;                     //����ĭ�� pixel���� (20)      
      this.currentXY = new Block(0,0);         //���簪(x=0,y=0)
      this.xCnt = xCnt;                     //Center�г� �迭�� ���α���

      for (int i=0; i<cnt; i++){         //�гλ���
         this.panel[i] = new JPanel();   //��� ���� 4���� �г��� ������
      }
   }
   public void setDefaultRandom(){
      this.current_angle = (int)(Math.random() * this.angle);      //cuttent_angle -> ������ ���� �� �ϳ��� ����
      this.block = this.block_info[this.current_angle];         //�� ���� ������ block_info[��������][4]�� block[4]�� ����
   }
   //�����̳ʿ� ���
   public void setItem(Container c){
      for (int i=0; i<panel.length; i++){                     //i�� 0���� 3����
         panel[i].setBackground(this.color);                  //���ǳ��� ��������
         panel[i].setSize(area, area);                     //���ǳ��� ���̼���
         panel[i].setLocation(((block[i].getX()) * area)-100, ((block[i].getY()) * area)-100);   //�⺻��ġ �Ⱥ��̴°��� ����
         panel[i].setBorder(new SoftBevelBorder(BevelBorder.RAISED));                     //�׵θ� ��Ÿ�� ����
         c.add(panel[i]);   //�����̳ʿ� ���
      }
   }
   //������ġ����
   public void setNextLocation(){
      for (int i=0; i<panel.length; i++){               //i�� 0���� 3����
         int x = block[i].getX() + (xCnt-3);            //���� ��� x���� 14-3�� ���� x��ǥ�� ����
         int y = block[i].getY() + 1;               //���� ��� y���� 1�� ���� y��ǥ�� ����
         panel[i].setLocation(x * area, y * area);      //panel�� �������� x,y�� �������� ���̿� ���̸� 20���� �����Ѵ�.
      }
      this.currentXY.setXY((xCnt-3),1);               //�ֱ� ���� x,y���� ���� (11,1�� ����ȴ�.) -> �������� ��Ż�� ��ġ
   }
   //������ġ����
   public void setDefaultLocation(){
      for (int i=0; i<panel.length; i++){               //i�� 0���� 3����
         int x = block[i].getX() + (int)(xCnt/2-2);      //���� ��� x���� 14/2-2�� ���� x��ǥ�� ����
         int y = block[i].getY() +2;                  //���� ��� y���� 2�� ���� y��ǥ�� ����
         panel[i].setLocation(x * area, y * area);      //panel�� ���������� x,y�� �������� ���̿� ���̸� 20���� �����Ѵ�.
      }
      this.currentXY.setXY((int)(xCnt/2-2),3);         //�ֱ� ���� x,y���� ���� (5,2�� ����ȴ�.) -> ���� �������� �� ������ġ
   }
   //������ ��ġ����
   public void setReadyLocation(){
      for (int i=0; i<panel.length; i++){               //i�� 0���� 3���� 4�� �ݺ�
         panel[i].setLocation(((block[i].getX()) * area)-100, ((block[i].getY()) * area)-100);   //�⺻��ġ �Ⱥ��̴� ���� ����
      }
   }
   //������ġ����
   public void setCurrentXY(int x, int y){
      this.currentXY.move(x,y);
   }
   //������ġ��ȯ
   public Block getCurrentXY(){
      return this.currentXY;
   }
   //���� ����Ʈ ����
   public Block[] getBlock(){
      Block[] temp = new Block[cnt];                     //temp��� Block[4]��ŭ�� �迭����
      for (int i=0; i<block.length; i++){                  //i�� 0���� 3����
         int x = block[i].getX() + this.currentXY.getX();  //currentXY�� ����� x�� ���� block[i]�� x�� ���� x��ǥ�� ���� -> �簢���� ���ε�� 4���� ���� ù��°��  
         int y = block[i].getY() + this.currentXY.getY();  //currentXY�� ����� y�� ���� block[i]�� y�� ���� y��ǥ�� ����   -> 0,0�� 0�� currentXY�� x��ǥ�� ����
         temp[i] = new Block(x,y);      //temp[4]�� ���� ��ġ�� ���� ��ġ�� ���� ��Ƽ�
      }         
      return temp;                  //temp[4]�� ����
   }
   //���������ϰ����� ����Ʈ���� ��ȯ
   public Block[] getNextBlock(){
      int nextAngle;                                    //���� ���� ����
      if(this.angle==1)   return getBlock();                  //������1�����̸� ���� ���� ����
      else if(this.angle-1 == this.current_angle)   nextAngle=0;   //���簡 �������ޱ��̸� ù���� ������
      else   nextAngle=this.current_angle+1;                  //�������� ����
      
      Block[] temp = new Block[cnt];                        //temp��� �̸����� Block[4]�� �迭 ����
      for (int i=0; i<block.length; i++){                     //i�� 0���� 3���� 4�� �ݺ�
         int x = block_info[nextAngle][i].getX() + this.currentXY.getX();   //���������� �� x�� ������ x�� ���� x�� ��ǥ�� ���� 
         int y = block_info[nextAngle][i].getY() + this.currentXY.getY();   //���������� �� y�� ������ y�� ���� y�� ��ǥ�� ����
         temp[i] = new Block(x,y);         //���ο� ���� ������ temp�� ������
      }
      return temp;                     //temp�� ����
   }
   //����ޱ۸���
   public int getCurrentAngle(){
      return this.current_angle;
   }
   //������Ʈ
   public void moveRotate(){
      if(this.angle==1)   return;                        //������1�����̸� ����
      if(this.current_angle+1 == this.angle){               //�ְ����� ó�������� -> current_angle�� ������ 0~3������
         this.block = this.block_info[0];               //����0�� �ش��ϴ� ������ ����
         this.current_angle = 0;                        //������ 0���� ����
      }else{                                       //�׿ܿ���
         this.current_angle++;                        //������ �ϳ� ������Ű��
         this.block = this.block_info[this.current_angle];   //���������� �ش��ϴ� ������ ����
      }
      this.setMove();                                 //�������� �ǳڿ� ����
   }
   //������ ����Ʈ ������ �ǳڿ� �����Ͽ� �������� 
   public void setMove(){
      for (int i=0; i<panel.length; i++){                        //i�� 0���� 3����
         //�������� x,y���� ����x,y����Ʈ���� ���Ѱ��� ��area���� ���Ѵ�.
         int x = this.block[i].getX() + this.currentXY.getX();
         int y = this.block[i].getY() + this.currentXY.getY();;
         panel[i].setLocation(x * area, y * area);               //�ǳ��� ��ġ�� ����
      }
   }
   //�Ʒ��� ��ĭ ������
   public void moveDown(){
      this.currentXY.move(0,1);      //y��ǥ�� �ϳ� �������� �Ʒ��� ��ĭ �̵�
      this.setMove();               //�������� �ǳڿ� ����
   }
   //���������� ��ĭ ������
   public void moveRight(){
      this.currentXY.move(1,0);      //x��ǥ�� �ϳ� �������� ���������� ��ĭ �̵�
      this.setMove();               //�������� �ǳڿ� ����
   }
   //�������� ��ĭ ������
   public void moveLeft(){
      this.currentXY.move(-1,0);      //x��ǥ�� �ϳ� ���ҽ��� �������� ��ĭ �̵�
      this.setMove();               //�������� �ǳڿ� ����
   }
   //���� �� ����
   public Color getColor(){
      return this.color;
   }
   //���� �� ����
   public void setColor(Color c){
      this.color = c;
      for (int i=0; i<panel.length; i++){
         panel[i].setBackground(this.color);
      }
   }
}



//�Ʒ��� �� ������ ��ü�� -> �����ڰ� ȣ��Ǹ� ���ڷ� area(20) center(JPanel), xCnt(14)�� �Ѱܹ޴´�. ----------------------------------------------
//�׸��� �� ������ area(20)�� xCnt(14)�� �θ�(Item Class)�� ������ ���ڷ� �ѱ��. --------------------------------------------------------------

//�簢�� 
class Rect extends Item
{
   public Rect(int area, Container con, int xCnt){
      super(area, 1, 4, xCnt);   //��������, ��������, �ǳڰ���

      block_info[0][0] = new Block(0,0);
      block_info[0][1] = new Block(0,1);
      block_info[0][2] = new Block(1,0);
      block_info[0][3] = new Block(1,1);

      this.setDefaultRandom();   //�������� -> �θ𿡰Լ� ��ӹ��� �Լ�
      this.setItem(con);         //�����̳ʿ� ��� -> �θ𿡰Լ� ��ӹ��� �Լ�
   }
}

//�����
class OneThree extends Item
{
   public OneThree(int area, Container con, int xCnt){
      super(area, 4, 4, xCnt);   //��������, ��������, �ǳڰ���

      block_info[0][0] = new Block(0,0);
      block_info[0][1] = new Block(0,1);
      block_info[0][2] = new Block(1,1);
      block_info[0][3] = new Block(2,1);

      block_info[1][0] = new Block(0,2);
      block_info[1][1] = new Block(1,2);
      block_info[1][2] = new Block(1,1);
      block_info[1][3] = new Block(1,0);

      block_info[2][0] = new Block(2,1);
      block_info[2][1] = new Block(2,0);
      block_info[2][2] = new Block(1,0);
      block_info[2][3] = new Block(0,0);

      block_info[3][0] = new Block(1,0);
      block_info[3][1] = new Block(0,0);
      block_info[3][2] = new Block(0,1);
      block_info[3][3] = new Block(0,2);

      this.setDefaultRandom();   //�������� -> �θ𿡰Լ� ��ӹ��� �Լ�
      this.setItem(con);         //�����̳ʿ� ��� -> �θ𿡰Լ� ��ӹ��� �Լ�
   }
}

//����� �ݴ�
class ThreeOne extends Item
{
   public ThreeOne(int area, Container con, int xCnt){
      super(area, 4, 4, xCnt);   //��������, ��������, �ǳڰ���

      block_info[0][0] = new Block(0,1);
      block_info[0][1] = new Block(0,0);
      block_info[0][2] = new Block(1,0);
      block_info[0][3] = new Block(2,0);

      block_info[1][0] = new Block(1,2);
      block_info[1][1] = new Block(0,2);
      block_info[1][2] = new Block(0,1);
      block_info[1][3] = new Block(0,0);

      block_info[2][0] = new Block(2,0);
      block_info[2][1] = new Block(2,1);
      block_info[2][2] = new Block(1,1);
      block_info[2][3] = new Block(0,1);

      block_info[3][0] = new Block(0,0);
      block_info[3][1] = new Block(1,0);
      block_info[3][2] = new Block(1,1);
      block_info[3][3] = new Block(1,2);

      this.setDefaultRandom();   //�������� -> �θ𿡰Լ� ��ӹ��� �Լ�
      this.setItem(con);         //�����̳ʿ� ��� -> �θ𿡰Լ� ��ӹ��� �Լ�
   }
}

//����
class LineBlock extends Item
{
   public LineBlock(int area, Container con, int xCnt){
      super(area, 2, 4, xCnt);   //��������, ��������, �ǳڰ���

      block_info[0][0] = new Block(0,-1);
      block_info[0][1] = new Block(0,0);
      block_info[0][2] = new Block(0,1);
      block_info[0][3] = new Block(0,2);

      block_info[1][0] = new Block(-1,0);
      block_info[1][1] = new Block(0,0);
      block_info[1][2] = new Block(1,0);
      block_info[1][3] = new Block(2,0);

      this.setDefaultRandom();   //�������� -> �θ𿡰Լ� ��ӹ��� �Լ�
      this.setItem(con);         //�����̳ʿ� ��� -> �θ𿡰Լ� ��ӹ��� �Լ�
   }
}

//�� ����~ ����
class Triangle extends Item
{
   public Triangle(int area, Container con, int xCnt){
      super(area, 4, 4, xCnt);   //��������, ��������, �ǳڰ���

      block_info[0][0] = new Block(1,0);
      block_info[0][1] = new Block(0,1);
      block_info[0][2] = new Block(1,1);
      block_info[0][3] = new Block(2,1);

      block_info[1][0] = new Block(0,0);
      block_info[1][1] = new Block(0,1);
      block_info[1][2] = new Block(0,2);
      block_info[1][3] = new Block(1,1);

      block_info[2][0] = new Block(0,0);
      block_info[2][1] = new Block(1,0);
      block_info[2][2] = new Block(2,0);
      block_info[2][3] = new Block(1,1);

      block_info[3][0] = new Block(0,1);
      block_info[3][1] = new Block(1,0);
      block_info[3][2] = new Block(1,1);
      block_info[3][3] = new Block(1,2);

      this.setDefaultRandom();   //�������� -> �θ𿡰Լ� ��ӹ��� �Լ�
      this.setItem(con);         //�����̳ʿ� ��� -> �θ𿡰Լ� ��ӹ��� �Լ�
   }
}

//_|- ����? ����
class RightBlock extends Item
{
   public RightBlock(int area, Container con, int xCnt){
      super(area, 2, 4, xCnt);   //��������, ��������, �ǳڰ���

      block_info[0][0] = new Block(0,0);
      block_info[0][1] = new Block(0,1);
      block_info[0][2] = new Block(1,1);
      block_info[0][3] = new Block(1,2);

      block_info[1][0] = new Block(1,0);
      block_info[1][1] = new Block(0,0);
      block_info[1][2] = new Block(0,1);
      block_info[1][3] = new Block(-1,1);

      this.setDefaultRandom();   //�������� -> �θ𿡰Լ� ��ӹ��� �Լ�
      this.setItem(con);         //�����̳ʿ� ��� -> �θ𿡰Լ� ��ӹ��� �Լ�
   }
}

//-|_ ���� ������
class LeftBlock extends Item
{
   public LeftBlock(int area, Container con, int xCnt){
      super(area, 2, 4, xCnt);   //��������, ��������, �ǳڰ���

      block_info[0][0] = new Block(0,0);
      block_info[0][1] = new Block(1,0);
      block_info[0][2] = new Block(1,1);
      block_info[0][3] = new Block(2,1);

      block_info[1][0] = new Block(0,1);
      block_info[1][1] = new Block(0,0);
      block_info[1][2] = new Block(1,0);
      block_info[1][3] = new Block(1,-1);

      this.setDefaultRandom();   //�������� -> �θ𿡰Լ� ��ӹ��� �Լ�
      this.setItem(con);         //�����̳ʿ� ��� -> �θ𿡰Լ� ��ӹ��� �Լ�
   }
}