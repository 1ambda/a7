import { Injectable, NgModule, OnInit } from '@angular/core'
import { HttpClient } from '@angular/common/http'

@Injectable()
export class ApiService {

  getApiBaseUrl() {
    return 'http://localhost:8034/api'
  }
}
