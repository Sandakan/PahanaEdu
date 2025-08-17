document.addEventListener('DOMContentLoaded', function () {
	// Category form validation
	const categoryForm = document.querySelector('form');

	if (categoryForm) {
		categoryForm.addEventListener('submit', function (e) {
			const nameField = document.getElementById('name');

			if (!nameField) {
				console.error('Name field not found');
				return;
			}

			if (!app.validateRequiredFields([nameField])) {
				e.preventDefault();
				app.showAlert('Please fill in the category name.');
				return false;
			}

			const submitButton = categoryForm.querySelector('button[type="submit"]');
			if (submitButton) {
				app.setButtonLoading(submitButton, true);
			}
		});
	}
});
