document.addEventListener('DOMContentLoaded', function () {
	const deleteButtons = document.querySelectorAll('a[href*="action=delete"]');
	deleteButtons.forEach((button) => {
		button.addEventListener('click', function (e) {
			if (!confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
				e.preventDefault();
				return false;
			}
		});
	});
});
