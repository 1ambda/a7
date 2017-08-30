import {
  Component,
  OnInit,
  ViewEncapsulation,
} from '@angular/core'
import {ApiService} from './shared/util'
import {SessionService} from './shared/session'
import {AccessService} from './shared/access'

/**
 * App Component
 * Top Level Component
 */
@Component({
  selector: 'app',
  encapsulation: ViewEncapsulation.None,
  styleUrls: [ './app.component.css' ],
  templateUrl: './app.component.html',
})
export class AppComponent implements OnInit {
  constructor(private apiService: ApiService,
              private sessionService: SessionService,
              private accessService: AccessService) {}

  public ngOnInit() {
    /** initialization */
    this.sessionService.createSession()
      .then(() => this.accessService.sendInitialAccessHistory())
  }
}
