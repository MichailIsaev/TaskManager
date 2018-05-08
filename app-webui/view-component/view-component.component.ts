import { AuthGuardService } from 'app/auth-guard.service';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { LoginComponent } from 'app/login/login.component';
import { Component, OnInit, ViewChild, Inject, Output, Input } from '@angular/core';
import { MatPaginator, MatTableDataSource } from '@angular/material';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA, MatCheckboxModule } from '@angular/material';
import { MatDatepickerInputEvent } from '@angular/material/datepicker';
import { UUID } from 'angular2-uuid';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { SelectionModel } from '@angular/cdk/collections';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PaginatorComponent } from 'app/paginator/paginator.component'
import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { FormControl } from '@angular/forms';
import { map } from 'rxjs/operators/map';
import { startWith } from 'rxjs/operators/startWith';
import { Observable } from 'rxjs/Rx';
import { CookieService } from 'ngx-cookie-service';
import { MatMenuModule } from '@angular/material/menu';



import * as _moment from 'moment';
import 'rxjs/add/operator/map';
const moment = _moment;

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const MY_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};


@Component({
  selector: 'app-view-component',
  templateUrl: './view-component.component.html',
  styleUrls: ['./view-component.component.css'],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE]
    },
    {
      provide: MAT_DATE_FORMATS,
      useValue: MY_FORMATS
    },

  ]
})
export class ViewComponentComponent implements OnInit {

  ELEMENT_DATA: Element[] = [

  ];

