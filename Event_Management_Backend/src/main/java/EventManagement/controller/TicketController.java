package EventManagement.controller;

import EventManagement.model.*;
import EventManagement.repository.*;

import EventManagement.security.services.UserDetailsServiceImpl;
import EventManagement.service.UserService;
import com.itextpdf.text.DocumentException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    GridFsOperations gridFsOperations;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    UserService userService;

    @Autowired
    UserDetailsServiceImpl service;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpecialEventRepository specialEventRepository;

    @Autowired
    GeneralEventRepository generalEventRepository;

    @Autowired
    AmountFileRepo amountFileRepo;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/amount/{username}/{eventId}")
    public AmountFile amountToBePaid(@PathVariable("username") String username,
                               @PathVariable("eventId") String eventId) {
        String fileName = username + "-" + eventId;
        return amountFileRepo.findByFileName(fileName);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping("/check-conflict/{username}/{eventId}")
    public ResponseEntity<String> checkConflict(@PathVariable("username") String username, @PathVariable("eventId") String eventId, @RequestBody List<SubEvent> subEvents) throws MessagingException, FileNotFoundException, DocumentException {
        int amount = 0;

        if(subEvents.size() == 1) {
            for(int i = 0; i < subEvents.size(); i++) {
                amount += subEvents.get(i).getAmount();
            }

            AmountFile amountFile = new AmountFile(username + "-" + eventId, amount);
            AmountFile existingFile = amountFileRepo.findByFileName(amountFile.getFileName());
            if(existingFile == null) {
                amountFileRepo.save(amountFile);
            } else {
                System.out.println("I am inside a loop.");
                existingFile.setTotalAmount(amountFile.getTotalAmount());
                AmountFile amountFile1 = new AmountFile(existingFile.getId(), existingFile.getFileName(), amountFile.getTotalAmount());
                amountFileRepo.save(amountFile1);
            }

            System.out.println("No Time Conflict");
            userService.makePdf(username, eventId, subEvents);
            return ResponseEntity.status(HttpStatus.OK).body("No Time Conflict");
        }

        if(userService.isConflict(subEvents)) {
            System.out.println("Time Conflict");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Time Conflict");
        } else {
            System.out.println("No Time Conflict");

            for(int i = 0; i < subEvents.size(); i++) {
                amount += subEvents.get(i).getAmount();
            }
            AmountFile amountFile = new AmountFile(username + "-" + eventId, amount);
            AmountFile existingFile = amountFileRepo.findByFileName(amountFile.getFileName());
            if(existingFile == null) {
                amountFileRepo.save(amountFile);
            } else {
                System.out.println("I am inside a loop");
                existingFile.setTotalAmount(amountFile.getTotalAmount());
                AmountFile amountFile1 = new AmountFile(existingFile.getId(), existingFile.getFileName(), amountFile.getTotalAmount());
                amountFileRepo.save(amountFile1);
            }

            userService.makePdf(username, eventId, subEvents);
            return ResponseEntity.status(HttpStatus.OK).body("No Time Conflict");
        }

    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/send-mail/{username}/{eventId}")
    public ResponseEntity<String> sendMail(@PathVariable("username") String username, @PathVariable("eventId") String eventId) throws MessagingException, FileNotFoundException {
        System.out.println("Send Mail is called");
        User user = userRepository.findUserByUsername(username);
        SpecialEvent specialEvent = specialEventRepository.findSpecialEventById(eventId);

        String dest = "E:/Pdf/";

        String fileName = user.getUsername() + "-" + specialEvent.getId() + ".pdf";

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom("eventmanageteam@gmail.com");
        helper.setTo(user.getEmail());
        helper.setSubject("Event Management");
        helper.setText("Here is your attachment");

        FileSystemResource fileSystemResource = new FileSystemResource(dest + fileName);

        //save amountFile
        DBObject dbObject = new BasicDBObject();
        dbObject.put("Orgazitaion", "SEU");

        InputStream inputStream = new FileInputStream(dest + fileName);
        gridFsOperations.store(inputStream, fileName, "pdf", dbObject);

        helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
        javaMailSender.send(mimeMessage);

        return ResponseEntity.status(HttpStatus.OK).body("Email Sent");
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/retrieve-file")
    public String retrieveFile() {

        List<GridFSDBFile> files = gridFsOperations.find(new Query(Criteria.where("metadata.type").is("data")));

        files.forEach(file -> {
            String fileName = file.getFilename();
            System.out.println("AmountFile Name: " + fileName);
        });

        return "AmountFile Retrieved";
    }


   /* @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public List<Ticket> getAllTicket() {
        return ticketRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public Ticket saveTicket(@RequestBody Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public Optional<Ticket> getTicket(@PathVariable("id") String id) {
        return ticketRepository.findById(id);
    }*/


}
