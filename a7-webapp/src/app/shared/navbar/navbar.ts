import {Component, NgModule, OnInit, OnDestroy} from '@angular/core'
import {MdButtonModule, MdToolbarModule, MdMenuModule} from '@angular/material'
import {RouterModule} from '@angular/router'
import {Subscription} from 'rxjs/Subscription'

import {SessionService} from '../session'

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.scss']
})
export class Navbar implements OnInit, OnDestroy {
  private session: string = null
  private subscriptions: Array<Subscription> = []

  constructor(private sessionService: SessionService) {
  }

  ngOnInit() {
    this.subscriptions.push(
      this.sessionService.subscribeSessionChange()
        .subscribe(session => {
          this.session = session
        })
    )
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => {
      sub.unsubscribe()
    })
  }

  createButtonClicked(event) {
    event.preventDefault();
    this.sessionService.createSession()
  }

  invalidateButtonClicked(event) {
    event.preventDefault();
    this.sessionService.invalidateSession()
  }
}

@NgModule({
  imports: [MdButtonModule, MdToolbarModule, MdMenuModule, RouterModule],
  exports: [Navbar],
  declarations: [Navbar],
})
export class NavbarModule {}