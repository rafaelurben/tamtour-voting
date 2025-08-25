import { Component } from '@angular/core';
import { Button } from '../../components/button/button';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-rules-page',
  imports: [Button, RouterLink],
  templateUrl: './rules-page.html',
  styleUrl: './rules-page.css',
})
export class RulesPage {}
