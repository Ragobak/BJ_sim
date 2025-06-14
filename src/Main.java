

public class Main
{

    //find errors/bugs


    public static void main(String[] args) {
        Shoe newShoe = new Shoe(6);
        BlackJack n = new BlackJack(newShoe,0,1);
        AutoBlackJack H17 = new AutoBlackJack(newShoe, 0, 1,"src/SC/HitOnSoft17.txt");
        AutoBlackJack S17 = new AutoBlackJack(newShoe, 0, 1, "src/SC/StayOnSoft17.txt");


        System.out.println("H17 game w H17 strategy");
        H17.play(1,15000000,3);
        BlackJack.HIT_ON_SOFT_17 = false;
        System.out.println("S17 game w S17 strategy");
        S17.play(1,15000000,3);
    }
}
