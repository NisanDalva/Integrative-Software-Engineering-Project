package twins.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import twins.boundaries.OperationBoundary;

@Component
public class MomListener {
	private ObjectMapper jackson;
	private AdvancedOperationsService logic;
	
	@Autowired
	public MomListener(AdvancedOperationsService logic){
		this.jackson = new  ObjectMapper(); 
		this.logic = logic;
	}
	

	@JmsListener(destination = "Operations")
	@Transactional
	public void handleMessagesFromMom (String json) {
		try {
			// unmarshalling of message json
			OperationBoundary boundary = this.jackson
					.readValue(json, OperationBoundary.class);
			
			logic.invokeAsynchronousOperation(boundary);
		} catch (Exception e) {
			// make sure you log error messages - so that errors will not be lost
			e.printStackTrace();
		}
	}
}
