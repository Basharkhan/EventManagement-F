export class SpecialEvent {
 
      eventName:String;
      eventAddress:String;
      eventDescription:String;
      url:String;
      organizerName:String;
      organizerDetails:String;
      eventCreationDate:String;
      eventDate:String;
      buyEvent:String;
      country:String;
      date: String;
      startTime: String;
      endTime: String;
      vacancy: number;
      category: string;
      counter: number;
      price: number;
      // subEvents: Array<any> = [];
      subEvents: SubEvent;
}

export class SubEvent{
      name: string;
      startTime: string;
      endTime: string;
      selected: boolean;
      vacancy: number;
}