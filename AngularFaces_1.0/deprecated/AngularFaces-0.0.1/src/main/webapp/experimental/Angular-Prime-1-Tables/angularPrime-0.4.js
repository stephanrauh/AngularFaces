/*globals angular */

(function () {
    "use strict";

    angular.module('angular.prime.config', []).value('angular.prime.config', {
            labelPrefix: 'lbl'
        });

    angular.module('angular.prime', ['angular.prime.config']).run(['$rootScope', function ($rootScope) {

        $rootScope.safeApply = function (fn) {
            var phase = this.$root.$$phase;
            if (phase == '$apply' || phase == '$digest') {
                if (fn && (typeof(fn) === 'function')) {
                    fn();
                }
            } else {
                this.$apply(fn);
            }
        };

    }]);

    angular.module('angular.prime').value('version', "v0.4");

}());
;"use strict";
/*globals $ window document*/

/**
 * PUI Object
 */
var PUI = {  // Changed for AngularPrime

    zindex : 1000,

    /**
     *  Aligns container scrollbar to keep item in container viewport, algorithm copied from jquery-ui menu widget
     */
    scrollInView: function(container, item) {
        var borderTop = parseFloat(container.css('borderTopWidth')) || 0,
            paddingTop = parseFloat(container.css('paddingTop')) || 0,
            offset = item.offset().top - container.offset().top - borderTop - paddingTop,
            scroll = container.scrollTop(),
            elementHeight = container.height(),
            itemHeight = item.outerHeight(true);

        if(offset < 0) {
            container.scrollTop(scroll + offset);
        }
        else if((offset + itemHeight) > elementHeight) {
            container.scrollTop(scroll + offset - elementHeight + itemHeight);
        }
    },

    isIE: function(version) {
        return ($.browser.msie && parseInt($.browser.version, 10) === version);
    },

    escapeRegExp: function(text) {
        return text.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1");
    },

    escapeHTML: function(value) {
        return value.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
    },

    clearSelection: function() {
        if(window.getSelection) {
            if(window.getSelection().empty) {
                window.getSelection().empty();
            } else if(window.getSelection().removeAllRanges) {
                window.getSelection().removeAllRanges();
            }
        } else if(document.selection && document.selection.empty) {
            document.selection.empty();
        }
    },

    inArray: function(arr, item) {
        for(var i = 0; i < arr.length; i++) {
            if(arr[i] === item) {
                return true;
            }
        }

        return false;
    }
};;/**
 * jQuery plugin for getting position of cursor in textarea
 *
 * @author Bevis Zhao (i@bevis.me, http://bevis.me)
 */
$(function() {

	var calculator = {
		// key styles
		primaryStyles: ['fontFamily', 'fontSize', 'fontWeight', 'fontVariant', 'fontStyle',
			'paddingLeft', 'paddingTop', 'paddingBottom', 'paddingRight',
			'marginLeft', 'marginTop', 'marginBottom', 'marginRight',
			'borderLeftColor', 'borderTopColor', 'borderBottomColor', 'borderRightColor',  
			'borderLeftStyle', 'borderTopStyle', 'borderBottomStyle', 'borderRightStyle', 
			'borderLeftWidth', 'borderTopWidth', 'borderBottomWidth', 'borderRightWidth',
			'line-height', 'outline'],

		specificStyle: {
			'word-wrap': 'break-word',
			'overflow-x': 'hidden',
			'overflow-y': 'auto'
		},
		
		simulator : $('<div id="textarea_simulator"/>').css({
				position: 'absolute',
				top: 0,
				left: 0,
				visibility: 'hidden'
			}).appendTo(document.body),

		toHtml : function(text) {
			return text.replace(/\n/g, '<br>')
				.split(' ').join('<span style="white-space:prev-wrap">&nbsp;</span>');
		},
		// calculate position 
		getCaretPosition: function() {
			var cal = calculator, self = this, element = self[0], elementOffset = self.offset();

			// IE has easy way to get caret offset position
			if ($.browser.msie) {
				// must get focus first
				element.focus();
			    var range = document.selection.createRange();  
			    $('#hskeywords').val(element.scrollTop);
			    return {  
			        left: range.boundingLeft - elementOffset.left,
			        top: parseInt(range.boundingTop) - elementOffset.top + element.scrollTop 
						+ document.documentElement.scrollTop + parseInt(self.getComputedStyle("fontSize")) 
			    };  
			}  
			cal.simulator.empty();
			// clone primary styles to imitate textarea
			$.each(cal.primaryStyles, function(index, styleName) {
				self.cloneStyle(cal.simulator, styleName);
			});

			// caculate width and height
			cal.simulator.css($.extend({
				'width': self.width(),
				'height': self.height()
			}, cal.specificStyle));

			var value = self.val(), cursorPosition = self.getCursorPosition();
			var beforeText = value.substring(0, cursorPosition),
				afterText = value.substring(cursorPosition);

			var before = $('<span class="before"/>').html(cal.toHtml(beforeText)),
				focus = $('<span class="focus"/>'),
				after = $('<span class="after"/>').html(cal.toHtml(afterText));

			cal.simulator.append(before).append(focus).append(after);
			var focusOffset = focus.offset(), simulatorOffset = cal.simulator.offset();
			// alert(focusOffset.left  + ',' +  simulatorOffset.left + ',' + element.scrollLeft);
			return { 
				top: focusOffset.top - simulatorOffset.top - element.scrollTop 
					// calculate and add the font height except Firefox
					+ ($.browser.mozilla ? 0 : parseInt(self.getComputedStyle("fontSize"))), 
				left: focus[0].offsetLeft -  cal.simulator[0].offsetLeft - element.scrollLeft
			};
		}
	};

	$.fn.extend({
		getComputedStyle: function(styleName) {
			if (this.length == 0) return;
			var thiz = this[0];
			var result = this.css(styleName);
			result = result || ($.browser.msie ?
				thiz.currentStyle[styleName]:
				document.defaultView.getComputedStyle(thiz, null)[styleName]);
			return result;			
		},
		// easy clone method 
		cloneStyle: function(target, styleName) {
			var styleVal = this.getComputedStyle(styleName);
			if (!!styleVal) {
				$(target).css(styleName, styleVal);
			}
		},
		cloneAllStyle: function(target, style) {
			var thiz = this[0];
			for (var styleName in thiz.style) {
				var val = thiz.style[styleName];
				typeof val == 'string' || typeof val == 'number' 
					? this.cloneStyle(target, styleName)
					: NaN;
			}
		},
		getCursorPosition : function() {
	        var thiz = this[0], result = 0;
	        if ('selectionStart' in thiz) {
	            result = thiz.selectionStart;
	        } else if('selection' in document) {
	        	var range = document.selection.createRange();
	        	if (parseInt($.browser.version) > 6) {
		            thiz.focus();
		            var length = document.selection.createRange().text.length;
		            range.moveStart('character', - thiz.value.length);
		            result = range.text.length - length;
	        	} else {
	                var bodyRange = document.body.createTextRange();
	                bodyRange.moveToElementText(thiz);
	                for (; bodyRange.compareEndPoints("StartToStart", range) < 0; result++)
	                	bodyRange.moveStart('character', 1);
	                for (var i = 0; i <= result; i ++){
	                    if (thiz.value.charAt(i) == '\n')
	                        result++;
	                }
	                var enterCount = thiz.value.split('\n').length - 1;
					result -= enterCount;
                    return result;
	        	}
	        }
	        return result;
	    },
		getCaretPosition: calculator.getCaretPosition
	});
});;/**
 * @license Rangy Text Inputs, a cross-browser textarea and text input library plug-in for jQuery.
 *
 * Part of Rangy, a cross-browser JavaScript range and selection library
 * http://code.google.com/p/rangy/
 *
 * Depends on jQuery 1.0 or later.
 *
 * Copyright 2010, Tim Down
 * Licensed under the MIT license.
 * Version: 0.1.205
 * Build date: 5 November 2010
 */
(function($) {
    var UNDEF = "undefined";
    var getSelection, setSelection, deleteSelectedText, deleteText, insertText;
    var replaceSelectedText, surroundSelectedText, extractSelectedText, collapseSelection;

    // Trio of isHost* functions taken from Peter Michaux's article:
    // http://peter.michaux.ca/articles/feature-detection-state-of-the-art-browser-scripting
    function isHostMethod(object, property) {
        var t = typeof object[property];
        return t === "function" || (!!(t == "object" && object[property])) || t == "unknown";
    }

    function isHostProperty(object, property) {
        return typeof(object[property]) != UNDEF;
    }

    function isHostObject(object, property) {
        return !!(typeof(object[property]) == "object" && object[property]);
    }

    function fail(reason) {
        if (window.console && window.console.log) {
            window.console.log("TextInputs module for Rangy not supported in your browser. Reason: " + reason);
        }
    }

    function adjustOffsets(el, start, end) {
        if (start < 0) {
            start += el.value.length;
        }
        if (typeof end == UNDEF) {
            end = start;
        }
        if (end < 0) {
            end += el.value.length;
        }
        return { start: start, end: end };
    }

    function makeSelection(el, start, end) {
        return {
            start: start,
            end: end,
            length: end - start,
            text: el.value.slice(start, end)
        };
    }

    function getBody() {
        return isHostObject(document, "body") ? document.body : document.getElementsByTagName("body")[0];
    }

    $(document).ready(function() {
        var testTextArea = document.createElement("textarea");

        getBody().appendChild(testTextArea);

        if (isHostProperty(testTextArea, "selectionStart") && isHostProperty(testTextArea, "selectionEnd")) {
            getSelection = function(el) {
                var start = el.selectionStart, end = el.selectionEnd;
                return makeSelection(el, start, end);
            };

            setSelection = function(el, startOffset, endOffset) {
                var offsets = adjustOffsets(el, startOffset, endOffset);
                el.selectionStart = offsets.start;
                el.selectionEnd = offsets.end;
            };

            collapseSelection = function(el, toStart) {
                if (toStart) {
                    el.selectionEnd = el.selectionStart;
                } else {
                    el.selectionStart = el.selectionEnd;
                }
            };
        } else if (isHostMethod(testTextArea, "createTextRange") && isHostObject(document, "selection") &&
                   isHostMethod(document.selection, "createRange")) {

            getSelection = function(el) {
                var start = 0, end = 0, normalizedValue, textInputRange, len, endRange;
                var range = document.selection.createRange();

                if (range && range.parentElement() == el) {
                    len = el.value.length;

                    normalizedValue = el.value.replace(/\r\n/g, "\n");
                    textInputRange = el.createTextRange();
                    textInputRange.moveToBookmark(range.getBookmark());
                    endRange = el.createTextRange();
                    endRange.collapse(false);
                    if (textInputRange.compareEndPoints("StartToEnd", endRange) > -1) {
                        start = end = len;
                    } else {
                        start = -textInputRange.moveStart("character", -len);
                        start += normalizedValue.slice(0, start).split("\n").length - 1;
                        if (textInputRange.compareEndPoints("EndToEnd", endRange) > -1) {
                            end = len;
                        } else {
                            end = -textInputRange.moveEnd("character", -len);
                            end += normalizedValue.slice(0, end).split("\n").length - 1;
                        }
                    }
                }

                return makeSelection(el, start, end);
            };

            // Moving across a line break only counts as moving one character in a TextRange, whereas a line break in
            // the textarea value is two characters. This function corrects for that by converting a text offset into a
            // range character offset by subtracting one character for every line break in the textarea prior to the
            // offset
            var offsetToRangeCharacterMove = function(el, offset) {
                return offset - (el.value.slice(0, offset).split("\r\n").length - 1);
            };

            setSelection = function(el, startOffset, endOffset) {
                var offsets = adjustOffsets(el, startOffset, endOffset);
                var range = el.createTextRange();
                var startCharMove = offsetToRangeCharacterMove(el, offsets.start);
                range.collapse(true);
                if (offsets.start == offsets.end) {
                    range.move("character", startCharMove);
                } else {
                    range.moveEnd("character", offsetToRangeCharacterMove(el, offsets.end));
                    range.moveStart("character", startCharMove);
                }
                range.select();
            };

            collapseSelection = function(el, toStart) {
                var range = document.selection.createRange();
                range.collapse(toStart);
                range.select();
            };
        } else {
            getBody().removeChild(testTextArea);
            fail("No means of finding text input caret position");
            return;
        }

        // Clean up
        getBody().removeChild(testTextArea);

        deleteText = function(el, start, end, moveSelection) {
            var val;
            if (start != end) {
                val = el.value;
                el.value = val.slice(0, start) + val.slice(end);
            }
            if (moveSelection) {
                setSelection(el, start, start);
            }
        };

        deleteSelectedText = function(el) {
            var sel = getSelection(el);
            deleteText(el, sel.start, sel.end, true);
        };

        extractSelectedText = function(el) {
            var sel = getSelection(el), val;
            if (sel.start != sel.end) {
                val = el.value;
                el.value = val.slice(0, sel.start) + val.slice(sel.end);
            }
            setSelection(el, sel.start, sel.start);
            return sel.text;
        };

        insertText = function(el, text, index, moveSelection) {
            var val = el.value, caretIndex;
            el.value = val.slice(0, index) + text + val.slice(index);
            if (moveSelection) {
                caretIndex = index + text.length;
                setSelection(el, caretIndex, caretIndex);
            }
        };

        replaceSelectedText = function(el, text) {
            var sel = getSelection(el), val = el.value;
            el.value = val.slice(0, sel.start) + text + val.slice(sel.end);
            var caretIndex = sel.start + text.length;
            setSelection(el, caretIndex, caretIndex);
        };

        surroundSelectedText = function(el, before, after) {
            var sel = getSelection(el), val = el.value;

            el.value = val.slice(0, sel.start) + before + sel.text + after + val.slice(sel.end);
            var startIndex = sel.start + before.length;
            var endIndex = startIndex + sel.length;
            setSelection(el, startIndex, endIndex);
        };

        function jQuerify(func, returnThis) {
            return function() {
                var el = this.jquery ? this[0] : this;
                var nodeName = el.nodeName.toLowerCase();

                if (el.nodeType == 1 && (nodeName == "textarea" || (nodeName == "input" && el.type == "text"))) {
                    var args = [el].concat(Array.prototype.slice.call(arguments));
                    var result = func.apply(this, args);
                    if (!returnThis) {
                        return result;
                    }
                }
                if (returnThis) {
                    return this;
                }
            };
        }

        $.fn.extend({
            getSelection: jQuerify(getSelection, false),
            setSelection: jQuerify(setSelection, true),
            collapseSelection: jQuerify(collapseSelection, true),
            deleteSelectedText: jQuerify(deleteSelectedText, true),
            deleteText: jQuerify(deleteText, true),
            extractSelectedText: jQuerify(extractSelectedText, false),
            insertText: jQuerify(insertText, true),
            replaceSelectedText: jQuerify(replaceSelectedText, true),
            surroundSelectedText: jQuerify(surroundSelectedText, true)
        });
    });
})(jQuery);;/*jshint laxcomma:true*/

/*globals $ document window PUI */

/**
 * PrimeUI BaseMenu widget
 */
$(function() {
    "use strict";    // Added for AngularPrime

    $.widget("primeui.puibasemenu", {

        options: {
            popup: false,
            trigger: null,
            my: 'left top',
            at: 'left bottom',
            triggerEvent: 'click'
        },

        _create: function() {
            if(this.options.popup) {
                this._initPopup();
            }
        },

        _initPopup: function() {
            var $this = this;

            this.element.closest('.pui-menu').addClass('pui-menu-dynamic pui-shadow').appendTo(document.body);

            this.positionConfig = {
                my: this.options.my
                ,at: this.options.at
                ,of: this.options.trigger
            };

            this.options.trigger.on(this.options.triggerEvent + '.pui-menu', function(e) {
                var trigger = $(this);

                if($this.element.is(':visible')) {
                    $this.hide();
                }
                else {
                    $this.show();
                }

                e.preventDefault();
            });

            //hide overlay on document click
            $(document.body).on('click.pui-menu', function (e) {
                var popup = $this.element.closest('.pui-menu');
                if(popup.is(":hidden")) {
                    return;
                }

                //do nothing if mousedown is on trigger
                var target = $(e.target);
                if(target.is($this.options.trigger.get(0))||$this.options.trigger.has(target).length > 0) {
                    return;
                }

                //hide if mouse is outside of overlay except trigger
                var offset = popup.offset();
                if(e.pageX < offset.left ||
                    e.pageX > offset.left + popup.width() ||
                    e.pageY < offset.top ||
                    e.pageY > offset.top + popup.height()) {

                    $this.hide(e);
                }
            });

            //Hide overlay on resize
            $(window).on('resize.pui-menu', function() {
                if($this.element.closest('.pui-menu').is(':visible')) {
                    $this.align();
                }
            });
        },

        show: function() {
            this.align();
            this.element.closest('.pui-menu').css('z-index', ++PUI.zindex).show();
        },

        hide: function() {
            this.element.closest('.pui-menu').fadeOut('fast');
        },

        align: function() {
            this.element.closest('.pui-menu').css({left:'', top:''}).position(this.positionConfig);
        }
    });
});
;/*globals $ */

/**
 * PrimeUI Accordion widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puiaccordion", {

        options: {
            activeIndex: 0,
            multiple: false
        },

        _create: function() {
            if(this.options.multiple) {
                this.options.activeIndex = [];
            }

            var $this = this;
            this.element.addClass('pui-accordion ui-widget ui-helper-reset');

            this.element.children('h3').addClass('pui-accordion-header ui-helper-reset ui-state-default').each(function(i) {
                var header = $(this),
                    title = header.html(),
                    headerClass = (i == $this.options.activeIndex) ? 'ui-state-active ui-corner-top' : 'ui-corner-all',
                    iconClass = (i == $this.options.activeIndex) ? 'ui-icon ui-icon-triangle-1-s' : 'ui-icon ui-icon-triangle-1-e';

                header.addClass(headerClass).html('<span class="' + iconClass + '"></span><a href="#">' + title + '</a>');
            });

            this.element.children('div').each(function(i) {
                var content = $(this);
                content.addClass('pui-accordion-content ui-helper-reset ui-widget-content');

                if(i != $this.options.activeIndex) {
                    content.addClass('ui-helper-hidden');
                }
            });

            this.headers = this.element.children('.pui-accordion-header');
            this.panels = this.element.children('.pui-accordion-content');
            this.headers.children('a').disableSelection();

            this._bindEvents();
        },

        _bindEvents: function() {
            var $this = this;

            this.headers.mouseover(function() {
                var element = $(this);
                if(!element.hasClass('ui-state-active')&&!element.hasClass('ui-state-disabled')) {
                    element.addClass('ui-state-hover');
                }
            }).mouseout(function() {
                    var element = $(this);
                    if(!element.hasClass('ui-state-active')&&!element.hasClass('ui-state-disabled')) {
                        element.removeClass('ui-state-hover');
                    }
                }).click(function(e) {
                    var element = $(this);
                    if(!element.hasClass('ui-state-disabled')) {
                        var tabIndex = element.index() / 2;

                        if(element.hasClass('ui-state-active')) {
                            $this.unselect(tabIndex);
                        }
                        else {
                            $this.select(tabIndex);
                        }
                    }

                    e.preventDefault();
                });
        },

        /**
         *  Activates a tab with given index
         */
        select: function(index) {
            var panel = this.panels.eq(index);

            //this._trigger('change', panel);

            //update state
            if(this.options.multiple)
                this._addToSelection(index);
            else
                this.options.activeIndex = index;

            this._trigger('change', null, {'index':index}); // AngularPrime Must be after the setting of the activeIndex

            this._show(panel);
        },

        /**
         *  Deactivates a tab with given index
         */
        unselect: function(index) {
            var panel = this.panels.eq(index),
                header = panel.prev();

            header.attr('aria-expanded', false).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-e');
            header.removeClass('ui-state-active ui-corner-top').addClass('ui-corner-all');
            panel.attr('aria-hidden', true).slideUp();

            this._removeFromSelection(index);
        },

        _show: function(panel) {
            //deactivate current
            if(!this.options.multiple) {
                var oldHeader = this.headers.filter('.ui-state-active');
                oldHeader.children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-e');
                oldHeader.attr('aria-expanded', false).removeClass('ui-state-active ui-corner-top').addClass('ui-corner-all').next().attr('aria-hidden', true).slideUp();
            }

            //activate selected
            var newHeader = panel.prev();
            newHeader.attr('aria-expanded', true).addClass('ui-state-active ui-corner-top').removeClass('ui-state-hover ui-corner-all')
                .children('.ui-icon').removeClass('ui-icon-triangle-1-e').addClass('ui-icon-triangle-1-s');

            panel.attr('aria-hidden', false).slideDown('normal');
        },

        _addToSelection: function(nodeId) {
            this.options.activeIndex.push(nodeId);
        },

        _removeFromSelection: function(index) {
            this.options.activeIndex = $.grep(this.options.activeIndex, function(r) {
                return r != index;
            });
        },

        // Added for AngularPrime
        getActiveIndex : function() {
            return this.options.activeIndex;
        }

    });
});
;/*globals angular $ */

(function () {
    "use strict";

    angular.module('angular.prime').directive('puiAccordion', ['$http', '$templateCache', '$compile', '$log',
                                                  function ($http, $templateCache, $compile, $log) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {

            return function postLink (scope, element, attrs) {

                var options = scope.$eval(attrs.puiAccordion) || {} ,
                    dynamicPanels = angular.isArray(options) || angular.isArray(options.urls) ,
                    content = [] ,
                    urls = [] ,
                    remaining ,
                    initialCall = true;

                function renderAccordion(panels) {
                    var htmlContent = '';
                    angular.forEach(panels, function(panelContent) {
                       htmlContent = htmlContent + panelContent;
                    });
                    element.html(htmlContent);
                    $compile(element.contents())(scope);
                    $(function () {
                        if (!initialCall) {
                            element.puiaccordion('destroy', {});
                        }
                        element.puiaccordion({
                            multiple: options.multiple, activeIndex: options.activeIndex
                        });
                        initialCall = false;

                    });
                }

                function loadHtmlContents(idx, url) {
                    $http.get(url, {cache: $templateCache}).success(function (response) {
                        content[idx] = response;
                        remaining--;
                        if (remaining === 0) {
                            renderAccordion(content);
                        }
                    }).error(function () {
                            $log.error('Error loading included file ' + url + ' for panel of accordion');
                        });

                }

                function loadAndRenderAccordion() {
                    remaining = urls.length;
                    for (var i = 0; i < urls.length; i++) {
                        loadHtmlContents(i, urls[i]);
                    }
                }

                if (dynamicPanels) {

                    if (angular.isArray(options)) {
                        scope.$watch(attrs.puiAccordion, function(x) {
                            urls = x;
                            loadAndRenderAccordion();
                        }, true);

                    } else {
                        scope.$watch(attrs.puiAccordion+'.urls', function(x) {
                            urls = x;
                            loadAndRenderAccordion();
                        }, true);
                    }

                    loadAndRenderAccordion();


                } else {
                    var scopedOptions = attrs.puiAccordion && attrs.puiAccordion.trim().charAt(0) !== '{';

                    options.activeIndex = options.activeIndex || 0;
                    $(function () {
                        element.puiaccordion({
                            multiple: options.multiple, activeIndex: options.activeIndex
                        });

                    });

                    if (scopedOptions || options.callback) {
                        // Listen for change events to enable binding
                        element.bind('puiaccordionchange', function (eventData, idx) {
                            var index = idx.index;
                            if (scopedOptions) {
                                scope.safeApply(read(index));
                            }
                            if (options.callback) {
                                options.callback(index);
                            }

                        });
                    }
                    if (scopedOptions) {
                        read(undefined); // initialize

                        scope.$watch(attrs.puiAccordion + '.activeIndex', function (value) {
                            var index = element.puiaccordion('getActiveIndex');
                            // Only select the panel if not already selected (otherwise additional collapse/expand)
                            if (value !== index) {
                                element.puiaccordion('select', value);
                            }


                        });
                    }


                }
                // Write data to the model
                function read (index) {
                    var idx = (index !== undefined) ? index : element.puiaccordion('getActiveIndex');
                    scope[attrs.puiAccordion].activeIndex = idx;
                }
            };
        }};
}]);

}());
;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiAutocomplete', function () {
    return {
        restrict: 'A',
        require: '?ngModel', // get a hold of NgModelController
        link: function (scope, element, attrs, ngModel) {
            if (!ngModel) {
                return;
            } // do nothing if no ng-model

            var htmlElementName = element[0].nodeName;
            if ('TEXTAREA' === htmlElementName) {
                // This is handles by the pui-input on a text area element.
                return;
            }
            var options = scope.$eval(attrs.puiAutocomplete) || {},
                optionIsFunction = angular.isFunction(options),
                optionIsArray = angular.isArray(options),
                completeSource = null;

            if (optionIsFunction || optionIsArray) {
                completeSource = options;
                options = {};
            } else {
                options.dropdown = (options.dropdown !== undefined) ? options.dropdown : false;
                options.multiple = (options.multiple !== undefined) ? options.multiple : false;
                options.forceSelection = (options.forceSelection !== undefined) ? options.forceSelection : false;

                options.delay = options.delay || 300;
                options.minQueryLength = options.minQueryLength || 1;
                options.scrollHeight = options.scrollHeight || 200;
                options.effectSpeed = options.effectSpeed || 'normal';
                options.caseSensitive = (options.caseSensitive !== undefined) ? options.caseSensitive : false;
                if (!options.completeSource) {
                    throw ("completeSource property required for auto complete functionality");
                }
                completeSource = options.completeSource;
            }

            $(function () {

                element.puiautocomplete({
                    dropdown : options.dropdown ,
                    multiple : options.multiple ,
                    completeSource: completeSource ,
                    forceSelection: options.forceSelection ,

                    delay: options.delay ,
                    minQueryLength: options.minQueryLength ,
                    scrollHeight: options.scrollHeight ,
                    effectSpeed: options.effectSpeed ,
                    caseSensitive: options.caseSensitive ,
                    effect: options.effect ,
                    effectOptions: options.effectOptions
                });

                var helper = {
                    read: function (label, addForMultiple) {
                        $(function () {
                            if (options.multiple) {

                                if (addForMultiple) {
                                    scope.safeApply(function () {
                                        options.multipleValues.push(label);
                                        ngModel.$setViewValue("");
                                    });
                                } else {
                                    var index = options.multipleValues.indexOf(label);
                                    scope.safeApply(function () {
                                        options.multipleValues.splice(index, 1);
                                    });
                                }
                            } else {
                                scope.safeApply(function () {
                                    ngModel.$setViewValue(element.val());
                                });
                            }

                        });

                    }
                };

                // Listen for select events to enable binding
                element.bind('puiautocompleteselect', function (event, token) {
                    var label = (options.multiple) ? token : null;
                    helper.read(label, true);
                    if (options.callback) {
                        options.callback(token);
                    }
                });

                element.bind('puiautocompleteunselect', function (event, token) {
                    var label = (options.multiple) ? token : null;
                    helper.read(label, false);
                });

                if (attrs.ngDisabled) {
                    scope.$watch(attrs.ngDisabled, function (value) {
                        if (value === false) {
                            $(function () {
                                element.puiautocomplete('enable');
                            });
                        } else {
                            $(function () {
                                element.puiautocomplete('disable');

                            });

                        }
                    });
                }


            });


        }

    };

});

}());
;/*globals $ window PUI document*/
/*jshint laxcomma:true*/

