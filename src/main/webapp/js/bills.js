document.addEventListener('DOMContentLoaded', function () {
	const deleteButtons = document.querySelectorAll('a[href*="action=delete"]');
	deleteButtons.forEach((button) => {
		button.addEventListener('click', function (e) {
			e.preventDefault();
			const billId = this.href.match(/id=(\d+)/)?.[1];
			if (app.confirmAction(`Are you sure you want to delete Bill #${billId}? This action cannot be undone.`)) {
				window.location.href = this.href;
			}
		});
	});

	const paidButtons = document.querySelectorAll('a[href*="status=PAID"]');
	paidButtons.forEach((button) => {
		button.addEventListener('click', function (e) {
			e.preventDefault();
			const billId = this.href.match(/id=(\d+)/)?.[1];
			if (app.confirmAction(`Mark Bill #${billId} as paid?`)) {
				window.location.href = this.href;
			}
		});
	});
});
