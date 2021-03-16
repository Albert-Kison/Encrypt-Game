import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Instant start = Instant.now();
        while (true) {
            System.out.println("Enter: ");
            String userAnswer = in.next();
            if (userAnswer.equals("a")) {
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).getSeconds();
                System.out.println(timeElapsed);
                break;
            }
        }


    }
}
