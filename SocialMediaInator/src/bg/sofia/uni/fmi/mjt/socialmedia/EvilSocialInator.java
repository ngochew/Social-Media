
package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Post;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Story;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.ContentNotFoundException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.NoUsersException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.socialmedia.exceptions.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;


public class EvilSocialInator implements SocialMediaInator {

    private static int numberOfPublication = 0;

    private Map<String, User> users = new LinkedHashMap<>();
    private Map<String, Post> posts = new LinkedHashMap<>();
    private Map<String, Story> storys = new LinkedHashMap<>();
    private Map<String, TreeMap<LocalDateTime, String>> activityLog = new LinkedHashMap<>();

    @Override
    public void register(String username) {
        if (username == null) {
            throw new IllegalArgumentException();
        }

        if (users.containsKey(username)) {
            throw new UsernameAlreadyExistsException();
        }

        User newUser = new User(username);
        users.put(username, newUser);
        TreeMap<LocalDateTime, String> emptyTreeMap = new TreeMap<>();
        activityLog.put(username, emptyTreeMap);
    }

    @Override
    public String publishPost(String username, LocalDateTime publishedOn, String description) {
        if (username == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException();
        }
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException();
        }

        String id = username + '-' + numberOfPublication;
        numberOfPublication++;
        Post newPost = new Post(id, publishedOn, description);

        users.get(username).addContent(newPost);
        posts.put(id, newPost);
        activityLog.get(username).put(publishedOn, " Created a post with id " + id);
        return id;
    }

    @Override
    public String publishStory(String username, LocalDateTime publishedOn, String description) {
        if (username == null || publishedOn == null || description == null) {
            throw new IllegalArgumentException();
        }
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException();
        }

        String id = username + '-' + numberOfPublication;
        numberOfPublication++;
        Story newStory = new Story(id, publishedOn, description);

        users.get(username).addContent(newStory);
        storys.put(id, newStory);
        activityLog.get(username).put(publishedOn, " Created a story with id " + id);
        return id;
    }

    @Override
    public void like(String username, String id) {
        if (username == null || id == null) {
            throw new IllegalArgumentException();
        }
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException();
        }
        if (!posts.containsKey(id) && !storys.containsKey(id)) {
            throw new ContentNotFoundException();
        }

        if (posts.containsKey(id)) {
            posts.get(id).addLike(username);

        } else {
            storys.get(id).addLike(username);
        }
        activityLog.get(username).put(LocalDateTime.now(), " Liked a content with id " + id);
    }

    @Override
    public void comment(String username, String text, String id) {
        if (username == null || id == null) {
            throw new IllegalArgumentException();
        }

        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException();
        }

        if (!posts.containsKey(id) && !storys.containsKey(id)) {
            throw new ContentNotFoundException();
        }

        if (posts.containsKey(id)) {
            posts.get(id).addComment(username, text);
        } else {
            storys.get(id).addComment(username, text);
        }

        activityLog.get(username).put(LocalDateTime.now(), " Commented \"" + text + "\" on a content with id " + id);
    }

    @Override
    public Collection<Content> getNMostPopularContent(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }

        Set<Content> mostPopularContent = new TreeSet<>(new PopularityComparator());
        for (Map.Entry<String, Post> currentPost : posts.entrySet()) {
            mostPopularContent.add(currentPost.getValue());
        }

        for (Map.Entry<String, Story> currentStory : storys.entrySet()) {
            mostPopularContent.add(currentStory.getValue());
        }

        List<Content> mostPopularNContent = new ArrayList<>();
        for (Content content : mostPopularContent) {
            if (mostPopularNContent.size() == n) {
                return mostPopularNContent;
            }
            mostPopularNContent.add(content);
        }
        return mostPopularNContent;
    }

    @Override
    public Collection<Content> getNMostRecentContent(String username, int n) {
        if (username == null || n < 0) {
            throw new IllegalArgumentException();
        }
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException();
        }

        List<Content> mostRecentContent = new ArrayList<>();

        for (Content currentPublication : users.get(username).getPublicationsActivityLog()) {
            if (mostRecentContent.size() == n) {
                break;
            }
            if (currentPublication instanceof Story
                    && ((Story) currentPublication).getPublishDate().isAfter(LocalDateTime.now().minusHours(24))) {
                mostRecentContent.add(currentPublication);
            } else if (currentPublication instanceof Post
                    && ((Post) currentPublication).getPublishDate().isAfter(LocalDateTime.now().minusDays(30))) {
                mostRecentContent.add(currentPublication);
            }
        }

        return mostRecentContent;
    }

    @Override
    public String getMostPopularUser() {
        if (users.isEmpty()) {
            throw new NoUsersException();
        }

        Map<String, Integer> userPopularitys = new LinkedHashMap<>();
        for (Map.Entry<String, User> currentUser : users.entrySet()) {
            userPopularitys.put(currentUser.getKey(), 0);
        }
        for (Map.Entry<String, Post> currentPublication : posts.entrySet()) {
            for (String currentMention : currentPublication.getValue().getMentions()) {
                userPopularitys.put(currentMention.substring(1), userPopularitys.get(currentMention.substring(1)) + 1);
            }
        }
        for (Map.Entry<String, Story> currentPublication : storys.entrySet()) {
            for (String currentMention : currentPublication.getValue().getMentions()) {
                userPopularitys.put(currentMention.substring(1), userPopularitys.get(currentMention.substring(1)) + 1);
            }
        }

        int highestPopularity = -1;
        String mostPopularUser = null;
        for (Map.Entry<String, Integer> currentUser : userPopularitys.entrySet()) {
            if (currentUser.getValue() > highestPopularity) {
                highestPopularity = currentUser.getValue();
                mostPopularUser = currentUser.getKey();
            }
        }
        return mostPopularUser;
    }

    @Override
    public Collection<Content> findContentByTag(String tag) {
        if (tag == null) {
            throw new IllegalArgumentException();
        }

        List<Content> contentWithCurrentTag = new ArrayList<>();

        for (Map.Entry<String, Post> currentPost : posts.entrySet()) {
            if (currentPost.getValue().getPublishDate().isAfter(LocalDateTime.now().minusDays(30))
                    && currentPost.getValue().getTags().contains(tag)) {
                contentWithCurrentTag.add(currentPost.getValue());
            }
        }
        for (Map.Entry<String, Story> currentStory : storys.entrySet()) {
            if (currentStory.getValue().getPublishDate().isAfter(LocalDateTime.now().minusHours(24))
                    && currentStory.getValue().getTags().contains(tag)) {
                contentWithCurrentTag.add(currentStory.getValue());
            }
        }

        return contentWithCurrentTag;
    }


    @Override
    public List<String> getActivityLog(String username) {
        List<String> currentActivityLog = new ArrayList<>();

        for (Map.Entry<LocalDateTime, String> currentActivity : activityLog.get(username).entrySet()) {
            currentActivityLog.add(formatTime(currentActivity.getKey()) + currentActivity.getValue());
        }

        Collections.reverse(currentActivityLog);
        return currentActivityLog;
    }

    private String formatTime(LocalDateTime timeToFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd.MM.yy");
        return timeToFormat.format(formatter);
    }
}
