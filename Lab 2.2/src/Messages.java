import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sun.rmi.runtime.Log;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Messages {
    private List<Message> messages;

    public Messages() {
        messages = new ArrayList<>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void newMessage() {
        long id = 0;
        String message;
        String author;
        long timestamp = 0;

        Date date = new Date();
        Scanner sc = new Scanner(System.in);


        System.out.println("Enter name: ");
        author = sc.nextLine();

        System.out.println("Enter message: ");
        message = sc.nextLine();

        id = generateID();

        timestamp = date.getTime();

        addMessage(new Message(id, message, author, String.valueOf(timestamp)));
    }

    public long generateID() {
        long id;
        if (messages.size() > 0) {
            id = messages.get(messages.size() - 1).getId() + 1;
        } else {
            id = 1;
        }
        return id;
    }

    public void showMessages() {
        for (Message ms : messages) {
            System.out.println(ms.show());
        }
    }

    public void readJSON(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(file));
        if (file.length() > 4) {
            messages = gson.fromJson(br, new TypeToken<ArrayList<Message>>() {
            }.getType());
            System.out.println(messages.toString());
        } else {
            System.out.println("Empty file");
        }
    }

    public void writeJSON(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        PrintStream ps = new PrintStream(file);
        ps.append(gson.toJson(messages));
    }

    public void mesDel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter id of message to delete: ");
        long id = sc.nextLong();
        for (Message ms : messages) {
            if (ms.getId() == id) {
                messages.remove(ms);
                return;
            }
        }
    }

    public void findAuth() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter author name to find: ");
        String author = sc.nextLine();
        for (Message ms : messages) {
            if (ms.getAuthor().equals(author)) {
                System.out.println(ms.show());
            }
        }
    }

    public void findWord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter word (words) to find: ");
        String word = sc.nextLine();
        for (Message ms : messages) {
            if (ms.getMessage().indexOf(word) > -1) {
                System.out.println(ms.show());
            }
        }
    }

    public void regexSearch() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter regex to find: ");
        String regex = sc.nextLine();
        for (Message ms : messages) {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(ms.getMessage());
            if (m.matches() == true) {
                System.out.println(ms.show());
            }
        }
    }

    public void dataSearch() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the date from which period to search (yyyy-MM-dd HH:mm): ");
        String start = sc.nextLine();
        System.out.println("Enter the date to which period to search (yyyy-MM-dd HH:mm): ");
        String finish = sc.nextLine();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            long s = sdf.parse(start).getTime();
            long f = sdf.parse(finish).getTime();

            for (Message ms : messages) {
                if ((Long.valueOf(ms.getTimestamp()) > s) && (Long.valueOf(ms.getTimestamp()) < f)) {
                    System.out.println(ms.show());
                }
            }
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }
}


