function getContext() { return '/heystk'; }

angular.
    module(
        "heystk",
        [
            "ui.bootstrap",
            "ui.bootstrap.dropdown",
            "ui.bootstrap.modal",
            "ui.bootstrap.tabs",
            "ui.bootstrap.datepicker",
            "ui.bootstrap.pagination"
        ]
    ).
    controller(
        "NavigationController",
        [
            "$scope",
            function($scope) {}
        ]
    );