/**
 * PrimeUI autocomplete widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puiautocomplete", {

        options: {
            delay: 300,
            minQueryLength: 1,
            multiple: false,
            dropdown: false,
            scrollHeight: 200,
            forceSelection: false,
            effect:null,
            effectOptions: {},
            effectSpeed: 'normal',
            content: null,
            caseSensitive: false
        },

        _create: function() {
            this.element.puiinputtext();
            this.panel = $('<div class="pui-autocomplete-panel ui-widget-content ui-corner-all ui-helper-hidden pui-shadow"></div>').appendTo('body');

            if(this.options.multiple) {
                this.element.wrap('<ul class="pui-autocomplete-multiple ui-widget pui-inputtext ui-state-default ui-corner-all">' +
                    '<li class="pui-autocomplete-input-token"></li></ul>');
                this.inputContainer = this.element.parent();
                this.multiContainer = this.inputContainer.parent();
            }
            else {
                if(this.options.dropdown) {
                    this.dropdown = $('<button type="button" class="pui-button ui-widget ui-state-default ui-corner-right pui-button-icon-only">' +
                        '<span class="pui-button-icon-primary ui-icon ui-icon-triangle-1-s"></span><span class="pui-button-text">&nbsp;</span></button>')
                        .insertAfter(this.element);
                    this.element.removeClass('ui-corner-all').addClass('ui-corner-left');
                }
            }

            this._bindKeyEvents(); // Added for AngularPrime
            this._bindEvents();
        },

        _bindEvents: function() {
            var $this = this;

            //this._bindKeyEvents(); // Removed for AngularPrime

            if(this.options.dropdown) {
                this.dropdown.on('hover.puiautocomplete', function() {
                    if(!$this.element.prop('disabled')) {
                        $this.dropdown.toggleClass('ui-state-hover');
                    }
                })
                    .on('mousedown.puiautocomplete', function() {
                        if(!$this.element.prop('disabled')) {
                            $this.dropdown.addClass('ui-state-active');
                        }
                    })
                    .on('mouseup.puiautocomplete', function() {
                        if(!$this.element.prop('disabled')) {
                            $this.dropdown.removeClass('ui-state-active');
                            $this.search('');
                            $this.element.focus();
                        }
                    })
                    .on('focus.puiautocomplete', function() {
                        $this.dropdown.addClass('ui-state-focus');
                    })
                    .on('blur.puiautocomplete', function() {
                        $this.dropdown.removeClass('ui-state-focus');
                    })
                    .on('keydown.puiautocomplete', function(e) {
                        var keyCode = $.ui.keyCode;

                        if(e.which == keyCode.ENTER || e.which == keyCode.NUMPAD_ENTER) {
                            $this.search('');
                            $this.input.focus();

                            e.preventDefault();
                        }
                    });
            }

            if(this.options.multiple) {
                this.multiContainer.on('hover.puiautocomplete', function() {
                    $(this).toggleClass('ui-state-hover');
                })
                    .on('click.puiautocomplete', function() {
                        $this.element.trigger('focus');
                    });

                this.element.on('focus.pui-autocomplete', function() {
                    $this.multiContainer.addClass('ui-state-focus');
                })
                    .on('blur.pui-autocomplete', function(e) {
                        $this.multiContainer.removeClass('ui-state-focus');
                    });
            }

            if(this.options.forceSelection) {
                this.cachedResults = [this.element.val()];
                this.element.on('blur.puiautocomplete', function() {
                    var value = $this.element.val(),
                        valid = false;

                    for(var i = 0; i < $this.cachedResults.length; i++) {
                        if($this.cachedResults[i] == value) {
                            valid = true;
                            break;
                        }
                    }

                    if(!valid) {
                        $this.element.val('');
                        $this._trigger('select', null, ''); // Added for AngularPrime
                    }
                });
            }

            $(document.body).bind('mousedown.puiautocomplete', function (e) {
                if($this.panel.is(":hidden")) {
                    return;
                }

                if(e.target === $this.element.get(0)) {
                    return;
                }

                var offset = $this.panel.offset();
                if (e.pageX < offset.left ||
                    e.pageX > offset.left + $this.panel.width() ||
                    e.pageY < offset.top ||
                    e.pageY > offset.top + $this.panel.height()) {
                    $this.hide();
                }
            });

            $(window).bind('resize.' + this.element.id, function() {
                if($this.panel.is(':visible')) {
                    $this._alignPanel();
                }
            });
        },

        _bindKeyEvents: function() {
            var $this = this;

            this.element.on('keyup.puiautocomplete', function(e) {
                if(key == keyCode.UP ||
                    key == keyCode.LEFT ||
                    key == keyCode.DOWN ||
                    key == keyCode.RIGHT ||
                    key == keyCode.TAB ||
                    key == keyCode.SHIFT ||
                    key == keyCode.ENTER ||
                    key == keyCode.NUMPAD_ENTER) {
                    shouldSearch = false;
                }

                var keyCode = $.ui.keyCode,
                    key = e.which,
                    shouldSearch = true;

                if(shouldSearch) {
                    var value = $this.element.val();

                    if(!value.length) {
                        $this.hide();
                    }

                    if(value.length >= $this.options.minQueryLength) {
                        if($this.timeout) {
                            window.clearTimeout($this.timeout);
                        }

                        $this.timeout = window.setTimeout(function() {
                                $this.search(value);
                            },
                            $this.options.delay);
                    }
                }

            }).on('keydown.puiautocomplete', function(e) {
                    if($this.panel.is(':visible')) {
                        var keyCode = $.ui.keyCode,
                            highlightedItem = $this.items.filter('.ui-state-highlight');

                        switch(e.which) {
                            case keyCode.UP:
                            case keyCode.LEFT:
                                var prev = highlightedItem.prev();

                                if(prev.length == 1) {
                                    highlightedItem.removeClass('ui-state-highlight');
                                    prev.addClass('ui-state-highlight');

                                    if($this.options.scrollHeight) {
                                        PUI.scrollInView($this.panel, prev);
                                    }
                                }

                                e.preventDefault();
                                break;

                            case keyCode.DOWN:
                            case keyCode.RIGHT:
                                var next = highlightedItem.next();

                                if(next.length == 1) {
                                    highlightedItem.removeClass('ui-state-highlight');
                                    next.addClass('ui-state-highlight');

                                    if($this.options.scrollHeight) {
                                        PUI.scrollInView($this.panel, next);
                                    }
                                }

                                e.preventDefault();
                                break;

                            case keyCode.ENTER:
                            case keyCode.NUMPAD_ENTER:
                                highlightedItem.trigger('click');

                                e.preventDefault();
                                break;

                            case keyCode.ALT:
                            case 224:
                                break;

                            case keyCode.TAB:
                                highlightedItem.trigger('click');
                                $this.hide();
                                break;
                        }
                    }

                });
        },

        _bindDynamicEvents: function() {
            var $this = this;

            this.items.on('mouseover.puiautocomplete', function() {
                var item = $(this);

                if(!item.hasClass('ui-state-highlight')) {
                    $this.items.filter('.ui-state-highlight').removeClass('ui-state-highlight');
                    item.addClass('ui-state-highlight');
                }
            })
                .on('click.puiautocomplete', function(event) {
                    var item = $(this);

                    if($this.options.multiple) {
                        var tokenMarkup = '<li class="pui-autocomplete-token ui-state-active ui-corner-all ui-helper-hidden">';
                        tokenMarkup += '<span class="pui-autocomplete-token-icon ui-icon ui-icon-close" />';
                        tokenMarkup += '<span class="pui-autocomplete-token-label">' + item.data('label') + '</span></li>';

                        $(tokenMarkup).data(item.data())
                            .insertBefore($this.inputContainer).fadeIn()
                            .children('.pui-autocomplete-token-icon').on('click.pui-autocomplete', function(e) {
                                var token = $(this).parent();
                                $this._removeItem(token);
                            $this._trigger('unselect', e, token.data('label'));  // Changed for AngularPrime
                            });

                        $this.element.val('').trigger('focus');
                    }
                    else {
                        $this.element.val(item.data('label')).focus();
                    }

                //$this._trigger('select', event, item); // Changed for AngularPrime
                $this._trigger('select', event, item.data('label'));
                    $this.hide();
                });
        },

        search: function(q) {
            this.query = this.options.caseSensitive ? q : q.toLowerCase();

            var request = {
                query: this.query
            };

            if(this.options.completeSource) {
                if($.isArray(this.options.completeSource)) {
                    var sourceArr = this.options.completeSource,
                        data = [],
                        emptyQuery = ($.trim(q) === '');

                    for(var i = 0 ; i < sourceArr.length; i++) {
                        var item = sourceArr[i],
                            itemLabel = item.label||item;

                        if(!this.options.caseSensitive) {
                            itemLabel = itemLabel.toLowerCase();
                        }

                        if(emptyQuery||itemLabel.indexOf(this.query) === 0) {
                            data.push({label:sourceArr[i], value: item});
                        }
                    }

                    this._handleData(data);
                }
                else {
                    this.options.completeSource.call(this, request, this._handleData);
                }
            }
        },

        _handleData: function(data) {
            var $this = this;
            this.panel.html('');
            this.listContainer = $('<ul class="pui-autocomplete-items pui-autocomplete-list ui-widget-content ui-widget ui-corner-all ui-helper-reset"></ul>').appendTo(this.panel);

            for(var i = 0; i < data.length; i++) {
                var item = $('<li class="pui-autocomplete-item pui-autocomplete-list-item ui-corner-all"></li>');
                item.data(data[i]);

                if(this.options.content)
                    item.html(this.options.content.call(this, data[i]));
                else
                    item.text(data[i].label);

                this.listContainer.append(item);
            }

            this.items = this.listContainer.children('.pui-autocomplete-item');

            this._bindDynamicEvents();

            if(this.items.length > 0) {
                var firstItem = $this.items.eq(0),
                    hidden = this.panel.is(':hidden');
                firstItem.addClass('ui-state-highlight');

                if($this.query.length > 0 && !$this.options.content) {
                    $this.items.each(function() {
                        var item = $(this),
                            text = item.html(),
                            re = new RegExp(PUI.escapeRegExp($this.query), 'gi'),
                            highlighedText = text.replace(re, '<span class="pui-autocomplete-query">$&</span>');

                        item.html(highlighedText);
                    });
                }

                if(this.options.forceSelection) {
                    this.cachedResults = [];
                    $.each(data, function(i, item) {  // Changed for AngularPrime
                        $this.cachedResults.push(item.label);
                    });
                }

                //adjust height
                if($this.options.scrollHeight) {
                    var heightConstraint = hidden ? $this.panel.height() : $this.panel.children().height();

                    if(heightConstraint > $this.options.scrollHeight)
                        $this.panel.height($this.options.scrollHeight);
                    else
                        $this.panel.css('height', 'auto');

                }

                if(hidden) {
                    $this.show();
                }
                else {
                    $this._alignPanel();
                }
            }
            else {
                this.panel.hide();
            }
        },

        show: function() {
            this._alignPanel();

            if(this.options.effect)
                this.panel.show(this.options.effect, {}, this.options.effectSpeed);
            else
                this.panel.show();
        },

        hide: function() {
            this.panel.hide();
            this.panel.css('height', 'auto');
        },

        _removeItem: function(item) {
            item.fadeOut('fast', function() {
                var token = $(this);

                token.remove();
            });
        },

        _alignPanel: function() {
            var panelWidth = null;

            if(this.options.multiple) {
                panelWidth = this.multiContainer.innerWidth() - (this.element.position().left - this.multiContainer.position().left);
            }
            else {
                if(this.panel.is(':visible')) {
                    panelWidth = this.panel.children('.pui-autocomplete-items').outerWidth();
                }
                else {
                    this.panel.css({'visibility':'hidden','display':'block'});
                    panelWidth = this.panel.children('.pui-autocomplete-items').outerWidth();
                    this.panel.css({'visibility':'visible','display':'none'});
                }

                var inputWidth = this.element.outerWidth();
                if(panelWidth < inputWidth) {
                    panelWidth = inputWidth;
                }
            }

            this.panel.css({
                'left':'',
                'top':'',
                'width': panelWidth,
                'z-index': ++PUI.zindex
            })
                .position({
                    my: 'left top'
                    ,at: 'left bottom'
                    ,of: this.element
                });
        },

        // Added for AngularPrime
        _unbindEvents: function() {
            if(this.options.dropdown) {
                this.dropdown.off();
            }

        },

        enable : function() {
            this._bindEvents();
            if(this.options.dropdown) {
                this.dropdown.removeClass('ui-state-disabled');

            }
        },

        disable : function() {
            this._unbindEvents();
            if(this.options.dropdown) {
                this.dropdown.addClass('ui-state-disabled');
            }
        }

    });

});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiBreadcrumb', ['$compile',
                                                    function ($compile) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            return function postLink(scope, element, attrs) {

                var options = scope.$eval(attrs.puiBreadcrumb) || {} ,
                    dynamicBreadcrumb = angular.isArray(options) || angular.isArray(options.items) ,
                    breadcrumbItems = [] ,
                    initialCall = true;

                function renderBreadcrumb() {
                    var htmlContents = '' ,
                        globalActionText = null;
                    angular.forEach(breadcrumbItems, function (breadcrumbItem) {
                        var actionText = breadcrumbItem.onclick ,
                            hasPlaceholder = actionText !== undefined && actionText.indexOf('{id}') > -1;

                        if (actionText === undefined && globalActionText !== null) {
                            actionText = globalActionText;
                        }
                        if (breadcrumbItem.globalAction)  {
                            globalActionText = actionText;
                        }

                        if (hasPlaceholder) {
                            actionText = actionText.replace('{id}', breadcrumbItem.id);
                        }

                        htmlContents = htmlContents + '<li id="' + breadcrumbItem.id + '"><a ';
                        if (actionText !== undefined) {
                            htmlContents = htmlContents + 'ng-click="' + actionText +'"';
                        }
                        htmlContents = htmlContents + '>' + breadcrumbItem.label + '</a></li>';
                    });
                    element.html(htmlContents);
                    $compile(element.contents())(scope);
                    $(function () {
                        if (!initialCall) {
                            element.puibreadcrumb('destroy', {});
                            element.unwrap();
                        }
                        element.puibreadcrumb({

                        });
                        initialCall = false;

                    });
                }

                if (dynamicBreadcrumb) {

                    if (angular.isArray(options)) {
                        scope.$watch(attrs.puiBreadcrumb, function(x) {
                            breadcrumbItems = x;
                            renderBreadcrumb();
                        }, true);

                    } else {
                        scope.$watch(attrs.puiBreadcrumb+'.items', function(x) {
                            breadcrumbItems = x;
                            renderBreadcrumb();
                        }, true);
                    }

                } else {
                    element.puibreadcrumb({
                        homeIcon: options.homeIcon
                    });

                }

            };
        }
    };
}]);

}());
;/*globals $ */

/**
 * PrimeUI BreadCrumb Widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puibreadcrumb", {

        // Added for AngularPrime
        options: {
            homeIcon: 'ui-icon-home'
        },

        _create: function() {
            this.element.wrap('<div class="pui-breadcrumb ui-module ui-widget ui-widget-header ui-helper-clearfix ui-corner-all" role="menu">');

            var customIcon = this.options.homeIcon;  // Added for AngularPrime
            this.element.children('li').each(function(index) {
                var listItem = $(this);

                listItem.attr('role', 'menuitem');
                var menuitemLink = listItem.children('a');
                menuitemLink.addClass('pui-menuitem-link ui-corner-all').contents().wrap('<span class="ui-menuitem-text" />');

                if(index > 0 || customIcon === null)  // Changed for AngularPrime
                    listItem.before('<li class="pui-breadcrumb-chevron ui-icon ui-icon-triangle-1-e"></li>');
                else
                    menuitemLink.addClass('ui-icon ' + customIcon); // Changed for AngularPrime
            });
        }
    });
});
;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiButton', ['$interpolate', function ($interpolate) {
        return {
            restrict: 'A',
            compile: function (element, attrs) {
                return function postLink (scope, element, attrs) {
                    var titleWatches = [] ,
                        parsedExpression = $interpolate(element.text());
                    element.text(scope.$eval(parsedExpression));
                    angular.forEach(parsedExpression.parts, function (part) {
                        if (angular.isFunction(part)) {
                            titleWatches.push(part.exp);
                        }
                    }, titleWatches);

                    $(function () {

                        var options = scope.$eval(attrs.puiButton) || {};
                        element.puibutton({
                            icon: options.icon,
                            iconPos: options.iconPosition || 'left'
                        });
                    });

                    if (attrs.ngDisabled) {
                        scope.$watch(attrs.ngDisabled, function (value) {
                            if (value === false) {
                                $(function () {
                                    element.puibutton('enable');
                                });
                            } else {
                                $(function () {
                                    element.puibutton('disable');
                                });

                            }
                        });
                    }
                    angular.forEach(titleWatches, function(watchValue) {
                        scope.$watch(watchValue, function (value) {
                            $(function () {
                                element.puibutton('setTitle', scope.$eval(parsedExpression));
                            });
                        });
                    });

                };
            }

        };
    }]

);

}());
;/*jshint laxcomma:true*/
/*globals $ */

/**
 * PrimeFaces Button Widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puibutton", {

        options: {
            icon: null
            ,iconPos : 'left'
        },

        _create: function() {
            var element = this.element,
                text = element.text()||'pui-button',
                disabled = element.prop('disabled'),
                styleClass = null;

            if(this.options.icon) {
                styleClass = (text === 'pui-button') ? 'pui-button-icon-only' : 'pui-button-text-icon-' + this.options.iconPos;
            }
            else {
                styleClass = 'pui-button-text-only';
            }

            if(disabled) {
                styleClass += ' ui-state-disabled';
            }

            this.element.addClass('pui-button ui-widget ui-state-default ui-corner-all ' + styleClass).text('');

            if(this.options.icon) {
                this.element.append('<span class="pui-button-icon-' + this.options.iconPos + ' ui-icon ' + this.options.icon + '" />');
            }

            this.element.append('<span class="pui-button-text">' + text + '</span>');

            //aria
            element.attr('role', 'button').attr('aria-disabled', disabled);

            if(!disabled) {
                this._bindEvents();
            }
        },

        _bindEvents: function() {
            var element = this.element,
                $this = this;

            element.on('mouseover.puibutton', function(){
                if(!element.prop('disabled')) {
                    element.addClass('ui-state-hover');
                }
            }).on('mouseout.puibutton', function() {
                    $(this).removeClass('ui-state-active ui-state-hover');
                }).on('mousedown.puibutton', function() {
                    if(!element.hasClass('ui-state-disabled')) {
                        element.addClass('ui-state-active').removeClass('ui-state-hover');
                    }
                }).on('mouseup.puibutton', function(e) {
                    element.removeClass('ui-state-active').addClass('ui-state-hover');

                    $this._trigger('click', e);
                }).on('focus.puibutton', function() {
                    element.addClass('ui-state-focus');
                }).on('blur.puibutton', function() {
                    element.removeClass('ui-state-focus');
                }).on('keydown.puibutton',function(e) {
                    if(e.keyCode == $.ui.keyCode.SPACE || e.keyCode == $.ui.keyCode.ENTER || e.keyCode == $.ui.keyCode.NUMPAD_ENTER) {
                        element.addClass('ui-state-active');
                    }
                }).on('keyup.puibutton', function() {
                    element.removeClass('ui-state-active');
                });

            return this;
        },

        _unbindEvents: function() {
            this.element.off('mouseover.puibutton mouseout.puibutton mousedown.puibutton mouseup.puibutton focus.puibutton blur.puibutton keydown.puibutton keyup.puibutton');
        },

        disable: function() {
            this._unbindEvents();

            this.element.attr({
                'disabled':'disabled',
                'aria-disabled': true
            }).addClass('ui-state-disabled');
        },

        enable: function() {
            this._bindEvents();

            this.element.removeAttr('disabled').attr('aria-disabled', false).removeClass('ui-state-disabled');
        },

        // Added for AngularPrime
        setTitle: function(title) {
            this.element.find('.pui-button-text').html(title);
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiDatatable', [ '$log', function ($log) {
    return {
        restrict: 'A',
        priority: 5,
        compile: function (element, attrs) {
            return function postLink(scope, element, attrs) {
                var options = scope.$eval(attrs.puiDatatable) || {},
                    data = [],
                    functionBasedData = false,
                    columns = element.data('puiColumns') || [],
                    selectionMode = null,
                    paginator = null;

                if (angular.isArray(options)) {
                    data = options;
                }

                if (angular.isFunction(options)) {
                    data = options;
                    functionBasedData = true;
                }

                if (angular.isArray(data) && data.length === 0) {
                    if (angular.isFunction(options.tableData)) {
                        functionBasedData = true;
                    }
                    data = options.tableData;

                }

                if (columns.length === 0) {
                    if (options.columns) {
                        columns = options.columns;
                    }
                    if (!functionBasedData && columns.length === 0) {
                        for (var property in data[0]) {
                            columns.push({field: property, headerText: property});
                        }
                    }
                }

                if (options.selectionMode) {
                    selectionMode = options.selectionMode;
                }

                if (options.rowSelect && selectionMode === null) {
                    selectionMode = 'single';
                }

                if (options.selectedData) {

                    if (selectionMode === null) {
                        selectionMode = 'multiple';
                    }

                    element.bind('puidatatablerowselect', function (eventData, idx) {
                        $(function () {
                            var data = element.puidatatable('getData');
                            scope.safeApply(function () {
                                var rowIndex = data.indexOf(idx),
                                    index = options.selectedData.indexOf(rowIndex);
                                if (index === -1) {
                                    options.selectedData.push(rowIndex);
                                }


                            });


                        });
                    });

                    element.bind('puidatatablerowunselect', function (eventData, idx) {
                        $(function () {
                            var data = element.puidatatable('getData');
                            scope.safeApply(function () {
                                var rowIndex = data.indexOf(idx),
                                    index = options.selectedData.indexOf(rowIndex);
                                if (index !== -1) {
                                    options.selectedData.splice(index, 1);
                                }

                            });

                        });
                    });

                    element.bind('puidatatableunselectallrows', function (eventData, idx) {
                        $(function () {
                            scope.safeApply(function () {
                                options.selectedData = [];
                            });
                        });
                    });

                }

                if (options.paginatorRows) {
                    paginator = {
                        rows: options.paginatorRows
                    };
                }

                if (options.selectedData) {
                    scope.$watch(attrs.puiDatatable + '.selectedData', function (x) {
                        $(function () {
                            element.puidatatable('unselectAllRows', true);
                            if (selectionMode === 'single' && x.length === 2) {
                                x = x.splice(1,1);  // assume the last added one (now 2 elements) is the one we need
                                options.selectedData = x;
                            }
                            angular.forEach(x, function (row) {
                                element.puidatatable('selectRowByIndex', row);
                            });
                        });
                    }, true);
                }

                $(function () {

                    element.puidatatable({
                        caption: options.caption,
                        datasource : data,
                        columns: columns,
                        selectionMode: selectionMode,
                        rowSelect: options.rowSelect,
                        rowUnselect: options.rowUnselect,
                        paginator: paginator
                    });

                });

                if (options.selectedPage !== undefined) {
                    if (options.paginatorRows !== undefined) {
                        $(function () {
                            var paginator = element.puidatatable('getPaginator');

                            paginator.bind('puipaginatorpaginate', function (eventData, pageState) {
                                scope.safeApply(function () {
                                    options.selectedPage = pageState.page;
                                });

                            });

                            scope.$watch(attrs.puiDatatable + '.selectedPage', function (selectedPage) {
                                $(function () {
                                    paginator.puipaginator('setPage', parseInt(selectedPage, 10));
                                });
                            });
                        });
                    } else {
                        $log.warn('selectedPage option specified but no value for paginatorRows option defined');
                    }
                }

            };
        }
    };
}]);

angular.module('angular.prime').directive('puiColumn', function () {
    return {
        restrict: 'A',
        priority: 5,
        compile: function (element, attrs) {
            return function postLink(scope, element, attrs) {
                  var columns = element.parent().data('puiColumns') ,
                      options = scope.$eval(attrs.puiColumn) || {} ,
                      columnInfo = {};

                if (columns === undefined) {
                    columns = [];
                }

                if (options.hasOwnProperty('field')) {
                    columnInfo.field = options.field;
                    columnInfo.headerText = options.headerText;
                    if (columnInfo.headerText === undefined) {
                        columnInfo.headerText = columnInfo.field;
                    }
                    columnInfo.sortable = options.sortable;

                } else {
                    columnInfo.field = attrs.puiColumn;
                    columnInfo.headerText = attrs.puiColumn;
                }
                columns.push(columnInfo);
                element.parent().data('puiColumns', columns);
            };
        }
    };
});

}());
;/*globals $ PUI document*/

/**
 * PrimeUI Datatable Widget
 */
