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
        option.text = `${stock.symbol} - ${stock.name}`;
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
