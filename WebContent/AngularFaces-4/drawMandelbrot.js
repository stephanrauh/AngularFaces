function activatePlaneDemo() {
	document.getElementById('mandelbrot').innerHTML += 'done.<br />3-d rendering on the client...';
	window.setTimeout('initPlane(80, 256, 4);animatePlane();', 60);
	stopAnimation = false;
}

function initPlane(aperture, resolution, quality) {
	quality = Math.pow(2, (5 - quality));
	stopAnimation = true;
	var start = new Date().getTime();
	container = document.getElementById('mandelbrot');
	console.log(aperture);

	camera = new THREE.PerspectiveCamera(aperture, window.innerWidth / window.innerHeight, 1, 10000);
	camera.position.z = 500;
	camera.position.x = 0;
	camera.position.y = 1000;

	scene = new THREE.Scene();

	var data = generateHeight();
	var texture = new THREE.Texture(generateTexture(data, resolution, resolution));
	texture.needsUpdate = true;

	var material = new THREE.MeshBasicMaterial({
		map : texture,
		overdraw : true
	});

	var step = resolution / quality;

	var plane = new THREE.PlaneGeometry(2000, 2000, quality - 1, quality - 1);
	plane.applyMatrix(new THREE.Matrix4().makeRotationX(-Math.PI / 2));

	for ( var i = 0, l = plane.vertices.length; i < l; i++) {

		var x = i % quality, y = ~~(i / quality);
		var currentPixel = data[(x * step) + (y * step) * resolution];
		plane.vertices[i].y = 1023 - currentPixel;

	}

	plane.computeCentroids();

	mesh = new THREE.Mesh(plane, material);
	scene.add(mesh);

	renderer = new THREE.CanvasRenderer();
	renderer.setSize(window.innerWidth, window.innerHeight);

	container.innerHTML = "";

	console.log("done:" + (new Date().getTime() - start));

	container.appendChild(renderer.domElement);
	renderer.domElement.addEventListener('mousemove', onDocumentMouseMovePlane, false);

	window.addEventListener('resize', onWindowResize, false);
	stopAnimationPlane = false;

}

function generateTexture(data, width, height) {

	var canvas, context, image, imageData, vector3, sun, shade;

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
		var pixel = data[j];
		if (pixel == 1023) {
			imageData[i] = 0;
			imageData[i + 1] = 0;
			imageData[i + 2] = 192; // blue
		} else {
			if (pixel < 32) {
				var green = (32 + shade * 96) * (pixel * 0.007) * 16;
				green += (32 - pixel) * 3;
				if (green > 255) {
					green = 255;
				}
				imageData[i] = (96 + shade * 128) * (pixel * 0.007) * 16;
				imageData[i + 1] = green;
				imageData[i + 2] = (shade * 96) * (pixel * 0.007) * 16;

			} else {
				pixel = Math.sqrt(pixel);
				imageData[i] = (96 + shade * 128) * (pixel * 0.007) * 16;
				imageData[i + 1] = (32 + shade * 96) * (pixel * 0.007) * 16;
				imageData[i + 2] = (shade * 96) * (pixel * 0.007) * 16;
			}
		}

	}

	context.putImageData(image, 0, 0);
	return canvas;

}

function onDocumentMouseMovePlane(event) {

	mouseX = event.clientX - windowHalfX;
	mouseY = event.clientY;

}

function animatePlane() {
	if (!stopAnimationPlane) {
		requestAnimationFrame(animatePlane);
		renderPlane();
	}
}

function renderPlane() {
	var targetX = mouseX;
	var targetY = 3500 - (mouseY * 2);
	if (targetY < 1050)
		targetY = 1050;
	var deltaX = (targetX - camera.position.x) * 0.05;
	camera.position.x += deltaX; // converges smoothly to targetX
	var deltaY = (targetY - camera.position.y) * 0.05;
	camera.position.y += deltaY; // converges smoothly to targetY
	camera.lookAt(scene.position);
	if (deltaY > 1 || deltaY < -1 || deltaX > 1 || deltaX < -1) {
		renderer.render(scene, camera);
	}

}
