import { Component, OnInit } from '@angular/core';
import { MainService } from '../main.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {

  time: string = '';

  constructor(
    private mainService: MainService
  ) { }

  ngOnInit(): void {
    this.mainService.getTime().subscribe(res => {
      this.time = res;
    })
  }

}
