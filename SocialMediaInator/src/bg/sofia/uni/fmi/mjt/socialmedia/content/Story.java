package bg.sofia.uni.fmi.mjt.socialmedia.content;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;

public class Story implements Content {
    private String id;
    private List<String> tags = new ArrayList<>();
    private List<String> mentions = new ArrayList<>();
    private Set<String> likes = new LinkedHashSet<>();
    private Map<String, List<String>> comments = new LinkedHashMap<>();
    private LocalDateTime publishedOn;

    public Story(String id, LocalDateTime publishedOn, String description) {
        if (id == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.publishedOn = publishedOn;

        String[] descriptionWords = description.split(" ");
        for (String str : descriptionWords) {
            if (str.startsWith("@")) {
                mentions.add(str);
            } else if (str.startsWith("#")) {
                tags.add(str);
            }
        }
    }

    @Override
    public int getNumberOfLikes() {
        return likes.size();
    }

    @Override
    public int getNumberOfComments() {
        return comments.size();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Collection<String> getTags() {
        return this.tags;
    }

    @Override
    public Collection<String> getMentions() {
        return this.mentions;
    }

    public LocalDateTime getPublishDate() {
        return publishedOn;
    }

    public void addComment(String username, String text) {
        if (username == null || text == null) {
            throw new IllegalArgumentException();
        }

        if (this.comments.containsKey(username)) {
            this.comments.get(username).add(text);
        } else {
            this.comments.put(username, Collections.singletonList(text));
        }
    }

    public void addLike(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }

        likes.add(username);
    }
}
