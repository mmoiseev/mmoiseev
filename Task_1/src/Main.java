import java.util.Random;
import java.util.Scanner;

/**
 todo выкосить все комментарии, не относящиеся к делу
 todo мои замечания тут по мелочи, но они нужны
 todo как сделаете, поставлю галочку

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
                //e.printStackTrace(); todo и этот тоже выкосить.
                // todo закомментированный код,
                // todo который не исполняется вводит читающего в заблуждение, не
                // todo понятно, зачем он нужен.
                continue;
            }
            if(val == numb){  // todo нужно отформатировать код (CTRL_ALT_L)
                System.out.println("Ура");
                break;
            } else if (val > numb) {
                System.out.println("Загаданное число меньше");
            } else {
                System.out.println("Загаданное число больше");
            }
            System.out.println("Осталось попыток " + (5-++i));
            // todo преинкримент это здорово, но мне кажется, что это затрудняет чтение
            // todo чем проще программа, тем лучше. я бы вытащил наружу, но не настаиваю
        }

        System.out.println("Правильный ответ " + numb);

    }
}

