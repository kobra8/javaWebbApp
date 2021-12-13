import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

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
}