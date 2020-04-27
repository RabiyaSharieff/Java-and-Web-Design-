/*
Rabiya Sharieff 
Midterm Assignment
 */
package midterm_sharieff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Survey {
    static JLabel question;
    static JButton leftAns;
    static JButton rightAns;
    static int num_questions;
    static Question[] questionArr;
    static String[] responses;
    static QuestionThread qThread; 
    static JPanel panel;
    //static JFrame frame;
    
 
    public static void main(String[] args) {
      
         questionArr = new Question[5]; // initialize question array to 5
         responses= new String[5]; // what ever is pressed, this will store what is pressed 
         //initialize response array with null/no response 
         for (int i =0; i <5; ++i){
             responses[i]="NO RESPONSE";
         }
         
         
         //create questions
         Question q1 = new Question(" Favorite Ice Cream", "Vanilla", "Chocolate" );
         Question q2 = new Question(" Which season is better", "Winter", "Summer");
         Question q3 = new Question(" Which pet is better", "Cat", "Dog");
         Question q4 = new Question(" Unicorns are real");
         Question q5 = new Question(" Text or call", "Text", "Call");
         questionArr[0] = q1;
         questionArr[1] = q2; 
         questionArr[2] = q3; 
         questionArr[3] = q4; 
         questionArr[4] = q5; 
         num_questions = 0; //counter. Questions start at first element in questionArr i.e 0 
        
         final JFrame frame = new JFrame("Midterm Question"); 
         
         question = new JLabel(questionArr[0].questionText);
         
         leftAns = new JButton(questionArr[0].leftText);
         leftAns.addActionListener(new QuestionAnswer()); 
         
         rightAns = new JButton(questionArr[0].rightText);
         rightAns.addActionListener(new QuestionAnswer());
         
        
         frame.setSize(400,400);
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         JPanel panel = new JPanel();//1
         panel.setLayout(new GridLayout(2,1));//2
         panel.add(question);
   
         JPanel answerPanel = new JPanel(new GridLayout(2,3,130,1));
         answerPanel.add(new JLabel());
         answerPanel.add(new JLabel());
         answerPanel.add(leftAns);
         answerPanel.add(rightAns);
         panel.add(answerPanel, BorderLayout.SOUTH);
         
         frame.add(panel);
         frame.setVisible(true);
       
         (qThread = new QuestionThread()).start();
        
    }
}

class Question {
    String questionText;
    String leftText;
    String rightText;
   
    
    //constructor 
    Question(final String qText, final String lText, final String rText ){
        this.questionText = qText;
        this.leftText = lText;
        this.rightText = rText;
    }
    
    // true/false constructor
    Question(String qText){
        this.questionText = qText;
        this.leftText = "True";
        this.rightText = "False";
    }
    
}


class QuestionAnswer implements ActionListener {
    
    @Override 
    public void actionPerformed( ActionEvent e){
        final JButton ans = (JButton)e.getSource();
        if (ans == Survey.leftAns){
            Survey.responses[Survey.num_questions] = Survey.questionArr[Survey.num_questions].leftText;
        }
        else{
            Survey.responses[Survey.num_questions] = Survey.questionArr[Survey.num_questions].rightText; 
        }
        
        //disable buttons when survey is at last question
        if (Survey.num_questions==4){
            String answers = " ";
            for (int i =0; i <5; ++i){
                answers = answers + Survey.responses[i] + ",";    
            }
            Survey.leftAns.setEnabled(false);
            Survey.rightAns.setEnabled(false);
            // set final answers in JLabel 
            Survey.question.setText(answers);  
        }
        else{ // if not at last question, go to the next question
            Survey.num_questions += 1; // increment to next question
            
            Survey.question.setText(Survey.questionArr[Survey.num_questions].questionText);
            Survey.leftAns.setText(Survey.questionArr[Survey.num_questions].leftText);
            Survey.rightAns.setText(Survey.questionArr[Survey.num_questions].rightText);
   
            (Survey.qThread = new QuestionThread()).start(); //resets the Survey thread for the next question
            
        } 
    }
}


class QuestionThread extends Thread{
    @Override
    public void run(){
        int q = Survey.num_questions; // store question number before sleep
        try{
            sleep(5000);
        }catch (InterruptedException e){} //specific exception
        
        // check to see if question number before sleep is equal to question number after sleep
        // if true then button was not pressed and 5 sec limit exceeded 
        if (Survey.num_questions == q) {  
            String answers= " ";
            for (int i =0; i <5; ++i){
                answers = answers + Survey.responses[i] + ","; 
            }
            Survey.leftAns.setEnabled(false);
            Survey.rightAns.setEnabled(false);
            // set final answers in JLabel 
            Survey.question.setText(answers);
        }
    }
}