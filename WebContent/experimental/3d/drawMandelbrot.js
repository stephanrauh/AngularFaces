var container;

var camera, scene, renderer;

var mesh;

var mouseX = 0, mouseY = 0;

var windowHalfX = window.innerWidth / 2;
var windowHalfY = window.innerHeight / 2;
var counter=0;
var resolution=1024;

init();
animate();

function init() {

	container = document.getElementById('mandelbrot');

	camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 10000);
	camera.position.z = 50;
	camera.position.x = 1000;
	camera.position.y = 1500;

	scene = new THREE.Scene();

	var data = generateHeight(resolution, resolution);
	var texture = new THREE.Texture(generateTexture(data, resolution, resolution));
	texture.needsUpdate = true;

	var material = new THREE.MeshBasicMaterial({
		map : texture,
		overdraw : true
	});

	var quality = 2, step = resolution / quality;

	var plane = new THREE.PlaneGeometry(2000, 2000, quality - 1, quality - 1);
	plane.applyMatrix(new THREE.Matrix4().makeRotationX(-Math.PI / 2));

	for ( var i = 0, l = plane.vertices.length; i < l; i++) {

		var x = i % quality, y = ~~(i / quality);
		plane.vertices[i].y = data[(x * step) + (y * step) * resolution] * 2 - 128;

	}

	plane.computeCentroids();

	mesh = new THREE.Mesh(plane, material);
	scene.add(mesh);

	renderer = new THREE.CanvasRenderer();
	renderer.setSize(window.innerWidth, window.innerHeight);

	container.innerHTML = "";

	container.appendChild(renderer.domElement);

	document.addEventListener('mousemove', onDocumentMouseMove, false);

	//

	window.addEventListener('resize', onWindowResize, false);

}

function onWindowResize() {

	windowHalfX = window.innerWidth / 2;
	windowHalfY = window.innerHeight / 2;

	camera.aspect = window.innerWidth / window.innerHeight;
	camera.updateProjectionMatrix();

	renderer.setSize(window.innerWidth, window.innerHeight);

}


function generateTexture(data, width, height) {

	var canvas, context, image, imageData, level, diff, vector3, sun, shade;

	vector3 = new THREE.Vector3(0, 0, 0);

	sun = new THREE.Vector3(1, 1, 1);
	sun.normalize();

	canvas = document.createElement('canvas');
	canvas.width = width;
	canvas.height = height;

	context = canvas.getContext('2d');
	context.fillStyle = '#000';
	context.fillRect(0, 0, width, height);

	image = context.getImageData(0, 0, width, height);
	imageData = image.data;

	for ( var i = 0, j = 0, l = imageData.length; i < l; i += 4, j++) {

		vector3.x = data[j - 1] - data[j + 1];
		vector3.y = 2;
		vector3.z = data[j - width] - data[j + width];
		vector3.normalize();

		shade = vector3.dot(sun);

		imageData[i] = (96 + shade * 128) * (data[j] * 0.007);
		imageData[i + 1] = (32 + shade * 96) * (data[j] * 0.007);
		imageData[i + 2] = (shade * 96) * (data[j] * 0.007);

	}

	context.putImageData(image, 0, 0);

	return canvas;

}

function onDocumentMouseMove(event) {

	mouseX = event.clientX - windowHalfX;
	mouseY = event.clientY; // - windowHalfY;

}

//

function animate() {

	requestAnimationFrame(animate);
    counter++;
    if (counter%60==0)
    	{
	console.log(mouseX + ", " + mouseY + " -> " + camera.position.x + ", "  + camera.position.y+ ", "  + camera.position.z);
    	}
	render();

}

function render() {
	var targetX=mouseX/3;
	var targetY=1000-mouseY/2;
	//console.log(targetY);
	camera.position.x += (targetX - camera.position.x) * 0.05;
	camera.position.y += (targetY-camera.position.y) * 0.05; 
	// camera.position.y += (-mouseY - camera.position.y) * 0.05;
	camera.lookAt(scene.position);

	renderer.render(scene, camera);

}
