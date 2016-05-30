package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.json";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    public InMemoryMessageStorage() {
        try {
            readJSON(new File(DEFAULT_PERSISTENCE_FILE));
        } catch (IOException e) {
            logger.error("Error while opening file:", e);
        }
    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        try {
            writeJSON(new File(DEFAULT_PERSISTENCE_FILE));
        } catch (FileNotFoundException e) {
            logger.error("Eror while opening file: ", e);
        }
    }

    @Override
    public boolean updateMessage(Message message) {
        for (Message ms : messages) {
            if (message.getId().equals(ms.getId())) {
                ms.setText(message.getText());
                ms.setEdited(true);
                //logger.append("Message the given id was successfully removed");
                try {
                    writeJSON(new File(DEFAULT_PERSISTENCE_FILE));
                } catch (FileNotFoundException e) {
                    logger.error("Eror while opening file: ", e);
                }
                return true;
            }
        }
        return false;
        //throw new UnsupportedOperationException("Update for messages is not supported yet");
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (Message ms : messages) {
            if (messageId.equals(ms.getId())) {
                ms.setDeleted(true);
                //logger.append("Message the given id was successfully removed");
                try {
                    writeJSON(new File(DEFAULT_PERSISTENCE_FILE));
                } catch (FileNotFoundException e) {
                    logger.error("Eror while opening file: ", e);
                }
                return true;
            }
        }
        //throw new Exception("Messages with this id does not exist!");
        //throw new UnsupportedOperationException("Removing of messages is not supported yet");
        return false;
    }

    public void readJSON(File file) throws IOException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(file));
        if (file.length() > 4) {
            messages = gson.fromJson(br, new TypeToken<ArrayList<Message>>() {
            }.getType());
            logger.info("File has been successfully read");
            //System.out.println(messages.toString());
        } else {
            System.out.println("JSON-file is empty");
            //logger.append("JSON-file is empty");
        }
        br.close();
    }

    public void writeJSON(File file) throws FileNotFoundException {
        Gson gson = new Gson();
        PrintStream psf = new PrintStream(file);
        psf.append(gson.toJson(messages));
        //logger.append("Information was recorded\n");
    }

    @Override
    public int size() {
        return messages.size();
    }
}
