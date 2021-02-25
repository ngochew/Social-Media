package bg.sofia.uni.fmi.mjt.socialmedia;

import bg.sofia.uni.fmi.mjt.socialmedia.content.Content;

import java.util.Comparator;

public class PopularityComparator implements Comparator<Content> {
    @Override
    public int compare(Content c1, Content c2) {
        // the Set treats the equal values as same Content
        // so we return true when they are equal, because we don't care
        // which Content will be first
        if ((c2.getNumberOfLikes() + c2.getNumberOfComments()) ==
                (c1.getNumberOfComments() + c1.getNumberOfComments())) {
            return 1;
        }
        return Integer.compare((c2.getNumberOfLikes() + c2.getNumberOfComments()),
                (c1.getNumberOfComments() + c1.getNumberOfComments()));
    }
}
