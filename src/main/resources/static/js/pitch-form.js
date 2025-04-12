document.addEventListener("DOMContentLoaded", function () {
    fetchStocks();
    setInterval(fetchStocks, 10000);

    document.getElementById('pitchFormElement').addEventListener('submit', async function (e) {
        e.preventDefault();

        const stock = document.getElementById('stock').value;
        const quantity = document.getElementById('quantity').value;
        const price = document.getElementById('price').value;
        const action = document.getElementById('action').value;

        if (!stock || !quantity || !price || !action) {
            alert("Please fill all fields before submitting.");
            return;
        }

        try {
            const response = await fetch('/pitch/create', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    stockSymbol: stock,
                    quantity: parseInt(quantity),
                    price: parseFloat(price),
                    action: action
                })
            });

            if (response.ok) {
                alert("Pitch submitted successfully!");
                togglePitchForm();
            } else {
                alert("Failed to submit pitch. Try again.");
            }
        } catch (error) {
            console.error("Error:", error);
            alert("Something went wrong.");
        }
    });
});

async function fetchStocks() {
    const response = await fetch('/api/stocks/live');
    const data = await response.json();

    const tableBody = document.getElementById('stockTableBody');
    tableBody.innerHTML = '';

    data.forEach(stock => {
        const row = document.createElement('tr');
        row.innerHTML = `<td>${stock.symbol}</td><td>${stock.price.toFixed(2)}</td>`;
        tableBody.appendChild(row);
    });
}

async function populateStockDropdown() {
    const response = await fetch('/api/stocks/all');
    const stocks = await response.json();
    const dropdown = document.getElementById('stock');
    dropdown.innerHTML = '';

    stocks.forEach(stock => {
        const option = document.createElement('option');
        option.value = stock.symbol;
        option.text = `${stock.symbol}`;
        dropdown.appendChild(option);
    });
}

function togglePitchForm() {
    const form = document.getElementById('pitchForm');
    form.classList.toggle('active');
    if (form.classList.contains('active')) {
        populateStockDropdown();
    }
}

function togglePitchRequests() {
    const requestsPanel = document.getElementById('pitchRequests');
    requestsPanel.classList.toggle('active');

    if (requestsPanel.classList.contains('active')) {
        fetchPitchRequests();
    }
}

async function fetchPitchRequests() {
    const response = await fetch('/pitch/all');
    const data = await response.json();
    const container = document.getElementById('pitchRequestsList');
    container.innerHTML = '';

    data.forEach(pitch => {
        const card = document.createElement('div');
        card.classList.add('pitch-request-card');
        let statusClass = '';
        if (pitch.status === 'APPROVED') {
            statusClass = 'status-approved';
        } else if (pitch.status === 'REJECTED') {
            statusClass = 'status-rejected';
        } else {
            statusClass = 'status-pending';
        }
        card.innerHTML = `
            <strong>User:</strong> ${pitch.username}<br> <!-- 🆕 Add this line -->
            <strong>Stock:</strong> ${pitch.stockSymbol}<br>
            <strong>Quantity:</strong> ${pitch.quantity}<br>
            <strong>Price:</strong> $${pitch.price}<br>
            <strong>Action:</strong> ${pitch.action}<br>
            <strong>Status:</strong> <span class="${statusClass}">${pitch.status}</span><br>
            <strong>Date:</strong> ${new Date(pitch.createdAt).toLocaleString()}
        `;
        container.appendChild(card);
    });
}

function togglePendingRequests() {
    const requestsPanel = document.getElementById('pendingRequests');
    
    // Toggle the 'active' class to show or hide the panel
    requestsPanel.classList.toggle('active'); // This should now toggle the sliding panel
    
    // If you want to fetch pending requests only when it's active, fetch them here
    if (requestsPanel.classList.contains('active')) {
        fetchPendingRequests(); // Fetch and display pending requests when shown
    }
}


// Fetch Pending Requests for Team Members excluding the current user
async function fetchPendingRequests() {
    const userId = document.getElementById('userId').value; // Get the logged-in user's ID (hidden input or global variable)
    
    try {
        const response = await axios.get('http://localhost:8080/pitch/pending', { params: { userId } });
        const requests = response.data;
        displayPendingRequests(requests);
    } catch (error) {
        console.error('Error fetching pending requests:', error);
    }
}
function displayPendingRequests(requests) {
    const pendingRequestsList = document.getElementById('pendingRequestsList');
    pendingRequestsList.innerHTML = ''; // Clear previous requests
    
    if (requests.length === 0) {
        pendingRequestsList.innerHTML = '<p>No pending requests</p>';
    } else {
        requests.forEach(request => {
            const requestCard = document.createElement('div');
            requestCard.classList.add('pending-request-card');
            requestCard.setAttribute('data-id', request.id); // Add unique ID for each request
            requestCard.innerHTML = `
                <strong>User:</strong> ${request.username}<br> <!-- Corrected here -->
                <strong>Stock:</strong> ${request.stockSymbol}<br>
                <strong>Action:</strong> ${request.action}<br>
                <strong>Quantity:</strong>${request.quantity}<br>
                <strong>Price:</strong> $${request.price}<br>
                <strong>Date:</strong> ${new Date(request.createdAt).toLocaleString()}<br>

                <div class="button-group">
                <button class="accept-btn" onclick="handleAcceptReject(${request.id}, 'accept')">Accept</button>
                <button class="reject-btn" onclick="handleAcceptReject(${request.id}, 'reject')">Reject</button>
                </div>
            `;
            pendingRequestsList.appendChild(requestCard);
        });
    }
}

// Handle Accept/Reject actions (only remove from slider, no backend change)
async function handleAcceptReject(pitchId, action) {
    // Remove the request from the slider (UI)
    const requestCard = document.querySelector(`#pendingRequestsList div[data-id="${pitchId}"]`);
    if (requestCard) {
        requestCard.remove();
    }

    // If needed, you can send the accept/reject info to the backend for future handling (status update)
    const userId = document.getElementById('userId').value; // Get the logged-in user's ID
    
    try {
        await axios.post('/api/pitch/accept-reject', { pitchId, userId, action });
    } catch (error) {
        console.error('Error accepting/rejecting request:', error);
    }
}
