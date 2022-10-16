package spyrabarber.web.exception;

public class FieldException extends RuntimeException{
    private String field;

    public FieldException(String message, String field){
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
