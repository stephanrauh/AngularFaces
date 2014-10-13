// if (typeof(PrimeFaces) != "undefined") {
// if (typeof(PrimeFaces.ajax.AjaxUtils)!= "undefined") {
// var primeFacesOriginalSendFunction = PrimeFaces.ajax.AjaxUtils.send;
//	
// PrimeFaces.ajax.AjaxUtils.send = function(cfg){
// var callSource = null;
//		
// // if not string, the caller is a process - in this case we do not interfere
// if(typeof(cfg.source) == 'string') {
// callSource = $(PrimeFaces.escapeClientId(cfg.source));
// }
// // in each case call original send
// primeFacesOriginalSendFunction(cfg);
// if (null != callSource) {
// findNGAppAndReinitAngular(callSource);
// }
// }
// }
// else if (typeof(PrimeFaces.ajax.Request)!= "undefined") {
// var primeFacesOriginalSendFunction = PrimeFaces.ajax.Request.send;
//	
// PrimeFaces.ajax.AjaxUtils.send = function(cfg){
// var callSource = null;
//		
// // if not string, the caller is a process - in this case we do not interfere
// if(typeof(cfg.source) == 'string') {
// callSource = $(PrimeFaces.escapeClientId(cfg.source));
// }
// // in each case call original send
// primeFacesOriginalSendFunction(cfg);
// if (null != callSource) {
// findNGAppAndReinitAngular(callSource);
// }
// }
// }
// }
