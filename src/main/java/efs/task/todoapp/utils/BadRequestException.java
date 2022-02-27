package efs.task.todoapp.utils;

public class BadRequestException extends Exception{
    private int responseCode;
    public BadRequestException(String errorMessage, int responseCode){
        super(errorMessage);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
