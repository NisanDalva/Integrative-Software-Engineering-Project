package twins.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UnsupportedOperationException extends RuntimeException {
	private static final long serialVersionUID = -3852861819213897449L;

	public UnsupportedOperationException() {
	}

	public UnsupportedOperationException(String message) {
		super(message);
	}

	public UnsupportedOperationException(Throwable cause) {
		super(cause);
	}

	public UnsupportedOperationException(String message, Throwable cause) {
		super(message, cause);
	}

}
