package spyrabarber.web.exception;

public class SelfExclusionException extends RuntimeException{
    public SelfExclusionException(String m){
        super(m);
    }
}
