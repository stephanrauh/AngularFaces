/**
 * This is a slightly modified version of Marco Rinck's jua module.
 * 
 * jua - v0.1.0 - 2014-12-22
 * https://github.com/marcorinck/jsf-updates-angular
 * Copyright (c) 2014 Marco Rinck; Licensed MIT
 */

var app = angular.module('jua', ['ng']);

/**
 * Adds a couple of general-purpose functions to the root scope.
 */
app.run(function($rootScope, $compile) {
     (function (window, $compile, $rootScope) {
         "use strict";
     
         var onCompleteCallbacks = [],
             requestOngoing = false,
             document = window.document,
             jsf = window.jsf,
             $ = window.jQuery;
             window.$juaCompile=$compile;
             window.$juaRootScope=$rootScope;
     
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
             var updates = data.responseXML.getElementsByTagName('update');
     
             $.each(updates, function(index, update) {
                 var id = escapeJSFClientId(update.id);
     
                 if (id.indexOf("ViewState") == -1) {
                 
                     var $compile = window.$juaCompile;
                 
                 
                     $(id).find(".ng-scope, .ng-isolate-scope").each(function(index, scopedChildElement) {
                         if (window.jua.debug) {
                             console.log("destroying child scope for element", scopedChildElement);
                         }
     
                         angular.element(scopedChildElement.firstChild).scope().$destroy();
                     });
                 }
             });
         }
     
         function handleAjaxUpdates(data) {
             window.setTimeout(function () {
                 var updates = data.responseXML.getElementsByTagName('update');
     
                 $.each(updates, function(index, update) {
                     var id = escapeJSFClientId(update.id), element;
     
                     if (id.indexOf("ViewState") == -1) {
                         element = angular.element($(id));
                         var $compile = window.$juaCompile;
                         if (element) {
                             if (window.jua.debug) {
                                 console.log("compiling angular element", element);
                             }
     
                             if (element.scope()) {
                                 $compile(element)(element.scope());
                             } else {
                                 $compile(element)(window.$juaRootScope);
                             }
                             
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
     
         // Register JUA with Mojarra and MyFaces
         if (jsf) {
             jsf.ajax.addOnEvent(function (data) {
                 if (data.status === 'begin') {
                     requestOngoing = true;
                     onCompleteCallbacks = [];
                 }
                 if (data.status === 'complete') {
                     destroyScopes(data);
                 }
                 if (data.status === 'success') {
                     handleAjaxUpdates(data);
                     requestOngoing = false;
                 }
             });
         }
         // Register JUA with PrimeFaces
         else if (typeof(PrimeFaces)!='undefined') {
             $(document).on('pfAjaxStart', function() {
                 requestOngoing = true;
                 onCompleteCallbacks = [];
             });
             
             $(document).on('pfAjaxComplete', function(event, xhr) {
                 if (xhr && xhr.responseXML) {
                     destroyScopes(xhr);
                 }
             });
             
             $(document).on('pfAjaxSuccess', function(event, xhr) {
                 if (xhr && xhr.responseXML) {
                     handleAjaxUpdates(xhr);
                 }
             
                 requestOngoing = false;
             });
         }
         //This should handle ajax requests of non-standard jsf libraries too when they are using jquery internally
         else if ($) {
     
             $(document).ajaxStart(function() {
                 requestOngoing = true;
                 onCompleteCallbacks = [];
             });
     
             $(document).ajaxComplete(function(event, xhr) {
                 if (xhr && xhr.responseXML) {
                     destroyScopes(xhr);
                 }
             });
     
             $(document).ajaxSuccess(function(event, xhr) {
                 if (xhr && xhr.responseXML) {
                     handleAjaxUpdates(xhr);
                 }
     
                 requestOngoing = false;
             });
         }
     
         if (!$ && !jsf && console) {
             console.warn('jsf-updates-angular: no jquery and no jsf object found, so doing nothing after ajax requests. This is probably not what you want!');
         } else {
             window.jua = {
                 debug: true,
                 onComplete: onComplete,
                 onCompleteEvent: onCompleteEvent,
                 ensureExecutionAfterAjaxRequest: ensureExecutionAfterAjaxRequest,
                 get requestOngoing() {
                     return requestOngoing;
                 }
             };
         }
     })(window, $compile, $rootScope);
});