

public class Main
{

    //find errors/bugs


    public static void main(String[] args) {
        Shoe newShoe = new Shoe(6);
        AutoBlackJack Count = new CountingBlackJack(newShoe, 0, 1,
                6, 3,"H17CtSt1");
        AutoBlackJack Regular = new AutoBlackJack(newShoe, 0, 1, "src/SC/HitOnSoft17.txt");


        System.out.println("Counting:");
        Count.play(1,1000000,3);
        System.out.println("Regular: ");
        Regular.play(1,1000000,3);
    }
}
