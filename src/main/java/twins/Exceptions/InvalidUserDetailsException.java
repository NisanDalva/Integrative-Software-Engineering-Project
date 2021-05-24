package twins.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class InvalidUserDetailsException extends RuntimeException {
	private static final long serialVersionUID = -3852861819213897449L;

	public InvalidUserDetailsException() {
	}

	public InvalidUserDetailsException(String message) {
		super(message);
	}

	public InvalidUserDetailsException(Throwable cause) {
		super(cause);
	}

	public InvalidUserDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
}
