package twins.logic;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Utils {
	private ObjectMapper jackson;
	
	public Utils() {
		this.jackson = new ObjectMapper();
	}

	// use Jackson to convert JSON to Object
	public <T> T unmarshal(String json, Class<T> type) {
		try {
			return this.jackson
					.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String marshal(Object moreDetails) {
		try {
			return this.jackson
					.writeValueAsString(moreDetails);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
