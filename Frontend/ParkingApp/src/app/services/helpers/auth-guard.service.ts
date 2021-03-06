import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from '../account/auth.service';
import { actions, api } from '../navigation/app.endpoints';


/**
 * Guard service for authentication routes
 */
@Injectable({ providedIn: 'root' })
export class AuthGuardService implements CanActivate {

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) {
    }

    /**
     * Check if user have access to this page
     * @param route - target requested route
     * @param state - router state
     */
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const currentUser = this.authenticationService.isUserLoggedIn();
        if (currentUser) {
            // logged in so return true
            return true;
        }

        // not logged in so redirect to login page with the return url
        this.router.navigate([api.login], {
            queryParams: { action: actions.login }
        });
        return false;
    }
}
