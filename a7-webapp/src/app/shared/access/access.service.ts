import { Injectable, } from '@angular/core'
import { HttpClient } from '@angular/common/http'
import { Observable } from "rxjs/Observable"
import { Message } from '@stomp/stompjs'
import { StompService } from '@stomp/ng2-stompjs'

import { ApiService, TOPIC_BROADCAST_ACCESS_COUNT, TOPIC_UNICAST_ACCESS_COUNT } from '../util'
import { Access } from './access'
import { PaginatedResponse } from '../model'

@Injectable()
export class AccessService {
  constructor(private http: HttpClient,
              private stompService: StompService,
              private apiService: ApiService) {
  }

  getUrlBase() {
    const url = `${this.apiService.getApiBaseUrl()}/access`
    return url
  }

  sendInitialAccessHistory() {
    const request = Access.create()
    const url = this.getUrlBase()
    return this.http.post(url, request, { withCredentials: true }).toPromise()
  }

  findAllAccess(pageNumber: number, pageSize: number,
                sortColumn: string, sortDirection: string): Promise<PaginatedResponse> {
    let url = `${this.getUrlBase()}?page=${pageNumber}&size=${pageSize}`

    if (typeof sortDirection !== "undefined" && typeof sortColumn !== "undefined") {
      url = `${url}&sort=${sortColumn},${sortDirection}`
    }
    return this.http.get<PaginatedResponse>(url)
      .toPromise()
  }

  subscribeTotalAccessCountBroadcast(): Observable<number> {
    return this.stompService
      .subscribe(TOPIC_BROADCAST_ACCESS_COUNT)
      .map(message => {
        let count = 0
        if (message.body != null) { count = parseInt(message.body) }
        return count
      })
  }

  subscribeOnceTotalAccessCountUnicast(): Observable<number> {
    return this.stompService
      .subscribe(TOPIC_UNICAST_ACCESS_COUNT)
      .map(message => {
        let count = 0
        if (message.body != null) { count = parseInt(message.body) }
        return count
      })
  }
}
