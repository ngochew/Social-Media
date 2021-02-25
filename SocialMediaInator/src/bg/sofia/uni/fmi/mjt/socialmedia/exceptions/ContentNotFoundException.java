package bg.sofia.uni.fmi.mjt.socialmedia.exceptions;

public class ContentNotFoundException extends RuntimeException{
    public ContentNotFoundException(){
        super("Content not found.");
    }
}