$(function() {
    "use strict"; // added for AngularPrime

    $.widget("primeui.puidatatable", {

        options: {
            columns: null,
            datasource: null,
            paginator: null,
            selectionMode: null,
            rowSelect: null,
            rowUnselect: null,
            caption: null
        },

        _create: function() {
            this.id = this.element.attr('id');
            if(!this.id) {
                this.id = this.element.uniqueId().attr('id');
            }

            this.element.addClass('pui-datatable ui-widget');
            this.tableWrapper = $('<div class="pui-datatable-tablewrapper" />').appendTo(this.element);
            this.table = $('<table><thead></thead><tbody></tbody></table>').appendTo(this.tableWrapper);
            this.thead = this.table.children('thead');
            this.tbody = this.table.children('tbody').addClass('pui-datatable-data');

            if(this.options.datasource) {
                if($.isArray(this.options.datasource)) {
                    this.data = this.options.datasource;
                    this._initialize();
                }
                else if($.type(this.options.datasource) === 'function') {
                    this.options.datasource.call(this, this._handleDataLoad);
                }
            }
        },

        _initialize: function() {
            var $this = this;

            if(this.options.columns) {
                $.each(this.options.columns, function(i, col) {
                    var header = $('<th class="ui-state-default"></th>').data('field', col.field).appendTo($this.thead);

                    if(col.headerText) {
                        header.text(col.headerText);
                    }

                    if(col.sortable) {
                        header.addClass('pui-sortable-column')
                            .data('order', 1)
                            .append('<span class="pui-sortable-column-icon ui-icon ui-icon-carat-2-n-s"></span>');
                    }
                });
            }

            if(this.options.caption) {
                this.table.prepend('<caption class="pui-datatable-caption ui-widget-header">' + this.options.caption + '</caption>');
            }

            if(this.options.paginator) {
                this.options.paginator.paginate = function(state) {
                    $this.paginate();
                };

                this.options.paginator.totalRecords = this.options.paginator.totalRecords||this.data.length;
                this.paginator = $('<div></div>').insertAfter(this.tableWrapper).puipaginator(this.options.paginator);
            }

            if(this._isSortingEnabled()) {
                this._initSorting();
            }

            if(this.options.selectionMode) {
                this._initSelection();
            }

            this._renderData();
        },

        _handleDataLoad: function(data) {
            this.data = data;
            if(!this.data) {
                this.data = [];
            }

            // Added for AngularPrime
            if (this.options.columns.length === 0) {
                for (var property in data[0]) {
                    this.options.columns.push({field: property, headerText: property}) ;
                }
            }
            // End added section


            this._initialize();
        },

        _initSorting: function() {
            var $this = this,
                sortableColumns = this.thead.children('th.pui-sortable-column');

            sortableColumns.on('mouseover.puidatatable', function() {
                var column = $(this);

                if(!column.hasClass('ui-state-active'))
                    column.addClass('ui-state-hover');
            })
                .on('mouseout.puidatatable', function() {
                    var column = $(this);

                    if(!column.hasClass('ui-state-active'))
                        column.removeClass('ui-state-hover');
                })
                .on('click.puidatatable', function() {
                    var column = $(this),
                        field = column.data('field'),
                        order = column.data('order'),
                        sortIcon = column.children('.pui-sortable-column-icon');

                    //clean previous sort state
                    column.siblings().filter('.ui-state-active').data('order', 1).removeClass('ui-state-active').children('span.pui-sortable-column-icon')
                        .removeClass('ui-icon-triangle-1-n ui-icon-triangle-1-s');

                    $this.sort(field, order);

                    //update state
                    column.data('order', -1*order);

                    column.removeClass('ui-state-hover').addClass('ui-state-active');
                    if(order === -1)
                        sortIcon.removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
                    else if(order === 1)
                        sortIcon.removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
                });
        },

        paginate: function() {
            this._renderData();
        },

        sort: function(field,order) {
            this.data.sort(function(data1, data2) {
                var value1 = data1[field],
                    value2 = data2[field],
                    result = (value1 < value2) ? -1 : (value1 > value2) ? 1 : 0;

                return (order * result);
            });

            if(this.options.selectionMode) {
                this.selection = [];
            }

            if(this.paginator) {
                this.paginator.puipaginator('option', 'page', 0);
            }

            this._renderData();
        },

        sortByField: function(a, b) {
            var aName = a.name.toLowerCase();
            var bName = b.name.toLowerCase();
            return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
        },

        _renderData: function() {
            if(this.data) {
                this.tbody.html('');

                var first = this.getFirst(),
                    rows = this.getRows();

                for(var i = first; i < (first + rows); i++) {
                    var rowData = this.data[i];

                    if(rowData) {
                        var row = $('<tr class="ui-widget-content" />').appendTo(this.tbody),
                            zebraStyle = (i%2 === 0) ? 'pui-datatable-even' : 'pui-datatable-odd';

                        row.addClass(zebraStyle);

                        if(this.options.selectionMode && PUI.inArray(this.selection, i)) {
                            row.addClass("ui-state-highlight");
                        }

                        for(var j = 0; j < this.options.columns.length; j++) {
                            var column = $('<td />').appendTo(row),
                                fieldValue = rowData[this.options.columns[j].field];

                            column.text(fieldValue);
                        }
                    }
                }
            }
        },

        getFirst: function() {
            if(this.paginator) {
                var page = this.paginator.puipaginator('option', 'page'),
                    rows = this.paginator.puipaginator('option', 'rows');

                return (page * rows);
            }
            else {
                return 0;
            }
        },

        getRows: function() {
            return this.paginator ? this.paginator.puipaginator('option', 'rows') : this.data.length;
        },

        _isSortingEnabled: function() {
            var cols = this.options.columns;
            if(cols) {
                for(var i = 0; i < cols.length; i++) {
                    if(cols[i].sortable) {
                        return true;
                    }
                }
            }

            return false;
        },

        _initSelection: function() {
            var $this = this;
            this.selection = [];
            this.rowSelector = '#' + this.id + ' tbody.pui-datatable-data > tr.ui-widget-content:not(.ui-datatable-empty-message)';

            //shift key based range selection
            if(this._isMultipleSelection()) {
                this.originRowIndex = 0;
                this.cursorIndex = null;
            }

            $(document).off('mouseover.puidatatable mouseout.puidatatable click.puidatatable', this.rowSelector)
                .on('mouseover.datatable', this.rowSelector, null, function() {
                    var element = $(this);

                    if(!element.hasClass('ui-state-highlight')) {
                        element.addClass('ui-state-hover');
                    }
                })
                .on('mouseout.datatable', this.rowSelector, null, function() {
                    var element = $(this);

                    if(!element.hasClass('ui-state-highlight')) {
                        element.removeClass('ui-state-hover');
                    }
                })
                .on('click.datatable', this.rowSelector, null, function(e) {
                    $this._onRowClick(e, this);
                });
        },

        _onRowClick: function(event, rowElement) {
            if(!$(event.target).is(':input,:button,a')) {
                var row = $(rowElement),
                    selected = row.hasClass('ui-state-highlight'),
                    metaKey = event.metaKey||event.ctrlKey,
                    shiftKey = event.shiftKey;

                //unselect a selected row if metakey is on
                if(selected && metaKey) {
                    this.unselectRow(row, false);  // Changed for AngularPrime
                }
                else {
                    //unselect previous selection if this is single selection or multiple one with no keys
                    if(this._isSingleSelection() || (this._isMultipleSelection() && !metaKey && !shiftKey)) {
                        this.unselectAllRows();
                    }

                    this.selectRow(row, false);  // Changed for AngularPrime
                }

                PUI.clearSelection();
            }
        },

        _isSingleSelection: function() {
            return this.options.selectionMode === 'single';
        },

        _isMultipleSelection: function() {
            return this.options.selectionMode === 'multiple';
        },

        unselectAllRows: function(silent) { // Changed for AngularPrime
            this.tbody.children('tr.ui-state-highlight').removeClass('ui-state-highlight').attr('aria-selected', false);
            this.selection = [];

            // added for AngularPrime
            if(!silent) {
                this._trigger('unselectAllRows');
            }
        },

        unselectRow: function(row, silent) {
            var rowIndex = this._getRowIndex(row);
            row.removeClass('ui-state-highlight').attr('aria-selected', false);

            this._removeSelection(rowIndex);

            if(!silent) {
                this._trigger('rowUnselect', null, this.data[rowIndex]);
            }
        },

        selectRow: function(row, silent) {
            var rowIndex = this._getRowIndex(row);
            row.removeClass('ui-state-hover').addClass('ui-state-highlight').attr('aria-selected', true);

            this._addSelection(rowIndex);

            if(!silent) {
                this._trigger('rowSelect', null, this.data[rowIndex]);
            }
        },

        _removeSelection: function(rowIndex) {
            this.selection = $.grep(this.selection, function(value) {
                return value !== rowIndex;
            });
        },

        _addSelection: function(rowIndex) {
            if(!this._isSelected(rowIndex)) {
                this.selection.push(rowIndex);
            }
        },

        _isSelected: function(rowIndex) {
            return PUI.inArray(this.selection, rowIndex);
        },

        _getRowIndex: function(row) {
            var index = row.index();

            return this.options.paginator ? this.getFirst() + index : index;
        },

        // Added for AngularPrime
        selectRowByIndex: function(rowIndex) {
            this.selectRow($( this.tbody[0].children[rowIndex] ), true);
        },

        getData: function() {
            return this.data;
        },

        getPaginator: function() {
            return this.paginator;
        }
    });
});;/*globals $ */

/**
 * PrimeUI Paginator Widget
 */
