import 'clientjs'
import moment from 'moment'

export class Access {
  constructor(
    private browserName: string,
    private browserVersion: string,
    private osName: string,
    private osVersion: string,
    private isMobile: string,
    private timezone: string,
    private timestamp: number,
    private language: string,
    private userAgent: string
  ) {}

  static create(): Access {
    const client = new ClientJS();

    return new Access(
      client.getBrowser(),
      client.getBrowserVersion(),
      client.getOS(),
      client.getOSVersion(),
      client.isMobile() ? 'Y' : 'N' ,
      client.getTimeZone(),
      moment.utc().unix(),
      client.getLanguage(),
      window.navigator.userAgent,
    )
  }
}