

public class Main
{



    public static void main(String[] args) {
        Shoe newShoe = new Shoe(6);
        AutoBlackJack Count1 = new CountingBlackJack(newShoe, 0, 1,
                6, 4,"H17CtSt1");
        AutoBlackJack Count2 = new CountingBlackJack(newShoe, 0, 1,
                6, 4,"H17CtSt2");
        AutoBlackJack Regular = new AutoBlackJack(newShoe, 0, 1, "src/SC/HitOnSoft17.txt");


        System.out.println("Strat 1:");
        Count1.play(1,1000000,3);
        System.out.println("Strat 2: ");
        Count2.play(1,1000000,3);
    }
}