$(function() {
    "use strict"; // added for AngularPrime

    var ElementHandlers = { // Changed for AngularPrime

        '{FirstPageLink}': {
            markup: '<span class="pui-paginator-first pui-paginator-element ui-state-default ui-corner-all"><span class="ui-icon ui-icon-seek-first">p</span></span>',

            create: function(paginator) {
                var element = $(this.markup);

                if(paginator.options.page === 0) {
                    element.addClass('ui-state-disabled');
                }

                element.on('click.puipaginator', function() {
                    if(!$(this).hasClass("ui-state-disabled")) {
                        paginator.option('page', 0);
                    }
                });

                return element;
            },

            update: function(element, state) {
                if(state.page === 0)
                    element.addClass('ui-state-disabled').removeClass('ui-state-hover ui-state-active');
                else
                    element.removeClass('ui-state-disabled');
            }
        },

        '{PreviousPageLink}': {
            markup: '<span class="pui-paginator-prev pui-paginator-element ui-state-default ui-corner-all"><span class="ui-icon ui-icon-seek-prev">p</span></span>',

            create: function(paginator) {
                var element = $(this.markup);

                if(paginator.options.page === 0) {
                    element.addClass('ui-state-disabled');
                }

                element.on('click.puipaginator', function() {
                    if(!$(this).hasClass("ui-state-disabled")) {
                        paginator.option('page', paginator.options.page - 1);
                    }
                });

                return element;
            },

            update: function(element, state) {
                if(state.page === 0)
                    element.addClass('ui-state-disabled').removeClass('ui-state-hover ui-state-active');
                else
                    element.removeClass('ui-state-disabled');
            }
        },

        '{NextPageLink}': {
            markup: '<span class="pui-paginator-next pui-paginator-element ui-state-default ui-corner-all"><span class="ui-icon ui-icon-seek-next">p</span></span>',

            create: function(paginator) {
                var element = $(this.markup);

                if(paginator.options.page === (paginator.getPageCount() - 1)) {
                    element.addClass('ui-state-disabled').removeClass('ui-state-hover ui-state-active');
                }

                element.on('click.puipaginator', function() {
                    if(!$(this).hasClass("ui-state-disabled")) {
                        paginator.option('page', paginator.options.page + 1);
                    }
                });

                return element;
            },

            update: function(element, state) {
                if(state.page === (state.pageCount - 1))
                    element.addClass('ui-state-disabled').removeClass('ui-state-hover ui-state-active');
                else
                    element.removeClass('ui-state-disabled');
            }
        },

        '{LastPageLink}': {
            markup: '<span class="pui-paginator-last pui-paginator-element ui-state-default ui-corner-all"><span class="ui-icon ui-icon-seek-end">p</span></span>',

            create: function(paginator) {
                var element = $(this.markup);

                if(paginator.options.page === (paginator.getPageCount() - 1)) {
                    element.addClass('ui-state-disabled').removeClass('ui-state-hover ui-state-active');
                }

                element.on('click.puipaginator', function() {
                    if(!$(this).hasClass("ui-state-disabled")) {
                        paginator.option('page', paginator.getPageCount() - 1);
                    }
                });

                return element;
            },

            update: function(element, state) {
                if(state.page === (state.pageCount - 1))
                    element.addClass('ui-state-disabled').removeClass('ui-state-hover ui-state-active');
                else
                    element.removeClass('ui-state-disabled');
            }
        },

        '{PageLinks}': {
            markup: '<span class="pui-paginator-pages"></span>',

            create: function(paginator) {
                var element = $(this.markup),
                    boundaries = this.calculateBoundaries({
                        page: paginator.options.page,
                        pageLinks: paginator.options.pageLinks,
                        pageCount: paginator.getPageCount()  // Changed for AngularPrime
                    }),
                    start = boundaries[0],
                    end = boundaries[1];

                for(var i = start; i <= end; i++) {
                    var pageLinkNumber = (i + 1),
                        pageLinkElement = $('<span class="pui-paginator-page pui-paginator-element ui-state-default ui-corner-all">' + pageLinkNumber + "</span>");

                    if(i === paginator.options.page) {
                        pageLinkElement.addClass('ui-state-active');
                    }

                    pageLinkElement.on('click.puipaginator', function(e){
                        var link = $(this);

                        if(!link.hasClass('ui-state-disabled')&&!link.hasClass('ui-state-active')) {
                            paginator.option('page', parseInt(link.text(), 10) - 1); // Changed for AngularPrime
                        }
                    });

                    element.append(pageLinkElement);
                }

                return element;
            },

            update: function(element, state) {
                var pageLinks = element.children(),
                    boundaries = this.calculateBoundaries({
                        page: state.page,
                        pageLinks: state.pageLinks,
                        pageCount: state.pageCount // Changed for AngularPrime
                    }),
                    start = boundaries[0],
                    end = boundaries[1],
                    p = 0;

                pageLinks.filter('.ui-state-active').removeClass('ui-state-active');

                for(var i = start; i <= end; i++) {
                    var pageLinkNumber = (i + 1),
                        pageLink = pageLinks.eq(p);

                    if(i === state.page) {
                        pageLink.addClass('ui-state-active');
                    }

                    pageLink.text(pageLinkNumber);

                    p++;
                }
            },

            calculateBoundaries: function(config) {
                var page = config.page,
                    pageLinks = config.pageLinks,
                    pageCount = config.pageCount,
                    visiblePages = Math.min(pageLinks, pageCount);

                //calculate range, keep current in middle if necessary
                var start = Math.max(0, parseInt(Math.ceil(page - ((visiblePages) / 2)), 10)), // Changed for AngularPrime
                    end = Math.min(pageCount - 1, start + visiblePages - 1);

                //check when approaching to last page
                var delta = pageLinks - (end - start + 1);
                start = Math.max(0, start - delta);

                return [start, end];
            }
        }

    };

    $.widget("primeui.puipaginator", {

        options: {
            pageLinks: 5,
            totalRecords: 0,
            page: 0,
            rows: 0,
            template: '{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}'
        },

        _create: function() {
            this.element.addClass('pui-paginator ui-widget-header');
            this.paginatorElements = [];

            var elementKeys = this.options.template.split(/[ ]+/);
            for(var i = 0; i < elementKeys.length;i++) {
                var elementKey = elementKeys[i],
                    handler = ElementHandlers[elementKey];

                if(handler) {
                    var paginatorElement = handler.create(this);
                    this.paginatorElements[elementKey] = paginatorElement;
                    this.element.append(paginatorElement);
                }
            }

            this._bindEvents();
        },

        _bindEvents: function() {
            this.element.find('span.pui-paginator-element')
                .on('mouseover.puipaginator', function() {
                    var el = $(this);
                    if(!el.hasClass('ui-state-active')&&!el.hasClass('ui-state-disabled')) {
                        el.addClass('ui-state-hover');
                    }
                })
                .on('mouseout.puipaginator', function() {
                    var el = $(this);
                    if(el.hasClass('ui-state-hover')) {
                        el.removeClass('ui-state-hover');
                    }
                });
        },

        _setOption: function(key, value) {
            if(key === 'page') {
                this.setPage(value);  // Changed for AngularPrime
            }
            else {
                $.Widget.prototype._setOption.apply(this, arguments);
            }
        },

        setPage: function(p) {  // Changed for AngularPrime
            var pc = this.getPageCount();

            if(p >= 0 && p < pc && this.options.page !== p) {
                var newState = {
                    first: this.options.rows * p,
                    rows: this.options.rows,
                    page: p,
                    pageCount: pc,
                    pageLinks: this.options.pageLinks
                };

                this.options.page = p;
                this._updateUI(newState);
                this._trigger('paginate', null, newState);
            }
        },

        _updateUI: function(newState) {
            for(var paginatorElementKey in this.paginatorElements) {
                ElementHandlers[paginatorElementKey].update(this.paginatorElements[paginatorElementKey], newState);
            }
        },

        getPageCount: function() {
            return Math.ceil(this.options.totalRecords / this.options.rows)||1;
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiDialog', function () {
        return {
            restrict: 'A',
            compile: function (element, attrs) {
                return function postLink (scope, element, attrs) {
                    // TODO check if no inline object created.
                    var options = scope.$eval(attrs.puiDialog) || {};
                    if (!(typeof options.dlgVisible == 'boolean')) {
                        throw new Error('The options object ' + attrs.puiDialog + ' needs a boolean property dlgVisible');
                    }
                    options.draggable = (options.draggable !== undefined) ? options.draggable : true;
                    options.modal = (options.modal !== undefined) ? options.modal : true;
                    options.closable = (options.closable !== undefined) ? options.closable : true;
                    options.location = options.location || 'center';
                    options.height = options.height || 'auto';
                    options.width = options.width || '300px';
                    options.minWidth = options.minWidth || 150;
                    options.minHeight = options.minHeight || 25;
                    options.resizable = (options.resizable !== undefined) ? options.resizable : false;
                    options.showEffect = options.showEffect || 'fade';
                    options.hideEffect = options.hideEffect || 'fade';
                    options.effectSpeed = options.effectSpeed || 'normal';
                    options.closeOnEscape = (options.closeOnEscape !== undefined) ? options.closeOnEscape : true;
                    options.minimizable = (options.minimizable !== undefined) ? options.minimizable : false;
                    options.maximizable = (options.maximizable !== undefined) ? options.maximizable : false;

                    $(function () {
                        element.puidialog({
                            draggable: options.draggable,
                            resizable: options.resizable,
                            location: options.location,
                            minWidth: options.minWidth,
                            minHeight: options.minHeight,
                            height: options.height,
                            width: options.width,
                            modal: options.modal,
                            showEffect: options.showEffect,
                            hideEffect: options.hideEffect,
                            effectSpeed: options.effectSpeed,
                            closeOnEscape: options.closeOnEscape,
                            closable: options.closable,
                            minimizable: options.minimizable,
                            maximizable: options.maximizable
                        });
                    });
                    scope.$watch(attrs.puiDialog + '.dlgVisible', function (value) {
                        if (value === false) {
                            $(function () {
                                element.puidialog('hide');
                            });
                        } else {
                            $(function () {
                                element.puidialog('show');
                            });

                        }
                    });
                    // required  when you close the dialog with the close icon.
                    element.bind("puidialogafterhide", function () {
                        scope.$apply(function () {
                            scope[attrs.puiDialog].dlgVisible = false;
                        });
                    });
                };
            }
        };

    }

);

}());
;/*jshint laxcomma:true*/
/*globals $ document PUI window*/

/**
 * PrimeUI Dialog Widget
 */
$(function() {
    "use strict"; // added for AngularPrime

    $.widget("primeui.puidialog", {

        options: {
            draggable: true,
            resizable: true,
            location: 'center',
            minWidth: 150,
            minHeight: 25,
            height: 'auto',
            width: '300px',
            visible: false,
            modal: false,
            showEffect: null,
            hideEffect: null,
            effectOptions: {},
            effectSpeed: 'normal',
            closeOnEscape: true,
            rtl: false,
            closable: true,
            minimizable: false,
            maximizable: false,
            appendTo: null,
            buttons: null
        },

        _create: function() {
            //container
            this.element.addClass('pui-dialog ui-widget ui-widget-content ui-helper-hidden ui-corner-all pui-shadow')
                .contents().wrapAll('<div class="pui-dialog-content ui-widget-content" />');

            //header
            this.element.prepend('<div class="pui-dialog-titlebar ui-widget-header ui-helper-clearfix ui-corner-top">' +
                    '<span id="' + this.element.attr('id') + '_label" class="pui-dialog-title">' + this.element.attr('title') + '</span>')
                .removeAttr('title');

            //footer
            if(this.options.buttons) {
                this.footer = $('<div class="pui-dialog-buttonpane ui-widget-content ui-helper-clearfix"></div>').appendTo(this.element);
                for(var i = 0; i < this.options.buttons.length; i++) {
                    var buttonMeta = this.options.buttons[i],
                        button = $('<button type="button"></button>').appendTo(this.footer);
                    if(buttonMeta.text) {
                        button.text(buttonMeta.text);
                    }

                    button.puibutton(buttonMeta);
                }
            }

            if(this.options.rtl) {
                this.element.addClass('pui-dialog-rtl');
            }

            //elements
            this.content = this.element.children('.pui-dialog-content');
            this.titlebar = this.element.children('.pui-dialog-titlebar');

            if(this.options.closable) {
                this._renderHeaderIcon('pui-dialog-titlebar-close', 'ui-icon-close');
            }

            if(this.options.minimizable) {
                this._renderHeaderIcon('pui-dialog-titlebar-maximize', 'ui-icon-extlink');
            }

            if(this.options.minimizable) {
                this._renderHeaderIcon('pui-dialog-titlebar-minimize', 'ui-icon-minus');
            }

            //icons
            this.icons = this.titlebar.children('.pui-dialog-titlebar-icon');
            this.closeIcon = this.titlebar.children('.pui-dialog-titlebar-close');
            this.minimizeIcon = this.titlebar.children('.pui-dialog-titlebar-minimize');
            this.maximizeIcon = this.titlebar.children('.pui-dialog-titlebar-maximize');

            this.blockEvents = 'focus.puidialog mousedown.puidialog mouseup.puidialog keydown.puidialog keyup.puidialog';
            this.parent = this.element.parent();

            //size
            this.element.css({'width': this.options.width, 'height': 'auto'});
            this.content.height(this.options.height);

            //events
            this._bindEvents();

            if(this.options.draggable) {
                this._setupDraggable();
            }

            if(this.options.resizable){
                this._setupResizable();
            }

            if(this.options.appendTo){
                this.element.appendTo(this.options.appendTo);
            }

            //docking zone
            if($(document.body).children('.pui-dialog-docking-zone').length === 0) {
                $(document.body).append('<div class="pui-dialog-docking-zone"></div>');
            }

            //aria
            this._applyARIA();

            if(this.options.visible){
                this.show();
            }
        },

        _renderHeaderIcon: function(styleClass, icon) {
            this.titlebar.append('<a class="pui-dialog-titlebar-icon ' + styleClass + ' ui-corner-all" href="#" role="button">' +
                '<span class="ui-icon ' + icon + '"></span></a>');
        },

        _enableModality: function() {
            var $this = this,
                doc = $(document);

            this.modality = $('<div id="' + this.element.attr('id') + '_modal" class="ui-widget-overlay"></div>').appendTo(document.body)
                .css({
                    'width' : doc.width(),
                    'height' : doc.height(),
                    'z-index' : this.element.css('z-index') - 1
                });

            //Disable tabbing out of modal dialog and stop events from targets outside of dialog
            doc.bind('keydown.puidialog',
                function(event) {
                    if(event.keyCode == $.ui.keyCode.TAB) {
                        var tabbables = $this.content.find(':tabbable'),
                            first = tabbables.filter(':first'),
                            last = tabbables.filter(':last');

                        if(event.target === last[0] && !event.shiftKey) {
                            first.focus(1);
                            return false;
                        }
                        else if (event.target === first[0] && event.shiftKey) {
                            last.focus(1);
                            return false;
                        }
                    }
                })
                .bind(this.blockEvents, function(event) {
                    if ($(event.target).zIndex() < $this.element.zIndex()) {
                        return false;
                    }
                });
        },

        _disableModality: function(){
            this.modality.remove();
            this.modality = null;
            $(document).unbind(this.blockEvents).unbind('keydown.dialog');
        },

        show: function() {
            if(this.element.is(':visible')) {
                return;
            }

            if(!this.positionInitialized) {
                this._initPosition();
            }

            this._trigger('beforeShow', null);

            if(this.options.showEffect) {
                var $this = this;

                this.element.show(this.options.showEffect, this.options.effectOptions, this.options.effectSpeed, function() {
                    $this._postShow();
                });
            }
            else {
                this.element.show();

                this._postShow();
            }

            this._moveToTop();

            if(this.options.modal) {
                this._enableModality();
            }
        },

        _postShow: function() {
            //execute user defined callback
            this._trigger('afterShow', null);

            this.element.attr({
                'aria-hidden': false
                ,'aria-live': 'polite'
            });

            this._applyFocus();
        },

        hide: function() {
            if(this.element.is(':hidden')) {
                return;
            }

            this._trigger('beforeHide', null);

            if(this.options.hideEffect) {
                var _self = this;

                this.element.hide(this.options.hideEffect, this.options.effectOptions, this.options.effectSpeed, function() {
                    _self._postHide();
                });
            }
            else {
                this.element.hide();

                this._postHide();
            }

            if(this.options.modal) {
                this._disableModality();
            }
        },

        _postHide: function() {
            //execute user defined callback
            this._trigger('afterHide', null);

            this.element.attr({
                'aria-hidden': true
                ,'aria-live': 'off'
            });
        },

        _applyFocus: function() {
            this.element.find(':not(:submit):not(:button):input:visible:enabled:first').focus();
        },

        _bindEvents: function() {
            var $this = this;

            this.icons.mouseover(function() {
                $(this).addClass('ui-state-hover');
            }).mouseout(function() {
                    $(this).removeClass('ui-state-hover');
                });

            this.closeIcon.on('click.puidialog', function(e) {
                $this.hide();
                e.preventDefault();
            });

            this.maximizeIcon.click(function(e) {
                $this.toggleMaximize();
                e.preventDefault();
            });

            this.minimizeIcon.click(function(e) {
                $this.toggleMinimize();
                e.preventDefault();
            });

            if(this.options.closeOnEscape) {
                $(document).on('keydown.dialog_' + this.element.attr('id'), function(e) {
                    var keyCode = $.ui.keyCode,
                        active = parseInt($this.element.css('z-index'), 10) === PUI.zindex;

                    if(e.which === keyCode.ESCAPE && active && $this.element.is(':visible')) { // Changed for AngularPrime
                        $this.hide();
                    }
                });
            }

            if(this.options.modal) {
                $(window).on('resize.puidialog', function() {
                    $(document.body).children('.ui-widget-overlay').css({
                        'width': $(document).width()
                        ,'height': $(document).height()
                    });
                });
            }
        },

        _setupDraggable: function() {
            this.element.draggable({
                cancel: '.pui-dialog-content, .pui-dialog-titlebar-close',
                handle: '.pui-dialog-titlebar',
                containment : 'document'
            });
        },

        _setupResizable: function() {
            this.element.resizable({
                minWidth : this.options.minWidth,
                minHeight : this.options.minHeight,
                alsoResize : this.content,
                containment: 'document'
            });

            this.resizers = this.element.children('.ui-resizable-handle');
        },

        _initPosition: function() {
            //reset
            this.element.css({left:0,top:0});

            if(/(center|left|top|right|bottom)/.test(this.options.location)) {
                this.options.location = this.options.location.replace(',', ' ');

                this.element.position({
                    my: 'center'
                    ,at: this.options.location
                    ,collision: 'fit'
                    ,of: window
                    //make sure dialog stays in viewport
                    ,using: function(pos) {
                        var l = pos.left < 0 ? 0 : pos.left,
                            t = pos.top < 0 ? 0 : pos.top;

                        $(this).css({
                            left: l
                            ,top: t
                        });
                    }
                });
            }
            else {
                var coords = this.options.position.split(','),
                    x = $.trim(coords[0]),
                    y = $.trim(coords[1]);

                this.element.offset({
                    left: x
                    ,top: y
                });
            }

            this.positionInitialized = true;
        },

        _moveToTop: function() {
            this.element.css('z-index',++PUI.zindex);
        },

        toggleMaximize: function() {
            if(this.minimized) {
                this.toggleMinimize();
            }

            if(this.maximized) {
                this.element.removeClass('pui-dialog-maximized');
                this._restoreState();

                this.maximizeIcon.removeClass('ui-state-hover').children('.ui-icon').removeClass('ui-icon-newwin').addClass('ui-icon-extlink');
                this.maximized = false;
            }
            else {
                this._saveState();

                var win = $(window);

                this.element.addClass('pui-dialog-maximized').css({
                    'width': win.width() - 6
                    ,'height': win.height()
                }).offset({
                        top: win.scrollTop()
                        ,left: win.scrollLeft()
                    });

                //maximize content
                this.content.css({
                    width: 'auto',
                    height: 'auto'
                });

                this.maximizeIcon.removeClass('ui-state-hover').children('.ui-icon').removeClass('ui-icon-extlink').addClass('ui-icon-newwin');
                this.maximized = true;
                this._trigger('maximize');
            }
        },

        toggleMinimize: function() {
            var animate = true,
                dockingZone = $(document.body).children('.pui-dialog-docking-zone');

            if(this.maximized) {
                this.toggleMaximize();
                animate = false;
            }

            var $this = this;

            if(this.minimized) {
                this.element.appendTo(this.parent).removeClass('pui-dialog-minimized').css({'position':'fixed', 'float':'none'});
                this._restoreState();
                this.content.show();
                this.minimizeIcon.removeClass('ui-state-hover').children('.ui-icon').removeClass('ui-icon-plus').addClass('ui-icon-minus');
                this.minimized = false;

                if(this.options.resizable) {
                    this.resizers.show();
                }

                if(this.footer) {
                    this.footer.show();
                }
            }
            else {
                this._saveState();

                if(animate) {
                    this.element.effect('transfer', {
                            to: dockingZone
                            ,className: 'pui-dialog-minimizing'
                        }, 500,
                        function() {
                            $this._dock(dockingZone);
                            $this.element.addClass('pui-dialog-minimized');
                        });
                }
                else {
                    this._dock(dockingZone);
                }
            }
        },

        _dock: function(zone) {
            this.element.appendTo(zone).css('position', 'static');
            this.element.css({'height':'auto', 'width':'auto', 'float': 'left'});
            this.content.hide();
            this.minimizeIcon.removeClass('ui-state-hover').children('.ui-icon').removeClass('ui-icon-minus').addClass('ui-icon-plus');
            this.minimized = true;

            if(this.options.resizable) {
                this.resizers.hide();
            }

            if(this.footer) {
                this.footer.hide();
            }

            zone.css('z-index',++PUI.zindex);

            this._trigger('minimize');
        },

        _saveState: function() {
            this.state = {
                width: this.element.width()
                ,height: this.element.height()
            };

            var win = $(window);
            this.state.offset = this.element.offset();
            this.state.windowScrollLeft = win.scrollLeft();
            this.state.windowScrollTop = win.scrollTop();
        },

        _restoreState: function() {
            this.element.width(this.state.width).height(this.state.height);

            var win = $(window);
            this.element.offset({
                top: this.state.offset.top + (win.scrollTop() - this.state.windowScrollTop)
                ,left: this.state.offset.left + (win.scrollLeft() - this.state.windowScrollLeft)
            });
        },

        _applyARIA: function() {
            this.element.attr({
                'role': 'dialog'
                ,'aria-labelledby': this.element.attr('id') + '_title'
                ,'aria-hidden': !this.options.visible
            });

            this.titlebar.children('a.pui-dialog-titlebar-icon').attr('role', 'button');
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiDropdown', ['$parse', function ($parse) {
        return {
            restrict: 'A',
            require: '?ngModel', // get a hold of NgModelController
            compile: function (element, attrs) {
                return function postLink (scope, element, attrs, ngModel) {

                    if (attrs.ngOptions) {
                        // This builds the Option tags
                        ngModel.$render();
                        // Remove the null option added by angular
                        var firstChild = element.children()[0];
                        if (firstChild.text === '' && firstChild.value === '?') {
                            element[0].removeChild(firstChild);
                        }

                    }

                    var options = scope.$eval(attrs.puiDropdown) || {};

                    options.editable = (options.editable !== undefined) ? options.editable : false;
                    if (options.editable) {
                        // TODO Give warning when explicit useLabel set to False as not compatible with editable
                        options.useLabel = true;
                    }
                    options.useLabel = (options.useLabel !== undefined) ? options.useLabel : false;
                    options.filter = (options.filter !== undefined) ? options.filter : false;
                    options.effect = options.effect || 'fade';
                    options.effectSpeed = options.effectSpeed || 'normal';
                    options.filterMatchMode = options.filterMatchMode || 'startsWith';
                    options.caseSensitiveFilter = (options.caseSensitiveFilter !== undefined) ? options.caseSensitiveFilter : false;
                    options.scrollHeight = options.scrollHeight || 200;


                    $(function () {
                        element.puidropdown({
                            editable: options.editable ,
                            filter : options.filter ,
                            effect: options.effect ,
                            effectSpeed: options.effectSpeed ,
                            filterMatchMode: options.filterMatchMode ,
                            caseSensitiveFilter: options.caseSensitiveFilter ,
                            filterFunction: options.filterFunction ,
                            scrollHeight: options.scrollHeight
                        });
                    });

                    // Specify how UI should be updated
                    ngModel.$render = function () {
                        if (options.useLabel) {
                            element.puidropdown('selectValue', ngModel.$viewValue);
                        } else {
                            element.puidropdown('selectIndex', ngModel.$viewValue);
                        }
                    };

                    // Listen for change events to enable binding
                    element.bind('puidropdownchange', function () {
                        scope.safeApply(read());
                        if (options.callback) {
                            var idx = element.puidropdown('getSelectedValue'),
                                label = element.puidropdown('getCustomInputVal'); // This also works when not editable
                            options.callback(idx, label);
                        }
                    });

                    read(); // initialize

                    // Write data to the model
                    function read () {
                        var sel;
                        if (options.editable) {
                            sel = element.puidropdown('getCustomInputVal');
                        } else {
                            if (options.useLabel) {
                                sel = element.puidropdown('getSelectedLabel');

                            } else {
                                sel = element.puidropdown('getSelectedValue');
                            }
                        }
                        ngModel.$setViewValue(sel);
                    }

                    if (attrs.ngDisabled) {
                        scope.$watch(attrs.ngDisabled, function (value) {

                            if (value === false) {
                                $(function () {
                                    element.puidropdown('enable');
                                });
                            } else {
                                $(function () {
                                    element.puidropdown('disable');

                                });

                            }
                        });
                    }


                };
            }
        };

    }]

);

}());
;/*jshint laxcomma:true*/
/*globals $ document PUI window*/

/**
 * PrimeUI dropdown widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puidropdown", {

        options: {
            effect: 'fade',
            effectSpeed: 'normal',
            filter: false,
            filterMatchMode: 'startsWith',
            caseSensitiveFilter: false,
            filterFunction: null,
            source: null,
            content: null,
            scrollHeight: 200
        },

        _create: function() {
            if(this.options.source) {
                for(var i = 0; i < this.options.source.length; i++) {
                    var choice = this.options.source[i];
                    if(choice.label)
                        this.element.append('<option value="' + choice.value + '">' + choice.label + '</option>');
                    else
                        this.element.append('<option value="' + choice + '">' + choice + '</option>');
                }
            }

            this.element.wrap('<div class="pui-dropdown ui-widget ui-state-default ui-corner-all ui-helper-clearfix" />')
                .wrap('<div class="ui-helper-hidden-accessible" />');
            this.container = this.element.closest('.pui-dropdown');
            this.focusElementContainer = $('<div class="ui-helper-hidden-accessible"><input type="text" /></div>').appendTo(this.container);
            this.focusElement = this.focusElementContainer.children('input');
            this.label = this.options.editable ? $('<input type="text" class="pui-dropdown-label pui-inputtext ui-corner-all"">')
                : $('<label class="pui-dropdown-label pui-inputtext ui-corner-all"/>');
            this.label.appendTo(this.container);
            this.menuIcon = $('<div class="pui-dropdown-trigger ui-state-default ui-corner-right"><span class="ui-icon ui-icon-triangle-1-s"></span></div>')
                .appendTo(this.container);
            this.panel = $('<div class="pui-dropdown-panel ui-widget-content ui-corner-all ui-helper-hidden pui-shadow" />').appendTo(document.body);
            this.itemsWrapper = $('<div class="pui-dropdown-items-wrapper" />').appendTo(this.panel);
            this.itemsContainer = $('<ul class="pui-dropdown-items pui-dropdown-list ui-widget-content ui-widget ui-corner-all ui-helper-reset"></ul>')
                .appendTo(this.itemsWrapper);
            this.disabled = this.element.prop('disabled');
            this.choices = this.element.children('option');
            this.optGroupsSize = this.itemsContainer.children('li.puiselectonemenu-item-group').length;

            if(this.options.filter) {
                this.filterContainer = $('<div class="pui-dropdown-filter-container" />').prependTo(this.panel);
                this.filterInput = $('<input type="text" autocomplete="off" class="pui-dropdown-filter pui-inputtext ui-widget ui-state-default ui-corner-all" />')
                    .appendTo(this.filterContainer);
                this.filterContainer.append('<span class="ui-icon ui-icon-search"></span>');
            }

            this._generateItems();

            var $this = this,
                selectedOption = this.choices.filter(':selected');

            //disable options
            this.choices.filter(':disabled').each(function() {
                $this.items.eq($(this).index()).addClass('ui-state-disabled');
            });

            //triggers
            this.triggers = this.options.editable ? this.menuIcon : this.container.children('.pui-dropdown-trigger, .pui-dropdown-label');

            //activate selected
            if(this.options.editable) {
                var customInputVal = this.label.val();

                //predefined input
                if(customInputVal === selectedOption.text()) {
                    this._highlightItem(this.items.eq(selectedOption.index()));
                }
                //custom input
                else {
                    this.items.eq(0).addClass('ui-state-highlight');
                    this.customInput = true;
                    this.customInputVal = customInputVal;
                }
            }
            else {
                this._highlightItem(this.items.eq(selectedOption.index()));
            }

            if(!this.disabled) {
                this._bindEvents();
                this._bindConstantEvents();
            }

            this._initDimensions();
        },

        _generateItems: function() {
            for(var i = 0; i < this.choices.length; i++) {
                var option = this.choices.eq(i),
                    optionLabel = option.text(),
                    content = this.options.content ? this.options.content.call(this, this.options.source[i]) : optionLabel;

                this.itemsContainer.append('<li data-label="' + optionLabel + '" class="pui-dropdown-item pui-dropdown-list-item ui-corner-all">' + content + '</li>');
            }

            this.items = this.itemsContainer.children('.pui-dropdown-item');
        },

        _bindEvents: function() {
            var $this = this;

            this.items.filter(':not(.ui-state-disabled)').on('mouseover.puidropdown', function() {
                var el = $(this);

                if(!el.hasClass('ui-state-highlight'))
                    $(this).addClass('ui-state-hover');
            })
                .on('mouseout.puidropdown', function() {
                    $(this).removeClass('ui-state-hover');
                })
                .on('click.puidropdown', function() {
                    $this._selectItem($(this));
                });

            this.triggers.on('mouseenter.puidropdown', function() {
                if(!$this.container.hasClass('ui-state-focus')) {
                    $this.container.addClass('ui-state-hover');
                    $this.menuIcon.addClass('ui-state-hover');
                }
            })
                .on('mouseleave.puidropdown', function() {
                    $this.container.removeClass('ui-state-hover');
                    $this.menuIcon.removeClass('ui-state-hover');
                })
                .on('click.puidropdown', function(e) {
                    if($this.panel.is(":hidden")) {
                        $this._show();
                    }
                    else {
                        $this._hide();

                        $this._revert();
                    }

                    $this.container.removeClass('ui-state-hover');
                    $this.menuIcon.removeClass('ui-state-hover');
                    $this.focusElement.trigger('focus.puidropdown');
                    e.preventDefault();
                });

            this.focusElement.on('focus.puidropdown', function() {
                $this.container.addClass('ui-state-focus');
                $this.menuIcon.addClass('ui-state-focus');
            })
                .on('blur.puidropdown', function() {
                    $this.container.removeClass('ui-state-focus');
                    $this.menuIcon.removeClass('ui-state-focus');
                });

            if(this.options.editable) {
                this.label.on('change.pui-dropdown', function() {
                    //$this._triggerChange(true);  Moved for AngularPrime
                    $this.customInput = true;
                    $this.customInputVal = $(this).val();
                    $this._triggerChange(true); // new place for AngularPrime
                    $this.items.filter('.ui-state-highlight').removeClass('ui-state-highlight');
                    $this.items.eq(0).addClass('ui-state-highlight');
                });
            }

            this._bindKeyEvents();

            if(this.options.filter) {
                this._setupFilterMatcher();

                this.filterInput.puiinputtext();

                this.filterInput.on('keyup.pui-dropdown', function() {
                    $this._filter($(this).val());
                });
            }
        },

        _bindConstantEvents: function() {
            var $this = this;

            $(document.body).bind('mousedown.pui-dropdown', function (e) {
                if($this.panel.is(":hidden")) {
                    return;
                }

                var offset = $this.panel.offset();
                if (e.target === $this.label.get(0) ||
                    e.target === $this.menuIcon.get(0) ||
                    e.target === $this.menuIcon.children().get(0)) {
                    return;
                }

                if (e.pageX < offset.left ||
                    e.pageX > offset.left + $this.panel.width() ||
                    e.pageY < offset.top ||
                    e.pageY > offset.top + $this.panel.height()) {

                    $this._hide();
                    $this._revert();
                }
            });

            this.resizeNS = 'resize.' + this.id;
            this._unbindResize();
            this._bindResize();
        },

        _bindKeyEvents: function() {
            var $this = this;

            this.focusElement.on('keydown.puiselectonemenu', function(e) {
                var keyCode = $.ui.keyCode,
                    key = e.which;

                switch(key) {
                    case keyCode.UP:
                    case keyCode.LEFT:
                        var activeItem = $this._getActiveItem(),
                            prev = activeItem.prevAll(':not(.ui-state-disabled,.ui-selectonemenu-item-group):first');

                        if(prev.length == 1) {
                            if($this.panel.is(':hidden')) {
                                $this._selectItem(prev);
                            }
                            else {
                                $this._highlightItem(prev);
                                PUI.scrollInView($this.itemsWrapper, prev);
                            }
                        }

                        e.preventDefault();
                        break;

                    case keyCode.DOWN:
                    case keyCode.RIGHT:
                        var activeItem = $this._getActiveItem(),
                            next = activeItem.nextAll(':not(.ui-state-disabled,.ui-selectonemenu-item-group):first');

                        if(next.length == 1) {
                            if($this.panel.is(':hidden')) {
                                if(e.altKey) {
                                    $this._show();
                                } else {
                                    $this._selectItem(next);
                                }
                            }
                            else {
                                $this._highlightItem(next);
                                PUI.scrollInView($this.itemsWrapper, next);
                            }
                        }

                        e.preventDefault();
                        break;

                    case keyCode.ENTER:
                    case keyCode.NUMPAD_ENTER:
                        if($this.panel.is(':hidden')) {
                            $this._show();
                        }
                        else {
                            $this._selectItem($this._getActiveItem());
                        }

                        e.preventDefault();
                        break;

                    case keyCode.TAB:
                        if($this.panel.is(':visible')) {
                            $this._revert();
                            $this._hide();
                        }
                        break;

                    case keyCode.ESCAPE:
                        if($this.panel.is(':visible')) {
                            $this._revert();
                            $this._hide();
                        }
                        break;

                    default:
                        var k = String.fromCharCode((96 <= key && key <= 105)? key-48 : key),
                            currentItem = $this.items.filter('.ui-state-highlight');

                        //Search items forward from current to end and on no result, search from start until current
                        var highlightItem = $this._search(k, currentItem.index() + 1, $this.options.length);
                        if(!highlightItem) {
                            highlightItem = $this._search(k, 0, currentItem.index());
                        }

                        if(highlightItem) {
                            if($this.panel.is(':hidden')) {
                                $this._selectItem(highlightItem);
                            }
                            else {
                                $this._highlightItem(highlightItem);
                                PUI.scrollInView($this.itemsWrapper, highlightItem);
                            }
                        }

                        break;
                }
            });
        },

        _initDimensions: function() {
            var userStyle = this.element.attr('style');

            //do not adjust width of container if there is user width defined
            if(!userStyle||userStyle.indexOf('width') == -1) {
                this.container.width(this.element.outerWidth(true) + 5);
            }

            //width of label
            this.label.width(this.container.width() - this.menuIcon.width());

            //align panel and container
            var jqWidth = this.container.innerWidth();
            if(this.panel.outerWidth() < jqWidth) {
                this.panel.width(jqWidth);
            }

            this.element.parent().addClass('ui-helper-hidden').removeClass('ui-helper-hidden-accessible');

            if(this.options.scrollHeight && this.panel.outerHeight() > this.options.scrollHeight) {
                this.itemsWrapper.height(this.options.scrollHeight);
            }
        },

        _selectItem: function(item, silent) {
            var selectedOption = this.choices.eq(this._resolveItemIndex(item)),
                currentOption = this.choices.filter(':selected'),
                sameOption = selectedOption.val() == currentOption.val(),
                shouldChange = null;

            if(this.options.editable) {
                shouldChange = (!sameOption)||(selectedOption.text() != this.label.val());
            }
            else {
                shouldChange = !sameOption;
            }

            if(shouldChange) {
                this._highlightItem(item);
                this.element.val(selectedOption.val());

                // this._triggerChange();  Moved for AngularPrime

                if(this.options.editable) {
                    this.customInput = false;
                }

                this._triggerChange();  // New location for AngularPrime
            }

            if(!silent) {
                this.focusElement.trigger('focus.puidropdown');
            }

            if(this.panel.is(':visible')) {
                this._hide();
            }
        },

        _highlightItem: function(item) {
            this.items.filter('.ui-state-highlight').removeClass('ui-state-highlight');
            item.addClass('ui-state-highlight');

            this._setLabel(item.data('label'));
        },

        _triggerChange: function(edited) {
            //this.changed = false; Removed for AngularPrime

            // if(this.options.change) { Removed for AngularPrime
                this._trigger('change');
            // }

            if(!edited) {
                this.value = this.choices.filter(':selected').val();
            }
        },

        _resolveItemIndex: function(item) {
            if(this.optGroupsSize === 0)
                return item.index();
            else
                return item.index() - item.prevAll('li.pui-dropdown-item-group').length;
        },

        _setLabel: function(value) {
            if(this.options.editable) {
                this.label.val(value);
            }
            else {
                if(value === '&nbsp;')
                    this.label.html('&nbsp;');
                else
                    this.label.text(value);
            }
        },

        _bindResize: function() {
            var $this = this;

            $(window).bind(this.resizeNS, function(e) {
                if($this.panel.is(':visible')) {
                    $this._alignPanel();
                }
            });
        },

        _unbindResize: function() {
            $(window).unbind(this.resizeNS);
        },

        _unbindEvents: function() {
            this.items.off();
            this.triggers.off();
            // this.input.off(); Is undefined and not set in this file
            // this.focusInput.off(); Does not exists, should be removed (AngularPrime)
            this.label.off();
        },

        _alignPanel: function() {
            this.panel.css({left:'', top:''}).position({
                my: 'left top'
                ,at: 'left bottom'
                ,of: this.container
            });
        },

        _show: function() {
            this._alignPanel();

            this.panel.css('z-index', ++PUI.zindex);

            if(this.options.effect != 'none')
                this.panel.show(this.options.effect, {}, this.options.effectSpeed);
            else
                this.panel.show();

            this.preShowValue = this.choices.filter(':selected');
        },

        _hide: function() {
            this.panel.hide();
        },

        _revert: function() {
            if(this.options.editable && this.customInput) {
                this._setLabel(this.customInputVal);
                this.items.filter('.ui-state-active').removeClass('ui-state-active');
                this.items.eq(0).addClass('ui-state-active');
            }
            else {
                this._highlightItem(this.items.eq(this.preShowValue.index()));
            }
        },

        _getActiveItem: function() {
            return this.items.filter('.ui-state-highlight');
        },

        _setupFilterMatcher: function() {
            this.filterMatchers = {
                'startsWith': this._startsWithFilter
                ,'contains': this._containsFilter
                ,'endsWith': this._endsWithFilter
                ,'custom': this.options.filterFunction
            };

            this.filterMatcher = this.filterMatchers[this.options.filterMatchMode];
        },

        _startsWithFilter: function(value, filter) {
            return value.indexOf(filter) === 0;
        },

        _containsFilter: function(value, filter) {
            return value.indexOf(filter) !== -1;
        },

        _endsWithFilter: function(value, filter) {
            return value.indexOf(filter, value.length - filter.length) !== -1;
        },

        _filter: function(value) {
            this.initialHeight = this.initialHeight||this.itemsWrapper.height();
            var filterValue = this.options.caseSensitiveFilter ? $.trim(value) : $.trim(value).toLowerCase();

            if(filterValue === '') {
                this.items.filter(':hidden').show();
            }
            else {
                for(var i = 0; i < this.choices.length; i++) {
                    var option = this.choices.eq(i),
                        itemLabel = this.options.caseSensitiveFilter ? option.text() : option.text().toLowerCase(),
                        item = this.items.eq(i);

                    if(this.filterMatcher(itemLabel, filterValue))
                        item.show();
                    else
                        item.hide();
                }
            }

            if(this.itemsContainer.height() < this.initialHeight) {
                this.itemsWrapper.css('height', 'auto');
            }
            else {
                this.itemsWrapper.height(this.initialHeight);
            }
        },

        _search: function(text, start, end) {
            for(var i = start; i  < end; i++) {
                var option = this.choices.eq(i);

                if(option.text().indexOf(text) === 0) {
                    return this.items.eq(i);
                }
            }

            return null;
        },

        getSelectedValue: function() {
            return this.element.val();
        },

        getSelectedLabel: function() {
            return this.choices.filter(':selected').text();
        },

        selectValue : function(value) {
            var option = this.choices.filter('[value="' + value + '"]');

            this._selectItem(this.items.eq(option.index()), true);
        },

        // Added for AngularPrime
        getCustomInputVal: function() {
            return this.customInput === true ? this.customInputVal : this.getSelectedLabel();
        },

        disable: function() {
            this._unbindEvents();
            this.label.addClass('ui-state-disabled');
            this.menuIcon.addClass('ui-state-disabled');
        },

        enable: function() {
            this._bindEvents();
            //this._bindConstantEvents();  Because they are never unbinded
            this.label.removeClass('ui-state-disabled');
            this.menuIcon.removeClass('ui-state-disabled');
        },

        selectIndex : function(idx) {
            this._selectItem(this.items.eq(idx), true);
        }
    });

});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiFieldset', function () {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            return function postLink (scope, element, attrs) {
                $(function () {

                    var options = scope.$eval(attrs.puiFieldset) || {};
                    var toggleable = options.collapsed !== undefined;
                    options.toggleDuration = options.toggleDuration || 'normal';
                    element.puifieldset({
                        toggleable: toggleable,
                        toggleDuration: options.toggleDuration,
                        collapsed: options.collapsed
                    });

                    if (toggleable) {

                        if (attrs.puiFieldset.trim().charAt(0) !== '{') {
                            scope.$watch(attrs.puiFieldset + '.collapsed', function (value) {
                                if (value === true) {
                                    element.puifieldset('collapse');
                                } else {
                                    element.puifieldset('expand');
                                }

                            });
                        }
                    }
                    if (options.callback) {
                        // TODO Warning when toggleable === false
                        element.bind('puifieldsetaftertoggle', function () {
                            options.callback();
                        });
                    }

                });
            };
        }
    };
});

}());
;/*globals $ */

/**
 * PrimeFaces Fieldset Widget
 */
$(function() {
    "use strict"; // Added for AngularPrime
    $.widget("primeui.puifieldset", {

        options: {
            toggleable: false,
            toggleDuration: 'normal',
            collapsed: false
        },

        _create: function() {
            this.element.addClass('pui-fieldset ui-widget ui-widget-content ui-corner-all').
                children('legend').addClass('pui-fieldset-legend ui-corner-all ui-state-default');

            this.element.contents(':not(legend)').wrapAll('<div class="pui-fieldset-content" />');

            this.legend = this.element.children('legend.pui-fieldset-legend');
            this.content = this.element.children('div.pui-fieldset-content');

            this.legend.prependTo(this.element);

            if(this.options.toggleable) {
                this.element.addClass('pui-fieldset-toggleable');
                this.toggler = $('<span class="pui-fieldset-toggler ui-icon" />').prependTo(this.legend);

                this._bindEvents();

                if(this.options.collapsed) {
                    this.content.hide();
                    this.toggler.addClass('ui-icon-plusthick');
                } else {
                    this.toggler.addClass('ui-icon-minusthick');
                }
            }
        },

        _bindEvents: function() {
            var $this = this;

            this.legend.on('click.puifieldset', function(e) {$this.toggle(e);})
                .on('mouseover.puifieldset', function() {$this.legend.addClass('ui-state-hover');})
                .on('mouseout.puifieldset', function() {$this.legend.removeClass('ui-state-hover ui-state-active');})
                .on('mousedown.puifieldset', function() {$this.legend.removeClass('ui-state-hover').addClass('ui-state-active');})
                .on('mouseup.puifieldset', function() {$this.legend.removeClass('ui-state-active').addClass('ui-state-hover');});
        },

        toggle: function(e) {
            var $this = this;

            this._trigger('beforeToggle', e);

            if(this.options.collapsed) {
                this.toggler.removeClass('ui-icon-plusthick').addClass('ui-icon-minusthick');
            } else {
                this.toggler.removeClass('ui-icon-minusthick').addClass('ui-icon-plusthick');
            }

            this.content.slideToggle(this.options.toggleSpeed, 'easeInOutCirc', function() {
                $this._trigger('afterToggle', e);
                $this.options.collapsed = !$this.options.collapsed;
            });
        },

        // added for AngularPrime
        collapse: function() {
            if (!this.options.collapsed) {
                this.toggle(null);
            }
        },

        expand: function() {
            if (this.options.collapsed) {
                this.toggle(null);
            }
        }

    });
});;/*globals angular $ */
(function () {
    "use strict";

angular.module('angular.prime').directive('puiGalleria', function () {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            return function postLink (scope, element, attrs) {
                var options = scope.$eval(attrs.puiGalleria) || {};
                $(function () {
                    element.puigalleria({
                        panelWidth: options.panelWidth,
                        panelHeight: options.panelHeight
                    });
                });


            };

        }
    };
});

}());
;/*globals $ window */

