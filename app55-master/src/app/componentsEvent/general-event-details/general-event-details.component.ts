import { Component, OnInit, ElementRef ,ViewChild} from '@angular/core';  
import { GeneralEventServiceService } from 'src/app/servicesEvent/general/general-event-service.service';
import { ActivatedRoute } from '@angular/router';
import { TokenStorageServiceService } from '../../services/token-storage-service.service';

@Component({
  selector: 'app-general-event-details',
  templateUrl: './general-event-details.component.html',
  styleUrls: ['./general-event-details.component.css']
})
export class GeneralEventDetailsComponent implements OnInit {

  event:any;
  userName: string;

  constructor(private eventService: GeneralEventServiceService, 
              private activatedRoute: ActivatedRoute,
              private tokenStorageService: TokenStorageServiceService
              ) {
                  this.userName = this.tokenStorageService.getUsername()
                }
  
  ngOnInit() {
    this.getEvent()        
  }

  getEvent() {
    const id = this.activatedRoute.snapshot.paramMap.get('id');
    this.eventService.getGeneralEventById(id).subscribe(
      event => {this.event = event, console.log(event)}
    )
  }
}
