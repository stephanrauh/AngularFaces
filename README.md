AngularFaces and BabbageFaces
=============================

<b>AngularFaces</b> is a JSF library making it easy to integrate AngularJS code.<br>
<b>BabbageFaces</b> optimizes the JSF responses. In most cases JSF exchanges an unnecessary large portion of the DOM tree. BabbageFaces fixes this. 

<b>Breaking news:</b>
AngularFaces 2.0 provides AngularJS integration in Apache MyFaces, Oracle Mojarra, PrimeFaces and the new HTML5 friendly style. It doesn't need widgets of its own.

Therefore the AngularJS and AngularDart widgets I already implemented have been moved to a widgets subproject ("AngularFaces-widgets"). 

<b>State of the art:</b>
<ul>
<li>A basic version of AngularFaces-core currently runs under MyFaces with or without PrimeFaces. Mojarra compatibility seems to be temporarily broken.</li>
<li>The Javascript version of the widget library ought the run, but I didn't test it yet after extracting the core project, so don't expect too much.</li>
<li>AngularDart/Prime is currently broken due to API changes of the AngularDart team. Currently I'm waiting for an announced API change to be released, so I put
AngularDart/Prime on hold. As a consequence the Dart version of AngularFaces is temporarily stopped, too.</li>
</ul>

To learn more about AngularFaces, have a look at http://www.beyondjava.net/blog/angularfaces-jsf-beyond-ajax/.

There's also a showcase at http://showcase.angularfaces.com demonstrating the older version of AngularFaces 1.0.

Legal disclaimer:
This is an early version. Use at own risk.
