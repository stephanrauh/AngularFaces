/**
 * puimessage resembles ng-messages, but translates the errors reported by AngularJS automatically to the user-readable message.
 */
app.directive('puimessage', function() {
 return {
    restrict: 'E',
    scope: {},
    template: function($scope, $element) {
       if ($element['primefaces']=="true") {
          var msg = '<div ng-show="messageVisible()" aria-live="polite" class="ui-message ui-message-error ui-widget ui-corner-all">';
          msg += '<span class="ui-message-error-icon"></span>';
          msg += '<span class="ui-message-error-detail">{{currentMessage}}</span>';
          msg += '</div>';
          return msg;
       }
       return "<div class='pui-message'>{{currentMessage}}</div>";
    },
    controller: function($scope) {
      $scope.currentMessage="";
      $scope.messageVisible = function() {
         if (typeof($scope.currentMessage)=='undefined') 
           return false;
         if ($scope.currentMessage == null)
           return false;
         if ($scope.currentMessage == "")
           return false;
         return true;
      };
   
      this.renderMessages = function(errorMessagesClasses, message, inputField) {
          $scope.currentMessage=getErrorMessage(errorMessagesClasses,inputField);
      };
    },
    
    link: function($scope, $element, $attrs, ctrl) {
        var watchFieldID= $attrs['field'].replace(/:/g, '\\:');
        var target = document.querySelector('#'+watchFieldID);
        if (target==null) {
        	target = document.getElementsByName($attrs['field'])[0];
        }
        var observer = new MutationObserver(function(mutations) {
          mutations.forEach(function(mutation) {
            if (mutation.type=='attributes')
              if (mutation.attributeName=='class') {
                ctrl.renderMessages(target.classList, $element, target);
                $scope.$apply();
              }
                
          });    
        });
        observer.observe(target, { attributes: true, childList: false, characterData: false});
    }
 };
});


/**
 * pui-label watches the $error object of the associated input field.
 * The label is colored red if there's a violated validation rule.
 */
app.directive('puilabel', function() {
	  return {
	    restrict: 'E',
	    scope: {},
	    template: function($scope, $element) {
           var forFieldFragment="";
           if ($element['field']) {
               forFieldFragment = ' for="' + $element['field'] + '"';
           }
           var primeFacesFragment="";
           if ($element['primefaces']=="true") {
               primeFacesFragment=" ui-outputlabel ui-widget";
           }
           var bootsFacesFragment="";
           if ($element['bootsfaces']=="true") {
               bootsFacesFragment=" control-label";
           }
           var label=$element['label'];
           return '<label class="pui-label' + bootsFacesFragment + primeFacesFragment + ' {{errorClass}}"' + forFieldFragment + '>' + label +'</label>';
	    },
	    controller: function($scope) {
	      $scope.currentMessage="";
	      $scope.errorClass="";
	      this.renderMessages = function(errorMessagesClasses, label, inputField) {
	          var innerLabel=$(label).children()[0];
	          if (typeof(innerLabel!='undefined')) {
        	      if (errorMessagesClasses) {
        	          if (hasErrorMessage(errorMessagesClasses) > 0) {
                          $(innerLabel).addClass("ui-state-error");
        	              $scope.errorClass="ui-state-error";
        	          }
        	          else {
        	              $scope.errorClass="";
        	              $(innerLabel).removeClass("ui-state-error");
        	          }
        	      }
        	      else {
        	          $scope.errorClass="";
                      $(innerLabel).removeClass("ui-state-error");
        	      }
	          }
    	  };
	    },
	    
	    link: function($scope, $element, $attrs, ctrl) {
	        var watchFieldID= $attrs['field'].replace(/:/g, '\\:');
	        
	        var target = document.querySelector('#'+watchFieldID);
	        if (target==null) {
	        	target = document.getElementsByName($attrs['field'])[0];
	        }
	        
	        var observer = new MutationObserver(function(mutations) {
	          mutations.forEach(function(mutation) {
	            if (mutation.type=='attributes')
	              if (mutation.attributeName=='class')
	                ctrl.renderMessages(target.classList, $element, target);
	          });    
	        });
	        observer.observe(target, { attributes: true, childList: false, characterData: false});
	    }
	 };
});

/**
 * puimessages displays errors that don't belong to a particular input field.
 */
app.directive('puimessages', function($compile) {
  return {
    restrict: 'E',
    transclude: true,
    scope: {},
    controller: function($scope, $element) {
    				$scope.primefaces="true" == $element.attr('primefaces');
    				$scope.message= function() {
    					if (typeof($scope.$parent.facesmessages)=="undefined" || $scope.$parent.facesmessages==null) {
    						return "";
    					}
    					return $scope.$parent.facesmessages.detail;
    				};
    				$scope.hasMessage= function() {
    					if (typeof($scope.$parent.facesmessages)=="undefined" || $scope.$parent.facesmessages==null) {
    						return false;
    					}
    					return true;
    				};
    				$scope.visibilityClass= function() {
    					if (typeof($scope.$parent.facesmessages)=="undefined" || $scope.$parent.facesmessages==null) {
    						return "hidden";
    					}

    					return "";
    				};

    				$scope.getTemplate = function() {
    					var t='<span class="pui-message ui-state-error-text {{visibilityClass()}}"><ul><li ng-repeat="msg in $parent.facesmessages"><span style="padding-right:10px">{{msg.severity}}</span><span style="padding-right:10px">{{msg.summary}}</span><span>{{msg.detail}}</span></li></ul></span>';
    					
    					
    					
    					if ($scope.primefaces) {
    					  $scope.severity="info";
    					  if (typeof($scope.$parent.facesmessages)!="undefined" || $scope.$parent.facesmessages!=null) {
    					    for (key in $scope.$parent.facesmessages) {
    					      var currentMsg = $scope.$parent.facesmessages[key];
    					      if (currentMsg.severity == "FATAL") $scope.severity="fatal"; break;
                              if (currentMsg.severity == "ERROR") $scope.severity="error";
                              if (currentMsg.severity == "WARNING" && $scope.severity=="info") $scope.severity="warning";
    					    }
    					  }
    					  t='<div ng-show="hasMessage()" class="pui-message ui-messages-{{severity}} ui-corner-all"><div class="ui-messages ui-widget" aria-live="polite"><ul><li ng-repeat="msg in $parent.facesmessages"><span class="ui-messages-{{severity}}-icon"></span><span class="ui-messages-{{severity}}-summary">{{msg.summary}}</span><span class="ui-messages-{{severity}}-detail">{{msg.detail}}</span></li></ul></div></div>';
    					}
    					return t; 
    				};
    			},
	link: function(scope, element, attrs) {
        var el = $compile(scope.getTemplate())(scope);
        element.replaceWith(el);
    },
    replace: true
	};
});




