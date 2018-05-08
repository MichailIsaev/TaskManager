import { Component, OnInit, ViewChild, Inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Router } from "@angular/router";
import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { AuthGuardService } from 'app/auth-guard.service';
import { MatSnackBar } from '@angular/material'
import { Observable } from 'rxjs/Observable';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  constructor(private http: HttpClient ,
     private router: Router) { }

  signUp(name : string , mail : string , password : string , description : string){
    return this.http.post(
      'http://localhost:55555/users',
      JSON.stringify({
        email: mail,
        authorities : [
          "OTHER"
        ] ,
        accountNonExpired : true ,
      	accountNonLocked : true ,
      	credentialsNonExpired : true ,
      	enabled : true ,
        name: name,
        description: description,
        password : password
      })
      , {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': 'Content-Type',
          'Access-Control-Allow-Origin': '*'
        }),
        observe: 'response'
      }).subscribe(data => {
        this.router.navigate(['/login']);
      });
  }

}
