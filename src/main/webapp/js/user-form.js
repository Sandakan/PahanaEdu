document.addEventListener('DOMContentLoaded', function () {
	const form = document.querySelector('.form-container');
	const password = document.getElementById('password');
	const confirmPassword = document.getElementById('confirmPassword');

	if (form) {
		form.addEventListener('submit', function (e) {
			if (!validateForm()) {
				e.preventDefault();
				return false;
			}
		});
	}

	function validateForm() {
		const firstNameField = document.getElementById('firstName');
		const lastNameField = document.getElementById('lastName');
		const emailField = document.getElementById('email');
		const roleField = document.getElementById('role');
		const requiredFields = [firstNameField, lastNameField, emailField, roleField];

		if (!app.validateRequiredFields(requiredFields)) {
			app.showAlert('Please fill in all required fields.');
			return false;
		}

		if (!isValidEmail(emailField.value)) {
			app.showAlert('Please enter a valid email address.');
			emailField.focus();
			return false;
		}

		if (password && confirmPassword) {
			const isEdit = document.querySelector('input[name="action"]').value === 'update';
			const passwordValue = password.value;
			const confirmPasswordValue = confirmPassword.value;

			if (!isEdit && passwordValue.trim() === '') {
				app.showAlert('Password is required.');
				password.focus();
				return false;
			}

			if (passwordValue.trim() !== '') {
				if (passwordValue.length < 6) {
					app.showAlert('Password must be at least 6 characters long.');
					password.focus();
					return false;
				}

				if (passwordValue !== confirmPasswordValue) {
					app.showAlert('Passwords do not match.');
					confirmPassword.focus();
					return false;
				}
			} else if (confirmPasswordValue.trim() !== '') {
				app.showAlert('Please enter a password or clear the confirm password field.');
				password.focus();
				return false;
			}
		}

		return true;
	}

	function isValidEmail(email) {
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		return emailRegex.test(email);
	}

	if (confirmPassword) {
		confirmPassword.addEventListener('input', function () {
			if (password.value !== confirmPassword.value) {
				confirmPassword.setCustomValidity('Passwords do not match');
			} else {
				confirmPassword.setCustomValidity('');
			}
		});
	}
});
