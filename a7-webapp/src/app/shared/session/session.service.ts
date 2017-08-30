import { Injectable, } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { Observable } from "rxjs/Observable"
import { ReplaySubject } from "rxjs/ReplaySubject"
import { Message } from '@stomp/stompjs'
import { StompService } from '@stomp/ng2-stompjs'


import { ApiService, TOPIC_UNICAST_SESSION_COUNT, TOPIC_BROADCAST_SESSION_COUNT } from '../util'

const EMPTY_SESSION = "empty session"

@Injectable()
export class SessionService {
  private sessionEmitter: ReplaySubject<string> = new ReplaySubject()

  constructor(private http: HttpClient,
              private stompService: StompService,
              private apiService: ApiService) {
    this.sessionEmitter.next(EMPTY_SESSION)
  }

  getUrlBase() {
    const url = `${this.apiService.getApiBaseUrl()}/session`
    return url
  }

  subscribeSessionChange(): Observable<string> {
    return this.sessionEmitter
  }

  createSession() {
    const options = { withCredentials: true, }
    const url = `${this.getUrlBase()}/action/create`

    return this.http.post(url, {}, options)
      .toPromise()
      .then((res: any)  => {
        if (res.session) {
          this.sessionEmitter.next(res.session)
        }
      })
  }

  invalidateSession() {
    const url = `${this.getUrlBase()}/action/invalidate`
    return this.http.delete(url, { withCredentials: true })
      .toPromise()
      .then(() => {
        this.sessionEmitter.next(EMPTY_SESSION)
      })
  }

  subscribeLiveSessionCountBroadcast(): Observable<number> {
    return this.stompService
      .subscribe(TOPIC_BROADCAST_SESSION_COUNT)
      .map(message => {
        let count = 0
        if (message.body != null) { count = parseInt(message.body) }
        return count
      })
  }

  subscribeOnceLiveSessionCountUnicast(): Observable<number> {
    return this.stompService
      .subscribe(TOPIC_UNICAST_SESSION_COUNT)
      .map(message => {
        let count = 0
        if (message.body != null) { count = parseInt(message.body) }
        return count
      })
  }
}
