package news_recommend.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class NewsRecommendApplication{

	public static void main(String[] args) {
		SpringApplication.run(NewsRecommendApplication.class, args);
	}

}
