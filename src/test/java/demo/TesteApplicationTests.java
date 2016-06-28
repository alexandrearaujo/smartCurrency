package demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TesteApplication.class)
@WebIntegrationTest("server.port:8080")
public abstract class TesteApplicationTests {
	
	@Autowired
	private WebApplicationContext webContext;

	@Test
	public void contextLoads() {
	}

}
