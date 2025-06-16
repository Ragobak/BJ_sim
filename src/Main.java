

public class Main
{

    //find errors/bugs


    public static void main(String[] args) {
        Shoe newShoe = new Shoe(6);
        AutoBlackJack H17 = new CountingBlackJack(newShoe, 0, 1, "H17CtSt1");
        AutoBlackJack S17 = new AutoBlackJack(newShoe, 0, 1, "src/SC/StayOnSoft17.txt");



        System.out.println("H17 game w H17 strategy");
        H17.play(1,10,3);
        BlackJack.HIT_ON_SOFT_17 = false;
    }
}
