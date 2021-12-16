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
  messages: Message[] = [];
  newMessage: Message = {author: '', content: ''};

  constructor(
    private mainService: MainService
  ) { }

  ngOnInit(): void {
    this.mainService.getTime().subscribe(res => {
      this.time = res;
    })
    this.mainService.getMessages().subscribe(res => {
      this.messages = res;
    })
  }

  sendMessage(): void {
    this.mainService.postMessage(this.newMessage).subscribe(res => {
      this.messages = res;
      this.newMessage.content = '';
    })
  }
}
