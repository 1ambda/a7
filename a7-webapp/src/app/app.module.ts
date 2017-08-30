import { BrowserModule } from '@angular/platform-browser'
import { FormsModule } from '@angular/forms'
import { HttpClientModule } from '@angular/common/http'
import { HTTP_INTERCEPTORS } from '@angular/common/http'

import {
  NgModule,
  ApplicationRef
} from '@angular/core'
import {
  removeNgStyles,
  createNewHosts,
  createInputTransfer
} from '@angularclass/hmr'
import {
  RouterModule,
  PreloadAllModules
} from '@angular/router'

/** angular material related libraries */
import 'hammerjs' /* for gesture support */
import { BrowserAnimationsModule } from '@angular/platform-browser/animations' /* for animation support */
import { FlexLayoutModule } from "@angular/flex-layout"
/** end: angular material related libraries */

/** rx */
import 'rxjs/Rx'
import 'rxjs/add/operator/toPromise'
import 'rxjs/add/operator/catch'
import 'rxjs/add/operator/map'
/** end: rx */

import {MdCardModule} from '@angular/material'
import {NgxDatatableModule} from '@swimlane/ngx-datatable'
import {StompConfig, StompService} from '@stomp/ng2-stompjs'

/*
 * Platform and Environment providers/directives/pipes
 */
import { ENV_PROVIDERS } from './environment'
import { ROUTES } from './app.routes'
// App is our top level component
import { AppComponent } from './app.component'
import { HomeComponent } from './pages/home'
import { AboutComponent } from './pages/about'
import { NoContentComponent } from './pages/no-content'
import { NavbarModule } from './shared/navbar'
import { AccessService } from './shared/access'
import { WebsocketService } from './shared/websocket'
import { SessionService } from './shared/session'
import { ApiService } from './shared/util'

import '../styles/styles.scss'

type StoreType = {
  restoreInputValues: () => void,
  disposeOldHosts: () => void
}

const stompConfig: StompConfig = {
  url: 'ws://127.0.0.1:8034/stomp',
  headers: {},
  heartbeat_in: 0, // disabled
  heartbeat_out: 20000, // every 20 seconds
  reconnect_delay: 5000, // every 5 seconds
  debug: false,
}

@NgModule({
  bootstrap: [ AppComponent ],
  declarations: [
    AppComponent,
    AboutComponent,
    HomeComponent,
    NoContentComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FlexLayoutModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(ROUTES, { useHash: true, preloadingStrategy: PreloadAllModules }),
    NavbarModule,
    MdCardModule,
    NgxDatatableModule,
  ],
  providers: [
    ENV_PROVIDERS,
    StompService,
    { provide: StompConfig, useValue: stompConfig },
    ApiService,
    AccessService,
    WebsocketService,
    SessionService,
  ]
})
export class AppModule {

  constructor(
    public appRef: ApplicationRef,
  ) {}

  public hmrOnInit(store: StoreType) {
    if (!store) {
      return;
    }
    console.log('HMR store', JSON.stringify(store, null, 2));
    if ('restoreInputValues' in store) {
      let restoreInputValues = store.restoreInputValues;
      setTimeout(restoreInputValues);
    }

    this.appRef.tick();
    delete store.restoreInputValues;
  }

  public hmrOnDestroy(store: StoreType) {
    const cmpLocation = this.appRef.components.map((cmp) => cmp.location.nativeElement);
    /**
     * Recreate root elements
     */
    store.disposeOldHosts = createNewHosts(cmpLocation);
    /**
     * Save input values
     */
    store.restoreInputValues  = createInputTransfer();
    /**
     * Remove styles
     */
    removeNgStyles();
  }

  public hmrAfterDestroy(store: StoreType) {
    /**
     * Display new elements
     */
    store.disposeOldHosts();
    delete store.disposeOldHosts;
  }

}
