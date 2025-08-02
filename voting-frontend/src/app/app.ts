import { Component, inject, OnInit } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterOutlet,
} from '@angular/router';
import { AuthService } from './api/auth.service';
import { Header } from './components/header/header';
import { filter, map, mergeMap } from 'rxjs';
import { Spinner } from './components/spinner/spinner';
import { Footer } from './components/footer/footer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Spinner, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected readonly authService = inject(AuthService);
  protected readonly router = inject(Router);
  protected readonly activatedRoute = inject(ActivatedRoute);

  protected showHeader = false;
  protected showFooter = false;

  constructor() {
    this.router.events
      .pipe(
        filter(event => event instanceof NavigationEnd),
        map(() => {
          let route = this.activatedRoute;
          while (route.firstChild) route = route.firstChild;
          return route;
        }),
        mergeMap(route => route.data)
      )
      .subscribe(data => {
        this.showHeader = !data['hideHeader'];
        this.showFooter = !data['hideFooter'];
      });
  }

  ngOnInit(): void {
    console.log('App initialized, loading user data...');
    this.authService.fetchWhoami().subscribe(data => {
      console.log('User data loaded:', data);
    });
  }
}
