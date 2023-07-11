function togglePasswordVisibility(icon) {
    let pwdField = icon.previousElementSibling;

    if (pwdField.type === 'password') {
        pwdField.type = 'text';
        icon.classList.remove('fa-eye');
        icon.classList.add('fa-eye-slash');
    } else {
        pwdField.type = 'password';
        icon.classList.remove('fa-eye-slash');
        icon.classList.add('fa-eye');
    }
}