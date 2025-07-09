

public class Card{

    protected final int value;

    public Card(){
        this.value = 0;
    }

    public Card(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public char getSuit() {
        return ' ';
    }

    public String toString(){
        return "This card has value of "+value;
    }

}