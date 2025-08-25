import { Component, HostListener, inject, OnInit } from '@angular/core';
import {
  ActivatedRoute,
  NavigationEnd,
  Router,
  RouterOutlet,
} from '@angular/router';
import { AuthService } from './service/auth.service';
import { Header } from './components/header/header';
import { filter, map, mergeMap } from 'rxjs';
import { Spinner } from './components/spinner/spinner';
import { Footer } from './components/footer/footer';
import { NgClass } from '@angular/common';
import { UnsavedChangesService } from './service/unsaved-changes.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, Spinner, Footer, NgClass],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected readonly authService = inject(AuthService);
  protected readonly unsavedChangesService = inject(UnsavedChangesService);
  protected readonly router = inject(Router);
  protected readonly activatedRoute = inject(ActivatedRoute);

  protected fullWidth = false;
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
        this.fullWidth = !!data['fullWidth'];
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

  @HostListener('window:beforeunload', ['$event'])
  beforeUnloadHandler(event: BeforeUnloadEvent) {
    if (this.unsavedChangesService.hasUnsavedChanges()) {
      event.preventDefault();
    }
  }
}
