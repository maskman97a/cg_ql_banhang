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

const BaseResponse = {
    errorCode: 0,
    errorMessage: '',
    additionalData: {
        cartCount: 0,
        cartProductList: []
    }
}

async function addToCart(productId) {
    let fetchUrl = contextPath + '/product/add-to-cart?productId=' + productId;
    let data = await callApi(fetchUrl, "GET")
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
        .then(response => response.json())
        .then(data => {
            return data
        })
        .catch(error => console.log(error));
}

async function openCart() {
    document.getElementById("btn-open-cart").click();

    let returnHtml = "";
    let fetchUrl = contextPath + '/product/get-cart';
    let data = await callApi(fetchUrl, "GET")
    if (data.errorCode === 0) {
        let listProductInCart = data.additionalData.productList;
        for (let i = 0; i < listProductInCart.length; i++) {
            returnHtml += `<tr>`
            returnHtml += `<td>` + listProductInCart[i].index + `</td>`
            returnHtml += `<td>` + listProductInCart[i].product.productName + `</td>`
            returnHtml += `<td>` + formatNumber(listProductInCart[i].quantity) + `</td>`
            returnHtml += `<td>` + formatNumber(listProductInCart[i].product.price) + `</td>`
            returnHtml += `<td>` + formatNumber(listProductInCart[i].amount) + `</td>`
            returnHtml += "</tr>"
        }
        document.getElementById("tbody-table-cart").innerHTML = returnHtml;
    } else {
        alert("Có lỗi khi load giỏ hàng, vui lòng thử lại sau");
    }
}

function encodeHTML(str) {
    var aStr = str.split(''),
        i = aStr.length,
        aRet = [];

    while (--i) {
        var iC = aStr[i].charCodeAt();
        if (iC < 65 || iC > 127 || (iC > 90 && iC < 97)) {
            aRet.push('&#' + iC + ';');
        } else {
            aRet.push(aStr[i]);
        }
    }
    return aRet.reverse().join('');
}


