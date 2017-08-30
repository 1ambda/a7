import { Component, OnInit, OnDestroy, } from '@angular/core'
import { Subscription } from 'rxjs/Subscription'
import { Message } from '@stomp/stompjs'

import { AccessService, Access } from '../../shared/access'
import { Pagination } from '../../shared/model'
import { WebsocketService } from '../../shared/websocket'
import { SessionService } from '../../shared/session'

const PAGE_SIZE = 10

@Component({
  selector: 'home',
  providers: [
  ],
  styleUrls: [ './home.component.css' ],
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit, OnDestroy {
  private rows: Array<Access> = []
  private columns: Array<any> = [
    { name: 'browserName' },
    { name: 'browserVersion' },
    { name: 'osName' },
    { name: 'osVersion' },
    { name: 'isMobile' },
    { name: 'timezone' },
    { name: 'timestamp' },
    { name: 'language' },
    { name: 'userAgent', width: 1000, },
  ]
  private currentWebsocketConnectionCount = 0
  private currentLiveSessionCount = 0
  private subscriptions: Array<Subscription> = []
  private pagination: Pagination = new Pagination(0, PAGE_SIZE, 0)
  private sortDirection: string = undefined
  private sortColumn: string = undefined
  private tableLoading = true;

  constructor(private accessService: AccessService,
              private websocketService: WebsocketService,
              private sessionService: SessionService) {
  }

  public ngOnInit() {
    this.subscriptions.push(
      this.accessService.subscribeTotalAccessCountBroadcast()
        .subscribe(count => {
          this.pagination.contentSize = count
        })
    )

    this.subscriptions.push(
      this.sessionService.subscribeLiveSessionCountBroadcast()
        .subscribe(count => {
          this.currentLiveSessionCount = count
        })
    )

    this.subscriptions.push(
      this.websocketService.subscribeCurrentConnectionCountBroadcast()
        .subscribe(count => {
          this.currentWebsocketConnectionCount = count
        })
    )

    // fetch count when (re)connected
    this.subscriptions.push(
      this.websocketService.subscribeWebsocketConnectedStatus()
        .subscribe(() => { this.initialize() })
    )
  }

  initialize() {
    setTimeout(() => {
      // automatically unsubscribed
      this.accessService.subscribeOnceTotalAccessCountUnicast()
        .subscribe(count => { this.pagination.contentSize = count })

      // automatically unsubscribed
      this.sessionService.subscribeOnceLiveSessionCountUnicast()
        .subscribe(count => { this.currentLiveSessionCount = count })

      // automatically unsubscribed
      this.websocketService.subscribeOnceCurrentConnectionCountUnicast()
        .subscribe(count => { this.currentWebsocketConnectionCount = count })
    }, 500)

    this.fetchAllAccess()
  }

  fetchAllAccess() {
    this.tableLoading = true
    this.accessService.findAllAccess(this.pagination.pageNumber, this.pagination.pageSize,
      this.sortColumn, this.sortDirection)
      .then(paginatedResponse => {
        this.pagination = paginatedResponse.meta
        this.rows = paginatedResponse.content
        this.tableLoading = false
      })
  }

  onPageChange(event){
    this.pagination.pageNumber = event.offset;
    this.fetchAllAccess()
  }

  onSortChange(event) {
    const sort = event.sorts[0] /** ngx-datatable doesn't support multiple column sorting */
    this.sortDirection = sort.dir
    this.sortColumn = sort.prop
    this.pagination.pageNumber = 0 /** reset to page number 0 when sorting */
    this.fetchAllAccess()
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => {
      sub.unsubscribe()
    })
  }
}
