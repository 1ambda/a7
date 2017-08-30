import { Injectable, } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { Observable } from "rxjs/Observable"
import { Message } from '@stomp/stompjs'
import { StompService, StompState } from '@stomp/ng2-stompjs'
import { ApiService, TOPIC_BROADCAST_WEBSOCKET_COUNT, TOPIC_UNICAST_WEBSOCKET_COUNT } from '../util/'

@Injectable()
export class WebsocketService {
  constructor(private stompService: StompService) {
  }

  subscribeWebsocketConnectedStatus(): Observable<string> {
    return this.stompService.state
      .map((state: number) => StompState[state])
      .filter(state => state === 'CONNECTED')
  }

  subscribeCurrentConnectionCountBroadcast(): Observable<number> {
    return this.stompService
      .subscribe(TOPIC_BROADCAST_WEBSOCKET_COUNT)
      .map(message => {
        let count = 0
        if (message.body != null) { count = parseInt(message.body) }
        return count
      })
  }

  subscribeOnceCurrentConnectionCountUnicast(): Observable<number> {
    return this.stompService
      .subscribe(TOPIC_UNICAST_WEBSOCKET_COUNT)
      .map(message => {
        let count = 0
        if (message.body != null) { count = parseInt(message.body) }
        return count
      })
  }
}
