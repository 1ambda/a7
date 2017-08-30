import { Pagination } from  './pagination'

export class PaginatedResponse {
  constructor(public content: Array<any>,
              public meta: Pagination) {}
}