/**
 * jua - v0.1.0 - 2014-11-03
 * https://github.com/marcorinck/jsf-updates-angular
 * Copyright (c) 2014 Marco Rinck; Licensed MIT
 */
if (typeof(jsf)=='undefined') {
    if (typeof(PrimeFaces)=='undefined')
        alert("JUA requires JSF.");
    else {
        activateJUA(window, angular, null, PrimeFaces, document, jQuery);
    }
}
else {
    activateJUA(window, angular, jsf, null, document, jQuery);
}

/* global jsf: true, angular: true, jQuery: true */
function activateJUA(window, angular, jsf, primefaces, document, $) {
    "use strict";

    var onCompleteCallbacks = [],
        requestOngoing = false;

    function escapeJSFClientId(id) {
        return "#" + id.replace(/:/g, "\\:");
    }

    /**
     * Adds the given callback to be executed after a JSF ajax request is successfully completed.
     *
     * This function is intended to
     * be used as a parameter for the onevent attribut of a f:ajax component. As f:ajax onevent calls the given callback
     * always 3 times for every ajax event this needs to be handled
     * @param {Function} callback
     * @returns {Function}
     */
    function onCompleteEvent(callback) {
        return function (data) {
            if (data.status === 'complete') {
                onCompleteCallbacks.push(callback);
            }
        };
    }

    /**
     * Adds the given callback to be executed after the next (or currently ongoing) JSF ajax request is successfully
     * completed.
     *
     * @param {Function} callback
     * @returns {Function}
     */
    function onComplete(callback) {
        onCompleteCallbacks.push(callback);
    }

    /**
     * Executes the given callback immediately when no JSF AJAX request is currently running or executes after JSF AJAX request
     * is successfully completed.
     *
     * @param callback
     */
    function ensureExecutionAfterAjaxRequest(callback) {
        if (!requestOngoing) {
            callback();
        } else {
            onCompleteCallbacks.push(callback);
        }
    }

    function destroyScopes(data) {
        var controllerElement=angular.element(document.querySelector('[ng-controller]'));
        var theInjector = controllerElement.injector();
        var updates = data.getElementsByTagName('update');

        $.each(updates, function(index, update) {
            var id = escapeJSFClientId(update.id);
            
            if (!(id.indexOf("ViewState")>=0)) {
                $(id).find(".ng-scope, .ng-isolate-scope").each(function(index, scopedChildElement) {
                    if (window.jua.debug) {
                        console.log("destroying child scope for element", scopedChildElement);
                    }

                    angular.element(scopedChildElement.firstChild).scope().$destroy();
                });
            }
        });
        return theInjector;
    }

    function handleAjaxUpdates(data, theInjector) {
        window.setTimeout(function () {
            var $compile = theInjector.get('$compile');
            var updates = data.getElementsByTagName('update');

            $.each(updates, function(index, update) {
                var id = escapeJSFClientId(update.id), element;

                if (!(id.indexOf("ViewState")>=0)) {
                    element = angular.element($(id));

                    if (element) {
                        if (window.jua.debug) {
                            console.log("compiling angular element", element);
                        }

                        var myScope=element.scope();
                        if (typeof(myScope)=='undefined')
                            alert("AngularFaces requests must not update the ng-controller!");
                        else 
                            $compile(element)(myScope);
                    }
                }
            });

            if (onCompleteCallbacks.length) {
                onCompleteCallbacks.forEach(function (onCompleteCallback) {
                    onCompleteCallback();
                });
                onCompleteCallbacks = [];
            }
        });
    }

    if (null != jsf) {
        jsf.ajax.addOnEvent(function (data) {
            if (data.status === 'begin') {
                requestOngoing = true;
                onCompleteCallbacks = [];
            }
            if (data.status === 'complete') {
            }
            if (data.status === 'success') {
                // todo: handleAjaxUpdates() should be called in the 'complete' branch - find a way to pass the injector around
                var theInjector=destroyScopes(data.responseXML);
                handleAjaxUpdates(data.responseXML, theInjector);
                requestOngoing = false;
            }
        });
    }
    else if (null != primefaces) {
        var originalPrimeFacesAjaxUtilsSend = primefaces.ajax.Request.send;
        primefaces.ajax.Request.send = function(cfg) {
            requestOngoing = true;
            onCompleteCallbacks = [];
            var theInjector=null;
            if (!cfg.onsuccess) {
               cfg.onsuccess = function(data, status, xhr) {
                   theInjector=destroyScopes(data);     
               }
            }
            if (!cfg.oncomplete) {
               cfg.oncomplete = function(xhr, status) {
                   handleAjaxUpdates(xhr.responseXML, theInjector);
                   requestOngoing = false;
                   return true;
               }
            }
            originalPrimeFacesAjaxUtilsSend.apply(this, arguments);
        };
    }

    window.jua = {
        onComplete: onComplete,
        onCompleteEvent: onCompleteEvent,
        ensureExecutionAfterAjaxRequest: ensureExecutionAfterAjaxRequest,
        get requestOngoing() {
            return requestOngoing;
        }
    };
}

