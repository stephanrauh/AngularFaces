AngularFaces and BabbageFaces
=============================

<b>AngularFaces 2.1 has been released!</b>
Add these lines to your Maven pom.xml file:
		<dependency>
			<groupId>de.beyondjava</groupId>
			<artifactId>angularFaces-core</artifactId>
			<version>2.1.2</version>
			<scope>compile</scope>
		</dependency>


<b>AngularFaces</b> is a JSF library making it easy to integrate AngularJS code.<br>

AngularFaces 2.1 provides AngularJS integration in Apache MyFaces, Oracle Mojarra, OmniFaces, PrimeFaces, BootsFaces and the new HTML5 friendly style. It doesn't define widgets of its own. Instead it 
supports most JSF and PrimeFaces widgets out of the box. Consider it a plug-in taking PrimeFaces and BootsFaces to a whole new level.

Therefore the AngularJS and AngularDart widgets I already implemented have been moved to a widgets subproject ("AngularFaces-widgets"). Those widgets make
the AngularFaces experience even more fun, but they aren't necessary.

<b>State of the art:</b>
<ul>
<li>AngularFaces-core currently runs under MyFaces or Mojarra, both with or without PrimeFaces and / or BootsFaces.</li>
<li>The Javascript version of the widget library ought the run, but I didn't test it yet after extracting the core project, so don't expect too much.</li>
<li>AngularDart/Prime is currently broken due to API changes of the AngularDart team. Currently I'm waiting for an announced API change to be released, so I put
AngularDart/Prime on hold. As a consequence the Dart version of AngularFaces is temporarily stopped, too.</li>
</ul>

To learn more about AngularFaces, have a look at https://www.angularFaces.net and http://www.beyondjava.net/blog/angularfaces-jsf-beyond-ajax/.

There's also a showcase at http://showcase.angularfaces.com demonstrating the older version of AngularFaces 1.0.

Legal disclaimer:
As stated in the Licence conditions, the software is provided on a "as is" basis, without warranties or conditions of any kind. Use at own risk. Read the license file for the details.

