package hu.progmasters.ujratervezes.week16.dailybugle.service;

public class TableDoesNotExistException extends RuntimeException {

    public TableDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
