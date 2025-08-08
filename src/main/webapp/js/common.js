const app = {
	confirmAction: function (message) {
		return confirm(message || 'Are you sure you want to proceed?');
	},

	showAlert: function (message) {
		alert(message);
	},

	validateEmail: function (email) {
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		return emailRegex.test(email);
	},
	formatTelephone: function (value, maxLength) {
		const cleaned = value.replace(/\D/g, '');
		const max = maxLength || 15;
		return cleaned.substring(0, max);
	},

	validateRequiredFields: function (fields) {
		for (let i = 0; i < fields.length; i++) {
			const field = fields[i];
			if (!field.value.trim()) {
				field.focus();
				return false;
			}
		}
		return true;
	},

	focusFirstInput: function () {
		const firstInput = document.querySelector('input[type="text"], input[type="email"], input[type="password"]');
		if (firstInput) {
			firstInput.focus();
		}
	},

	setButtonLoading: function (button, loading) {
		if (loading) {
			button.disabled = true;
			button.dataset.originalText = button.textContent;
			button.textContent = 'Loading...';
		} else {
			button.disabled = false;
			button.textContent = button.dataset.originalText || button.textContent;
		}
	},
};

document.addEventListener('DOMContentLoaded', function () {
	// Auto-focus on first input if no other focus is set
	if (!document.activeElement || document.activeElement === document.body) {
		app.focusFirstInput();
	}

	document.querySelectorAll('[data-confirm]').forEach(function (element) {
		element.addEventListener('click', function (e) {
			const message = this.dataset.confirm;
			if (!app.confirmAction(message)) {
				e.preventDefault();
				return false;
			}
		});
	});
});

window.PahanaEdu = app;