/**
 * PrimeUI Lightbox Widget
 */
$(function() {
    "use strict"; //Added for AngularPrime

    $.widget("primeui.puigalleria", {

        options: {
            panelWidth: 600,
            panelHeight: 400,
            frameWidth: 60,
            frameHeight: 40,
            activeIndex: 0,
            showFilmstrip: true,
            autoPlay: true,
            transitionInterval: 4000,
            effect: 'fade',
            effectSpeed: 250,
            effectOptions: {},
            showCaption: true,
            customContent: false
        },

        _create: function() {
            this.element.addClass('pui-galleria ui-widget ui-widget-content ui-corner-all');
            this.panelWrapper = this.element.children('ul');
            this.panelWrapper.addClass('pui-galleria-panel-wrapper');
            this.panels = this.panelWrapper.children('li');
            this.panels.addClass('pui-galleria-panel ui-helper-hidden');

            this.element.width(this.options.panelWidth);
            this.panelWrapper.width(this.options.panelWidth).height(this.options.panelHeight);
            this.panels.width(this.options.panelWidth).height(this.options.panelHeight);

            if(this.options.showFilmstrip) {
                this._renderStrip();
                this._bindEvents();
            }

            if(this.options.customContent) {
                this.panels.children('img').remove();
                this.panels.children('div').addClass('pui-galleria-panel-content');
            }

            //show first
            var activePanel = this.panels.eq(this.options.activeIndex);
            activePanel.removeClass('ui-helper-hidden');
            if(this.options.showCaption) {
                this._showCaption(activePanel);
            }

            this.element.css('visibility', 'visible');

            if(this.options.autoPlay) {
                this.startSlideshow();
            }
        },

        _renderStrip: function() {
            var frameStyle = 'style="width:' + this.options.frameWidth + "px;height:" + this.options.frameHeight + 'px;"';

            this.stripWrapper = $('<div class="pui-galleria-filmstrip-wrapper"></div>')
                .width(this.element.width() - 50)
                .height(this.options.frameHeight)
                .appendTo(this.element);

            this.strip = $('<ul class="pui-galleria-filmstrip"></div>').appendTo(this.stripWrapper);

            for(var i = 0; i < this.panels.length; i++) {
                var image = this.panels.eq(i).children('img'),
                    frameClass = (i == this.options.activeIndex) ? 'pui-galleria-frame pui-galleria-frame-active' : 'pui-galleria-frame',
                    frameMarkup = '<li class="'+ frameClass + '" ' + frameStyle + '>' +
                        '<div class="pui-galleria-frame-content" ' + frameStyle + '>' +
                        '<img src="' + image.attr('src') + '" class="pui-galleria-frame-image" ' + frameStyle + '/>' +
                        '</div></li>';

                this.strip.append(frameMarkup);
            }

            this.frames = this.strip.children('li.pui-galleria-frame');

            //navigators
            this.element.append('<div class="pui-galleria-nav-prev ui-icon ui-icon-circle-triangle-w" style="bottom:' + (this.options.frameHeight / 2) + 'px"></div>' +
                '<div class="pui-galleria-nav-next ui-icon ui-icon-circle-triangle-e" style="bottom:' + (this.options.frameHeight / 2) + 'px"></div>');

            //caption
            if(this.options.showCaption) {
                this.caption = $('<div class="pui-galleria-caption"></div>').css({
                    'bottom': this.stripWrapper.outerHeight(true),
                    'width': this.panelWrapper.width()
                }).appendTo(this.element);
            }
        },

        _bindEvents: function() {
            var $this = this;

            this.element.children('div.pui-galleria-nav-prev').on('click.puigalleria', function() {
                if($this.slideshowActive) {
                    $this.stopSlideshow();
                }

                if(!$this.isAnimating()) {
                    $this.prev();
                }
            });

            this.element.children('div.pui-galleria-nav-next').on('click.puigalleria', function() {
                if($this.slideshowActive) {
                    $this.stopSlideshow();
                }

                if(!$this.isAnimating()) {
                    $this.next();
                }
            });

            this.strip.children('li.pui-galleria-frame').on('click.puigalleria', function() {
                if($this.slideshowActive) {
                    $this.stopSlideshow();
                }

                $this.select($(this).index(), false);
            });
        },

        startSlideshow: function() {
            var $this = this;

            this.interval = window.setInterval(function() {
                $this.next();
            }, this.options.transitionInterval);

            this.slideshowActive = true;
        },

        stopSlideshow: function() {
            window.clearInterval(this.interval);

            this.slideshowActive = false;
        },

        isSlideshowActive: function() {
            return this.slideshowActive;
        },

        select: function(index, reposition) {
            if(index !== this.options.activeIndex) {
                if(this.options.showCaption) {
                    this._hideCaption();
                }

                var oldPanel = this.panels.eq(this.options.activeIndex),
                    oldFrame = this.frames.eq(this.options.activeIndex),
                    newPanel = this.panels.eq(index),
                    newFrame = this.frames.eq(index);

                //content
                oldPanel.hide(this.options.effect, this.options.effectOptions, this.options.effectSpeed);
                newPanel.show(this.options.effect, this.options.effectOptions, this.options.effectSpeed);

                //frame
                oldFrame.removeClass('pui-galleria-frame-active').css('opacity', '');
                newFrame.animate({opacity:1.0}, this.options.effectSpeed, null, function() {
                    $(this).addClass('pui-galleria-frame-active');
                });

                //caption
                if(this.options.showCaption) {
                    this._showCaption(newPanel);
                }

                //viewport
                if(reposition === undefined || reposition === true) {
                    var frameLeft = newFrame.position().left,
                        stepFactor = this.options.frameWidth + parseInt(newFrame.css('margin-right'), 10),
                        stripLeft = this.strip.position().left,
                        frameViewportLeft = frameLeft + stripLeft,
                        frameViewportRight = frameViewportLeft + this.options.frameWidth;

                    if(frameViewportRight > this.stripWrapper.width()) {
                        this.strip.animate({left: '-=' + stepFactor}, this.options.effectSpeed, 'easeInOutCirc');
                    } else if(frameViewportLeft < 0) {
                        this.strip.animate({left: '+=' + stepFactor}, this.options.effectSpeed, 'easeInOutCirc');
                    }
                }

                this.options.activeIndex = index;
            }
        },

        _hideCaption: function() {
            this.caption.slideUp(this.options.effectSpeed);
        },

        _showCaption: function(panel) {
            var image = panel.children('img');
            this.caption.html('<h4>' + image.attr('title') + '</h4><p>' + image.attr('alt') + '</p>').slideDown(this.options.effectSpeed);
        },

        prev: function() {
            if(this.options.activeIndex !== 0) {
                this.select(this.options.activeIndex - 1);
            }
        },

        next: function() {
            if(this.options.activeIndex !== (this.panels.length - 1)) {
                this.select(this.options.activeIndex + 1);
            }
            else {
                this.select(0, false);
                this.strip.animate({left: 0}, this.options.effectSpeed, 'easeInOutCirc');
            }
        },

        isAnimating: function() {
            return this.strip.is(':animated');
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

    angular.module('angular.prime').factory('puiGrowl', function () {

        var growl = {};
        var options = {
            sticky: false,
            life: 3000
        };

        var growlElement;

        var initializeGrowl = function () {
            if (growlElement === undefined) {
                $(function () {
                    growlElement = $('#growl');
                    if (growlElement.length === 1 ) {
                        growlElement.puigrowl();
                    } else {
                        if (growlElement.length === 0) {
                            $('body').append('<div id="growl"></div>');
                            growlElement = $('#growl');
                            growlElement.puigrowl();
                        } else {
                            throw "Growl needs a exactly 1 div with id 'growl'";
                        }
                    }
                });
            }
        };

        growl.showInfoMessage = function (title, msg) {
            initializeGrowl();
            growlElement.puigrowl('show', [
                {severity: 'info', summary: title, detail: msg}
            ]);
        };

        growl.showWarnMessage = function (title, msg) {
            initializeGrowl();
            growlElement.puigrowl('show', [
                {severity: 'warn', summary: title, detail: msg}
            ]);
        };

        growl.showErrorMessage = function (title, msg) {
            initializeGrowl();
            growlElement.puigrowl('show', [
                {severity: 'error', summary: title, detail: msg}
            ]);
        };

        growl.setSticky = function(sticky) {
            if ( typeof sticky !== 'boolean') {
                throw new Error('Only boolean allowed as parameter of setSticky function');
            }
            options.sticky = sticky;
            initializeGrowl();
            growlElement.puigrowl('setOptions', options);
        };

        growl.setStickyRememberOption = function() {
            options.previousStickyValue = options.sticky;
            this.setSticky(true);
        };

        growl.resetStickyOption = function() {
            this.setSticky(options.previousStickyValue);
        };

        growl.setLife = function(time) {
            if ( typeof time !== 'int') {
                throw new Error('Only int allowed as parameter of setSticky function');
            }
            options.life = time;
            initializeGrowl();
            growlElement.puigrowl('setOptions', options);
        };

        growl.clear = function() {
            initializeGrowl();
            growlElement.puigrowl('clear');

        };

        return growl;

    });

}());

;/*globals $ window document*/

/**
 * PrimeFaces Growl Widget
 */
$(function() {
    "use strict"; // Added for AngularPrime
    $.widget("primeui.puigrowl", {

        options: {
            sticky: false,
            life: 3000
        },

        _create: function() {
            var container = this.element;

            container.addClass("pui-growl ui-widget").appendTo(document.body);
        },

        show: function(msgs) {
            var $this = this;

            //this.jq.css('z-index', ++PrimeFaces.zindex);

            //this.clear();  Changed for AngularPrime

            $.each(msgs, function(i, msg) {
                $this._renderMessage(msg);
            });
        },

        clear: function() {
            this.element.children('div.pui-growl-item-container').remove();
        },

        _renderMessage: function(msg) {
            var markup = '<div class="pui-growl-item-container ui-state-highlight ui-corner-all ui-helper-hidden" aria-live="polite">';
            markup += '<div class="pui-growl-item pui-shadow">';
            markup += '<div class="pui-growl-icon-close ui-icon ui-icon-closethick" style="display:none"></div>';
            markup += '<span class="pui-growl-image pui-growl-image-' + msg.severity + '" />';
            markup += '<div class="pui-growl-message">';
            markup += '<span class="pui-growl-title">' + msg.summary + '</span>';
            markup += '<p>' + msg.detail + '</p>';
            markup += '</div><div style="clear: both;"></div></div></div>';

            var message = $(markup);

            this._bindMessageEvents(message);
            message.appendTo(this.element).fadeIn();
        },

        _removeMessage: function(message) {
            message.fadeTo('normal', 0, function() {
                message.slideUp('normal', 'easeInOutCirc', function() {
                    message.remove();
                });
            });
        },

        _bindMessageEvents: function(message) {
            var $this = this,
                sticky = this.options.sticky;

            message.on('mouseover.puigrowl', function() {
                var msg = $(this);

                if(!msg.is(':animated')) {
                    msg.find('div.pui-growl-icon-close:first').show();
                }
            })
                .on('mouseout.puigrowl', function() {
                    $(this).find('div.pui-growl-icon-close:first').hide();
                });

            //remove message on click of close icon
            message.find('div.pui-growl-icon-close').on('click.puigrowl',function() {
                $this._removeMessage(message);

                if(!sticky) {
                    window.clearTimeout(message.data('timeout'));
                }
            });

            if(!sticky) {
                this._setRemovalTimeout(message);
            }
        },

        _setRemovalTimeout: function(message) {
            var $this = this;

            var timeout = window.setTimeout(function() {
                $this._removeMessage(message);
            }, this.options.life);

            message.data('timeout', timeout);
        },

        // Added for AngularPrime
        setOptions: function(newOptions) {
          this.options = newOptions;
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').factory('puiInput.helper', function () {

    var puiInputHelper = {};

    puiInputHelper.handleAttrubutes = function (element, attrs, handledAttributes, attrsToRemove) {
        var contents = '';
        for (var property in attrs) {
            if (attrs.hasOwnProperty(property) && property.substring(0, 1) !== '$') {
                if (handledAttributes.indexOf(property) === -1) {
                    // attrs.$attr[property] is the original name of the attribute on the element
                    contents += attrs.$attr[property] + '="' + attrs[property] + '" ';
                }
                if (attrsToRemove.indexOf(property) !== -1) {
                    element.removeAttr(attrs.$attr[property]);
                }
            }

        }
        return contents;
    };

    puiInputHelper.defineLabel = function(id, label, prefix) {
        var contents = '';

        contents += '<label id="'+ prefix + id + '"';
        contents += 'for="' + id + '"';
        contents += '>'+label;
        contents += '</label>';

        return contents;
    };

    return puiInputHelper;
});


angular.module('angular.prime').directive('puiInput', function () {
    return {
        restrict: 'A',
        require: '?ngModel', // get a hold of NgModelController
        link: function (scope, element, attrs, ngModel) {
            if (!ngModel) {
                return;
            } // do nothing if no ng-model

            if (attrs.type === 'range') {
                return;
                // When input type range, theming has not much sense.
            }
            var htmlElementName = element[0].nodeName;
            $(function () {
                var checkbox = false;
                var radiobutton = false;
                var password = false;
                var options = scope.$eval(attrs.puiInput) || {};
                var helper = {};

                if ('INPUT' === htmlElementName) {
                    if (attrs.type === 'password') {
                        options.inline = (options.inline !== undefined) ? options.inline : false;
                        element.puipassword({
                            inline: options.inline,
                            promptLabel: options.promptLabel || 'Please enter a password',
                            weakLabel: options.weakLabel || 'Weak',
                            goodLabel: options.goodLabel || 'Medium',
                            strongLabel: options.strongLabel || 'Strong'
                        });
                        password = true;
                    }
                    if (attrs.type === 'checkbox') {
                        element.puicheckbox();
                        if (attrs.checked) {
                            scope.safeApply(function () {
                                ngModel.$setViewValue(true);
                            });
                        }
                        checkbox = true;
                    }
                    if (attrs.type === 'radio') {
                        element.puiradiobutton();

                        radiobutton = true;
                    }
                    if (!checkbox && !radiobutton && !password) {
                        element.puiinputtext();
                    }

                }
                if ('TEXTAREA' === htmlElementName) {
                    var autoComplete = attrs.puiAutocomplete;
                    var completeSourceMethod = scope.$eval(attrs.puiAutocomplete);

                    options.autoResize = (options.autoResize !== undefined) ? options.autoResize : false;
                    element.puiinputtextarea({
                        autoResize: options.autoResize,
                        autoComplete: autoComplete,
                        scrollHeight: options.scrollHeight || 150,
                        completeSource: completeSourceMethod,
                        minQueryLength: options.minQueryLength || 3,
                        queryDelay: options.queryDelay || 700,
                        counter: $(options.display),
                        counterTemplate: options.template,
                        maxlength: options.maxLength
                    });

                    if (options.display) {
                        // At this moment, we don't have the scope value yet on the element
                        scope.$watch(ngModel.$viewValue, function (value) {
                            element.puiinputtextarea('updateCounter');
                        });
                    }

                }
                if (checkbox) {
                    // Write data to the model
                    helper = {
                        read: function () {
                            $(function () {
                                var checked = element.puicheckbox('isChecked');
                                var viewValue = element.val();
                                if (!checked) {
                                    viewValue = null;
                                }
                                scope.safeApply(function () {
                                    ngModel.$setViewValue(viewValue);
                                });
                            });

                        }
                    };

                    // Specify how UI should be updated
                    ngModel.$render = function () {
                        $(function () {
                            if (ngModel.$viewValue) {
                                element.puicheckbox('check', true, true);
                            } else {
                                element.puicheckbox('uncheck', true, true);
                            }

                        });

                    };


                    // Listen for change events to enable binding
                    element.bind('puicheckboxchange', function () {
                        helper.read();
                    });

                }

                if (radiobutton) {
                    // Write data to the model
                    helper = {
                        read: function () {
                            $(function () {
                                var checked = element.puiradiobutton('isChecked');
                                var viewValue = element.val();
                                if (checked) {
                                    scope.safeApply(function () {
                                        ngModel.$setViewValue(viewValue);
                                    });
                                }
                            });

                        }
                    };

                    // Specify how UI should be updated

                    ngModel.$render = function () {
                        $(function () {
                            if (ngModel.$viewValue == element.val() && !element.puiradiobutton('isChecked')) {
                                element.trigger('click');
                            }
                        });
                    };

                    // Listen for change events to enable binding
                    element.bind('puiradiobuttonchange', function () {
                        helper.read();
                    });

                }

                if (attrs.ngDisabled) {
                    scope.$watch(attrs.ngDisabled, function (value) {

                        if (value === false) {
                            $(function () {
                                if (checkbox) {
                                    element.puicheckbox('enable');
                                } else {
                                    if (radiobutton) {
                                        element.puiradiobutton('enable');
                                    } else {
                                        element.puiinputtext('enable');
                                    }
                                }
                            });
                        } else {
                            $(function () {
                                if (checkbox) {
                                    element.puicheckbox('disable');
                                } else {
                                    if (radiobutton) {
                                        element.puiradiobutton('disable');
                                    } else {
                                        element.puiinputtext('disable');
                                    }
                                }

                            });

                        }
                    });
                }
            });
        }
    };
});

angular.module('angular.prime').directive('puiCheckbox', ['$compile', '$parse', 'puiInput.helper', 'angular.prime.config',
                                                function ($compile, $parse,  puiInputHelper, angularPrimeConfig) {

    return {
        restrict: 'EA',
        priority: 1005,
        compile: function (element, attrs) {

            return function postLink(scope, element, attrs) {
                var id = attrs.id,
                    label = '',
                    contents = '<input type="checkbox" pui-input ',
                    handledAttributes = 'id ngModel puiInput ngShow ngHide puiCheckbox'.split(' '),
                    attrsToRemove = 'id ngModel puiInput'.split(' ');

                try {
                    $parse(attrs.puiCheckbox); // see if it is a valid AngularExpression
                    label = scope.$eval(attrs.puiCheckbox) || attrs.puiCheckbox;
                } catch (e) {
                    label = attrs.puiCheckbox;
                }

                contents += 'id="' + id + '"';
                contents += 'ng-model="' + attrs.ngModel + '" ';

                contents += puiInputHelper.handleAttrubutes(element, attrs, handledAttributes, attrsToRemove, contents);

                contents += ' />';

                contents += puiInputHelper.defineLabel(id, label, angularPrimeConfig.labelPrefix);

                element.html(contents);


                $compile(element.contents())(scope);

            };
        }
    };
}]);

angular.module('angular.prime').directive('puiRadiobutton', ['$compile', '$parse', 'puiInput.helper', 'angular.prime.config',
                                                    function ($compile, $parse, puiInputHelper, angularPrimeConfig) {

    return {
        restrict: 'EA',
        priority: 1005,
        compile: function (element, attrs) {

            return function postLink(scope, element, attrs) {
                var id = attrs.id,
                    label = '',
                    contents = '<input type="radio" pui-input ',
                    handledAttributes = 'id ngModel puiInput ngShow ngHide puiRadiobutton name value'.split(' '),
                    attrsToRemove = 'id ngModel puiInput'.split(' ');

                try {
                    $parse(attrs.puiRadiobutton); // see if it is a valid AngularExpression
                    label = scope.$eval(attrs.puiRadiobutton) || attrs.puiRadiobutton;
                } catch (e) {
                    label = attrs.puiRadiobutton;
                }

                contents += 'id="' + id + '"';
                contents += 'ng-model="' + attrs.ngModel + '" ';
                contents += 'name="' + attrs.name + '" ';
                contents += 'value="' + attrs.value + '" ';

                contents += puiInputHelper.handleAttrubutes(element, attrs, handledAttributes, attrsToRemove, contents);

                contents += ' />';

                contents += puiInputHelper.defineLabel(id, label, angularPrimeConfig.labelPrefix);

                element.html(contents);

                $compile(element.contents())(scope);

            };
        }
    };
}]);

}());

;/*globals $ */

/**
 * PrimeUI checkbox widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puicheckbox", {

        _create: function() {
            this.element.wrap('<div class="pui-chkbox ui-widget"><div class="ui-helper-hidden-accessible"></div></div>');
            this.container = this.element.parent().parent();
            this.box = $('<div class="pui-chkbox-box ui-widget ui-corner-all ui-state-default">').appendTo(this.container);
            this.icon = $('<span class="pui-chkbox-icon pui-c"></span>').appendTo(this.box);
            this.disabled = this.element.prop('disabled');
            this.label = $('label[for="' + this.element.attr('id') + '"]');

            if(this.element.prop('checked')) {
                this.box.addClass('ui-state-active');
                this.icon.addClass('ui-icon ui-icon-check');
            }

            if(this.disabled) {
                this.box.addClass('ui-state-disabled');
            } else {
                this._bindEvents();
            }
        },

        _bindEvents: function() {
            var $this = this;

            this.box.on('mouseover.puicheckbox', function() {
                if(!$this.isChecked())
                    $this.box.addClass('ui-state-hover');
            })
                .on('mouseout.puicheckbox', function() {
                    $this.box.removeClass('ui-state-hover');
                })
                .on('click.puicheckbox', function() {
                    $this.toggle();
                });

            this.element.focus(function() {
                if($this.isChecked()) {
                    $this.box.removeClass('ui-state-active');
                }

                $this.box.addClass('ui-state-focus');
            })
                .blur(function() {
                    if($this.isChecked()) {
                        $this.box.addClass('ui-state-active');
                    }

                    $this.box.removeClass('ui-state-focus');
                })
                .keydown(function(e) {
                    var keyCode = $.ui.keyCode;
                    if(e.which == keyCode.SPACE) {
                        e.preventDefault();
                    }
                })
                .keyup(function(e) {
                    var keyCode = $.ui.keyCode;
                    if(e.which == keyCode.SPACE) {
                        $this.toggle(true);

                        e.preventDefault();
                    }
                });

            this.label.on('click.puicheckbox', function(e) {
                $this.toggle();
                e.preventDefault();
            });
        },

        toggle: function(keypress) {
            if(this.isChecked()) {
                this.uncheck(keypress);
            } else {
                this.check(keypress);
            }

            this._trigger('change', null, this.isChecked());
        },

        isChecked: function() {
            return this.element.prop('checked');
        },

        check: function(activate, silent) {
            if(!this.isChecked()) {
                this.element.prop('checked', true);
                this.icon.addClass('ui-icon ui-icon-check');

                if(!activate) {
                    this.box.addClass('ui-state-active');
                }

                if(!silent) {
                    this.element.trigger('change');
                }
            }
        },

        uncheck: function() {
            if(this.isChecked()) {
                this.element.prop('checked', false);
                this.box.removeClass('ui-state-active');
                this.icon.removeClass('ui-icon ui-icon-check');

                this.element.trigger('change');
            }
        },

        // Added for AngularPrime
        _unbindEvents: function() {
            this.box.off();
            this.element.focus(function() {

            })
            .blur(function() {

            })
            .keydown(function(e) {

            })
            .keyup(function(e) {

            });

            this.label.off();
        },

        disable: function() {
            var input = this.box;

            input.attr('aria-disabled', input.prop('disabled'));
            input.addClass('ui-state-disabled').removeClass('ui-state-hover');
            this._unbindEvents();
        },

        enable: function() {

            this.box.attr('aria-disabled', this.element.prop('disabled'));
            this.box.removeClass('ui-state-disabled');
            this._bindEvents();
        }
    });

});;/*globals $ */

/**
 * PrimeUI inputtext widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puiinputtext", {

        _create: function() {
            var input = this.element,
                disabled = input.prop('disabled');

            //visuals
            input.addClass('pui-inputtext ui-widget ui-state-default ui-corner-all');

            if(disabled) {
                input.addClass('ui-state-disabled');
            }
            else {
                this._enableMouseEffects(input); // Added For AngularPrime
                /*
                 wrapped in method For AngularPrime
                input.hover(function() {
                    input.toggleClass('ui-state-hover');
                }).focus(function() {
                        input.addClass('ui-state-focus');
                    }).blur(function() {
                        input.removeClass('ui-state-focus');
                    });
                */
            }

            //aria
            input.attr('role', 'textbox').attr('aria-disabled', disabled)
                .attr('aria-readonly', input.prop('readonly'))
                .attr('aria-multiline', input.is('textarea'));
        },

        _destroy: function() {

        },

        // Added for AngularPrime
        _enableMouseEffects: function () {
            var input = this.element;
            input.hover(function () {
                input.toggleClass('ui-state-hover');
            }).focus(function () {
                    input.addClass('ui-state-focus');
                }).blur(function () {
                    input.removeClass('ui-state-focus');
                });
        },

        _disableMouseEffects: function () {
            var input = this.element;
            input.hover(function () {

            }).focus(function () {

                }).blur(function () {

                });
        },

        disable: function() {
            var input = this.element;

            input.attr('aria-disabled', input.prop('disabled'));
            input.addClass('ui-state-disabled');
            input.removeClass('ui-state-focus');
            input.removeClass('ui-state-hover');
            this._disableMouseEffects();
        },

        enable: function() {
            this.element.attr('aria-disabled', this.element.prop('disabled'));
            this.element.removeClass('ui-state-disabled');
            this._enableMouseEffects();
        }

    });

});;/*jshint laxcomma:true*/
/*globals $ document PUI window _self*/

