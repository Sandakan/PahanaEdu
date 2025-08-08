document.addEventListener('DOMContentLoaded', function () {
	// Handle delete confirmations with category name
	const deleteButtons = document.querySelectorAll('a[href*="action=delete"]');
	deleteButtons.forEach(function (button) {
		button.addEventListener('click', function (e) {
			const categoryRow = this.closest('tr');
			const categoryName = categoryRow.querySelector('td:nth-child(2)').textContent.trim();

			if (
				!app.confirmAction(
					`Are you sure you want to delete the category "${categoryName}"? This action cannot be undone.`
				)
			) {
				e.preventDefault();
				return false;
			}
		});
	});
});
