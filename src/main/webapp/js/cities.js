const URL = 'http://localhost:8080/dreamjob/cities.do';

const addCityBtn = document.getElementById("add_city");
const cityNameInput = document.getElementById("new_city");

addCityBtn.onclick = function () {
    let newCityName = {
        name: cityNameInput.value
    }
    fetch(URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(newCityName)
    })
        .then(response => response.json())
        .then(result => createNewCityRow(result));
}

function createNewCityRow(city) {
    const row = document.createElement('tr');
    const colId = document.createElement('td');
    const cityId = document.createTextNode(city.id);
    colId.appendChild(cityId);
    row.appendChild(colId);
    const colName = document.createElement('td');
    const cityName = document.createTextNode(city.name);
    colName.appendChild(cityName);
    row.appendChild(colName);
    document.getElementById("cityTable").appendChild(row);
}