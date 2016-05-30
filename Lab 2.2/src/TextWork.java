import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by ilia1 on 21.02.2016.
 */
public class TextWork {
    public void main() throws FileNotFoundException {
        try {
            Messages messages = new Messages();
            String exit = "n";

            Scanner sc = new Scanner(System.in);
            while (exit != "y") {
                System.out.println("***INTERFACE***" + "\nMake your choice:" + "\n1)Load messages from a file."
                        + "\n2)Save messages to a file." + "\n3)Add new message."
                        + "\n4)Show all messages." + "\n5)Delete message by ID."
                        + "\n6)Search messages by author." + "\n7)Search messages by word (words)."
                        + "\n8)Regex search." + "\n9)Data search." + "\n10)Exit.");

                int choice = sc.nextInt();
                while (choice < 1 || choice > 10) {
                    System.err.println("Incorrect choice, try again : ");
                    choice = sc.nextInt();
                }
                switch (choice) {
                    case 1: {
                        System.out.println("Enter file name:");
                        String fileName = sc.next();
                        File file = new File(fileName);
                        messages.readJSON(file);
                        break;
                    }
                    case 2: {
                        System.out.println("Enter file name:");
                        String fileName = sc.next();
                        File fileWrite = new File(fileName);
                        messages.writeJSON(fileWrite);
                        break;
                    }
                    case 3: {
                        messages.newMessage();
                        break;
                    }
                    case 4: {
                        messages.showMessages();
                        break;
                    }
                    case 5: {
                        messages.mesDel();
                        break;
                    }
                    case 6: {
                        messages.findAuth();
                        break;
                    }
                    case 7: {
                        messages.findWord();
                        break;
                    }
                    case 8: {
                        messages.regexSearch();
                        break;
                    }
                    case 9: {
                        messages.dataSearch();
                        break;
                    }
                    case 10: {
                        exit = "y";
                    }
                }
            }
        } catch (InputMismatchException e) {
            e.printStackTrace();
            System.err.println("Incorrect input!");
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.err.println("File not ");
        }
    }
}