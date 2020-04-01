import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/account/auth.service';
import {storageKeys} from '../../services/account/session-storage.service';
import {NavigationService} from '../../services/navigation/navigation.service';
import {capitalize} from '../../utils/string-utils';
import {openClose} from '../animations/animations';


@Component({
    selector: 'app-menu',
    animations: [openClose],
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
    // for refreshing data
    @Output()
    goBackEvent = new EventEmitter();

    private modeToggleText: string;
    private modeStatusElement: string;

    private themeToogleChecked = false;

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService,
        private navigation: NavigationService
    ) {
    }

    ngOnInit() {
        this.initThemeToogle();
    }

    private navigateToMain() {
        this.navigation.navigateToMain();
    }

    private navigateToStatistics() {
        this.navigation.navigateToStatistics();
    }

    private navigateToLayout() {
        this.navigation.navigateToLayout();
    }

    private navigateToLogin() {
        this.navigation.navigateToLogin();
    }

    private navigateTo404() {
        this.navigation.navigateTo404();
    }

    private isAdminLoggedIn() {
        return this.authenticationService.isAdminLoggedIn();
    }

    private isUserLoggedIn() {
        // console.log('isUserLoggedIn ' + this.authenticationService.isUserLoggedIn());
        return this.authenticationService.isUserLoggedIn();
    }

    private logout() {
        this.authenticationService.processLogout();

        console.log(sessionStorage.getItem(storageKeys.TOKEN_NAME));
        this.router.navigate(['']);

        // for refreshing data
        this.goBackEvent.emit();
    }

    private getUserName() {
        return this.authenticationService.getLoggedInUserName();
    }

    private capitalize(field: string) {
        return capitalize(field);
    }

    private initThemeToogle() {
        document.documentElement.classList.remove('no-js');

        const STORAGE_KEY = 'user-color-scheme';
        const COLOR_MODE_KEY = '--color-mode';

        // const modeToggleButton = document.querySelector('.js-mode-toggle');
        const modeToggleButton = document.querySelector('.slider');
        // console.log('mdtb ' + modeToggleButton);

        /**
         * Pass in an element and its CSS Custom Property that you want the value of.
         * Optionally, you can determine what datatype you get back.
         *
         // tslint:disable-next-line:no-redundant-jsdoc
         * @param {String} propKey
         * @param {HTMLElement} element=document.documentElement
         * @param {String} castAs='string'
         * @returns {*}
         */
        const getCSSCustomProp = (
            propKey,
            element = document.documentElement,
            castAs = 'string'
        ) => {
            let response = getComputedStyle(element).getPropertyValue(propKey);

            // Tidy up the string if there's something to work with
            if (response.length) {
                response = response.replace(/\'|"/g, '').trim();
            }

            // Convert the response into a whatever type we wanted
            switch (castAs) {
                case 'number':
                case 'int':
                    return parseInt(response, 10);
                case 'float':
                    return parseFloat(response);
                case 'boolean':
                case 'bool':
                    return response === 'true' || response === '1';
            }

            // Return the string response by default
            return response;
        };

        /**
         * Takes either a passed settings ('light'|'dark') or grabs that from localStorage.
         * If it can’t find the setting in either, it tries to load the CSS color mode,
         * controlled by the media query
         */
        const applySetting = passedSetting => {
            // console.log('test const applySetting');
            const currentSetting =
                passedSetting || localStorage.getItem(STORAGE_KEY);

            if (currentSetting) {
                document.documentElement.setAttribute(
                    'data-user-color-scheme',
                    currentSetting
                );
                setButtonLabelAndStatus(currentSetting);
            } else {
                setButtonLabelAndStatus(getCSSCustomProp(COLOR_MODE_KEY));
            }
        };

        const initSettings = () => {
            // console.log('test const initSetting');
            const currentSetting = localStorage.getItem(STORAGE_KEY);

            if (currentSetting) {
                this.themeToogleChecked = currentSetting === 'dark';
            }
        };

        /**
         * Get’s the current setting > reverses it > stores it
         */
        const toggleSetting = () => {
            console.log('toggleSetting');
            let currentSetting = localStorage.getItem(STORAGE_KEY);

            switch (currentSetting) {
                case null:
                    currentSetting =
                        getCSSCustomProp(COLOR_MODE_KEY) === 'dark'
                            ? 'light'
                            : 'dark';
                    break;
                case 'light':
                    currentSetting = 'dark';
                    break;
                case 'dark':
                    currentSetting = 'light';
                    break;
            }

            localStorage.setItem(STORAGE_KEY, currentSetting);

            return currentSetting;
        };

        /**
         * A shared method for setting the button text label and visually hidden status element
         */
        const setButtonLabelAndStatus = currentSetting => {
            this.modeToggleText = `Enable ${
                currentSetting === 'dark' ? 'light' : 'dark'
            } mode`;
            this.modeStatusElement = `Color mode is now "${currentSetting}"`;
        };

        /**
         * Clicking the button runs the apply setting method which grabs its parameter
         * from the toggle setting method.
         */
        modeToggleButton.addEventListener('click', evt => {
            // evt.preventDefault();
            applySetting(toggleSetting());
        });

        applySetting(null);
        initSettings();
    }
}
