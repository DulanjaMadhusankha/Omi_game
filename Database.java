
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dulanja
 */
public class Database {
    private static ArrayList<String> cardList;
    private static List<String> cardTypes;
    
    static void loadDatabase() {
        cardList = new ArrayList<String> ();
        cardTypes = new ArrayList<String>();
        
        cardTypes.add("club");
        cardTypes.add("diamond");
        cardTypes.add("heart");
        cardTypes.add("spade");
        //add all cards to the card list
        int i, j;
        for(i = 0; i < 4; i++) {
            for(j = 2; j < 15; j++) {
                String cardType = cardTypes.get(i);
                cardList.add(cardType + "/" + j + ".png");
            }
        }
        
        // shuffle the cards array
        shuffl();
    }

    private static void shuffl() {
        
        for (int i = 0; i < cardList.size(); i++) {
            int swapIndex = (int) (Math.random() * 52);
            String temp = cardList.get(swapIndex);
            cardList.set(swapIndex, cardList.get(i));
            cardList.set(i, temp);
        }
    }

    public static ArrayList<String> getList() {
        return cardList;
    }
}
