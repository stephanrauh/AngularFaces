AngularFaces and BabbageFaces
=============================
AngularFaces is a JSF component library aiming to simplify JSF development by allowing you to replace a lot of clever AJAX code with simple AngularJS code. AngularFaces approach to simplicity allows you to reduce boilerplate code as well as an easy way to integrate AngularJS with JSF.

Key features involves:
 - **Bringing JSR 303 annotations to the client.**
 - **Labeled input fiends.**
 - **Validating constraints as you type.**
 - **Easy integration with AngularJS.**

AngularFaces 2.1 provides AngularJS integration in Apache MyFaces, Oracle Mojarra, OmniFaces, PrimeFaces, BootsFaces and the new HTML5 friendly style. It doesn't define widgets of its own. Instead it supports most JSF and PrimeFaces widgets out of the box. Consider it a plug-in taking PrimeFaces and BootsFaces to a whole new level.

The AngularJS and AngularDart widgets I already implemented have been moved to a widgets subproject ("AngularFaces-widgets"). Those widgets make the AngularFaces experience even more fun, but they aren't necessary.

To learn more about AngularFaces have a look at: [ https://www.angularFaces.net ](https://www.angularFaces.net) and [http://www.beyondjava.net/blog/angularfaces-jsf-beyond-ajax/](http://www.beyondjava.net/blog/angularfaces-jsf-beyond-ajax/)

[Learn more about how to get started with AngularFaces.](http://www.beyondjava.net/blog/started-angularfaces)

Installation
---------
####**Maven**
Add these lines to your Maven pom.xml file:
```XML
		<dependency>
			<groupId>de.beyondjava</groupId>
			<artifactId>angularFaces-core</artifactId>
			<version>2.1.2</version>
			<scope>compile</scope>
		</dependency>
```

Contributing
---------
AngularFaces happily accepts contributions to evolve the projects every aspect. The development of AngularFaces happens here as open source on GitHub. We are gratefull to the community for contributions through issues found using AngularFaces as well as bugfixes and improovements. 

####**Information for contributors**
 - AngularFaces-core currently runs under MyFaces or Mojarra, both with or without PrimeFaces and / or BootsFaces.
 - The Javascript version of the widget library ought the run, but it has not beed tested after extracting the core project so it is not guaranteed to work as expected.
 - AngularDart/Prime is currently broken due to API changes of the AngularDart team. Currently I'm waiting for an announced API change to be released, so I put AngularDart/Prime on hold. As a consequence the Dart version of AngularFaces is temporarily stopped, too.

####**Getting started contributing**
If you want to contribute code, fork this repository and then commit your changes to your fork. When you are finished please provide a pull request and it will be reviewed as soon as possible.

####**Reporting Issues**
To file a bug report, please visit the [GitHub issues page](https://github.com/TeamBreak/BManager/issues).  Pleace attatch a detailed explanation of your problem or question as well as code if your issue involves code.

Legal disclaimer:
As stated in the Licence conditions, the software is provided on a "as is" basis, without warranties or conditions of any kind. Use at own risk. Read the license file for the details.

Legal
---------
AngularFaces is maintained under the [Apache 2.0 license](https://github.com/stephanrauh/AngularFaces/blob/master/LICENSE). 
As stated in the Licence conditions, the software is provided on a "as is" basis, without warranties or conditions of any kind. Use at own risk. Read the license file for the details.
