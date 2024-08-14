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
    await callApiAndDrawCart();
}

async function callApiAndDrawCart() {
    let fetchUrl = contextPath + '/product/get-cart';
    let data = await callApi(fetchUrl, "GET")
    if (data.errorCode === 0) {
        document.getElementById("count-cart").innerHTML = data.additionalData.cartCount;
        let listProductInCart = data.additionalData.productList;
        drawProductList(listProductInCart);
    } else {
        alert("Có lỗi khi load giỏ hàng, vui lòng thử lại sau");
    }
}


