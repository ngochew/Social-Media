package bg.sofia.uni.fmi.mjt.socialmedia.exceptions;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(){
        super("Couldn't find this username");
    }
}
