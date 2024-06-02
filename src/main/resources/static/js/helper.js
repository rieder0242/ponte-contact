/* global doT */

function elementAboveBottomViewport(e) {
    let top = e.offsetTop;

    while (e.offsetParent) {
        e = e.offsetParent;
        top += e.offsetTop;
    }
    return top < window.pageYOffset + window.innerHeight;
}
function insertAfter(referenceNode, newNode) {
    referenceNode.parentNode.insertBefore(newNode, referenceNode.nextSibling);
}
function insertBefore(referenceNode, newNode) {
    referenceNode.parentNode.insertBefore(newNode, referenceNode);
}

((global) => {
    let div = document.createElement('div');
    global.html2Element = (html) => {
        div.innerHTML = html;
        return div.firstElementChild;
    };

    let templates = {};

    global.template = (name, it) => {
        if (!templates[name]) {
            templates[name] = doT.template(document.querySelector('#dot-' + name).innerHTML);
        }
        return html2Element(templates[name](it));
    };

})(window);

class Notify {
    constructor(msg, clazz) {
        let e = html2Element(doT.template(document.querySelector('#dot-notify').innerHTML)({msg: msg, class: clazz}));
        document.body.appendChild(e);
        setTimeout(() => {
            e.classList.add('out');
            setTimeout(() => {
                e.remove();
            }, 1500);
        }, 3000);
    }
}
class Modal {
    constructor() {
        this.element = document.createElement("div");
        this.element.classList.add("modal", "hide");
        document.body.appendChild(this.element);
        this.click = () => {
        };
        this.element.addEventListener("click", (e) => this.modalClick(e));
    }
    hide() {
        this.element.classList.add("hide");
    }
    show() {
        this.element.classList.remove("hide");
    }
    appendChild(e) {
        this.element.appendChild(e);

    }
    onclick(f) {
        this.click = f;
    }
    modalClick(e) {
        if (e.target === this.element) {
            this.click(e);
        }
    }
}
class MessageBox {
    constructor(text, clazz) {
        this.modal = new Modal();
        this.element = html2Element(doT.template(document.querySelector('#dot-message').innerHTML)({text: text, clazz: clazz}));
        this.modal.appendChild(this.element);
        this.modal.onclick(() => {
            this.hide();
            this.resolve("quit");
        });
        this.buttons = this.element.querySelector('.buttons');
    }
    addButton(text, value, clazz) {
        let button = html2Element(doT.template(document.querySelector('#dot-message-button').innerHTML)({text: text, class: clazz}));
        button.addEventListener("click", (e) => {
            this.hide();
            this.resolve(value);
        });
        this.buttons.appendChild(button);
        return this;
    }
    show() {
        this.modal.show();
        return new Promise((resolve) => {
            this.resolve = resolve;
        });
    }
    hide() {
        this.modal.hide();
    }

}