  dialogComponent: Object;
  time: string;
  data: Object;
  from: string;
  to: string;
  displayedColumns = ['name', 'describe', 'targetTime', 'contacts', 'status', 'user', 'delete'];
  dataSource = new MatTableDataSource<Element>(this.ELEMENT_DATA);
  //minDate = new Date();
  startFromDate : FormControl//= new FormControl(new Date());
  startToDate : FormControl// = new FormControl(new Date());
  selection = new SelectionModel<Element>(true, []);
  size: number = 20;
  pages: number = 0;
  page: number = 1;
  name: string = "";
  lastRequest: string;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private http: HttpClient,
    public dialog: MatDialog,
    private snackBar: MatSnackBar,
    private login: LoginComponent,
    private guard: AuthGuardService,
    private cookies: CookieService) {
      var from_date = new Date();
      from_date.setMonth(from_date.getMonth() - 1);
      this.startFromDate = new FormControl(from_date);
      this.from = +from_date;
      var to_date = new Date();
      this.startToDate = new FormControl(to_date);
      this.to = +to_date;
      this.name = "";
      this.onPaginatorChange();
  }

  onFromDateChange(event: MatDatepickerInputEvent<Date>) {
    //this.page = 1;
    this.page = 1;
    var from = +new Date(event.value))
    //var to = from + 86400000;
    this.from = from;
    alert(this.from);
    //this.to = to;
    //this.ELEMENT_DATA = [];
    this.onPaginatorChange();
    /*this.http.get('http://localhost:44444/tasks?from=' + from + '&to=' + to + '&page=' + this.page + '&size=' + this.size,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token')
        })
      }).subscribe(data => {
        var dataset = Object.values(data);
        if (Object.values(dataset[1]).length == 0) {
          this.dataSource.data = [];
          this.dataSource._updateChangeSubscription();
        }
        this.parse(data);
        this.lastRequest = 'Time';
      });*/
  }

  onToDateChange(event: MatDatepickerInputEvent<Date>){
    //this.page = 1;
    this.page = 1;
    var to = +new Date(event.value))
    alert(this.to);
    //var to = from + 86400000;
    //this.from = from;
    this.to = to;
    this.onPaginatorChange();
    /*this.http.get('http://localhost:44444/tasks?from=' + from + '&to=' + to + '&page=' + this.page + '&size=' + this.size,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token')
        })
      }).subscribe(data => {
        var dataset = Object.values(data);
        if (Object.values(dataset[1]).length == 0) {
          this.dataSource.data = [];
          this.dataSource._updateChangeSubscription();
        }
        this.parse(data);
        this.lastRequest = 'Time';
      });*/
  }




  logout() {
    this.login.logout();
  }


  startSearch(name: string): void {
    this.name = name;
    this.ELEMENT_DATA = [];
    this.http.get('http://localhost:44444/tasks?name=' + name + '&from=' + this.from  + '&to=' + this.to + '&page=' + this.page + '&size=' + this.size,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token'),
        })
      }).subscribe(data => {
        this.parse(data);
        //this.lastRequest = 'Name';
        console.log('The dialog was closed');
      });
    //this.dialogComponent = dialogRef.componentInstance;
    //this.name = this.dialogComponent.name;
    /*if (name != "") {
      this.http.get('http://localhost:44444/tasks?name=' + name + '&page=' + this.page + '&size=' + this.size,
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Access-Control-Allow-Headers': '*',
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer' + this.cookies.get('access_token'),
          })
        }).subscribe(data => {
          this.parse(data);
          this.lastRequest = 'Name';
          console.log('The dialog was closed');
        });
    }
    else {
      this.http.get('http://localhost:44444/tasks?page=' + this.page + '&size=' + this.size,
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Access-Control-Allow-Headers': '*',
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer' + this.cookies.get('access_token'),
          })
        }).subscribe(data => {
          this.parse(data);
          this.lastRequest = 'All';
          console.log('The dialog was closed');
        });
    }*/

  }

  onPaginatorChange(): void {
    this.ELEMENT_DATA = [];
    this.http.get('http://localhost:44444/tasks?name=' + name + '&from=' + this.from  + '&to=' + this.to + '&page=' + this.page + '&size=' + this.size,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token'),
        })
      }).subscribe(data => {
        this.parse(data);
        //this.lastRequest = 'Name';
        //console.log('The dialog was closed');
      });

    /*if (this.lastRequest == 'Time') {
      this.ELEMENT_DATA = [];
      this.http.get('http://localhost:44444/tasks?from=' + this.from + '&to=' + this.to + '&page=' + this.page + '&size=' + this.size,
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Access-Control-Allow-Headers': '*',
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer' + this.cookies.get('access_token'),
          })
        }).subscribe(data => {
          this.parse(data);
          this.lastRequest = 'Time';
        });

    }*/
    /*else if (this.lastRequest == 'All') {
      this.ELEMENT_DATA = [];
      this.http.get('http://localhost:44444/tasks?page=' + this.page + '&size=' + this.size,
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Access-Control-Allow-Headers': '*',
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer' + this.cookies.get('access_token'),
          })
        }).subscribe(data => {
          this.parse(data);
          this.lastRequest = 'All';
        }
        );

    }*/
    /*else if (this.lastRequest == 'Name') {
      this.ELEMENT_DATA = [];
      this.http.get('http://localhost:44444/tasks?name=' + this.name + '&page=' + this.page + '&size=' + this.size,
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Access-Control-Allow-Headers': '*',
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer' + this.cookies.get('access_token'),
          })
        }).subscribe(data => {
          this.parse(data);
          this.lastRequest = 'Name';
        });

    }*/
    //else { alert('Something`s happening wrong') }
  }

  parse(data) {
    data = JSON.parse(JSON.stringify(data))
    var data_size = data.size;
    data = Object.values(data.tasks);
    data.forEach(item => {
      this.ELEMENT_DATA.push({
        id: item.id,
        name: item.name,
        describe: item.describe,
        email: item.email,
        notificationInterval: item.notificationInterval,
        targetTime: moment(item.targetTime).format('hh:mm DD.MM.YYYY'),
        contacts: item.contacts,
        status: item.status,
      });
    });
    if (data_size % this.size == 0) {
      this.pages = parseInt(data_size / this.size);
    }
    else {
      this.pages = parseInt(data_size / this.size) + 1;
    }
    this.dataSource = new MatTableDataSource<Element>(this.ELEMENT_DATA);
    this.ngAfterViewInit();
  }

  deleteRow(row) {
    var index = this.dataSource.data.indexOf(row);
    this.dataSource.data.splice(index, 1);
    this.dataSource._updateChangeSubscription();
    this.http.delete('http://localhost:44444/tasks/' + row.id,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token'),
        })
      }).subscribe(data => {

      });
    this.openSnackBar('Id of deleted task : ' + row.id, 'OK');
  }

  deleteAll() {
    this.dataSource.data = [];
    this.dataSource._updateChangeSubscription();
    this.http.get('http://localhost:44444/tasks/*',
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token'),
        })
      }).subscribe(data => {

      });
    this.openSnackBar('All tasks deleted.', 'OK');
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }

  openDialog(): void {
    let dialogRef = this.dialog.open(DialogOverviewExampleDialog, {
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  openAccountDialog(): void {
    let dialogRef = this.dialog.open(AccountInfoDialog, {
    });
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  ngAfterViewInit() {

  }

  ngOnInit() {

  }

}

export interface Element {
  id: UUID;
  name: string;
  describe: string;
  email: string;
  notificationInterval: long[];
  targetTime: Date;
  contacts: string;
  status: string;

}


@Component({
  selector: 'dialog-overview',
  templateUrl: './dialog-overview.html',
  styleUrls: ['./dialog-overview.css']
})
export class DialogOverviewExampleDialog {


  uuuid: UUID = UUID.UUID();
  num: number;
  userControl: FormControl;

  types = [
    { value: 'SENDER', viewValue: 'MailSender' },
    { value: 'OUTPUT', viewValue: 'Output' },
    { value: 'SLEEP', viewValue: 'Sleep' }
  ];


  notificationIntervals = [
    { name: '5 mins', value: 5 * 60 * 1000 },
    { name: '30 mins', value: 30 * 60 * 1000 },
    { name: '1 hour', value: 60 * 60 * 1000 },
    { name: '1.5 hours', value: 90 * 60 * 1000 }
  ];
  constructor(private http: HttpClient, private guard: AuthGuardService,
    public dialogRef: MatDialogRef<DialogOverviewExampleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any, private cookies: CookieService) {
  }
  users: Observable<any[]>;
  user: string;

  ngOnInit() {
    this.userControl = new FormControl();
    this.userControl.valueChanges.subscribe(data => {
      this.http.get('http://localhost:55555/users?name=' + data + '&page=1&size=5',
        {
          headers: new HttpHeaders({
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Access-Control-Allow-Headers': '*',
            'Access-Control-Allow-Origin': '*',
            'Authorization': 'Bearer' + this.cookies.get('access_token')
          })
        }
      ).subscribe(response => {
        this.users = Observable.of<any[]>(response.users);
      });
    });
  }
  constructor(private http: HttpClient,
    public dialogRef: MatDialogRef<DialogOverviewExampleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any, private view: ViewComponentComponent, private cookies: CookieService) {
  }
  postData(name: string, describe: string, date: string, time: string, contacts: string, notificationInterval: long[]) {
    let uuid = UUID.UUID();
    if (this.cookies.get('authority') == 'SUPER_USER') {
      this.mail = this.user;
    }
    else {
      this.mail = this.cookies.get('mail');
    }

    return this.http.post(
      'http://localhost:44444/tasks',
      JSON.stringify({
        id: uuid,
        name: name,
        describe: describe,
        email: this.mail,
        notificationInterval: notificationInterval,
        targetTime: +new Date(date + ' ' + time),
        contacts: contacts.split(','),
        status: 'SCHEDULED'
      })
      , {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token'),
        })
      }).subscribe(data => {
        this.onNoClick();
      });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
@Component({
  selector: 'account-info',
  templateUrl: './account-info.html',
  styleUrls: [],
})
export class AccountInfoDialog {
  constructor(
    public dialogRef: MatDialogRef<DialogOverviewExampleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private guard: AuthGuardService,
    private cookies: CookieService, private http: HttpClient) {
  }

  name: string;
  mail: string;
  password: string;
  describe: string;
  authority: string;
  want_to_change: boolean = false;


  ngOnInit() {
    this.name = this.guard.getUserName();
    this.mail = this.guard.getUserMail();
    this.password = this.guard.getUserPassword();
    this.describe = this.cookies.get("description");
    this.authority = this.cookies.get("authority");
  }

  wantChangePassword() {
    this.want_to_change = true;
  }

  changePassword(password : string) {
    this.http.put('http://localhost:55555/users?email=' + this.mail + '&password=' + password,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Access-Control-Allow-Headers': '*',
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer' + this.cookies.get('access_token')
        })
      }
    ).subscribe(response => {
      this.onNoClick();
      this.guard.checkUser();
      //this.password = password;
    });
  }


  onNoClick(): void {
    this.want_to_change = false;
    this.dialogRef.close();
  }
}
