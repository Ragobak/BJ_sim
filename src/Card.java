

public class Card implements Comparable<Card> {

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
        return "" + value;
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.value, o.value);
    }

}