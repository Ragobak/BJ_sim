

public class Main
{

    //TODO: make way to store findings
 //st1 and st2 unit: true count - 1
 //st1 stay on 16 vs 10 & 11 on all +tc, st2 stay on 16 vs only 10 on all +tc, both insurance at tc 4+
//conclusion: st1 slightly better

    public static void main(String[] args) {
        SuitedDeck suitedDeck = new SuitedDeck();
        UltimateTexasHoldem ultimateTexasHoldem = new UltimateTexasHoldem(suitedDeck, 1000, 15);
        ultimateTexasHoldem.play(3, 5);
//        Shoe newShoe = new Shoe(6);
//        AutoBlackJack Count1 = new CountingBlackJack(newShoe, 0, 1,
//                6, 4,"H17CtSt1");
//        AutoBlackJack Count2 = new CountingBlackJack(newShoe, 0, 1,
//                6, 4,"H17CtSt2");
//        AutoBlackJack Regular = new AutoBlackJack(newShoe, 0, 1, "src/SC/HitOnSoft17.txt");
//
//
//        System.out.println("Strat 1:");
//        Count1.play(1,10000000, 3);
//        System.out.println("Strat 2: ");
//        Count2.play(1,10000000, 3);
    }
}
