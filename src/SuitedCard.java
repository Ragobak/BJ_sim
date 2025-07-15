public class SuitedCard extends Card{
    private final char suit;

    public SuitedCard(int value, char suit){
        super(value);
        this.suit = suit;
    }

    @Override
    public char getSuit(){
        return suit;
    }

    public String toString(){
        if(value == 11) return "J" + suit;
        else if(value == 12) return "Q" + suit;
        else if(value == 13) return "K" + suit;
        else if(value == 14) return "A" + suit;
        return "" + value + suit;
    }

}
