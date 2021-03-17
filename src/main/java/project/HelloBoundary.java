package DanielHays.Team;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// JSON SAMPLE: 
//{
//	"message":"hello", 
//	"counter":55, 
//	"messageTimestamp":"2021-03-03T16:15:12.000+00:00",
//	"userDetails":{
//		"first":"Jane",
//		"last":"Janes"
//  }, 
//	"important": true,
//	"moreDetails":{
//		"x":"string",
//		"y":12.32,
//		"z":false,
//		"t":{
//			"t1":"other string"
//		}
//	}
//}
public class HelloBoundary {
	private String message;
	private int counter;
	private Date messageTimestamp;
	private UserDetailsBoundary userDetails;
	private Boolean important;
	private Map<String, Object> moreDetails;

	public HelloBoundary() {
		this.important = false;
		this.moreDetails = new HashMap<>();
	}

	public HelloBoundary(String message, int counter, UserDetailsBoundary userDetails) {
		this();
		this.message = message;
		this.counter = counter;
		this.messageTimestamp = new Date();
		this.userDetails = userDetails;
	}

	public Boolean getImportant() {
		return important;
	}
	
	public void setImportant(Boolean important) {
		this.important = important;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public Date getMessageTimestamp() {
		return messageTimestamp;
	}
	
	public void setMessageTimestamp(Date messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}

	public UserDetailsBoundary getUserDetails() {
		return userDetails;
	}
	
	public void setUserDetails(UserDetailsBoundary userDetails) {
		this.userDetails = userDetails;
	}
	
	public Map<String, Object> getMoreDetails() {
		return moreDetails;
	}
	
	public void setMoreDetails(Map<String, Object> moreDetails) {
		this.moreDetails = moreDetails;
	}	
	
	@Override
	public String toString() {
		return "HelloBoundary [message=" + message 
				+ ", counter=" + counter 
				+ ", messageTimestamp=" + messageTimestamp
				+ ", userDetails=" + this.userDetails
				+ ", important=" + this.important
				+ ", moreDetails=" + this.moreDetails
				+ "]";
	}
	
	
}



