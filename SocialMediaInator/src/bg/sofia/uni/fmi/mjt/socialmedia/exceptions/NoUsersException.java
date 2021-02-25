package bg.sofia.uni.fmi.mjt.socialmedia.exceptions;

public class NoUsersException extends RuntimeException {
    public NoUsersException(){
        super("No users registered");
    }
}
