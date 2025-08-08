document.addEventListener('DOMContentLoaded', function () {
	const unitPriceInput = document.getElementById('unitPrice');
	if (unitPriceInput) {
		unitPriceInput.addEventListener('input', function (e) {
			// Remove non numeric values except the dot
			let value = e.target.value.replace(/[^0-9.]/g, '');

			// Make sure that only one decimal point
			const parts = value.split('.');
			if (parts.length > 2) {
				value = parts[0] + '.' + parts.slice(1).join('');
			}

			e.target.value = value;
		});

		unitPriceInput.addEventListener('blur', function (e) {
			const value = parseFloat(e.target.value);
			if (!isNaN(value) && value > 0) {
				e.target.value = value.toFixed(2);
			}
		});
	}

	// Form validation
	const itemForm = document.querySelector('form');
	if (itemForm) {
		itemForm.addEventListener('submit', function (e) {
			const requiredFields = [document.getElementById('name'), document.getElementById('unitPrice')];

			if (requiredFields.some((field) => !field)) {
				console.error('Required form elements not found');
				return;
			}

			if (!app.validateRequiredFields(requiredFields)) {
				e.preventDefault();
				app.showAlert('Please fill in all required fields (Name and Unit Price).');
				return false;
			}

			const unitPriceValue = parseFloat(requiredFields[1].value);
			if (isNaN(unitPriceValue) || unitPriceValue <= 0) {
				e.preventDefault();
				app.showAlert('Please enter a valid unit price (greater than 0).');
				requiredFields[1].focus();
				return false;
			}

			const submitButton = itemForm.querySelector('button[type="submit"]');
			if (submitButton) {
				app.setButtonLoading(submitButton, true);
			}
		});
	}
});
