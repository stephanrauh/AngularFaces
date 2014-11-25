AngularFaces and BabbageFaces
=============================

<b>AngularFaces</b> is a JSF library making it easy to integrate AngularJS code.<br>

AngularFaces 2.0 provides AngularJS integration in Apache MyFaces, Oracle Mojarra, PrimeFaces and the new HTML5 friendly style. It doesn't need widgets of its own. Instead it 
supports most JSF and PrimeFaces widgets out of the box.

Therefore the AngularJS and AngularDart widgets I already implemented have been moved to a widgets subproject ("AngularFaces-widgets"). Those widgets make
the AngularFaces experience even more fun, but they aren't necessary.

<b>State of the art:</b>
<ul>
<li>A basic version of AngularFaces-core currently runs under MyFaces or Mojarra, both with or without PrimeFaces.</li>
<li>The Javascript version of the widget library ought the run, but I didn't test it yet after extracting the core project, so don't expect too much.</li>
<li>AngularDart/Prime is currently broken due to API changes of the AngularDart team. Currently I'm waiting for an announced API change to be released, so I put
AngularDart/Prime on hold. As a consequence the Dart version of AngularFaces is temporarily stopped, too.</li>
</ul>

To learn more about AngularFaces, have a look at http://www.beyondjava.net/blog/angularfaces-jsf-beyond-ajax/.

There's also a showcase at http://showcase.angularfaces.com demonstrating the older version of AngularFaces 1.0.

Legal disclaimer:
This is an early version. Use at own risk.