/**
 * PrimeUI inputtextarea widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puiinputtextarea", {

        options: {
            autoResize: false
            ,autoComplete: false
            ,maxlength: null
            ,counter: null
            ,counterTemplate: '{0}'
            ,minQueryLength: 3
            ,queryDelay: 700
        },

        _create: function() {
            var $this = this;

            this.element.puiinputtext();

            if(this.options.autoResize) {
                this.options.rowsDefault = this.element.attr('rows');
                this.options.colsDefault = this.element.attr('cols');

                this.element.addClass('pui-inputtextarea-resizable');

                this.element.keyup(function() {
                    $this._resize();
                }).focus(function() {
                        $this._resize();
                    }).blur(function() {
                        $this._resize();
                    });
            }

            if(this.options.maxlength) {
                this.element.keyup(function(e) {
                    var value = $this.element.val(),
                        length = value.length;

                    if(length > $this.options.maxlength) {
                        $this.element.val(value.substr(0, $this.options.maxlength));
                    }

                    if($this.options.counter) {
                        $this.updateCounter(); // Changed for AngularPrime (visibility)
                    }
                });
            }

            if(this.options.counter) {
                this.updateCounter(); // Changed for AngularPrime (visibility)
            }

            if(this.options.autoComplete) {
                this._initAutoComplete();
            }
        },

        updateCounter: function() {  // Changed for AngularPrime (visibility)
            var value = this.element.val(),
                length = value.length;

            if(this.options.counter) {
                var remaining = this.options.maxlength - length,
                    remainingText = this.options.counterTemplate.replace('{0}', remaining);

                this.options.counter.text(remainingText);
            }
        },

        _resize: function() {
            var linesCount = 0,
                lines = this.element.val().split('\n');

            for(var i = lines.length-1; i >= 0 ; --i) {
                linesCount += Math.floor((lines[i].length / this.options.colsDefault) + 1);
            }

            var newRows = (linesCount >= this.options.rowsDefault) ? (linesCount + 1) : this.options.rowsDefault;

            this.element.attr('rows', newRows);
        },


        _initAutoComplete: function() {
            var panelMarkup = '<div id="' + this.id + '_panel" class="pui-autocomplete-panel ui-widget-content ui-corner-all ui-helper-hidden ui-shadow"></div>',
                $this = this;

            this.panel = $(panelMarkup).appendTo(document.body);

            this.element.keyup(function(e) {
                var keyCode = $.ui.keyCode;

                switch(e.which) {

                    case keyCode.UP:
                    case keyCode.LEFT:
                    case keyCode.DOWN:
                    case keyCode.RIGHT:
                    case keyCode.ENTER:
                    case keyCode.NUMPAD_ENTER:
                    case keyCode.TAB:
                    case keyCode.SPACE:
                    case keyCode.CONTROL:
                    case keyCode.ALT:
                    case keyCode.ESCAPE:
                    case 224:   //mac command
                        //do not search
                        break;

                    default:
                        var query = $this._extractQuery();
                        if(query && query.length >= $this.options.minQueryLength) {

                            //Cancel the search request if user types within the timeout
                            if($this.timeout) {
                                $this._clearTimeout($this.timeout);
                            }

                            $this.timeout = window.setTimeout(function() {
                                $this.search(query);
                            }, $this.options.queryDelay);

                        }
                        break;
                }

            }).keydown(function(e) {
                    var overlayVisible = $this.panel.is(':visible'),
                        keyCode = $.ui.keyCode;

                    switch(e.which) {
                        case keyCode.UP:
                        case keyCode.LEFT:
                            if(overlayVisible) {
                                var highlightedItem = $this.items.filter('.ui-state-highlight'),
                                    prev = highlightedItem.length === 0 ? $this.items.eq(0) : highlightedItem.prev();

                                if(prev.length == 1) {
                                    highlightedItem.removeClass('ui-state-highlight');
                                    prev.addClass('ui-state-highlight');

                                    if($this.options.scrollHeight) {
                                        PUI.scrollInView($this.panel, prev);
                                    }
                                }

                                e.preventDefault();
                            }
                            else {
                                $this._clearTimeout();
                            }
                            break;

                        case keyCode.DOWN:
                        case keyCode.RIGHT:
                            if(overlayVisible) {
                                var highlightedItem = $this.items.filter('.ui-state-highlight'),
                                    next = highlightedItem.length === 0 ? _self.items.eq(0) : highlightedItem.next();

                                if(next.length == 1) {
                                    highlightedItem.removeClass('ui-state-highlight');
                                    next.addClass('ui-state-highlight');

                                    if($this.options.scrollHeight) {
                                        PUI.scrollInView($this.panel, next);
                                    }
                                }

                                e.preventDefault();
                            }
                            else {
                                $this._clearTimeout();
                            }
                            break;

                        case keyCode.ENTER:
                        case keyCode.NUMPAD_ENTER:
                            if(overlayVisible) {
                                $this.items.filter('.ui-state-highlight').trigger('click');

                                e.preventDefault();
                            }
                            else {
                                $this._clearTimeout();
                            }
                            break;

                        case keyCode.SPACE:
                        case keyCode.CONTROL:
                        case keyCode.ALT:
                        case keyCode.BACKSPACE:
                        case keyCode.ESCAPE:
                        case 224:   //mac command
                            $this._clearTimeout();

                            if(overlayVisible) {
                                $this._hide();
                            }
                            break;

                        case keyCode.TAB:
                            $this._clearTimeout();

                            if(overlayVisible) {
                                $this.items.filter('.ui-state-highlight').trigger('click');
                                $this._hide();
                            }
                            break;
                    }
                });

            //hide panel when outside is clicked
            $(document.body).bind('mousedown.puiinputtextarea', function (e) {
                if($this.panel.is(":hidden")) {
                    return;
                }
                var offset = $this.panel.offset();
                if(e.target === $this.element.get(0)) {
                    return;
                }

                if (e.pageX < offset.left ||
                    e.pageX > offset.left + $this.panel.width() ||
                    e.pageY < offset.top ||
                    e.pageY > offset.top + $this.panel.height()) {
                    $this._hide();
                }
            });

            //Hide overlay on resize
            var resizeNS = 'resize.' + this.id;
            $(window).unbind(resizeNS).bind(resizeNS, function() {
                if($this.panel.is(':visible')) {
                    $this._hide();
                }
            });
        },

        _bindDynamicEvents: function() {
            var $this = this;

            //visuals and click handler for items
            this.items.bind('mouseover', function() {
                var item = $(this);

                if(!item.hasClass('ui-state-highlight')) {
                    $this.items.filter('.ui-state-highlight').removeClass('ui-state-highlight');
                    item.addClass('ui-state-highlight');
                }
            })
                .bind('click', function(event) {
                    var item = $(this),
                        itemValue = item.attr('data-item-value'),
                        insertValue = itemValue.substring($this.query.length);

                    $this.element.focus();

                    $this.element.insertText(insertValue, $this.element.getSelection().start, true);

                    $this._hide();

                    $this._trigger("itemselect", event, item);
                });
        },

        _clearTimeout: function() {
            if(this.timeout) {
                window.clearTimeout(this.timeout);
            }

            this.timeout = null;
        },

        _extractQuery: function() {
            var end = this.element.getSelection().end,
                result = /\S+$/.exec(this.element.get(0).value.slice(0, end)),
                lastWord = result ? result[0] : null;

            return lastWord;
        },

        search: function(q) {
            this.query = q;

            var request = {
                query: q
            };

            if(this.options.completeSource) {
                this.options.completeSource.call(this, request, this._handleResponse);
            }
        },

        _handleResponse: function(data) {
            this.panel.html('');

            var listContainer = $('<ul class="pui-autocomplete-items pui-autocomplete-list ui-widget-content ui-widget ui-corner-all ui-helper-reset"></ul>');

            for(var i = 0; i < data.length; i++) {
                var item = $('<li class="pui-autocomplete-item pui-autocomplete-list-item ui-corner-all"></li>');
                item.attr('data-item-value', data[i].value);
                item.text(data[i].label);

                listContainer.append(item);
            }

            this.panel.append(listContainer).show();
            this.items = this.panel.find('.pui-autocomplete-item');

            this._bindDynamicEvents();

            if(this.items.length > 0) {
                //highlight first item
                this.items.eq(0).addClass('ui-state-highlight');

                //adjust height
                if(this.options.scrollHeight && this.panel.height() > this.options.scrollHeight) {
                    this.panel.height(this.options.scrollHeight);
                }

                if(this.panel.is(':hidden')) {
                    this._show();
                }
                else {
                    this._alignPanel(); //with new items
                }

            }
            else {
                this.panel.hide();
            }
        },

        _alignPanel: function() {
            var pos = this.element.getCaretPosition(),
                offset = this.element.offset();

            this.panel.css({
                'left': offset.left + pos.left,
                'top': offset.top + pos.top,
                'width': this.element.innerWidth()
            });
        },

        _show: function() {
            this._alignPanel();

            this.panel.show();
        },

        _hide: function() {
            this.panel.hide();
        }
    });

});;/*globals $ window PUI*/

/**
 * PrimeUI password widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puipassword", {

        options: {
            promptLabel: 'Please enter a password',
            weakLabel: 'Weak',
            goodLabel: 'Medium',
            strongLabel: 'Strong',
            inline: false
        },

        _create: function() {
            this.element.puiinputtext().addClass('pui-password');

            if(!this.element.prop(':disabled')) {
                var panelMarkup = '<div class="pui-password-panel ui-widget ui-state-highlight ui-corner-all ui-helper-hidden">';
                panelMarkup += '<div class="pui-password-meter" style="background-position:0pt 0pt">&nbsp;</div>';
                panelMarkup += '<div class="pui-password-info">' + this.options.promptLabel + '</div>';
                panelMarkup += '</div>';

                this.panel = $(panelMarkup).insertAfter(this.element);
                this.meter = this.panel.children('div.pui-password-meter');
                this.infoText = this.panel.children('div.pui-password-info');

                if(this.options.inline) {
                    this.panel.addClass('pui-password-panel-inline');
                } else {
                    this.panel.addClass('pui-password-panel-overlay').appendTo('body');
                }

                this._bindEvents();
            }
        },

        _destroy: function() {
            this.panel.remove();
        },

        _bindEvents: function() {
            var $this = this;

            this.element.on('focus.puipassword', function() {
                $this.show();
            })
                .on('blur.puipassword', function() {
                    $this.hide();
                })
                .on('keyup.puipassword', function() {
                    var value = $this.element.val(),
                        label = null,
                        meterPos = null;

                    if(value.length === 0) {
                        label = $this.options.promptLabel;
                        meterPos = '0px 0px';
                    }
                    else {
                        var score = $this._testStrength($this.element.val());

                        if(score < 30) {
                            label = $this.options.weakLabel;
                            meterPos = '0px -10px';
                        }
                        else if(score >= 30 && score < 80) {
                            label = $this.options.goodLabel;
                            meterPos = '0px -20px';
                        }
                        else if(score >= 80) {
                            label = $this.options.strongLabel;
                            meterPos = '0px -30px';
                        }
                    }

                    $this.meter.css('background-position', meterPos);
                    $this.infoText.text(label);
                });

            if(!this.options.inline) {
                var resizeNS = 'resize.' + this.element.attr('id');
                $(window).unbind(resizeNS).bind(resizeNS, function() {
                    if($this.panel.is(':visible')) {
                        $this.align();
                    }
                });
            }
        },

        _testStrength: function(str) {
            var grade = 0,
                val = 0,
                $this = this;

            val = str.match('[0-9]');
            grade += $this._normalize(val ? val.length : 1/4, 1) * 25;

            val = str.match('[a-zA-Z]');
            grade += $this._normalize(val ? val.length : 1/2, 3) * 10;

            val = str.match('[!@#$%^&*?_~.,;=]');
            grade += $this._normalize(val ? val.length : 1/6, 1) * 35;

            val = str.match('[A-Z]');
            grade += $this._normalize(val ? val.length : 1/6, 1) * 30;

            grade *= str.length / 8;

            return grade > 100 ? 100 : grade;
        },

        _normalize: function(x, y) {
            var diff = x - y;

            if(diff <= 0) {
                return x / y;
            }
            else {
                return 1 + 0.5 * (x / (x + y/4));
            }
        },

        align: function() {
            this.panel.css({
                left:'',
                top:'',
                'z-index': ++PUI.zindex
            })
                .position({
                    my: 'left top',
                    at: 'right top',
                    of: this.element
                });
        },

        show: function() {
            if(!this.options.inline) {
                this.align();

                this.panel.fadeIn();
            }
            else {
                this.panel.slideDown();
            }
        },

        hide: function() {
            if(this.options.inline)
                this.panel.slideUp();
            else
                this.panel.fadeOut();
        }
    });

});;/*globals $ */

/**
 * PrimeUI radiobutton widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    var checkedRadios = {};

    $.widget("primeui.puiradiobutton", {

        _create: function() {
            this.element.wrap('<div class="pui-radiobutton ui-widget"><div class="ui-helper-hidden-accessible"></div></div>');
            this.container = this.element.parent().parent();
            this.box = $('<div class="pui-radiobutton-box ui-widget pui-radiobutton-relative ui-state-default">').appendTo(this.container);
            this.icon = $('<span class="pui-radiobutton-icon pui-c"></span>').appendTo(this.box);
            this.disabled = this.element.prop('disabled');
            this.label = $('label[for="' + this.element.attr('id') + '"]');

            if(this.element.prop('checked')) {
                this.box.addClass('ui-state-active');
                this.icon.addClass('ui-icon ui-icon-bullet');
                checkedRadios[this.element.attr('name')] = this.box;
            }

            if(this.disabled) {
                this.box.addClass('ui-state-disabled');
            } else {
                this._bindEvents();
            }
        },

        _bindEvents: function() {
            var $this = this;

            this.box.on('mouseover.puiradiobutton', function() {
                if(!$this.isChecked())  // Changed for angularPrime (changed visibility)
                    $this.box.addClass('ui-state-hover');
            }).on('mouseout.puiradiobutton', function() {
                if(!$this.isChecked())  // Changed for angularPrime (changed visibility)
                        $this.box.removeClass('ui-state-hover');
                }).on('click.puiradiobutton', function() {
                if(!$this.isChecked()) {// Changed for angularPrime (changed visibility)
                        $this.element.trigger('click');

                        if($.browser.msie && parseInt($.browser.version, 10) < 9) {
                            $this.element.trigger('change');
                        }
                    }
                });

            if(this.label.length > 0) {
                this.label.on('click.puiradiobutton', function(e) {
                    $this.element.trigger('click');

                    e.preventDefault();
                });
            }

            this.element.focus(function() {
                if($this.isChecked()) { // Changed for angularPrime (changed visibility)
                    $this.box.removeClass('ui-state-active');
                }

                $this.box.addClass('ui-state-focus');
            })
                .blur(function() {
                if($this.isChecked()) { // Changed for angularPrime (changed visibility)
                        $this.box.addClass('ui-state-active');
                    }

                    $this.box.removeClass('ui-state-focus');
                })
                .change(function(e) {
                    var name = $this.element.attr('name');
                    if(checkedRadios[name]) {
                        checkedRadios[name].removeClass('ui-state-active ui-state-focus ui-state-hover').children('.pui-radiobutton-icon').removeClass('ui-icon ui-icon-bullet');
                    }

                    $this.icon.addClass('ui-icon ui-icon-bullet');
                    if(!$this.element.is(':focus')) {
                        $this.box.addClass('ui-state-active');
                    }

                    checkedRadios[name] = $this.box;

                    $this._trigger('change', null);
                });
        },

        isChecked: function() {  // Changed visibility for AngularPrime
            return this.element.prop('checked');
        },

        // Added for AngularPrime
        _unbindEvents: function () {
            this.box.off();

            if (this.label.length > 0) {
                this.label.off();
            }
        },

        enable: function () {
            this._bindEvents();
            this.box.removeClass('ui-state-disabled');
        },

        disable: function () {
            this._unbindEvents();
            this.box.addClass('ui-state-disabled');
        }

    });

});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiLightbox', ['$compile', function ($compile) {
    return {
        restrict: 'A',
        priority: 5,
        compile: function (element, attrs) {

            return function postLink (scope, element, attrs) {
                var options = scope.$eval(attrs.puiLightbox) || {},
                    dynamicList = angular.isArray(options) || angular.isArray(options.items),
                    items = [],
                    initialCall = true;

                // TODO check if iframeWidth or iframeWidth the directive is placed on a <a>-tag
                options.iframe = 'A' === element[0].nodeName;

                function renderLightbox() {
                    var htmlContent = '';
                    angular.forEach(items, function(item) {
                        htmlContent = htmlContent +
                            '<a href="'+item.image+'" title="'+item.oneLiner+'"><img src="'+item.thumbnail+'" title="'+item.title+'"/></a>';
                    });
                    element.html(htmlContent);
                    $compile(element.contents())(scope);
                    $(function () {
                        if (!initialCall) {
                            element.puilightbox('destroy', {});
                        }
                        element.puilightbox({
                            iframe: options.iframe,
                            iframeWidth: options.iframeWidth,
                            iframeHeight: options.iframeHeight
                        });
                        initialCall = false;

                    });
                }

                if (dynamicList) {
                    if (angular.isArray(options)) {
                        scope.$watch(attrs.puiLightbox, function(x) {
                            items = x;
                            renderLightbox();
                        }, true);

                    } else {
                        scope.$watch(attrs.puiLightbox+'.items', function(x) {
                            items = x;
                            renderLightbox();
                        }, true);
                    }

                } else {
                    $(function () {
                        element.puilightbox({
                            iframe: options.iframe,
                            iframeWidth: options.iframeWidth,
                            iframeHeight: options.iframeHeight
                        });
                    });
                }

            };

        }
    };
}]).directive('puiLightboxItem', function () {
    return {
        restrict: 'A',
        priority: 10,
        compile: function (element, attrs) {
            var lightboxItemType = function() {
                var items = element.parent().children('[pui-lightbox-item]');
                if (items.length === 0) {
                    // This is the case for the ng-repeat situation
                } else {
                    if (items.length > 1) {
                        return 'images';
                    } else {
                        return 'inline';
                    }
                }
            };

            var type = element.parent().data('puiLightboxType');

            if (type === undefined) {
                type = lightboxItemType();
                element.parent().data('puiLightboxType', type);
            }

            var lightboxInlineType = function() {
                element.addClass('ui-helper-hidden');
                element.before('<a class="group" href="#">'+attrs.title +'</a>  ');
            };

            var lightboxImagesType = function() {
                var image = element.children('img');
                var title = element.attr('title');
                var thumbnailSrc = element.attr('src');
                var imageSrc = image.attr('src');
                var imageTitle = image.attr('alt') || image.attr('title');

                $(function () {
                    element.children().attr('src', thumbnailSrc);
                    element.children().attr('alt', title);
                    element.children().attr('title', title);
                    element.children().wrap('<a href="' + imageSrc + '" title="' + imageTitle + '">');
                    element.replaceWith(element.html());
                });

            };

            if ('inline' === type) {
                lightboxInlineType();
                element.removeAttr('pui-lightbox-item');
            }

            if ('images' === type) {
                lightboxImagesType();
            }
        }
    };
});

}());
;/*jshint laxcomma:true*/
/*globals $ document window PUI*/

/**
 * PrimeUI Lightbox Widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puilightbox", {

        options: {
            iframeWidth: 640,
            iframeHeight: 480,
            iframe: false
        },

        _create: function() {
            this.options.mode = this.options.iframe ? 'iframe' : (this.element.children('div').length == 1) ? 'inline' : 'image';

            var dom = '<div class="pui-lightbox ui-widget ui-helper-hidden ui-corner-all pui-shadow">';
            dom += '<div class="pui-lightbox-content-wrapper">';
            dom += '<a class="ui-state-default pui-lightbox-nav-left ui-corner-right ui-helper-hidden"><span class="ui-icon ui-icon-carat-1-w">go</span></a>';
            dom += '<div class="pui-lightbox-content ui-corner-all"></div>';
            dom += '<a class="ui-state-default pui-lightbox-nav-right ui-corner-left ui-helper-hidden"><span class="ui-icon ui-icon-carat-1-e">go</span></a>';
            dom += '</div>';
            dom += '<div class="pui-lightbox-caption ui-widget-header"><span class="pui-lightbox-caption-text"></span>';
            dom += '<a class="pui-lightbox-close ui-corner-all" href="#"><span class="ui-icon ui-icon-closethick"></span></a><div style="clear:both" /></div>';
            dom += '</div>';

            this.panel = $(dom).appendTo(document.body);
            this.contentWrapper = this.panel.children('.pui-lightbox-content-wrapper');
            this.content = this.contentWrapper.children('.pui-lightbox-content');
            this.caption = this.panel.children('.pui-lightbox-caption');
            this.captionText = this.caption.children('.pui-lightbox-caption-text');
            this.closeIcon = this.caption.children('.pui-lightbox-close');

            if(this.options.mode === 'image') {
                this._setupImaging();
            }
            else if(this.options.mode === 'inline') {
                this._setupInline();
            }
            else if(this.options.mode === 'iframe') {
                this._setupIframe();
            }

            this._bindCommonEvents();

            this.links.data('puilightbox-trigger', true).find('*').data('puilightbox-trigger', true);
            this.closeIcon.data('puilightbox-trigger', true).find('*').data('puilightbox-trigger', true);
        },

        _bindCommonEvents: function() {
            var $this = this;

            this.closeIcon.hover(function() {
                $(this).toggleClass('ui-state-hover');
            }).click(function(e) {
                    $this.hide();
                    e.preventDefault();
                });

            //hide when outside is clicked
            $(document.body).bind('click.pui-lightbox', function (e) {
                if($this.isHidden()) {
                    return;
                }

                //do nothing if target is the link
                var target = $(e.target);
                if(target.data('puilightbox-trigger')) {
                    return;
                }

                //hide if mouse is outside of lightbox
                var offset = $this.panel.offset();
                if(e.pageX < offset.left ||
                    e.pageX > offset.left + $this.panel.width() ||
                    e.pageY < offset.top ||
                    e.pageY > offset.top + $this.panel.height()) {

                    $this.hide();
                }
            });

            //sync window resize
            $(window).resize(function() {
                if(!$this.isHidden()) {
                    $(document.body).children('.ui-widget-overlay').css({
                        'width': $(document).width()
                        ,'height': $(document).height()
                    });
                }
            });
        },

        _setupImaging: function() {
            var $this = this;

            this.links = this.element.children('a');
            this.content.append('<img class="ui-helper-hidden"></img>');
            this.imageDisplay = this.content.children('img');
            this.navigators = this.contentWrapper.children('a');

            this.imageDisplay.load(function() {
                var image = $(this);

                $this._scaleImage(image);

                //coordinates to center overlay
                var leftOffset = ($this.panel.width() - image.width()) / 2,
                    topOffset = ($this.panel.height() - image.height()) / 2;

                //resize content for new image
                $this.content.removeClass('pui-lightbox-loading').animate({
                        width: image.width()
                        ,height: image.height()
                    },
                    500,
                    function() {
                        //show image
                        image.fadeIn();
                        $this._showNavigators();
                        $this.caption.slideDown();
                    });

                $this.panel.animate({
                    left: '+=' + leftOffset
                    ,top: '+=' + topOffset
                }, 500);
            });

            this.navigators.hover(function() {
                $(this).toggleClass('ui-state-hover');
            })
                .click(function(e) {
                    var nav = $(this),
                        index; // Added for AngularPrime

                    $this._hideNavigators();

                    if(nav.hasClass('pui-lightbox-nav-left')) {
                        index = $this.current === 0 ? $this.links.length - 1 : $this.current - 1; // Changed for AngularPrime

                        $this.links.eq(index).trigger('click');
                    }
                    else {
                        index = $this.current == $this.links.length - 1 ? 0 : $this.current + 1; // Changed for AngularPrime

                        $this.links.eq(index).trigger('click');
                    }

                    e.preventDefault();
                });

            this.links.click(function(e) {
                var link = $(this);

                if($this.isHidden()) {
                    $this.content.addClass('pui-lightbox-loading').width(32).height(32);
                    $this.show();
                }
                else {
                    $this.imageDisplay.fadeOut(function() {
                        //clear for onload scaling
                        $(this).css({
                            'width': 'auto'
                            ,'height': 'auto'
                        });

                        $this.content.addClass('pui-lightbox-loading');
                    });

                    $this.caption.slideUp();
                }

                window.setTimeout(function() {
                    $this.imageDisplay.attr('src', link.attr('href'));
                    $this.current = link.index();

                    var title = link.attr('title');
                    if(title) {
                        $this.captionText.html(title);
                    }
                }, 1000);


                e.preventDefault();
            });
        },

        _scaleImage: function(image) {
            var win = $(window),
                winWidth = win.width(),
                winHeight = win.height(),
                imageWidth = image.width(),
                imageHeight = image.height(),
                ratio = imageHeight / imageWidth;

            if(imageWidth >= winWidth && ratio <= 1){
                imageWidth = winWidth * 0.75;
                imageHeight = imageWidth * ratio;
            }
            else if(imageHeight >= winHeight){
                imageHeight = winHeight * 0.75;
                imageWidth = imageHeight / ratio;
            }

            image.css({
                'width':imageWidth + 'px'
                ,'height':imageHeight + 'px'
            });
        },

        _setupInline: function() {
            this.links = this.element.children('a');
            this.inline = this.element.children('div').addClass('pui-lightbox-inline');
            this.inline.appendTo(this.content).show();
            var $this = this;

            this.links.click(function(e) {
                $this.show();

                var title = $(this).attr('title');
                if(title) {
                    $this.captionText.html(title);
                    $this.caption.slideDown();
                }

                e.preventDefault();
            });
        },

        _setupIframe: function() {
            var $this = this;
            this.links = this.element;
            this.iframe = $('<iframe frameborder="0" style="width:' + this.options.iframeWidth + 'px;height:' +
                this.options.iframeHeight + 'px;border:0 none; display: block;"></iframe>').appendTo(this.content);

            if(this.options.iframeTitle) {
                this.iframe.attr('title', this.options.iframeTitle);
            }

            this.element.click(function(e) {
                if(!$this.iframeLoaded) {
                    $this.content.addClass('pui-lightbox-loading').css({
                        width: $this.options.iframeWidth
                        ,height: $this.options.iframeHeight
                    });

                    $this.show();

                    $this.iframe.on('load', function() {
                        $this.iframeLoaded = true;
                        $this.content.removeClass('pui-lightbox-loading');
                    })
                        .attr('src', $this.element.attr('href'));
                }
                else {
                    $this.show();
                }

                var title = $this.element.attr('title');
                if(title) {
                    $this.caption.html(title);
                    $this.caption.slideDown();
                }

                e.preventDefault();
            });
        },

        show: function() {
            this.center();

            this.panel.css('z-index', ++PUI.zindex).show();

            if(!this.modality) {
                this._enableModality();
            }

            this._trigger('show');
        },

        hide: function() {
            this.panel.fadeOut();
            this._disableModality();
            this.caption.hide();

            if(this.options.mode === 'image') {
                this.imageDisplay.hide().attr('src', '').removeAttr('style');
                this._hideNavigators();
            }

            this._trigger('hide');
        },

        center: function() {
            var win = $(window),
                left = (win.width() / 2 ) - (this.panel.width() / 2),
                top = (win.height() / 2 ) - (this.panel.height() / 2);

            this.panel.css({
                'left': left,
                'top': top
            });
        },

        _enableModality: function() {
            this.modality = $('<div class="ui-widget-overlay"></div>')
                .css({
                    'width': $(document).width()
                    ,'height': $(document).height()
                    ,'z-index': this.panel.css('z-index') - 1
                })
                .appendTo(document.body);
        },

        _disableModality: function() {
            this.modality.remove();
            this.modality = null;
        },

        _showNavigators: function() {
            this.navigators.zIndex(this.imageDisplay.zIndex() + 1).show();
        },

        _hideNavigators: function() {
            this.navigators.hide();
        },

        isHidden: function() {
            return this.panel.is(':hidden');
        },

        showURL: function(opt) {
            if(opt.width)
                this.iframe.attr('width', opt.width);
            if(opt.height)
                this.iframe.attr('height', opt.height);

            this.iframe.attr('src', opt.src);

            this.show();
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiMenu', ['$log', function ($log) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            return function postLink (scope, element, attrs) {

                var options = scope.$eval(attrs.puiMenu) || {};
                options.popup = false;
                if (options.trigger) {
                    var triggerElement = $(options.trigger);
                    if (triggerElement !== undefined) {
                        options.popup = true;
                        options.trigger = triggerElement;
                    } else {
                        options.trigger = null;
                    }

                }
                if ( options.isContextMenu && options.isSlideMenu) {
                    throw new Error("ContextMenu can't be combined with the SlideMenu");
                }
                $(function() {
                    var hasSubMenu = element.find("ul").length > 0,
                        hasH3Element = element.find("h3").length > 0;

                    if (hasSubMenu || ! hasH3Element) {
                        if (hasH3Element) {
                            $log.warn("Menu with submenu and h3 elements found");
                        }
                        if (options.isContextMenu) {
                            element.puicontextmenu({
                                popup: options.popup,
                                trigger: options.trigger
                            });

                        } else {
                            if (options.isSlideMenu) {
                                element.puislidemenu({
                                    popup: options.popup,
                                    trigger: options.trigger
                                });

                            } else {
                                element.puitieredmenu({
                                    popup: options.popup,
                                    trigger: options.trigger,
                                    autoDisplay: options.autoDisplay
                                });

                            }
                        }
                    } else {
                        element.puimenu({
                            popup: options.popup,
                            trigger: options.trigger
                        });

                    }
                });
            };
        }
    };
}]);

}());
;/*jshint laxcomma:true*/
/*globals $ PUI */
/**
 * PrimeUI Menu widget
 */
