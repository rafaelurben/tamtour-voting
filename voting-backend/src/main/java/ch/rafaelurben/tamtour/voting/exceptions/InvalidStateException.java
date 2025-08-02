package ch.rafaelurben.tamtour.voting.exceptions;

public class InvalidStateException extends RuntimeException {
  public InvalidStateException(String message) {
    super(message);
  }
}
