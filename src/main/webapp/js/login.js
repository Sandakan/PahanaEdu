/**
 * Login Form JavaScript functionality
 * Handles form validation and user interaction
 */

document.addEventListener('DOMContentLoaded', function () {
	// Client-side validation
	const loginForm = document.getElementById('loginForm');
	if (loginForm) {
		loginForm.addEventListener('submit', function (e) {
			const emailField = document.getElementById('email');
			const passwordField = document.getElementById('password');

			if (!emailField || !passwordField) {
				console.error('Form elements not found');
				return;
			}

			// Validate required fields
			if (!app.validateRequiredFields([emailField, passwordField])) {
				e.preventDefault();
				app.showAlert('Please fill in all fields.');
				return false;
			}

			// Validate email format
			const emailValue = emailField.value.trim();
			if (!app.validateEmail(emailValue)) {
				e.preventDefault();
				app.showAlert('Please enter a valid email address.');
				emailField.focus();
				return false;
			}

			// Show loading state
			const submitButton = loginForm.querySelector('button[type="submit"]');
			if (submitButton) {
				app.setButtonLoading(submitButton, true);
			}
		});
	}

	// Focus on email field when page loads
	const emailField = document.getElementById('email');
	if (emailField) {
		emailField.focus();
	}
});
