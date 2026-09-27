import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { RouterLink } from '@angular/router';

@Component({
  imports: [MatIconModule, MatListModule, MatSidenavModule, RouterLink],
  selector: 'app-side-navbar',
  styleUrl: './side-navbar.scss',
  templateUrl: './side-navbar.html',
})
export class SideNavbar {}
