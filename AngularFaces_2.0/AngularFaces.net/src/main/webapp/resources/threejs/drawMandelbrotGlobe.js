var container;

var camera, scene, renderer;

var mesh;

var mouseX = 0, mouseY = 0;

var windowHalfX = window.innerWidth / 2;
var windowHalfY = window.innerHeight / 2;
var counter = 0;
var stopAnimation = false;
var stopAnimationPlane = true;

var group;

function onWindowResize() {

    windowHalfX = window.innerWidth / 2;
    windowHalfY = window.innerHeight / 2;

//    camera.aspect = window.innerWidth / window.innerHeight;
    camera.aspect = 1;
    camera.updateProjectionMatrix();

//    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.setSize(300,300);

}

function activateGlobeDemo() {
	stopAnimationPlane = true;
	stopAnimation = false;
	init();
	animate();
}

function init() {

	container = document.getElementById('mandelbrot');
	var ratio = 1;
	camera = new THREE.PerspectiveCamera(60, ratio, 1, 2000);
	camera.position.z = 500;

	scene = new THREE.Scene();

	group = new THREE.Object3D();
	scene.add(group);

	// load mandelbrot set as texture for the globe
	// determine which subfolder we're in
	var image="globe/Mandelbrot2.png";
	var loader = new THREE.TextureLoader();
	var url=window.location.href;
	var pos = url.indexOf("version2.1");
	if (pos>0) {
	  image = "../" + image;
	  pos = url.indexOf("/", pos+12);
	  if (pos>0) {
	    image="../"+image;
	    if (url.indexOf("/", pos+1)>0)
	      image="../"+image;
	  }
	}
	loader.load(image, function(texture) {

		var geometry = new THREE.SphereGeometry(200, 20, 20);

		var material = new THREE.MeshBasicMaterial({
			map : texture,
			overdraw : true
		});
		var mesh = new THREE.Mesh(geometry, material);
		group.add(mesh);

	});

	// shadow

	var canvas = document.createElement('canvas');
	canvas.width = 128;
	canvas.height = 128;

	var context = canvas.getContext('2d');
	var gradient = context.createRadialGradient(canvas.width / 2, canvas.height / 2, 0, canvas.width / 2,
			canvas.height / 2, canvas.width / 2);
	gradient.addColorStop(0.1, 'rgba(210,210,210,1)');
	gradient.addColorStop(1, 'rgba(255,255,255,1)');

	context.fillStyle = gradient;
	context.fillRect(0, 0, canvas.width, canvas.height);

	var texture = new THREE.Texture(canvas);
	texture.needsUpdate = true;

	var geometry = new THREE.PlaneGeometry(300, 300, 3, 3);
	var material = new THREE.MeshBasicMaterial({
		map : texture,
		overdraw : true
	});

	var mesh = new THREE.Mesh(geometry, material);
	mesh.position.y = -250;
	mesh.rotation.x = -Math.PI / 2;
	group.add(mesh);

	renderer = new THREE.CanvasRenderer();
	
	container.innerHTML = "";
	container.appendChild(renderer.domElement);

	document.addEventListener('mousemove', onDocumentMouseMove, false);
	window.addEventListener('resize', onWindowResize, false);

}

function onDocumentMouseMove(event) {
	mouseX = (event.clientX - windowHalfX);
	mouseY = (event.clientY - windowHalfY);
}

function animate() {
	if (!stopAnimation) {
		requestAnimationFrame(animate);
	}
	render();
}

function render() {
    var size=container.clientWidth;
    if (size>300) size=300;
    renderer.setSize(size,size);

	camera.position.x += (mouseX - camera.position.x) * 0.03;
	camera.position.y += (-mouseY - camera.position.y) * 0.03;
	camera.lookAt(scene.position);
	group.rotation.y -= 0.005;
	renderer.render(scene, camera);
}
