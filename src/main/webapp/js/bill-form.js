let billItems = [];
let itemIdCounter = 1;

document.addEventListener('DOMContentLoaded', function () {
	initializeBillForm();
	updateItemsDisplay();
	calculateTotal();
});

function loadExistingBillItems(existingItems) {
	billItems = existingItems.map((item) => ({
		id: item.id,
		itemId: item.itemId,
		name: item.name,
		category: item.category,
		unitPrice: parseFloat(item.unitPrice),
		quantity: parseInt(item.quantity),
		lineTotal: parseFloat(item.lineTotal),
	}));

	// Update the itemIdCounter to avoid ID conflicts
	itemIdCounter = Math.max(...billItems.map((item) => item.id)) + 1;

	updateItemsDisplay();
	calculateTotal();
}

function initializeBillForm() {
	const form = document.getElementById('billForm');
	if (form) {
		form.addEventListener('submit', validateForm);
	}
}

function addItemToBill() {
	const itemSelect = document.getElementById('itemSelect');
	const quantityInput = document.getElementById('quantityInput');

	if (!itemSelect.value) {
		app.showAlert('Please select an item');
		itemSelect.focus();
		return;
	}

	const quantity = parseInt(quantityInput.value);
	if (!quantity || quantity <= 0) {
		app.showAlert('Please enter a valid quantity');
		quantityInput.focus();
		return;
	}

	const selectedOption = itemSelect.options[itemSelect.selectedIndex];
	const itemId = parseInt(selectedOption.value);
	const itemName = selectedOption.dataset.name;
	const itemPrice = parseFloat(selectedOption.dataset.price);
	const itemCategory = selectedOption.dataset.category;

	// Check if item already exists
	const existingIndex = billItems.findIndex((item) => item.itemId === itemId);
	if (existingIndex !== -1) {
		billItems[existingIndex].quantity += quantity;
		billItems[existingIndex].lineTotal = billItems[existingIndex].quantity * billItems[existingIndex].unitPrice;
	} else {
		const newItem = {
			id: itemIdCounter++,
			itemId: itemId,
			name: itemName,
			category: itemCategory,
			unitPrice: itemPrice,
			quantity: quantity,
			lineTotal: itemPrice * quantity,
		};
		billItems.push(newItem);
	}

	itemSelect.value = '';
	quantityInput.value = '1';
	itemSelect.focus();

	updateItemsDisplay();
	calculateTotal();
}

function removeItem(itemIndex) {
	if (app.confirmAction('Are you sure you want to remove this item?')) {
		billItems.splice(itemIndex, 1);
		updateItemsDisplay();
		calculateTotal();
	}
}

function updateItemQuantity(itemIndex, newQuantity) {
	const quantity = parseInt(newQuantity);
	if (quantity > 0) {
		billItems[itemIndex].quantity = quantity;
		billItems[itemIndex].lineTotal = billItems[itemIndex].unitPrice * quantity;
		calculateTotal();
	} else {
		removeItem(itemIndex);
	}
}

function updateItemsDisplay() {
	const container = document.getElementById('items-container');

	if (billItems.length === 0) {
		container.innerHTML = `
            <div class="no-items-message">
                No items added yet. Select items from the dropdown above to add them to the bill.
            </div>
        `;
		return;
	}

	let html = '';
	billItems.forEach((item, index) => {
		html += `
            <div class="item-row">
                <div class="item-info">
                    <div class="item-name">${item.name}</div>
                    ${item.category ? `<div class="item-category">${item.category}</div>` : ''}
                </div>
                <div class="item-price">LKR ${item.unitPrice.toFixed(2)}</div>
                <div class="item-quantity">
                    <input type="number" 
                           value="${item.quantity}" 
                           min="1" 
                           onchange="updateItemQuantity(${index}, this.value)"
                           onblur="updateItemQuantity(${index}, this.value)">
                </div>
                <div class="item-total">LKR ${item.lineTotal.toFixed(2)}</div>
                <div class="item-actions">
                    <button type="button" class="btn-remove" onclick="removeItem(${index})" title="Remove Item">
                        ✕
                    </button>
                </div>
                <input type="hidden" name="items[${index}].itemId" value="${item.itemId}">
                <input type="hidden" name="items[${index}].quantity" value="${item.quantity}">
                <input type="hidden" name="items[${index}].unitPrice" value="${item.unitPrice}">
            </div>
        `;
	});

	container.innerHTML = html;
}

function calculateTotal() {
	const total = billItems.reduce((sum, item) => sum + item.lineTotal, 0);
	const totalElement = document.getElementById('totalAmount');
	if (totalElement) {
		totalElement.textContent = total.toFixed(2);
	}
}

function validateForm(event) {
	const form = event.target;
	const errors = [];

	if (!form.customerId.value) {
		errors.push('Please select a customer');
	}

	if (!form.paymentMethod.value) {
		errors.push('Please select a payment method');
	}

	if (!form.paymentStatus.value) {
		errors.push('Please select a payment status');
	}

	if (billItems.length === 0) {
		errors.push('Please add at least one item to the bill');
	}

	if (errors.length > 0) {
		event.preventDefault();
		app.showAlert('Please correct the following errors:\n• ' + errors.join('\n• '));
		return false;
	}

	const submitBtn = form.querySelector('button[type="submit"]');
	if (submitBtn) {
		app.setButtonLoading(submitBtn, true);
	}

	return true;
}
