# Java-and-Web-Design-
**Java Appliations:**

**Chat Room**
1) The chat server will accept connections from clients on port 5190 and relay anything sent to the server to all clients (including the one who originally sent the message), with the sender's name prepended to the message.The server does not produce any output on the screen. Clients, upon connecting, have to announce their username, which is NOT be forwarded to the other clients. The server does not introduce itself, to the client!

2) Chat client for the above server. The client will have a top box which is displays all the messages from the clients, and a bottom input which will allow text to be written to the server.Upon starting the client,  asks which server to connect to and what username to use. When the client connects to the server it sends the username as the first message. After the first message is sent, anything received from the server is displayed in the top box and anything written in the bottom box (after the send button is pushed) is sent to the server.

**Auto Changing Buttons**
A window with 8 buttons (this may change, in the future, to 100 buttons so make sure to be able to do that easily) organized in a 4x2 grid.  Have the background color of each button start at some random value (See java class Random).  When a button is pressed, it should cause every OTHER button to change background color.  The button pressed should not change background color at all. GUI program so that the buttons change color, automatically, about every second unless they've been pressed

**Survey**
A software to take a survey, there are 5 questions in our survey and each has justtwo options. We want our users to be presented with the options and choose quickly, as too much thinking spoils the results. Therefore, we are imposing a 5 second rule on all questions. That is, if you exceed 5 seconds for any one question, the survey ends and only the questions you answered arerecorded. At the end of the survey, print the user’s choices in the question space.

**Analog Clock**
A program which displays an analog clock with hours, minutes and seconds. As you know, the sleep function is imprecise, periodically, the time is updated from the NIST by connecting a socket to time-a-g.nist.gov on port 13. 


