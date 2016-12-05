package day1202;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

//��Ʈ���� ����~
public class Tetris extends JFrame implements Runnable, KeyListener
{
   private int width;         //����
   private int height;         //����
   private   int xCnt;         //���ι迭ũ��
   private   int yCnt;         //���ι迭ũ��
   private int area;         //���μ��α���
   private int time;         //������
   private boolean[][] grid;   //
   private JPanel[][] background;   //����ǳ�
   private Container fc;      //�����̳�
   private Item nextItem;      //�������ð�
   private Item currentItem;   //������ ������
   private ArrayList<Item> itemList;   //�����۸���Ʈ
   private ArrayList<Color> colorList;   //�÷�����Ʈ
   private Random rnd;
   private JPanel top, next, center;         //��� �����ºκ�
   private boolean isKey = true;      //Ű����Ȱ��ȭ����
   private final Color bgColor = Color.white;   //����÷�
//   public static boolean isRight = false;      //�����ʿ���
   Thread t;

   public Tetris(String str){
      //========== �⺻���� ���� ���� ===========
      this.setTitle(str);                        //������
      this.xCnt = 14;                           //x�� ���� �迭 14ĭ
      this.yCnt = 25;                           //y�� ���� �迭 25ĭ
      this.time = 500;                        //�������� ������
      this.area = 20;                           //�迭 ��ĭ�� �ش��ϴ� ũ��
      this.width = this.xCnt * this.area;            //�迭 ���� ��ĭ�� area�� ���� ���̸� ����
      this.height = this.yCnt * this.area;         //�迭 ���� ��ĭ�� area�� ���� ���̸� ����
      this.itemList = new ArrayList<Item>();         //�������� ���� list����
      this.background = new JPanel[this.xCnt][this.yCnt];   //����ǳ��� [14][25]�� 2���� �迭�� ����
      this.grid = new boolean[this.xCnt][this.yCnt];      //boolean������ [14][25]�� 2���� �迭�� ����
      this.rnd = new Random(System.currentTimeMillis());   //���� �ð����������� random�� ����

      this.fc = this.getContentPane();               //�������� contentPane��ü�� ���´�

      this.center = new JPanel();                     //���� ǥ���� �κ��� �г� ����
      this.center.setSize(this.width, this.height);      //ũ�⸦ width�� height�� ���� -> ������� width�� �迭�� ũ��(14)�� area(20)������ 280�̵ȴ�.
      this.center.setLayout(null);                  //���̾ƿ� ��������
      this.center.setBackground(new Color(224,255,216));   //���� ����
      this.fc.add(this.center, "Center");               //�����̳ʿ� �г��� ���δ�.

      this.addKeyListener(this);                        //Ű�����̺�Ʈ ����
      this.setBounds(200,200,this.width+8,this.height+13);   //�����쿡 �г��� �ٿ����� ��ġ(��ǻ�� ȭ�鿡 �������� ��ġ)��, â�� ���̿� ���� ����

      //========== �⺻���� ���� �� ===========

      //������ �߰��ϱ� -> �����ڸ� ���� ����� ���ڷ� area(20) center(JPanel), xCnt(14)�� �ѱ��. 
      itemList.add(new Rect(this.area, this.center, this.xCnt));         //�簢��
      itemList.add(new OneThree(this.area, this.center, this.xCnt));      //�����
      itemList.add(new ThreeOne(this.area, this.center, this.xCnt));      //����� �ݴ�
      itemList.add(new LineBlock(this.area, this.center, this.xCnt));      //����
      itemList.add(new Triangle(this.area, this.center, this.xCnt));      //����
      itemList.add(new RightBlock(this.area, this.center, this.xCnt));   //S���
      itemList.add(new LeftBlock(this.area, this.center, this.xCnt));      //Z���

      //�� �߰� -> ArrayList�� �ϳ��� �߰��Ѵ�.
      this.colorList = new ArrayList<Color>();
      this.colorList.add(Color.red);
      this.colorList.add(Color.blue);
      this.colorList.add(Color.green);
      this.colorList.add(Color.orange);
      this.colorList.add(Color.pink);
      this.colorList.add(new Color(170,40,150));   //����

      //��� ���� ����======
      this.top = new JPanel();                           //top�̶�� JPanel����
      this.next = new JPanel();                           //next��� JPanel����
      this.top.setBounds(0,0,this.xCnt*this.area, this.area*4);   //top(JPanel)�� ��ġ�� 0,0(��������)�����̸� ���̴� 14*20, ���̴� 20*4�̴�.
      this.top.setBackground(new Color(244,211,99));            //top�� ���� ����
      this.next.setBounds((this.xCnt-4)*this.area,0,this.area*4, this.area*4);   //�������� ǥ���Ǵ� next(JPanel)�� ��ġ�� (14-4)*20,0�̸� ���̴� 20*4, ���̴� 20*4�̴�.
      this.next.setBackground(new Color(245,180,250));                     //next�� ���� ����

      this.center.add(this.top);            //center�� top�� �ְ�
      this.top.setLayout(null);
      this.top.add(this.next);            //top�� next�� �־ ���̾ƿ� ��ġ�� �ϼ��Ѵ�.

      //��� ���� ��======

      //��׶��� �г� ���� ���� ==========
      for (int i=0; i<background.length; i++){               //i�� 0���� 24����
         for (int p=0; p<background[i].length; p++){            //p�� 0���� 13���� ���� -> �� 25*14�� �ݺ�
            this.background[i][p] = new JPanel();            //0,0���� 25,14���� ��� �迭�� JPanel�� ����
            this.background[i][p].setBounds(i * this.area, p * this.area, this.area, this.area); //�� �гε��� ��ġ�� ����, ���г��� ���̿� ���̴� ���� 20
            this.background[i][p].setBackground(this.bgColor);   //���� ����
            this.center.add(background[i][p]);               //center(JPanel)�� ��ġ�� ������ �� �г��� �߰� -> center�гο��� top�гΰ� 25*14���� background �г����ִ�. 
         }
      }
      //��׶��� �г� ���� ���� ==========

      //������ ���ۼ���
      this.currentItem = itemList.get(rnd.nextInt(itemList.size()));                        //������ �����ۿ� ����Ʈ�� ������ �� �ϳ� �������� �̾Ƽ� ����
      this.currentItem.setColor(this.colorList.get(this.rnd.nextInt(this.colorList.size())));      //���� �������� ����
      this.currentItem.setDefaultLocation();                                          //������ġ���� -> ItemŬ������ setDefaultLocation()�޼ҵ� ����                                                         
      setNextItem();                                                            //������ ����
   
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);
//      this.setResizable(false);
      t = new Thread(this);         //�����带 �ϳ� ����
      t.start();                  //�����带 ������ run�޼ҵ� �κ� ���� -> run�޼ҵ�� ������time������ sleep�� �����ϸ鼭 goDown(������)�� �ݺ��Ѵ�
   }
   //�ؽ�Ʈ ������ ����
   public void setNextItem(){
      Item temp;
      do{
         temp = itemList.get(rnd.nextInt(itemList.size()));      //�����۸���Ʈ �߿��� ���������� �ϳ� ����
      }
      while (temp==this.currentItem);                        //��������۰� �ߺ�X
      this.nextItem = temp;                              //������ ����
      this.nextItem.setColor(this.colorList.get(this.rnd.nextInt(this.colorList.size())));   //�������� ������
      this.nextItem.setNextLocation();                                          //��ġ���� -> ItemŬ������ setNextLocation()�Լ� ����
   }
   //������ ���� ������ ����
   public void setNewItem(){
      this.currentItem = this.nextItem;
      this.currentItem.setDefaultLocation();
      setNextItem();
   }
   //��׶��� �� ä���
   public void setBack(int x, int y, Color c){                                 //���ڰ����� �� ����� x,y�� ��ǥ���� ������ �޴´�/.
      this.background[x][y].setBackground(c);                                 //x,y�� ��ǥ������ �ϴ� ���� �ǳ��� ����� ĥ�Ѵ�
      this.background[x][y].setBorder(new SoftBevelBorder(BevelBorder.RAISED));      //�׵θ� ��Ÿ���� �����Ѵ�.
      this.grid[x][y] = true;                                             //ä���� �迭�� ��ġ�� true�� �����Ͽ� ���� ä������ ��Ÿ����.
//      System.out.println("x="+x+", y="+y);
   }
   //��׶��� �� ����
   public void setEmptyBack(int x, int y){                                 //���ڰ����� ������ x,y��ǥ�� �޴´�.
      this.background[x][y].setBorder(null);                              //�׵θ� �Ӽ��� ����
      this.background[x][y].setBackground(this.bgColor);                     //����� ĵ������ ������ �������� ����
      this.grid[x][y] = false;                                       //������ �迭�� ��ġ�� false�� �����Ͽ� ���� ������ ��Ÿ����.
   }
   //������ ��� ��׶���� ����
   public void setCopyBlock(){
      Block[] tempBlock = this.currentItem.getBlock();                           //���� ���� ��ġ�� �����´� 
      for (int i=0; i<tempBlock.length; i++){                                    //i�� 0���� 3����
         setBack(tempBlock[i].getX(), tempBlock[i].getY(), this.currentItem.getColor());   //setBack�Լ��� ������� x,y��ǥ���� ������ �Ѱ��ش�.
      }
      this.currentItem.setReadyLocation();   //�����ġ�� ���ư���
   }
   //�پ��ֱ� üũ
   public void checkLine(){
      for (int i=0; i<grid[0].length; i++){                     // i = Y�� = ROW -> �� i�� 0���� 24���� 25��
//         System.out.println(
         boolean isLine = true;
         for (int p=0; p<grid.length; p++){                     // p = X�� = Column -> �� p�� 0���� 13���� 14��
//            System.out.print(p+","+i+" : " + grid[p][i]);
            if(!grid[p][i]){                        //�ϳ��� ������ ������ break;
               isLine = false;                        
               break;
            }
         }
         if(isLine){                                 //������ ���ٸ� �ش� �� ����
            deleteLine(i);                           //deleteline�޼ҵ��� ���ڷ� row��ȣ�� �Ѱ��ش�. 
            System.out.println(i + "�� ����");               //������� �� ������ĭ�� �����Ѵٸ� 23�� ���ڷ� ����
         }
      }
   }
   //�پ��ְ� ������ ��ĭ�� ������
   public void deleteLine(int line){
      boolean temp[] = new boolean[xCnt];                  //temp��� �̸����� boolean[14]��ŭ�� �迭����
      JPanel   tempPanel[] = new JPanel[xCnt];               //tempPanel�̶�� �̸����� JPanel[14]��ŭ�� �迭����

      
      for (int i=line; i>0; i--){                     // i = �� = Y    -> ���ڰ����� �Ѿ�� line(������ ��)���� 0���� i�� �����Ѵ�
         for (int p=0; p<grid.length; p++){            // p = �� = X   -> p�� 0���� 13���� 14�� �ݺ�
            if(i==line){                           //�������ΰ��
               tempPanel[p] = background[p][i];         //�����ٿ� �ִ� ����г��� �� temp�� ����
               tempPanel[p].setLocation(p*this.area,0);   //??????????
            }
            //����� ��ĭ�� ������
            grid[p][i] = grid[p][i-1];
            background[p][i] = background[p][i-1];
            background[p][i].setLocation(p*this.area, i*this.area);      //background�ǳ��� ���پƷ� ��ġ�� ����
         }
      }
      //������ ������ �ø���
      for (int i=0; i<grid.length; i++){      //i�� 0���� 13���� 14�� �ݺ�
         background[i][0] = tempPanel[i];   //�����ٿ� temp�� ����Ǿ��ִ� �ǾƷ� �г� ����
         setEmptyBack(i,0);               //�� ���� ���� ��� ���� ����
      }
   }
   //����Ʈ������� �ӽ�
   public void printInfo(){
      Block temp = this.currentItem.getCurrentXY();
      System.out.println("x : " + temp.getX() + ", y : " + temp.getY());
   }
   //������ ȸ��üũ -> ȸ��
   public void goRotate(){
      Block[] tempBlock = this.currentItem.getNextBlock();            //���� ������ ���� �� ������ �޾ƿ´�.
      for (int i=0; i<tempBlock.length; i++){                        //i�� 0���� 3���� 4�� �ݺ�
         int x = tempBlock[i].getX();                           //�� ���� x�� ��ǥ
         int y = tempBlock[i].getY();                           //�� ���� y�� ��ǥ
         if(x<0 || x>=this.xCnt || y+1>=this.yCnt || this.grid[x][y]){   //���� x�� 0���� �۰�(���ʺ��� ������ �Ѱų�) / x�� xCnt(14)���� ũ�ų� ������(������ ������ �Ѱų�) /
            return;                                          //y+1�� yCnt(25)���� ũ�ų� ������(�������ٿ����� �°��) / grid[x][y]�� true(���� �ִ°�) �̸�
         }                                                //���� �Լ�(goRatate) ����
      }
      this.currentItem.moveRotate();                              //�ƹ� ������ ������ ���� ȸ����Ų��.
   }
   //�����۴ٿ�üũ -> �̵�
   public boolean goDown(){
      Block[] tempBlock = this.currentItem.getBlock();         //���� currentItem ���� ��ġ�� ã�Ƽ� tempBlock[4]�� ��´�. 
      for (int i=0; i<tempBlock.length; i++){                  //i�� 0���� 3����
         int x = tempBlock[i].getX();                     //�� ���� x�� x��ǥ�� ����
         int y = tempBlock[i].getY() + 1;                  //�� ���� y�� 1���� y��ǥ�� ���� -> �� y������ ��ĭ �Ʒ��� y��ǥ�� �缳����
         if(y+1 >= this.yCnt || this.grid[x][y]){            //���� ������ [���� ��(���� ĭ)]�� �����̳��� ���̺��� Ŭ��� -> 23���� ������ / �Ǵ� grid[x][y]�� false��� �� ���� �̹� �ִ°��  
            if(!this.isKey)   gameEnd();                     //isKey�� false�̸� �� top�ǳڱ��� ���� �� ��� ���ӳ�
            setCopyBlock();                  //��׶���� ����
            checkLine();                  //�پ��ֱ� üũ
            setNewItem();                  //���������� ����
            return false;
         }
      }
      this.currentItem.moveDown();            //currentXY�� ���� y�� ���� +1�������� �Ʒ��� ��ĭ �̵�
      return true;                        //true�� ����
   }
   //�����ۿ������̵�üũ -> �̵�
   public void goRight(){
      Block[] tempBlock = this.currentItem.getBlock();      //���� currentItem ���� ��ġ�� ã�Ƽ� tempBlock[4]�� ��´�. 

      for (int i=0; i<tempBlock.length; i++){               //i�� 0���� 3���� 4���ݺ�
         int x = tempBlock[i].getX()+1;                  //������ x�� 1�� ���� x��ǥ�� ����
         int y = tempBlock[i].getY();                  //������ y�� y��ǥ�� ����
         if(x >= this.xCnt || this.grid[x][y]){            //x�� xCnt(14)���� ũ�ų� ������(�����ʺ������� ������) / Ȥ�� �̹� ���� ������
            return;                                 //�Լ�����
         }
      }
      this.currentItem.moveRight();                     //�� ���� ���� ���������� �̵�
   }
   //�����ۿ����̵�üũ -> �̵�
   public void goLeft(){
      Block[] tempBlock = this.currentItem.getBlock();      //���� currentItem ���� ��ġ�� ã�Ƽ� tempBlock[4]�� ��´�. 

      for (int i=0; i<tempBlock.length; i++){               //i�� 0���� 3���� 4���ݺ�
         int x = tempBlock[i].getX()-1;                  //������ x�� 1�� ������  x��ǥ�� ����
         int y = tempBlock[i].getY();                  //������ y�� y��ǥ�� ����
         if(x < 0 || this.grid[x][y]){                  //x�� 0���� ������(���ʺ������� ������) / Ȥ�� �̹� ���� ������
            return;                                 //�Լ�����
         }
      }
      this.currentItem.moveLeft();                     //�� ���� ���� �������� �̵�
   }
   //�������ֱ� üũ -> ���ֱ�
   //Ű����׼Ǹ�����
   public void keyPressed(KeyEvent e){
      if(!this.isKey)   return;
      switch (e.getKeyCode()){
         case KeyEvent.VK_DOWN:
            goDown();
            break;
         case KeyEvent.VK_LEFT:
            goLeft();
            break;
         case KeyEvent.VK_RIGHT:
            goRight();
            break;
         case KeyEvent.VK_UP:
            goRotate();
            break;
         case KeyEvent.VK_SPACE:
            while(goDown()){} //���� ������ ���������� �ݺ��ؼ� �����ٰ� ���� ������ false�� ������ while���� �������´�
            break;
      }
   }
   public void keyReleased(KeyEvent e){}
   public void keyTyped(KeyEvent e){}
   //��������üũ   
   public void gameEnd(){
      JOptionPane.showMessageDialog(null, "������ ����Ǿ����ϴ�.", "��������", JOptionPane.ERROR_MESSAGE);
      t.stop();
   }


   //���������
   public void run(){
      try
      {
         while(true){
            Thread.sleep(this.time);                                    //500milliseconds��ŭ sleep
            if(this.currentItem.getCurrentXY().getY() < 3)   this.isKey = false;      //�ǳ������̸� Ű������ ����X
            else   this.isKey = true;                                    //�׿��� ���� ����O
            goDown();                                                //�����۹������̵�
         }
      }
      catch (Exception e){
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args) 
   {
      new Tetris("Tetris by 1��");
   }
}