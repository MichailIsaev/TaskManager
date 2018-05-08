import {FormControl} from '@angular/forms';
import {AppComponent} from './app.component';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {SelectionModel} from '@angular/cdk/collections';
import {OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {
  MatFormFieldModule,
  MatAutocompleteModule,
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatExpansionModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatRippleModule,
  MatSelectModule,
  MatSidenavModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
  MatStepperModule,
} from '@angular/material';

import {HttpModule} from '@angular/http';
import {CdkTableModule} from '@angular/cdk/table';
import 'hammerjs';
import { Routes, RouterModule, Router, RouterLink, RouterLinkActive } from "@angular/router"
import { routes } from "./app.routes";
import { HttpClientModule }   from '@angular/common/http';
import { ViewComponentComponent , DialogOverviewExampleDialog , AccountInfoDialog} from './view-component/view-component.component';
import { PaginatorComponent } from './paginator/paginator.component';
import { LoginComponent} from './login/login.component';
import {AuthGuardService} from './auth-guard.service.ts';
import { provide } from '@angular/core';
import { BrowserXhr } from '@angular/http';
import {CustExtBrowserXhr} from './cust-ext-browser-xhr';
import { SignUpComponent } from './sign-up/sign-up.component';
import { CookieService } from 'ngx-cookie-service';


@NgModule({
  declarations: [
    AppComponent,
    ViewComponentComponent,
    DialogOverviewExampleDialog,
    PaginatorComponent,
    AccountInfoDialog,
    LoginComponent,
    SignUpComponent,
  ],
  imports: [
    MatSelectModule,
    MatRadioModule,
    MatCardModule,
    HttpModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpModule ,
    MatButtonModule,
    MatButtonToggleModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatInputModule,
    ReactiveFormsModule,
    MatDialogModule,
    HttpClientModule,
    MatPaginatorModule,
    MatTableModule,
    MatToolbarModule,
    MatIconModule,
    MatCheckboxModule,
    MatSnackBarModule,
    MatMenuModule,
    OwlDateTimeModule,
    OwlNativeDateTimeModule,
    MatAutocompleteModule,
    RouterModule.forRoot(routes)
  ],
  entryComponents: [ViewComponentComponent ,  DialogOverviewExampleDialog , AccountInfoDialog],
  providers: [AuthGuardService , LoginComponent , CookieService],
  bootstrap: [AppComponent ],
})
export class AppModule { }

platformBrowserDynamic().bootstrapModule(AppModule);
