package EventManagement.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "specialEvent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialEvent {
    private String id;
    private String eventName;
    private String eventAddress;
    private String eventDescription;
    private String url;
    private String organizerName;
    private String organizerDetails;
    private String eventCreationDate;
    private String eventDate;
    private String buyEvent;
    private String country;
    private String date;
    private int counter = 0;
    private String startTime;
    private String endTime;
    private int vacancy;
    private String category;
    private List<SubEvent> subEvents;
}
