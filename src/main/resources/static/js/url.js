function contactUrlBulder() {    
    return '/api/contact';
}
function contactUrlBulderById(id) {    
    return `/api/contact/${
            encodeURIComponent(id)
            }`;
}
function searchUrlBulder(page, query) {

    return `/api/contact/list/${
            encodeURIComponent(page)
            }/${
            encodeURIComponent(query)
            }`;
}