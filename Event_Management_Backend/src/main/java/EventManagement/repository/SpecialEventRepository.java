package EventManagement.repository;

import EventManagement.model.SpecialEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpecialEventRepository extends MongoRepository<SpecialEvent, String> {
    SpecialEvent findSpecialEventById(String id);
    void deleteSpecialEventById(String id);

}
