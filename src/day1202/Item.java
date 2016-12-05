package day1202;

/*
   Class 설명
   - Block : 블록의 X좌표 Y좌표의 정보를 가지고 있는 블록 클래스.
   - Item : 블록을 가지고 테트리스 아이템(모양) 을 만든다. (부모클래스)
   - Rect, OneThree, ThreeOne ... : Item클래스를 상속받아 각블록위치정보를 셋팅한다.
*/

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

//블록 클래스
class Block
{
   private int x;
   private int y;
   //생성자
   public Block(){
   }
   public Block(int x, int y){
      this.x = x;
      this.y = y;
   }
   //해당 포인트맛큼 감산
   public void move(int xPlus, int yPlus){
      this.x += xPlus;
      this.y += yPlus;
   }
   //X포인트 반환
   public int getX(){
      return this.x;
   }
   //Y포인트 반환
   public int getY(){
      return this.y;
   }
   //자신 반환
   public Block getBlock(){
      return this;
   }
   //XY셋팅
   public void setXY(int x, int y){
      this.x = x;
      this.y = y;
   }
}

//아이템 클래스
public class Item 
{
   JPanel[] panel;      //판넬
   Block[] block;      //현재포인트(x,y값)
   Block[][] block_info;      //각 각도별 포인트정보
   //앞의배열 0~3 각도 0-0도 1-90도 2-180도 3-270도
   //뒤의 배열은 판넬 갯수
   Block currentXY;
   int cnt;            //총판넬개수
   int angle;            //총각도개수
   int current_angle;      //현재각도값
   int xCnt;            //가로값

   Color color;      //색
   int area;         //넓이
   
