package twins;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.boundaries.ItemBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoTests {
	
	private RestTemplate restTemplate;
	private int port;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
	}
	
	@Test
	public void testGetAllItemsWithEmptyDatabase() {
		ItemBoundary newItem = new ItemBoundary();
		newItem.setName("test1");
		newItem.setType("demo");
		
//		this.restTemplate.postForObject("http://localhost:" + this.port +"/twins/items/{userSpace}/{userEmail}",
//				newItem, ItemBoundary.class, "demoSpace", "demo@email.com");
		
		
		
		ItemBoundary[] arr = this.restTemplate.getForObject("http://localhost:" + this.port + "/twins/items/{userSpace}/{userEmail}",
				ItemBoundary[].class, "demoSpace", "demo@email.com");
		
		assertThat(arr).isNotNull().isEmpty();
	}
	
	
}
