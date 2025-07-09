public class SuitedCard extends Card{
    private final String suit;

    public SuitedCard(int value, String suit){
        super(value);
        this.suit = suit;
    }

    public String getSuit(){
        return suit;
    }

    public String toString(){
        return "This card has value of "+value+" and suit: "+ suit;
    }

}