$(function() {
    "use strict"; // Added for AngularPrime
    $.widget("primeui.puimenu", $.primeui.puibasemenu, {

        options: {

        },

        _create: function() {
            this.element.addClass('pui-menu-list ui-helper-reset').
                wrap('<div class="pui-menu ui-widget ui-widget-content ui-corner-all ui-helper-clearfix" />');

            this.element.children('li').each(function() {
                var listItem = $(this);

                if(listItem.children('h3').length > 0) {
                    listItem.addClass('ui-widget-header ui-corner-all');
                }
                else {
                    listItem.addClass('pui-menuitem ui-widget ui-corner-all');
                    var menuitemLink = listItem.children('a'),
                        icon = menuitemLink.data('icon');

                    menuitemLink.addClass('pui-menuitem-link ui-corner-all').contents().wrap('<span class="ui-menuitem-text" />');

                    if(icon) {
                        menuitemLink.prepend('<span class="pui-menuitem-icon ui-icon ' + icon + '"></span>');
                    }
                }
            });

            this.menuitemLinks = this.element.find('.pui-menuitem-link:not(.ui-state-disabled)');

            this._bindEvents();

            this._super();
        },

        _bindEvents: function() {
            var $this = this;

            this.menuitemLinks.on('mouseenter.pui-menu', function(e) {
                $(this).addClass('ui-state-hover');
            })
                .on('mouseleave.pui-menu', function(e) {
                    $(this).removeClass('ui-state-hover');
                });

            if(this.options.popup) {
                this.menuitemLinks.on('click.pui-menu', function() {
                    $this.hide();
                });
            }
        }
    });
});

/*
 * PrimeUI TieredMenu Widget
 */
$(function() {

    $.widget("primeui.puitieredmenu", $.primeui.puibasemenu, {

        options: {
            autoDisplay: true
        },

        _create: function() {
            this._render();

            this.links = this.element.find('.pui-menuitem-link:not(.ui-state-disabled)');

            this._bindEvents();

            this._super();
        },

        _render: function() {
            this.element.addClass('pui-menu-list ui-helper-reset').
                wrap('<div class="pui-tieredmenu pui-menu ui-widget ui-widget-content ui-corner-all ui-helper-clearfix" />');

            this.element.parent().uniqueId();
            this.options.id = this.element.parent().attr('id');

            this.element.find('li').each(function() {
                var listItem = $(this),
                    menuitemLink = listItem.children('a'),
                    icon = menuitemLink.data('icon');

                menuitemLink.addClass('pui-menuitem-link ui-corner-all').contents().wrap('<span class="ui-menuitem-text" />');

                if(icon) {
                    menuitemLink.prepend('<span class="pui-menuitem-icon ui-icon ' + icon + '"></span>');
                }

                listItem.addClass('pui-menuitem ui-widget ui-corner-all');
                if(listItem.children('ul').length > 0) {
                    listItem.addClass('pui-menu-parent');
                    listItem.children('ul').addClass('ui-widget-content pui-menu-list ui-corner-all ui-helper-clearfix pui-menu-child pui-shadow');
                    menuitemLink.prepend('<span class="ui-icon ui-icon-triangle-1-e"></span>');
                }


            });
        },

        _bindEvents: function() {
            this._bindItemEvents();

            this._bindDocumentHandler();
        },

        _bindItemEvents: function() {
            var $this = this;

            this.links.on('mouseenter.pui-menu',function() {
                var link = $(this),
                    menuitem = link.parent(),
                    autoDisplay = $this.options.autoDisplay;

                var activeSibling = menuitem.siblings('.pui-menuitem-active');
                if(activeSibling.length === 1) {
                    $this._deactivate(activeSibling);
                }

                if(autoDisplay||$this.active) {
                    if(menuitem.hasClass('pui-menuitem-active')) {
                        $this._reactivate(menuitem);
                    }
                    else {
                        $this._activate(menuitem);
                    }
                }
                else {
                    $this._highlight(menuitem);
                }
            });

            if(this.options.autoDisplay === false) {
                this.rootLinks = this.element.find('> .pui-menuitem > .pui-menuitem-link');
                this.rootLinks.data('primeui-tieredmenu-rootlink', this.options.id).find('*').data('primeui-tieredmenu-rootlink', this.options.id);

                this.rootLinks.on('click.pui-menu', function(e) {
                    var link = $(this),
                        menuitem = link.parent(),
                        submenu = menuitem.children('ul.pui-menu-child');

                    if(submenu.length === 1) {
                        if(submenu.is(':visible')) {
                            $this.active = false;
                            $this._deactivate(menuitem);
                        }
                        else {
                            $this.active = true;
                            $this._highlight(menuitem);
                            $this._showSubmenu(menuitem, submenu);
                        }
                    }
                });
            }

            this.element.parent().find('ul.pui-menu-list').on('mouseleave.pui-menu', function(e) {
                if($this.activeitem) {
                    $this._deactivate($this.activeitem);
                }

                e.stopPropagation();
            });
        },

        _bindDocumentHandler: function() {
            var $this = this;

            $(document.body).on('click.pui-menu', function(e) {
                var target = $(e.target);
                if(target.data('primeui-tieredmenu-rootlink') === $this.options.id) {
                    return;
                }

                $this.active = false;

                $this.element.find('li.pui-menuitem-active').each(function() {
                    $this._deactivate($(this), true);
                });
            });
        },

        _deactivate: function(menuitem, animate) {
            this.activeitem = null;
            menuitem.children('a.pui-menuitem-link').removeClass('ui-state-hover');
            menuitem.removeClass('pui-menuitem-active');

            if(animate)
                menuitem.children('ul.pui-menu-child:visible').fadeOut('fast');
            else
                menuitem.children('ul.pui-menu-child:visible').hide();
        },

        _activate: function(menuitem) {
            this._highlight(menuitem);

            var submenu = menuitem.children('ul.pui-menu-child');
            if(submenu.length === 1) {
                this._showSubmenu(menuitem, submenu);
            }
        },

        _reactivate: function(menuitem) {
            this.activeitem = menuitem;
            var submenu = menuitem.children('ul.pui-menu-child'),
                activeChilditem = submenu.children('li.pui-menuitem-active:first'),
                _self = this;

            if(activeChilditem.length === 1) {
                _self._deactivate(activeChilditem);
            }
        },

        _highlight: function(menuitem) {
            this.activeitem = menuitem;
            menuitem.children('a.pui-menuitem-link').addClass('ui-state-hover');
            menuitem.addClass('pui-menuitem-active');
        },

        _showSubmenu: function(menuitem, submenu) {
            submenu.css({
                'left': menuitem.outerWidth()
                ,'top': 0
                ,'z-index': ++PUI.zindex
            });

            submenu.show();
        }

    });

});

/**
 * PrimeUI Context Menu Widget
 */

$(function() {

    $.widget("primeui.puicontextmenu", $.primeui.puitieredmenu, {

        options: {
            autoDisplay: true,
            target: null,
            event: 'contextmenu'
        },

        _create: function() {
            this._super();
            this.element.parent().removeClass('pui-tieredmenu').
                addClass('pui-contextmenu pui-menu-dynamic pui-shadow');

            var $this = this;

            this.options.target = this.options.target||$(document);

            if(!this.element.parent().parent().is(document.body)) {
                this.element.parent().appendTo('body');
            }

            this.options.target.on(this.options.event + '.pui-contextmenu' , function(e){
                $this.show(e);
            });
        },

        _bindItemEvents: function() {
            this._super();

            var $this = this;

            //hide menu on item click
            this.links.bind('click', function() {
                $this.hide(); // Changed for AngularPrime
            });
        },

        _bindDocumentHandler: function() {
            var $this = this;

            //hide overlay when document is clicked
            $(document.body).bind('click.pui-contextmenu', function (e) {
                if($this.element.parent().is(":hidden")) {
                    return;
                }

                $this.hide(); // Changed for AngularPrime
            });
        },

        show: function(e) {
            //hide other contextmenus if any
            $(document.body).children('.pui-contextmenu:visible').hide();

            var win = $(window),
                left = e.pageX,
                top = e.pageY,
                width = this.element.parent().outerWidth(),
                height = this.element.parent().outerHeight();

            //collision detection for window boundaries
            if((left + width) > (win.width())+ win.scrollLeft()) {
                left = left - width;
            }
            if((top + height ) > (win.height() + win.scrollTop())) {
                top = top - height;
            }

            if(this.options.beforeShow) {
                this.options.beforeShow.call(this);
            }

            this.element.parent().css({
                'left': left,
                'top': top,
                'z-index': ++PUI.zindex
            }).show();

            e.preventDefault();
            e.stopPropagation();
        },

        hide: function() { // Changed for AngularPrime
            var $this = this;

            //hide submenus
            this.element.parent().find('li.pui-menuitem-active').each(function() {
                $this._deactivate($(this), true);
            });

            this.element.parent().fadeOut('fast');
        },

        isVisible: function() {
            return this.element.parent().is(':visible');
        },

        getTarget: function() {
            return this.jqTarget;
        }

    });

});

/*
 * PrimeUI SlideMenu Widget
 */

$(function() {

    $.widget("primeui.puislidemenu", $.primeui.puibasemenu, {

        _create: function() {

            this._render();

            //elements
            this.rootList = this.element;
            this.content = this.element.parent();
            this.wrapper = this.content.parent();
            this.container = this.wrapper.parent();
            this.submenus = this.container.find('ul.pui-menu-list');

            this.links = this.element.find('a.pui-menuitem-link:not(.ui-state-disabled)');
            this.backward = this.wrapper.children('div.pui-slidemenu-backward');

            //config
            this.stack = [];
            this.jqWidth = this.container.width();

            var $this = this;

            if(!this.element.hasClass('pui-menu-dynamic')) {
                this._applyDimensions();
            }
            this._super();

            this._bindEvents();
        },

        _render: function() {
            this.element.addClass('pui-menu-list ui-helper-reset').
                wrap('<div class="pui-menu pui-slidemenu ui-widget ui-widget-content ui-corner-all ui-helper-clearfix"/>').
                wrap('<div class="pui-slidemenu-wrapper" />').
                after('<div class="pui-slidemenu-backward ui-widget-header ui-corner-all ui-helper-clearfix">' + // Changed for AngularPrime
                    '<span class="ui-icon ui-icon-triangle-1-w"></span>Back</div>').
                wrap('<div class="pui-slidemenu-content" />');

            this.element.parent().uniqueId();
            this.options.id = this.element.parent().attr('id');

            this.element.find('li').each(function() {
                var listItem = $(this),
                    menuitemLink = listItem.children('a'),
                    icon = menuitemLink.data('icon');

                menuitemLink.addClass('pui-menuitem-link ui-corner-all').contents().wrap('<span class="ui-menuitem-text" />');

                if(icon) {
                    menuitemLink.prepend('<span class="pui-menuitem-icon ui-icon ' + icon + '"></span>');
                }

                listItem.addClass('pui-menuitem ui-widget ui-corner-all');
                if(listItem.children('ul').length > 0) {
                    listItem.addClass('pui-menu-parent');
                    listItem.children('ul').addClass('ui-widget-content pui-menu-list ui-corner-all ui-helper-clearfix pui-menu-child ui-shadow');
                    menuitemLink.prepend('<span class="ui-icon ui-icon-triangle-1-e"></span>');
                }


            });
        },

        _bindEvents: function() {
            var $this = this;

            this.links.on('mouseenter.pui-menu',function() {
                $(this).addClass('ui-state-hover');
            })
                .on('mouseleave.pui-menu',function() {
                    $(this).removeClass('ui-state-hover');
                })
                .on('click.pui-menu',function() {
                    var link = $(this),
                        submenu = link.next();

                    if(submenu.length == 1) {
                        $this._forward(submenu);
                    }
                });

            this.backward.on('click.pui-menu',function() {
                $this._back();
            });
        },

        _forward: function(submenu) {
            var $this = this;

            this._push(submenu);

            var rootLeft = -1 * (this._depth() * this.jqWidth);

            submenu.show().css({
                left: this.jqWidth
            });

            this.rootList.animate({
                left: rootLeft
            }, 500, 'easeInOutCirc', function() {
                if($this.backward.is(':hidden')) {
                    $this.backward.fadeIn('fast');
                }
            });
        },

        _back: function() {
            var $this = this,
                last = this._pop(),
                depth = this._depth();

            var rootLeft = -1 * (depth * this.jqWidth);

            this.rootList.animate({
                left: rootLeft
            }, 500, 'easeInOutCirc', function() {
                last.hide();

                if(depth === 0) {
                    $this.backward.fadeOut('fast');
                }
            });
        },

        _push: function(submenu) {
            this.stack.push(submenu);
        },

        _pop: function() {
            return this.stack.pop();
        },

        _last: function() {
            return this.stack[this.stack.length - 1];
        },

        _depth: function() {
            return this.stack.length;
        },

        _applyDimensions: function() {
            this.submenus.width(this.container.width());
            this.wrapper.height(this.rootList.outerHeight(true) + this.backward.outerHeight(true));
            this.content.height(this.rootList.outerHeight(true));
            this.rendered = true;
        },

        show: function() {
            this.align();
            this.container.css('z-index', ++PUI.zindex).show();

            if(!this.rendered) {
                this._applyDimensions();
            }
        }
    });

});
;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiMenubar', ['$log', function ($log) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            return function postLink (scope, element, attrs) {

                var options = scope.$eval(attrs.puiMenu) || {};
                if (element.find("h3").length > 0) {
                    $log.warn("Warning: "); // TODO
                }
                element.puimenubar({
                    autoDisplay: options.autoDisplay
                });

            };
        }
    };
}]);

}());
;/*globals $ PUI window*/
/**
 * PrimeUI Menubar Widget
 */

$(function() {
    "use strict";  // Added for AngularPrime

    $.widget("primeui.puimenubar", $.primeui.puitieredmenu, {

        options: {
            autoDisplay: true
        },

        _create: function() {
            this._super();
            this.element.parent().removeClass('pui-tieredmenu').
                addClass('pui-menubar');
        },

        _showSubmenu: function(menuitem, submenu) {
            var win = $(window),
                submenuOffsetTop = null,
                submenuCSS = {
                    'z-index': ++PUI.zindex
                };

            if(menuitem.parent().hasClass('pui-menu-child')) {
                submenuCSS.left = menuitem.outerWidth();
                submenuCSS.top = 0;
                submenuOffsetTop = menuitem.offset().top - win.scrollTop();
            }
            else {
                submenuCSS.left = 0;
                submenuCSS.top = menuitem.outerHeight();
                menuitem.offset().top - win.scrollTop(); // AngularPrime Question?
                submenuOffsetTop = menuitem.offset().top + submenuCSS.top - win.scrollTop();
            }

            //adjust height within viewport
            submenu.css('height', 'auto');
            if((submenuOffsetTop + submenu.outerHeight()) > win.height()) {
                submenuCSS.overflow = 'auto';
                submenuCSS.height = win.height() - (submenuOffsetTop + 20);
            }

            submenu.css(submenuCSS).show();
        }
    });

});

;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiNotify', function () {
        return {
            restrict: 'A',
            compile: function (element, attrs) {
                return function postLink (scope, element, attrs) {
                    // TODO check if no inline object created.
                    var options = scope.$eval(attrs.puiNotify) || {},
                        userTrigger = true; // because we support user closable and programmatic.

                    if (!(typeof options.visible == 'boolean')) {
                        throw new Error('The options object ' + attrs.puiNotify + ' needs a boolean property visible');
                    }
                    options.position = options.position || 'top';
                    options.animate = (options.animate !== undefined) ? options.animate : true;
                    options.effectSpeed = options.effectSpeed || 'normal';
                    options.easing = options.easing || 'swing';
                    $(function () {
                        element.puinotify({
                            position : options.position,
                            animate: options.animate,
                            effectSpeed: options.effectSpeed,
                            easing: options.easing
                        });
                    });

                    scope.$watch(attrs.puiNotify + '.visible', function (value) {
                        if (value === true) {
                            $(function () {
                                element.puinotify('show');
                            });

                        } else {
                            $(function () {
                                userTrigger = false;
                                element.puinotify('hide');
                            });

                        }
                    });
                    // required  when you close the notify with the close icon.
                    element.bind("puinotifyafterhide", function () {
                        scope.safeApply(function () {
                            scope[attrs.puiNotify].visible = false;
                            if (options.callback && userTrigger) {
                                options.callback();
                            }
                            userTrigger = true;
                        });
                    });
                };
            }
        };

    }

);

}());
;/*globals $ document PUI*/

/**
 * PrimeFaces Notify Widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puinotify", {

        options: {
            position: 'top',
            visible: false,
            animate: true,
            effectSpeed: 'normal',
            easing: 'swing'
        },

        _create: function() {
            this.element.addClass('pui-notify pui-notify-' + this.options.position + ' ui-widget ui-widget-content pui-shadow')
                .wrapInner('<div class="pui-notify-content" />').appendTo(document.body);
            this.content = this.element.children('.pui-notify-content');
            this.closeIcon = $('<span class="ui-icon ui-icon-closethick pui-notify-close"></span>').appendTo(this.element);

            this._bindEvents();

            if(this.options.visible) {
                this.show();
            }
        },

        _bindEvents: function() {
            var $this = this;

            this.closeIcon.on('click.puinotify', function() {
                $this.hide();
            });
        },

        show: function(content) {
            var $this = this;

            if(content) {
                this.update(content);
            }

            this.element.css('z-index',++PUI.zindex);

            this._trigger('beforeShow');

            if(this.options.animate) {
                this.element.slideDown(this.options.effectSpeed, this.options.easing, function() {
                    $this._trigger('afterShow');
                });
            }
            else {
                this.element.show();
                $this._trigger('afterShow');
            }
        },

        hide: function() {
            var $this = this;

            this._trigger('beforeHide');

            if(this.options.animate) {
                this.element.slideUp(this.options.effectSpeed, this.options.easing, function() {
                    $this._trigger('afterHide');
                });
            }
            else {
                this.element.hide();
                $this._trigger('afterHide');
            }
        },

        update: function(content) {
            this.content.html(content);
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiPanel', ['$interpolate', function ($interpolate) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            return function postLink (scope, element, attrs) {
                var options = scope.$eval(attrs.puiPanel) || {},
                    withinPuiAccordion = false,
                    withinPuiTabview = false;

                $(function () {
                    withinPuiAccordion = element.parent().attr('pui-accordion') !== undefined;
                    withinPuiTabview = element.parent().attr('pui-tabview') !== undefined;
                });

                if (withinPuiAccordion) {
                    element.replaceWith('<h3>'+element.attr('title')+'</h3><div>'+element.html()+'</div>');
                }

                if (withinPuiTabview) {
                    var id = element.attr('id');
                    element.replaceWith('<li><a href="#"'+id+'">'+element.attr('title')+'</a></li><div id="'+id+'">'+element.html()+'</div>');
                }
                if (!withinPuiAccordion && !withinPuiTabview) {
                    var titleWatches = [];
                    if (element.attr('title')) {
                        var parsedExpression = $interpolate(element.attr('title'));
                        element.attr('title', scope.$eval(parsedExpression));
                        angular.forEach(parsedExpression.parts, function (part) {
                            if (angular.isFunction(part)) {
                                titleWatches.push(part.exp);
                            }
                        }, titleWatches);
                    }

                    $(function () {
                        element.puipanel({
                            toggleable: options.collapsed !== undefined, closable: options.closable || false, toggleOrientation: options.toggleOrientation || 'vertical', toggleDuration: options.toggleDuration || 'normal', closeDuration: options.closeDuration || 'normal'
                        });
                    });
                    if (options.collapsed !== undefined && attrs.puiPanel.trim().charAt(0) !== '{') {
                        scope.$watch(attrs.puiPanel + '.collapsed', function (value) {
                            $(function () {
                                if (value === false) {
                                    element.puipanel('expand');
                                }
                                if (value === true) {
                                    element.puipanel('collapse');
                                }
                            });

                        });
                    }

                    angular.forEach(titleWatches, function (watchValue) {
                        scope.$watch(watchValue, function (value) {
                            $(function () {
                                element.puipanel('setTitle', scope.$eval(parsedExpression));
                            });
                        });
                    });
                }

            };

        }
    };
}]);

}());
;/*jshint laxcomma:true*/
/*globals $ */

/**
 * PrimeUI Panel Widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puipanel", {

        options: {
            toggleable: false,
            toggleDuration: 'normal',
            toggleOrientation : 'vertical',
            collapsed: false,
            closable: false,
            closeDuration: 'normal'
        },

        _create: function() {
            this.element.addClass('pui-panel ui-widget ui-widget-content ui-corner-all')
                .contents().wrapAll('<div class="pui-panel-content ui-widget-content" />');

            var title = this.element.attr('title');
            if(title) {
                this.element.prepend('<div class="pui-panel-titlebar ui-widget-header ui-helper-clearfix ui-corner-all"><span class="ui-panel-title">' +
                        title + "</span></div>")
                    .removeAttr('title');
            }

            this.header = this.element.children('div.pui-panel-titlebar');
            this.title = this.header.children('span.ui-panel-title');
            this.content = this.element.children('div.pui-panel-content');

            var $this = this;

            if(this.options.closable) {
                this.closer = $('<a class="pui-panel-titlebar-icon ui-corner-all ui-state-default" href="#"><span class="ui-icon ui-icon-closethick"></span></a>')
                    .appendTo(this.header)
                    .on('click.puipanel', function(e) {
                        $this.close();
                        e.preventDefault();
                    });
            }

            if(this.options.toggleable) {
                var icon = this.options.collapsed ? 'ui-icon-plusthick' : 'ui-icon-minusthick';

                this.toggler = $('<a class="pui-panel-titlebar-icon ui-corner-all ui-state-default" href="#"><span class="ui-icon ' + icon + '"></span></a>')
                    .appendTo(this.header)
                    .on('click.puipanel', function(e) {
                        $this.toggle();
                        e.preventDefault();
                    });

                if(this.options.collapsed) {
                    this.content.hide();
                }
            }

            this._bindEvents();
        },

        _bindEvents: function() {
            this.header.find('a.pui-panel-titlebar-icon').on('hover.puipanel', function() {$(this).toggleClass('ui-state-hover');});
        },

        close: function() {
            var $this = this;

            this._trigger('beforeClose', null);

            this.element.fadeOut(this.options.closeDuration,
                function() {
                    $this._trigger('afterClose', null);
                }
            );
        },

        toggle: function() {
            if(this.options.collapsed) {
                this.expand();
            }
            else {
                this.collapse();
            }
        },

        expand: function() {
            this.toggler.children('span.ui-icon').removeClass('ui-icon-plusthick').addClass('ui-icon-minusthick');

            if(this.options.toggleOrientation === 'vertical') {
                this._slideDown();
            }
            else if(this.options.toggleOrientation === 'horizontal') {
                this._slideRight();
            }
        },

        collapse: function() {
            this.toggler.children('span.ui-icon').removeClass('ui-icon-minusthick').addClass('ui-icon-plusthick');

            if(this.options.toggleOrientation === 'vertical') {
                this._slideUp();
            }
            else if(this.options.toggleOrientation === 'horizontal') {
                this._slideLeft();
            }
        },

        _slideUp: function() {
            var $this = this;

            this._trigger('beforeCollapse');

            this.content.slideUp(this.options.toggleDuration, 'easeInOutCirc', function() {
                $this._trigger('afterCollapse');
                $this.options.collapsed = !$this.options.collapsed;
            });
        },

        _slideDown: function() {
            var $this = this;

            this._trigger('beforeExpand');

            this.content.slideDown(this.options.toggleDuration, 'easeInOutCirc', function() {
                $this._trigger('afterExpand');
                $this.options.collapsed = !$this.options.collapsed;
            });
        },

        _slideLeft: function() {
            var $this = this;

            this.originalWidth = this.element.width();

            this.title.hide();
            this.toggler.hide();
            this.content.hide();

            this.element.animate({
                width: '42px'
            }, this.options.toggleSpeed, 'easeInOutCirc', function() {
                $this.toggler.show();
                $this.element.addClass('pui-panel-collapsed-h');
                $this.options.collapsed = !$this.options.collapsed;
            });
        },

        _slideRight: function() {
            var $this = this,
                expandWidth = this.originalWidth||'100%';

            this.toggler.hide();

            this.element.animate({
                width: expandWidth
            }, this.options.toggleSpeed, 'easeInOutCirc', function() {
                $this.element.removeClass('pui-panel-collapsed-h');
                $this.title.show();
                $this.toggler.show();
                $this.options.collapsed = !$this.options.collapsed;

                $this.content.css({
                    'visibility': 'visible'
                    ,'display': 'block'
                    ,'height': 'auto'
                });
            });
        },

        // Added for AngularPrime
        setTitle: function(title) {
            this.element.find('.ui-panel-title').html(title);
        }
    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiProgressbar', function () {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            $(function () {

                var options = scope.$eval(attrs.puiProgressbar);
                var onlyValueSpecified = false;

                if (angular.isNumber(options)) {
                    onlyValueSpecified = true;
                    element.puiprogressbar({
                    });

                } else {
                    element.puiprogressbar({
                        value: options.value,
                        labelTemplate: options.labelTemplate,
                        showLabel: options.showLabel,
                        easing: options.easing,
                        effectSpeed: options.effectSpeed
                    });

                }

                function setNewValue(value) {
                    if (value !== null) {
                        element.puiprogressbar('setValue', value);
                    }
                }

                if (onlyValueSpecified) {
                    scope.$watch(attrs.puiProgressbar, function(value) {
                        setNewValue(value);
                    });
                } else {
                    scope.$watch(attrs.puiProgressbar+'.value', function(value) {
                        setNewValue(value);
                    });

                }


            });
        }
    };

});

}());
;/*globals $ */

