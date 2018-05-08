import { LoginComponent } from './login/login.component';
import { ViewComponentComponent } from './view-component/view-component.component';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuardService } from './auth-guard.service.ts';
import { SignUpComponent } from './sign-up/sign-up.component';


export const routes: Routes = [
  {
    path: '', redirectTo: 'login', pathMatch: 'full'
  },
  {
    path: "login",
    component: LoginComponent,
  },
  {
    path: "tasks",
    component: ViewComponentComponent,
    canActivate: [AuthGuardService],
  },
  {
    path: "sign_up",
    component : SignUpComponent,
  }
];

export const routing = RouterModule.forRoot(routes);
