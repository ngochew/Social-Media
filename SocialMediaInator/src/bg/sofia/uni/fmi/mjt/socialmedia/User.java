package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;


import java.util.Set;
import java.util.TreeSet;

public class User {
    private String username;
    private Set<Content> publicationsActivityLog = new TreeSet<>(new PublishDateComparator());

    public User(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void addContent(Content content) {
        if (content == null) {
            throw new IllegalArgumentException();
        }
        publicationsActivityLog.add(content);
    }

    public Set<Content> getPublicationsActivityLog() {
        return publicationsActivityLog;
    }
}
