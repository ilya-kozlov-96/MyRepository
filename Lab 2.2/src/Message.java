import sun.plugin2.gluegen.runtime.StructAccessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by ilia1 on 21.02.2016.
 */
public class Message {
    private long id;
    private String message;
    private String author;
    private String timestamp;

    public Message(long id, String message, String author, String timestamp) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String date(String timestamp) {
        String data;
        DateFormat TimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        data = TimeStamp.format(Long.parseLong(timestamp));
        return data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", author='" + author + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public String show() {
        return "ID = " + id + "\nauthor = " + author + "\nmessage = " + message + "\ntime = " + date(timestamp) + "\n";
    }
}
