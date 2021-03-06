import { Injectable } from '@angular/core';
import { User } from '@app/models/User';
import { HttpClientService } from '@app/services/helpers/http-client.service';
import { api } from '@app/services/navigation/app.endpoints';
import { environment } from '@env';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';


/**
 * Service for performing user related requests
 */
@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(private http: HttpClientService) {
    }

    /**
     * Get user by username
     * @param username - target user username
     */
    getUserByUsername(username: string): Observable<User> {
        return this.http
            .postJsonRequest<User>(environment.restUrl + api.profile, username)
            .pipe(
                map((user) => {
                    // console.log('USER' + JSON.stringify(user));
                    return user;
                })
            );
    }

    /**
     * Get all users from server
     */
    getAllUsers(): Observable<Array<User>> {
        return this.http
            .getJsonRequest<Array<User>>(environment.restUrl + api.users)
            .pipe(map((data) => data.map((user) => User.fromHttp(user))));
    }

    /**
     * Save user information
     * @param user - user update information
     * @param username - target user username
     */
    saveUser(user: User, username: string) {
        return this.http.postJsonRequest<any>(
            environment.restUrl + api.profileUpdate,
            { user, username }
        );
    }

    /**
     * Delete user from server by username
     * @param username - target user username
     */
    deleteUser(username: string) {
        return this.http.postJsonRequest<any>(
            environment.restUrl + api.profileDelete,
            username
        );
    }
}
