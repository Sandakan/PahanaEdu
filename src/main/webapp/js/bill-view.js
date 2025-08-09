document.addEventListener('DOMContentLoaded', function () {
	const printButton = document.querySelector('#printBillBtn');
	if (printButton) {
		printButton.addEventListener('click', function (e) {
			e.preventDefault();
			window.print();
		});
	}
});