   //사각형블록을 예를들면 -> 생성자의 인자로 20, 1, 4, 14가 들어옴
   public Item(int area, int angle, int cnt, int xCnt){
      this.angle = angle;                     //각도의 개수
      this.cnt = cnt;                        //블럭한칸의 개수 -> 예를들이 일자 블럭은 ---- 이니까 4개의 블록으로 이루어짐, 즉 모든블럭은 4개로 이루어짐
      this.panel = new JPanel[cnt];            //판넬개수 셋팅
      this.block = new Block[cnt];            //포인트 셋팅
      this.block_info = new Block[angle][cnt];   //포인트 각도, 개수셋팅
      this.area = area;                     //블럭한칸의 pixel범위 (20)      
      this.currentXY = new Block(0,0);         //현재값(x=0,y=0)
      this.xCnt = xCnt;                     //Center패널 배열의 가로길이

      for (int i=0; i<cnt; i++){         //패널생성
         this.panel[i] = new JPanel();   //모든 블럭은 4개의 패널을 생성함
      }
   }
   public void setDefaultRandom(){
      this.current_angle = (int)(Math.random() * this.angle);      //cuttent_angle -> 각도를 랜덤 중 하나로 세팅
      this.block = this.block_info[this.current_angle];         //그 랜덤 각도의 block_info[랜덤각도][4]를 block[4]에 대입
   }
   //컨테이너에 등록
   public void setItem(Container c){
      for (int i=0; i<panel.length; i++){                     //i는 0부터 3까지
         panel[i].setBackground(this.color);                  //각판넬의 배경색설정
         panel[i].setSize(area, area);                     //각판넬의 넓이설정
         panel[i].setLocation(((block[i].getX()) * area)-100, ((block[i].getY()) * area)-100);   //기본위치 안보이는곳에 생성
         panel[i].setBorder(new SoftBevelBorder(BevelBorder.RAISED));                     //테두리 스타일 설정
         c.add(panel[i]);   //컨테이너에 등록
      }
   }
   //다음위치조정
   public void setNextLocation(){
      for (int i=0; i<panel.length; i++){               //i는 0부터 3까지
         int x = block[i].getX() + (xCnt-3);            //블럭에 담긴 x값에 14-3을 더해 x좌표로 설정
         int y = block[i].getY() + 1;               //블럭에 담긴 y값에 1을 더해 y좌표로 설정
         panel[i].setLocation(x * area, y * area);      //panel에 위에정한 x,y를 기준으로 넓이와 높이를 20으로 설정한다.
      }
      this.currentXY.setXY((xCnt-3),1);               //최근 블럭의 x,y값을 저장 (11,1가 저장된다.) -> 다음블럭을 나탈낼 위치
   }
   //시작위치조정
   public void setDefaultLocation(){
      for (int i=0; i<panel.length; i++){               //i는 0부터 3까지
         int x = block[i].getX() + (int)(xCnt/2-2);      //블럭에 담긴 x값에 14/2-2를 더해 x좌표로 설정
         int y = block[i].getY() +2;                  //블럭에 담긴 y값에 2를 더해 y좌표로 설정
         panel[i].setLocation(x * area, y * area);      //panel에 위에지정한 x,y를 기준으로 넓이와 높이를 20으로 설정한다.
      }
      this.currentXY.setXY((int)(xCnt/2-2),3);         //최근 블럭의 x,y값을 저장 (5,2가 저장된다.) -> 블럭이 떨어지기 전 최초위치
   }
   //대기상태 위치조정
   public void setReadyLocation(){
      for (int i=0; i<panel.length; i++){               //i는 0부터 3까지 4번 반복
         panel[i].setLocation(((block[i].getX()) * area)-100, ((block[i].getY()) * area)-100);   //기본위치 안보이는 곳에 설정
      }
   }
   //현재위치조정
   public void setCurrentXY(int x, int y){
      this.currentXY.move(x,y);
   }
   //현재위치반환
   public Block getCurrentXY(){
      return this.currentXY;
   }
   //현재 포인트 리턴
   public Block[] getBlock(){
      Block[] temp = new Block[cnt];                     //temp라는 Block[4]만큼의 배열선언
      for (int i=0; i<block.length; i++){                  //i는 0부터 3까지
         int x = block[i].getX() + this.currentXY.getX();  //currentXY에 저장된 x의 값과 block[i]의 x를 더해 x좌표로 설정 -> 사각형을 예로들면 4가지 블럭중 첫번째블럭  
         int y = block[i].getY() + this.currentXY.getY();  //currentXY에 저장된 y의 값과 block[i]의 y를 더해 y좌표로 설정   -> 0,0의 0에 currentXY의 x좌표를 더함
         temp[i] = new Block(x,y);      //temp[4]에 현재 위치한 블럭의 위치를 전부 담아서
      }         
      return temp;                  //temp[4]를 리턴
   }
   //다음움직일각도의 포인트정보 반환
   public Block[] getNextBlock(){
      int nextAngle;                                    //다음 각도 변수
      if(this.angle==1)   return getBlock();                  //각도가1개뿐이면 지금 블럭을 리턴
      else if(this.angle-1 == this.current_angle)   nextAngle=0;   //현재가 마지막앵글이면 첫번쨰 각도로
      else   nextAngle=this.current_angle+1;                  //다음각도 셋팅
      
      Block[] temp = new Block[cnt];                        //temp라는 이름으로 Block[4]의 배열 선언
      for (int i=0; i<block.length; i++){                     //i는 0부터 3까지 4번 반복
         int x = block_info[nextAngle][i].getX() + this.currentXY.getX();   //다음각도의 블럭 x와 현재의 x를 더해 x의 좌표를 구함 
         int y = block_info[nextAngle][i].getY() + this.currentXY.getY();   //다음각도의 블럭 y와 현재의 y를 더해 y의 좌표를 구함
         temp[i] = new Block(x,y);         //새로운 블럭을 생성해 temp에 저장후
      }
      return temp;                     //temp를 리턴
   }
   //현재앵글리턴
   public int getCurrentAngle(){
      return this.current_angle;
   }
   //로테이트
   public void moveRotate(){
      if(this.angle==1)   return;                        //각도가1개뿐이면 리턴
      if(this.current_angle+1 == this.angle){               //최고각도면 처음각도로 -> current_angle의 범위는 0~3까지임
         this.block = this.block_info[0];               //각도0에 해당하는 블럭으로 설정
         this.current_angle = 0;                        //각도를 0으로 설정
      }else{                                       //그외에는
         this.current_angle++;                        //각도를 하나 증가시키고
         this.block = this.block_info[this.current_angle];   //다음각도에 해달하는 블럭으로 걸정
      }
      this.setMove();                                 //그정보를 판넬에 적용
   }
   //현재의 포인트 정보를 판넬에 적용하여 움직여라 
   public void setMove(){
      for (int i=0; i<panel.length; i++){                        //i는 0부터 3까지
         //현재블록의 x,y값에 현재x,y포인트값을 더한값을 각area값과 곱한다.
         int x = this.block[i].getX() + this.currentXY.getX();
         int y = this.block[i].getY() + this.currentXY.getY();;
         panel[i].setLocation(x * area, y * area);               //판넬의 위치를 설정
      }
   }
   //아래로 한칸 움직임
   public void moveDown(){
      this.currentXY.move(0,1);      //y좌표를 하나 증가시켜 아래로 한칸 이동
      this.setMove();               //그정보를 판넬에 적용
   }
   //오른쪽으로 한칸 움직임
   public void moveRight(){
      this.currentXY.move(1,0);      //x좌표를 하나 증가시켜 오른쪽으로 한칸 이동
      this.setMove();               //그정보를 판넬에 적용
   }
   //왼쪽으로 한칸 움직임
   public void moveLeft(){
      this.currentXY.move(-1,0);      //x좌표를 하나 감소시켜 왼쪽으로 한칸 이동
      this.setMove();               //그정보를 판넬에 적용
   }
   //현재 색 리턴
   public Color getColor(){
      return this.color;
   }
   //현재 색 셋팅
   public void setColor(Color c){
      this.color = c;
      for (int i=0; i<panel.length; i++){
         panel[i].setBackground(this.color);
      }
   }
}



