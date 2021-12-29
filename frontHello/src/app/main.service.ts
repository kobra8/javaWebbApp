import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Message } from './model';


@Injectable({
  providedIn: 'root'
})
export class MainService {

  constructor(
    private http: HttpClient,
  ) { }

  getTime(): Observable<string> {
    return this.http.get('/api/time', {responseType: 'text'}).pipe(map(response => {
      return response;
    }));
  }

  getMessages(topicName: string): Observable<Message[]> {
    return this.http.get(`/api/messages/${topicName}`).pipe(map(response => {
      return response as Message[];
    }));
  }

  postMessage(message: Message, topicName: string): Observable<Message[]> {
    return this.http.post(`/api/messages/${topicName}`, message).pipe(map(response => {
      return response as Message[];
    }))
  }
}
