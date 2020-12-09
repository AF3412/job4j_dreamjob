const form  = document.getElementsByTagName('form')[0];

form.addEventListener('submit', function (event) {
    const userName = document.getElementById('name');
    const city = document.getElementById('city');
    const photo = document.getElementById('customFile');
    if (!userName || (city.value === '0') || photo.files.length === 0) {
        event.preventDefault();
        alert('Необходимо заполнить все поля!');
    }
});