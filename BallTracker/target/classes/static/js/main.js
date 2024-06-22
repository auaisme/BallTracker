
let ballX = 0, ballY = 0, ballZ = 0;

let ballForm = document.querySelector('#ballFormParent');
let ballFormSubmitButton = document.querySelector('#ballLocationSendButtonParent');
let ballFormSubmitButtonParent = document.querySelector('#ballLocationSendButtonParent');
let ballXInput = document.querySelector('#ballXInput');
let ballYInput = document.querySelector('#ballYInput');
let ballZInput = document.querySelector('#ballZInput');

let ballGraphParent = document.querySelector("#graph");

let usernameInputField = document.querySelector('#usernameInput');

let stompClient = null;
let username = null;

let connectButton = document.querySelector('#connectButton');
let registerButton = document.querySelector('#registerButton');
let disconnectButton = document.querySelector('#disconnectButton');

connectButton.addEventListener('click', connectUser);
registerButton.addEventListener('click', registerUser);
disconnectButton.addEventListener('click', disconnect);
ballFormSubmitButton.addEventListener('click', sendBallData)

disconnectButton.disabled = true;

async function sendBallData() {
    event.preventDefault();

    let x = ballXInput.value;
    let y = ballYInput.value;
    let z = ballZInput.value;

    stompClient.send('/app/update/ball', {}, JSON.stringify({x: x, y: y, z: z}));
}

async function registerUser() {
    event.preventDefault()

    username = usernameInputField.value;

    const response = await fetch('/api/register/user', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({username: username})
    });

    console.log(response);

    if (response.ok)
    {
        connect();
        return;
    }
    alert('User already exists!');
}

async function connectUser() {
    event.preventDefault()

    username = usernameInputField.value;

    const response = await fetch('/api/connect/user', {
       method: 'POST',
        headers: {
           'Content-Type': 'application/json'
        },
        body: JSON.stringify({username: username})
    });

    console.log(response);

    if (response.ok)
    {
        connect();
        return;
    }
    alert('User doesn\'t exist!');
}

function disconnect() {
    event.preventDefault();

    stompClient.disconnect();

    connectButton.disabled = false;
    registerButton.disabled = false;

    disconnectButton.disabled = true;
    ballFormSubmitButton.disabled = true;

    connectButton.classList.remove('hidden');
    registerButton.classList.remove('hidden');

    disconnectButton.classList.add('hidden');
    ballGraphParent.classList.add('hidden');
    ballForm.classList.add('hidden');
    ballFormSubmitButtonParent.classList.add('hidden');
}

function connect()
{
    ballGraphParent.classList.remove('hidden'); // show ball

    let socket = new SockJS('/wss');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, connected, connectError);

    connectButton.disabled = true;
    registerButton.disabled = true;

    disconnectButton.disabled = false;
    ballFormSubmitButton.disabled = false;

    connectButton.classList.add('hidden');
    registerButton.classList.add('hidden');

    disconnectButton.classList.remove('hidden');
    ballGraphParent.classList.remove('hidden');
    ballForm.classList.remove('hidden');
    ballFormSubmitButtonParent.classList.remove('hidden');
}

function connected()
{
    // subscribe to topic:
    stompClient.subscribe('/topic/ball', onBallPositionUpdate);
    stompClient.subscribe('/topic/userlist', onUserListUpdate);
    stompClient.subscribe('/topic/ping', onPingResponse);
}


function onPingResponse(payload) {
    console.log(payload);
}

function onBallPositionUpdate(payload)
{
    console.log("Ball position updated");

    let response = JSON.parse(payload.body);

    console.log(response);

    ballX = response.x; ballY = response.y; ballZ = response.z;

    console.log("Ball position updated: ", ballX, ballY, ballZ);

    updateBallPosition();
}

function onUserListUpdate(payload) {
    console.log("Message received");

    let message = JSON.parse(payload.body);

    console.log(message);
}

function connectError()
{
    console.log("Failed to connect!");
}

// BALL AND GRID

// Scene, Camera, Renderer setup
let sphere = null;
const scene = new THREE.Scene();
const aspect = window.innerWidth / window.innerHeight;
const camera = new THREE.PerspectiveCamera(75, aspect, 0.1, 1000);
const renderer = new THREE.WebGLRenderer();
renderer.setSize(window.innerWidth, window.innerHeight);

ballGraphParent.appendChild(renderer.domElement);

// Set the camera position
camera.position.set(5, 5, 5);
camera.lookAt(scene.position);

// Add Axes Helper
const axesHelper = new THREE.AxesHelper(1000);  // 1000 is the length of each axis
scene.add(axesHelper);

// Add Grid Helper for XZ plane
const gridHelperXZ = new THREE.GridHelper(1000, 1000);  // 1000 units, 1000 divisions
scene.add(gridHelperXZ);

// Function to create a point
function createPoint(x, y, z) {
    const geometry = new THREE.SphereGeometry(0.1, 32, 32);
    const material = new THREE.MeshBasicMaterial({ color: 0xff0000 });
    sphere = new THREE.Mesh(geometry, material);
    sphere.position.set(x, y, z);
    scene.add(sphere);
}

// Add some points
createPoint(ballX, ballY, ballZ);

function updateBallPosition() {
    if (sphere) {
        sphere.position.set(ballX, ballY, ballZ);
    }
}

// Render function
function animate() {
    requestAnimationFrame(animate);
    renderer.render(scene, camera);
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();

    renderer.setSize(window.innerWidth, window.innerHeight);
}

window.addEventListener("resize", onWindowResize);

animate();
