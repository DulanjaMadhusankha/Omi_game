

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author dulanja
 */

public class Join extends HttpServlet {
 //   private final int PLAYERS = 4;
    
    private int nofPlayers; //Initally no players are connected. Wchich is defined here.
    private int winner; //position of the winner
    private int[] score; //scores of the current round
    private int Playturn; //The player who is need to play
    private String[] cardsStr; //current hand
    private String trumpSuit; //the trup suit of the current round
    private int trickLeader; //position of the player, who leads the trick
    @Override
    /*Below function is used to initialize the omi game.*/
    public void init() throws ServletException {
        nofPlayers = 0;
        Playturn = 1;
        cardsStr = new String[4];
        score = new int[4];
        trickLeader = 1;
        
        Database.loadDatabase();
    }

    @Override





    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        /*If the sesion is new then begin new session*/
        if(session.isNew()) {
            session.setAttribute("position", ++nofPlayers);
            session.setAttribute("status", Status.Loading);
        }

       /*When 4 players are connected then deal the cards amoung the 4 players*/
        if(nofPlayers == 4) {
            if(session.getAttribute("status").equals(Status.Loading)) {        
                //distribute cards to the player
                deal(session);
            }

            if((int)(Integer)session.getAttribute("position") == Playturn) {
                session.setAttribute("status", Status.Play);
            } else {
                session.setAttribute("status", Status.Waiting);
            }
            
           /*The playing state is begun.*/
            if(session.getAttribute("status").equals(Status.Play)) {
              
             /*Get the cards to a string array*/
                List<String> cards = (List<String>) session.getAttribute("cards");
                JSONArray cardArr = new JSONArray();
                for (String card : cards) {   
                    JSONObject tempCard = new JSONObject();
                    tempCard.put("image", "cards/" + card);
                    cardArr.put(tempCard);
                }
                JSONObject json = new JSONObject();
              /*In the below part sends the object to the front end*/
                response.setContentType("text/event-stream");
                response.setCharacterEncoding("UTF-8");   
                PrintWriter out = response.getWriter();
                json.put("cards", cardArr);
              
                for(int i = 1; i < (int) (Integer) session.getAttribute("position"); i++) {
                    json.put("card" + i, "cards/" + cardsStr[i-1]);
                }
                 json.put("showHand", true);
                 json.put("showCards", true);
                 json.put("message", "Play your card");
                 out.write("data: " + json + "\n\n");
                 out.close();
              } 
                else if(session.getAttribute("status").equals(Status.Waiting)) {
                //get card list from session object, convert to json format and send to the player
                   List<String> cards = (List<String>) session.getAttribute("cards");
                   JSONArray cardArray = new JSONArray();
                  for (String card : cards) {   
                    JSONObject tempCard = new JSONObject();
                    tempCard.put("image", "cards/" + card);
                    cardArray.put(tempCard);
                  }
                JSONObject json = new JSONObject();
                response.setContentType("text/event-stream");
                response.setCharacterEncoding("UTF-8");   
                PrintWriter out = response.getWriter();
                json.put("cards", cardArray);
                //In here set the cards played by other players.
                int j = 1;
                for(int i = 1; i <= 4; i++) {
                    if(cardsStr[i-1] != null && i != (int) (Integer) session.getAttribute("position")) {
                        json.put("card" + j++, "cards/" + cardsStr[i-1]);
                    }
                }
            
                if(cardsStr[(int) (Integer) session.getAttribute("position") - 1] != null) {
                    json.put("mycard", "cards/" + cardsStr[(int) (Integer) session.getAttribute("position") - 1]);
                }
                json.put("showHand", true);
                json.put("showCards", true);
                json.put("message", "Wait for others to play");
                out.write("data: " + json + "\n\n");
                out.close(); 
            } else {
                
            }
        } else {
            JSONObject json = new JSONObject();
            response.setContentType("text/event-stream");
            response.setCharacterEncoding("UTF-8");   
            PrintWriter out = response.getWriter();
            json.put("cards", new JSONArray());
            json.put("showHand", false);
            json.put("showCards", false);
            if(nofPlayers == 1) {
                json.put("message", "Waiting for others to connect. Only 1 player connected ..");
            } else {
                json.put("message", "Waiting for others to connect. Only " + nofPlayers +  " players connected ..");
            }
            out.write("data: " + json + "\n\n");
            out.close();
        } 
    }

    private void deal(HttpSession session) {      
        List<String> cards = new ArrayList();
        ArrayList<String> cardList = Database.getList();
        int start = 13*((int) (Integer) session.getAttribute("position")-1);
        int end = start + 12;
        
        for(int i = start; i <= end; i++) {
            cards.add(cardList.get(i));
        }
        
        //Set the trump suit here.
        if((int) (Integer) session.getAttribute("position") == 4) {
            trumpSuit = (cards.get(12).split("/"))[0];
          
        }
             
        session.setAttribute("cards", cards);
    }
    
    public void handTo(){//to find who wins the round 
        
        
    }
    
    public void scoreAdd(){//adding score to the players
    
    }
    public void play(){//playin he game
    
    }
}
