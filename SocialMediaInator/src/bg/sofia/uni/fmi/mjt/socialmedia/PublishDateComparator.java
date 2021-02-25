package bg.sofia.uni.fmi.mjt.socialmedia;


import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Post;
import bg.sofia.uni.fmi.mjt.socialmedia.content.Story;

import java.util.Comparator;

public class PublishDateComparator implements Comparator<Content> {
    @Override
    public int compare(Content c1, Content c2) {
        if (c1 instanceof Post && c2 instanceof Post) {
            return ((Post) c2).getPublishDate().compareTo(((Post) c1).getPublishDate());
        }
        if (c1 instanceof Story && c2 instanceof Story) {
            return ((Story) c2).getPublishDate().compareTo(((Story) c1).getPublishDate());
        }
        if (c1 instanceof Post && c2 instanceof Story) {
            return ((Story) c2).getPublishDate().compareTo(((Post) c1).getPublishDate());
        }
        if (c1 instanceof Story && c2 instanceof Post) {
            return ((Post) c2).getPublishDate().compareTo(((Story) c1).getPublishDate());
        }
        return 0;
    }
}