/**
 * This directive makes sure an integer value is entered into an input field. This constraint is stricter than the type="number" check.
 */
var INTEGER_REGEXP = /^\-?\d*$/;
app.directive('integer', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (INTEGER_REGEXP.test(viewValue)) {
					// it is valid
					ctrl.$setValidity('integer', true);
					return viewValue;
				} else {
					// it is invalid, return undefined (no model update)
					ctrl.$setValidity('integer', false);
					return undefined;
				}
			});
		}
	};
});



/** 
 * This function takes an AngularJS $error object and counts the attributes. If there's an attribute,
 * there's violation of an AngularJS validation rule.
 */
function hasErrorMessage(errors) {
    var key;
    for (key in errors) {
        if ('ng-invalid'==errors[key]) return true;
    }
    return false;
}

/**
 * Finds the AngularJS $error object of an input field given by its ID.
 * Returns the name of the $error object. 
 */
function findErrorObject(watchFieldID) {
    var errorObjectToBeWatchedName=null;
    var watchField=document.getElementById(watchFieldID);
    if (watchField==null) {
        watchField = document.getElementsByName(watchFieldID)[0];
    }
    while (watchField) {
        var name =watchField.getAttribute("name");
        if (null != name && typeof(name) != 'undefined') {
            if (null == errorObjectToBeWatchedName)
                errorObjectToBeWatchedName = name;
            else
                errorObjectToBeWatched = name + "." + errorObjectToBeWatchedName;
        }
        watchField=watchField.parentElement;
    }
    var errorObjectToBeWatched = errorObjectToBeWatched+".$error";
    return errorObjectToBeWatched;
}

/**
 * Analyzes the AngularJS $error object and returns the appropriate error message.
 * Along the way it's translated to a foreign language (if needed).
 * It's possible to add variables in the error message (such as the minimum or maximum
 * value).
 */
function getErrorMessage(errors, inputField) {
    if (inputField.getAttribute("type")=="hidden")
        return "";
    if ((typeof(errors)=='undefined') || (!errors))
        return "";

    if (errors.contains('ng-invalid-min')) {
        var min = inputField.getAttribute("min");
        var msg = translateErrorMessage("This number must be at least {}.");
        msg=msg.replace("{}", min);
        return msg;
    }
    if (errors.contains('ng-invalid-max')) {
        var max = inputField.getAttribute("max");
        var msg = translateErrorMessage("This number must be less or equal {}.");
        msg=msg.replace("{}", max);
        return msg;
    }
    if (errors.contains('ng-invalid-integer')) {
      var msg = translateErrorMessage("Please enter a valid integer number.");
      return msg;
    }
    if (errors.contains('ng-invalid-number')) {
      var msg = translateErrorMessage("Please enter a valid number.");
      return msg;
    }
    if (errors.contains('ng-invalid-required')) {
       var msg = translateErrorMessage("Please fill out this field.");
       return msg;
    }
    
    if (errors.contains('ng-invalid-integer')) {
        var msg = translateErrorMessage("Please enter a valid integer number.");
        return msg;
    }
    if (errors.contains('ng-invalid-minlength')) {
        var min = inputField.getAttribute("ng-minlength");
        var msg = translateErrorMessage("At least {} characters required.");
        msg=msg.replace("{}", min);
        return msg;
    }
    if (errors.contains('ng-invalid-maxlength')) {
        var max = inputField.getAttribute("ng-maxlength");
        if (typeof(max)=='undefined' || max==null) {
            max=inputField.getAttribute("maxlength");
        }
        if (typeof(max)=='undefined' || max==null) {
           max="?";
           console.log("AngularJS reports a maxlength error, but ng-maxlength is undefined");
        }
        var msg = translateErrorMessage("{} characters accepted at most.");
        msg=msg.replace("{}", max);
        return msg;
    }
    
    if (hasErrorMessage()) {
        var keys="";
        var key;
        for (key in errors) {
            if (errors.hasOwnProperty(key)) {
                var msg = angularFacesMessages[key];
                if (msg) 
                    return msg;
                if (keys=="")
                    keys += "key";
                else
                    keys += ", " + key;
            }
        }

        var msg = angularFacesMessages["A validation rule is violated: {}"];
        msg=msg.replace("{}", keys);
        return msg;
    }
    if (inputField.getAttribute('servermessage')) {
        return inputField.getAttribute('servermessage');
    }
    return "";
}

/**
 * Reads the internationalized error messages from the messages bundle (if it's available).
 */
function translateErrorMessage(defaultText) {
   if (angularFacesMessages) {
       var msg = angularFacesMessages[defaultText];
       if (msg)
           return msg;
       else
           return defaultText;
   }
   else 
       return defaultText;
}
