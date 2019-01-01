import { Component, OnInit } from '@angular/core';
import { SpecialEventServiceService } from 'src/app/servicesEvent/Special/special-event-service.service';
import { TokenStorageServiceService } from 'src/app/services/token-storage-service.service';

@Component({
  selector: 'app-special-event-list',
  templateUrl: './special-event-list.component.html',
  styleUrls: ['./special-event-list.component.css']
})
export class SpecialEventListComponent implements OnInit {

  generalEvent: any;
  searchText: string;
  currentUser:string;


  constructor(private eventService: SpecialEventServiceService, private token: TokenStorageServiceService) { 
    this.currentUser = this.token.getUsername();

  }

  ngOnInit() {
    this.eventService.getAllSpecialEvents().subscribe(
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
