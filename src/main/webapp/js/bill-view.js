document.addEventListener('DOMContentLoaded', function () {
	const printButton = document.querySelector('button[onclick*="print"]');
	if (printButton) {
		printButton.addEventListener('click', function (e) {
			e.preventDefault();
			window.print();
		});
	}
});
