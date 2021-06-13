package twins.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3852861819213897449L;

	public ItemNotFoundException() {
	}

	public ItemNotFoundException(String message) {
		super(message);
	}

	public ItemNotFoundException(Throwable cause) {
		super(cause);
	}

	public ItemNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
