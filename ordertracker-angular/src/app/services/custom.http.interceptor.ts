import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {Observable} from 'rxjs';
import {SessionService} from "./session/session.service";


@Injectable()
export class CustomHttpInterceptor implements HttpInterceptor {
  constructor(private sessionService: SessionService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    request = request.clone({
      setHeaders: {
        'Authorization': `${this.sessionService.token}`
      }
    });

    return next.handle(request);
  }

}
