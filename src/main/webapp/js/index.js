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

async function addToCart(productId, productImageElementId) {
    let fetchUrl = contextPath + '/product/add-to-cart?productId=' + productId;
    let data = await callApi(fetchUrl, "GET")
    if (data.errorCode === 0) {
        document.getElementById("count-cart").innerHTML = data.additionalData.cartCount;
        animationCart(productImageElementId);
    } else {
    }
}

async function callApi(apiUrl, method, body) {
    return await fetch(apiUrl, {
        method: method,
        body: body,
        headers: {
            'Content-Type': 'application/json; charset=UTF-8'
        }
    })
        .then(response => {
            return response.arrayBuffer();
        }).then(buffer => {
            // Decode the ArrayBuffer to a string using TextDecoder
            const decoder = new TextDecoder('utf-8');
            const jsonString = decoder.decode(buffer);
            // Parse the JSON string into an object
            return JSON.parse(jsonString)
        })
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
        return drawProductList(listProductInCart);
    } else {
        alert("Có lỗi khi load giỏ hàng, vui lòng thử lại sau");
    }
}

function animationCart(productImageElementId) {
    const productImage = document.getElementById(productImageElementId);
    const cart = document.getElementById('cart-icon');

    // Clone the product image
    const flyImage = productImage.cloneNode(true);
    flyImage.classList.add('product-image-fly');
    document.body.appendChild(flyImage);

    // Get the position of the image and the cart
    const productRect = productImage.getBoundingClientRect();
    const cartRect = cart.getBoundingClientRect();

    // Set the initial position of the fly image
    flyImage.style.left = productRect.left + 'px';
    flyImage.style.top = productRect.top + 'px';
    flyImage.style.width = productRect.width + 'px';

    // Calculate the translation needed
    const translateX = (cartRect.left + cartRect.width / 2) - (productRect.left + productRect.width / 2);
    const translateY = (cartRect.top + cartRect.height / 2) - (productRect.top + productRect.height / 2);

    // Apply the translation
    setTimeout(() => {
        flyImage.style.transform = `translate(${translateX}px, ${translateY}px) scale(0.1)`;
        flyImage.style.opacity = '0';
        flyImage.style.zIndex = `1000`;
    }, 100);

    // Remove the fly image after the animation
    setTimeout(() => {
        flyImage.remove();
    }, 3000);
}


function renderErrorMessage(elementId, message) {
    document.getElementById(elementId).innerHTML = message;
    return false;
}

function openConfirmDialog(openDialogBtnId, confirmMessage, formElementId) {
    let openDialogButton = document.getElementById(openDialogBtnId);
    openDialogButton.click();
    let confirmMessageLabel = document.getElementById("confirm-message");
    confirmMessageLabel.innerHTML = confirmMessage;
    let confirmBtn = document.getElementById("confirm-button");
    confirmBtn.onclick = function () {
        submitFormByElementIdComponentInForm(formElementId);
    }
}

function submitFormByElementIdComponentInForm(elementId) {
    const inputElement = document.getElementById(elementId);

// Find the closest ancestor that is a form
    inputElement.closest('form').submit();
}

