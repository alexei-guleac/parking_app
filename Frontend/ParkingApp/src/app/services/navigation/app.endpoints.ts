export const api = {
    parking: '/parking',
    statistics: '/statistics',

    login: '/login',
    auth: '/auth',
    social: '/auth/social',
    recaptcha: '/validate_captcha',

    registration: '/registration',
    confirmReg: '/confirm_account',

    confirmReset: '/confirm_reset',
    forgotPass: '/forgot-password',
    resetPass: '/reset-password',

    reservation: '/reserve',
    unreservation: '/unreserve'
};

export const routes = {
    main: '',
    account: 'account',
    registration: 'registration',
    login: 'login',
    logout: 'logout',
    reset: 'account_reset',
    statistics: 'statistics',
    layout: 'layout',
    notFound: '404',
    serverError: '500'
};

export const actions = {
    login: 'login',
    view: 'view',
    serverError: 'serverError',
    reset: 'reset'
};
