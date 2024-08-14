let contextPath = window.location.origin;

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

(() => {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    const forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
            }

            form.classList.add('was-validated')
        }, false)
    })
})()

function addToCart(productId) {
    let fetchUrl = contextPath + '/product/add-to-cart?productId=' + productId;
    let data = callApi(fetchUrl, "GET", null);
    if (data.errorCode === 0) {
        alert("Thêm hàng vào giỏ thành công!");
        document.getElementById("count-cart").innerHTML = data.additionalData.cartCount;
    } else {
        alert("Thêm vào giỏ hàng thất bại");
    }

}

function callApi(apiUrl, method, body) {
    return fetch(apiUrl, {
        method: method, body: body
    })
        .then(response => response.json)
        .then(data => data)
        .catch(error => error);
}

function openCart() {
    document.getElementById("btn-open-cart").click();

    let returnHtml = "";
    let data = callApiToGetCart()
    if (data.errorCode === 0) {
        let listProductInCart = data.additionalData.cartProductList
        for (let i = 0; i < listProductInCart.length; i++) {
            returnHtml += `<tr>`
            returnHtml += `<td>${listProductInCart[i].index}</td>`
            returnHtml += `<td>${listProductInCart[i].product.productName}</td>`
            returnHtml += `<td>${listProductInCart[i].quantity}</td>`
            returnHtml += `<td>${listProductInCart[i].product.price}</td>`
            returnHtml += `<td>${listProductInCart[i].amount}</td>`
            returnHtml += "</tr>"
        }
        document.getElementById("tbody-table-cart").innerHTML = returnHtml;
    } else {
        alert("Có lỗi khi load giỏ hàng, vui lòng thử lại sau");
    }
}

function callApiToGetCart() {
    let fetchUrl = contextPath + '/product/get-cart';
    return callApi(fetchUrl, "GET")
}