//아래는 각 도형의 객체들 -> 생성자가 호출되면 인자로 area(20) center(JPanel), xCnt(14)를 넘겨받는다. ----------------------------------------------
//그리고 그 인자중 area(20)와 xCnt(14)를 부모(Item Class)의 생성자 인자로 넘긴다. --------------------------------------------------------------

//사각형 
class Rect extends Item
{
   public Rect(int area, Container con, int xCnt){
      super(area, 1, 4, xCnt);   //영역길이, 각도갯수, 판넬개수

      block_info[0][0] = new Block(0,0);
      block_info[0][1] = new Block(0,1);
      block_info[0][2] = new Block(1,0);
      block_info[0][3] = new Block(1,1);

      this.setDefaultRandom();   //랜덤셋팅 -> 부모에게서 상속받은 함수
      this.setItem(con);         //컨테이너에 등록 -> 부모에게서 상속받은 함수
   }
}

//기억자
class OneThree extends Item
{
   public OneThree(int area, Container con, int xCnt){
      super(area, 4, 4, xCnt);   //영역길이, 각도갯수, 판넬개수

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

      this.setDefaultRandom();   //랜덤셋팅 -> 부모에게서 상속받은 함수
      this.setItem(con);         //컨테이너에 등록 -> 부모에게서 상속받은 함수
   }
}

//기억자 반대
class ThreeOne extends Item
{
   public ThreeOne(int area, Container con, int xCnt){
      super(area, 4, 4, xCnt);   //영역길이, 각도갯수, 판넬개수

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

      this.setDefaultRandom();   //랜덤셋팅 -> 부모에게서 상속받은 함수
      this.setItem(con);         //컨테이너에 등록 -> 부모에게서 상속받은 함수
   }
}

//일자
class LineBlock extends Item
{
   public LineBlock(int area, Container con, int xCnt){
      super(area, 2, 4, xCnt);   //영역길이, 각도갯수, 판넬개수

      block_info[0][0] = new Block(0,-1);
      block_info[0][1] = new Block(0,0);
      block_info[0][2] = new Block(0,1);
      block_info[0][3] = new Block(0,2);

      block_info[1][0] = new Block(-1,0);
      block_info[1][1] = new Block(0,0);
      block_info[1][2] = new Block(1,0);
      block_info[1][3] = new Block(2,0);

      this.setDefaultRandom();   //랜덤셋팅 -> 부모에게서 상속받은 함수
      this.setItem(con);         //컨테이너에 등록 -> 부모에게서 상속받은 함수
   }
}

//┷ 요모양~ ㅋㅋ
class Triangle extends Item
{
   public Triangle(int area, Container con, int xCnt){
      super(area, 4, 4, xCnt);   //영역길이, 각도갯수, 판넬개수

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

      this.setDefaultRandom();   //랜덤셋팅 -> 부모에게서 상속받은 함수
      this.setItem(con);         //컨테이너에 등록 -> 부모에게서 상속받은 함수
   }
}

//_|- 요모양? ㅋㅋ
class RightBlock extends Item
{
   public RightBlock(int area, Container con, int xCnt){
      super(area, 2, 4, xCnt);   //영역길이, 각도갯수, 판넬개수

      block_info[0][0] = new Block(0,0);
      block_info[0][1] = new Block(0,1);
      block_info[0][2] = new Block(1,1);
      block_info[0][3] = new Block(1,2);

      block_info[1][0] = new Block(1,0);
      block_info[1][1] = new Block(0,0);
      block_info[1][2] = new Block(0,1);
      block_info[1][3] = new Block(-1,1);

      this.setDefaultRandom();   //랜덤셋팅 -> 부모에게서 상속받은 함수
      this.setItem(con);         //컨테이너에 등록 -> 부모에게서 상속받은 함수
   }
}

//-|_ 요모양 ㅋㅋㅋ
class LeftBlock extends Item
{
   public LeftBlock(int area, Container con, int xCnt){
      super(area, 2, 4, xCnt);   //영역길이, 각도갯수, 판넬개수

      block_info[0][0] = new Block(0,0);
      block_info[0][1] = new Block(1,0);
      block_info[0][2] = new Block(1,1);
      block_info[0][3] = new Block(2,1);

      block_info[1][0] = new Block(0,1);
      block_info[1][1] = new Block(0,0);
      block_info[1][2] = new Block(1,0);
      block_info[1][3] = new Block(1,-1);

      this.setDefaultRandom();   //랜덤셋팅 -> 부모에게서 상속받은 함수
      this.setItem(con);         //컨테이너에 등록 -> 부모에게서 상속받은 함수
   }
}