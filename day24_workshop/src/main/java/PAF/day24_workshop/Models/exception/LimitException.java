package PAF.day24_workshop.Models.exception;



public class LimitException extends RuntimeException {
    public LimitException(){
        super();
    }

    public LimitException(String msg){
        super(msg);
    }

    public LimitException(String msg, Throwable throwable){
        super(msg,throwable);
    }
}
