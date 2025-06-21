

public class Main
{

    //find errors/bugs


    public static void main(String[] args) {
        Shoe newShoe = new Shoe(6);
        AutoBlackJack Count1 = new CountingBlackJack(newShoe, 0, 1,
                6, 3,"H17CtSt1");
        AutoBlackJack Count2 = new CountingBlackJack(newShoe, 0, 1,
                6, 4,"H17CtSt2");
        AutoBlackJack Regular = new AutoBlackJack(newShoe, 0, 1, "src/SC/HitOnSoft17.txt");


        System.out.println("Counting insurance true 3:");
        Count1.play(1,15000000,3);
        System.out.println("Counting insurance true 4: ");
        Count2.play(1,15000000,3);
    }
}
