'use strict';
app.controller('appCommon', function AppMainController($scope) {
    var commonSelf = this;

    $scope.loadingState = false;
    commonSelf.uploadProgress = true;
});