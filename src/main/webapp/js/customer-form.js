document.addEventListener('DOMContentLoaded', function () {
	const telephoneInput = document.getElementById('telephone');
	if (telephoneInput) {
		telephoneInput.addEventListener('input', function (e) {
			e.target.value = app.formatTelephone(e.target.value, 10);
		});
	}

	// Form validation
	const customerForm = document.querySelector('form');
	if (customerForm) {
		customerForm.addEventListener('submit', function (e) {
			const requiredFields = [
				document.getElementById('name'),
				document.getElementById('address'),
				document.getElementById('telephone'),
			];

			if (requiredFields.some((field) => !field)) {
				console.error('Required form elements not found');
				return;
			}

			if (!app.validateRequiredFields(requiredFields)) {
				e.preventDefault();
				app.showAlert('Please fill in all required fields (Name, Address, and Telephone).');
				return false;
			}

			// Validate telephone length
			const telephoneValue = requiredFields[2].value.trim();
			if (telephoneValue.length < 9) {
				e.preventDefault();
				app.showAlert('Please enter a valid telephone number (at least 9 digits).');
				requiredFields[2].focus();
				return false;
			}

			// Validate email if provided
			const emailField = document.getElementById('email');
			if (emailField && emailField.value.trim() && !app.validateEmail(emailField.value.trim())) {
				e.preventDefault();
				app.showAlert('Please enter a valid email address.');
				emailField.focus();
				return false;
			}

			// Show loading state
			const submitButton = customerForm.querySelector('button[type="submit"]');
			if (submitButton) {
				app.setButtonLoading(submitButton, true);
			}
		});
	}
});
