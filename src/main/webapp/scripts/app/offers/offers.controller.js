'use strict';

angular.module('jobvacancyApp')
    .controller('OffersController', function ($scope, Offer, ParseLinks) {
        $scope.jobOffers = [];
        $scope.word = " ";
        $scope.focus = false;
        $scope.regex = /\w+(\s\w+)*/g;
        $scope.page = 0;
        $scope.searching = false;
        $scope.myFunct = function(keyEvent) {
  			if (keyEvent.which === 13)
    			$scope.search();
		};
        $scope.loadAll = function() {
            Offer.query({page:$scope.page, size:20, word:$scope.word, isSearch:$scope.searching}, function(result, headers) {
            	$scope.links = ParseLinks.parse(headers('link'));
                $scope.jobOffers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.search = function(page) {
        	$scope.searching = true;
            $scope.page = page;
            $scope.loadAll();
        };
        
        $scope.loadAll();

    });
