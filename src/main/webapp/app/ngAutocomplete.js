angular.module('chharkoiApp.ngPlaces', []).
directive('ngPlaces', function() {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {
            ngModel: '=',
            options: '=?',
            details: '=?'
          },
        link: function (scope, element, attrs, ngModel){
            //options for autocomplete
            var opts,
                watchEnter = false,
                //convert options provided to opts
                initOpts = function () {
                  opts = {};
                  if (scope.options) {
                    if (scope.options.watchEnter !== true) {
                      watchEnter = false;
                    } else {
                      watchEnter = true;
                    }

                    if (scope.options.types) {
                      opts.types = [];
                      opts.types.push(scope.options.types);
                      scope.gPlace.setTypes(opts.types);
                    } else {
                      scope.gPlace.setTypes([]);
                    }

                    if (scope.options.bounds) {
                      opts.bounds = scope.options.bounds;
                      scope.gPlace.setBounds(opts.bounds);
                    } else {
                      scope.gPlace.setBounds(null);
                    }

                    if (scope.options.country) {
                      opts.componentRestrictions = {
                        country: scope.options.country
                      };
                      scope.gPlace.setComponentRestrictions(opts.componentRestrictions);
                    } else {
                      scope.gPlace.setComponentRestrictions(null);
                    }
                  }
                },
                //function to get retrieve the autocompletes first result using the AutocompleteService
                getPlace = function(result) {
                  var autocompleteService = new google.maps.places.AutocompleteService();
                  if (result.name.length > 0) {
                    autocompleteService.getPlacePredictions(
                      {
                        input: result.name,
                        offset: result.name.length
                      },
                      function listentoresult(list, status) {
                        if(list === null || list.length === 0) {
                          scope.$apply(function() {
                            scope.details = null;
                          });
                        } else {
                          var placesService = new google.maps.places.PlacesService(element[0]);
                          placesService.getDetails(
                            {'reference': list[0].reference},
                            function detailsresult(detailsResult, placesServiceStatus) {
                              if (placesServiceStatus == google.maps.GeocoderStatus.OK) {
                                scope.$apply(function() {
                                  ngModel.$setViewValue(detailsResult.formatted_address);
                                  element.val(detailsResult.formatted_address);

                                  scope.details = detailsResult;

                                  //on focusout the value reverts, need to set it again.
                                  element.on('focusout', function() {
                                    element.val(detailsResult.formatted_address);
                                    element.unbind('focusout');
                                  });
                                });
                              }
                            }
                          );
                        }
                      }
                    );
                  }
                };

            if (scope.gPlace === undefined) {
              scope.gPlace = new google.maps.places.Autocomplete(element[0], {});
            }

            google.maps.event.addListener(scope.gPlace, 'place_changed', function () {
              var result = scope.gPlace.getPlace();
              if (result !== undefined) {
                if (result.address_components !== undefined) {
                  scope.$apply(function () {
                    scope.details = result;
                    ngModel.$setViewValue(element.val());
                  });
                } else {
                  if (watchEnter) {
                    getPlace(result);
                  }
                }
              }

              // Say our scope we have a result
              scope.$emit('chharkoi:place_changed', result);
            });

            //watch options provided to directive
            scope.watchOptions = function () {
              return scope.options;
            };

            scope.$watch(scope.watchOptions, function () {
              initOpts();
            }, true);
        }
    };
})
