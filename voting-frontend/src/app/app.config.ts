import {
  ApplicationConfig,
  ErrorHandler,
  LOCALE_ID,
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient } from '@angular/common/http';
import { ErrorHandlerService } from './service/error-handler.service';
import { registerLocaleData } from '@angular/common';
import localeDeCh from '@angular/common/locales/de-CH';

registerLocaleData(localeDeCh);

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(),
    {
      provide: ErrorHandler,
      useClass: ErrorHandlerService,
    },
    { provide: LOCALE_ID, useValue: 'de-CH' },
  ],
};
