package bg.sofia.uni.fmi.mjt.socialmedia.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(){
        super("Username already exists");
    }
}
