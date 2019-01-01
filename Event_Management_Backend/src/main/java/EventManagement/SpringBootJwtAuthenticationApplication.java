package EventManagement;

import EventManagement.model.GeneralEvent;
import EventManagement.repository.GeneralEventRepository;
import EventManagement.repository.SpecialEventRepository;
import EventManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SpringBootJwtAuthenticationApplication  {

    @Autowired
    private GeneralEventRepository generalEventRepository;
    @Autowired
    private SpecialEventRepository specialEventRepository;
    @Autowired
    UserRepository userRepository;
    public static void main(String[] args) {
        SpringApplication.run(SpringBootJwtAuthenticationApplication.class, args);
    }

    @Bean
    CommandLineRunner runner () {
        return args -> {
            List<GeneralEvent> generalEventList = generalEventRepository.findAll();
            for(int i = 0; i < generalEventList.size(); i++) {
                System.out.println(generalEventList.get(i).getEventName());
                System.out.println(generalEventList.get(i).getEventDescription());
            }
        };
    }
}