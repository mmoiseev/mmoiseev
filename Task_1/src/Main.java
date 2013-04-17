import java.util.Random;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Matt
 * Date: 13.04.13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int numb = random.nextInt(10);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите число от 0 до 10 " + " ответ " + numb);

        for(int i = 0; i<5;){
            int val;
            try{
                val = scanner.nextInt();
            } catch (Exception e){
                scanner.nextLine();
                System.out.println("Введите число от 1 до 10!");
                //e.printStackTrace();
                continue;
            }
            if(val == numb){
                System.out.println("Ура");
                break;
            } else if (val > numb) {
                System.out.println("Загаданное число меньше");
            } else {
                System.out.println("Загаданное число больше");
            }
            System.out.println("Осталось попыток " + (5-++i));
        }

        System.out.println("Правильный ответ " + numb);

    }
}

