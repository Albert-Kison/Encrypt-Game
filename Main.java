import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Random random = new Random();

        String[] paragraphs = {
                "The greatest glory in living lies \nnot in never falling, \nbut in rising every time we fall.",
                "When you reach the end of your rope, \ntie a knot in it and hang on.",
                "Don't judge each day by the harvest you reap \nbut by the seeds that you plant.",
                "The future belongs to those who believe in therty beauty \nof their dreams.",
                "Tell me and I forget. \nTeach me and I remember. \nInvolve me and I learn.",
                "It is during our darkest moments \nthat we must focus \nto see the light."
        };

        String chosenString;



        //Task 1

        displayWelcomeLines();

        Scanner in = new Scanner(System.in);
        int userChoice = getIntInRange(1, 2);


        // the test mode
        if (userChoice == 2) {
            System.out.println("You can choose between the following paragraphs...");

            displayPreview(paragraphs);

            userChoice = getIntInRange(1, paragraphs.length);

            chosenString = paragraphs[userChoice - 1];

        } else {
            chosenString = paragraphs[random.nextInt(paragraphs.length)];
        }

        //debug
        System.out.println(chosenString);
        System.out.println();


        //Task 2

        String encryptedText = encryptText(chosenString);




        //Task 3, 4, 5, 6


        //to store th encrypted version for a backup
        String cloneString = encryptedText;

        //to track the users moves and display them
        LinkedHashMap<String, String> userMoves = new LinkedHashMap<String, String>();

        //the allowed number of 'help' to use
        int helpCounter = 5;

        int SECONDS_TO_SOLVE = 1000;

        displayRules(SECONDS_TO_SOLVE);
        System.out.println();
        System.out.println(encryptedText);

        //starts the timer
        Instant start = Instant.now();


        while (true) {
            System.out.println();
            System.out.println("Enter your answer: ");
            String userAnswer = getAnswer();
            System.out.println();


            //handles the "help"
            if (userAnswer.equalsIgnoreCase("help")) {

                if (helpCounter > 0) {
                    helpCounter -= 1;
                    encryptedText = helpUser(helpCounter, encryptedText, userMoves, chosenString);
                } else {
                    System.out.println("You are out of help");
                }

            }

            //handles "reset"
            else if (userAnswer.equalsIgnoreCase("reset")) {

                encryptedText = cloneString;
                userMoves.clear();

            }

            else {

                //erases the pair
                if (userAnswer.length() == 1 && userMoves.containsValue(String.valueOf(Character.toUpperCase(userAnswer.charAt(0))))) {

                    String replace = String.valueOf(Character.toUpperCase(userAnswer.charAt(0)));
                    encryptedText = erasePair(userAnswer, userMoves, encryptedText, cloneString, replace);

                } else if (userAnswer.length() > 1) {

                    String replace = String.valueOf(userAnswer.charAt(0));
                    String replaceWith = String.valueOf(Character.toUpperCase(userAnswer.charAt(1)));


                    if (userMoves.containsValue(replaceWith)) {

                        encryptedText = erasePair(userAnswer, userMoves, encryptedText, cloneString, replaceWith);

                    }

                    userMoves.remove(replace);
                    userMoves.put(replace, replaceWith);


                    //replace
                    for (int i = 0; i < chosenString.length(); i++) {
                        if (String.valueOf(cloneString.charAt(i)).equals(replace)) {
                            char[] stringArr = encryptedText.toCharArray();
                            stringArr[i] = replaceWith.toCharArray()[0];
                            encryptedText = String.valueOf(stringArr);
                        }
                    }

                } else {
                    System.out.println("Incorrect input. Please try again...");
                }
            }
            System.out.println(encryptedText);

            //displays the users steps
            for (String i: userMoves.keySet()) {
                System.out.println(i + " > " + userMoves.get(i));
            }

            //calculates the score
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).getSeconds();
            long score = SECONDS_TO_SOLVE - timeElapsed;
            if (score < 0) {
                score = 0;
            }

            if (encryptedText.equalsIgnoreCase(chosenString)) {
                System.out.println("Your score is " + score);
                break;
            }
        }

    }



    public static String encryptText(String chosenString) {
        Random random = new Random();

        //for each letter there is an encoded one
        HashMap<Character, Character> encryptSet = new HashMap<Character, Character>();

        //get the list of letters
        ArrayList<Character> letters = new ArrayList<Character>();
        for (int i = 0; i < 26; i++) {
            letters.add(Character.toChars(97 + i)[0]);
        }

        for (int i = 0; i < chosenString.length(); i++) {

            //check if the letter is already encoded
            //check if the character is not the space
            //check if the character is a letter
            if (!encryptSet.containsKey(Character.toLowerCase(chosenString.charAt(i))) &&
                    !String.valueOf(chosenString.charAt(i)).equals(" ") &&
                    Character.isAlphabetic((int)chosenString.charAt(i))) {

                //random letter of a lower case
                Character randomLetter = letters.get(random.nextInt(letters.size()));
                while (Character.toLowerCase(randomLetter) == Character.toLowerCase(chosenString.charAt(i))) {
                    randomLetter = letters.get(random.nextInt(letters.size()));
                }
                letters.remove(randomLetter);

                encryptSet.put(Character.toLowerCase(chosenString.charAt(i)), randomLetter);
            }
        }

        //writes the encrypted letters to the string
        char[] chosenStringArr = chosenString.toCharArray();
        for (int i = 0; i < chosenStringArr.length; i++) {
            if (Character.isAlphabetic(chosenStringArr[i])) {
                chosenStringArr[i] = encryptSet.get(Character.toLowerCase(chosenStringArr[i]));
            }
        }
        chosenString = String.valueOf(chosenStringArr);

        return chosenString;
    }



    public static void displayWelcomeLines() {
        System.out.println("Welcome the Cipher Game!");
        System.out.println("You have 2 options:");
        System.out.println("1) Normal mode");
        System.out.println("2) Test mode");
        System.out.println("Choose 1 or 2");
    }



    public static int getIntInRange(int start, int end) {
        Scanner in = new Scanner(System.in);

        int userChoice = -1;
        boolean shouldContinue = true;
        do {
            try {
                userChoice = in.nextInt();

                if (userChoice >= start && userChoice <= end) {
                    shouldContinue = false;
                } else {
                    System.out.println("Incorrect input. Please try again...");
                }

            } catch (InputMismatchException e) {
                System.out.println("Incorrect input!");
                in.nextLine();
            }
        } while (shouldContinue);

        return userChoice;
    }



    public static String getAnswer() {
        Scanner in = new Scanner(System.in);

        String userAnswer;

        boolean shouldContinue = false;
        do {
            shouldContinue = false;

            userAnswer = in.next();

            for (int i = 0; i < userAnswer.length(); i++) {
                if (!Character.isAlphabetic(userAnswer.charAt(i))) {
                    shouldContinue = true;
                    break;
                }
            }

            if (shouldContinue) {
                System.out.println("Incorrect input! Please try again...");
            }
        } while (shouldContinue);

        return userAnswer;
    }



    public static void displayPreview(String[] paragraphs) {
        for (int i = 0; i < paragraphs.length; i++){

            String firstLine;
            if (paragraphs[i].contains("\n")) {
                firstLine = paragraphs[i].substring(0, paragraphs[i].indexOf("\n"));
            } else {
                firstLine = paragraphs[i];
            }

            if (firstLine.length() > 50) {
                //displays the line within 50 characters
                firstLine = firstLine.substring(0, 49);
                firstLine = firstLine.substring(0, firstLine.lastIndexOf(" "));
            }
            System.out.println((i + 1) + ") " + firstLine + "...");
        }
    }



    public static void displayRules(int seconds) {
        System.out.println("To decrypt a letter, just type the letter you want to decrypt and the letter you want to replace it with.");
        System.out.println("To erase the pair, type the letter you replaced it with");
        System.out.println("You have " + seconds + " seconds to solve this");
    }



    public static ArrayList<String> getOptions(String encryptedText, LinkedHashMap<String, String> userMoves) {
        ArrayList<String> options = new ArrayList<String>();

        for (int i = 0; i < encryptedText.length(); i++) {

            //checks if the letter does not exist in the users moves
            //check if it is a letter
            //check if it is not stored yet
            if (!userMoves.containsValue(String.valueOf(encryptedText.charAt(i))) &&
                    Character.isAlphabetic(encryptedText.charAt(i)) &&
                    !options.contains(String.valueOf(encryptedText.charAt(i)))) {

                options.add(String.valueOf(encryptedText.charAt(i)));
            }
        }

        return options;
    }



    public static String helpUser(int helpCounter, String encryptedText, LinkedHashMap<String, String> userMoves, String chosenString) {

        //stores the available options to choose from
        ArrayList<String> options = getOptions(encryptedText, userMoves);


        //gets the random letter from the available options
        Random random = new Random();
        String replace = options.get(random.nextInt(options.size()));

        //replaces the letter
        char replaceWith = ' ';
        for (int i = 0; i < encryptedText.length(); i++) {

            if (String.valueOf(encryptedText.charAt(i)).equals(replace)) {

                char[] stringArr = encryptedText.toCharArray();
                replaceWith = Character.toUpperCase(chosenString.charAt(i));
                stringArr[i] = replaceWith;
                encryptedText = String.valueOf(stringArr);

            }
        }

        userMoves.put(replace, String.valueOf(replaceWith));

        return encryptedText;
    }



    public static String erasePair(String userAnswer, LinkedHashMap<String, String> userMoves, String encryptedText, String cloneString, String replace) {

        //deletes the previous letter
        for (String i : userMoves.keySet()) {
            if (userMoves.get(i).equals(replace)) {
                userMoves.remove(i);
                break;
            }
        }

        //replaces the letter
        for (int i = 0; i < encryptedText.length(); i++) {

            if (String.valueOf(encryptedText.charAt(i)).equals(replace)) {

                char[] stringArr = encryptedText.toCharArray();
                stringArr[i] = cloneString.charAt(i);
                encryptedText = String.valueOf(stringArr);
            }
        }

        return encryptedText;
    }
}
