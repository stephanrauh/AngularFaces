// mode=badge:
// <div class="col-md-1 col-xs-1 col-sm-1 col-lg-1">
//    <span id="j_idt54:j_idt66_badge" class="badge">34</span>
//    <input id="j_idt54:j_idt66" name="j_idt54:j_idt66" type="hidden" size="2" min="0" max="100" maxlength="3" class="form-control input-sm" value="34">
// </div>
//<div class="col-md-4 col-xs-4 col-sm-4 col-lg-4">
//  <div id="j_idt54:j_idt66_slider" class="ui-slider ui-slider-horizontal ui-widget ui-widget-content ui-corner-all">
//    <div class="ui-slider-range ui-widget-header ui-corner-all ui-slider-range-min" style="width: 34%;"></div>
//    <span class="ui-slider-handle ui-state-default ui-corner-all" tabindex="0" style="left: 34%;"></span>
//  </div>
//</div>

//Mode=Edit:
//<div class="row">
//  <div class="col-md-1 col-xs-1 col-sm-1 col-lg-1">
//    <input id="j_idt54:j_idt62" name="j_idt54:j_idt62" type="text" size="2" min="0" max="100" maxlength="3" class="form-control input-sm" value="87">
//  </div>
//  <div class="col-md-4 col-xs-4 col-sm-4 col-lg-4">
//    <div id="j_idt54:j_idt62_slider" class="ui-slider ui-slider-horizontal ui-widget ui-widget-content ui-corner-all">
//      <div class="ui-slider-range ui-widget-header ui-corner-all ui-slider-range-min" style="width: 87%;"></div>
//      <span class="ui-slider-handle ui-state-default ui-corner-all" tabindex="0" style="left: 87%;"></span>
//    </div>
//  </div>
//</div>



/*
 * copied from https://github.com/angular-ui/ui-slider/blob/master/src/slider.js
 * jQuery UI Slider plugin wrapper
*/
angular.module('ui.slider', []).value('uiSliderConfig',{}).directive('uiSlider', ['uiSliderConfig', '$timeout', function(uiSliderConfig, $timeout) {
    uiSliderConfig = uiSliderConfig || {};
    return {
        require: 'ngModel',
        compile: function () {
            return function (scope, elm, attrs, ngModel) {

                function parseNumber(n, decimals) {
                    return (decimals) ? parseFloat(n) : parseInt(n, 10);
                }

                var options = angular.extend(scope.$eval(attrs.uiSlider) || {}, uiSliderConfig);
                // Object holding range values
                var prevRangeValues = {
                    min: null,
                    max: null
                };

                // convenience properties
                var properties = ['min', 'max', 'step'];
                var useDecimals = (!angular.isUndefined(attrs.useDecimals)) ? true : false;

                var init = function() {
                    // When ngModel is assigned an array of values then range is expected to be true.
                    // Warn user and change range to true else an error occurs when trying to drag handle
                    if (angular.isArray(ngModel.$viewValue) && options.range !== true) {
                        console.warn('Change your range option of ui-slider. When assigning ngModel an array of values then the range option should be set to true.');
                        options.range = true;
                    }

                    // Ensure the convenience properties are passed as options if they're defined
                    // This avoids init ordering issues where the slider's initial state (eg handle
                    // position) is calculated using widget defaults
                    // Note the properties take precedence over any duplicates in options
                    angular.forEach(properties, function(property) {
                        if (angular.isDefined(attrs[property])) {
                            options[property] = parseNumber(attrs[property], useDecimals);
                        }
                    });

                    elm.slider(options);
                    init = angular.noop;
                };

                // Find out if decimals are to be used for slider
                angular.forEach(properties, function(property) {
                    // support {{}} and watch for updates
                    attrs.$observe(property, function(newVal) {
                        if (!!newVal) {
                            init();
                            options[property] = parseNumber(newVal, useDecimals);
                            elm.slider('option', property, parseNumber(newVal, useDecimals));
                            ngModel.$render();
                        }
                    });
                });
                attrs.$observe('disabled', function(newVal) {
                    init();
                    elm.slider('option', 'disabled', !!newVal);
                });

                // Watch ui-slider (byVal) for changes and update
                scope.$watch(attrs.uiSlider, function(newVal) {
                    init();
                    if(newVal !== undefined) {
                      elm.slider('option', newVal);
                    }
                }, true);

                // Late-bind to prevent compiler clobbering
                $timeout(init, 0, true);

                // Update model value from slider
                elm.bind('slide', function(event, ui) {
                    ngModel.$setViewValue(ui.values || ui.value);
                    scope.$apply();
                });

                // Update slider from model value
                ngModel.$render = function() {
                    init();
                    var method = options.range === true ? 'values' : 'value';
                    
                    if (!options.range && isNaN(ngModel.$viewValue) && !(ngModel.$viewValue instanceof Array)) {
                        ngModel.$viewValue = 0;
                    }
                    else if (options.range && !angular.isDefined(ngModel.$viewValue)) {
                            ngModel.$viewValue = [0,0];
                    }

                    // Do some sanity check of range values
                    if (options.range === true) {
                        
                        // Check outer bounds for min and max values
                        if (angular.isDefined(options.min) && options.min > ngModel.$viewValue[0]) {
                            ngModel.$viewValue[0] = options.min;
                        }
                        if (angular.isDefined(options.max) && options.max < ngModel.$viewValue[1]) {
                            ngModel.$viewValue[1] = options.max;
                        }

                        // Check min and max range values
                        if (ngModel.$viewValue[0] > ngModel.$viewValue[1]) {
                            // Min value should be less to equal to max value
                            if (prevRangeValues.min >= ngModel.$viewValue[1]) {
                                ngModel.$viewValue[0] = prevRangeValues.min;
                            }
                            // Max value should be less to equal to min value
                            if (prevRangeValues.max <= ngModel.$viewValue[0]) {
                                ngModel.$viewValue[1] = prevRangeValues.max;
                            }
                        }

                        // Store values for later user
                        prevRangeValues.min = ngModel.$viewValue[0];
                        prevRangeValues.max = ngModel.$viewValue[1];

                    }
                    elm.slider(method, ngModel.$viewValue);
                };

                scope.$watch(attrs.ngModel, function() {
                    if (options.range === true) {
                        ngModel.$render();
                    }
                }, true);

                function destroy() {
                    elm.slider('destroy');
                }
                scope.$on("$destroy", function() {
                    destroy();
                });
            };
        }
    };
}]);