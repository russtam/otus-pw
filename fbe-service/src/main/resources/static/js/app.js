// app.js
function addToCart(productId, price) {
    const Http = new XMLHttpRequest();
    const url='api/addToCart?productId=' + productId + "&price=" + price;
    Http.open("GET", url);
    Http.send();
}