package news_recommend.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsRecommendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsRecommendApplication.class, args);
	}

}
