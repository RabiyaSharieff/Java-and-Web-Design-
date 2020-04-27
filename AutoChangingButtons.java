
package hw03;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random; 

/*
Rabiya Sharieff 
AutoChangingButtons - expanded on HW02
 */

public class AutoChangingButtons  {
    static Button btn;
    static int num_buttons = 8;
    
    public static void main(String[] args) {
        //PressButton p = new PressButton();
 
        JFrame frame = new JFrame("HW02");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set the size of the frame
        frame.setSize(350, 350);
        // set the rows and cols of the grid, as well the distances between them
        GridLayout grid = new GridLayout(4, 2, 20, 20);
        // what layout we want to use for our frame
        frame.setLayout(grid);
         
        Button[] arrayBtn = new Button[num_buttons];
        // add JButtons dynamically
        for(int i=0; i < arrayBtn.length; i++) {
            arrayBtn[i] = new Button();
            frame.add(arrayBtn[i]);
        }
        frame.setVisible(true);
        
        // assign random colors to buttons
        for ( Button btn: arrayBtn ){
            Random r = new Random();
            int red, blue, green;
            red = r.nextInt(255);
            blue = r.nextInt(255);
            green = r.nextInt(255);
            btn.setBackground( new Color(red, blue, green));
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.addActionListener(new PressButton(arrayBtn));
            btn.bThread.start();
        }
    }
    
 }

class PressButton implements ActionListener {
   Button[] arrayBtn;
   
   PressButton(Button[] buttons) {
        arrayBtn = buttons;
    }
        
    @Override 
    public void actionPerformed( ActionEvent e){
          Button location = (Button)e.getSource();
          for ( Button btn: arrayBtn){
              if(btn == location){                                                  
                if(btn.pressed){
                    btn.pressed = false;
                    return;
                }
                btn.pressed = true;
                return;
            }
        }
    }
}

class Button extends JButton{
    ButtonThread bThread;
    boolean pressed;
    
    Button(){
        pressed = false;
        bThread= new ButtonThread(this); // each button has its own thread 
    }
}
 
class ButtonThread extends Thread{
    Button button;
    
    ButtonThread(Button btn){
        button = btn;
    }
    @Override
    public void run(){                                                         
        while(true){
            try{
               
                sleep(1000);
                if(!button.pressed){                                           
                    Random r = new Random();
                    int red, blue, green;
                    red = r.nextInt(255);
                    blue = r.nextInt(255);
                    green = r.nextInt(255);
                    button.setBackground( new Color(red, blue, green));
                }
                if(button.pressed){                                                
                    sleep(1000);
                }
            }catch(InterruptedException e){}
        }
    }
}
 



//int q = Survey.num_questions; // store question number before sleep/try


 // check to see if question number before sleep is equal to question number after sleep
        // if true then button was ont pressed and 5 sec limit exceeded 
        //if (Survey.num_questions == q) {  