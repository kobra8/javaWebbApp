import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';
import { Message } from '../model';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  time: string = '';
  topicName: string = 'java'
  messages: Message[] = [];
  newMessage: Message = {author: '', content: ''};

  constructor(
    private mainService: MainService
  ) { }

  ngOnInit(): void {
    this.mainService.getTime().subscribe(res => {
      this.time = res;
    })
    this.getMessages();
  }

  changeTopic(topicName: string) {
    this.topicName = topicName;
    this.getMessages();
  }

  getMessages() {
    this.mainService.getMessages(this.topicName).subscribe(res => {
      this.messages = res;
    })
  }

  sendMessage(): void {
    this.mainService.postMessage(this.newMessage, this.topicName).subscribe(res => {
      this.messages = res;
      this.newMessage.content = '';
      this.getMessages();
    })
  }
}
