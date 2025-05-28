

public class Card{

    private int value;
    private int amtLeft;

    public Card(){
        this.value = 0;
        this.amtLeft = 0;
    }

    public Card(int value, int amt){
        this.value = value;
        this.amtLeft = amt;
    }

    public void setValue(int value){
        this.value = value;
    }

    public void setAmtLeft(int amtLeft){
        this.amtLeft = amtLeft;
    }

    public int getValue(){
        return value;
    }

    public int getAmtLeft(){
        return amtLeft;
    }

    public String toString(){
        return "This card has value of "+value+" and there are this many: "+amtLeft;
    }

}