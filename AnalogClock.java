
package hw05;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.net.*;

public class HW05 {
    
    // initialize curr time
    static MyTime timer;
    static MyClock clk;
    static String curr;
  
  
    
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("time-a-g.nist.gov", 13);
            if (socket.isConnected()) {
                Scanner input = new Scanner(socket.getInputStream());
                input.next();
                input.next();
                curr = input.next();
                System.out.println(curr);
                socket.close();
                
            } 
        } catch (IOException e) {}
        
        JFrame frame = new JFrame("Analog Clock");
        frame.setSize(420,435);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clk = new MyClock();
        frame.add(clk);
        frame.setVisible(true);
        
        timer = new MyTime();
        timer.start();
    }
    
}

class MyTime extends Thread {
    int counter = 0;
    
    @Override
    public void run() {
        while(true) {
            try {
                sleep(1000);
                HW05.clk.updateClock();
                counter++;
                if (counter == 60) {
                    Socket socket = new Socket("time-a-g.nist.gov", 13);
                    if (socket.isConnected()) {
                        Scanner input = new Scanner(socket.getInputStream());
                        input.next();
                        input.next();
                        HW05.curr = input.next();
                        System.out.println(HW05.curr); //requested time every min
                        socket.close();
                        HW05.clk.setClock();
                    }
                    counter = 0;
                }
            } catch (Exception e) {}
            
        }
    }
}



class MyClock extends JPanel {
    
    int hr;
    int min;
    int sec;
    
    double hrDeg;
    double minDeg;
    double secDeg;
    
   
    public MyClock() {
        super();
        setClock();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(Color.BLACK);
        g2.fillOval(5, 5,400,400);
        g2.setColor(Color.WHITE);
        g2.fillOval(10, 10,390,390);
        g2.setColor(Color.BLACK);
        g2.fillOval(197,197,15,15);
         
        //hour hand 
        g2.setStroke(new BasicStroke(8));
        g2.drawLine(204, 204, 204 + (int)(100 * Math.cos(hrDeg)), 
                204 + (int)(100 * Math.sin(hrDeg))); 
        
        //minute hand 
        g2.setStroke(new BasicStroke(5));
        g2.setColor(Color.BLACK);
        g2.drawLine(204, 204, 204 + (int)(160 * Math.cos(minDeg)), 
                204 + (int)(160 * Math.sin(minDeg)));;
              
                
        //second hand 
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.RED);
        g2.drawLine(204, 204, 204 + (int)(180 * Math.cos(secDeg)), 
                204 + (int)(180 * Math.sin(secDeg)));;
        
    }
    
     public void setClock() {
        hr = Integer.parseInt(HW05.curr.substring(0, 2));
        min = Integer.parseInt(HW05.curr.substring(3, 5));
        sec = Integer.parseInt(HW05.curr.substring(6, 8));
        
        if (hr >= 12) {
            hr = hr - 12;
        }
        hrDeg = Math.toRadians((270 + 30 * hr + min / 2) % 360);
        minDeg = Math.toRadians((270 + 6 * min) % 360);
        secDeg = Math.toRadians((270 + 6 * sec) % 360);
    }
    
    public void updateClock() {
        sec++;
        if (sec == 60) {
            min++;
            sec = 0;
        }
        if (min == 60) {
            hr++;
            min = 0;
        }
        
        hrDeg = Math.toRadians((270 + 30 * hr + min / 2) % 360);
        minDeg = Math.toRadians((270 + 6 * min) % 360);
        secDeg = Math.toRadians((270 + 6 * sec) % 360);
     
        this.repaint();
    }
    
}


    