/**
 * PrimeUI progressbar widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puiprogressbar", {

        options: {
            value: 0,
            labelTemplate: '{value}%',
            complete: null,
            easing: 'easeInOutCirc',
            effectSpeed: 'normal',
            showLabel: true
        },

        _create: function() {
            this.element.addClass('pui-progressbar ui-widget ui-widget-content ui-corner-all')
                .append('<div class="pui-progressbar-value ui-widget-header ui-corner-all"></div>')
                .append('<div class="pui-progressbar-label"></div>');

            this.jqValue = this.element.children('.pui-progressbar-value');
            this.jqLabel = this.element.children('.pui-progressbar-label');

            if(this.options.value !==0) {
                this._setValue(this.options.value, false);
            }

            this.enableARIA();
        },

        setValue: function(value, animate) { // changed visibility for AngularPrime
            var anim = (animate === undefined || animate) ? true : false;

            if(value >= 0 && value <= 100) {
                if(value === 0) {
                    this.jqValue.hide().css('width', '0%').removeClass('ui-corner-right');

                    this.jqLabel.hide();
                }
                else {
                    if(anim) {
                        this.jqValue.show().animate({
                            'width': value + '%'
                        }, this.options.effectSpeed, this.options.easing);
                    }
                    else {
                        this.jqValue.show().css('width', value + '%');
                    }

                    if(this.options.labelTemplate && this.options.showLabel) {
                        var formattedLabel = this.options.labelTemplate.replace(/{value}/gi, value);

                        this.jqLabel.html(formattedLabel).show();
                    }

                    if(value === 100) {
                        this._trigger('complete');
                    }
                }

                this.options.value = value;
                this.element.attr('aria-valuenow', value);
            }
        },

        _getValue: function() {
            return this.options.value;
        },

        enableARIA: function() {
            this.element.attr('role', 'progressbar')
                .attr('aria-valuemin', 0)
                .attr('aria-valuenow', this.options.value)
                .attr('aria-valuemax', 100);
        },

        _setOption: function(key, value) {
            if(key === 'value') {
                this._setValue(value);
            }

            $.Widget.prototype._setOption.apply(this, arguments);
        },

        _destroy: function() {

        }

    });

});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiRating', function () {
    return {
        restrict: 'A',
        require: '?ngModel', // get a hold of NgModelController
        link: function (scope, element, attrs, ngModel) {
            if (!ngModel) {
                return;
            } // do nothing if no ng-model
            $(function () {

                var options = scope.$eval(attrs.puiRating) || {};

                options.cancel = (options.cancel !== undefined) ? options.cancel : true;
                options.stars = options.stars || 5;

                element.puirating({
                    cancel: options.cancel,
                    stars: options.stars
                });

                element.hide();


                // Specify how UI should be updated
                ngModel.$render = function () {
                    element.puirating('setValue', ngModel.$viewValue);
                };


                // Listen for change events to enable binding
                element.bind('puiratingrate puiratingcancel', function () {
                    scope.safeApply(read());
                    if (options.callback) {
                        options.callback(ngModel.$viewValue);
                    }

                });

                // Write data to the model
                function read() {
                    if (ngModel.$viewValue !== parseInt(element.val(), 10))   {
                        // Only set Angular model value when effective changed. Otherwise ng-change can be triggered to many times.
                        ngModel.$setViewValue(element.val());
                    }
                }


                if (attrs.ngDisabled) {
                    scope.$watch(attrs.ngDisabled, function (value) {

                        if (value === false) {
                            $(function () {
                                element.puirating('enable');
                            });
                        } else {
                            $(function () {
                                element.puirating('disable');

                            });

                        }
                    });
                }

            });
        }
    };

});

}());
;/*globals $ */

/**
 * PrimeUI rating widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puirating", {

        options: {
            stars: 5,
            cancel: true
        },

        _create: function() {
            var input = this.element;

            input.wrap('<div />');
            this.container = input.parent();
            this.container.addClass('pui-rating');

            var inputVal = input.val(),
                value = inputVal === '' ? null : parseInt(inputVal, 10);

            if(this.options.cancel) {
                this.container.append('<div class="pui-rating-cancel"><a></a></div>');
            }

            for(var i = 0; i < this.options.stars; i++) {
                var styleClass = (value > i) ? "pui-rating-star pui-rating-star-on" : "pui-rating-star";

                this.container.append('<div class="' + styleClass + '"><a></a></div>');
            }

            this.stars = this.container.children('.pui-rating-star');

            if(input.prop('disabled')) {
                this.container.addClass('ui-state-disabled');
            }
            else if(!input.prop('readonly')){
                this._bindEvents();
            }
        },

        _bindEvents: function() {
            var $this = this;

            this.stars.on('click',function() {  // Changed for AngularPrime (from .click to .on('click',
                var value = $this.stars.index(this) + 1;   //index starts from zero

                $this.setValue(value);
            });

            this.container.children('.pui-rating-cancel').hover(function() {
                $(this).toggleClass('pui-rating-cancel-hover');
            })
                .click(function() {
                    $this.cancel();
                });
        },

        cancel: function() {
            this.element.val('');

            this.stars.filter('.pui-rating-star-on').removeClass('pui-rating-star-on');

            this._trigger('cancel', null);
        },

        getValue: function() {
            var inputVal = this.element.val();

            return inputVal === '' ? null : parseInt(inputVal, 10);
        },

        setValue: function(value) {
            this.element.val(value);

            //update visuals
            this.stars.removeClass('pui-rating-star-on');
            for(var i = 0; i < value; i++) {
                this.stars.eq(i).addClass('pui-rating-star-on');
            }

            this._trigger('rate', null, value);
        },

        // Added for AngularPrime
        enable: function() {
            this.container.removeClass('ui-state-disabled');
            this._bindEvents();
        },

        disable: function() {
            this.container.addClass('ui-state-disabled');
            this._unbindEvents();
        },

        _unbindEvents: function() {
            this.stars.off('click');

            this.container.children('.pui-rating-cancel').hover(function() {
            })
            .click(function() {
            });
        }
    });

});;/*globals angular $ */
(function () {
    "use strict";

angular.module('angular.prime').directive('puiSpinner', function () {
    return {
        restrict: 'A',
        require: '?ngModel', // get a hold of NgModelController
        link: function (scope, element, attrs, ngModel) {
            if (!ngModel) {
                return;
            } // do nothing if no ng-model
            $(function () {

                var options = scope.$eval(attrs.puiSpinner) || {},
                    helper = {
                        read: function () {
                            $(function () {
                                scope.safeApply(function () {
                                    ngModel.$setViewValue(element.val());
                                });

                            });
                        }};
                element.puispinner({
                    step: options.step,
                    prefix: options.prefix,
                    suffix: options.suffix,
                    min: options.min,
                    max: options.max
                });

                // Listen for select events to enable binding
                element.bind('puispinnerchange', function () {
                    helper.read();
                });

                if (attrs.ngDisabled) {
                    scope.$watch(attrs.ngDisabled, function (value) {

                        if (value === false) {
                            element.puispinner('enable');
                        } else {
                            element.puispinner('disable');

                        }
                    });
                }

            });
        }
    };

});

}());
;/*jshint laxcomma:true*/
/*globals $ window*/

/**
 * PrimeUI spinner widget
 */
$(function() {
    "use strict"; // Added for AngularPrime

    $.widget("primeui.puispinner", {

        options: {
            step: 1.0
        },

        _create: function() {
            var input = this.element,
                disabled = input.prop('disabled');

            input.puiinputtext().addClass('pui-spinner-input').wrap('<span class="pui-spinner ui-widget ui-corner-all" />');
            this.wrapper = input.parent();
            this.wrapper.append('<a class="pui-spinner-button pui-spinner-up ui-corner-tr ui-button ui-widget ui-state-default ui-button-text-only"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-n"></span></span></a><a class="pui-spinner-button pui-spinner-down ui-corner-br ui-button ui-widget ui-state-default ui-button-text-only"><span class="ui-button-text"><span class="ui-icon ui-icon-triangle-1-s"></span></span></a>');
            //this.upButton = this.wrapper.children('a.pui-spinner-up');
            //this.downButton = this.wrapper.children('a.pui-spinner-down');
            this.options.step = this.options.step||1;

            if(parseInt(this.options.step, 10) === 0) {
                this.options.precision = this.options.step.toString().split(/[,]|[.]/)[1].length;
            }

            this._initValue();

            if(!disabled&&!input.prop('readonly')) {
                this._bindEvents();
            }

            if(disabled) {
                this.wrapper.addClass('ui-state-disabled');
            }

            //aria
            input.attr({
                'role': 'spinner'
                ,'aria-multiline': false
                ,'aria-valuenow': this.value
            });

            if(this.options.min !== undefined)
                input.attr('aria-valuemin', this.options.min);

            if(this.options.max !== undefined)
                input.attr('aria-valuemax', this.options.max);

            if(input.prop('disabled'))
                input.attr('aria-disabled', true);

            if(input.prop('readonly'))
                input.attr('aria-readonly', true);
        },


        _bindEvents: function() {
            var $this = this;

            //visuals for spinner buttons
            this.wrapper.children('.pui-spinner-button')
                .mouseover(function() {
                    $(this).addClass('ui-state-hover');
                }).mouseout(function() {
                    $(this).removeClass('ui-state-hover ui-state-active');

                    if($this.timer) {
                        window.clearInterval($this.timer);
                    }
                }).mouseup(function() {
                    window.clearInterval($this.timer);
                    $(this).removeClass('ui-state-active').addClass('ui-state-hover');
                }).mousedown(function(e) {
                    var element = $(this),
                        dir = element.hasClass('pui-spinner-up') ? 1 : -1;

                    element.removeClass('ui-state-hover').addClass('ui-state-active');

                    if($this.element.is(':not(:focus)')) {
                        $this.element.focus();
                    }

                    $this._repeat(null, dir);

                    //keep focused
                    e.preventDefault();
                });

            this.element.keydown(function (e) {
                var keyCode = $.ui.keyCode;

                switch(e.which) {
                    case keyCode.UP:
                        $this._spin($this.options.step);
                        break;

                    case keyCode.DOWN:
                        $this._spin(-1 * $this.options.step);
                        break;

                    default:
                        //do nothing
                        break;
                }
            })
                .keyup(function () {
                    $this._updateValue();
                })
                .blur(function () {
                    $this._format();
                })
                .focus(function () {
                    //remove formatting
                    $this.element.val($this.value);
                });

            //mousewheel
            this.element.bind('mousewheel', function(event, delta) {
                if($this.element.is(':focus')) {
                    if(delta > 0)
                        $this._spin($this.options.step);
                    else
                        $this._spin(-1 * $this.options.step);

                    return false;
                }
            });
        },

        _repeat: function(interval, dir) {
            var $this = this,
                i = interval || 500;

            window.clearTimeout(this.timer);
            this.timer = window.setTimeout(function() {
                $this._repeat(40, dir);
            }, i);

            this._spin(this.options.step * dir);
        },

        _toFixed: function (value, precision) {
            var power = Math.pow(10, precision||0);
            return String(Math.round(value * power) / power);
        },

        _spin: function(step) {
            var newValue, // Changed for AngularPrime
            currentValue = this.value ? this.value : 0;

            if(this.options.precision)
                newValue = parseFloat(this._toFixed(currentValue + step, this.options.precision));
            else
                newValue = parseInt(currentValue + step, 10);

            if(this.options.min !== undefined && newValue < this.options.min) {
                newValue = this.options.min;
            }

            if(this.options.max !== undefined && newValue > this.options.max) {
                newValue = this.options.max;
            }

            this.element.val(newValue).attr('aria-valuenow', newValue);
            this.value = newValue;

            this._trigger('change'); // Changed for AngularPrime
        },

        _updateValue: function() {
            var value = this.element.val();

            if(value === '') {
                if(this.options.min !== undefined)
                    this.value = this.options.min;
                else
                    this.value = 0;
            }
            else {
                if(this.options.step)
                    value = parseFloat(value);
                else
                    value = parseInt(value, 10);

                if(!isNaN(value)) {
                    this.value = value;
                }
            }
        },

        _initValue: function() {
            var value = this.element.val();

            if(value === '') {
                if(this.options.min !== undefined)
                    this.value = this.options.min;
                else
                    this.value = 0;
            }
            else {
                if(this.options.prefix)
                    value = value.split(this.options.prefix)[1];

                if(this.options.suffix)
                    value = value.split(this.options.suffix)[0];

                if(this.options.step)
                    this.value = parseFloat(value);
                else
                    this.value = parseInt(value, 10);
            }
        },

        _format: function() {
            var value = this.value;

            if(this.options.prefix)
                value = this.options.prefix + value;

            if(this.options.suffix)
                value = value + this.options.suffix;

            this.element.val(value);
            this._trigger('change'); // added for AngularPrime
        },

        // Added for AngularPrime
        _unbindEvents: function() {
            var $this = this;

            //visuals for spinner buttons
            this.wrapper.children('.pui-spinner-button')
                .off();

            this.element.off();

        },

        enable: function() {
            this.wrapper.removeClass('ui-state-disabled');
            this._bindEvents();
        },

        disable: function() {
            this.wrapper.addClass('ui-state-disabled');
            this._unbindEvents();
        }
    });
});;/*globals angular $ */
(function () {
    "use strict";

angular.module('angular.prime').directive('puiTabview', ['$http', '$templateCache', '$compile', '$log',
                                                    function ($http, $templateCache, $compile, $log) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {
            return function postLink (scope, element, attrs) {
                var options = scope.$eval(attrs.puiTabview) || {};
                var dynamicPanels = angular.isArray(options) || angular.isArray(options.urls);
                var content = [];
                var urls = [];
                var remaining;
                var initialCall = true;

                function supportForCloseableTabs() {
                    if (options.closeable === true) {
                        element.find('a').after('<span class="ui-icon ui-icon-close"></span>');
                    }
                }

                function generateHtml(contentArray, tagName) {
                    var filtered = $.grep(contentArray, function(n, i){
                        return tagName === n.nodeName;
                    });
                    var result = '';
                    angular.forEach(filtered, function (part) {
                        result = result+ part.outerHTML;
                    });
                    return result;
                }

                function renderTabPanels(panels) {
                    var htmlContent = '';
                    angular.forEach(panels, function(panelContent) {
                        htmlContent = htmlContent + panelContent;
                    });

                    var tmp = $.parseHTML(htmlContent);

                    var titleHtml = generateHtml(tmp, 'LI');
                    var panelHtml = generateHtml(tmp, 'DIV');

                    element.html('<ul>'+titleHtml+'</ul><div>'+panelHtml+'</div>');
                    supportForCloseableTabs();
                    $compile(element.contents())(scope);
                    $(function () {
                        if (!initialCall) {
                            element.puitabview('destroy', {});
                        }
                        element.puitabview({
                            orientation: options.orientation || 'top'
                        });
                        initialCall = false;

                    });
                }

                function loadHtmlContents(idx, url) {
                    $http.get(url, {cache: $templateCache}).success(function (response) {
                        content[idx] = response;
                        remaining--;
                        if (remaining === 0) {
                            renderTabPanels(content);
                        }
                    }).error(function () {
                            $log.error('Error loading included file ' + url + 'for panel of accordion');
                        });

                }

                function loadAndRenderTabPanels() {
                    remaining = urls.length;
                    for (var i = 0; i < urls.length; i++) {
                        loadHtmlContents(i, urls[i]);
                    }
                }

                if (dynamicPanels) {
                    if (angular.isArray(options)) {
                        scope.$watch(attrs.puiTabview, function(x) {
                            urls = x;
                            loadAndRenderTabPanels();
                        }, true);

                    } else {
                        scope.$watch(attrs.puiTabview+'.urls', function(x) {
                            urls = x;
                            loadAndRenderTabPanels();
                        }, true);
                    }

                    loadAndRenderTabPanels();

                } else {
                    $(function () {
                        if (element.children('ul').length === 0) {
                            element.children('li').wrapAll("<ul />");
                            element.children('div').wrapAll("<div />");
                        }

                        supportForCloseableTabs();
                        element.puitabview({
                            orientation: options.orientation || 'top'
                        });

                    });
                }

                if (options.callback) {
                    element.bind('puitabviewchange', function (eventData, index) {
                        options.callback(index);
                    });
                }

                if (options.activeElement !== undefined && attrs.puiTabview.trim().charAt(0) !== '{' ) {
                    scope.$watch(attrs.puiTabview+'.activeElement', function (value) {
                        $(function () {
                            if (angular.isNumber(value)) {
                                element.puitabview('select', value);
                            }
                        });
                    });
                }

            };
        }
    };
}]);

}());
;/*jshint laxcomma:true*/
/*globals $ */

/**
 * PrimeUI tabview widget
 */
$(function() {
    "use strict"; // Added for AngularPrime
    $.widget("primeui.puitabview", {

        options: {
            activeIndex:0
            ,orientation:'top'
        },

        _create: function() {
            var element = this.element;

            element.addClass('pui-tabview ui-widget ui-widget-content ui-corner-all ui-hidden-container')
                .children('ul').addClass('pui-tabview-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all')
                .children('li').addClass('ui-state-default ui-corner-top');

            element.addClass('pui-tabview-' + this.options.orientation);

            element.children('div').addClass('pui-tabview-panels').children().addClass('pui-tabview-panel ui-widget-content ui-corner-bottom');

            element.find('> ul.pui-tabview-nav > li').eq(this.options.activeIndex).addClass('pui-tabview-selected ui-state-active');
            element.find('> div.pui-tabview-panels > div.pui-tabview-panel:not(:eq(' + this.options.activeIndex + '))').addClass('ui-helper-hidden');

            this.navContainer = element.children('.pui-tabview-nav');
            this.panelContainer = element.children('.pui-tabview-panels');

            this._bindEvents();
        },

        _bindEvents: function() {
            var $this = this;

            //Tab header events
            this.navContainer.children('li')
                .on('mouseover.tabview', function(e) {
                    var element = $(this);
                    if(!element.hasClass('ui-state-disabled')&&!element.hasClass('ui-state-active')) {
                        element.addClass('ui-state-hover');
                    }
                })
                .on('mouseout.tabview', function(e) {
                    var element = $(this);
                    if(!element.hasClass('ui-state-disabled')&&!element.hasClass('ui-state-active')) {
                        element.removeClass('ui-state-hover');
                    }
                })
                .on('click.tabview', function(e) {
                    var element = $(this);

                    if($(e.target).is(':not(.ui-icon-close)')) {
                        var index = element.index();

                        if(!element.hasClass('ui-state-disabled') && index != $this.options.selected) {
                            $this.select(index);
                        }
                    }

                    e.preventDefault();
                });

            //Closable tabs
            this.navContainer.find('li .ui-icon-close')
                .on('click.tabview', function(e) {
                    var index = $(this).parent().index();

                    $this.remove(index);

                    e.preventDefault();
                });
        },

        select: function(index) {
            this.options.selected = index;

            var newPanel = this.panelContainer.children().eq(index),
                headers = this.navContainer.children(),
                oldHeader = headers.filter('.ui-state-active'),
                newHeader = headers.eq(newPanel.index()),
                oldPanel = this.panelContainer.children('.pui-tabview-panel:visible'),
                $this = this;

            //aria
            oldPanel.attr('aria-hidden', true);
            oldHeader.attr('aria-expanded', false);
            newPanel.attr('aria-hidden', false);
            newHeader.attr('aria-expanded', true);

            if(this.options.effect) {
                oldPanel.hide(this.options.effect.name, null, this.options.effect.duration, function() {
                    oldHeader.removeClass('pui-tabview-selected ui-state-active');

                    newHeader.removeClass('ui-state-hover').addClass('pui-tabview-selected ui-state-active');
                    newPanel.show($this.options.name, null, $this.options.effect.duration, function() {
                        $this._trigger('change', null, index);
                    });
                });
            }
            else {
                oldHeader.removeClass('pui-tabview-selected ui-state-active');
                oldPanel.hide();

                newHeader.removeClass('ui-state-hover').addClass('pui-tabview-selected ui-state-active');
                newPanel.show();

                this._trigger('change', null, index);
            }
        },

        remove: function(index) {
            var header = this.navContainer.children().eq(index),
                panel = this.panelContainer.children().eq(index);

            this._trigger('close', null, index);

            header.remove();
            panel.remove();

            //active next tab if active tab is removed
            if(index == this.options.selected) {
                var newIndex = this.options.selected == this.getLength() ? this.options.selected - 1: this.options.selected;
                this.select(newIndex);
            }
        },

        getLength: function() {
            return this.navContainer.children().length;
        },

        getActiveIndex: function() {
            return this.options.selected;
        },

        _markAsLoaded: function(panel) {
            panel.data('loaded', true);
        },

        _isLoaded: function(panel) {
            return panel.data('loaded') === true;
        },

        disable: function(index) {
            this.navContainer.children().eq(index).addClass('ui-state-disabled');
        },

        enable: function(index) {
            this.navContainer.children().eq(index).removeClass('ui-state-disabled');
        }

    });
});;/*globals angular $ */

(function () {
    "use strict";

angular.module('angular.prime').directive('puiTooltip', ['$interpolate', function ($interpolate) {
    return {
        restrict: 'A',
        compile: function (element, attrs) {

            return function postLink (scope, element, attrs) {

                var titleAttr = attrs.title;

                var options = scope.$eval(attrs.puiTooltip) || {};
                options.my = options.my || 'left top';
                options.at = options.at || 'right bottom';
                options.content = options.content || titleAttr;
                options.showEvent = options.showEvent || 'mouseover';
                options.hideEvent = options.hideEvent || 'mouseout';
                options.showEffect = options.showEffect || 'fade';
                options.showEffectSpeed = options.showEffectSpeed || 'normal';
                options.hideEffectSpeed = options.hideEffectSpeed || 'normal';
                options.showDelay = options.showDelay || 150;

                var tooltipWatches = [];

                if (options.content && options.content !== '') {
                    var parsedExpression = $interpolate(options.content);
                    options.content = scope.$eval(parsedExpression);
                    angular.forEach(parsedExpression.parts, function(part) {
                        if (angular.isFunction(part)) {
                            tooltipWatches.push(part.exp);
                        }
                    }, tooltipWatches);

                    $(function () {
                        element.puitooltip({
                            content: options.content,
                            showEvent: options.showEvent,
                            hideEvent: options.hideEvent,
                            showEffect: options.showEffect,
                            hideEffect: options.hideEffect,
                            showEffectSpeed: options.showEffectSpeed,
                            hideEffectSpeed: options.hideEffectSpeed,
                            my: options.my,
                            at: options.at,
                            showDelay: options.showDelay
                        });
                    });

                    angular.forEach(tooltipWatches, function(watchValue) {
                        scope.$watch(watchValue, function (value) {
                            $(function () {
                                element.puitooltip('setTooltipContent', scope.$eval(parsedExpression));
                            });
                        });
                    });

                }
            };

        }
    };
}]);

}());
;/*globals $ window document PUI*/

/**
 * PrimeFaces Tooltip Widget
 */
$(function() {
    "use strict"; // Added for AnfularPrime

    $.widget("primeui.puitooltip", {

        options: {
            showEvent: 'mouseover',
            hideEvent: 'mouseout',
            showEffect: 'fade',
            hideEffect: null,
            showEffectSpeed: 'normal',
            hideEffectSpeed: 'normal',
            my: 'left top',
            at: 'right bottom',
            showDelay: 150
        },

        _create: function() {
            this.options.showEvent = this.options.showEvent + '.puitooltip';
            this.options.hideEvent = this.options.hideEvent + '.puitooltip';

            if(this.element.get(0) === document) {
                this._bindGlobal();
            }
            else {
                this._bindTarget();
            }
        },

        _bindGlobal: function() {
            this.container = $('<div class="pui-tooltip pui-tooltip-global ui-widget ui-widget-content ui-corner-all pui-shadow" />').appendTo(document.body);
            this.globalSelector = 'a,:input,:button,img';
            var $this = this;

            $(document).off(this.options.showEvent + ' ' + this.options.hideEvent, this.globalSelector)
                .on(this.options.showEvent, this.globalSelector, null, function() {
                    var target = $(this),
                        title = target.attr('title');

                    if(title) {
                        $this.container.text(title);
                        $this.globalTitle = title;
                        $this.target = target;
                        target.attr('title', '');
                        $this.show();
                    }
                })
                .on(this.options.hideEvent, this.globalSelector, null, function() {
                    var target = $(this);

                    if($this.globalTitle) {
                        $this.container.hide();
                        target.attr('title', $this.globalTitle);
                        $this.globalTitle = null;
                        $this.target = null;
                    }
                });

            var resizeNS = 'resize.puitooltip';
            $(window).unbind(resizeNS).bind(resizeNS, function() {
                if($this.container.is(':visible')) {
                    $this._align();
                }
            });
        },

        _bindTarget: function() {
            this.container = $('<div class="pui-tooltip ui-widget ui-widget-content ui-corner-all pui-shadow" />').appendTo(document.body);

            var $this = this;
            this.element.off(this.options.showEvent + ' ' + this.options.hideEvent)
                .on(this.options.showEvent, function() {
                    $this.show();
                })
                .on(this.options.hideEvent, function() {
                    $this.hide();
                });

            this.container.html(this.options.content);

            this.element.removeAttr('title');
            this.target = this.element;

            var resizeNS = 'resize.' + this.element.attr('id');
            $(window).unbind(resizeNS).bind(resizeNS, function() {
                if($this.container.is(':visible')) {
                    $this._align();
                }
            });
        },

        _align: function() {
            this.container.css({
                left:'',
                top:'',
                'z-index': ++PUI.zindex
            })
                .position({
                    my: this.options.my,
                    at: this.options.at,
                    of: this.target
                });
        },

        show: function() {
            var $this = this;

            this.timeout = window.setTimeout(function() {
                $this._align();
                $this.container.show($this.options.showEffect, {}, $this.options.showEffectSpeed);
            }, this.options.showDelay);
        },

        hide: function() {
            window.learTimeout(this.timeout);

            this.container.hide(this.options.hideEffect, {}, this.options.hideEffectSpeed, function() {
                $(this).css('z-index', '');
            });
        },

        // Added for AngularPrime
        setTooltipContent: function(tooltipContent) {
            this.container.html(tooltipContent);
        }
    });
});