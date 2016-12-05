package day1202;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

//테트리스 게임~
public class Tetris extends JFrame implements Runnable, KeyListener
{
   private int width;         //가로
   private int height;         //세로
   private   int xCnt;         //가로배열크기
   private   int yCnt;         //세로배열크기
   private int area;         //가로세로길이
   private int time;         //빠르기
   private boolean[][] grid;   //
   private JPanel[][] background;   //배경판넬
   private Container fc;      //컨테이너
   private Item nextItem;      //다음나올것
   private Item currentItem;   //현재의 아이템
   private ArrayList<Item> itemList;   //아이템리스트
   private ArrayList<Color> colorList;   //컬러리스트
   private Random rnd;
   private JPanel top, next, center;         //상단 가리는부분
   private boolean isKey = true;      //키보드활성화여부
   private final Color bgColor = Color.white;   //배경컬러
//   public static boolean isRight = false;      //오른쪽여부
   Thread t;

   public Tetris(String str){
      //========== 기본설정 셋팅 시작 ===========
      this.setTitle(str);                        //제목설정
      this.xCnt = 14;                           //x축 방향 배열 14칸
      this.yCnt = 25;                           //y축 방향 배열 25칸
      this.time = 500;                        //내려오는 빠르기
      this.area = 20;                           //배열 한칸에 해당하는 크기
      this.width = this.xCnt * this.area;            //배열 가로 한칸에 area를 곱해 넓이를 설정
      this.height = this.yCnt * this.area;         //배열 세로 한칸에 area를 곱해 높이를 설정
      this.itemList = new ArrayList<Item>();         //아이템을 담을 list선언
      this.background = new JPanel[this.xCnt][this.yCnt];   //배경판넬을 [14][25]의 2차원 배열로 선언
      this.grid = new boolean[this.xCnt][this.yCnt];      //boolean변수를 [14][25]의 2차원 배열로 선언
      this.rnd = new Random(System.currentTimeMillis());   //현재 시간을기준으로 random값 설정

      this.fc = this.getContentPane();               //프레임의 contentPane객체를 얻어온다

      this.center = new JPanel();                     //블럭이 표현될 부분의 패널 선언
      this.center.setSize(this.width, this.height);      //크기를 width와 height로 설정 -> 예를들어 width는 배열의 크기(14)와 area(20)을곱해 280이된다.
      this.center.setLayout(null);                  //레이아웃 지정안함
      this.center.setBackground(new Color(224,255,216));   //배경색 설정
      this.fc.add(this.center, "Center");               //컨테이너에 패널이 붙인다.

      this.addKeyListener(this);                        //키보드이벤트 설정
      this.setBounds(200,200,this.width+8,this.height+13);   //윈도우에 패널을 붙여넣을 위치(컴퓨터 화면에 보여지는 위치)와, 창의 넓이와 높이 지정

      //========== 기본설정 셋팅 끝 ===========

      //아이템 추가하기 -> 생성자를 통해 만들고 인자로 area(20) center(JPanel), xCnt(14)를 넘긴다. 
      itemList.add(new Rect(this.area, this.center, this.xCnt));         //사각형
      itemList.add(new OneThree(this.area, this.center, this.xCnt));      //기억자
      itemList.add(new ThreeOne(this.area, this.center, this.xCnt));      //기억자 반대
      itemList.add(new LineBlock(this.area, this.center, this.xCnt));      //일자
      itemList.add(new Triangle(this.area, this.center, this.xCnt));      //산모양
      itemList.add(new RightBlock(this.area, this.center, this.xCnt));   //S모양
      itemList.add(new LeftBlock(this.area, this.center, this.xCnt));      //Z모양

      //색 추가 -> ArrayList에 하나씩 추가한다.
      this.colorList = new ArrayList<Color>();
      this.colorList.add(Color.red);
      this.colorList.add(Color.blue);
      this.colorList.add(Color.green);
      this.colorList.add(Color.orange);
      this.colorList.add(Color.pink);
      this.colorList.add(new Color(170,40,150));   //보라

      //상단 셋팅 시작======
      this.top = new JPanel();                           //top이라는 JPanel생성
      this.next = new JPanel();                           //next라는 JPanel생성
      this.top.setBounds(0,0,this.xCnt*this.area, this.area*4);   //top(JPanel)의 위치는 0,0(좌측맨위)부터이며 넓이는 14*20, 놃이는 20*4이다.
      this.top.setBackground(new Color(244,211,99));            //top의 배경색 설정
      this.next.setBounds((this.xCnt-4)*this.area,0,this.area*4, this.area*4);   //다음블럭이 표현되는 next(JPanel)의 위치는 (14-4)*20,0이며 넓이는 20*4, 높이는 20*4이다.
      this.next.setBackground(new Color(245,180,250));                     //next의 배경색 설정

      this.center.add(this.top);            //center에 top을 넣고
      this.top.setLayout(null);
      this.top.add(this.next);            //top에 next를 넣어서 레이아웃 배치를 완성한다.

      //상단 셋팅 끝======

      //백그라운드 패널 셋팅 시작 ==========
      for (int i=0; i<background.length; i++){               //i는 0부터 24까지
         for (int p=0; p<background[i].length; p++){            //p는 0부터 13까지 증가 -> 총 25*14번 반복
            this.background[i][p] = new JPanel();            //0,0부터 25,14까지 모든 배열에 JPanel을 생성
            this.background[i][p].setBounds(i * this.area, p * this.area, this.area, this.area); //그 패널들의 위치를 설정, 각패널의 넓이와 높이는 각각 20
            this.background[i][p].setBackground(this.bgColor);   //배경색 설정
            this.center.add(background[i][p]);               //center(JPanel)에 위치가 지정된 각 패널을 추가 -> center패널에는 top패널과 25*14개의 background 패널이있다. 
         }
      }
      //백그라운드 패널 셋팅 시작 ==========

      //아이템 시작셋팅
      this.currentItem = itemList.get(rnd.nextInt(itemList.size()));                        //현재의 아이템에 리스트의 아이템 중 하나 랜덤으로 뽑아서 대입
      this.currentItem.setColor(this.colorList.get(this.rnd.nextInt(this.colorList.size())));      //색상도 랜덤으로 설정
      this.currentItem.setDefaultLocation();                                          //시작위치조정 -> Item클래스의 setDefaultLocation()메소드 참조                                                         
      setNextItem();                                                            //다음블럭 지정
   
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);
//      this.setResizable(false);
      t = new Thread(this);         //쓰레드를 하나 생성
      t.start();                  //쓰레드를 시작해 run메소드 부분 실행 -> run메소드는 설정된time값으로 sleep을 실행하면서 goDown(블럭내림)을 반복한다
   }
   //넥스트 아이템 셋팅
   public void setNextItem(){
      Item temp;
      do{
         temp = itemList.get(rnd.nextInt(itemList.size()));      //아이템리스트 중에서 랜덤값으로 하나 선택
      }
      while (temp==this.currentItem);                        //현재아이템과 중복X
      this.nextItem = temp;                              //다음블럭 설정
      this.nextItem.setColor(this.colorList.get(this.rnd.nextInt(this.colorList.size())));   //랜덤으로 색지정
      this.nextItem.setNextLocation();                                          //위치설정 -> Item클래스의 setNextLocation()함수 참조
   }
   //아이템 새로 나오기 셋팅
   public void setNewItem(){
      this.currentItem = this.nextItem;
      this.currentItem.setDefaultLocation();
      setNextItem();
   }
   //백그라운드 블럭 채우기
   public void setBack(int x, int y, Color c){                                 //인자값으로 각 블록의 x,y의 좌표값과 색상을 받는다/.
      this.background[x][y].setBackground(c);                                 //x,y의 좌표값으로 하는 곳의 판넬의 배경을 칠한다
      this.background[x][y].setBorder(new SoftBevelBorder(BevelBorder.RAISED));      //테두리 스타일을 설정한다.
      this.grid[x][y] = true;                                             //채워진 배열의 위치는 true로 설정하여 블럭이 채워짐을 나타낸다.
//      System.out.println("x="+x+", y="+y);
   }
   //백그라운드 블럭 비우기
   public void setEmptyBack(int x, int y){                                 //인자값으로 각블럭의 x,y좌표를 받는다.
      this.background[x][y].setBorder(null);                              //테두리 속성을 삭제
      this.background[x][y].setBackground(this.bgColor);                     //배경을 캔버스와 동일한 색상으로 변경
      this.grid[x][y] = false;                                       //지워진 배열의 위치는 false로 설정하여 블럭이 없음을 나타낸다.
   }
   //현재의 블록 백그라운드로 복사
   public void setCopyBlock(){
      Block[] tempBlock = this.currentItem.getBlock();                           //현재 블럭의 위치를 가져온다 
      for (int i=0; i<tempBlock.length; i++){                                    //i는 0부터 3까지
         setBack(tempBlock[i].getX(), tempBlock[i].getY(), this.currentItem.getColor());   //setBack함수로 각블록의 x,y좌표값과 색상을 넘겨준다.
      }
      this.currentItem.setReadyLocation();   //대기위치로 돌아가기
   }
   //줄없애기 체크
   public void checkLine(){
      for (int i=0; i<grid[0].length; i++){                     // i = Y값 = ROW -> 즉 i는 0부터 24까지 25번
//         System.out.println(
         boolean isLine = true;
         for (int p=0; p<grid.length; p++){                     // p = X값 = Column -> 즉 p는 0부터 13까지 14번
//            System.out.print(p+","+i+" : " + grid[p][i]);
            if(!grid[p][i]){                        //하나라도 공백이 있으면 break;
               isLine = false;                        
               break;
            }
         }
         if(isLine){                                 //공백이 없다면 해당 줄 제거
            deleteLine(i);                           //deleteline메소드의 인자로 row번호를 넘겨준다. 
            System.out.println(i + "줄 없앰");               //예를들어 맨 마지막칸을 삭제한다면 23이 인자로 들어간다
         }
      }
   }
   //줄없애고 위에거 한칸씩 내리기
   public void deleteLine(int line){
      boolean temp[] = new boolean[xCnt];                  //temp라는 이름으로 boolean[14]만큼의 배열선언
      JPanel   tempPanel[] = new JPanel[xCnt];               //tempPanel이라는 이름으로 JPanel[14]만큼의 배열선언

      
      for (int i=line; i>0; i--){                     // i = 줄 = Y    -> 인자값으로 넘어온 line(삭제할 줄)부터 0까지 i를 감소한다
         for (int p=0; p<grid.length; p++){            // p = 열 = X   -> p는 0부터 13까지 14번 반복
            if(i==line){                           //현재줄인경우
               tempPanel[p] = background[p][i];         //현재줄에 있는 모든패널을 을 temp에 저장
               tempPanel[p].setLocation(p*this.area,0);   //??????????
            }
            //모든줄 한칸씩 내리기
            grid[p][i] = grid[p][i-1];
            background[p][i] = background[p][i-1];
            background[p][i].setLocation(p*this.area, i*this.area);      //background판넬을 한줄아래 위치에 설정
         }
      }
      //없앤줄 맨위로 올리기
      for (int i=0; i<grid.length; i++){      //i는 0부터 13까지 14번 반복
         background[i][0] = tempPanel[i];   //맨위줄에 temp에 저장되어있는 맨아래 패널 대입
         setEmptyBack(i,0);               //그 맨위 줄의 모든 블럭을 삭제
      }
   }
   //프린트정보출력 임시
   public void printInfo(){
      Block temp = this.currentItem.getCurrentXY();
      System.out.println("x : " + temp.getX() + ", y : " + temp.getY());
   }
   //아이템 회전체크 -> 회전
   public void goRotate(){
      Block[] tempBlock = this.currentItem.getNextBlock();            //다음 각도에 대한 블럭 정보를 받아온다.
      for (int i=0; i<tempBlock.length; i++){                        //i는 0부터 3까지 4번 반복
         int x = tempBlock[i].getX();                           //각 블럭의 x의 좌표
         int y = tempBlock[i].getY();                           //각 블럭의 y의 좌표
         if(x<0 || x>=this.xCnt || y+1>=this.yCnt || this.grid[x][y]){   //만약 x가 0보다 작고(왼쪽벽의 범위를 넘거나) / x가 xCnt(14)보다 크거나 같으면(오른쪽 범위를 넘거나) /
            return;                                          //y+1이 yCnt(25)보다 크거나 같으면(마지막줄에까지 온경우) / grid[x][y]가 true(블럭이 있는곳) 이면
         }                                                //현재 함수(goRatate) 종료
      }
      this.currentItem.moveRotate();                              //아무 제한이 없으면 블럭을 회전시킨다.
   }
   //아이템다운체크 -> 이동
   public boolean goDown(){
      Block[] tempBlock = this.currentItem.getBlock();         //현재 currentItem 블럭의 위치를 찾아서 tempBlock[4]에 담는다. 
      for (int i=0; i<tempBlock.length; i++){                  //i는 0에서 3까지
         int x = tempBlock[i].getX();                     //각 블럭의 x를 x좌표로 설정
         int y = tempBlock[i].getY() + 1;                  //각 블럭의 y를 1더해 y좌표로 설정 -> 즉 y축으로 한칸 아래에 y좌표가 재설정됨
         if(y+1 >= this.yCnt || this.grid[x][y]){            //블럭이 내려올 [다음 줄(다음 칸)]이 컨테이너의 높이보다 클경우 -> 23줄이 마지막 / 또는 grid[x][y]가 false경우 즉 블럭이 이미 있는경우  
            if(!this.isKey)   gameEnd();                     //isKey가 false이면 즉 top판넬까지 블럭이 찬 경우 게임끝
            setCopyBlock();                  //백그라운드블럭 셋팅
            checkLine();                  //줄없애기 체크
            setNewItem();                  //다음아이템 셋팅
            return false;
         }
      }
      this.currentItem.moveDown();            //currentXY의 값중 y의 값을 +1증가시켜 아래로 한칸 이동
      return true;                        //true를 리턴
   }
   //아이템오른쪽이동체크 -> 이동
   public void goRight(){
      Block[] tempBlock = this.currentItem.getBlock();      //현재 currentItem 블럭의 위치를 찾아서 tempBlock[4]에 담는다. 

      for (int i=0; i<tempBlock.length; i++){               //i는 0부터 3까지 4번반복
         int x = tempBlock[i].getX()+1;                  //각블럭의 x에 1을 더해 x좌표로 설정
         int y = tempBlock[i].getY();                  //각블럭의 y를 y좌표로 설정
         if(x >= this.xCnt || this.grid[x][y]){            //x가 xCnt(14)보다 크거나 같으면(오른쪽벽범위를 넘으면) / 혹은 이미 블럭이 있으면
            return;                                 //함수종료
         }
      }
      this.currentItem.moveRight();                     //그 외의 경우는 오른쪽으로 이동
   }
   //아이템왼쪽이동체크 -> 이동
   public void goLeft(){
      Block[] tempBlock = this.currentItem.getBlock();      //현재 currentItem 블럭의 위치를 찾아서 tempBlock[4]에 담는다. 

      for (int i=0; i<tempBlock.length; i++){               //i는 0부터 3까지 4번반복
         int x = tempBlock[i].getX()-1;                  //각블럭의 x에 1을 감소해  x좌표로 설정
         int y = tempBlock[i].getY();                  //각블럭의 y를 y좌표로 설정
         if(x < 0 || this.grid[x][y]){                  //x가 0보다 작으면(왼쪽벽범위를 넘으면) / 혹은 이미 블럭이 있으면
            return;                                 //함수종료
         }
      }
      this.currentItem.moveLeft();                     //그 외의 경우는 왼쪽으로 이동
   }
   //벽돌없애기 체크 -> 없애기
   //키보드액션리스너
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
            while(goDown()){} //블럭을 만나지 않을때까지 반복해서 내리다가 블럭을 만나면 false을 리턴해 while문을 빠져나온다
            break;
      }
   }
   public void keyReleased(KeyEvent e){}
   public void keyTyped(KeyEvent e){}
   //게임종료체크   
   public void gameEnd(){
      JOptionPane.showMessageDialog(null, "게임이 종료되었습니다.", "게임종료", JOptionPane.ERROR_MESSAGE);
      t.stop();
   }


   //쓰레드메인
   public void run(){
      try
      {
         while(true){
            Thread.sleep(this.time);                                    //500milliseconds만큼 sleep
            if(this.currentItem.getCurrentXY().getY() < 3)   this.isKey = false;      //판넬위쪽이면 키리스너 동작X
            else   this.isKey = true;                                    //그외의 경우는 동작O
            goDown();                                                //아이템밑으로이동
         }
      }
      catch (Exception e){
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args) 
   {
      new Tetris("Tetris by 1조");
   }
}