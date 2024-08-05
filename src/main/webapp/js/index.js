function formatNumber(number) {
    return number.toLocaleString();
}

formatAllNumbers();

function formatAllNumbers() {
    let elements = document.getElementsByClassName('formatted-number');
    for (let i = 0; i < elements.length; i++) {
        elements.item(i).value = formatNumber(elements.item(i).value())
    }
}


