public class SuitedCard extends Card{
    private final String suit;

    public SuitedCard(){
        super();
        suit = null;
    }

    public SuitedCard(int value, String suit){
        super(value, 1);
        this.suit = suit;
    }

    public String getSuit(){
        return suit;
    }

    public String toString(){
        return "This card has value of "+value+" and suit: "+ suit;
    }

}
