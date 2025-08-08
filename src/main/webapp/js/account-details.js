document.addEventListener('DOMContentLoaded', function () {
	const deleteButtons = document.querySelectorAll('.btn-danger[href*="action=delete"]');
	deleteButtons.forEach(function (button) {
		button.addEventListener('click', function (e) {
			if (!app.confirmAction('Are you sure you want to delete this customer?')) {
				e.preventDefault();
				return false;
			}
		});
	});
});
