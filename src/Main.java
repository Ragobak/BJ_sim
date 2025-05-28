

public class Main
{

    //find errors/bugs


    public static void main(String[] args) {
        Shoe newShoe = new Shoe(6);
//        BlackJack vamps = new BlackJack(newShoe, 0, 1);
//        vamps.play(1);
//        AutoBlackJack Test = new AutoBlackJack(newShoe, 0, 100, "src/SC/Test.txt");
        AutoBlackJack H17 = new AutoBlackJack(newShoe, 0, 1,"src/SC/HitOnSoft17.txt");
        AutoBlackJack S17 = new AutoBlackJack(newShoe, 0, 1, "src/SC/StayOnSoft17.txt");

        System.out.println("H17 game w S17 strat");
//        S17.play(1,500000, 3);
//        System.out.println("H17 game w H17 strat");
        H17.play(1,5, 3);
//        H17.HIT_ON_SOFT_17 = false;
//        S17.HIT_ON_SOFT_17 = false;
//        System.out.println("S17 game w H17 strat");
//        H17.play(1,500000, 3);
//        System.out.println("S17 game w S17 strat");
//        S17.play(1,500000, 3);
    }
}
