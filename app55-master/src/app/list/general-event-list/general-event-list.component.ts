import { Component, OnInit } from '@angular/core';
import { GeneralEventServiceService } from 'src/app/servicesEvent/general/general-event-service.service';
import { TokenStorageServiceService } from 'src/app/services/token-storage-service.service';

@Component({
  selector: 'app-general-event-list',
  templateUrl: './general-event-list.component.html',
  styleUrls: ['./general-event-list.component.css']
})
export class GeneralEventListComponent implements OnInit {

  generalEvent: any;
  searchText: string

  constructor(private eventService: GeneralEventServiceService, private token: TokenStorageServiceService) { }

  ngOnInit() {
    this.eventService.getAllGeneralEvents().subscribe(
      data => this.generalEvent = data
    ), error => console.log(error)
  }

  delete(event) {
    this.eventService.deleteEvent(event.id)
    .subscribe( data => {
      this.generalEvent.splice(this.generalEvent.indexOf(event), 1);
      window.location.reload();
    },error => {
      console.log(error);
    })
  }

}
