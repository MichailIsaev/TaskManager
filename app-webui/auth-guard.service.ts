import { Injectable } from '@angular/core';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { Router } from "@angular/router";
import { LoginComponent } from './login/login.component.ts';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { CookieService } from 'ngx-cookie-service';
import { HttpClient, HttpHeaders } from '@angular/common/http';



@Injectable()
export class AuthGuardService implements CanActivate {


  constructor(private router: Router,
              private http: HttpClient,
    private cookies: CookieService) { }

    private mail : string;
    private password : string;
    private name : string;


    getUserName(){
      return this.name;
    }

    getUserPassword(){
      return this.password;
    }

    getUserMail(){
      return this.mail;
    }

  checkUser() {
    this.http.get('http://localhost:55555/users?email=' + this.cookies.get("mail"),
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get("access_token")
        })
      }).subscribe(data => {
        this.name = data.users[0].name;
        this.password = data.users[0].password;
        this.mail = data.users[0].email;
        this.cookies.set('description' , data.users[0].description);
        this.cookies.set('authority', data.users[0].authorities);
      });

  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    this.checkUser();
    if (next.url[0].path == '/login') {
      if (this.cookies.get("authenticated") == null) {
        return false;
      }
      else
        return true;
    }

    if (this.cookies.get("authenticated")) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
